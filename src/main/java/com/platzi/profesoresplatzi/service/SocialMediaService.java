package com.platzi.profesoresplatzi.service;

import java.util.List;

import com.platzi.profesoresplatzi.model.SocialMedia;
import com.platzi.profesoresplatzi.model.TeacherSocialMedia;

public interface SocialMediaService {

	void insertarSocialMedia(SocialMedia socialMedia);
	
	void actualizarSocialMedia(SocialMedia socialMedia);
	
	void borrarSocialMediaPorId(Long idSocialMedia);
	
	SocialMedia buscarSocialMediaPorId(Long idSocialMedia);
	
	List<SocialMedia> obtenerSocialMedias();
	
	SocialMedia buscarPorNombre(String nombre);
	
	TeacherSocialMedia buscarSocialMediaPorNicknameAndId(String nickname, Long idSocialMedia);
	
	TeacherSocialMedia findSocialMediaByIdTeacherAndIdSocialMedia(Long idTeacher, Long idSocialMedia);
}
