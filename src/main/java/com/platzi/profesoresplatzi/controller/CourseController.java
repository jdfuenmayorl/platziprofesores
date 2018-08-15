package com.platzi.profesoresplatzi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.platzi.profesoresplatzi.model.Course;
import com.platzi.profesoresplatzi.service.CourseService;
import com.platzi.profesoresplatzi.util.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class CourseController {

	@Autowired
	private CourseService _courseService;
	
	//GET
	@RequestMapping(value="/courses", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Course>> getCourses(@RequestParam(value="name", required=false) String name, @RequestParam(value="idTeacher", required=false) Long idTeacher){
		
		List<Course> courses = new ArrayList<>();
		
		if (name != null) {
			Course course = _courseService.buscarCursoPorNombre(name);
			if (course == null) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			courses.add(course);
		}
		
		if (idTeacher != null) {
			courses = _courseService.buscarCursosPorIdTeacher(idTeacher);
			if (courses == null || courses.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
		}
		
		if (name == null && idTeacher == null) {
			courses = _courseService.mostrarCursos();
			if (courses.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
		}
		
		return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);
	}
	
	//GET
	@RequestMapping(value="/courses/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Course> getCourseById(@PathVariable("id") Long idCourse){
		if (idCourse == null || idCourse <= 0) {
			return new ResponseEntity(new CustomErrorType("Error, idCourse is required"), HttpStatus.CONFLICT);
		}
		
		Course course = _courseService.buscarCursoPorID(idCourse);
		
		if (course == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<Course>(course, HttpStatus.OK);
	}
	
	//POST
	@RequestMapping(value="/courses", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> createCourse(@RequestBody Course course, UriComponentsBuilder uriComponentBuilder) {
		
		if (course.getName().isEmpty() || course.equals(null)) {
			return new ResponseEntity(new CustomErrorType("Error, Course is required"), HttpStatus.CONFLICT);
		}
		
		_courseService.insertarCurso(course);
		
		Course course2 = _courseService.buscarCursoPorNombre(course.getName());
		HttpHeaders header = new HttpHeaders();
		header.setLocation(
				uriComponentBuilder.path("var1/courses/{id}")
				.buildAndExpand(course2.getIdCourse())
				.toUri()
				);
		return new ResponseEntity<String>(header, HttpStatus.CREATED);
	}
	
	//UPDATE
	@RequestMapping(value="/courses/{id}", method = RequestMethod.PATCH, headers = "Accept=application/json")
	public ResponseEntity<?> updateCourse(@PathVariable("id") Long idCourse, @RequestBody Course course){
		
		Course currentCourse = _courseService.buscarCursoPorID(idCourse);
		
		if (currentCourse.equals(null)) {
			return new ResponseEntity(new CustomErrorType("Error, idCourse required"), HttpStatus.CONFLICT);
		}
		
		currentCourse.setName(course.getName());
		currentCourse.setProject(course.getProject());
		currentCourse.setThemes(course.getThemes());
		currentCourse.setTeacher(course.getTeacher());
		
		_courseService.actualizarCurso(currentCourse);
		
		return new ResponseEntity<Course>(currentCourse, HttpStatus.OK);
	}

	//DELETE
	@RequestMapping(value="/courses/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<?> deleteCourse(@PathVariable("id") Long idCourse){
		if (idCourse == null || idCourse <= 0) {
			return new ResponseEntity(new CustomErrorType("Error, idCourse is required"), HttpStatus.CONFLICT);
		}
		
		Course course = _courseService.buscarCursoPorID(idCourse);
		
		if (course == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		_courseService.borrarCursoPorID(idCourse);
		
		return new ResponseEntity<Course>(HttpStatus.OK);
	}
}
