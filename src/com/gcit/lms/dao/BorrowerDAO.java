package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;

public class BorrowerDAO extends BaseDAO{
	
	public BorrowerDAO(Connection conn) {
		super(conn);
	}

	public void addBorrower(Borrower borrower) throws SQLException{
		save("insert into tbl_borrower(name) values (?)", new Object[] {borrower.getName()});
	}
	
	public Integer addBorrowerWithID(Borrower borrower) throws SQLException{
		return saveWithID("insert into tbl_borrower(name) values (?)", new Object[] {borrower.getName()});
	}
	
	public void updateBorrowerName(Borrower borrower) throws SQLException{
		save("update tbl_borrower set name =? where cardNo = ?", new Object[] {borrower.getName(), borrower.getCardNo()});
	}
	
	public void updateBorrowerAddress(Borrower borrower) throws SQLException{
		save("update tbl_borrower set address =? where cardNo = ?", new Object[] {borrower.getAddress(), borrower.getCardNo()});
	}
	
	public void updateBorrowerAddress(Integer cardNo, String address) throws SQLException{
		save("update tbl_borrower set address =? where cardNo = ?", new Object[] {address,cardNo});
	}
	
	public void updateBorrowerPhone(Borrower borrower) throws SQLException{
		save("update tbl_borrower set phone =? where cardNo = ?", new Object[] {borrower.getPhone(), borrower.getCardNo()});
	}
	
	public void updateBorrowerPhone(Integer cardNo, String phone) throws SQLException{
		save("update tbl_borrower set phone =? where cardNo = ?", new Object[] {phone,cardNo});
	}
	
	public Integer getBorrowersCount() throws SQLException{
		return getCount("select count(*) as COUNT from tbl_borrower", null);
	}
	
	public Integer getBorrowersCountByPk(Integer cardNo) throws SQLException{
		//searchString = "%"+searchString+"%";
		//setPageNo(pageNo);
		return getCount("select count(*) as COUNT from tbl_borrower where cardNo = ?", new Object[]{cardNo});
	}
	
	public Integer getBorrowersCount(String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		//setPageNo(pageNo);
		return getCount("select count(*) as COUNT from tbl_borrower where name like ?", new Object[]{searchString});
	}
	
	@SuppressWarnings("unchecked")
	public List<Borrower> readAllBorrowers(Integer pageNo) throws SQLException{
		setPageNo(pageNo);
		return (List<Borrower>) read("select * from tbl_borrower", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Borrower> readAllBorrowersByName(Integer pageNo, String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		setPageNo(pageNo);
		return (List<Borrower>) read("select * from tbl_borrower where name like ?", new Object[]{searchString});
	}
	
	
	public void deleteBorrower(Borrower borrower) throws SQLException{
		save("delete from tbl_borrower where cardNo = ?", new Object[] {borrower.getCardNo()});
	}
	
	@SuppressWarnings("unchecked")
	public List<Borrower> readAllBorrowers() throws SQLException{
		return (List<Borrower>) read("select * from tbl_borrower", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Borrower> readAllBorrowersByName(String searchString) throws SQLException{
		searchString = "%"+searchString+"%";
		return (List<Borrower>) read("select * from tbl_borrower where name like ?", new Object[]{searchString});
	}
	
	@SuppressWarnings("unchecked")
	public Borrower readBorrowerByPK(Integer cardNo) throws SQLException{
		List<Borrower> borrowers = (List<Borrower>) read("select * from tbl_borrower where cardNo = ?", new Object[]{cardNo});
		if(borrowers!=null){
			return borrowers.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Borrower> extractData(ResultSet rs) throws SQLException {
		List<Borrower> borrowers = new ArrayList<>();
		BookLoanDAO bldao = new BookLoanDAO(conn);
		while(rs.next()){
			Borrower b = new Borrower();
			b.setCardNo(rs.getInt("cardNo"));
			b.setName(rs.getString("name"));
			b.setAddress(rs.getString("address"));
			b.setPhone(rs.getString("phone"));
			//b.setBookLoans((List<BookLoan>) bldao.readFirstLevel("select * from tbl_book_loans where cardNo = ?", new Object[]{b.getCardNo()}));
			borrowers.add(b);
		}
		return borrowers;
	}
	
	@Override
	public List<Borrower> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<Borrower> borrowers = new ArrayList<>();
		while(rs.next()){
			Borrower b = new Borrower();
			b.setCardNo(rs.getInt("cardNo"));
			b.setName(rs.getString("name"));
			b.setAddress(rs.getString("address"));
			b.setPhone(rs.getString("phone"));
			borrowers.add(b);
		}
		return borrowers;
	}


}
