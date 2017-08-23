package com.gcit.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;

public class AdminService {
	ConnectionUtil cUtil = new ConnectionUtil();

	public void saveAuthor(Author author) throws SQLException {
		Connection conn = null;

		conn = cUtil.getConnection();
		AuthorDAO adao = new AuthorDAO(conn);
		try {
			if (author.getAuthorId() != null) {
				adao.updateAuthor(author);
			} else {
				int authId = adao.addAuthorWithID(author);
				if (author.getBooks() != null) {

					BookDAO bdao = new BookDAO(conn);
					for (Book b : author.getBooks()) {
						bdao.addBookAuthor(b, authId);
					}

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

	public void deleteAuthor(Author author) throws SQLException {
		Connection conn = null;

		conn = cUtil.getConnection();
		AuthorDAO adao = new AuthorDAO(conn);
		try {
			adao.deleteAuthor(author);
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

	public Author getAuthorByPK(Integer authorId) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		AuthorDAO adao = new AuthorDAO(conn);
		try {
			return adao.readAuthorsByPK(authorId);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}

	public Integer getAuthorsCount() throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		AuthorDAO adao = new AuthorDAO(conn);
		try {
			return adao.getAuthorsCount();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}

	public Integer getAuthorsCount(String searchString) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		AuthorDAO adao = new AuthorDAO(conn);
		try {
			return adao.getAuthorsCount(searchString);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}

	public List<Author> getAllAuthors() throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		AuthorDAO adao = new AuthorDAO(conn);
		try {
			return adao.readAllAuthors();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}

	public List<Author> getAllAuthors(Integer pageNo, String searchString)
			throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		AuthorDAO adao = new AuthorDAO(conn);
		try {
			if (searchString != null) {
				return adao.readAllAuthorsByName(pageNo, searchString);
			} else {
				return adao.readAllAuthors(pageNo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}

	public List<Book> getAllBooks() throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BookDAO bdao = new BookDAO(conn);
		try {
			return bdao.readAllBooks();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}

	public Book getBookByPK(Integer bookId) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BookDAO bdao = new BookDAO(conn);
		try {
			return bdao.readBookByPK(bookId);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}

	public List<Book> getAllBooks(Integer pageNo, String searchString)
			throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BookDAO bdao = new BookDAO(conn);
		try {
			if (searchString != null) {
				return bdao.readAllBooksByName(pageNo, searchString);
			} else {
				return bdao.readAllBooks(pageNo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}

	public Integer getBooksCount(String searchString) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BookDAO bdao = new BookDAO(conn);
		try {
			return bdao.getBooksCount(searchString);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}

	public void saveBook(Book book) throws SQLException {
		Connection conn = null;

		conn = cUtil.getConnection();
		BookDAO bdao = new BookDAO(conn);
		AuthorDAO adao = new AuthorDAO(conn);
		GenreDAO gdao = new GenreDAO(conn);
		PublisherDAO pdao = new PublisherDAO(conn);
		int bookId;
		try {
			if (book.getBookId() != null) {
				
				bookId = book.getBookId();
				bdao.resetBookAuthors(bookId);
				bdao.resetBookGenres(bookId);
				bdao.resetBookPublisher(bookId);
				
				if (book.getAuthors() != null) {
					for (Author a : book.getAuthors()) {
						adao.addAuthorBook(a, bookId);
					}
				}
				
				if (book.getGenres() != null) {
					for (Genre g : book.getGenres()) {
						gdao.addGenreBook(g, bookId);
					}
				}
				
				if (book.getPublisher() != null) {
					pdao.addPublisherBook(book.getPublisher(), bookId);
				}
				bdao.updateBook(book);
			} else {
				
				bookId = bdao.addBookWithID(book);
				if (book.getAuthors() != null) {
					for (Author a : book.getAuthors()) {
						adao.addAuthorBook(a, bookId);
					}
				}
				
				if (book.getGenres() != null) {
					for (Genre g : book.getGenres()) {
						gdao.addGenreBook(g, bookId);
					}
				}
				
				if (book.getPublisher() != null) {
					pdao.addPublisherBook(book.getPublisher(), bookId);
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

	public void deleteBook(Book Book) throws SQLException {
		Connection conn = null;

		conn = cUtil.getConnection();
		BookDAO bdao = new BookDAO(conn);
		try {
			bdao.deleteBook(Book);
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

	public void saveGenre(Genre genre) throws SQLException {
		Connection conn = null;

		conn = cUtil.getConnection();
		GenreDAO gdao = new GenreDAO(conn);
		try {
			if (genre.getGenreId() != null) {
				gdao.updateGenre(genre);
			} else {
				gdao.addGenre(genre);
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

	public List<Genre> getAllGenres() throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		GenreDAO gdao = new GenreDAO(conn);
		try {
			return gdao.readAllGenres();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}

	public Genre getGenreByPK(Integer authorId) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		GenreDAO gdao = new GenreDAO(conn);
		try {
			return gdao.readGenresByPK(authorId);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}

	public void deleteGenre(Genre genre) throws SQLException {
		Connection conn = null;

		conn = cUtil.getConnection();
		GenreDAO gdao = new GenreDAO(conn);
		try {
			gdao.deleteGenre(genre);
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

	public List<Publisher> getAllPublishers() throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		PublisherDAO pdao = new PublisherDAO(conn);
		try {
			return pdao.readAllPublishers();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}

	public Publisher getPublisherByPK(Integer authorId) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		PublisherDAO pdao = new PublisherDAO(conn);
		try {
			return pdao.readPublisherByPK(authorId);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}
	
	public List<Publisher> getAllPublishers(Integer pageNo, String searchString)
			throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		PublisherDAO pdao = new PublisherDAO(conn);
		try {
			if (searchString != null) {
				return pdao.readAllPublishersByName(pageNo, searchString);
			} else {
				return pdao.readAllPublishers(pageNo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}
	
	public Integer getPublishersCount(String searchString) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		PublisherDAO pdao = new PublisherDAO(conn);
		try {
			return pdao.getPublishersCount(searchString);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}
	
	public void deletePublisher(Publisher publisher) throws SQLException {
		Connection conn = null;

		conn = cUtil.getConnection();
		PublisherDAO pdao = new PublisherDAO(conn);
		try {
			pdao.deletePublisher(publisher);
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
	
	public void savePublisher(Publisher publisher) throws SQLException {
		Connection conn = null;

		conn = cUtil.getConnection();
		PublisherDAO pdao = new PublisherDAO(conn);
		try {
			if (publisher.getPublisherId() != null) {
				pdao.updatePublisherName(publisher);
				if (publisher.getPublisherAddress() != null){pdao.updatePublisherAddress(publisher);}
				if (publisher.getPublisherPhone() != null){pdao.updatePublisherPhone(publisher);}
				
				//pdao.updatePublisherAddress(publisher);
				//pdao.updatePublisherPhone(publisher);
				
			} else {
				int pubId = pdao.addPublisherWithID(publisher);
				if (publisher.getPublisherAddress() != null){
					pdao.updatePublisherAddress(pubId,publisher.getPublisherAddress());
					}
				if (publisher.getPublisherPhone() != null){
					pdao.updatePublisherPhone(pubId,publisher.getPublisherPhone());
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
	
	public Branch getBranchByPK(Integer authorId) throws SQLException {
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
	
	public List<Branch> getAllBranchs() throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BranchDAO pdao = new BranchDAO(conn);
		try {
			return pdao.readAllBranchs();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}

	
	public List<Branch> getAllBranchs(Integer pageNo, String searchString)
			throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BranchDAO pdao = new BranchDAO(conn);
		try {
			if (searchString != null) {
				return pdao.readAllBranchesByName(pageNo, searchString);
			} else {
				return pdao.readAllBranches(pageNo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}
	
	public Integer getBranchsCount(String searchString) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BranchDAO bdao = new BranchDAO(conn);
		try {
			return bdao.getBranchesCount(searchString);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}
	
	public void deleteBranch(Branch branch) throws SQLException {
		Connection conn = null;

		conn = cUtil.getConnection();
		BranchDAO pdao = new BranchDAO(conn);
		try {
			pdao.deleteBranch(branch);
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
	
	public List<Borrower> getAllBorrowers() throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BorrowerDAO pdao = new BorrowerDAO(conn);
		try {
			return pdao.readAllBorrowers();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}

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
	
	public List<Borrower> getAllBorrowers(Integer pageNo, String searchString)
			throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BorrowerDAO pdao = new BorrowerDAO(conn);
		try {
			if (searchString != null) {
				return pdao.readAllBorrowersByName(pageNo, searchString);
			} else {
				return pdao.readAllBorrowers(pageNo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}
	
	public Integer getBorrowersCount(String searchString) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BorrowerDAO pdao = new BorrowerDAO(conn);
		try {
			return pdao.getBorrowersCount(searchString);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}
	
	public void deleteBorrower(Borrower borrower) throws SQLException {
		Connection conn = null;

		conn = cUtil.getConnection();
		BorrowerDAO pdao = new BorrowerDAO(conn);
		try {
			pdao.deleteBorrower(borrower);
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
	
	public void saveBorrower(Borrower borrower) throws SQLException {
		Connection conn = null;

		conn = cUtil.getConnection();
		BorrowerDAO pdao = new BorrowerDAO(conn);
		try {
			if (borrower.getCardNo() != null) {
				pdao.updateBorrowerName(borrower);
				if (borrower.getAddress() != null){pdao.updateBorrowerAddress(borrower);}
				if (borrower.getPhone() != null){pdao.updateBorrowerPhone(borrower);}
				
				//pdao.updateAddress(borrower);
				//pdao.updatePhone(borrower);
				
			} else {
				int userId = pdao.addBorrowerWithID(borrower);
				if (borrower.getAddress() != null){
					pdao.updateBorrowerAddress(userId,borrower.getAddress());
					}
				if (borrower.getPhone() != null){
					pdao.updateBorrowerPhone(userId,borrower.getPhone());
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
	
	
	
	public BookLoan getBookLoanByDateOut(String dateout) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BookLoanDAO bdao = new BookLoanDAO(conn);
		try {
			//System.out.println(bdao.readBookLoanByDateOut("2011-11-11 00:00:01").getBook().getTitle());
			return bdao.readBookLoanByDateOut(dateout);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}
	
	public BookLoan getBookLoanBy4Pks( Integer bookId, Integer branchId, Integer cardNo, String dateout) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BookLoanDAO bdao = new BookLoanDAO(conn);
		try {
			//System.out.println(bdao.readBookLoanByDateOut("2011-11-11 00:00:01").getBook().getTitle());
			return bdao.readBookLoanBy4Pks( bookId,  branchId,  cardNo,  dateout);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}
	
	public List<BookLoan> getAllBookLoans() throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BookLoanDAO pdao = new BookLoanDAO(conn);
		try {
			return pdao.readAllBookLoans();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}

	
	public List<BookLoan> getAllBookLoans(Integer pageNo, String searchString)
			throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BookLoanDAO pdao = new BookLoanDAO(conn);
		try {
			if (searchString != null) {
				return pdao.readAllBookLoansByDateOut(pageNo, searchString);
			} else {
				return pdao.readAllBookLoans(pageNo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}
	
	public List<BookLoan> getAllDueBookLoans(Integer pageNo, Integer cardNo)
			throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BookLoanDAO pdao = new BookLoanDAO(conn);
		try {
			
				return pdao.readBookLoansByBorrowerCardNo(pageNo, cardNo);
		

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
		
	}
	
	public Integer getBookLoansCount(String searchString) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BookLoanDAO bdao = new BookLoanDAO(conn);
		try {
			return bdao.getBookLoansCount(searchString);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}
	
	public Integer getBookLoansCount(Integer cardNo) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BookLoanDAO bdao = new BookLoanDAO(conn);
		try {
			return bdao.getBookLoansCount(cardNo);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}
	
	public Integer getDueBookLoansCount(Integer cardNo) throws SQLException {
		Connection conn = null;
		conn = cUtil.getConnection();
		BookLoanDAO bdao = new BookLoanDAO(conn);
		try {
			return bdao.getDueBookLoansCount(cardNo);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;
	}
	
	public void deleteBookLoan(BookLoan bookloan) throws SQLException {
		Connection conn = null;

		conn = cUtil.getConnection();
		BookLoanDAO pdao = new BookLoanDAO(conn);
		try {
			pdao.deleteBookLoan(bookloan);
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
	
	public void overrideDueDate(BookLoan bookloan, String newDueDate) throws SQLException {
		Connection conn = null;

		conn = cUtil.getConnection();
		BookLoanDAO pdao = new BookLoanDAO(conn);
		try {
			bookloan.setDueDate(newDueDate);
			pdao.updateBookLoanDueDate(bookloan);
			//pdao.overrideDueDate(bookloan, newDueDate);
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
