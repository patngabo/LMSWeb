package com.gcit.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookCopyDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopy;
import com.gcit.lms.entity.Branch;

public class LibrarianService {
	ConnectionUtil cUtil = new ConnectionUtil();

	public List<BookCopy> getAllBookCopiesOwnedBy(Branch branch) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BookCopyDAO bdao = new BookCopyDAO(conn);
		try {
			return bdao.readAllBookCopiesByBranch(branch);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}
	
	public Branch getBranchByPk(Integer authorId) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BranchDAO bdao = new BranchDAO(conn);
		try {
			return bdao.readBranchByPK(authorId);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}
	
	public BookCopy getBookCopyByPks(Integer branchId, Integer bookId) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BookCopyDAO bdao = new BookCopyDAO(conn);
		try {
			return bdao.readBookCopyByPks(branchId, bookId);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
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

	public void addBookCopiesToBranch(Book book, Branch branch,
			Integer noOfCopies) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BranchDAO branchdao = new BranchDAO(conn);
		BookDAO bookdao = new BookDAO(conn);
		BookCopyDAO bookcopydao = new BookCopyDAO(conn);

		try {
			if (book.getBookId() != null) {
				bookdao.updateBook(book);
			} else {
				bookdao.addBook(book);
			}

			saveBranch(branch);
			// TODO: Not sure the below implementation is the best way to do it
			int currentNoOfCopies = bookcopydao.getBookCopyNoOfCopies(book,
					branch);
			BookCopy bookcopy = new BookCopy();
			bookcopy.setBook(book);
			bookcopy.setBranch(branch);
			bookcopy.setNoOfCopies(noOfCopies + currentNoOfCopies);
			bookcopydao.addBookCopy(bookcopy);
			// TODO add that book and branch to book_copies in one of the DAOs
			// i realized i may have to create a new bookCopy entity class

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

	public void updateBookCopiesOfBranch(Book book, Branch branch,
			Integer noOfCopies) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BranchDAO branchdao = new BranchDAO(conn);
		BookDAO bookdao = new BookDAO(conn);
		BookCopyDAO bookcopydao = new BookCopyDAO(conn);

		try {
			if (book.getBookId() != null) {
				bookdao.updateBook(book);
			} else {
				bookdao.addBook(book);
			}

			saveBranch(branch);
			// TODO: Not sure the below implementation is the best way to do it
			// int currentNoOfCopies = bookcopydao.getBookCopyNoOfCopies(book,
			// branch);
			BookCopy bookcopy = new BookCopy();
			bookcopy.setBook(book);
			bookcopy.setBranch(branch);
			bookcopy.setNoOfCopies(noOfCopies);
			bookcopydao.addBookCopy(bookcopy);
			// TODO add that book and branch to book_copies in one of the DAOs
			// i realized i may have to create a new bookCopy entity class

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
	
	public void deleteBookCopy(Integer bookId, Integer branchId) throws SQLException {
		Connection conn = null;

		conn = cUtil.getConnection();
		BookCopyDAO adao = new BookCopyDAO(conn);
		try {
			adao.deleteBookCopy(bookId, branchId);
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

	public void saveBranch(Branch branch) throws SQLException {
		Connection conn = null;

		conn = cUtil.getConnection();
		BranchDAO pdao = new BranchDAO(conn);
		try {
			if (branch.getBranchId() != null) {
				pdao.updateBranchName(branch);
				if (branch.getBranchAddress() != null){
					pdao.updateBranchNameAndAddress(branch);
					}else{
						pdao.updateBranchName(branch);
					}
				
				
				//pdao.updateBranchAddress(branch);
				//pdao.updateBranchPhone(branch);
				
			} else {
				int branchId = pdao.addBranchWithID(branch);
				if (branch.getBranchAddress() != null){
					pdao.updateBranchAddress(branchId,branch.getBranchAddress());
					}
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
	
	public void saveBookCopy(BookCopy copy) throws SQLException {
		Connection conn = null;

		conn = cUtil.getConnection();
		BookCopyDAO pdao = new BookCopyDAO(conn);
		try {
			if (copy.getBranch() != null && copy.getBook() != null) {
				pdao.updateNoOfCopies(copy);	
			} else {
				pdao.addBookCopy(copy);
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

}
