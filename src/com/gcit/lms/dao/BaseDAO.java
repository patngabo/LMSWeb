package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.Statement;

public abstract class BaseDAO {

	public static Connection conn = null;

	public BaseDAO(Connection conn) {
		this.conn = conn;
		//BaseDAO.conn = conn;
	}

	private Integer pageNo = 0;
	private Integer pageSize = 10;

	public void save(String query, Object[] vals) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(query);
		setPstmtObjects(vals, pstmt);
		pstmt.executeUpdate();
	}

	public Integer saveWithID(String query, Object[] vals) throws SQLException {
		PreparedStatement pstmt = null;
		pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		setPstmtObjects(vals, pstmt);
		pstmt.executeUpdate();
		ResultSet rs = pstmt.getGeneratedKeys();
		if (rs.next()) {
			return rs.getInt(1);
		}
		return null;
	}

	public List<?> read(String query, Object[] vals) throws SQLException {
		PreparedStatement pstmt = null;
		if (getPageNo() > 0) {
			int index = (getPageNo() - 1) * 10;
			query += " LIMIT " + index + " , " + getPageSize();
		}
		pstmt = conn.prepareStatement(query);
		setPstmtObjects(vals, pstmt);
		ResultSet rs = pstmt.executeQuery();
		return extractData(rs);
	}

	public abstract List<?> extractData(ResultSet rs) throws SQLException;

	public List<?> readFirstLevel(String query, Object[] vals) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(query);
		setPstmtObjects(vals, pstmt);
		ResultSet rs = pstmt.executeQuery();
		return extractDataFirstLevel(rs);
	}

	public abstract List<?> extractDataFirstLevel(ResultSet rs) throws SQLException;

	public Integer getCount(String query, Object[] vals) throws SQLException {
		PreparedStatement pstmt = null;
		pstmt = conn.prepareStatement(query);
		setPstmtObjects(vals, pstmt);
		ResultSet rs = pstmt.executeQuery();
		// System.out.println(rs);
		// TODO: REMOVE THIS
//		while (rs.next()){
//			System.out.println(rs.getInt("COUNT"));
//		}
		if (rs.next())
			return rs.getInt("COUNT");
		return null;
	}

	private void setPstmtObjects(Object[] vals, PreparedStatement pstmt) throws SQLException {
		if (vals != null) {
			int count = 1;
			for (Object o : vals) {
				pstmt.setObject(count, o);
				count++;
			}
		}
	}

	/**
	 * @return the pageNo
	 */
	public Integer getPageNo() {
		return pageNo;
	}

	/**
	 * @param pageNo
	 *            the pageNo to set
	 */
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
