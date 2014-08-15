package com.citi.mc.db;


import java.io.Serializable;


public class Lvmsg implements Serializable{
	private static final long serialVersionUID = -4170611456166784447L;
	public String Id;
	public String contact;
	public String content;
	public boolean processor;
	public String processorName;
	public String processorid;
	public String createdTime;// 留言的时间
	public String remark;
	public int badyImage;

	public Lvmsg(String id, String contact, String processorName,
			String message, boolean isprocessed, String Time, String remark) {
		super();
		this.Id = id;
		this.contact = contact;
		this.processorName = processorName;
		// this.processorid=processorid;
		this.content = message;
		this.processor = isprocessed;
		this.createdTime = Time;
		this.remark = remark;
	}

	public Lvmsg(String id, String content, String time) {
		super();
		this.Id = id;
		this.content = content;
		this.createdTime = time;
	}

	public Lvmsg() {
		super();
	}

	public String getmId() {
		return this.Id;
	}

	public void setmId(String num) {
		this.Id = num;
	}

	public String getcontact() {
		return this.contact;
	}

	public void setcontact(String contact) {
		this.contact = contact;
	}

	public boolean getisprocessed() {
		return this.processor;
	}

	public void setisprocessed(boolean bool) {
		this.processor = bool;
	}

	public String getcontent() {
		return this.content;
	}

	public void setMessage(String object) {
		this.content = object;
	}

	public void setBadyImage(int badyImage) {
		this.badyImage = badyImage;
	}

	public int getBadyImage() {
		return badyImage;
	}

	public String getcreatedTime() {
		return createdTime;
	}

	public void setcreatedTime(String data) {
		this.createdTime = data;
	}

	public String getremark() {
		return remark;
	}

	public void setremark(String remark) {
		this.remark = remark;
	}

	public String getprocessorName() {
		return this.processorName;
	}

	public void setprocessorName(String name) {
		this.processorName = name;
	}

	public String toString() {
		return "[_id:" + Id + "/message content:" + content
				+ "/message contact: " + contact + "/createdTime: "
				+ createdTime + "/isprocessed:" + processor + "/processorName:"
				+ processorName + "/processorId:" + processorid + "]";

	}

}
