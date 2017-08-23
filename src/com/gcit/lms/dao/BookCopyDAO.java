package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopy;
import com.gcit.lms.entity.Branch;

public class BookCopyDAO extends BaseDAO{
	
	public BookCopyDAO(Connection conn) {
		super(conn);
	}

	public void addBookCopy(BookCopy bookCopy) throws SQLException{
		save("insert into tbl_book_copies(bookId, branchId, noOfCopies) values (?,?,?)", new Object[] {
				bookCopy.getBook().getBookId(), bookCopy.getBranch().getBranchId(), 
				bookCopy.getNoOfCopies()});
	}
	
	public void decrementNoOfCopies(Integer bookId, Integer branchId) throws SQLException{
		save("update tbl_book_copies set noOfCopies = noOfCopies -1 where bookId = ? and branchId = ? ", new Object[] {bookId, branchId});
	}
	
	public void decrementNoOfCopiesToZero(Integer bookId, Integer branchId) throws SQLException{
		save("delete from tbl_book_copies where noOfCopies = 1 and bookId = ? and branchId = ? ", new Object[] {bookId, branchId});
	}
	
	public Integer getBookCopiesCount(BookCopy copy) throws SQLException{
		return getCount("select count(*) as COUNT from tbl_book_copies where bookId = ? and branchId = ?", new Object[]{
				copy.getBook().getBookId(), copy.getBranch().getBranchId()});
	}
	
	public void updateNoOfCopies(BookCopy bookCopy) throws SQLException{
		save("update tbl_book_copies set noOfCopies =  ? where bookId = ? and branchId = ? ", new Object[] {
				bookCopy.getNoOfCopies(),bookCopy.getBook().getBookId(), bookCopy.getBranch().getBranchId()});
		// TODO: check when it doesn't exist
	}
	
	public void incrementNoOfCopies(Integer bookId, Integer branchId) throws SQLException{
		save("update tbl_book_copies set noOfCopies =  noOfCopies + 1 where bookId = ? and branchId = ? ", new Object[] {bookId, branchId});
		// TODO: check when it doesn't exist
	}
	
	public Integer addBookCopyWithID(BookCopy bookCopy) throws SQLException{
		return saveWithID("insert into tbl_book_copies(bookId, branchId, noOfCopies) values (?)", new Object[] {
				bookCopy.getBook().getBookId(), bookCopy.getBranch().getBranchId(), 
				bookCopy.getNoOfCopies()});
	}
	
	@SuppressWarnings("unchecked")
	public BookCopy readBookCopyByPks(Integer branchId, Integer bookId) throws SQLException{
		List<BookCopy> copies = (List<BookCopy>) read("select * from tbl_book_copies where branchId = ? and bookId =?", new Object[]{branchId, bookId});
		if(copies!=null){
			return copies.get(0);
		}
		return null;
	}
	
	
	public void updateBookCopy(BookCopy bookCopy) throws SQLException{
		save("update tbl_book_copies set noOfCopies = ? where  bookId = ? and branchId = ?",
				new Object[] {bookCopy.getNoOfCopies(),  
				bookCopy.getBook().getBookId(), bookCopy.getBranch().getBranchId()});
	}
	// TODO: verify if this implementation is correct 
	public Integer getBookCopyNoOfCopies(Book book, Branch branch) throws SQLException{
		@SuppressWarnings("unchecked")
		List<BookCopy> bookCopies =  (List<BookCopy>) read("select noOfCopies from tbl_book_copies where bookId=? and branchId=?", new Object[]{
				book.getBookId(),branch.getBranchId()});
		if (bookCopies!=null){
			return bookCopies.get(0).getNoOfCopies();
		}
		return null;
	}
	
	public void deleteBookCopy(BookCopy bookCopy) throws SQLException{
		save("delete from tbl_book_copies where bookId = ? and branchId = ?", 
				new Object[] {bookCopy.getBook().getBookId(), bookCopy.getBranch().getBranchId()});
	}
	
	public void deleteBookCopy(Integer bookId, Integer branchId) throws SQLException{
		save("delete from tbl_book_copies where bookId = ? and branchId = ?", 
				new Object[] {bookId, branchId});
	}
	
	@SuppressWarnings("unchecked")
	public List<BookCopy> readAllBookCopies() throws SQLException{
		return (List<BookCopy>) read("select * from tbl_book_copies", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<BookCopy> readAllBookCopiesByBook(Book book) throws SQLException{
		//searchString = "%"+searchString+"%";
		return (List<BookCopy>) read("select * from tbl_book_copies where bookId = ?", new Object[]{book.getBookId()});
	}
	
	@SuppressWarnings("unchecked")
	public List<BookCopy> readAllBookCopiesByBranch(Branch branch) throws SQLException{
		//searchString = "%"+searchString+"%";
		return (List<BookCopy>) read("select * from tbl_book_copies where BranchId = ?", new Object[]{branch.getBranchId()});
	}
	
	/*
	 * readAllBookCopiesByLibrary
	 * readAllBookCopiesByBranch
	 */
	
	@SuppressWarnings("unchecked")
	public List<BookCopy> readBookCopiesByBorrowerCardNo(Integer cardNo) throws SQLException{
		List<BookCopy> bookCopies = (List<BookCopy>) read("select * from tbl_book_copies where cardNo = ?", new Object[]{cardNo});
//		if(bookCopies!=null){
//			return bookCopies.get(0);
//		}
//		return null;
		return bookCopies;
	}


	@Override
	public List<BookCopy> extractData(ResultSet rs) throws SQLException {
		List<BookCopy> bookCopies = new ArrayList<>();
		BookDAO bookdao = new BookDAO(conn);		
		BranchDAO branchdao = new BranchDAO(conn);
		
		while(rs.next()){
			BookCopy b = new BookCopy();
			// TODO my_implementation: verification and testing needed
			b.setBook(bookdao.readBookByPK(rs.getInt("bookId")));
			b.setBranch(branchdao.readBranchByPK(rs.getInt("branchId")));
			
			b.setNoOfCopies(rs.getInt("noOfCopies"));
			// end my_implementation
			bookCopies.add(b);
		}
		return bookCopies;
	}
	
	@Override
	public List<BookCopy> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<BookCopy> bookCopies = new ArrayList<>();
		BookDAO bookdao = new BookDAO(conn);		
		BranchDAO branchdao = new BranchDAO(conn);
		
		while(rs.next()){
			BookCopy b = new BookCopy();
			// TODO the three_ops may cause infinite loops
			b.setBook(bookdao.readBookByPK(rs.getInt("bookId")));
			b.setBranch(branchdao.readBranchByPK(rs.getInt("branchId")));
			
			// end three_ops
			b.setNoOfCopies(rs.getInt("noOfCopies"));
			bookCopies.add(b);
		}
		return bookCopies;
	}

}
