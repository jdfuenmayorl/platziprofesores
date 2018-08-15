package com.platzi.profesoresplatzi.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platzi.profesoresplatzi.dao.SocialMediaDAO;
import com.platzi.profesoresplatzi.model.SocialMedia;
import com.platzi.profesoresplatzi.model.TeacherSocialMedia;

@Service("socialMediaService")
@Transactional
public class SocialMediaServiceImpl implements SocialMediaService{

	@Autowired
	private SocialMediaDAO _socialMediaDao;
	
	@Override
	public void insertarSocialMedia(SocialMedia socialMedia) {
		_socialMediaDao.insertarSocialMedia(socialMedia);
		
	}

	@Override
	public void actualizarSocialMedia(SocialMedia socialMedia) {
		_socialMediaDao.actualizarSocialMedia(socialMedia);
	}

	@Override
	public void borrarSocialMediaPorId(Long idSocialMedia) {
		_socialMediaDao.borrarSocialMediaPorId(idSocialMedia);
	}

	@Override
	public SocialMedia buscarSocialMediaPorId(Long idSocialMedia) {
		return _socialMediaDao.buscarSocialMediaPorId(idSocialMedia);
	}

	@Override
	public List<SocialMedia> obtenerSocialMedias() {
		return _socialMediaDao.obtenerSocialMedias();
	}

	@Override
	public SocialMedia buscarPorNombre(String nombre) {
		return _socialMediaDao.buscarPorNombre(nombre);
	}

	@Override
	public TeacherSocialMedia buscarSocialMediaPorNicknameAndId(String nickname, Long idSocialMedia) {
		return _socialMediaDao.buscarSocialMediaPorNicknameAndId(nickname, idSocialMedia);
	}

	@Override
	public TeacherSocialMedia findSocialMediaByIdTeacherAndIdSocialMedia(Long idTeacher, Long idSocialMedia) {
		return _socialMediaDao.findSocialMediaByIdTeacherAndIdSocialMedia(idTeacher, idSocialMedia);
	}

}
