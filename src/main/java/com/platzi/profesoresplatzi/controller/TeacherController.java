package com.platzi.profesoresplatzi.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.platzi.profesoresplatzi.model.SocialMedia;
import com.platzi.profesoresplatzi.model.Teacher;
import com.platzi.profesoresplatzi.model.TeacherSocialMedia;
import com.platzi.profesoresplatzi.service.SocialMediaService;
import com.platzi.profesoresplatzi.service.TeacherService;
import com.platzi.profesoresplatzi.util.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class TeacherController {

	@Autowired
	private TeacherService _teacherService;
	
	@Autowired
	private SocialMediaService _socialMediaService;
	
	//GET
	@RequestMapping(value="/teachers", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Teacher>> getTeachers(@RequestParam(value="name", required=false) String name){
		List<Teacher> teachers = new ArrayList<>();
		
		if (name == null) {
			teachers = _teacherService.obtenerTodosLosTeacher();
			if (teachers.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Teacher>>(teachers, HttpStatus.OK);
		} else {
			Teacher teacher = _teacherService.buscarTeacherPorNombre(name);
			if (teacher == null) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			teachers.add(teacher);
			return new ResponseEntity<List<Teacher>>(teachers, HttpStatus.OK);
		}
	}
	
	//GET BY ID
	@RequestMapping(value="/teachers/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Teacher> getTeacherById(@PathVariable("id") Long idTeacher){
		
		if (idTeacher == null || idTeacher <= 0) {
			return new ResponseEntity(new CustomErrorType("Error, idTeacher is required"), HttpStatus.CONFLICT);
		}
		
		Teacher teacher = _teacherService.buscarTeacherPorID(idTeacher);
		
		if (teacher == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<Teacher>(teacher, HttpStatus.OK);
	}
	
	
	//CREATE TEACHER
	@RequestMapping(value="/teachers", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> createTeacher(@RequestBody Teacher teacher, UriComponentsBuilder uriComponentBuilder){
		
		if (teacher.getName().equals(null) || teacher.getName().isEmpty()) {
			return new ResponseEntity(new CustomErrorType("Error, Teacher is required"), HttpStatus.CONFLICT);
		}
		
		if (_teacherService.buscarTeacherPorNombre(teacher.getName()) != null) {
			return new ResponseEntity(new CustomErrorType("Error, Teacher exist"), HttpStatus.CONFLICT);
		}
		
		_teacherService.insertarTeacher(teacher);
		
		Teacher teacher2 = _teacherService.buscarTeacherPorNombre(teacher.getName());
		HttpHeaders header = new HttpHeaders();
		header.setLocation(
				uriComponentBuilder.path("/var1/teachers/{id}")
				.buildAndExpand(teacher2.getIdTeacher())
				.toUri()
				);
		
		return new ResponseEntity<String>(header, HttpStatus.CREATED);
	}
	
	//UPDATE
	@RequestMapping(value="teachers/{id}", method = RequestMethod.PATCH, headers = "Accept=application/json")
	public ResponseEntity<?> updateTeacherById(@PathVariable("id") Long idTeacher, @RequestBody Teacher teacher) {
		if (idTeacher == null || idTeacher <= 0) {
			return new ResponseEntity(new CustomErrorType("Error, invalid idTeacher"), HttpStatus.CONFLICT);
		}
		
		Teacher currentTeacher = _teacherService.buscarTeacherPorID(idTeacher);
		
		if (currentTeacher == null) {
			return new ResponseEntity(new CustomErrorType("Error, Teacher does not exist"), HttpStatus.NOT_FOUND);
		}
		
		currentTeacher.setName(teacher.getName());
		
		_teacherService.actualizarTeacher(currentTeacher);
		
		return new ResponseEntity<Teacher>(currentTeacher, HttpStatus.OK);
	}
	
	
	//DELETE
	@RequestMapping(value="/teachers/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<?> deleteTeacher (@PathVariable("id") Long idTeacher){
		
		if (idTeacher == null || idTeacher <= 0) {
			return new ResponseEntity(new CustomErrorType("Error, idTeacher is required"), HttpStatus.CONFLICT);
		}
		
		Teacher teacher = _teacherService.buscarTeacherPorID(idTeacher);
		
		if (teacher == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		_teacherService.eliminarTeacher(idTeacher);
		return new ResponseEntity<Teacher>(HttpStatus.OK);
	}
	
	public static final String TEACHER_UPLOADED_FOLDER = "images/teachers/";
	
	//CREATE TEACHER IMAGE
	@RequestMapping(value="/teachers/images", method = RequestMethod.POST, headers = ("content-type=multipart/form-data")) // se cambia el header porque no recibiremos un json
	public ResponseEntity<byte[]> uploadTeacherImage(@RequestParam("id") Long idTeacher, @RequestParam("file") MultipartFile multipartFile, UriComponentsBuilder uriComponentsBuilder){
		
		if (idTeacher == null || idTeacher <= 0) {
			return new ResponseEntity(new CustomErrorType("Please set idTeacher"), HttpStatus.CONFLICT);
		}
		
		if (multipartFile.isEmpty()) {
			return new ResponseEntity(new CustomErrorType("please select a file to upload"), HttpStatus.CONFLICT);
		}
		
		Teacher teacher = _teacherService.buscarTeacherPorID(idTeacher);
		
		if (teacher == null) {
			return new ResponseEntity(new CustomErrorType("Teacher with idTeacher " + idTeacher + "not found"), HttpStatus.NOT_FOUND);
		}
		
		if (teacher.getAvatar().isEmpty() || teacher.getAvatar() != null) {
			String fileName = teacher.getAvatar();
			Path path = Paths.get(fileName);
			File f = path.toFile();
			if (f.exists()) {
				f.delete();
			}
		}
		
		try {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String dateName = dateFormat.format(date);
			
			String fileName = String.valueOf(idTeacher) + "-pictureTeacher-"+ dateName + "." + multipartFile.getContentType().split("/")[1]; // se le da formato a la imagen
			teacher.setAvatar(TEACHER_UPLOADED_FOLDER + fileName);
			
			byte[] bytes = multipartFile.getBytes();
			Path path = Paths.get(TEACHER_UPLOADED_FOLDER + fileName);
			Files.write(path, bytes);
			
			_teacherService.actualizarTeacher(teacher);
			
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error during upload: " + multipartFile.getOriginalFilename()), HttpStatus.CONFLICT);
		}
	}
	
	//GET IMAGE
	@RequestMapping(value="/teachers/{id}/images", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getTeacherImage(@PathVariable("id") Long idTeacher){
		if (idTeacher == null || idTeacher <= 0) {
			return new ResponseEntity(new CustomErrorType("Please set idTeacher"), HttpStatus.CONFLICT);
		}
		
		Teacher teacher = _teacherService.buscarTeacherPorID(idTeacher);
		if (teacher == null) {
			return new ResponseEntity(new CustomErrorType("Teacher with idTeacher: " + idTeacher + " not found"), HttpStatus.NOT_FOUND);
		}
		
		try {
			
			String fileName = teacher.getAvatar();
			Path path = Paths.get(fileName);
			File f = path.toFile();
			if (!f.exists()) {
				return new ResponseEntity(new CustomErrorType("Image not found"), HttpStatus.CONFLICT);
			}
			
			byte[] image = Files.readAllBytes(path);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
			
		} catch (Exception e) {
			return new ResponseEntity(new CustomErrorType("Error to show image"), HttpStatus.CONFLICT);
		}
	}
	
	//DELETE IMAGE
	@RequestMapping(value="/teachers/{id}/images", method = RequestMethod.DELETE, headers="Accept=application/json")
	public ResponseEntity<?> deleteTeacherImage(@PathVariable("id") Long idTeacher) {
		if (idTeacher == null || idTeacher <= 0) {
			return new ResponseEntity(new CustomErrorType("Please set idTeacher"), HttpStatus.CONFLICT);
		}
		
		Teacher teacher = _teacherService.buscarTeacherPorID(idTeacher);
		if (teacher == null) {
			return new ResponseEntity(new CustomErrorType("Teacher with idTeacher: " + idTeacher + " not found"), HttpStatus.NOT_FOUND);
		}
		
		if (teacher.getAvatar().isEmpty() || teacher.getAvatar() == null) {
			return new ResponseEntity(new CustomErrorType("This teacher doesn't have image selected"), HttpStatus.CONFLICT);
		}
		
		String fileName = teacher.getAvatar();
		Path path = Paths.get(fileName);
		File f = path.toFile();
		if (f.exists()) {
			f.delete();
		}
		
		teacher.setAvatar("");
		_teacherService.actualizarTeacher(teacher);
		
		return new ResponseEntity<Teacher>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/teachers/socialMedias", method = RequestMethod.PATCH, headers = "Accept=application/json")
	public ResponseEntity<?> assignTeacherSocialMedias(@RequestBody Teacher teacher, UriComponentsBuilder uriComponentsBuilder) {
		if (teacher.getIdTeacher() == null) {
			return new ResponseEntity(new CustomErrorType("We need almost idTeacher, idSocialMedia and nickname"), HttpStatus.CONFLICT);
		}
		
		Teacher teacherSaved = _teacherService.buscarTeacherPorID(teacher.getIdTeacher());
		
		if (teacherSaved == null) {
			return new ResponseEntity(new CustomErrorType("The idTeacher: " + teacher.getIdTeacher() + " not found"), HttpStatus.NOT_FOUND);
		}
		
		if (teacher.getTeacherSocialMedias().size() <= 0) {
			return new ResponseEntity(new CustomErrorType("We need almost idTeacher, idSocialMedia and nickname"), HttpStatus.CONFLICT);
		} else {
			Iterator<TeacherSocialMedia> i = teacher.getTeacherSocialMedias().iterator();
			while (i.hasNext()) {
				TeacherSocialMedia teacherSocialMedia = i.next();
				if (teacherSocialMedia.getSocialMedia().getIdSocialMedia() == null || teacherSocialMedia.getNickname() == null) {
					return new ResponseEntity(new CustomErrorType("We need almost idTeacher, idSocialMedia and nickname"), HttpStatus.CONFLICT);
				} else {
					TeacherSocialMedia tsmAux = _socialMediaService.buscarSocialMediaPorNicknameAndId(teacherSocialMedia.getNickname(), teacherSocialMedia.getSocialMedia().getIdSocialMedia());
				
					if (tsmAux != null) {
						return new ResponseEntity(new CustomErrorType("The social media " + teacherSocialMedia.getSocialMedia().getIdSocialMedia() + " with nickname " + teacherSocialMedia.getNickname() + " already exists"), HttpStatus.NO_CONTENT);
					}
					
					SocialMedia socialMedia = _socialMediaService.buscarSocialMediaPorId(teacherSocialMedia.getSocialMedia().getIdSocialMedia());
					
					if (socialMedia == null) {
						return new ResponseEntity(new CustomErrorType("The idSocialMedia: " + teacherSocialMedia.getSocialMedia().getIdSocialMedia() + " not found"), HttpStatus.NOT_FOUND);
					}
					
					teacherSocialMedia.setSocialMedia(socialMedia);
					teacherSocialMedia.setTeacher(teacherSaved);
					
					if (tsmAux == null) {
						teacherSaved.getTeacherSocialMedias().add(teacherSocialMedia);
					} else {
						LinkedList<TeacherSocialMedia> teacherSocialMedias = new LinkedList<>();
						teacherSocialMedias.addAll(teacherSaved.getTeacherSocialMedias());
						for (int j = 0; j < teacherSocialMedias.size(); j++) {
							TeacherSocialMedia teacherSocialMedia2 = teacherSocialMedias.get(j);
							if (teacherSocialMedia.getTeacher().getIdTeacher() == teacherSocialMedia2.getTeacher().getIdTeacher()
									&& teacherSocialMedia.getSocialMedia().getIdSocialMedia() == teacherSocialMedia2.getSocialMedia().getIdSocialMedia()) {
								teacherSocialMedia2.setNickname(teacherSocialMedia.getNickname());
								teacherSocialMedias.set(j, teacherSocialMedia2);							
							} else {
								teacherSocialMedias.set(j, teacherSocialMedia2);
							}
						}
						
						teacherSaved.getTeacherSocialMedias().clear();
						teacherSaved.getTeacherSocialMedias().addAll(teacherSocialMedias);
						
					}
				}
			}
		}
		
		_teacherService.actualizarTeacher(teacherSaved);
		return new ResponseEntity<Teacher>(teacherSaved, HttpStatus.OK);
	}
	
	
}





