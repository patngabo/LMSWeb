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
import com.gcit.lms.service.AdminService;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet({ "/addAuthor","/editAuthor" ,"/deleteAuthor", "/pageAuthors","/searchAuthors" })
public class AdminAuthorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminAuthorServlet() {
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
		Author author = new Author();
		
		String message = "";
		Boolean isAjax = Boolean.FALSE;
		switch (reqUrl) {
		case "/deleteAuthor":
			if (request.getParameter("authorId") != null
					&& !request.getParameter("authorId").isEmpty()) {
				author.setAuthorId(Integer.parseInt(request
						.getParameter("authorId")));

				try {
					adminService.deleteAuthor(author);
					message = "Author deleted Successfully";
				} catch (SQLException e) {
					e.printStackTrace();
					message = "Author delete failed. Try Again!";
				}
			}
			break;
		
		
	
		case "/pageAuthors":
			if (request.getParameter("pageNo") != null
					&& !request.getParameter("pageNo").isEmpty()) {
				Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
				//Integer pages = Integer.parseInt(request.getParameter("pages"));
				String searchString = request.getParameter("searchString");
				if (searchString == null) {
					searchString = "";
				}
				try {
					List<Author> authors = adminService.getAllAuthors(pageNo,
							searchString);
					request.setAttribute("authors", authors);
					//request.setAttribute("pageNo", pageNo);
					//request.setAttribute("searchString", searchString);
					//request.setAttribute("pages", pages);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			break;
			
			
		case "/searchAuthors":
			//System.out.println("pageNo = "+ request.getParameter("pageNo"));
			//System.out.println("pages = "+ request.getParameter("pages"));
			if (request.getParameter("pageNo") != null
					&& !request.getParameter("pageNo").isEmpty()) {
				Integer pageNo = Integer.parseInt(request
						.getParameter("pageNo"));
				
				String searchString = request.getParameter("searchString");
				if (searchString == null) {
					searchString = "";}
				
				
				
				try {
					

					Integer authCount = adminService.getAuthorsCount(searchString);
					int pages = 0;
					if (authCount % 10 > 0) {
						pages = authCount / 10 + 1;
					} else {
						pages = authCount / 10;
					}
					
					List<Author> authors = adminService.getAllAuthors(pageNo,
							searchString);
					StringBuffer strBuf = new StringBuffer();
					
					strBuf.append("<nav aria-label='Page navigation'><ul class='pagination'><li><a href='#' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>");
					for (int i = 1; i<= pages; i++){
						strBuf.append("<li><a href='pageAuthors?pageNo="+i+"&searchString="+searchString+"'>"+i+"</a></li>");
					}
					strBuf.append("<li><a href='#' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li></ul></nav>");
					strBuf.append("<table class='table' id='authorsTable'>");
					//TODO: added all the above append
					
					strBuf.append("<tr><th>Author ID</th><th>Author Name</th><th>Books by Author</th><th>Edit</th><th>Delete</th></tr>");
					for (Author a : authors) {
						int idx = authors.indexOf(a) + 1;
						strBuf.append("<tr><td>" + idx
								+ "</td><td>" + a.getAuthorName() + "</td><td>");
						for (Book b : a.getBooks()) {
							strBuf.append(" '"+b.getTitle() + "' ");
						}
						strBuf.append("</td><td><button type='button' class='btn btn-sm btn-primary'data-toggle='modal' data-target='#editAuthorModal' href='a_editauthor.jsp?authorId="
								+ a.getAuthorId() + "'>Edit!</button></td>");
						strBuf.append("<td><button type='button' class='btn btn-sm btn-danger' onclick='javascript:location.href='deleteAuthor?authorId="
								+ a.getAuthorId()
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
					.getRequestDispatcher("/a_viewauthors.jsp");
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
		Author author = new Author();
		switch (reqUrl) {
		case "/addAuthor":
			author.setAuthorName(request.getParameter("authorName"));
			try {
				
				if (request.getParameterValues("bookId") != null){
					String[] bookIds = request.getParameterValues("bookId");
					ArrayList<Book> books = new ArrayList<Book>();
					for (String b: bookIds){
						Book book = adminService.getBookByPK(Integer.parseInt(b));
						books.add(book);
					}
					author.setBooks(books);
				}else{}
				adminService.saveAuthor(author);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "/editAuthor":
			author.setAuthorName(request.getParameter("authorName"));
			author.setAuthorId(Integer.parseInt(request
					.getParameter("authorId")));
			try {
				adminService.saveAuthor(author);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
			
			/*
		case "/searchAuthors":
			String searchString = request.getParameter("searchString");
			try {

				List<Author> authors = adminService.getAllAuthors(1,
						searchString);
				request.setAttribute("authors", authors);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break; */
			
		default:
			break;
		}
		RequestDispatcher rd = request
				.getRequestDispatcher("/a_viewauthors.jsp");
		rd.forward(request, response);
	}

}
