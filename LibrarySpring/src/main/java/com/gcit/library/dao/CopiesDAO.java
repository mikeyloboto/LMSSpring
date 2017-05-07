package com.gcit.library.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.library.entity.Book;
import com.gcit.library.entity.Branch;

public class CopiesDAO  extends BaseDAO implements ResultSetExtractor<Map<Book, Integer>>{


//
//	public Map<Book, Integer> readCopiesFirstLevel(Branch branch, Integer pageNo) throws ClassNotFoundException, SQLException {
//		setPageNo(pageNo);
//		Map<Book, Integer> map = new HashMap<>();
//		List<Book> books = template.query(
//				"select * from tbl_book where bookId in (select bookId from tbl_book_copies where branchId = ?)",
//				new Object[] { branch.getBranchNo() }, this);
//		for (Book b : books) {
//			Integer c = getBookCopies(b, branch);
//			map.put(b, c);
//		}
//		return map;
//	}
	
//	public Map<Book, Integer> readCopiesFirstLevelNotZero(Branch branch, Integer pageNo) throws ClassNotFoundException, SQLException {
//		setPageNo(pageNo);
//		Map<Book, Integer> map = new HashMap<>();
//		List<Book> books = read(
//				"select * from tbl_book where bookId in (select bookId from tbl_book_copies where branchId = ? and noOfCopies > 0)",
//				new Object[] { branch.getBranchNo() });
//		for (Book b : books) {
//			Integer c = getBookCopies(b, branch);
//			map.put(b, c);
//		}
//		return map;
//	}
//	
//	public Boolean modCopies(Branch branch, Book book, Integer copies) throws ClassNotFoundException, SQLException {
//		Integer bookExistence = readInt(
//				"select count(*) from tbl_book_copies where branchId = ? and bookId = ?",
//				new Object[] { branch.getBranchNo(), book.getBookId()});
//		if (bookExistence == 0) {
//			// add fresh
//			save("insert into tbl_book_copies (branchId, bookId, noOfCopies) values (?, ?, ?)", new Object[] {branch.getBranchNo(), book.getBookId(), copies});
//		}
//		else
//		{
//			//update
//			save("update tbl_book_copies set noOfCopies = ? where branchId = ? and bookId = ?", new Object[] {copies, branch.getBranchNo(), book.getBookId()});
//		}
//		return Boolean.TRUE;
//	}
	
//	public Boolean incrementCopies(Branch branch, Book book, Integer increment) throws ClassNotFoundException, SQLException {
//		Integer bookExistence = readInt(
//				"select count(*) from tbl_book_copies where branchId = ? and bookId = ?",
//				new Object[] { branch.getBranchNo(), book.getBookId()});
//		if (bookExistence == 0) {
//			// add fresh
//			save("insert into tbl_book_copies (branchId, bookId, noOfCopies) values (?, ?, ?)", new Object[] {branch.getBranchNo(), book.getBookId(), increment});
//		}
//		else
//		{
//			Integer copies = readInt(
//					"select noOfCopies from tbl_book_copies where branchId = ? and bookId = ?",
//					new Object[] { branch.getBranchNo(), book.getBookId()}) + increment;
//			//update
//			template.update("update tbl_book_copies set noOfCopies = ? where branchId = ? and bookId = ?", new Object[] {copies, branch.getBranchNo(), book.getBookId()});
//		}
//		return Boolean.TRUE;
//	
	@Override
	public Map<Book, Integer> extractData(ResultSet arg0) throws SQLException, DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

}
