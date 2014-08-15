package com.citi.mc.utils;

public class HttpUrls 
{
	private static final String MANGE_URL = "http://106.187.89.54:8180/";
	
	private static final String LOGIN_URL = "login";
	
	private static final String CHAT_URL = "http://106.187.89.54:8000/";
	
	public static String getChatUrl() {
		return CHAT_URL;
	}

	private static final String URLM_URL = "userver/getUnreadLeavemsgs";

	public static String getMangeUrl() {
		return MANGE_URL;
	}

	public static String getLoginUrl() {
		return LOGIN_URL;
	}

	public static String getUrlmUrl() {
		return URLM_URL;
	}
	
	

}
