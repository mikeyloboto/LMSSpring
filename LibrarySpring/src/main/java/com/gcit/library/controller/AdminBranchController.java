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
import com.gcit.library.entity.Branch;
import com.gcit.library.service.AdminService;

// @WebServlet({ "/addBook", "/editBook", "/removeBook", "/searchBooks" })
@Controller
public class AdminBranchController {

	@Autowired
	AdminService adminService;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/searchBranch", method = RequestMethod.GET)
	public @ResponseBody String searchBranches(@RequestParam("searchString") String searchString,
			@RequestParam("pageNo") Integer pageNo, Locale locale, Model model) {
		String table = searchBranches(searchString, pageNo);
		String pagination = pageBranches(searchString, pageNo);
		return table + '\n' + pagination;
	}

	private String pageBranches(String searchString, Integer pageNo) {
		StringBuffer strBuf = new StringBuffer();
		try {
			Integer count = adminService.getBranchesFromNameCount(searchString);
			Integer pages = 1;
			if (count != 0) {
				if (count % 10 == 0) {
					pages = count / 10;
				} else {
					pages = count / 10 + 1;
				}
			}
			for (int i = 1; i <= pages; i++) {

				strBuf.append("<li><a href=\"#\" onclick=\"searchBranch(" + i + ")\">" + i + "</a></li>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	private String searchBranches(String searchString, Integer pageNo) {
		if (pageNo == null)
			pageNo = 1;
		StringBuffer strBuf = new StringBuffer();
		try {
			List<Branch> branches = adminService.getBranchesFromName(pageNo, searchString);
			for (Branch a : branches) {
				strBuf.append("<tr><td>" + (branches.indexOf(a) + 1 + (pageNo - 1) * 10) + "</td><td>"
						+ a.getBranchName() + "</td>");
				strBuf.append("<td>" + a.getBranchAddress() + "</td>");

				strBuf.append("<td><button type=\"button\" class=\"btn btn-primary\""
						+ " data-toggle=\"modal\" data-target=\"#branchModal\""
						+ " href=\"adminBranchEdit?branchId=" + a.getBranchId() + "\">Update</button> ");
				strBuf.append("<a type=\"button\" class=\"btn btn-danger\"" + " href=\"removeBranch?branchId="
						+ a.getBranchId() + "\">Delete</a></td></tr>");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	/**
	 * Request for Modal
	 */
	@RequestMapping(value = "/adminBranchAdd", method = RequestMethod.GET)
	public String branchAddModal(Locale locale, Model model) {
		return "adminBranchAdd";
	}

	/**
	 * Request for Modal
	 */
	@RequestMapping(value = "/adminBranchEdit", method = RequestMethod.GET)
	public String branchEditModal(@RequestParam("branchId") Integer branchId, Locale locale, Model model) {
		try {
			Branch br = adminService.getBranchFromID(branchId);
			model.addAttribute("branchName", br.getBranchName());
			model.addAttribute("branchId", br.getBranchId());
			model.addAttribute("branchAddress", br.getBranchAddress());

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "adminBranchEdit";
	}

	@RequestMapping(value = "/removeBranch", method = RequestMethod.GET)
	private String removeBranch(@RequestParam("branchId") Integer branchId, Locale locale, Model model) {
		try {
			adminService.removeBranch(branchId);
			model.addAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"><strong>Success!</strong> Branch successfully deleted. </div>");
		} catch (SQLException e) {
			model.addAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		return "adminBranchManage";
	}

	@RequestMapping(value = "/editBranch", method = RequestMethod.POST)
	private String editBranch(@RequestParam("branchId") Integer branchId, @RequestParam("branchName") String branchName,
			@RequestParam("branchAddress") String branchAddress, Locale locale, Model model) {
		Branch br = new Branch();
		br.setBranchId(branchId);
		br.setBranchName(branchName);
		br.setBranchAddress(branchAddress);

		try {
			adminService.modBranch(br);
			model.addAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Branch details successfully updated. </div>");
		} catch (SQLException e) {
			model.addAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		return "adminBranchManage";
	}

	/**
	 * Adds new author with a provied name
	 */
	@RequestMapping(value = "/addBranch", method = RequestMethod.POST)
	private String addAuthor(@RequestParam("branchName") String branchName,
			@RequestParam("branchAddress") String branchAddress, Locale locale, Model model) {
		Branch br = new Branch();
		br.setBranchName(branchName);
		br.setBranchAddress(branchAddress);

		try {
			adminService.addBranch(br);
			model.addAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Branch successfully added. </div>");
		} catch (SQLException e) {
			model.addAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		return "adminBranchManage";
	}

}
