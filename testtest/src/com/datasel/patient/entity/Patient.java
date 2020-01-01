package com.datasel.patient.entity;

import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name= "Patient",propOrder = {"id","name","surname","gender","age","city"})
@XmlRootElement(name="patient")
public class Patient implements Serializable{
	private int id;
	private String name;
	private String surname;
	private String gender;
	private String age;
	private String city;
	
	@XmlTransient
	private boolean dirty = false;
	@XmlTransient
	private boolean selected = false;
	public Patient() {
		super();
	}
	public Patient(int id,String name, String surname,String gender, String city, String age) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.age = age;
		this.gender = gender;
		this.city = city;
	}

	public boolean isDirty() {
		return dirty;
	}
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "Patient [id=" + id + ", name=" + name + ", surname=" + surname
				+ ", gender=" + gender + ", age=" + age + ", city=" + city
				+ ", dirty=" + dirty + ", selected=" + selected + "]";
	}
}
