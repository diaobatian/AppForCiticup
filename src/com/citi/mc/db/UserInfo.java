package com.citi.mc.db;


import java.io.Serializable;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * 
 * @类名 UserInfo.java
 * @包名 com.example.db
 * @作者 ChunTian  
 * @时间 2013年12月26日 下午3:03:36
 * @Email ChunTian1314@vip.qq.com
 * @版本 V1.0 
 * @功能储蓄用户所有信息
 */
@Table(name = "userInfo")
public class UserInfo extends Model{

	@Column(name = "Cookie")
	public String cookie;
	@Column(name = "City")
	public String city;
	@Column(name = "Province")
	public String province;
	@Column(name = "Isp")
	public String isp;

	public UserInfo() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public static UserInfo getNameId(String cookie) {
		return new Select().from(UserInfo.class).where("Cookie = ?", cookie).executeSingle();
	}
	
	
}
