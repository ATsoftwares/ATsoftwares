package com.lp.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RegistrationModel {

	private static final long serialVersionUID = 1L;

	public RegistrationModel() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "country", nullable = false)
	private String country;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "phone", nullable = false, unique = true)
	private String phone;
}
