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

import com.gcit.lms.entity.BookCopy;
import com.gcit.lms.entity.Branch;
import com.gcit.lms.service.AdminService;
import com.gcit.lms.service.LibrarianService;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet({"/editBranchLib", "/getBookCopies", "/deleteBookCopy", "/editBookCopy" }) //,"/deleteBranch"  //"/addBranch" , "/pageBranchesLib",
public class LibrarianServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LibrarianServlet() {
		super();
	}

	AdminService adminService = new AdminService();
	LibrarianService librarianService = new LibrarianService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String reqUrl = request.getRequestURI().substring(
				request.getContextPath().length(),
				request.getRequestURI().length());
		Branch branch = new Branch();
		String message = "";
		switch (reqUrl) {
		
//		case "/deleteBranch":
//			if (request.getParameter("branchId") != null
//					&& !request.getParameter("branchId").isEmpty()) {
//				branch.setBranchId(Integer.parseInt(request
//						.getParameter("branchId")));
//
//				try {
//					adminService.deleteBranch(branch);
//					message = "Branch deleted Successfully";
//				} catch (SQLException e) {
//					e.printStackTrace();
//					message = "Author delete failed. Try Again!";
//				}
//			}
//			break;
	
		
			
			
//		case "/pageBranchesLib":
//			if (request.getParameter("pageNo") != null
//					&& !request.getParameter("pageNo").isEmpty()) {
//				Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
//				//Integer pages = Integer.parseInt(request.getParameter("pages"));
//				String searchString = request.getParameter("searchString");
//				if (searchString == null) {
//					searchString = "";
//				}
//				try {
//					List<Branch> branchs = adminService.getAllBranchs(pageNo,
//							searchString);
//					request.setAttribute("branchs", branchs);
//					//request.setAttribute("pageNo", pageNo);
//					request.setAttribute("searchString", "");
//					//request.setAttribute("pages", pages);
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			break;
			
		case "/getBookCopies":
			if (request.getParameter("branchId") != null
					&& !request.getParameter("branchId").isEmpty()) {
				Integer branchId = Integer.parseInt(request.getParameter("branchId"));
				//Integer pages = Integer.parseInt(request.getParameter("pages"));
//				String searchString = request.getParameter("searchString");
//				if (searchString == null) {
//					searchString = "";
//				}
				
				
				try {
					branch  = librarianService.getBranchByPk(branchId);
					List<BookCopy> copies = librarianService.getAllBookCopiesOwnedBy(branch);
					request.setAttribute("copies", copies);
					//request.setAttribute("pageNo", pageNo);
					//request.setAttribute("searchString", "");
					//request.setAttribute("pages", pages);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			break;
			
		case "/deleteBookCopy":
			if (request.getParameter("branchId") != null
					&& !request.getParameter("branchId").isEmpty() && request.getParameter("bookId") != null
							&& !request.getParameter("bookId").isEmpty()) {
				Integer branchId = Integer.parseInt(request.getParameter("branchId"));
				Integer bookId = Integer.parseInt(request.getParameter("bookId"));
				//Integer pages = Integer.parseInt(request.getParameter("pages"));
//				String searchString = request.getParameter("searchString");
//				if (searchString == null) {
//					searchString = "";
//				}
				//System.out.println("yayya, treue!!");
				
				try {
					librarianService.deleteBookCopy( bookId, branchId);
					//List<BookCopy> copies = librarianService.getAllBookCopiesOwnedBy(branch);
					//request.setAttribute("copies", copies);
					//request.setAttribute("branchId", branchId);
					//request.setAttribute("searchString", "");
					//request.setAttribute("pages", pages);
					//message  = "Book Copy Deleted Successfully!";
					System.out.println(message);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			break;

		default:
			break;
		}
		
		
		
		request.setAttribute("message", message);
		RequestDispatcher rd = request
				.getRequestDispatcher("/l_viewbookcopies.jsp");
		rd.forward(request, response);
	
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
		Branch branch = new Branch();
		BookCopy copy = new BookCopy();
		Boolean onCopies = Boolean.FALSE;
		switch (reqUrl) {
//		case "/addBranch":
//			branch.setBranchName(request.getParameter("branchName"));
//			if (request.getParameter("branchAddress") != null){
//				branch.setBranchAddress(request.getParameter("branchAddress"));
//			}else{}
//			
//			try {
//				adminService.saveBranch(branch);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			break;
		case "/editBranchLib":
			branch.setBranchName(request.getParameter("branchName"));
			if (request.getParameter("branchAddress") != null){
				branch.setBranchAddress(request.getParameter("branchAddress"));
			}else{}
			branch.setBranchId(Integer.parseInt(request
					.getParameter("branchId")));
			try {
				adminService.saveBranch(branch);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
			
		case "/editBookCopy":
			try {
				Integer branchId  = Integer.parseInt(request.getParameter("branchId"));
				Integer bookId  = Integer.parseInt(request.getParameter("bookId"));
				copy  = librarianService.getBookCopyByPks(branchId, bookId);
				if (request.getParameter("noOfCopies") !=null){
					Integer noOfCopies  = Integer.parseInt(request.getParameter("noOfCopies"));
					copy.setNoOfCopies(noOfCopies);
					librarianService.saveBookCopy(copy);
					onCopies = Boolean.TRUE;
				}
				
				//Service.saveBranch(branch);
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
		
		 if (onCopies){
			 //System.out.println("tets tes ");
			 RequestDispatcher rd = request
						.getRequestDispatcher("/l_viewbookcopies.jsp");
				rd.forward(request, response);
		 }else{
		RequestDispatcher rd = request
				.getRequestDispatcher("/librarian.jsp");
		rd.forward(request, response);
		 }
	}

}
