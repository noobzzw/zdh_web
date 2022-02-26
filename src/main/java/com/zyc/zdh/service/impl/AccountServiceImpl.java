package com.zyc.zdh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zyc.zdh.dao.AccountDao;
import com.zyc.zdh.entity.User;
import com.zyc.zdh.service.AccountService;

import javax.annotation.Resource;

/**
 * ClassName: AccountServiceImpl
 * @author zyc-admin
 * @date 2018年2月6日
 * @Description: TODO
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

	@Resource
	private AccountDao accountDao;

	@Override
	public User findByPw(User user) {
		return accountDao.findByPw(user);
	}

	@Override
	public List<User> findList(User user) {
		// TODO Auto-generated method stub
		@SuppressWarnings("rawtypes")
		Page startPage = PageHelper.startPage(user.getPageNum2(), user.getPageSize());
		List<User> findList = accountDao.findList(user);
		System.out.println("startPage.getTotal()------"+startPage.getTotal());
		return findList;
	}

	@Override
	public List<User> findByUserName(User user) {
		return accountDao.findByUsername(user);
	}

	@Override
	public int insert(User user) {
		return accountDao.insert(user);
	}

	@Override
	public int updateUser(User user) {
		return accountDao.update(user);
	}

	@Override
	public User selectByPrimaryKey(String id) {
		return accountDao.selectByPrimaryKey(id);
	}

}
