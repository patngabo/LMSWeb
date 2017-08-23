package com.gcit.lms.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;
import com.gcit.lms.service.AdminService;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet({"/editBook" ,"/deleteBook", "/pageBooks", "/addBook", "/searchBooks" })
public class AdminBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminBookServlet() {
		super();
	}

	AdminService adminService = new AdminService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String reqUrl = request.getRequestURI().substring(
				request.getContextPath().length(),
				request.getRequestURI().length());
		Book book = new Book();
		String message = "";
		Boolean isAjax = Boolean.FALSE;
		switch (reqUrl) {
		
		case "/deleteBook":
			if (request.getParameter("bookId") != null
					&& !request.getParameter("bookId").isEmpty()) {
				book.setBookId(Integer.parseInt(request
						.getParameter("bookId")));

				try {
					adminService.deleteBook(book);
					message = "Book deleted Successfully";
				} catch (SQLException e) {
					e.printStackTrace();
					message = "Author delete failed. Try Again!";
				}
			}
			break;
	
		
			
			
		case "/pageBooks":
			if (request.getParameter("pageNo") != null
					&& !request.getParameter("pageNo").isEmpty()) {
				Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
				//Integer pages = Integer.parseInt(request.getParameter("pages"));
				String searchString = request.getParameter("searchString");
				if (searchString == null) {
					searchString = "";
				}
				try {
					List<Book> books = adminService.getAllBooks(pageNo,
							searchString);
					request.setAttribute("books", books);
					//request.setAttribute("pageNo", pageNo);
					request.setAttribute("searchString", "");
					//request.setAttribute("pages", pages);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			break;
		
		case "/searchBooks":
			System.out.println("pageNo = "+ request.getParameter("pageNo"));
			System.out.println("pages = "+ request.getParameter("pages"));
			if (request.getParameter("pageNo") != null
					&& !request.getParameter("pageNo").isEmpty()) {
				Integer pageNo = Integer.parseInt(request
						.getParameter("pageNo"));
				
				String searchString = request.getParameter("searchString");
				if (searchString == null) {
					searchString = "";}
				
				
				
				try {
					

					Integer booksCount = adminService.getBooksCount(searchString);
					int pages = 0;
					if (booksCount % 10 > 0) {
						pages = booksCount / 10 + 1;
					} else {
						pages = booksCount / 10;
					}
					
					List<Book> books = adminService.getAllBooks(pageNo,
							searchString);
					StringBuffer strBuf = new StringBuffer();
					
					strBuf.append("<nav aria-label='Page navigation'><ul class='pagination'><li><a href='#' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>");
					for (int i = 1; i<= pages; i++){
						strBuf.append("<li><a href='pageBooks?pageNo="+i+"&searchString="+searchString+"'>"+i+"</a></li>");
					}
					strBuf.append("<li><a href='#' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li></ul></nav>");
					strBuf.append("<table class='table' id='authorsTable'>");
					//TODO: added all the above append
					
					strBuf.append("<tr><th>No</th><th>Book Name</th><th>Authors</th><th>Genres</th><th>Publisher</th><th>Edit</th><th>Delete</th></tr>");
					for (Book bk : books) {
						int idx = books.indexOf(bk) + 1;
						strBuf.append("<tr><td>" + idx
								+ "</td><td>" + bk.getTitle() + "</td><td>");
						for (Author a : bk.getAuthors()) {
							strBuf.append(" '"+a.getAuthorName() + "' ");
						}
						strBuf.append("</td><td>");
						for (Genre g : bk.getGenres()) {
							strBuf.append(" '"+g.getGenreName() + "' ");
						}
						strBuf.append("</td><td>");
						if (bk.getPublisher()!=null) {
							strBuf.append(bk.getPublisher().getPublisherName());
						}else{
							strBuf.append("");
						}
						strBuf.append("</td><td><button type='button' class='btn btn-sm btn-primary'data-toggle='modal' data-target='#editBookModal' href='a_editbook.jsp?bookId="
								+ bk.getBookId() + "'>Edit!</button></td>");
						strBuf.append("<td><button type='button' class='btn btn-sm btn-danger' onclick='javascript:location.href='deleteBook?bookId="
								+ bk.getBookId()
								+ "''>Delete!</button></td></tr>");
					}
					//response.getWriter().write("");
					strBuf.append("</table>");
					response.getWriter().write(strBuf.toString());
					isAjax = Boolean.TRUE;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			break;

		default:
			break;
		}
		
		if (!isAjax) {
			request.setAttribute("message", message);
			RequestDispatcher rd = request
					.getRequestDispatcher("/a_viewbooks.jsp");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String reqUrl = request.getRequestURI().substring(
				request.getContextPath().length(),
				request.getRequestURI().length());
		Book book = new Book();
		
		switch (reqUrl) {
		case "/addBook":
			book.setTitle(request.getParameter("title"));
			try {
				if (request.getParameterValues("authorId") != null){
					String[] authorIds = request.getParameterValues("authorId");
					ArrayList<Author> authors = new ArrayList<Author>();
					for (String a: authorIds){
						Author author = adminService.getAuthorByPK(Integer.parseInt(a));
						authors.add(author);
					}
					book.setAuthors(authors);
				}else{}
				
				if (request.getParameterValues("genreId") != null){
					String[] genreIds = request.getParameterValues("genreId");
					ArrayList<Genre> genres = new ArrayList<Genre>();
					for (String g: genreIds){
						Genre genre = adminService.getGenreByPK(Integer.parseInt(g));
						genres.add(genre);
					}
					book.setGenres(genres);
				}else{}
				if (request.getParameterValues("publisherId") != null){
					String pubId = request.getParameter("publisherId");
					Publisher publisher = adminService.getPublisherByPK(Integer.parseInt(pubId));
					book.setPublisher(publisher);
				}else{}
				adminService.saveBook(book);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "/editBook":			
			
			try {
				book = adminService.getBookByPK(Integer.parseInt(request.getParameter("bookId")));
				
				//book.setAuthors(null);
				//book.setGenres(null);
				
				if (request.getParameterValues("title") != null){
					String title = request.getParameter("title");
					
					book.setTitle(title);
				}else{}
				
				if (request.getParameterValues("authorId") != null){
					String[] authorIds = request.getParameterValues("authorId");
					ArrayList<Author> authors = new ArrayList<Author>();
					for (String a: authorIds){
						Author author = adminService.getAuthorByPK(Integer.parseInt(a));
						authors.add(author);
					}
					book.setAuthors(authors);
				}else{book.setAuthors(null);}
				
				if (request.getParameterValues("genreId") != null){
					String[] genreIds = request.getParameterValues("genreId");
					ArrayList<Genre> genres = new ArrayList<Genre>();
					for (String g: genreIds){
						Genre genre = adminService.getGenreByPK(Integer.parseInt(g));
						genres.add(genre);
					}
					book.setGenres(genres);
				}else{book.setGenres(null);}
				if (request.getParameterValues("publisherId") != null){
					String pubId = request.getParameter("publisherId");
					if (pubId != "0"){
					Publisher publisher = adminService.getPublisherByPK(Integer.parseInt(pubId));
					book.setPublisher(publisher);
					}else{
						book.setPublisher(null);
					}
				}else{}
				adminService.saveBook(book);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		// case "/searchAuthors":
		// String searchString = request.getParameter("searchString");
		// try {
		//
		// List<Author> authors = adminService.getAllAuthors(1, searchString);
		// request.setAttribute("authors", authors);
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// break;
		default:
			break;
		}
		RequestDispatcher rd = request
				.getRequestDispatcher("/a_viewbooks.jsp");
		rd.forward(request, response);
	}

}
