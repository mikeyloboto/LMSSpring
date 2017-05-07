package com.gcit.library.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gcit.library.dao.AuthorDAO;
import com.gcit.library.dao.BookDAO;
import com.gcit.library.dao.BorrowerDAO;
import com.gcit.library.dao.BranchDAO;
import com.gcit.library.dao.CopiesDAO;
import com.gcit.library.dao.GenreDAO;
import com.gcit.library.dao.LoanDAO;
import com.gcit.library.dao.PublisherDAO;
import com.gcit.library.entity.Book;
import com.gcit.library.entity.Branch;

public class LibrarianService {
	
	@Autowired
	AuthorDAO adao;

	@Autowired
	BookDAO bdao;

	@Autowired
	BorrowerDAO bordao;

	@Autowired
	BranchDAO brdao;

	@Autowired
	CopiesDAO cdao;

	@Autowired
	GenreDAO gdao;

	@Autowired
	LoanDAO ldao;

	@Autowired
	PublisherDAO pdao;
	
	@Transactional
	public void modBranch(Branch g) throws SQLException {
		try {
			brdao.updateBranch(g);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Transactional
	public List<Branch> getAllBranches(Integer pageNo) throws SQLException {
		try {
			return brdao.readAllBranches(pageNo);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	public Branch getBranchFromID(Integer id) throws SQLException {
		try {
			return brdao.readBranchByID(id);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	public Integer getBookCount(Branch branch) throws SQLException {
		try {
			return bdao.readBookCopiesCountInBranch(branch);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	public Map<Book, Integer> getAllBooksInBranch(Branch branch, Integer pageNo) throws SQLException {
		try {
			return cdao.readCopiesFirstLevel(branch, pageNo);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	public List<Book> getAllBooks(Integer pageNo) throws SQLException {
		try {
			return bdao.readAllBooks(pageNo);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	public Book getBookFromID(Integer id) throws SQLException {
		try {
			return bdao.readBookFromId(id);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	public Integer getBookCountInBranch(Book book, Branch branch) {
		Integer toRet = 0;
		try {
			toRet = getAllBooksInBranch(branch, null).get(book);
			if (toRet == null)
				toRet = 0;
			return toRet;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	public void updateCopies(Branch br, Book book, Integer copies) throws SQLException {
		try {
			cdao.modCopies(br, book, copies);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Transactional
	public void incrementCopies(Branch br, Book book, Integer increment) throws SQLException {
		try {
			cdao.incrementCopies(br, book, increment);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
