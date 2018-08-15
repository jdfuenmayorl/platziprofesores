package com.platzi.profesoresplatzi.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platzi.profesoresplatzi.dao.CourseDAO;
import com.platzi.profesoresplatzi.model.Course;

@Service("courseService")
@Transactional
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseDAO _courseDao;					// Cuando se llame este objeto traera todo lo que esta en la clase CourseDAOImpl
	
	@Override
	public Course buscarCursoPorID(Long idCourse) {
		return _courseDao.buscarCursoPorID(idCourse);
	}

	@Override
	public void insertarCurso(Course course) {
		_courseDao.insertarCurso(course);
	}

	@Override
	public void borrarCursoPorID(Long idCourse) {
		_courseDao.borrarCursoPorID(idCourse);
	}

	@Override
	public void actualizarCurso(Course course) {
		_courseDao.actualizarCurso(course);
		
	}

	@Override
	public List<Course> mostrarCursos() {
		return _courseDao.mostrarCursos();
	}

	@Override
	public Course buscarCursoPorNombre(String name) {
		return _courseDao.buscarCursoPorNombre(name);
	}

	@Override
	public List<Course> buscarCursosPorIdTeacher(Long idTeacher) {
		return _courseDao.buscarCursosPorIdTeacher(idTeacher);
	}

}
