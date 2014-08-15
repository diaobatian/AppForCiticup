package com.citi.mc.bean;

import java.io.Serializable;


/**
 * 应用程序更新实体类
 */
public class Update implements Serializable{
	
	private static final long serialVersionUID = -596389168756548242L;
	public final static String UTF8 = "UTF-8";
	public final static String NODE_ROOT = "mobilechat";
	
	private String versionCode;
	private String path;
	
	
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}

