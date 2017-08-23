package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Publisher;

public class BookLoanDAO extends BaseDAO{
	
	public BookLoanDAO(Connection conn) {
		super(conn);
	}

	public void addBookLoan(BookLoan bookLoan) throws SQLException{
		save("insert into tbl_book_loans(bookId, branchId, cardNo, dateOut, dueDate) values (?, ?, ?,?,?)", new Object[] {
				bookLoan.getBook().getBookId(), bookLoan.getBranch().getBranchId(), 
				bookLoan.getBorrower().getCardNo(), bookLoan.getDateOut(), bookLoan.getDueDate()});
	}
	
	public Integer addBookLoanWithID(BookLoan bookLoan) throws SQLException{
		return saveWithID("insert into tbl_book_loans(bookId, branchId, cardNo, dateOut) values (?,?,?,?)", new Object[] {
				bookLoan.getBook().getBookId(), bookLoan.getBranch().getBranchId(), 
				bookLoan.getBorrower().getCardNo(), bookLoan.getDateOut()});
	}
	
	public void updateBookLoan(BookLoan bookLoan) throws SQLException{
		save("update tbl_book_loans set dueDate = ?, dateIn = ? where  bookId = ? and branchId = ? and cardNo = ? and dateOut = ?",
				new Object[] {bookLoan.getDueDate(), bookLoan.getDateIn(), 
				bookLoan.getBook().getBookId(), bookLoan.getBranch().getBranchId(), 
				bookLoan.getBorrower().getCardNo(), bookLoan.getDateOut()});
	}
	
	public void updateBookLoanDueDate(BookLoan bookLoan) throws SQLException{
		save("update tbl_book_loans set dueDate = ? where  bookId = ? and branchId = ? and cardNo = ? and dateOut = ?",
				new Object[] {bookLoan.getDueDate(), 
				bookLoan.getBook().getBookId(), bookLoan.getBranch().getBranchId(), 
				bookLoan.getBorrower().getCardNo(), bookLoan.getDateOut()});	
		}
	
	public void updateBookLoanDateIn(BookLoan bookLoan) throws SQLException{
		save("update tbl_book_loans set dateIn = ? where  bookId = ? and branchId = ? and cardNo = ? and dateOut = ?",
				new Object[] {bookLoan.getDateIn(), 
				bookLoan.getBook().getBookId(), bookLoan.getBranch().getBranchId(), 
				bookLoan.getBorrower().getCardNo(), bookLoan.getDateOut()});	
		}
	
	public void returnBook(BookLoan bookLoan) throws SQLException{
		save("update tbl_book_loans set dateIn = NOW() where  bookId = ? and branchId = ? and cardNo = ? and dateOut = ?",
				new Object[] {
				bookLoan.getBook().getBookId(), bookLoan.getBranch().getBranchId(), 
				bookLoan.getBorrower().getCardNo(), bookLoan.getDateOut()});	
		}
	
	public void deleteBookLoan(BookLoan bookLoan) throws SQLException{
		save("delete from tbl_book_loans where bookId = ? and branchId = ? and cardNo = ? and dateOut = ?", 
				new Object[] {bookLoan.getBook().getBookId(), bookLoan.getBranch().getBranchId(), 
				bookLoan.getBorrower().getCardNo(), bookLoan.getDateOut()});
	}
	
	public Integer getBookLoansCount() throws SQLException{
		return getCount("select count(*) as COUNT from tbl_publisher", null);
	}
	
	public Integer getBookLoansCount(String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		//setPageNo(pageNo);
		return getCount("select count(*) as COUNT from tbl_book_loans where dateOut like ?", new Object[]{searchString});
	}
	
	public Integer getBookLoansCount(Integer cardNo) throws SQLException{
		//setPageNo(pageNo);
		return getCount("select count(*) as COUNT from tbl_book_loans where cardNo = ?", new Object[]{cardNo});
	}
	
	public Integer getDueBookLoansCount(Integer cardNo) throws SQLException{
		//setPageNo(pageNo);
		return getCount("select count(*) as COUNT from tbl_book_loans where cardNo = ? and dateIn IS NULL", new Object[]{cardNo});
	}
	
	public BookLoan readBookLoanByDateOut(String dateout) throws SQLException{
		@SuppressWarnings("unchecked")
		List<BookLoan> bookloans = (List<BookLoan>) read("select * from tbl_book_loans where dateOut = ?", new Object[]{dateout});
		if(bookloans!=null){
			return bookloans.get(0);
		}
		return null;
	}
	
	public BookLoan readBookLoanBy4Pks(Integer bookId, Integer branchId, Integer cardNo,String dateout) throws SQLException{
		@SuppressWarnings("unchecked")
		List<BookLoan> bookloans = (List<BookLoan>) read("select * from tbl_book_loans where bookId=? and branchId=? and cardNo=? and dateOut = ?", new Object[]{
				bookId, branchId, cardNo, dateout});
		if(bookloans!=null){
			return bookloans.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<BookLoan> readAllBookLoans(Integer pageNo) throws SQLException{
		setPageNo(pageNo);
		return (List<BookLoan>) read("select * from tbl_book_loans", null);
	}
	
	public void overrideDueDate(BookLoan bookloan, String newDueDate){
		// Probably not necessary
	}
	
	@SuppressWarnings("unchecked")
	public List<BookLoan> readAllBookLoansByDateOut(Integer pageNo, String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		setPageNo(pageNo);
		return (List<BookLoan>) read("select * from tbl_book_loans where dateOut like ?", new Object[]{searchString});
	}
	
	@SuppressWarnings("unchecked")
	public List<BookLoan> readAllBookLoans() throws SQLException{
		return (List<BookLoan>) read("select * from tbl_book_loans", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<BookLoan> readAllBookLoansByBorrower(Borrower borrower) throws SQLException{
		//searchString = "%"+searchString+"%";
		return (List<BookLoan>) read("select * from tbl_book_loans where cardNo = ?", new Object[]{borrower.getCardNo()});
	}
	
	/*
	 * readAllBookLoansByLibrary
	 * readAllBookLoansByBranch
	 */
	
	@SuppressWarnings("unchecked") // getting only books not returned yet
	public List<BookLoan> readBookLoansByBorrowerCardNo(Integer cardNo) throws SQLException{
		List<BookLoan> bookLoans = (List<BookLoan>) read("select * from tbl_book_loans where cardNo = ? and dateIn IS NULL", new Object[]{cardNo});
//		if(bookLoans!=null){
//			return bookLoans.get(0);
//		}
//		return null;
		return bookLoans;
	}
	
	@SuppressWarnings("unchecked") // getting only books not returned yet
	public List<BookLoan> readBookLoansByBorrowerCardNo(Integer pageNo, Integer cardNo) throws SQLException{
		setPageNo(pageNo);
		List<BookLoan> bookLoans = (List<BookLoan>) read("select * from tbl_book_loans where cardNo = ? and dateIn IS NULL", new Object[]{cardNo});
//		if(bookLoans!=null){
//			return bookLoans.get(0);
//		}
//		return null;
		return bookLoans;
	}


	@Override
	public List<BookLoan> extractData(ResultSet rs) throws SQLException {
		List<BookLoan> bookLoans = new ArrayList<>();
		BookDAO bookdao = new BookDAO(conn);		
		BranchDAO branchdao = new BranchDAO(conn);
		BorrowerDAO borrowerdao = new BorrowerDAO(conn);
		while(rs.next()){
			BookLoan b = new BookLoan();
			// TODO my_implementation: verification and testing needed
			b.setBook(bookdao.readBookByPK(rs.getInt("bookId")));
			b.setBranch(branchdao.readBranchByPK(rs.getInt("branchId")));
			b.setBorrower(borrowerdao.readBorrowerByPK(rs.getInt("cardNo")));
			b.setDateOut(rs.getString("dateOut"));
			b.setDueDate(rs.getString("dueDate"));
			b.setDateIn(rs.getString("dateIn"));
			// end my_implementation
			bookLoans.add(b);
		}
		return bookLoans;
	}
	
	@Override
	public List<BookLoan> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<BookLoan> bookLoans = new ArrayList<>();
		BookDAO bookdao = new BookDAO(conn);		
		BranchDAO branchdao = new BranchDAO(conn);
		BorrowerDAO borrowerdao = new BorrowerDAO(conn);
		while(rs.next()){
			BookLoan b = new BookLoan();
			// TODO the three_ops may cause infinite loops
			b.setBook(bookdao.readBookByPK(rs.getInt("bookId")));
			b.setBranch(branchdao.readBranchByPK(rs.getInt("branchId")));
			b.setBorrower(borrowerdao.readBorrowerByPK(rs.getInt("cardNo")));
			// end three_ops
			b.setDateOut(rs.getString("dateOut"));
			b.setDueDate(rs.getString("dueDate"));
			b.setDateIn(rs.getString("dateIn"));
			bookLoans.add(b);
		}
		return bookLoans;
	}

}
