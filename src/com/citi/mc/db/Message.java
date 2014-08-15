package com.citi.mc.db;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;

import com.citi.mc.bean.LeaveMessage;
import com.citi.mc.R;
import com.citi.mc.utils.MobileChatClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Message 
{
	
	public class InsertAsync extends AsyncTask<Integer, Integer, String> {

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
								Thread.sleep(10);  
								leaveMessage.notify();

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InterruptedException e) {
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

		    	
		        return "执行完毕";  
		    }  

		    @Override  
		    protected void onProgressUpdate(Integer... progress) {  
		        super.onProgressUpdate(progress);  
		    }  

		    @Override  
		    protected void onPostExecute(String result) {  
		        //doInBackground返回时触发，换句话说，就是doInBackground执行完后触发  
		        //这里的result就是上面doInBackground执行后的返回值，所以这里是"执行完毕"  
		        super.onPostExecute(result); 
		        
		    }  
	}
	
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
	public class PersonContentProvider extends ContentProvider 
	{

		@Override
		public int delete(Uri uri, String selection, String[] selectionArgs) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getType(Uri uri) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Uri insert(Uri uri, ContentValues values) {
			// TODO Auto-generated method stub
			InsertAsync async = new InsertAsync();
			async.execute(100);
			getContext().getContentResolver().notifyChange(uri, null);
			return null;
		}

		@Override
		public boolean onCreate() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Cursor query(Uri uri, String[] projection, String selection,
				String[] selectionArgs, String sortOrder) {
			
			return null;
		}

		@Override
		public int update(Uri uri, ContentValues values, String selection,
				String[] selectionArgs) {
			// TODO Auto-generated method stub
			return 0;
		}
	}
}
