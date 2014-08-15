package com.citi.mc.app;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.citi.mc.R;
import com.citi.mc.db.Monitor;
import com.citi.mc.service.MessageService;
import com.citi.mc.utils.MobileChatClient;
import com.citi.mc.utils.SongAndVibrator;
import com.citi.mc.utils.Timeparser;
import com.citi.mc.utils.UpdateManager;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class NoDisturbfragment extends Fragment {
	//private Monitor monitor;
	
 	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 取得当前登录的客服对象
	}
	
 	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.no_disturb, container, false); 
		//TextView alterText = (TextView)view.findViewById(R.id.alter_password);
		
		
		//finsh_btn = (Button)view.findViewById(R.id.mobile_finsh);
	
		final ToggleButton tbInvisiable = (ToggleButton)view.findViewById(R.id.invisiable_setbn);
		final ToggleButton tbNodisturb = (ToggleButton)view.findViewById(R.id.nodisturb_setbn);
	
		
		
		
		//保存设置
		final SPhelper sPhelper = new SPhelper(getActivity());
		//设置MainTabActivity的flag为true
		MainTabActivity.flag = true;
		
		if(sPhelper.getInvisiableState().equals("true"))
        {
			tbInvisiable.setBackgroundResource(R.drawable.slide_toggle_on);
        }
		else {
			tbInvisiable.setBackgroundResource(R.drawable.slide_toggle_off);
		}
		
		if(sPhelper.getNodisturbState().equals("true"))
		{
			tbNodisturb.setBackgroundResource(R.drawable.slide_toggle_on);
		}
		else {
			tbNodisturb.setBackgroundResource(R.drawable.slide_toggle_off);
		}

		//隐身
		tbInvisiable.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(isChecked)
				{
					
					
					HashMap<String, String> requestParams = new HashMap<String, String>();
					requestParams.put("unitid", MainTabActivity.monitor.unitid);// LoginActivity.inloginMonitor.unitid
					requestParams.put("usid", MainTabActivity.monitor.userid);
					requestParams.put("uscookie", MainTabActivity.monitor.uscookie);
					requestParams.put("deviceType", "android");
					
					RequestParams params = new RequestParams(requestParams);

					MobileChatClient.get("userver/online", params,
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONObject timeline) {
								try {
									// XXX: 检查下这里返回值true是字符串还是boolean类型
									if (timeline.getString("success").equals("true")) {
										// handler.sendMessage(message);
									}
									else
									{
										 
									}

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						
						
							@Override
							public void onFailure(Throwable arg0) {
								
							}
							

						});
					tbInvisiable.setBackgroundResource(R.drawable.slide_toggle_on);
					sPhelper.setInvisiableState("true");
					
					
				}
				else
				{	

					
					
					HashMap<String, String> requestParams = new HashMap<String, String>();
					requestParams.put("unitid", MainTabActivity.monitor.unitid);// LoginActivity.inloginMonitor.unitid
					requestParams.put("usid", MainTabActivity.monitor.userid);
					requestParams.put("uscookie", MainTabActivity.monitor.uscookie);
					requestParams.put("deviceType", "android");
					
					RequestParams params = new RequestParams(requestParams);

					MobileChatClient.get("userver/hide", params,
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONObject timeline) {
								try {
									// XXX: 检查下这里返回值true是字符串还是boolean类型
									if (timeline.getString("success").equals("true")) {
										// handler.sendMessage(message);
									}
									else
									{
										 
									}

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						
						
							@Override
							public void onFailure(Throwable arg0) {
								
							}
							

						});

					sPhelper.setInvisiableState("false");
					tbInvisiable.setBackgroundResource(R.drawable.slide_toggle_off);
				}
			}
		});
		tbNodisturb.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(isChecked)
				{
					 
					
					HashMap<String, String> requestParams = new HashMap<String, String>();
					requestParams.put("unitid", MainTabActivity.monitor.unitid);// LoginActivity.inloginMonitor.unitid
					requestParams.put("usid", MainTabActivity.monitor.userid);
					requestParams.put("cookie", MainTabActivity.monitor.uscookie);
					requestParams.put("client", "android");
					requestParams.put("open", "true");
					
					requestParams.put("from","h:"+"起始小时"+" m: 起始分钟");
					requestParams.put(" to","h:"+"小时"+" m: 分钟");
					
					
					
					
					RequestParams params = new RequestParams(requestParams);

					MobileChatClient.post("userver/noDisturb", params,
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONObject timeline) {
								try {
									// XXX: 检查下这里返回值true是字符串还是boolean类型
									if (timeline.getString("success").equals("true")) {
										// handler.sendMessage(message);
									}
									else
									{
										 
									}

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						
						
							@Override
							public void onFailure(Throwable arg0) {
								
							}
							

						});
					
					
					
					
					
					
					
					
					
					sPhelper.setNodisturbState("true");
					tbNodisturb.setBackgroundResource(R.drawable.slide_toggle_on);
					
					
				}
				else 
				{
					HashMap<String, String> requestParams = new HashMap<String, String>();
					requestParams.put("unitid", MainTabActivity.monitor.unitid);// LoginActivity.inloginMonitor.unitid
					requestParams.put("usid", MainTabActivity.monitor.userid);
					requestParams.put("cookie", MainTabActivity.monitor.uscookie);
					requestParams.put("client", "android");
					requestParams.put("open", "false");
					
					requestParams.put("from","h:"+"起始小时"+" m: 起始分钟");
					requestParams.put(" to","h:"+"小时"+" m: 分钟");
					
					
					
					
					RequestParams params = new RequestParams(requestParams);

					MobileChatClient.post("userver/noDisturb", params,
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONObject timeline) {
								try {
									// XXX: 检查下这里返回值true是字符串还是boolean类型
									if (timeline.getString("success").equals("true")) {
										// handler.sendMessage(message);
										
									}
									else
									{
										 
									}

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						
						
							@Override
							public void onFailure(Throwable arg0) {
								
							}
							

						});
					
					sPhelper.setNodisturbState("false");
					tbNodisturb.setBackgroundResource(R.drawable.slide_toggle_off);
				}
			}
		});

        return view;  
	}

	

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}


	
}
