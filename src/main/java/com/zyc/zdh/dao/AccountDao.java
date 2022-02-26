package com.zyc.zdh.dao;

import java.util.List;

import com.zyc.zdh.entity.User;
import org.apache.ibatis.annotations.*;


/**
 * ClassName: AccountDao
 * @author zyc-admin
 * @date 2018年2月6日
 * @Description: TODO
 */
public interface AccountDao {

	@Select(value="select * from account_info where user_name=#{username} and user_password=#{password}")
	@Results({@Result(column = "id",property = "id"),
			@Result(column = "user_name",property = "username"),
			@Result(column = "user_password",property = "password"),
			@Result(column = "email",property = "email"),
			@Result(column = "is_use_email",property = "is_use_email"),
			@Result(column = "phone",property = "phone"),
			@Result(column = "is_use_phone",property = "is_use_phone"),
			@Result(column = "image_url",property = "imageUrl")
	})
	public User findByPw(User user);

	@Select(value="select * from account_info where user_password=#{password}")
	@Results({@Result(column="id",property="id"),
			@Result(column="user_name",property="username"),
			@Result(column="user_password",property="password"),
			@Result(column="email",property="email"),
			@Result(column="is_use_email",property="is_use_email"),
			@Result(column="phone",property="phone"),
			@Result(column="is_use_phone",property="is_use_phone"),
			@Result(column = "head_portraits_url",property = "headPortraitsUrl")
	})
	public List<User> findList(User user);

	@Select(value="select * from account_info where user_name=#{username}")
	@Results({@Result(column="id",property="id"),
			@Result(column="user_name",property="username"),
			@Result(column="user_password",property="password"),
			@Result(column="email",property="email"),
			@Result(column="is_use_email",property="is_use_email"),
			@Result(column="phone",property="phone"),
			@Result(column="is_use_phone",property="is_use_phone")
	})
	public List<User> findByUsername(User user);


	@Insert({ "insert into account_info(user_name, user_password,email) values(#{username}, #{password},#{email})" })
	public int insert(User user);

	@Update({"update account_info set user_name=#{username},user_password=#{password},email=#{email} ,is_use_email=#{is_use_email},phone=#{phone},is_use_phone=#{is_use_phone} where id =#{id}"})
	public int update(User user);


	@Select("select * from account_info where id=#{id}")
	@Results({@Result(column="id",property="id"),
			@Result(column="user_name",property="username"),
			@Result(column="user_password",property="password"),
			@Result(column="email",property="email"),
			@Result(column="is_use_email",property="is_use_email"),
			@Result(column="phone",property="phone"),
			@Result(column="is_use_phone",property="is_use_phone")
	})
	public User selectByPrimaryKey(@Param("id") String id) ;

}
