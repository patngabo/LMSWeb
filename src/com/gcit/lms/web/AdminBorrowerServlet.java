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


import com.gcit.lms.entity.Borrower;
import com.gcit.lms.service.AdminService;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet({"/addBorrower" ,"/deleteBorrower","/editBorrower", "/pageBorrowers" })
public class AdminBorrowerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminBorrowerServlet() {
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
		Borrower borrower = new Borrower();
		String message = "";
		Boolean isAjax = Boolean.FALSE;
		switch (reqUrl) {
		
		case "/deleteBorrower":
			if (request.getParameter("borrowerId") != null
					&& !request.getParameter("borrowerId").isEmpty()) {
				borrower.setCardNo(Integer.parseInt(request
						.getParameter("borrowerId")));

				try {
					adminService.deleteBorrower(borrower);
					message = "Borrower deleted Successfully";
				} catch (SQLException e) {
					e.printStackTrace();
					message = "Author delete failed. Try Again!";
				}
			}
			break;
	
		
			
			
		case "/pageBorrowers":
			if (request.getParameter("pageNo") != null
					&& !request.getParameter("pageNo").isEmpty()) {
				Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
				//Integer pages = Integer.parseInt(request.getParameter("pages"));
				String searchString = request.getParameter("searchString");
				if (searchString == null) {
					searchString = "";
				}
				try {
					List<Borrower> borrowers = adminService.getAllBorrowers(pageNo,
							searchString);
					request.setAttribute("borrowers", borrowers);
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
					.getRequestDispatcher("/a_viewborrowers.jsp");
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
		Borrower borrower = new Borrower();
		
		switch (reqUrl) {
		case "/addBorrower":
			borrower.setName(request.getParameter("borrowerName"));
			if (request.getParameter("borrowerAddress") != null){
				borrower.setAddress(request.getParameter("borrowerAddress"));
			}else{}
			if (request.getParameter("borrowerPhone") != null){
				borrower.setPhone(request.getParameter("borrowerPhone"));
			}else{}
			try {
				adminService.saveBorrower(borrower);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "/editBorrower":
			borrower.setName(request.getParameter("borrowerName"));
			if (request.getParameter("borrowerAddress") != null){
				borrower.setAddress(request.getParameter("borrowerAddress"));
			}else{}
			if (request.getParameter("borrowerPhone") != null){
				borrower.setPhone(request.getParameter("borrowerPhone"));
			}else{}
			borrower.setCardNo(Integer.parseInt(request
					.getParameter("borrowerId")));
			try {
				adminService.saveBorrower(borrower);
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
				.getRequestDispatcher("/a_viewborrowers.jsp");
		rd.forward(request, response);
	}

}
