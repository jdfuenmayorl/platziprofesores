package com.platzi.profesoresplatzi.dao;


/**
 * Implementacion de CouseDAO
 */

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.platzi.profesoresplatzi.model.Course;

@Repository			// Se indica que sera el Reposiroty de nuestra API Rest
@Transactional		// Se indica que todo tiene que ser transaccional
public class CourseDAOImpl extends AbstractSession implements CourseDAO {

	@Override
	public void insertarCurso(Course course) {
		getSession().persist(course);
		
	}

	@Override
	public void borrarCursoPorID(Long idCourse) {
		Course course = buscarCursoPorID(idCourse);
		
		if (course != null) {
			getSession().delete(course);
		}
	}

	@Override
	public void actualizarCurso(Course course) {
		getSession().update(course);
		
	}

	@Override
	public List<Course> mostrarCursos() {
		return getSession().createQuery("from Course").list();
	}

	@Override
	public Course buscarCursoPorID(Long idCourse) {
		return (Course) getSession().get(Course.class, idCourse);
		
	}

	@Override
	public Course buscarCursoPorNombre(String nombre) {
		return (Course) getSession().createQuery("from Course where name =:nombre").setParameter("nombre", nombre).uniqueResult();
	}

	@Override
	public List<Course> buscarCursosPorIdTeacher(Long idTeacher) {
		return (List<Course>) getSession().createQuery("from Course c join c.teacher t where t.idTeacher =:idTeacher").setParameter("idTeacher", idTeacher).list();
	}

	
}
