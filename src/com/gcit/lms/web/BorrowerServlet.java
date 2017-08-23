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

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.service.AdminService;
import com.gcit.lms.service.BorrowerService;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet({ "/checkOutBook", "/returnBook", "/borrowerLogin", "/pageBookLoansByUser" })
public class BorrowerServlet extends HttpServlet { // deleteAuthor is going to DOGet  because we didn't specify POST
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BorrowerServlet() {
		super();
	}

	AdminService adminService = new AdminService();
	BorrowerService service = new BorrowerService();
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String reqUrl = request.getRequestURI().substring(
				request.getContextPath().length(),
				request.getRequestURI().length());
		Borrower borrower = new Borrower();
		BookLoan bookLoan = new BookLoan();
		String message = "";
		Boolean returning = Boolean.FALSE;
		Boolean checkOut = Boolean.FALSE;
		Integer cardNo;
		Integer branchId;
		Integer bookId;
		switch (reqUrl) {
		
		case "/pageBookLoansByUser":
			if (request.getParameter("pageNo") != null
					&& !request.getParameter("pageNo").isEmpty()) {
				Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
				cardNo = Integer.parseInt(request.getParameter("CardNo"));
				
				try {
					List<BookLoan> bookloans = adminService.getAllDueBookLoans(pageNo,cardNo);
					request.setAttribute("bookloans", bookloans);
					//request.setAttribute("pageNo", pageNo);
					request.setAttribute("searchString", "");
					//request.setAttribute("pages", pages);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			break;
			
			case "/checkOutBook":
				
				
				 cardNo = Integer.parseInt(request.getParameter("cardNo"));
				 branchId = Integer.parseInt(request.getParameter("branchId"));
				 bookId = Integer.parseInt(request.getParameter("bookId"));
				
				try {
					borrower = service.getBorrowerByPK(cardNo);
					service.checkOutBook(bookId, branchId, cardNo);
					message = borrower.getName()+". Book borrowed successfully";
					checkOut = Boolean.TRUE;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case "/returnBook":
				 cardNo = Integer.parseInt(request.getParameter("cardNo"));
				 branchId = Integer.parseInt(request.getParameter("branchId"));
				 bookId = Integer.parseInt(request.getParameter("bookId"));
				 String dateout = request.getParameter("dateOut");
				 dateout = dateout.replaceAll("T", " ");
				 //System.out.println(dateout);
				 dateout = dateout.substring(0, dateout.length()-2); // removing microseconds
				 //System.out.println(dateout);
				try {
					bookLoan.setBook(adminService.getBookByPK(bookId));
					bookLoan.setBorrower(adminService.getBorrowerByPK(cardNo));
					bookLoan.setBranch(adminService.getBranchByPK(branchId));
					bookLoan.setDateOut(dateout);
					service.returnBook(bookLoan);
					
					
					returning = Boolean.TRUE;
					borrower = service.getBorrowerByPK(cardNo);
					message = "Thank you "+borrower.getName()+"! Book returned successfully!";
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
		}
		
		if (checkOut){
			request.setAttribute("message", message);
			RequestDispatcher rd = request
					.getRequestDispatcher("/b_pickaction.jsp");
			rd.forward(request, response);
		}
		
		else if (returning){
			request.setAttribute("message", message);
			RequestDispatcher rd = request
					.getRequestDispatcher("/b_viewbookloans.jsp");
			rd.forward(request, response);
		}
		
		
		
		
		
			
			
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String reqUrl = request.getRequestURI().substring(
				request.getContextPath().length(),
				request.getRequestURI().length());
		Borrower borrower = new Borrower();
		String message = "";
		Boolean nullUser = Boolean.TRUE;
		
		switch (reqUrl) {
		case "/borrowerLogin":
			Integer cardNo = Integer.parseInt(request.getParameter("cardNo"));
			try {
				Boolean cardIsValid  = service.isValidCardNo(cardNo);
				
				
				if (cardIsValid){
					borrower = service.getBorrowerByPK(cardNo); 
					message = borrower.getName();
					nullUser = Boolean.FALSE;
				}
				else{
					message="The entered card number is not valid.  Try again!";
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
			
		default:
			break;
		}
		request.setAttribute("message", message);
		if (nullUser){
			RequestDispatcher rd = request
					.getRequestDispatcher("/borrower.jsp");
			rd.forward(request, response);
		}
		RequestDispatcher rd = request
				.getRequestDispatcher("/b_pickaction.jsp");
		rd.forward(request, response);
		 
		
		
	}

}
