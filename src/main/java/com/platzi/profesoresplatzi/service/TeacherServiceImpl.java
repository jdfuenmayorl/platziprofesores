package com.platzi.profesoresplatzi.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platzi.profesoresplatzi.dao.TeacherDAO;
import com.platzi.profesoresplatzi.model.Teacher;

@Service("teacherService")
@Transactional
public class TeacherServiceImpl implements TeacherService {

	@Autowired
	private TeacherDAO _teacherDao;
	
	@Override
	public void insertarTeacher(Teacher teacher) {
		_teacherDao.insertarTeacher(teacher);
	}

	@Override
	public void eliminarTeacher(Long idTeacher) {
		_teacherDao.eliminarTeacher(idTeacher);
	}

	@Override
	public void actualizarTeacher(Teacher teacher) {
		_teacherDao.actualizarTeacher(teacher);
	}

	@Override
	public Teacher buscarTeacherPorID(Long idTeacher) {
		return _teacherDao.buscarTeacherPorID(idTeacher);
	}

	@Override
	public Teacher buscarTeacherPorNombre(String nombre) {
		return _teacherDao.buscarTeacherPorNombre(nombre);
	}

	@Override
	public List<Teacher> obtenerTodosLosTeacher() {
		return _teacherDao.obtenerTodosLosTeacher();
	}

}
