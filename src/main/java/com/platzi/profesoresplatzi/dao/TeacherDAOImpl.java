package com.platzi.profesoresplatzi.dao;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.platzi.profesoresplatzi.model.Teacher;
import com.platzi.profesoresplatzi.model.TeacherSocialMedia;

/*
 * Esta clase implementa la interfaz teacherDAO para implementar sus metodos y hereda de Sesion el metodo para iniciar sesion
 */


@Repository			// Se indica que sera el Reposiroty de nuestra API Rest
@Transactional		// Se indica que todo tiene que ser transaccional
public class TeacherDAOImpl extends AbstractSession implements TeacherDAO{
	
	public void insertarTeacher(Teacher teacher) {
		getSession().persist(teacher);
	}

	public void eliminarTeacher(Long idTeacher) {
		Teacher teacher = buscarTeacherPorID(idTeacher);
		if(teacher != null) {
			
			Iterator<TeacherSocialMedia> i = teacher.getTeacherSocialMedias().iterator();  // Se agrega un iterator a la coleccon de SocialMedias del profesor
			while (i.hasNext()) {									
				TeacherSocialMedia teacherSocialMedia = i.next();
				i.remove();
				getSession().delete(teacherSocialMedia);
			}
			teacher.getTeacherSocialMedias().clear();										// Luego de la iteracion de eliminar cada SocialMedia debemos limpiar la coleccion de SocialMedias
			getSession().delete(teacher);
		}
	}

	public void actualizarTeacher(Teacher teacher) {
		getSession().update(teacher);
	}

	public Teacher buscarTeacherPorID(Long idTeacher) {
		return (Teacher) getSession().get(Teacher.class, idTeacher);
	}

	public Teacher buscarTeacherPorNombre(String nombre) {
		return (Teacher) getSession().createQuery(
				"from Teacher where name = :nombre").
				setParameter("nombre", nombre).uniqueResult();
	}

	public List<Teacher> obtenerTodosLosTeacher() {
		return getSession().createQuery("from Teacher").list();
	}

}
