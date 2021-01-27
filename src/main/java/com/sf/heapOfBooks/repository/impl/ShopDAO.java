package com.sf.heapOfBooks.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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

import com.sf.heapOfBooks.model.Shop;
import com.sf.heapOfBooks.model.ShoppingCart;
import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.repository.IShopDAO;

@Repository
public class ShopDAO implements IShopDAO{

	private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Autowired
	private JdbcTemplate jdbcTemplate;	
	
	@Autowired
	private ShoppingCartDAO shoppingCartRepository;
	
	private class ShopCallBackHandler implements RowCallbackHandler{

		private Map<Long, Shop> shops = new LinkedHashMap<Long, Shop>();
		
		@Override
		public void processRow(ResultSet rs) throws SQLException {
			
			int i = 1;
			
			Long shopId = rs.getLong(i++);
			float shopPrice = rs.getFloat(i++);
			LocalDate shoppingDate = LocalDate.parse(rs.getString(i++), formatterDate);
			int bookSum = rs.getInt(i++);
			
			Shop shop = shops.get(shopId);
			if(shop == null) {
				shop = new Shop();
				shop.setId(shopId);
				shop.setPrice(shopPrice);
				shop.setShoppingDate(shoppingDate);
				shop.setBookSum(bookSum);
			}
			
			Long scId = rs.getLong(i++);
			//Long userId = rs.getLong(i++);
			ShoppingCart sc = shoppingCartRepository.findOne(scId);
			
			shop.getBoughtBooks().add(sc);			
		}
		public List<Shop> getShop(){
			return new ArrayList<Shop>(shops.values());
		}		
	}

	@Override
	public List<Shop> findAllForUser(User user) {
		String sql = "SELECT s.* FROM shop s"
				+ "	WHERE s.userId = "+ user.getId() +"";

		ShopCallBackHandler sccbh = new ShopCallBackHandler();
		jdbcTemplate.query(sql, sccbh);
		
		if(sccbh.getShop().isEmpty())
			return null;
		
		return sccbh.getShop();		
	}

	@Override
	public List<Shop> findAll() {
		String sql = "SELECT s.* FROM shop s"
				+ "	ORDER BY s.id";

		ShopCallBackHandler sccbh = new ShopCallBackHandler();
		jdbcTemplate.query(sql, sccbh);
		
		if(sccbh.getShop().isEmpty())
			return null;
		
		return sccbh.getShop();	
	}

	@Transactional
	@Override
	public int create(Shop shop) {
		PreparedStatementCreator prepraredStatementCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "INSERT INTO shop (id, price, shoppingDate, bookSum, shoppingCartId, userId) "
						+ "	VALUES (?, ?, ?, ?, ?, ?)";
				
				for(ShoppingCart sp : shop.getBoughtBooks()) {
					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					int i = 1;
					ps.setLong(i++, shop.getId());
					ps.setFloat(i++, shop.getPrice());
					ps.setString(i++, shop.getShoppingDate().toString());
					ps.setInt(i++, shop.getBookSum());
					ps.setLong(i++, sp.getId());
					ps.setLong(i++, sp.getUser().getId());
					
					return ps;
				}
			}
		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(prepraredStatementCreator, keyHolder) == 1;
		if(uspeh) {
			String sql = "INSERT INTO shoppingCartUserBook (cartId, bookId, userId) VALUES (?, ?, ?)";
			uspeh = uspeh && jdbcTemplate.update(sql,keyHolder.getKey(),shoppingCart.getBook().getISBN(),shoppingCart.getUser().getId()) == 1;
		}
		return uspeh?1:0;
	}

}
