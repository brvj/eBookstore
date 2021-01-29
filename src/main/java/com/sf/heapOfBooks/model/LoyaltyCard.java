package com.sf.heapOfBooks.model;

public class LoyaltyCard {

	private Long id;
	private int discount;
	private int points;
	private User user;
	private boolean status;
	
	public LoyaltyCard() {}
	
	public LoyaltyCard(User user) {		
		this.points = 4;
		this.discount = this.points * 5;
		this.user = user;
		this.status = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
