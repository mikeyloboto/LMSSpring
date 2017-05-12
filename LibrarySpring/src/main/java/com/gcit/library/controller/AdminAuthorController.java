package com.gcit.library.controller;

import java.sql.SQLException;
import java.util.ArrayList;
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

import com.gcit.library.entity.Author;
import com.gcit.library.service.AdminService;

// @WebServlet({ "/addBook", "/editBook", "/removeBook", "/searchBooks" })
@Controller
public class AdminAuthorController {

	@Autowired
	AdminService adminService;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/searchAuthor", method = RequestMethod.GET)
	public @ResponseBody String searchAuthor(@RequestParam("searchString") String searchString,
			@RequestParam("pageNo") Integer pageNo, Locale locale, Model model) {
		String table = searchAuthors(searchString, pageNo);
		String pagination = pageAuthors(searchString, pageNo);
		return table + '\n' + pagination;
	}

	private String pageAuthors(String searchString, Integer pageNo) {
		StringBuffer strBuf = new StringBuffer();
		try {
			Integer count = adminService.getAuthorsFromNameCount(searchString);
			Integer pages = 1;
			if (count != 0) {
				if (count % 10 == 0) {
					pages = count / 10;
				} else {
					pages = count / 10 + 1;
				}
			}
			for (int i = 1; i <= pages; i++) {

				strBuf.append("<li><a href=\"#\" onclick=\"searchAuthor(" + i + ")\">" + i + "</a></li>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	private String searchAuthors(String searchString, Integer pageNo) {
		if (pageNo == null)
			pageNo = 1;
		StringBuffer strBuf = new StringBuffer();
		try {
			List<Author> auths = adminService.getAuthorsFromName(pageNo, searchString);
			for (Author a : auths) {
				strBuf.append("<tr><td>" + (auths.indexOf(a) + 1 + (pageNo - 1) * 10) + "</td><td>" + a.getAuthorName()
						+ "</td>");
				strBuf.append("<td><button type=\"button\" class=\"btn btn-primary\""
						+ " data-toggle=\"modal\" data-target=\"#authorModal\""
						+ " href=\"adminAuthorEdit?authorId=" + a.getAuthorId() + "\">Update</button> ");
				strBuf.append("<a type=\"button\" class=\"btn btn-danger\"" + " href=\"removeAuthor?authorId="
						+ a.getAuthorId() + "\">Delete</a></td></tr>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	@RequestMapping(value = "/adminAuthorAdd", method = RequestMethod.GET)
	public String authorAddModal(Locale locale, Model model) {
		return "adminAuthorAdd";
	}

	@RequestMapping(value = "/adminAuthorEdit", method = RequestMethod.GET)
	public String authorEditModal(@RequestParam("authorId") Integer authorId, Locale locale, Model model) {
		try {
			Author auth = adminService.getAuthorFromID(authorId);
			model.addAttribute("authorName", auth.getAuthorName());
			model.addAttribute("authorId", auth.getAuthorId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "adminAuthorEdit";
	}

	@RequestMapping(value = "/removeAuthor", method = RequestMethod.GET)
	private String removeAuthor(@RequestParam("authorId") Integer authorId, Locale locale, Model model) {
		// TODO Auto-generated method stub
		try {
			adminService.removeAuthor(authorId);
			model.addAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"><strong>Success!</strong> Author successfully deleted. </div>");
		} catch (SQLException e) {
			model.addAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		return "adminAuthorManage";
	}

	@RequestMapping(value = "/editAuthor", method = RequestMethod.POST)
	private String editAuthor(@RequestParam("authorId") Integer authorId, @RequestParam("authorName") String authorName,
			Locale locale, Model model) {
		Author author = new Author();
		author.setAuthorId(authorId);
		author.setAuthorName(authorName);

		try {
			adminService.modAuthor(author);
			model.addAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Author details successfully updated. </div>");
		} catch (SQLException e) {
			model.addAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		return "adminAuthorManage";
	}

	@RequestMapping(value = "/addAuthor", method = RequestMethod.POST)
	private String addAuthor(@RequestParam("authorName") String authorName, Locale locale, Model model) {
		// TODO Auto-generated method stub
		Author author = new Author();
		author.setAuthorName(authorName);

		try {
			adminService.addAuthor(author);
			model.addAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Author successfully added. </div>");
		} catch (SQLException e) {
			model.addAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		return "adminAuthorManage";
	}

}
