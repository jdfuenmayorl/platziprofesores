package com.platzi.profesoresplatzi.service;

import java.util.List;

import com.platzi.profesoresplatzi.model.Teacher;

public interface TeacherService {

	void insertarTeacher(Teacher teacher);				// Metodo para insertar un teacher
	
	void eliminarTeacher(Long idTeacher);				// Metodo para eliminar un teacher por si ID
	
	void actualizarTeacher(Teacher teacher);			// Metodo para actualizar un teacher obteniendo un Objeto Teacher
	
	Teacher buscarTeacherPorID(Long idTeacher);			// Metodo para buscar teacher por ID
	
	Teacher buscarTeacherPorNombre(String nombre);		// Metodo para buscar teacher por nombre
	
	List<Teacher> obtenerTodosLosTeacher();				// Metodo para obtener la lista de Teachers
	
}
