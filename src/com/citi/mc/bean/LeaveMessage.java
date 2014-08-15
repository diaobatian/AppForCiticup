package com.citi.mc.bean;


public class LeaveMessage 
{
	private int BadyImage;
	private String message;
	private String time;
	public int getBadyImage() {
		return BadyImage;
	}
	public void setBadyImage(int badyImage) {
		BadyImage = badyImage;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String object) {
		this.message = object;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
}
