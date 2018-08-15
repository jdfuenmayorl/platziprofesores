package com.platzi.profesoresplatzi.service;

import java.util.List;

import com.platzi.profesoresplatzi.model.Course;

public interface CourseService {

	Course buscarCursoPorID(Long idCourse);
	
	void insertarCurso(Course course);
	
	void borrarCursoPorID(Long idCourse);
	
	void actualizarCurso(Course course);
	
	List<Course> mostrarCursos();
	
	Course buscarCursoPorNombre(String name);
	
	List<Course> buscarCursosPorIdTeacher(Long idTeacher);
}
