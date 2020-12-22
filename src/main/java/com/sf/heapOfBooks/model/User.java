package com.sf.heapOfBooks.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sf.heapOfBooks.model.enums.UserEnum;

public class User {
	
	private Long id;
	private String userName;
	private String userPassword;
	private String eMail;
	private String name;
	private String surname;
	private LocalDate dateOfBirth;
	private String address;
	private String phoneNumber;
	private LocalDateTime registrationDateAndTime;
	private UserEnum userType;
	private boolean userBlocked;
	
	public User() {}
	
	public User(String userName, String userPassword, String eMail, String name, String surname, LocalDate dateOfBirth,
						String address, String phoneNumber, LocalDateTime registrationDateAndTime) {
		this.userName = userName;
		this.userPassword = userPassword;
		this.eMail = eMail;
		this.name = name;
		this.surname = surname;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.registrationDateAndTime = registrationDateAndTime;
		this.userType = UserEnum.Kupac;
		this.userBlocked = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
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

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public LocalDateTime getRegistrationDateAndTime() {
		return registrationDateAndTime;
	}

	public void setRegistrationDateAndTime(LocalDateTime registrationDateAndTime) {
		this.registrationDateAndTime = registrationDateAndTime;
	}

	public UserEnum getUserType() {
		return userType;
	}

	public void setUserType(UserEnum userType) {
		this.userType = userType;
	}

	public boolean isUserBlocked() {
		return userBlocked;
	}

	public void setUserBlocked(boolean userBlocked) {
		this.userBlocked = userBlocked;
	}
}
