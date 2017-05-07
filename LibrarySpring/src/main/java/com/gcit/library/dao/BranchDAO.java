package com.gcit.library.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.library.entity.Author;
import com.gcit.library.entity.Book;
import com.gcit.library.entity.Branch;
import com.gcit.library.entity.Publisher;

public class BranchDAO extends BaseDAO implements ResultSetExtractor<List<Branch>>{

	public void addBranch(Branch branch) throws ClassNotFoundException, SQLException {
		template.update("insert into tbl_library_branch (branchName, branchAddress) values (?, ?)",
				new Object[] { branch.getBranchName(), branch.getBranchAddress() });
	}

	public Integer addBranchWithID(Branch branch) throws ClassNotFoundException, SQLException {
		return template.update("insert into tbl_library_branch (branchName, branchAddress) values (?, ?)",
				new Object[] { branch.getBranchName(), branch.getBranchAddress() });
	}

	public void updateBranch(Branch branch) throws ClassNotFoundException, SQLException {
		template.update("update tbl_library_branch set branchName = ?, branchAddress = ? where branchId = ?",
				new Object[] { branch.getBranchName(), branch.getBranchAddress(), branch.getBranchNo() });
	}

	public void deleteBranch(Branch branch) throws ClassNotFoundException, SQLException {
		template.update("delete from tbl_library_branch where branchId = ?", new Object[] { branch.getBranchNo() });
	}

	public void deleteBranch(Integer branchId) throws ClassNotFoundException, SQLException {
		template.update("delete from tbl_library_branch where branchId = ?", new Object[] { branchId });
	}

	@Override
	public List extractData(ResultSet rs) throws SQLException {
		List<Branch> branches = new ArrayList<>();
		while (rs.next()) {
			Branch b = new Branch();
			b.setBranchNo(rs.getInt("branchId"));
			b.setBranchName(rs.getString("branchName"));
			b.setBranchAddress(rs.getString("branchAddress"));
			branches.add(b);
		}
		return branches;
	}

	public Integer readBranchCount() throws ClassNotFoundException, SQLException {
		return readInt("select count(*) as COUNT from tbl_library_branch", null);

	}

	public List<Branch> readAllBranches(Integer pageNo) throws ClassNotFoundException, SQLException {
		setPageNo(pageNo);
		return template.query("select * from tbl_library_branch", this);
	}

	public List<Branch> readBranchesByName(String string, Integer pageNo) throws ClassNotFoundException, SQLException {
		setPageNo(pageNo);
		return template.query("select * from tbl_library_branch where branchName like ?", new Object[]{string}, this);
	}

	public Integer readBranchesCountByName(String string) throws ClassNotFoundException, SQLException {
		return readInt("select count(*) from tbl_library_branch where branchName like ?", new Object[]{string});
	}
	
	public Branch readBranchByID(Integer id) throws ClassNotFoundException, SQLException {
		List<Branch> branches = template.query("select * from tbl_library_branch where branchId = ?", new Object[] { id }, this);
		if (branches != null && !branches.isEmpty()) {
			return branches.get(0);
		}
		return null;
	}



}
