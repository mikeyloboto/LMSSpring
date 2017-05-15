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

import com.gcit.library.entity.Genre;
import com.gcit.library.service.AdminService;

@Controller
public class AdminGenreController {

	@Autowired
	AdminService adminService;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/searchGenre", method = RequestMethod.GET)
	public @ResponseBody String searchGenre(@RequestParam("searchString") String searchString,
			@RequestParam("pageNo") Integer pageNo, Locale locale, Model model) {
		String table = searchGenres(searchString, pageNo);
		String pagination = pageGenres(searchString, pageNo);
		return table + '\n' + pagination;
	}

	private String pageGenres(String searchString, Integer pageNo) {
		StringBuffer strBuf = new StringBuffer();
		try {
			Integer count = adminService.getGenresFromNameCount(searchString);
			Integer pages = 1;
			if (count != 0) {
				if (count % 10 == 0) {
					pages = count / 10;
				} else {
					pages = count / 10 + 1;
				}
			}
			for (int i = 1; i <= pages; i++) {

				strBuf.append("<li><a href=\"#\" onclick=\"searchGenre(" + i + ")\">" + i + "</a></li>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	private String searchGenres(String searchString, Integer pageNo) {
		if (pageNo == null)
			pageNo = 1;
		StringBuffer strBuf = new StringBuffer();
		try {
			List<Genre> gens = adminService.getGenresFromName(pageNo, searchString);
			for (Genre a : gens) {
				strBuf.append("<tr><td>" + (gens.indexOf(a) + 1 + (pageNo - 1) * 10) + "</td><td>"
						+ a.getGenreName() + "</td>");

				strBuf.append("<td><button type=\"button\" class=\"btn btn-primary\""
						+ " data-toggle=\"modal\" data-target=\"#genreModal\""
						+ " href=\"adminGenreEdit?genreId=" + a.getGenreId() + "&pageNo=" + pageNo
						+ "\">Update</button> ");
				strBuf.append("<a type=\"button\" class=\"btn btn-danger\"" + " href=\"removeGenre?genreId="
						+ a.getGenreId() + "\">Delete</a></td></tr>");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	@RequestMapping(value = "/adminGenreAdd", method = RequestMethod.GET)
	public String genreAddModal(Locale locale, Model model) {
		return "adminGenreAdd";
	}

	@RequestMapping(value = "/adminGenreEdit", method = RequestMethod.GET)
	public String genreEditModal(@RequestParam("genreId") Integer genreId, Locale locale, Model model) {
		try {
			Genre auth = adminService.getGenreFromID(genreId);
			model.addAttribute("genreName", auth.getGenreName());
			model.addAttribute("genreId", auth.getGenreId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "adminGenreEdit";
	}

	@RequestMapping(value = "/removeGenre", method = RequestMethod.GET)
	private String removeGenre(@RequestParam("genreId") Integer genreId, Locale locale, Model model) {
		try {
			adminService.removeGenre(genreId);
			model.addAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"><strong>Success!</strong> Genre successfully deleted. </div>");
		} catch (SQLException e) {
			model.addAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		return "adminGenreManage";
	}

	@RequestMapping(value = "/editGenre", method = RequestMethod.POST)
	private String editGenre(@RequestParam("genreId") Integer genreId, @RequestParam("genreName") String genreName,
			Locale locale, Model model) {
		Genre genre = new Genre();
		genre.setGenreId(genreId);
		genre.setGenreName(genreName);

		try {
			adminService.modGenre(genre);
			model.addAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Genre details successfully updated. </div>");
		} catch (SQLException e) {
			model.addAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		return "adminGenreManage";
	}

	/**
	 * Adds new genre with a provied name
	 */
	@RequestMapping(value = "/addGenre", method = RequestMethod.POST)
	private String addGenre(@RequestParam("genreName") String genreName, Locale locale, Model model) {
		Genre genre = new Genre();
		genre.setGenreName(genreName);

		try {
			adminService.addGenre(genre);
			model.addAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Genre successfully added. </div>");
		} catch (SQLException e) {
			model.addAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		return "adminGenreManage";
	}

}
