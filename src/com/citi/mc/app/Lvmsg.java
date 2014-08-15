package com.citi.mc.app;

import java.util.List;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Lvmsg")
public class Lvmsg extends Model
{
@Column(name = "mId")
public String Id;
@Column(name = "contact")
	public String contact;
@Column(name = "content")
	public String content;
@Column(name = "processor")
	public boolean processor;
@Column(name = "processorName")
	public String processorName;
@Column(name = "processorid")
	public String processorid;
@Column(name = "createdtime")
	public String createdTime;//留言的时间
@Column(name = "remark")
public String remark;
public int badyImage;
 public Lvmsg(String id,String contact,String processorName,String message,boolean isprocessed,String Time,String remark)
 {
     super();
     this.Id=id;
     this.contact=contact;
     this.processorName=processorName;
    // this.processorid=processorid;
     this.content = message;
     this.processor =isprocessed;
     this.createdTime=Time;
     this.remark=remark;
}
 public Lvmsg(String id,String content,String time)
 {
	 super();
	 this.Id=id;
	 this.content=content;
	 this.createdTime=time;
	 
 }
 public Lvmsg(){
	 super();
 }
 public List<Lvmsg> getAll()
 {
	 return new Select().from(Lvmsg.class).execute();
 }
 public String getmId() {
		return this.Id;
	}
 public void setmId(String num){
	 this.Id=num;
 }
	public String getcontact() {
		return this.contact;
	}
	public void setcontact(String contact){
		this.contact=contact;
	}
	public boolean getisprocessed() {
		return this.processor;
	}
	public void setisprocessed(boolean bool){
		this.processor=bool;
	}
	public String getcontent() {
		return this.content;
	}
	public void setMessage(String object) {
		this.content = object;
	}
	public void setBadyImage(int badyImage)
	{
		this.badyImage = badyImage;
	}
	
	
	public int getBadyImage() {
		return badyImage;
	}
	public String getcreatedTime() {
		return createdTime;
	}
	public void setcreatedTime(String data){
		this.createdTime=data;
	}
	public String getremark() {
		return remark;
	}
	public void setremark(String remark){
		this.remark=remark;
	}
	public String getprocessorName() {
		return this.processorName;
	}
	public void setprocessorName(String name){
		this.processorName=name;
	}
	public List<Lvmsg> getunprocessedmsg()
	{
		return new Select().from(Lvmsg.class).where("processor=?",0).execute();
	}
	public int getunprocessedcnt(){
		return  this.getunprocessedmsg().size();
	}
	public Lvmsg getLvmsgfrommid(String string){
		return new Select().from(Lvmsg.class).where("mId=?",string).orderBy("RANDOM()").executeSingle();
	}
	
	public void SaveLvmsg(Lvmsg msg){
		if(getLvmsgfrommid(msg.getmId())==null)
		{
			
			msg.save();
			
		}
	}
	public List<Lvmsg> getallLvmsgs()
	{
		return getMany(Lvmsg.class, "Lvmsg");
	}
	public void Clear()
	{
		for(int i=1;i<=this.getallLvmsgs().size();i++)
		{
			delete(Lvmsg.class ,i);
		}
		
	}
	public String toString()
	{
		return "[_id:" + Id+"/message content:" + content + "/message contact: " + contact
                + "/createdTime: " +createdTime + "/isprocessed:" + processor+"/processorName:"+processorName
                +"/processorId:"+processorid
                + "]";

	}

}
