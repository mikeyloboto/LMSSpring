package com.gcit.library.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.library.entity.Author;
import com.gcit.library.entity.Book;
import com.gcit.library.entity.Branch;

public class BookDAO extends BaseDAO implements ResultSetExtractor<List<Book>>{

	public void addBook(Book book) throws ClassNotFoundException, SQLException {
		template.update("insert into tbl_book (title, pubId) values (?, ?)",
				new Object[] { book.getTitle(), book.getPublisher().getPublisherId() });
	}

	public Integer addBookWithID(Book book) throws ClassNotFoundException, SQLException {
		return template.update("insert into tbl_book (title, pubId) values (?, ?)",
				new Object[] { book.getTitle(), book.getPublisher().getPublisherId() });
	}

	public void addBookAuthors(Integer bookId, Integer authorId) throws ClassNotFoundException, SQLException {
		template.update("insert into tbl_book_authors values (?, ?)", new Object[] { bookId, authorId });
	}

	public void updateBook(Book book) throws ClassNotFoundException, SQLException {
		template.update("update tbl_book set title = ? where bookId = ?", new Object[] { book.getTitle(), book.getBookId() });
	}

	public void deleteBook(Book book) throws ClassNotFoundException, SQLException {
		template.update("delete from tbl_book where bookId = ?", new Object[] { book.getBookId() });
	}

	public void deleteBook(Integer bookId) throws ClassNotFoundException, SQLException {
		System.out.println("deleting");
		template.update("delete from tbl_book where bookId = ?", new Object[] { bookId });
	}

//	public Integer getBookCopies(Book book, Branch branch) throws ClassNotFoundException, SQLException {
//		return readInt("select noOfCopies from tbl_book_copies where bookId = ? and branchId = ?",
//				new Object[] { book.getBookId(), branch.getBranchNo() });
//	}

	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException {
		List<Book> books = new ArrayList<>();
		while (rs.next()) {
			Book b = new Book();
			b.setTitle(rs.getString("title"));
			b.setBookId(rs.getInt("bookId"));
			books.add(b);
		}
		return books;
	}

	public List<Book> readAllBooks(Integer pageNo) throws ClassNotFoundException, SQLException {
		setPageNo(pageNo);
		return template.query("select * from tbl_book", this);

	}

	public Book readBookFromId(Integer id) throws ClassNotFoundException, SQLException {
		List<Book> books = read("select * from tbl_book where bookId = ?", new Object[] { id });
		if (books != null && !books.isEmpty()) {
			return books.get(0);
		}
		return null;
	}

	public Integer readBookCount() throws ClassNotFoundException, SQLException {
		return readInt("select count(*) as COUNT from tbl_book", null);
	}

	public void removeBookAuthors(Integer bookId) throws ClassNotFoundException, SQLException {
		template.update("delete from tbl_book_authors where bookId = ?", new Object[] { bookId });
	}

	public void updateBookPublisher(Book book) throws ClassNotFoundException, SQLException {
		template.update("update tbl_book set pubId = ? where bookId = ?",
				new Object[] { book.getPublisher().getPublisherId(), book.getBookId() });
	}

	public Integer readBookCopiesCountInBranch(Branch branch) throws ClassNotFoundException, SQLException {
		return readInt("select count(*) as COUNT from tbl_book_copies where branchId = ? and noOfCopies > 0", new Object[]{branch.getBranchNo()});
	}

	public List<Book> readBookFromName(String string, Integer pageNo) throws ClassNotFoundException, SQLException {
		setPageNo(pageNo);
		return template.query("select * from tbl_book where title like ?", new Object[]{string}, this);
	}

	public Integer readBookCountByName(String string) throws ClassNotFoundException, SQLException {
		return readInt("select count(*) from tbl_book where title like ?", new Object[]{string});
	}


}