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

import com.gcit.library.entity.Borrower;
import com.gcit.library.service.AdminService;

@Controller
public class AdminBorrowerController {

	@Autowired
	AdminService adminService;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/searchBorrower", method = RequestMethod.GET)
	public @ResponseBody String searchBorrower(@RequestParam("searchString") String searchString,
			@RequestParam("pageNo") Integer pageNo, Locale locale, Model model) {
		String table = searchBorrowers(searchString, pageNo);
		String pagination = pageBorrowers(searchString, pageNo);
		return table + '\n' + pagination;
	}

	private String pageBorrowers(String searchString, Integer pageNo) {
		StringBuffer strBuf = new StringBuffer();
		try {
			Integer count = adminService.getBorrowersFromNameCount(searchString);
			Integer pages = 1;
			if (count != 0) {
				if (count % 10 == 0) {
					pages = count / 10;
				} else {
					pages = count / 10 + 1;
				}
			}
			for (int i = 1; i <= pages; i++) {
				strBuf.append("<li><a href=\"#\" onclick=\"searchBorrower(" + i + ")\">" + i + "</a></li>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	private String searchBorrowers(String searchString, Integer pageNo) {
		if (pageNo == null)
			pageNo = 1;
		StringBuffer strBuf = new StringBuffer();
		try {
			List<Borrower> bors = adminService.getBorrowersFromName(pageNo, searchString);
			for (Borrower a : bors) {
				strBuf.append("<tr><td>" + a.getCardNo() + "</td><td>" + a.getName() + "</td>");
				strBuf.append("<td>" + a.getAddress() + "</td><td>" + a.getPhone() + "</td>");

				strBuf.append("<td><button type=\"button\" class=\"btn btn-primary\""
						+ " data-toggle=\"modal\" data-target=\"#borrowerModal\""
						+ " href=\"adminBorrowerEdit?borrowerId=" + a.getCardNo() + "\">Update</button> ");
				strBuf.append("<a type=\"button\" class=\"btn btn-danger\"" + " href=\"removeBorrower?borrowerId="
						+ a.getCardNo() + "\">Delete</a></td></tr>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	@RequestMapping(value = "/adminBorrowerAdd", method = RequestMethod.GET)
	public String borrowerAddModal(Locale locale, Model model) {
		return "adminBorrowerAdd";
	}

	@RequestMapping(value = "/adminBorrowerEdit", method = RequestMethod.GET)
	public String borrowerEditModal(@RequestParam("borrowerId") Integer borrowerId, Locale locale, Model model) {
		try {
			Borrower borrower = adminService.getBorrowerFromID(borrowerId);
			model.addAttribute("borrowerName", borrower.getName());
			model.addAttribute("borrowerId", borrower.getCardNo());
			model.addAttribute("borrowerAddress", borrower.getAddress());
			model.addAttribute("borrowerPhone", borrower.getPhone());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "adminBorrowerEdit";
	}

	@RequestMapping(value = "/removeBorrower", method = RequestMethod.GET)
	private String removeBorrower(@RequestParam("borrowerId") Integer borrowerId, Locale locale, Model model) {
		try {
			adminService.removeBorrower(borrowerId);
			model.addAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"><strong>Success!</strong> Borrower successfully deleted. </div>");
		} catch (SQLException e) {
			model.addAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		return "adminBorrowerManage";
	}

	@RequestMapping(value = "/editBorrower", method = RequestMethod.POST)
	private String editBorrower(@RequestParam("borrowerId") Integer borrowerId,
			@RequestParam("borrowerName") String borrowerName, @RequestParam("borrowerAddress") String borrowerAddress,
			@RequestParam("borrowerPhone") String borrowerPhone, Locale locale, Model model) {
		Borrower borrower = new Borrower();
		borrower.setCardNo(borrowerId);
		borrower.setName(borrowerName);
		borrower.setAddress(borrowerAddress);
		borrower.setPhone(borrowerPhone);

		try {
			adminService.modBorrower(borrower);
			model.addAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Borrower details successfully updated. </div>");
		} catch (SQLException e) {
			model.addAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		return "adminBorrowerManage";
	}

	@RequestMapping(value = "/addBorrower", method = RequestMethod.POST)
	private String addBorrower(@RequestParam("borrowerName") String borrowerName,
			@RequestParam("borrowerAddress") String borrowerAddress,
			@RequestParam("borrowerPhone") String borrowerPhone, Locale locale, Model model) {
		Borrower borrower = new Borrower();
		borrower.setName(borrowerName);
		borrower.setName(borrowerName);
		borrower.setAddress(borrowerAddress);
		borrower.setPhone(borrowerPhone);
		try {
			adminService.addBorrower(borrower);
			model.addAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Borrower successfully added. </div>");
		} catch (SQLException e) {
			model.addAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		return "adminBorrowerManage";
	}

}
