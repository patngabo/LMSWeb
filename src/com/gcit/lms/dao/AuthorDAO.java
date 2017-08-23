package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;

public class AuthorDAO extends BaseDAO{

	public AuthorDAO(Connection conn) {
		super(conn);
	}

	public void addAuthor(Author author) throws SQLException{
		save("insert into tbl_author(authorName) values (?)", new Object[] {author.getAuthorName()});
	}
	
	public void addAuthorBook(Author author, Integer bookId) throws SQLException{
		save("insert ignore into tbl_book_authors(bookId, authorId) values (?, ?)", new Object[] {bookId, author.getAuthorId()});
	}
	
	public Integer addAuthorWithID(Author author) throws SQLException{
		return saveWithID("insert into tbl_author(authorName) values (?)", new Object[] {author.getAuthorName()});
	}
	
	public void updateAuthor(Author author) throws SQLException{
		save("update tbl_author set authorName =? where authorId = ?", new Object[] {author.getAuthorName(), author.getAuthorId()});
	}
	
	public void deleteAuthor(Author author) throws SQLException{
		save("delete from tbl_author where authorId = ?", new Object[] {author.getAuthorId()});
	}
	
	@SuppressWarnings("unchecked")
	public List<Author> readAllAuthors() throws SQLException{
		return (List<Author>) read("select * from tbl_author", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Author> readAllAuthors(Integer pageNo) throws SQLException{
		setPageNo(pageNo);
		return (List<Author>) read("select * from tbl_author", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Author> readAllAuthorsByName(Integer pageNo, String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		setPageNo(pageNo);
		return (List<Author>) read("select * from tbl_author where authorName like ?", new Object[]{searchString});
	}
	
//	@SuppressWarnings("unchecked")
//	public List<Author> readAllAuthorsByName(String searchString) throws SQLException{
//		searchString = "%"+searchString+"%";
//		return (List<Author>) read("select * from tbl_author where authorName like ?", new Object[]{searchString});
//	}
	
	public Integer getAuthorsCount() throws SQLException{
		return getCount("select count(*) as COUNT from tbl_author", null);
	}
	
	public Integer getAuthorsCount(String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		//setPageNo(pageNo);
		return getCount("select count(*) as COUNT from tbl_author where authorName like ?", new Object[]{searchString});
	}
	
	@SuppressWarnings("unchecked")
	public Author readAuthorsByPK(Integer authorId) throws SQLException{
		List<Author> authors = (List<Author>) read("select * from tbl_author where authorId = ?", new Object[]{authorId});
		if(authors!=null){
			return authors.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Author> extractData(ResultSet rs) throws SQLException {
		List<Author> authors = new ArrayList<>();
		BookDAO bdao = new BookDAO(conn);
		while(rs.next()){
			Author a = new Author();
			a.setAuthorId(rs.getInt("authorId"));
			a.setAuthorName(rs.getString("authorName"));
			a.setBooks((List<Book>) bdao.readFirstLevel("select * from tbl_book where bookId IN (Select bookId from tbl_book_authors where authorId = ?)", new Object[]{a.getAuthorId()}));
			authors.add(a);
		}
		return authors;
	}
	
	@Override
	public List<Author> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<Author> authors = new ArrayList<>();
		while(rs.next()){
			Author a = new Author();
			a.setAuthorId(rs.getInt("authorId"));
			a.setAuthorName(rs.getString("authorName"));
			authors.add(a);
		}
		return authors;
	}
	
}
