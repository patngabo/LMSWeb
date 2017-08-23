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


import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.service.AdminService;

/**
 * Servlet implementation class AdminServlet 
 */
@WebServlet({"/editBookLoan", "/pageBookLoans" }) // "/addBookLoan" , "/deleteBookLoan" ,
public class AdminBookLoanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminBookLoanServlet() {
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
		String message = "";
		Boolean isAjax = Boolean.FALSE;
		switch (reqUrl) {
		case "/pageBookLoans":
			if (request.getParameter("pageNo") != null
					&& !request.getParameter("pageNo").isEmpty()) {
				Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
				//Integer pages = Integer.parseInt(request.getParameter("pages"));
				String searchString = request.getParameter("searchString");
				if (searchString == null) {
					searchString = "";
				}
				try {
					List<BookLoan> bookloans = adminService.getAllBookLoans(pageNo,
							searchString);
					request.setAttribute("bookloans", bookloans);
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
					.getRequestDispatcher("/a_viewbookloans.jsp");
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
		BookLoan bookLoan = new BookLoan();
		
		switch (reqUrl) {
//		case "/addBookLoan":
//			bookLoan.setBookLoanName(request.getParameter("bookLoanName"));
//			if (request.getParameter("bookLoanAddress") != null){
//				bookLoan.setBookLoanAddress(request.getParameter("bookLoanAddress"));
//			}else{}
//			if (request.getParameter("bookLoanPhone") != null){
//				bookLoan.setBookLoanPhone(request.getParameter("bookLoanPhone"));
//			}else{}
//			
//			try {
//				adminService.saveBookLoan(bookLoan);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			break;
		case "/editBookLoan":
			try {
				System.out.println(request.getParameter("dateOut"));
				String dateOut  = request.getParameter("dateOut");
				
				dateOut = dateOut.replaceAll("T", " ");
				
				Integer cardNo  =  Integer.parseInt(request.getParameter("cardNo"));
				Integer bookId  =  Integer.parseInt(request.getParameter("bookId"));
				Integer branchId  =  Integer.parseInt(request.getParameter("branchId"));
				
				//System.out.println(bookId + " "+ branchId + " "+ cardNo + " "+ dateOut);
				
				bookLoan = adminService.getBookLoanBy4Pks(bookId,  branchId,  cardNo,  dateOut);
				//System.out.println(bookLoan.getBook().getTitle());
				if (request.getParameter("newDueDate") != null){
					String newDueDate = request.getParameter("newDueDate");
					System.out.println(newDueDate + " 00:00:00");
				adminService.overrideDueDate(bookLoan, newDueDate + " 00:00:00");
				}
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
				.getRequestDispatcher("/a_viewbookloans.jsp");
		rd.forward(request, response);
	}

}
