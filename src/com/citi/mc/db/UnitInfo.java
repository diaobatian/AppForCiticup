package com.citi.mc.db;


import java.util.List;

import android.R.integer;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
@Table(name="UnitInfo")
public class UnitInfo extends Model 
{	
	@Column(name="Unitid")
	public String unitid;
	@Column(name="Sbid")
	public int sbid;
	@Column(name="Unitname")
	public String unitname;
	@Column(name="Unitlogo")
	public String unitlogo;
	@Column(name="UnitContact")
	public String unitcontact;
	@Column(name="UnitEmail")
	public String unitemail;
	@Column(name="UnitTel")
	public String unittel;
	@Column(name="UnitUrl")
	public String uniturl;
	@Column(name="Address")
	public String address;
	
	public UnitInfo(String unitid,String unitname,String logo,String contact,String email,String tel,String url,String address)
	{
		super();
		this.unitid=unitid;
		this.sbid=1;
		this.unitcontact=contact;
		this.unitemail=email;
		this.unitlogo=logo;
		this.unittel=tel;
		this.uniturl=url;
		this.unitname=unitname;
		this.address=address;
		
	}
	public UnitInfo () {
		super();
	}
	public static  List<UnitInfo> getAllUnitinfo() {
		//return new Select().from(UnitInfo.class, "UnitInfo").;
		 return new Select().from(UnitInfo.class).execute();
	}
	public static UnitInfo getUnitinfofromId(String unitid) {
		return new Select().from(UnitInfo.class).where("unitid=?",unitid).executeSingle();
	//	return new Select().from(Consultmsg.class).where("mobilecookie=?",string).executeSingle();
	}
	public void deleteChat(Long Id)
	{
		new Delete().from(UnitInfo.class).where("mId = ?" , Id);
	}
}
