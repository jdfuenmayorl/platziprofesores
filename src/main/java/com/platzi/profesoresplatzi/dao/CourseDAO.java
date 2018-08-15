package com.platzi.profesoresplatzi.dao;

import java.util.List;

import com.platzi.profesoresplatzi.model.Course;
import com.platzi.profesoresplatzi.model.Teacher;

/**
 * DAO de Course con los metodos CRUD
 * @author Jose Fuenmayor
 *
 */

public interface CourseDAO {
	
	Course buscarCursoPorID(Long idCourse);
	
	void insertarCurso(Course course);
	
	void borrarCursoPorID(Long idCourse);
	
	void actualizarCurso(Course course);
	
	List<Course> mostrarCursos();
	
	Course buscarCursoPorNombre(String name);
	
	List<Course> buscarCursosPorIdTeacher(Long idTeacher);
}
