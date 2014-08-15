package com.citi.mc.db;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.InputFilter.LengthFilter;
import android.widget.ListView;

import com.citi.mc.bean.LeaveMessage;
import com.citi.mc.R;
import com.citi.mc.utils.MobileChatClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GetLeaveMessage
{
	
	public List<LeaveMessage>  getMessage() throws Exception
	{
		

		DataBase dataBase = new DataBase();
		//System.out.println(dataBase.getAll());
		List<LeaveMessage>mgs = null;
		
		mgs = new ArrayList<LeaveMessage>();
		
		for(int i=0;i<dataBase.getAll().size();i++)
		{
			
			LeaveMessage leaveMessage = new LeaveMessage();
			int j;
			int z;
			if(i== 0)
			{
				j=0;
				z=1;	
			}
			else
			{
				j=1;
				z=2;
			}
			//HashMap<String, Object> hashMap = new HashMap<String, Object>();
			//user.setImage(R.drawable.singchat_back);
			leaveMessage.setBadyImage(R.drawable.singchat_back);
			leaveMessage.setTime(dataBase.getAll().toString().split(",")[i].split(" ")[j]);
			leaveMessage.setMessage(dataBase.getAll().toString().split(",")[i].split(" ")[z]);
			
			mgs.add(leaveMessage);	
			
			
		}
		
		return mgs;
		
		
	}
	public List<LeaveMessage>  getMessageBySize(int begin,int end) throws Exception
	{
		

		DataBase dataBase = new DataBase();
		//System.out.println(dataBase.getAll());
		List<LeaveMessage>mgs = null;
		
		mgs = new ArrayList<LeaveMessage>();
		
		for(int i=begin;i<end;i++)
		{
			
			LeaveMessage leaveMessage = new LeaveMessage();
			int j;
			int z;
			if(i== 0)
			{
				j=0;
				z=1;	
			}
			else
			{
				j=1;
				z=2;
			}
			//HashMap<String, Object> hashMap = new HashMap<String, Object>();
			//user.setImage(R.drawable.singchat_back);
			leaveMessage.setBadyImage(R.drawable.singchat_back);
			leaveMessage.setTime(dataBase.getAll().toString().split(",")[i].split(" ")[j]);
			leaveMessage.setMessage(dataBase.getAll().toString().split(",")[i].split(" ")[z]);
			
			mgs.add(leaveMessage);	
			
			
		}
		
		return mgs;
		
		
	}
	public List<Lvmsg>  getMsgBySize(int begin) throws Exception
	{
		

		Lvmsg lvmsgData = new Lvmsg();
		//System.out.println(dataBase.getAll());
		List<Lvmsg>mgs = null;
		
		mgs = new ArrayList<Lvmsg>();
		//mgs = lvmsgData.getAll().subList(begin, end);
		
//		for(int i=begin;i<end;i++)
//		{
//			
//			Lvmsg lvmsg = new Lvmsg();
//			
//			
//			lvmsg.setBadyImage(R.drawable.singchat_back);
//			lvmsg.setmId(lvmsg.Id);
//			lvmsg.setMessage(lvmsg.content);
//			lvmsg.setcreatedTime(lvmsg.createdTime);
//			lvmsg.setisprocessed(lvmsg.processor);
//			lvmsg.setprocessorName(lvmsg.processorName);
//			lvmsg.setremark(lvmsg.remark);
//			mgs.add(lvmsg);	
//			
//			
//		}
//		
//		if(begin+5<=lvmsgData.getunprocessedmsg().size())
//		{
//			mgs = lvmsgData.getunprocessedmsg().subList(begin, begin+5);
//		}
//		if(begin+5>lvmsgData.getunprocessedmsg().size())
//		{
//			mgs = lvmsgData.getunprocessedmsg().subList(begin, lvmsgData.getunprocessedmsg().size());
//		}
		return mgs;
		
//		
	}
	public List<LeaveMessage> LeaveMobileChat( ) throws JSONException, UnsupportedEncodingException
	 {
		 
		 HashMap<String, String> paramMap = new HashMap<String, String>();
		 paramMap.put("unitid", "50e848d1447705745b000002");
		 paramMap.put("usid", "516620a2f1f9106e0500030e");
		 paramMap.put("uscookie", "gtrhjriyo");
		 RequestParams params = new RequestParams(paramMap);
		 final List<LeaveMessage> mgs = new ArrayList<LeaveMessage>();
	     MobileChatClient.get("userver/getReadLeavemsgs", params, new JsonHttpResponseHandler() {
	           
				@Override
				public void onSuccess(JSONArray arg0) {
					// TODO Auto-generated method stub
					super.onSuccess(arg0);
					
					for(int i =0;i<arg0.length();i++)
					{
						try {
							JSONObject jsonObject = arg0.getJSONObject(i);
							System.out.println(jsonObject.get("content"));
							LeaveMessage leaveMessage = new LeaveMessage();
							leaveMessage.setBadyImage(R.drawable.butn_close);
							//leaveMessage.setImage(R.drawable.butn_close);
							leaveMessage.setTime(jsonObject.getString("createdTime"));
							leaveMessage.setMessage(jsonObject.getString("content"));
							
							mgs.add(leaveMessage);
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}

				@Override
				public void onSuccess(JSONObject arg0) {
					// TODO Auto-generated method stub
					super.onSuccess(arg0);
					System.out.println("timeline6");
				}
	            
	            
	            
	        });
	     
		System.out.println(mgs.size());
		return mgs;
	 }
	
}


      

