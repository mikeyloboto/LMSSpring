package com.gcit.library.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gcit.library.entity.Book;
import com.gcit.library.service.AdminService;

/**
 * Test Controller, will probably have to rewrite alot of that shite
 */
// @WebServlet({ "/addBook", "/editBook", "/removeBook", "/searchBooks" })
@Controller
public class AdminBookController {

	@Autowired
	AdminService adminService;
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	// protected void doGet(HttpServletRequest request, HttpServletResponse
	// response)
	// throws ServletException, IOException {
	// String reqUrl =
	// request.getRequestURI().substring(request.getContextPath().length(),
	// request.getRequestURI().length());
	// String forwardPath = "/adminBookManage.jsp";
	// Boolean isAjax = Boolean.FALSE;
	// switch (reqUrl) {
	//
	// case "/pageAuthors":
	// // pageAuthors(request);
	// forwardPath = "/adminBookManage.jsp";
	// break;
	// case "/searchBooks":
	// // System.out.println("test");
	// String table = searchBooks(request);
	// String pagination = pageBooks(request);
	// // response.setContentType("application/json");
	// // response.setCharacterEncoding("UTF-8");
	// response.getWriter().write(table + '\n' + pagination);
	// // forwardPath = "/viewauthors.jsp";
	// isAjax = Boolean.TRUE;
	// break;
	// case "/removeBook":
	// removeBook(request);
	// break;
	// default:
	// break;
	// }
	// if (!isAjax) {
	// RequestDispatcher rd = request.getRequestDispatcher(forwardPath);
	// rd.forward(request, response);
	// }
	// }

	@RequestMapping(value = "/searchBooks", method = RequestMethod.GET)
	public @ResponseBody String searchBook(@RequestParam("searchString") String searchString, @RequestParam("pageNo") Integer pageNo,
			Locale locale, Model model) {
		String table = searchBooks(searchString, pageNo);
		String pagination = pageBooks(searchString, pageNo);
		model.addAttribute("table", table);
		model.addAttribute("pagination", pagination);
		System.out.println(searchString);
		return table + '\n' + pagination;
	}

	private String pageBooks(String searchString, Integer pageNo) {
		StringBuffer strBuf = new StringBuffer();
		try {
			Integer count = adminService.getBooksFromNameCount(searchString);
			Integer pages = 1;
			if (count != 0) {
				if (count % 10 == 0) {
					pages = count / 10;
				} else {
					pages = count / 10 + 1;
				}
			}
			for (int i = 1; i <= pages; i++) {
				strBuf.append("<li><a href=\"#\" onclick=\"searchBook(" + i + ")\">" + i + "</a></li>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	private String searchBooks(String searchString, Integer pageNo) {
		if (pageNo == null)
			pageNo = 1;
		StringBuffer strBuf = new StringBuffer();
		try {
			List<Book> books = adminService.getBooksFromName(pageNo, searchString);
			System.out.println(books.size());
			for (Book a : books) {
				strBuf.append("<tr><td>" + (books.indexOf(a) + 1 + (pageNo - 1) * 10) + "</td><td>" + a.getDescription()
						+ "</td>");
				strBuf.append("<td>" + a.getGenreList() + "</td><td>" + a.getPublisher().getPublisherName() + "</td>");

				strBuf.append("<td><button type=\"button\" class=\"btn btn-primary\""
						+ " data-toggle=\"modal\" data-target=\"#editBookModal\"" + " href=\"adminBookEdit?bookId="
						+ a.getBookId() + "&pageNo=" + pageNo + "\">Update</button> ");
				strBuf.append("<a type=\"button\" class=\"btn btn-danger\"" + " href=\"removeBook?bookId="
						+ a.getBookId() + "\">Delete</a></td></tr>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}
	//
	// /**
	// * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	// * response)
	// */
	// protected void doPost(HttpServletRequest request, HttpServletResponse
	// response)
	// throws ServletException, IOException {
	// String reqUrl =
	// request.getRequestURI().substring(request.getContextPath().length(),
	// request.getRequestURI().length());
	// String forwardPath = "/adminBookManage.jsp";
	// switch (reqUrl) {
	//
	// case "/addBook":
	// addBook(request);
	// break;
	// case "/editBook":
	// editBook(request);
	// break;
	// case "/removeBook":
	// removeBook(request);
	// break;
	// default:
	// break;
	// }
	// RequestDispatcher rd = request.getRequestDispatcher(forwardPath);
	// rd.forward(request, response);
	// }
	//
	// private void removeBook(HttpServletRequest request) {
	// // TODO Auto-generated method stub
	// AdminService service = new AdminService();
	// try {
	// Integer bookId = Integer.parseInt((String)
	// request.getParameter("bookId"));
	// service.removeBook(bookId);
	// request.setAttribute("message",
	// "<div class=\"alert alert-success\" role=\"alert\">
	// <strong>Success!</strong> Book successfully deleted. </div>");
	// } catch (NumberFormatException e) {
	// request.setAttribute("message",
	// "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong>
	// Something went wrong. </div>");
	// e.printStackTrace();
	// } catch (SQLException e) {
	// request.setAttribute("message",
	// "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong>
	// Something went wrong. </div>");
	// e.printStackTrace();
	// }
	// }
	//
	// private void editBook(HttpServletRequest request) {
	// Book book = new Book();
	// book.setBookId(Integer.parseInt(request.getParameter("bookId")));
	// book.setTitle(request.getParameter("bookName"));
	// String[] authorIds = request.getParameterValues("authors");
	// List<Author> authors = new ArrayList<>();
	// if (authorIds != null && authorIds.length > 0) {
	// for (int i = 0; i < authorIds.length; i++) {
	// Author a = new Author();
	// a.setAuthorId(Integer.parseInt(authorIds[i]));
	// System.out.println("adding authorId " + authorIds[i]);
	// authors.add(a);
	// }
	// }
	//
	// String[] genreIds = request.getParameterValues("genres");
	// List<Genre> genres = new ArrayList<>();
	// if (genreIds != null && genreIds.length > 0) {
	//
	// for (int i = 0; i < genreIds.length; i++) {
	// Genre g = new Genre();
	// g.setGenreId(Integer.parseInt(genreIds[i]));
	// genres.add(g);
	// }
	// }
	// Publisher pub = new Publisher();
	// pub.setPublisherId(Integer.parseInt(request.getParameter("publisher")));
	//
	// book.setAuthors(authors);
	// book.setGenres(genres);
	// book.setPublisher(pub);
	//
	// AdminService service = new AdminService();
	// try {
	// service.modBook(book);
	// } catch (SQLException e) {
	// request.setAttribute("message",
	// "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong>
	// Something went wrong. </div>");
	// e.printStackTrace();
	// }
	// request.setAttribute("message",
	// "<div class=\"alert alert-success\" role=\"alert\">
	// <strong>Success!</strong> Book details successfully updated. </div>");
	// }
	//
	// private void addBook(HttpServletRequest request) {
	// // TODO Auto-generated method stub
	// Book book = new Book();
	// book.setTitle(request.getParameter("bookName"));
	// String[] authorIds = request.getParameterValues("authors");
	// List<Author> authors = new ArrayList<>();
	// if (authorIds != null && authorIds.length > 0) {
	// for (int i = 0; i < authorIds.length; i++) {
	// Author a = new Author();
	// a.setAuthorId(Integer.parseInt(authorIds[i]));
	// System.out.println("adding authorId " + authorIds[i]);
	// authors.add(a);
	// }
	// }
	//
	// String[] genreIds = request.getParameterValues("genres");
	// List<Genre> genres = new ArrayList<>();
	// if (genreIds != null && genreIds.length > 0) {
	//
	// for (int i = 0; i < genreIds.length; i++) {
	// Genre g = new Genre();
	// g.setGenreId(Integer.parseInt(genreIds[i]));
	// genres.add(g);
	// }
	// }
	// Publisher pub = new Publisher();
	// pub.setPublisherId(Integer.parseInt(request.getParameter("publisher")));
	//
	// book.setAuthors(authors);
	// book.setGenres(genres);
	// book.setPublisher(pub);
	//
	// AdminService service = new AdminService();
	// try {
	// service.addBook(book);
	// } catch (SQLException e) {
	// request.setAttribute("message",
	// "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong>
	// Something went wrong. </div>");
	// e.printStackTrace();
	// }
	// request.setAttribute("message",
	// "<div class=\"alert alert-success\" role=\"alert\">
	// <strong>Success!</strong> Book successfully added. </div>");
	// }

}
