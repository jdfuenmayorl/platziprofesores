package com.platzi.profesoresplatzi.configuration;



import org.springframework.beans.factory.annotation.Autowired;

/**
 * estamos configurando spring como el archivo config de hibernate
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

import javax.sql.DataSource;

@Configuration									// Indica que es un archivo de configuracion
@EnableTransactionManagement					// Indica que todas las acciones que hagamos se ejecuten de manera transaccional, se hacen o no
public class DataBaseConfiguration {

	@Bean										// Me indica contra que clase se instancia
	public LocalSessionFactoryBean sessionFactory(){
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource());
		sessionFactoryBean.setPackagesToScan("com.platzi.profesoresplatzi.model");		// Aqui se traen todos los paquetes que tienen nuestras clases con las anotaciones de Hibernate
		sessionFactoryBean.setHibernateProperties(hibernateProperties());
		return sessionFactoryBean;
	}
	
	@Bean										// Los Beans los declaramos como objetos persistentes
	public DataSource dataSource() {			// En este metodo configuramos la conexion a la base de datos
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/platziprofesores");
		dataSource.setUsername("platziprofesores");
		dataSource.setPassword("platziprofesores");
		
		return dataSource;
	}
	
	public Properties hibernateProperties() {	// Se crea este objeto para configurar las propiedades de Hibernate como en el xml
		Properties properties = new Properties();
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		properties.put("show_sql", "true");
		return properties;
	}
	
	@Bean
	@Autowired									// Esta anotacion se coloca cuando se usa un metodo que es un Bean  dentro de otro Bean, en este caso sessionFactory()
	public HibernateTransactionManager transactionManager() {		// Este metodo configurara para que podamos trabajar con transacciones
		HibernateTransactionManager hibernateTransactionManager =  new HibernateTransactionManager();
		hibernateTransactionManager.setSessionFactory(sessionFactory().getObject());
		return hibernateTransactionManager;
	}
	
}
