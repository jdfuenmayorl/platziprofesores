package com.platzi.profesoresplatzi.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSession {

	@Autowired
	private SessionFactory sessionFactory;
	
	protected Session getSession() {				// Se crea el objeto Session que sera necesario para comentar la conexion a bbdd
		return sessionFactory.getCurrentSession();
	}
}
