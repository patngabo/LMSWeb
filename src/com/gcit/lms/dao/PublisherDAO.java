package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Publisher;

public class PublisherDAO extends BaseDAO{
	
	public PublisherDAO(Connection conn) {
		super(conn);
	}

	public void addPublisher(Publisher publisher) throws SQLException{
		save("insert into tbl_publisher(publisherName) values (?)", new Object[] {publisher.getPublisherName()});
	}
	
	public void addPublisherBook(Publisher publisher, Integer bookId) throws SQLException{
		save("update tbl_book set pubId=? where bookId=?", new Object[] {publisher.getPublisherId(),bookId});
	}
	
	public Integer addPublisherWithID(Publisher publisher) throws SQLException{
		return saveWithID("insert into tbl_publisher(publisherName) values (?)", new Object[] {publisher.getPublisherName()});
	}
	
	public void updatePublisherName(Publisher publisher) throws SQLException{
		save("update tbl_publisher set publisherName =? where publisherId = ?", new Object[] {publisher.getPublisherName(), publisher.getPublisherId()});
	}
	
	public void updatePublisherName(Integer publisherId, String publisherName) throws SQLException{
		save("update tbl_publisher set publisherName =? where publisherId = ?", new Object[] {publisherName, publisherId});
	}
	
	public void updatePublisherPhone(Publisher publisher) throws SQLException{
		save("update tbl_publisher set publisherPhone =? where publisherId = ?", new Object[] {publisher.getPublisherPhone(), publisher.getPublisherId()});
	}
	
	public void updatePublisherPhone(Integer publisherId, String publisherPhone) throws SQLException{
		save("update tbl_publisher set publisherPhone =? where publisherId = ?", new Object[] {publisherPhone,publisherId});
	}
	
	public void updatePublisherAddress(Publisher publisher) throws SQLException{
		save("update tbl_publisher set publisherAddress =? where publisherId = ?", new Object[] {publisher.getPublisherAddress(), publisher.getPublisherId()});
	}
	
	public void updatePublisherAddress(Integer publisherId, String publisherAddress) throws SQLException{
		save("update tbl_publisher set publisherAddress =? where publisherId = ?", new Object[] {publisherAddress,publisherId});
	}
	
	public void deletePublisher(Publisher publisher) throws SQLException{
		save("delete from tbl_publisher where publisherId = ?", new Object[] {publisher.getPublisherId()});
	}
	
	@SuppressWarnings("unchecked")
	public List<Publisher> readAllPublishers() throws SQLException{
		return (List<Publisher>) read("select * from tbl_publisher", null);
	}
	
	public Integer getPublishersCount() throws SQLException{
		return getCount("select count(*) as COUNT from tbl_publisher", null);
	}
	
	public Integer getPublishersCount(String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		//setPageNo(pageNo);
		return getCount("select count(*) as COUNT from tbl_publisher where publisherName like ?", new Object[]{searchString});
	}
	
	@SuppressWarnings("unchecked")
	public List<Publisher> readAllPublishers(Integer pageNo) throws SQLException{
		setPageNo(pageNo);
		return (List<Publisher>) read("select * from tbl_publisher", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Publisher> readAllPublishersByName(Integer pageNo, String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		setPageNo(pageNo);
		return (List<Publisher>) read("select * from tbl_publisher where publisherName like ?", new Object[]{searchString});
	}
	
	@SuppressWarnings("unchecked")
	public List<Publisher> readAllPublishersByPublisherName(String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		return (List<Publisher>) read("select * from tbl_publisher where publisherName like ?", new Object[]{searchString});
	}
	
	@SuppressWarnings("unchecked")
	public Publisher readPublisherByPK(Integer publisherId) throws SQLException{
		List<Publisher> publishers = (List<Publisher>) read("select * from tbl_publisher where publisherId = ?", new Object[]{publisherId});
		if(publishers!=null && publishers.size() > 0){
			return publishers.get(0);
		}
		return null;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Publisher> extractData(ResultSet rs) throws SQLException {
		List<Publisher> publishers = new ArrayList<>();
		BookDAO bdao = new BookDAO(conn);
		while(rs.next()){
			Publisher p = new Publisher();
			p.setPublisherId(rs.getInt("publisherId"));
			p.setPublisherName(rs.getString("publisherName"));
			p.setPublisherAddress(rs.getString("publisherAddress"));
			p.setPublisherPhone(rs.getString("publisherPhone"));
			p.setBooks((List<Book>) bdao.readFirstLevel("select * from tbl_book where pubId = ?", new Object[]{p.getPublisherId()}));
			publishers.add(p);
		}
		return publishers;
	}
	
	@Override
	public List<Publisher> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<Publisher> publishers = new ArrayList<>();
		while(rs.next()){
			Publisher b = new Publisher();
			b.setPublisherId(rs.getInt("publisherId"));
			b.setPublisherName(rs.getString("publisherName"));
			b.setPublisherAddress(rs.getString("publisherAddress"));
			b.setPublisherPhone(rs.getString("publisherPhone"));
			publishers.add(b);
		}
		return publishers;
	}


}
