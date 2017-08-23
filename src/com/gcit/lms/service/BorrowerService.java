package com.gcit.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.gcit.lms.dao.BookCopyDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopy;
import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;

public class BorrowerService {
	ConnectionUtil cUtil = new ConnectionUtil();
	
	
	public Borrower getBorrowerByPK(Integer authorId) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BorrowerDAO pdao = new BorrowerDAO(conn);
		try {
			return pdao.readBorrowerByPK(authorId);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}
	
	public Boolean isValidCardNo(Integer cardNo) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BorrowerDAO pdao = new BorrowerDAO(conn);
		try {
			Integer num =  pdao.getBorrowersCountByPk(cardNo);
			if(num > 0){
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return false;
	}
	
	public void checkOutBook(Integer bookId, Integer branchId, Integer cardNo) throws SQLException{
		Connection conn = null;
		conn = cUtil.getConnection();
		
		
		try {
			Branch branch = new Branch();
			Book book = new Book();
			Borrower borrower = new Borrower();
			
			branch.setBranchId(branchId);
			book.setBookId(bookId);
			borrower.setCardNo(cardNo);
			
			BookLoan bookloan = new BookLoan();
			
			bookloan.setBook(book);
			bookloan.setBorrower(borrower);
			bookloan.setBranch(branch);
			
			LocalDateTime todayDateTime   = LocalDateTime.now();
			bookloan.setDateOut(todayDateTime+"");
			bookloan.setDueDate(todayDateTime.plusWeeks(1)+"");
			
			BookCopyDAO cdao = new BookCopyDAO(conn);
			cdao.decrementNoOfCopiesToZero(bookId, branchId);
			cdao.decrementNoOfCopies(bookId, branchId);
			
			BookLoanDAO bdao = new BookLoanDAO(conn);
			bdao.addBookLoan(bookloan);
			
			conn.commit();
			
		} catch (SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally{
			if (conn != null){
				conn.close();
			}
		}
		
		
		//TODO: may be do it inn different steps on the UI
	}
	
	public void returnBook(BookLoan bookLoan) throws SQLException{
		Connection conn = null;
		conn = cUtil.getConnection();
		BookLoanDAO bdao = new BookLoanDAO(conn);
		BookCopyDAO cdao = new BookCopyDAO(conn);
		try {
			 bdao.returnBook(bookLoan);
			 
			 BookCopy bookCopy = new BookCopy();
			 bookCopy.setBook(bookLoan.getBook());
			 bookCopy.setBranch(bookLoan.getBranch());
			 if (cdao.getBookCopiesCount(bookCopy) > 0){
				 cdao.incrementNoOfCopies(bookLoan.getBook().getBookId(), bookLoan.getBranch().getBranchId());
			 }else{
				 bookCopy.setNoOfCopies(1);
				 cdao.addBookCopy(bookCopy);
			 }
			
			
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	public List<Branch> getAllBranches() throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BranchDAO bdao = new BranchDAO(conn);
		try {
			return bdao.readAllBranchs();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}
	
	public List<Book> getAllBooksOwned(Branch branch) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BookDAO bdao = new BookDAO(conn);
		try {
			return bdao.readAllBooksByBranch(branch);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}

}
