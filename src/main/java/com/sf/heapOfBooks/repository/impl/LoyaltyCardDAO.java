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

import com.sf.heapOfBooks.model.LoyaltyCard;
import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.model.enums.UserEnum;
import com.sf.heapOfBooks.repository.ILoyaltyCardDAO;

@Repository
public class LoyaltyCardDAO implements ILoyaltyCardDAO{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	private class LoyaltyCardRowCallBackHandler implements RowCallbackHandler{

		private Map<Long, LoyaltyCard> loyaltyCards = new LinkedHashMap<Long, LoyaltyCard>();
		
		@Override
		public void processRow(ResultSet rs) throws SQLException {
			int i = 1;
			
			Long lcId = rs.getLong(i++);
			int discount = rs.getInt(i++);
			int points = rs.getInt(i++);
			boolean status = Boolean.valueOf(rs.getString(i++));
			
			LoyaltyCard lc = loyaltyCards.get(lcId);
			if(lc == null) {
				lc = new LoyaltyCard();
				lc.setId(lcId);
				lc.setDiscount(discount);
				lc.setPoints(points);
				lc.setStatus(status);
				
				loyaltyCards.put(lcId, lc);
			}
			
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
			user.setId(id);
			user.setUserType(userType);
			user.setUserBlocked(userBlocked);
			
			lc.setUser(user);			
		}
		public List<LoyaltyCard> getLoyaltyCards(){
			return new ArrayList<LoyaltyCard>(loyaltyCards.values());
		}
		
	}
		
	@Override
	public LoyaltyCard findOne(Long id) {
		String sql = "SELECT lc.*,u.* FROM loyaltyCard lc"
				+ " LEFT JOIN loyaltyCardUser lcu ON lcu.loyaltyCardId = lc.id"
				+ " LEFT JOIN users u ON lcu.userId = u.id"
				+ " WHERE lc.id = ?";
		LoyaltyCardRowCallBackHandler lcbh = new LoyaltyCardRowCallBackHandler();
		jdbcTemplate.query(sql, lcbh, id);
		
		if(lcbh.getLoyaltyCards().isEmpty())
			return null;
		
		return lcbh.getLoyaltyCards().get(0);
	}

	@Override
	public LoyaltyCard findOneForUser(User user) {
		String sql = "SELECT lc.*,u.* FROM loyaltyCard lc"
				+ " LEFT JOIN loyaltyCardUser lcu ON lcu.loyaltyCardId = lc.id"
				+ " LEFT JOIN users u ON lcu.userId = u.id"
				+ " WHERE u.id = ?";
		LoyaltyCardRowCallBackHandler lcbh = new LoyaltyCardRowCallBackHandler();
		jdbcTemplate.query(sql, lcbh, user.getId());
		
		if(lcbh.getLoyaltyCards().isEmpty())
			return null;
		
		return lcbh.getLoyaltyCards().get(0);
	}

	@Override
	public List<LoyaltyCard> findAllWithStatusFalse() {
		String sql = "SELECT lc.*,u.* FROM loyaltyCard lc"
				+ " LEFT JOIN loyaltyCardUser lcu ON lcu.loyaltyCardId = lc.id"
				+ " LEFT JOIN users u ON lcu.userId = u.id"
				+ "	WHERE lc.status = 'false'";
		LoyaltyCardRowCallBackHandler lcbh = new LoyaltyCardRowCallBackHandler();
		jdbcTemplate.query(sql, lcbh);
		
		if(lcbh.getLoyaltyCards().isEmpty())
			return null;
		
		return lcbh.getLoyaltyCards();
	}

	@Transactional
	@Override
	public int create(LoyaltyCard loyaltyCart) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO loyaltyCard (id, discount, points,`status`) VALUES (?,?,?,?)";

				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int i = 1;
				ps.setLong(i++, loyaltyCart.getId());
				ps.setInt(i++, loyaltyCart.getDiscount());
				ps.setInt(i++, loyaltyCart.getPoints());
				ps.setString(i++, String.valueOf(loyaltyCart.isStatus()));				
				return ps;
			}

		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		if (uspeh) {
			String sql = "INSERT INTO loyaltyCardUser (loyaltyCardId, userId) VALUES (?, ?)";
			uspeh = uspeh && jdbcTemplate.update(sql, keyHolder.getKey(), loyaltyCart.getUser().getId()) == 1;
		}
		return uspeh?1:0;
	}

	@Transactional
	@Override
	public int accept(LoyaltyCard loyaltyCart) {
		boolean uspeh = true;
		String sql = "UPDATE loyaltyCard SET `status` = ? WHERE id = ?";
		
		uspeh = uspeh && jdbcTemplate.update(sql,"true",loyaltyCart.getId()) == 1;
		
		return uspeh?1:0;
	}

	@Transactional
	@Override
	public int reject(LoyaltyCard loyaltyCart) {
		boolean uspeh = true;
		String sql = "DELETE FROM loyaltyCard WHERE id = ?";
		
		uspeh = uspeh && jdbcTemplate.update(sql,loyaltyCart.getId()) == 1;
		
		return uspeh?1:0;
	}

	@Override
	public int updatePoints(LoyaltyCard loyaltyCart) {
		boolean uspeh = true;
		String sql = "UPDATE loyaltyCard SET points = ?, discount = ?  WHERE id = ?";
		
		uspeh = uspeh && jdbcTemplate.update(sql,loyaltyCart.getPoints(),loyaltyCart.getDiscount(),loyaltyCart.getId()) == 1;
		
		return uspeh?1:0;
	}

	@Override
	public List<LoyaltyCard> findAll() {
		String sql = "SELECT lc.*,u.* FROM loyaltyCard lc"
				+ " LEFT JOIN loyaltyCardUser lcu ON lcu.loyaltyCardId = lc.id"
				+ " LEFT JOIN users u ON lcu.userId = u.id";
		LoyaltyCardRowCallBackHandler lcbh = new LoyaltyCardRowCallBackHandler();
		jdbcTemplate.query(sql, lcbh);
		
		if(lcbh.getLoyaltyCards().isEmpty())
			return null;
		
		return lcbh.getLoyaltyCards();
	}

}
