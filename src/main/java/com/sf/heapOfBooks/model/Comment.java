package com.sf.heapOfBooks.model;

import java.time.LocalDate;

import com.sf.heapOfBooks.model.enums.CommentStatus;

public class Comment {

	private long id;
	private float rating;
	private LocalDate commentDate;
	private User user;
	private Book book;
	private CommentStatus status;
	
	public Comment() {}
	
	public Comment(float rating, LocalDate commentDate, User user, Book book) {
		this.rating = rating;
		this.commentDate = commentDate;
		this.user = user;
		this.book = book;
		this.status = CommentStatus.Čekanje;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public LocalDate getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(LocalDate commentDate) {
		this.commentDate = commentDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public CommentStatus getStatus() {
		return status;
	}

	public void setStatus(CommentStatus status) {
		this.status = status;
	}
}
