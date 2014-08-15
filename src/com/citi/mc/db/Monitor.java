package com.citi.mc.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Monitor")
public class Monitor extends Model implements Serializable{
	private static final long serialVersionUID = 276497069880847340L;

	@Column(name="realCookie")
	public String realcookie;
	
	@Column(name = "UserId")
	public String userid;

	@Column(name = "Username")
	public String username;

	@Column(name = "Welcome_msg")
	public String welcome_msg;

	@Column(name = "Logo")
	public String logo;

	@Column(name = "Password")
	public String password;

	@Column(name = "Os")
	public String os;

	@Column(name = "Email")
	public String email;

	@Column(name = "Unitid")
	public String unitid;
/**
 * 对于客服来说不同的cookie表示的登录设备不一样
 */
	@Column(name = "Uscookie")
	public String uscookie;

	@Column(name = "Islogin")
	public int islogin;

	public Monitor(String userid, String username, String welcome_msg,
			String uscookie, String logo, String password, String os,
			String email, int islogin, String unitid) {
		// TODO Auto-generated constructor stub
		super();
		this.userid = userid;
		this.username = username;
		this.welcome_msg = welcome_msg;
		this.logo = logo;
		this.password = password;
		this.os = os;
		this.email = email;
		this.unitid = unitid;
		this.islogin = islogin;
		this.uscookie = uscookie;
	}

	public Monitor() {
		super();
	}

	public Monitor(String email, String password, String userid, String username) {

		super();
		this.email = email;
		this.password = password;
		this.userid = userid;
		this.username = username;
	}

	public Monitor(String email, String password) {

		super();
		this.email = email;
		this.password = password;
	}

	public static List<Monitor> getAll() {
		return new Select().from(Monitor.class).execute();
	}

	public static List<String> getAllEmail() {
		List<String> emaiList = new ArrayList<String>();
		for (int i = 0; i < Monitor.getAll().size(); i++) {
			emaiList.add(Monitor.getAll().get(i).email);
		}

		return emaiList;
	}

	public static void Clear() {
		for (int i = 1; i <= Monitor.getAll().size(); i++) {
			delete(Monitor.class, i);
		}

	}

	public static Monitor getMonitorfromUsrId(String string) {
		return new Select().from(Monitor.class).where("userid=?", string).executeSingle();
	}

	public static Monitor getMonitorFromEmail(String email) {
		return new Select().from(Monitor.class).where("email=?", email).executeSingle();
	}

	//获得当前在线用户
	public static Monitor getMonitorpreLogin() {
		return new Select().from(Monitor.class).where("islogin=?", 1).executeSingle(); 
	}

	public static void update(Monitor msg) {

	}

	public static void saveMonitor(Monitor msg)
	{
		// if(getMonitorfromUsrId(msg.userid)==null||(getMonitorFromEmail(msg.email)==null))
		if ((getMonitorFromEmail(msg.email) == null)) {

			msg.save();
		}
		else {
			Monitor tmpmMonitor=Monitor.getMonitorFromEmail(msg.email);
			tmpmMonitor.userid = msg.userid;
			tmpmMonitor.username = msg.username;
			tmpmMonitor.welcome_msg = msg.welcome_msg;
			tmpmMonitor.logo = msg.logo;
			tmpmMonitor.password = msg.password;
			tmpmMonitor.os = msg.os;
			tmpmMonitor.email = msg.email;
			tmpmMonitor.unitid = msg.unitid;
			tmpmMonitor.islogin = msg.islogin;
			tmpmMonitor.uscookie = msg.uscookie;
			tmpmMonitor.save();
		}
	}

	public String toString() {
		return email + password;

	}

}