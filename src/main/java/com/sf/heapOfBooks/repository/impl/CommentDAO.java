package com.sf.heapOfBooks.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sf.heapOfBooks.model.Book;
import com.sf.heapOfBooks.model.Comment;
import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.model.enums.BookTypeEnum;
import com.sf.heapOfBooks.model.enums.CommentStatus;
import com.sf.heapOfBooks.model.enums.LetterEnum;
import com.sf.heapOfBooks.model.enums.UserEnum;
import com.sf.heapOfBooks.repository.ICommentDAO;

@Repository
public class CommentDAO implements ICommentDAO{

	private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private JdbcTemplate jdbcTemplate;	
	
	private class CommentCallBackHandler implements RowCallbackHandler{

		Map<Long, Comment> comments = new LinkedHashMap<Long, Comment>();
		
		@Override
		public void processRow(ResultSet rs) throws SQLException {
			int i = 1;
			
			Long commentId = rs.getLong(i++);
			float commentRating = rs.getFloat(i++);
			String commentComment = rs.getString(i++);
			LocalDate commentDate = LocalDate.parse(rs.getString(i++), formatterDate);
			CommentStatus commentStatus = CommentStatus.valueOf(rs.getString(i++));
			
			Comment comment = comments.get(commentId);
			if(comment == null) {
				comment = new Comment();
				comment.setId(commentId);
				comment.setRating(commentRating);
				comment.setComment(commentComment);
				comment.setCommentDate(commentDate);
				comment.setStatus(commentStatus);
				
				comments.put(commentId, comment);
			}
			
			List<String> authors = new ArrayList<String>();

			Long bookId = rs.getLong(i++);
			String bookName = rs.getString(i++);
			String bookPublisher = rs.getString(i++);
			String bookAuthorsLine = rs.getString(i++);
			String[] bookAuthors = bookAuthorsLine.split(",");
			for(String author : bookAuthors) {
				authors.add(author);
			}
			String bookDate = rs.getString(i++);
			String bookDescription = rs.getString(i++);
			String imagePath = rs.getString(i++);
			float bookPrice = rs.getFloat(i++);
			int bookNumberOfPages = rs.getInt(i++);
			BookTypeEnum bookType = BookTypeEnum.valueOf(rs.getString(i++));
			LetterEnum bookLetter = LetterEnum.valueOf(rs.getString(i++));
			String bookLanguage = rs.getString(i++);
			float bookAvgRating = rs.getFloat(i++);
			int bookCopies = rs.getInt(i++);
			
			Book book = new Book(bookName, bookPublisher, authors, LocalDate.parse(bookDate, formatterDate), bookDescription, 
					imagePath, bookPrice, bookNumberOfPages, bookType, bookLetter, bookLanguage, bookCopies);
			book.setISBN(bookId);
			book.setNumberOfCopies(bookCopies);
			book.setAverageRating(bookAvgRating);
			
			Long id = rs.getLong(i++);
			String userName = rs.getString(i++);
			String userPassword = rs.getString(i++);
			String eMail = rs.getString(i++);
			String name = rs.getString(i++);
			String surname = rs.getString(i++);
			String regDateOfBirth = rs.getString(i++);
			LocalDate dateOfBirth = LocalDate.parse(regDateOfBirth, formatterDate);
			String address = rs.getString(i++);
			String phoneNumber = rs.getString(i++);
			String regDate = rs.getString(i++); 
			LocalDateTime registrationDateAndTime = LocalDateTime.parse(regDate, formatterDateTime);
			String uType = rs.getString(i++);
			UserEnum userType = UserEnum.valueOf(uType);
			boolean userBlocked = rs.getBoolean(i++);
			
			User user = new User(userName, userPassword, eMail, name, surname, dateOfBirth, address, phoneNumber, registrationDateAndTime);
			user.setUserType(userType);
			user.setUserBlocked(userBlocked);
			user.setId(id);
			
			comment.setBook(book);
			comment.setUser(user);
		}
		public List<Comment> getComments(){
			return new ArrayList<Comment>(comments.values());
		}
	}
	
	
	@Override
	public Comment findOne(Long id) {
		String sql = "SELECT c.*,b.*,u.* FROM comments AS c"
				+ "	LEFT JOIN commentUserBook cub ON cub.commentId = c.id"
				+ "	LEFT JOIN books b ON cub.bookId = b.id"
				+ "	LEFT JOIN users u ON cub.userId = u.id"
				+ "	WHERE c.id = ?";
		
		CommentCallBackHandler ccbh = new CommentCallBackHandler();
		jdbcTemplate.query(sql, ccbh, id);
		
		if(ccbh.getComments().isEmpty())
			return null;
		
		return ccbh.getComments().get(0);
	}

	@Override
	public List<Comment> findAllOnWaiting() {
		String sql = "SELECT c.*,b.*,u.* FROM comments AS c"
				+ "	LEFT JOIN commentUserBook cub ON cub.commentId = c.id"
				+ "	LEFT JOIN books b ON cub.bookId = b.id"
				+ "	LEFT JOIN users u ON cub.userId = u.id"
				+ "	WHERE c.status = ?";
		
		CommentCallBackHandler ccbh = new CommentCallBackHandler();
		jdbcTemplate.query(sql, ccbh, CommentStatus.Čekanje.toString());
		
		if(ccbh.getComments().isEmpty())
			return null;
		
		return ccbh.getComments();
	}

	@Transactional
	@Override
	public int updateStatus(Comment comment) {
		String sql = "UPDATE comments SET `status` = ? WHERE id = ?";
		
		boolean uspeh = true;
		
		uspeh = uspeh && jdbcTemplate.update(sql,comment.getStatus().toString(),comment.getId()) == 1;
		
		return uspeh?1:0;
	}

	@Transactional
	@Override
	public int create(Comment comment) {
		PreparedStatementCreator preparedStatementCreateor = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "INSERT INTO comments(id,rating,`comment`,commDate, `status`)"
						+ " VALUES (?,?,?,?,?)";
				
				PreparedStatement ps = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				int i = 1;
				
				ps.setLong(i++, comment.getId());
				ps.setFloat(i++, comment.getRating());
				ps.setString(i++, comment.getComment());
				ps.setString(i++, comment.getCommentDate().toString());
				ps.setString(i++, comment.getStatus().toString());

				return ps;
			}
		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreateor, keyHolder) == 1;
		
		if(uspeh) {
			String sql = "INSERT INTO commentUserBook(commentId,userId,bookId) VALUES(?,?,?)";
			uspeh = uspeh && jdbcTemplate.update(sql,keyHolder.getKey(),comment.getUser().getId(),comment.getBook().getISBN()) == 1;
		}
		
		return uspeh?1:0;
	}

	@Override
	public List<Comment> findAllAccepted(Long isbn) {
		String sql = "SELECT c.*,b.*,u.* FROM comments AS c"
				+ "	LEFT JOIN commentUserBook cub ON cub.commentId = c.id"
				+ "	LEFT JOIN books b ON cub.bookId = b.id"
				+ "	LEFT JOIN users u ON cub.userId = u.id"
				+ "	WHERE c.status = ? AND b.id = ?";
		
		CommentCallBackHandler ccbh = new CommentCallBackHandler();
		jdbcTemplate.query(sql, ccbh, CommentStatus.Odobren.toString(), isbn);
		
		if(ccbh.getComments().isEmpty())
			return null;
		
		return ccbh.getComments();
	}

	@Override
	public List<Comment> findAll() {
		String sql = "SELECT c.*,b.*,u.* FROM comments AS c"
				+ "	LEFT JOIN commentUserBook cub ON cub.commentId = c.id"
				+ "	LEFT JOIN books b ON cub.bookId = b.id"
				+ "	LEFT JOIN users u ON cub.userId = u.id";
		
		CommentCallBackHandler ccbh = new CommentCallBackHandler();
		jdbcTemplate.query(sql, ccbh);
		
		if(ccbh.getComments().isEmpty())
			return null;
		
		return ccbh.getComments();
	}

}
