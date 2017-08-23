package com.gcit.lms.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.gcit.lms.entity.Publisher;
import com.gcit.lms.service.AdminService;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet({"/addPublisher" ,"/deletePublisher","/editPublisher", "/pagePublishers" })
public class AdminPublisherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminPublisherServlet() {
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
		Publisher publisher = new Publisher();
		String message = "";
		Boolean isAjax = Boolean.FALSE;
		switch (reqUrl) {
		
		case "/deletePublisher":
			if (request.getParameter("publisherId") != null
					&& !request.getParameter("publisherId").isEmpty()) {
				publisher.setPublisherId(Integer.parseInt(request
						.getParameter("publisherId")));

				try {
					adminService.deletePublisher(publisher);
					message = "Publisher deleted Successfully";
				} catch (SQLException e) {
					e.printStackTrace();
					message = "Author delete failed. Try Again!";
				}
			}
			break;
	
		
			
			
		case "/pagePublishers":
			if (request.getParameter("pageNo") != null
					&& !request.getParameter("pageNo").isEmpty()) {
				Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
				//Integer pages = Integer.parseInt(request.getParameter("pages"));
				String searchString = request.getParameter("searchString");
				if (searchString == null) {
					searchString = "";
				}
				try {
					List<Publisher> publishers = adminService.getAllPublishers(pageNo,
							searchString);
					request.setAttribute("publishers", publishers);
					//request.setAttribute("pageNo", pageNo);
					request.setAttribute("searchString", "");
					//request.setAttribute("pages", pages);
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
					.getRequestDispatcher("/a_viewpublishers.jsp");
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
		Publisher publisher = new Publisher();
		
		switch (reqUrl) {
		case "/addPublisher":
			publisher.setPublisherName(request.getParameter("publisherName"));
			if (request.getParameter("publisherAddress") != null){
				publisher.setPublisherAddress(request.getParameter("publisherAddress"));
			}else{}
			if (request.getParameter("publisherPhone") != null){
				publisher.setPublisherPhone(request.getParameter("publisherPhone"));
			}else{}
			
			try {
				adminService.savePublisher(publisher);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "/editPublisher":
			publisher.setPublisherName(request.getParameter("publisherName"));
			if (request.getParameter("publisherAddress") != null){
				publisher.setPublisherAddress(request.getParameter("publisherAddress"));
			}else{}
			if (request.getParameter("publisherPhone") != null){
				publisher.setPublisherPhone(request.getParameter("publisherPhone"));
			}else{}
			publisher.setPublisherId(Integer.parseInt(request
					.getParameter("publisherId")));
			try {
				adminService.savePublisher(publisher);
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
				.getRequestDispatcher("/a_viewpublishers.jsp");
		rd.forward(request, response);
	}

}
