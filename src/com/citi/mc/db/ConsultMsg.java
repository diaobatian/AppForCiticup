package com.citi.mc.db;


import java.io.Serializable;
import java.util.List;



import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.citi.mc.bean.Update;
import com.citi.mc.utils.Constant;

/*
 * ConsultMsg bean
 * refactor 
 * author:lianma 
 * time :2013.11.14
 * 
 * version:1.1
 * changelog:
 * 增加黑名单的列
 * version:1.2
 * 1.增加一个是否删除的字段isDelete
 * 
 * version1.3
 * 1.增加一个字段表示是否是新来的用户isFirstVisit
 * */
@Table(name = "ConsultMsg")
public class ConsultMsg extends Model implements Serializable
{
	
private static final long serialVersionUID = -3374138783713131562L;

@Column(name="isFirstVisit")
public boolean isFirstVisit;

@Column(name="isDelete")
public boolean isDelete;

@Column(name="isheimingdan")	
public boolean isHeimingdan;
	
@Column(name = "Image")
public int image; 
@Column(name = "Unreadcnt")
public int Unreadcnt; 
@Column(name = "Username")
public String Username;
@Column(name = "Tel")
public String tel;
@Column(name = "Mobilecookie")
public String mobilecookie;
@Column(name = "Createdtime")
public String createdtime;
@Column(name = "Os")
public String os;
@Column(name = "Email")
public String email;
@Column(name = "IM")
public String IM;
@Column(name = "Address")
public String Address;
@Column(name = "Remark")
public String remark;
@Column(name = "Domain")
public String domain;
@Column(name = "Keyword")
public String keyword;
@Column(name = "Appoinment")
public Boolean Appoinment;
@Column(name = "Latestmsg")
public String latestmsg;
@Column(name = "Userver")
public String userver;
@Column(name = "LDTime")
public String LDTime;
@Column(name = "Province")
public String province;
@Column(name = "City")
public String city;
@Column(name = "ISP")
public String isp;
@Column(name="IsOnline")
public Boolean isOnline;
@Column(name="latestTime")
public String latestTime;
@Column(name="MonitorId")
public String monitorId;
@Column(name="time")
public Long time;
/*"cookie": "006600137343675160264887", 
"os": "Linuxi686", 
"username": "用户1",
 "tel": "",
 "email": "",
 "IM": "",
 "address": "",
 "remark": "",
 "domain": "直接访问", 
"keyword": "", 
"appoinment": false, 
"latest": "小林你好啊", 
"userver": "101",
 "createdTime": 1373436754839, 
"LDTime": 1373436771500,
 "province": "四川", 
"city": "成都",
 "isp": "教育网"*/
 public ConsultMsg(int Image,String cookie,String os,String username,String tel,String email,String IM,String address,
		 String remark,String domain,String keyword,Boolean appoinment,String latestmsg,String userver,String createdtime,
		 String LDTime,String province,String city,String isp,int Unreadcnt,String monitorString) {
	// TODO Auto-generated constructor stub
     super();
     this.mobilecookie=cookie;
     this.image=Image;
     this.os=os;
     this.Username=username;
     this.tel=tel;
     this.email=email;
     this.IM=IM;
     this.Address=address;
     this.remark=remark;
     this.domain=domain;
     this.keyword=keyword;
     this.Appoinment=appoinment;
     this.latestmsg=latestmsg;
     this.userver=userver;
     this.createdtime=createdtime;
     this.LDTime=LDTime;
     this.province=province;
     this.city=city;
     this.isp=isp;
     this.Unreadcnt=Unreadcnt;
     this.monitorId=monitorString;
}
 public ConsultMsg() {
	// TODO Auto-generated constructor stub
    super();
 }
 public ConsultMsg(String cookie,String city,String isp,String createdtime,String latestmsg,String monitor) {
	
	 super();
	 this.mobilecookie=cookie;
	 this.city=city;
	 this.isp=isp;
	 this.createdtime=createdtime;
	 this.latestmsg=latestmsg;
	 this.monitorId=monitor;
	
}
 
 //通过Unreadcnt查询所有数据
 public static int UnreadcntSize(String monitorId)
	{
	 	int a =0;
		List<ConsultMsg> msgList = getAllbyMonitor(monitorId);
		
		//查询Unreadcnt字段的所有数据
		for(int i = 0 ; i<msgList.size(); i++){
			a += msgList.get(i).Unreadcnt;
		}
	    return a;
	}
 
 //通过monitorId查询所有数据
 public static List<ConsultMsg> getAllbyMonitor(String monitorId)
 {
	 return new Select().from(ConsultMsg.class).where("monitorId = ?", monitorId).orderBy("time desc").execute();
 }
 
	public static void ClearLast(String monitorid)
	{
		List<ConsultMsg> msgList = getByIsOnLine(0,monitorid);
		if(!msgList.isEmpty())
		{
			System.out.println("进来了啊========="+msgList.get(msgList.size()-1).getId());
			delete(ConsultMsg.class ,msgList.get(msgList.size()-1).getId());
		}
		
	}
	
	
	
	public static  List<ConsultMsg> ConsultList()
	{
		List<ConsultMsg> msgList = getAllConsultmsgs();
	    return msgList;
	}
 public static List<ConsultMsg> getAllConsultmsgs()
	{
		return new Select().from(ConsultMsg.class).execute();
	}
	public  List<ConsultMsg> getallConsultmsg()
	{
		return getMany(ConsultMsg.class, "ConsultMsg");
	}
	
	//查询一条数据
	 public static List<ConsultMsg> getByIsOnLine(int isOnLine,String monitorid)
	{
			return new Select().from(ConsultMsg.class).where("IsOnline=? AND MonitorId = ?",isOnLine,monitorid).execute();
	}
	 
	public static void  clearbyMonitor(String monitorId)
	{
		for(int i=1;i<=ConsultMsg.getAllbyMonitor(monitorId).size();i++)
		{
			delete(ConsultMsg.class ,i);
		}
	}
	//删除一条数据
	public static void RemoveMobileCookie(String  mobilecookie,String monitorid){
		
		new Delete().from(ConsultMsg.class).where("mobilecookie = ? AND MonitorId = ?" ,mobilecookie,monitorid).execute();
	}
	/**
	 *删除最早的一条历史记录 
	 */
	public static void deleteOldestDeadConsultmsg()
	{
//		new Delete().from(ConsultMsg.class).where("Createdtime=, args)
	}
	/**
	  * 
	  * @param mobilecookie
	  * @param monitorid
	  */
	//
	public static void setFirstVisitFalseByMobilecookie(String  mobilecookie,String monitorid) {
		
		ConsultMsg msg=new Select().from(ConsultMsg.class).where("mobilecookie=? AND MonitorId = ?",mobilecookie,monitorid).executeSingle();
		msg.isFirstVisit=false;
		msg.save();
	}
	//获得所有cookie大小
	public static boolean getConsultMsgFromMobileCookie(String  mobilecookie,String monitorid)
	{
		boolean flag = true;
		
		int size =new Select().from(ConsultMsg.class).where("mobilecookie=? AND MonitorId = ?",mobilecookie,monitorid).execute().size();
		flag=(size>0?false:true);
		return flag;
	}
 
	
	//查询cookie所有数据
	public static ConsultMsg getConsutFromUserName(String mobilecookie,String monitorid){
		return new Select().from(ConsultMsg.class).where("mobilecookie=? AND MonitorId = ?",mobilecookie,monitorid).executeSingle();
	}
	
	public static ConsultMsg getConsutFromId(Long id){
		return new Select().from(ConsultMsg.class).where("id=?",id).executeSingle();
	}
	/*
	 * 如果用户存在就不保存，如果不存在则保存,finish改变用户的状态
	 * */
	public static void saveConsultmsg(ConsultMsg msg,String userId)
	{
		if(getConsultMsgFromMobileCookie(msg.mobilecookie,msg.monitorId)&&getSerUserNum(userId))//getConsutFromUserName(msg.mobilecookie)==null
		{
			msg.save();
		}
		
	}
	//判断当前客服的服务客户的数量
	public static boolean getSerUserNum(String monitorId)
	{
		boolean flag = true;
		int size =new Select().from(ConsultMsg.class).where("MonitorId = ?",monitorId).execute().size();
		flag=(size>=Constant.COUNT_PERSISTENT_USERS?false:true);
		return flag;
	}
	
	
	public  String toString()
	{
		return "[consultmsg is " +"cookie:"+mobilecookie+"/createdtime:"+createdtime+"latestmsg:"+latestmsg+ "]";

	}

}
