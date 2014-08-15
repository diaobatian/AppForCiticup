package com.citi.mc.bean;


public class ChatContent 
{
	public boolean left;
	public String content;
	public int image;
	
	public ChatContent(boolean left,String content,int image)
	{
		this.left = left;
		this.content = content;
		this.image = image;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}
	
	
}
