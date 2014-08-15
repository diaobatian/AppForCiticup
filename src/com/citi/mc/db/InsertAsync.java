package com.citi.mc.db;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.citi.mc.bean.LeaveMessage;
import com.citi.mc.app.LeaveMsgFragment;
import com.citi.mc.R;
import com.citi.mc.utils.MobileChatClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.os.AsyncTask;

public class InsertAsync extends AsyncTask<Integer, Integer, String> 
{
	
	public List<LeaveMessage> list = new ArrayList<LeaveMessage>();
	 @Override  
	    protected void onPreExecute() {  
	        //第一个执行方法  
	        super.onPreExecute();  
	    }  
	    @Override
	    protected String doInBackground(Integer... params) {  
	        //第二个执行方法,onPreExecute()执行完后执行  
	    	HashMap<String, String> paramMap = new HashMap<String, String>();
	    	paramMap.put("unitid", "50e848d1447705745b000002");
			paramMap.put("usid", "516620a2f1f9106e0500030e");
			paramMap.put("uscookie", "gtrhjriyo");
			RequestParams param = new RequestParams(paramMap);
			MobileChatClient.get("userver/getReadLeavemsgs", param, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONArray arg0) {
					// TODO Auto-generated method stub
					super.onSuccess(arg0);
					
					
					for(int i =0;i<arg0.length();i++)
					{
						try {
							JSONObject jsonObject = arg0.getJSONObject(i);
							System.out.println(jsonObject.getString("content"));
							System.out.println(jsonObject.getString("createdTime"));
							LeaveMessage leaveMessage = new LeaveMessage();
							leaveMessage.setBadyImage(R.drawable.butn_close);
							leaveMessage.setTime(jsonObject.getString("createdTime"));
							leaveMessage.setMessage(jsonObject.getString("content"));
							DataBase dataBase = new DataBase();
							dataBase.name = jsonObject.getString("createdTime");
							dataBase.message = jsonObject.getString("content");
							dataBase.save();
							list.add(leaveMessage);
							Thread.sleep(10);  

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
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

	    	
	        return null;
	    }  

	    @Override  
	    protected void onProgressUpdate(Integer... progress) {  
	        super.onProgressUpdate(progress);  
	    }  

	    @Override  
	    protected void onPostExecute(String result) {  
	        //doInBackground返回时触发，换句话说，就是doInBackground执行完后触发  
	        //这里的result就是上面doInBackground执行后的返回值，所以这里是"执行完毕"  
	    	LeaveMsgFragment leaveMsgFragment = new LeaveMsgFragment();
	    	//leaveMsgFragment.list = list;
	    	System.out.println(leaveMsgFragment.list.size());
	        super.onPostExecute(result); 
	        
	    }  
}
