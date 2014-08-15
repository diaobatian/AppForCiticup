package com.citi.mc.utils;

/** 
 * @作者 newbie 
 * @email lianlupeng@gmail.com
 * @创建日期 2013-8-1 
 * @版本 V 1.0 
 */
import java.util.List;

public class GifObj {

	private String gifID;
	private List<GifTextDrawable> drawable; 
	
	public GifObj(String gifID,List<GifTextDrawable>  drawableList)
	{
		this.gifID = gifID;
		this.drawable = drawableList;
	}
	public String getGifId()
	{
		return this.gifID;
	}
	public List<GifTextDrawable> getGifTextDrawableList()
	{
		return this.drawable;
	}	
}
