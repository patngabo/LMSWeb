package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Book;

public class GenreDAO extends BaseDAO{

	public GenreDAO(Connection conn) {
		super(conn);
	}

	public void addGenre(Genre genre) throws SQLException{
		save("insert into tbl_genre(genreName) values (?)", new Object[] {genre.getGenreName()});
	}
	
	public void addGenreBook(Genre genre, Integer bookId) throws SQLException{
		save("insert ignore into tbl_book_genres(bookId, genre_id) values (?, ?)", new Object[] {bookId, genre.getGenreId()});
	}
	
	public Integer addGenreWithID(Genre genre) throws SQLException{
		return saveWithID("insert into tbl_genre(genre_name) values (?)", new Object[] {genre.getGenreName()});
	}
	
	public void updateGenre(Genre genre) throws SQLException{
		save("update tbl_genre set genreName =? where genre_id = ?", new Object[] {genre.getGenreName(), genre.getGenreId()});
	}
	
	public void deleteGenre(Genre genre) throws SQLException{
		save("delete from tbl_genre where genre_id = ?", new Object[] {genre.getGenreId()});
	}
	
	@SuppressWarnings("unchecked")
	public List<Genre> readAllGenres() throws SQLException{
		return (List<Genre>) read("select * from tbl_genre", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Genre> readAllGenresByName(String searchString) throws SQLException{ // more like search
		searchString = "%"+searchString+"%";
		return (List<Genre>) read("select * from tbl_genre where genre_name like ?", new Object[]{searchString});
	}
	
	@SuppressWarnings("unchecked")
	public Genre readGenresByPK(Integer genre_id) throws SQLException{
		List<Genre> genres = (List<Genre>) read("select * from tbl_genre where genre_id = ?", new Object[]{genre_id});
		if(genres!=null){
			return genres.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException {
		List<Genre> genres = new ArrayList<>();
		BookDAO bdao = new BookDAO(conn);
		while(rs.next()){
			Genre a = new Genre();
			a.setGenreId(rs.getInt("genre_id"));
			a.setGenreName(rs.getString("genre_name"));
			a.setBooks((List<Book>) bdao.readFirstLevel("select * from tbl_book where bookId IN (Select bookId from tbl_book_genres where genre_id = ?)", new Object[]{a.getGenreId()}));
			genres.add(a);
		}
		return genres;
	}
	
	@Override
	public List<Genre> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<Genre> genres = new ArrayList<>();
		while(rs.next()){
			Genre a = new Genre();
			a.setGenreId(rs.getInt("genre_id"));
			a.setGenreName(rs.getString("genre_name"));
			genres.add(a);
		}
		return genres;
	}
}
