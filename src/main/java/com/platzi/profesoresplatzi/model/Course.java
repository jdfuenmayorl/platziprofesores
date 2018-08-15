package com.platzi.profesoresplatzi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity													// Nombrar Clase como entidad en el proyecto
@Table(name="course")									// Nombrar Tabla a la cual se conectara esta clase
public class Course implements Serializable {
	
	@Id													// Se le indica que es un ID
	@Column(name="id_course")							// Se le indica la columna de la bbdd a la que pertenece este atributo
	@GeneratedValue(strategy=GenerationType.IDENTITY)	// Se le indica que va a ser autoincrementable
	private Long idCourse;
	
	@Column(name="name")
	private String name;
	
	@Column(name="themes")
	private String themes;
	
	@Column(name="project")
	private String project;
	
	@ManyToOne(optional=true, fetch=FetchType.EAGER)	// Se le indica a hibernate la relacion Muchos a Uno 
	@JoinColumn(name="id_teacher")						// Se relaciona con Teacher mediante la columna id_teacher
	private Teacher teacher;
	
	
	public Course() {									// Constructor vacio
		super();
		// TODO Auto-generated constructor stub
	}
	public Course(String name, String themes, String project) { //Constructor con los datos necesarios para ingresar un numero Curso
		super();
		this.name = name;
		this.themes = themes;
		this.project = project;
	}
	
	// Getters y Setters de los atributos de la tabla Course
	
	public Long getIdCourse() {
		return idCourse;
	}
	public void setIdCourse(Long idCourse) {
		this.idCourse = idCourse;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getThemes() {
		return themes;
	}
	public void setThemes(String themes) {
		this.themes = themes;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	

}
