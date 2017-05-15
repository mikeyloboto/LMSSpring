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
import com.gcit.library.entity.Publisher;
import com.gcit.library.service.AdminService;

@Controller
public class AdminPublisherController {

	@Autowired
	AdminService adminService;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/searchPublisher", method = RequestMethod.GET)
	public @ResponseBody String searchPublishers(@RequestParam("searchString") String searchString,
			@RequestParam("pageNo") Integer pageNo, Locale locale, Model model) {
		String table = searchPublishers(searchString, pageNo);
		String pagination = pagePublishers(searchString, pageNo);
		return table + '\n' + pagination;
	}

	private String pagePublishers(String searchString, Integer pageNo) {
		StringBuffer strBuf = new StringBuffer();
		try {
			Integer count = adminService.getPublishersFromNameCount(searchString);
			Integer pages = 1;
			if (count != 0) {
				if (count % 10 == 0) {
					pages = count / 10;
				} else {
					pages = count / 10 + 1;
				}
			}
			for (int i = 1; i <= pages; i++) {
				strBuf.append("<li><a href=\"#\" onclick=\"searchPublisher(" + i + ")\">" + i + "</a></li>");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	private String searchPublishers(String searchString, Integer pageNo) {
		if (pageNo == null)
			pageNo = 1;
		StringBuffer strBuf = new StringBuffer();
		try {
			List<Publisher> publishers = adminService.getPublishersFromName(pageNo, searchString);
			for (Publisher a : publishers) {
				strBuf.append("<tr><td>" + (publishers.indexOf(a) + 1 + (pageNo - 1) * 10) + "</td><td>"
						+ a.getPublisherName() + "</td>");
				strBuf.append("<td>" + a.getPublisherAddress() + "</td><td>" + a.getPublisherPhone() + "</td>");

				strBuf.append("<td><button type=\"button\" class=\"btn btn-primary\""
						+ " data-toggle=\"modal\" data-target=\"#publisherModal\""
						+ " href=\"adminPublisherEdit?publisherId=" + a.getPublisherId() + "\">Update</button> ");
				strBuf.append("<a type=\"button\" class=\"btn btn-danger\"" + " href=\"removePublisher?publisherId="
						+ a.getPublisherId() + "\">Delete</a></td></tr>");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	/**
	 * Request for Modal
	 */
	@RequestMapping(value = "/adminPublisherAdd", method = RequestMethod.GET)
	public String publisherAddModal(Locale locale, Model model) {
		return "adminPublisherAdd";
	}

	/**
	 * Request for Modal
	 */
	@RequestMapping(value = "/adminPublisherEdit", method = RequestMethod.GET)
	public String publisherEditModal(@RequestParam("publisherId") Integer publisherId, Locale locale, Model model) {
		try {
			Publisher br = adminService.getPublisherFromID(publisherId);
			model.addAttribute("publisherName", br.getPublisherName());
			model.addAttribute("publisherId", br.getPublisherId());
			model.addAttribute("publisherAddress", br.getPublisherAddress());
			model.addAttribute("publisherPhone", br.getPublisherPhone());

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "adminPublisherEdit";
	}

	@RequestMapping(value = "/removePublisher", method = RequestMethod.GET)
	private String removePublisher(@RequestParam("publisherId") Integer publisherId, Locale locale, Model model) {
		try {
			adminService.removePublisher(publisherId);
			model.addAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"><strong>Success!</strong> Publisher successfully deleted. </div>");
		} catch (SQLException e) {
			model.addAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		return "adminPublisherManage";
	}

	@RequestMapping(value = "/editPublisher", method = RequestMethod.POST)
	private String editPublisher(@RequestParam("publisherId") Integer publisherId,
			@RequestParam("publisherName") String publisherName,
			@RequestParam("publisherAddress") String publisherAddress, @RequestParam("publisherPhone") String publisherPhone, Locale locale, Model model) {
		Publisher p = new Publisher();
		p.setPublisherId(publisherId);
		p.setPublisherName(publisherName);
		p.setPublisherAddress(publisherAddress);
		p.setPublisherPhone(publisherPhone);

		try {
			adminService.modPublisher(p);
			model.addAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Publisher details successfully updated. </div>");
		} catch (SQLException e) {
			model.addAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		return "adminPublisherManage";
	}

	/**
	 * Adds new author with a provied name
	 */
	@RequestMapping(value = "/addPublisher", method = RequestMethod.POST)
	private String addAuthor(@RequestParam("publisherName") String publisherName,
			@RequestParam("publisherAddress") String publisherAddress, @RequestParam("publisherPhone") String publisherPhone, Locale locale, Model model) {
		Publisher p = new Publisher();
		p.setPublisherName(publisherName);
		p.setPublisherAddress(publisherAddress);
		p.setPublisherPhone(publisherPhone);

		try {
			adminService.addPublisher(p);
			model.addAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Publisher successfully added. </div>");
		} catch (SQLException e) {
			model.addAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		return "adminPublisherManage";
	}

}
