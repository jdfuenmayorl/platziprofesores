package com.platzi.profesoresplatzi.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.platzi.profesoresplatzi.model.SocialMedia;
import com.platzi.profesoresplatzi.service.SocialMediaService;
import com.platzi.profesoresplatzi.util.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class SocialMediaController {

	@Autowired										// Inyectar el atributo
	private SocialMediaService _socialMediaService;			// Atributo inyectado, necesario para la comunicacion con los Service
	
	//GET	Este metodo se usa para obtener informacion de la bbdd
	@RequestMapping(value="/socialMedias", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<SocialMedia>> getSocialMedias(@RequestParam(value="name", required=false) String name){
		
		List<SocialMedia> socialMedias = new ArrayList<>();
		
		if (name == null) {
			socialMedias = _socialMediaService.obtenerSocialMedias();
			if (socialMedias.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity<List<SocialMedia>>(socialMedias, HttpStatus.OK);
		} else {
			SocialMedia socialMedia = _socialMediaService.buscarPorNombre(name);
			if (socialMedia == null) {
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			
			socialMedias.add(socialMedia);
			
			return new ResponseEntity<List<SocialMedia>>(socialMedias, HttpStatus.OK);
		}
		
	}
	
	//GET	Este se crea para obtener la SocialMedia con el ID indicado
	@RequestMapping(value="/socialMedias/{id}", method = RequestMethod.GET, headers = "Accept=application/json")	
	public ResponseEntity<SocialMedia> obtenerSocialMediaPorId(@PathVariable("id") Long idSocialMedia){
		if (idSocialMedia == null || idSocialMedia <= 0) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia is required"), HttpStatus.CONFLICT);
		}
		SocialMedia socialMedia = _socialMediaService.buscarSocialMediaPorId(idSocialMedia);
		
		if (socialMedia == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<SocialMedia>(socialMedia, HttpStatus.OK);
	}
	
	
	//POST	Este metodo se usa para crear informacion en la bbdd
	@RequestMapping(value="/socialMedias", method = RequestMethod.POST,headers = "Accept=application/json")		// Se indica la URL a la cual pertenece, el metodo y el default de la solicitud que sera json
	public ResponseEntity<?> createSocialMedia(@RequestBody SocialMedia socialMedia, UriComponentsBuilder uriComponentBuilder){
		
		if (socialMedia.getName().isEmpty() || socialMedia.getName().equals(null)) {		// Se verifica que no venga nulo el campo
			return new ResponseEntity(new CustomErrorType("SocialMedia name is required"), HttpStatus.CONFLICT);
		}
		
		if (_socialMediaService.buscarPorNombre(socialMedia.getName()) != null) {			// Se verifica que no exista el nombre de esa SocialMedia
			return new ResponseEntity(new CustomErrorType("idSocialMedia is required"), HttpStatus.CONFLICT);
		}
		
		_socialMediaService.insertarSocialMedia(socialMedia);								// Se inserta la SocialMedia
		SocialMedia socialMedia2 = _socialMediaService.buscarPorNombre(socialMedia.getName());	// Se solicita para verificar que se inserto
		HttpHeaders header = new HttpHeaders();													// Se crea un header para la respuesta
		header.setLocation(																		// Se setea la url de la socialMedia
				uriComponentBuilder.path("/var1/socialMedias/{id}")
				.buildAndExpand(socialMedia2.getIdSocialMedia())								// Se solicita el ID del al SocialMedia para indicar en la URl
				.toUri()																		// Se transforma lo obtenido en URL
				);
		return new ResponseEntity<String>(header, HttpStatus.CREATED);							// Se envia la respuesta de que fue creado
	}
	
	//UPDATE
	@RequestMapping(value="/socialMedias/{id}", method = RequestMethod.PATCH,headers = "Accept=application/json")
	public ResponseEntity<?> updateSocialMedia(@PathVariable("id") Long idSocialMedia, @RequestBody SocialMedia socialMedia){
		SocialMedia currentSocialMedia = _socialMediaService.buscarSocialMediaPorId(idSocialMedia);
		if (currentSocialMedia == null) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia is required"), HttpStatus.CONFLICT);
		}
		
		currentSocialMedia.setName(socialMedia.getName());
		currentSocialMedia.setIcon(socialMedia.getIcon());
		
		_socialMediaService.actualizarSocialMedia(currentSocialMedia);
		return new ResponseEntity<SocialMedia>(currentSocialMedia, HttpStatus.OK);
	}
	
	//DELETE
	@RequestMapping(value="/socialMedias/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<?> deleteSocialMedia(@PathVariable("id") Long idSocialMedia){
		if (idSocialMedia == null || idSocialMedia <= 0) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia is required"), HttpStatus.CONFLICT);
		}
		SocialMedia socialMedia = _socialMediaService.buscarSocialMediaPorId(idSocialMedia);
		
		if (socialMedia == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		_socialMediaService.borrarSocialMediaPorId(idSocialMedia);
		return new ResponseEntity<SocialMedia>(HttpStatus.OK);
	}
	
	
	public static final String SOCIALMEDIA_UPLOADED_FOLDER = "images/socialMedias/";

	//CREATE SOCIAL MEDIA IMAGE
	@RequestMapping(value="socialMedias/images", method = RequestMethod.POST, headers = ("content-type=multipart/form-data"))
	public ResponseEntity<byte[]> uploadSocialMediaImage(@RequestParam("id") Long idSocialMedia, @RequestParam("file") MultipartFile multipartFile, UriComponentsBuilder uriComponentsBuilder) {
		
		if (idSocialMedia == null || idSocialMedia <= 0) {
			return new ResponseEntity(new CustomErrorType("Error, ingrese un idSocialMedia valido"), HttpStatus.CONFLICT);
		}
		
		if (multipartFile.isEmpty()) {
			return new ResponseEntity(new CustomErrorType("Please select a file to upload"), HttpStatus.CONFLICT);
		}
		
		SocialMedia socialMedia = _socialMediaService.buscarSocialMediaPorId(idSocialMedia);
		
		if (socialMedia == null) {
			return new ResponseEntity(new CustomErrorType("Error, SocialMedia no existe"), HttpStatus.CONFLICT);
		}
		
		if (socialMedia.getIcon().isEmpty() || socialMedia.getIcon() != null) {
			String fileName = socialMedia.getIcon();
			Path path = Paths.get(fileName);
			File f = path.toFile();
			if (f.exists()) {
				f.delete();
			}
		}
		
		try {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd-HH-mm-ss");
			String dateName = dateFormat.format(date);
			
			String fileName = String.valueOf(idSocialMedia) + "-iconSocialMedia-"+ dateName + "." + multipartFile.getContentType().split("/")[1]; // se le da formato a la imagen
			socialMedia.setIcon(SOCIALMEDIA_UPLOADED_FOLDER + fileName);
			
			byte[] bytes = multipartFile.getBytes();
			Path path = Paths.get(SOCIALMEDIA_UPLOADED_FOLDER + fileName);
			Files.write(path, bytes);
			
			_socialMediaService.actualizarSocialMedia(socialMedia);
			
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error during upload: " + multipartFile.getOriginalFilename()), HttpStatus.CONFLICT);
		}
		
		
	}
	
}
