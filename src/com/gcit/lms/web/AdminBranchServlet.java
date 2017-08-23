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


import com.gcit.lms.entity.Branch;
import com.gcit.lms.service.AdminService;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet({"/addBranch" ,"/deleteBranch","/editBranch", "/pageBranches" })
public class AdminBranchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminBranchServlet() {
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
		Branch branch = new Branch();
		String message = "";
		Boolean isAjax = Boolean.FALSE;
		switch (reqUrl) {
		
		case "/deleteBranch":
			if (request.getParameter("branchId") != null
					&& !request.getParameter("branchId").isEmpty()) {
				branch.setBranchId(Integer.parseInt(request
						.getParameter("branchId")));

				try {
					adminService.deleteBranch(branch);
					message = "Branch deleted Successfully";
				} catch (SQLException e) {
					e.printStackTrace();
					message = "Author delete failed. Try Again!";
				}
			}
			break;
	
		
			
			
		case "/pageBranches":
			if (request.getParameter("pageNo") != null
					&& !request.getParameter("pageNo").isEmpty()) {
				Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
				//Integer pages = Integer.parseInt(request.getParameter("pages"));
				String searchString = request.getParameter("searchString");
				if (searchString == null) {
					searchString = "";
				}
				try {
					List<Branch> branchs = adminService.getAllBranchs(pageNo,
							searchString);
					request.setAttribute("branchs", branchs);
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
					.getRequestDispatcher("/a_viewbranches.jsp");
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
		Branch branch = new Branch();
		
		switch (reqUrl) {
		case "/addBranch":
			branch.setBranchName(request.getParameter("branchName"));
			if (request.getParameter("branchAddress") != null){
				branch.setBranchAddress(request.getParameter("branchAddress"));
			}else{}
			
			try {
				adminService.saveBranch(branch);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "/editBranch":
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
				.getRequestDispatcher("/a_viewbranches.jsp");
		rd.forward(request, response);
	}

}
