package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Branch;
import com.gcit.lms.entity.Publisher;

public class BranchDAO extends BaseDAO{
	
	public BranchDAO(Connection conn) {
		super(conn);
	}

	public void addBranch(Branch branch) throws SQLException{
		save("insert into tbl_library_branch(branchName) values (?)", new Object[] {branch.getBranchName()});
	}
	
	public void addBranchWithAddress(Branch branch) throws SQLException{
		save("insert into tbl_library_branch(branchName, branchAddress) values (?, ?)", new Object[] {branch.getBranchName(), branch.getBranchAddress()});
	}
	
	public Integer addBranchWithID(Branch branch) throws SQLException{
		return saveWithID("insert into tbl_library_branch(branchName) values (?)", new Object[] {branch.getBranchName()});
	}
	
	public void updateBranchName(Branch branch) throws SQLException{
		save("update tbl_library_branch set branchName =? where branchId = ?", new Object[] {branch.getBranchName(), branch.getBranchId()});
	}
	
	public void updateBranchAddress(Integer branchId, String branchAddress) throws SQLException{
		save("update tbl_library_branch set branchAddress =? where branchId = ?", new Object[] {branchAddress, branchId});
	}
	
	public void updateBranchNameAndAddress(Branch branch) throws SQLException{
		save("update tbl_library_branch set branchName =?, branchAddress =? where branchId = ?", new Object[] {branch.getBranchName(), branch.getBranchAddress(),branch.getBranchId()});
	}
	
	public Integer getBranchesCount() throws SQLException{
		return getCount("select count(*) as COUNT from tbl_library_branch", null);
	}
	
	public Integer getBranchesCount(String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		//setPageNo(pageNo);
		return getCount("select count(*) as COUNT from tbl_library_branch where branchName like ?", new Object[]{searchString});
	}
	
	@SuppressWarnings("unchecked")
	public List<Branch> readAllBranches(Integer pageNo) throws SQLException{
		setPageNo(pageNo);
		return (List<Branch>) read("select * from tbl_library_branch", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Branch> readAllBranchesByName(Integer pageNo, String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		setPageNo(pageNo);
		return (List<Branch>) read("select * from tbl_library_branch where branchName like ?", new Object[]{searchString});
	}
	
	public void deleteBranch(Branch branch) throws SQLException{
		save("delete from tbl_library_branch where branchId = ?", new Object[] {branch.getBranchId()});
	}
	
	@SuppressWarnings("unchecked")
	public List<Branch> readAllBranchs() throws SQLException{
		return (List<Branch>) read("select * from tbl_library_branch", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Branch> readAllBranchsByBranchName(String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		return (List<Branch>) read("select * from tbl_library_branch where branchName like ?", new Object[]{searchString});
	}
	
	@SuppressWarnings("unchecked")
	public Branch readBranchByPK(Integer branchId) throws SQLException{
		List<Branch> branchs = (List<Branch>) read("select * from tbl_library_branch where branchId = ?", new Object[]{branchId});
		if(branchs!=null){
			return branchs.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Branch> extractData(ResultSet rs) throws SQLException {
		List<Branch> branchs = new ArrayList<>();
		BookDAO bdao = new BookDAO(conn);
		BookLoanDAO bldao = new BookLoanDAO(conn);
		while(rs.next()){
			Branch b = new Branch();
			b.setBranchId(rs.getInt("branchId"));
			b.setBranchName(rs.getString("branchName"));
			b.setBranchAddress(rs.getString("branchAddress"));
			// these two would cause infinite loops
			
			
			//b.setBooks((List<Book>) bdao.readFirstLevel("select * from tbl_book where bookId IN (Select bookId from tbl_book_copies where branchId = ?)", new Object[]{b.getBranchId()}));
			//b.setBookLoans((List<BookLoan>) bldao.readFirstLevel("select * from tbl_book_loans where branchId = ?", new Object[]{b.getBranchId()}));
			branchs.add(b);
		}
		return branchs;
	}
	
	@Override
	public List<Branch> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<Branch> branchs = new ArrayList<>();
		while(rs.next()){
			Branch b = new Branch();
			b.setBranchId(rs.getInt("branchId"));
			b.setBranchName(rs.getString("branchName"));
			b.setBranchAddress(rs.getString("branchAddress"));
			branchs.add(b);
		}
		return branchs;
	}

}
