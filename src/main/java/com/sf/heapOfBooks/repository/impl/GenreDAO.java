package com.sf.heapOfBooks.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

import com.sf.heapOfBooks.model.Genre;
import com.sf.heapOfBooks.repository.IGenreDAO;

@Repository
public class GenreDAO implements IGenreDAO{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private class GenreRowCallBackHandler implements RowCallbackHandler{

		private Map<Long, Genre> genres = new LinkedHashMap<Long, Genre>();
		
		@Override
		public void processRow(ResultSet rs) throws SQLException {
			int i = 1;
			Long id = rs.getLong(i++);
			String name = rs.getString(i++);
			String description = rs.getString(i++);
			boolean deleted = rs.getBoolean(i++);
			
			Genre genre = genres.get(id);
			if(genre == null) {
				genre = new Genre(name, description);
				genre.setId(id);
				genre.setDeleted(deleted);
				genres.put(genre.getId(), genre);
			}
		}
		
		public List<Genre> getGenres(){
			return new ArrayList<Genre>(genres.values());
		}		
	}
	
	@Override
	public List<Genre> findAll() {
		String sql = "SELECT g.id, g.name, g.description, g.deleted from genres as g";
		GenreRowCallBackHandler rcbh = new GenreRowCallBackHandler();
		jdbcTemplate.query(sql, rcbh);
		return rcbh.getGenres();
	}
	
	@Override
	public Genre findOne(Long id) {
		String sql = "SELECT g.id, g.name, g.description, g.deleted from genres as g"
				+ "		WHERE g.id = ?"
				+ "			ORDER BY g.id";
		GenreRowCallBackHandler rcbh = new GenreRowCallBackHandler();
		jdbcTemplate.query(sql, rcbh,id);
		
		return rcbh.getGenres().get(0);
	}

	@Override
	public Genre findByName(String name) {
		String sql = "SELECT g.id, g.name, g.description, g.deleted from genres as g"
				+ "		WHERE g.name = ?"
				+ "			ORDER BY g.name";
		GenreRowCallBackHandler rcbh = new GenreRowCallBackHandler();
		jdbcTemplate.query(sql, rcbh, name);
		
		if(rcbh.getGenres().isEmpty()) {
			return null;
		}
		
		return rcbh.getGenres().get(0);
	}

	@Transactional
	@Override
	public int create(Genre genre) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException{
				String sql = "INSERT INTO genres(id,name,description,deleted) VALUES(?,?,?,?)";
				
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int i = 1;
				ps.setLong(i++, genre.getId());
				ps.setString(i++, genre.getName());
				ps.setString(i++, genre.getDescription());
				ps.setString(i++, String.valueOf(genre.isDeleted()));
				
				return ps;
			}
		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		
		return uspeh?1:0;
	}

	@Transactional
	@Override
	public int update(Genre genre) {
		boolean uspeh = true;
		
		String sql = "UPDATE genres SET name = ?, description = ?, deleted = ? WHERE id = ?";
		uspeh = uspeh && jdbcTemplate.update(sql,genre.getName(),genre.getDescription(),String.valueOf(genre.isDeleted()),genre.getId()) == 1;
		
		return uspeh?1:0;
	}

	@Transactional
	@Override
	public int delete(Long id) {
		String sql = "DELETE FROM genres WHERE id = ?";
		return jdbcTemplate.update(sql,id);		
	}

	@Transactional
	@Override
	public int logicDelete(Genre genre) {
		boolean uspeh = true;
		
		String sql = "UPDATE genres SET deleted = ? WHERE id = ?";
		uspeh = uspeh && jdbcTemplate.update(sql,genre.isDeleted()) == 1;
		
		return uspeh?1:0;		
	}

	@Override
	public List<Genre> sortByNameAsc() {
		String sql = "SELECT g.id, g.name, g.description, g.deleted from genres as g"
				+ "		ORDER BY g.name ASC;";
		GenreRowCallBackHandler rcbh = new GenreRowCallBackHandler();
		jdbcTemplate.query(sql, rcbh);
		
		return rcbh.getGenres();
	}

	@Override
	public List<Genre> sortByNameDesc() {
		String sql = "SELECT g.id, g.name, g.description, g.deleted from genres as g"
				+ "		ORDER BY g.name DESC;";
		GenreRowCallBackHandler rcbh = new GenreRowCallBackHandler();
		jdbcTemplate.query(sql, rcbh);
		
		return rcbh.getGenres();
	}
}
