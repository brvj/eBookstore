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

import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.model.enums.UserEnum;
import com.sf.heapOfBooks.repository.IUserDAO;

@Repository
public class UserDAO implements IUserDAO{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	private class UserRowCallBackHandler implements RowCallbackHandler{

		private Map<Long, User> users = new LinkedHashMap<Long, User>();
		
		@Override
		public void processRow(ResultSet rs) throws SQLException {
			int i = 1;
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
			
			User user = users.get(id);
			if(user == null) {
				user = new User(userName, userPassword, eMail, name, surname, dateOfBirth, address, phoneNumber, registrationDateAndTime);
				user.setUserType(userType);
				user.setUserBlocked(userBlocked);
				user.setId(id);
				users.put(id, user);
			}			
		}
		
		public List<User> getUsers(){
			return new ArrayList<User>(users.values());
		}
	}
	
	public List<User> findAll(){
		String sql= "SELECT u.id, u.userName, u.userPassword, u.eMail, u.name, u.surname, u.dateOfBirth, u.address,"
				+ " u.phoneNumber, u.registrationDateAndTime, u.userType, u.userBlocked FROM users u";
		UserRowCallBackHandler rcbh = new UserRowCallBackHandler();
		jdbcTemplate.query(sql, rcbh);
		
		return rcbh.getUsers();		
	}
	
	@Override
	public User findOne(Long id) {
		String sql = "SELECT u.id, u.userName, u.userPassword, u.eMail, u.name, u.surname, u.dateOfBirth, u.address,"
				+ " u.phoneNumber, u.registrationDateAndTime, u.userType, u.userBlocked FROM users u"
				+ "WHERE u.id = ?"
				+ "ORDER BY u.id";
		
		UserRowCallBackHandler rcbh = new UserRowCallBackHandler();
		jdbcTemplate.query(sql, rcbh, id);
		
		return rcbh.getUsers().get(0);
	}

	@Override
	public User login(String userName, String userPassword) {
		String sql = "SELECT u.id, u.userName, u.userPassword, u.eMail, u.name, u.surname, u.dateOfBirth, u.address,"
				+ " u.phoneNumber, u.registrationDateAndTime, u.userType, u.userBlocked FROM users u"
				+ "WHERE u.userName = " + userName + " AND u.userPassword = " + userPassword;
		
		UserRowCallBackHandler rcbh = new UserRowCallBackHandler();
		jdbcTemplate.query(sql, rcbh);
		
		return rcbh.getUsers().get(0);
	}

	@Override
	public int createNewUser(User user) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO users (id,userName,userPassword,eMail,name,surname,dateOfBirth,address,phoneNumber,"
						+ "registrationDateAndTime,userType,userBlocked) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int i = 1;
				ps.setLong(i++, user.getId());
				ps.setString(i++, user.getUserName());
				ps.setString(i++, user.getUserPassword());
				ps.setString(i++, user.geteMail());
				ps.setString(i++, user.getName());
				ps.setString(i++, user.getSurname());
				ps.setString(i++, user.getDateOfBirth().toString());
				ps.setString(i++, user.getAddress());
				ps.setString(i++, user.getPhoneNumber());
				ps.setString(i++, user.getRegistrationDateAndTime().toString());
				ps.setString(i++, user.getUserType().toString());
				ps.setString(i++, String.valueOf(user.isUserBlocked()));
					
				return ps;
			}
		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		
		return uspeh?1:0;
	}

	@Override
	public User update(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User delete(User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
