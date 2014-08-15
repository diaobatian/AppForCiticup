package com.citi.mc.db;


import java.io.Serializable;
import java.util.List;



import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/*
 * ConsultMsg bean
 * refactor 
 * author:lianma 
 * time :2013.11.14
 * 
 * version:1.1
 * changelog:
 * 增加黑名单的列
 * */
@Table(name ="appinfo")
public class AppInfo extends Model implements Serializable
{
	
	private static final long serialVersionUID = -3374138783713131562L;

	@Column(name="isDeade")
	public int isDeade ;
	
	@Column(name="idinfo")
	public int idinfo;
	
	//查询一条数据
	public static AppInfo getById(int id)
	{
		return new Select().from(AppInfo.class).where("idinfo = ?",id).executeSingle();
	}
}

