package com.zyc.zdh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 基础用户类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends PageBase implements Serializable {
	private static final long serialVersionUID = -6409904097473655093L;
	private String id;
	private String username;
	private String password;
	private String email;
	private String is_use_email;
	private String phone;
	private String is_use_phone;
	// 头像url
	private String imageUrl;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

}
