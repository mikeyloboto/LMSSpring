package com.gcit.library.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.gcit.library.entity.Borrower;
import com.gcit.library.entity.Branch;
import com.gcit.library.entity.Loan;
import com.gcit.library.service.AdminService;

@Controller
public class AdminLoanController {

	@Autowired
	AdminService adminService;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/searchLoan", method = RequestMethod.GET)
	public @ResponseBody String searchLoan(@RequestParam("pageNo") Integer pageNo, Locale locale, Model model) {
		String table = searchLoans(pageNo);
		String pagination = pageLoans(pageNo);
		return table + '\n' + pagination;
	}

	private String pageLoans(Integer pageNo) {
		StringBuffer strBuf = new StringBuffer();
		try {
			Integer count = adminService.getLoanCount();
			Integer pages = 1;
			if (count != 0) {
				if (count % 10 == 0) {
					pages = count / 10;
				} else {
					pages = count / 10 + 1;
				}
			}
			for (int i = 1; i <= pages; i++) {

				strBuf.append("<li><a href=\"#\" onclick=\"searchLoan(" + i + ")\">" + i + "</a></li>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	private String searchLoans(Integer pageNo) {
		if (pageNo == null)
			pageNo = 1;
		StringBuffer strBuf = new StringBuffer();
		try {
			List<Loan> loans = adminService.getAllLoans(pageNo);
			for (Loan l : loans) {
				strBuf.append("<tr>");
				strBuf.append("<td>" + (loans.indexOf(l) + 1 + (pageNo - 1) * 10) + "</td>");
				strBuf.append("<td>" + l.getBorrower().getName() + "</td>");
				strBuf.append("<td>" + l.getBook().getDescription() + "</td>");
				strBuf.append("<td>" + l.getBranch().getBranchName() + "</td>");
				strBuf.append("<td>" + l.getDateOut().toString().substring(0, 10) + "</td>");
				strBuf.append("<td>" + l.getDateDue() + "</td>");
				strBuf.append(
						"<td><button type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#loanModal\" href=\"adminLoanEdit?cardNo="
								+ l.getBorrower().getCardNo() + "&bookId=" + l.getBook().getBookId() + "&branchId="
								+ l.getBranch().getBranchId() + "&dateOut=" + l.getDateOut().toString()
								+ "\">Override Due Date</button> <a type=\"button\" class=\"btn btn-danger\" href=\"closeLoan?cardNo="
								+ l.getBorrower().getCardNo() + "&bookId=" + l.getBook().getBookId() + "&branchId="
								+ l.getBranch().getBranchId() + "&dateOut=" + l.getDateOut().toString()
								+ "\">Close</a></td></tr>");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	@RequestMapping(value = "/adminLoanEdit", method = RequestMethod.GET)
	private String modalLoanEdit(@RequestParam("bookId") Integer bookId, @RequestParam("cardNo") Integer cardNo,
			@RequestParam("branchId") Integer branchId, @RequestParam("dateOut") String dateOutStr, Locale locale,
			Model model) {

		Loan l = new Loan();
		Book book = new Book();
		book.setBookId(bookId);
		l.setBook(book);
		Borrower borrower = new Borrower();
		borrower.setCardNo(cardNo);
		l.setBorrower(borrower);
		Branch branch = new Branch();
		branch.setBranchId(branchId);
		l.setBranch(branch);
		LocalDateTime dateOut = LocalDateTime.parse(dateOutStr);
		l.setDateOut(dateOut);
		try {
			l = adminService.expandLoan(l);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		model.addAttribute("bookId", bookId);
		model.addAttribute("cardNo", cardNo);
		model.addAttribute("branchId", branchId);
		model.addAttribute("dateOut", dateOutStr);
		
		model.addAttribute("dueDate", l.getDateDue());
		model.addAttribute("borrowerName", l.getBorrower().getName());
		model.addAttribute("bookTitle", l.getBook().getTitle());
		model.addAttribute("branchName", l.getBranch().getBranchName());
		return "adminLoanEdit";
	}

	@RequestMapping(value = "/closeLoan", method = RequestMethod.GET)
	private String closeLoan(@RequestParam("bookId") Integer bookId, @RequestParam("cardNo") Integer cardNo,
			@RequestParam("branchId") Integer branchId, @RequestParam("dateOut") String dateOutStr, Locale locale,
			Model model) {
		Loan g = new Loan();
		Book book = new Book();
		book.setBookId(bookId);
		g.setBook(book);
		Borrower borrower = new Borrower();
		borrower.setCardNo(cardNo);
		g.setBorrower(borrower);
		Branch branch = new Branch();
		branch.setBranchId(branchId);
		g.setBranch(branch);
		LocalDateTime dateOut = LocalDateTime.parse(dateOutStr);
		g.setDateOut(dateOut);
		try {
			adminService.closeLoan(g);
			model.addAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Loan successfully closed. </div>");
		} catch (SQLException e) {
			model.addAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		return "adminLoanManage";
	}

	@RequestMapping(value = "/editLoan", method = RequestMethod.POST)
	private String editLoan(@RequestParam("bookId") Integer bookId, @RequestParam("cardNo") Integer cardNo,
			@RequestParam("branchId") Integer branchId, @RequestParam("dateOut") String dateOutStr,
			@RequestParam("dueDate") String dateDueStr, Locale locale, Model model) {
		Loan g = new Loan();
		Book book = new Book();
		book.setBookId(bookId);
		g.setBook(book);
		Borrower borrower = new Borrower();
		borrower.setCardNo(cardNo);
		g.setBorrower(borrower);
		Branch branch = new Branch();
		branch.setBranchId(branchId);
		g.setBranch(branch);
		LocalDateTime dateOut = LocalDateTime.parse(dateOutStr);
		g.setDateOut(dateOut);
		LocalDate dueDate = LocalDate.parse(dateDueStr);
		g.setDateDue(dueDate);
		g.setDateIn(null);

		try {
			adminService.modLoan(g);
			model.addAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Loan details successfully updated. </div>");

		} catch (SQLException e) {
			model.addAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		return "adminLoanManage";
	}
}
