package com.platzi.profesoresplatzi.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.platzi.profesoresplatzi.model.SocialMedia;
import com.platzi.profesoresplatzi.model.TeacherSocialMedia;

@Repository			// Se indica que sera el Reposiroty de nuestra API Rest
@Transactional		// Se indica que todo tiene que ser transaccional
public class SocialMediaDAOImpl extends AbstractSession implements SocialMediaDAO{

	@Override
	public void insertarSocialMedia(SocialMedia socialMedia) {
		getSession().persist(socialMedia);
		
	}

	@Override
	public void actualizarSocialMedia(SocialMedia socialMedia) {
		getSession().update(socialMedia);
		
	}

	@Override
	public void borrarSocialMediaPorId(Long idSocialMedia) {
		SocialMedia socialMedia = buscarSocialMediaPorId(idSocialMedia);
		if (socialMedia != null) {
			getSession().delete(socialMedia);
		}			
	}

	@Override
	public SocialMedia buscarSocialMediaPorId(Long idSocialMedia) {
		return (SocialMedia) getSession().get(SocialMedia.class, idSocialMedia);
	}

	@Override
	public List<SocialMedia> obtenerSocialMedias() {
		return getSession().createQuery("from SocialMedia").list();
	}

	@Override
	public SocialMedia buscarPorNombre(String nombre) {
		return (SocialMedia) getSession().createQuery("from SocialMedia where name=:nombre").setParameter("nombre", nombre).uniqueResult();
	}

	@Override
	public TeacherSocialMedia buscarSocialMediaPorNicknameAndId(String nickname, Long idSocialMedia) {
		List<Object[]> objects = getSession().createQuery(													// Por ser una tabla de llaves nos devuelve un array de objetos en la cual buscaremos lo que necesitamos
				"from TeacherSocialMedia tsm join tsm.socialMedia sm "	
				+ "where sm.idSocialMedia =:idSocialMedia and tsm.nickname =:nickname")
				.setParameter("isSocialMedia", idSocialMedia)
				.setParameter("nickname", nickname).list();
		
		if (objects.size() > 0) {
			for (Object[] objects2 : objects) {								// Se hacen dos foreach porque es un arreglo de arreglos
					for (Object objects3 : objects2) {
						if (objects3 instanceof TeacherSocialMedia) {		// Se verifica si el objeto es una Instancia de TeacherSocialMedia porque estos vienen revueltos
							return (TeacherSocialMedia) objects3;
					}
				}
			}
		}
		return null;
	}

	@Override
	public TeacherSocialMedia findSocialMediaByIdTeacherAndIdSocialMedia(Long idTeacher, Long idSocialMedia) {
		List<Object[]> objects = getSession().createQuery(
				"from TeacherSocialMedia tsm join tsm.socialMedia sm "
				+ "join tsm.teacher t where sm.idSocialMedia = :id_social_media "
				+ "and t.idTeacher = :id_teacher")
				.setParameter("id_social_media", idSocialMedia)
				.setParameter("id_teacher", idTeacher).list();
		
		if (objects.size() > 0) {
			for (Object[] objects2 : objects) {
				for (Object objects3 : objects2) {
					if (objects3 instanceof TeacherSocialMedia) {
						return (TeacherSocialMedia) objects3;
					}
				}
			}
		}
		
		return null;
	}

	
}
