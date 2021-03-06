package com.sf.heapOfBooks.model;

public class Genre {

	private Long id;
	private String name;
	private String description;
	private boolean deleted;
	
	public Genre() {}
	
	public Genre(String name, String description) {
		this.name = name;
		this.description = description;
		this.deleted = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
