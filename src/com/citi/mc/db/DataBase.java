package com.citi.mc.db;


import java.util.List;

import android.content.Context;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
@Table(name="DataBase")
public class DataBase extends Model 
{
	@Column(name="Name")
	public String name;
	@Column(name="Message")
	public String message;
	
	public DataBase getDataBaseByName(String name)
	{
		return new Select().from(DataBase.class).where("Name = ?", name)
				.orderBy("RANDOM()").executeSingle();
				
	}
	
	private Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<DataBase> getAll()
	{
		return new Select().from(DataBase.class).execute();
	}
	
	public void delete(String name)
	{
		new Delete().from(DataBase.class).where("Name = ?",name);
		
	}
	 
	public String toString()
	{
		return name + ' ' + message;
	}
}
