package com.citi.mc.app;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.util.Log;
import com.baidu.mobstat.StatService;
import com.citi.mc.R;
import com.citi.mc.db.ConsultMsg;
import com.citi.mc.db.Monitor;
import com.citi.mc.utils.MobileChatClient;
import com.citi.mc.utils.IsNetWorkAvailable;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;
/*
 * 客户信息fragment
 * refactor 
 * author:lianma 
 * time :2013.11.14
 * 
 * version:1.1
 * changelog:
 * 
 * */
public class SetDuiFragment extends Fragment{

	private Button submitButton, backButton;
	private EditText nameTextView,phoneTextView,qQTextView;
	private CheckBox appointmentCheckBox,heimingdanCheckBox;
	private SPhelper sPhelper=null;
	private Monitor tmpMonitor;
	public Activity activity;
	public View view;
	private View v;
	private HashMap<String,Object> userinfoHashMap=null;
	//标志当前聊天的用户cookie
	private String mobilecookiefromconsult;
	private ConsultMsg consultmsgCurrent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		activity = getActivity();
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		v = inflater.inflate(R.layout.redirect_window, container, false); 
		tmpMonitor = MainTabActivity.monitor;
		mobilecookiefromconsult= getArguments().getString("mobilecookie");
		MainTabActivity.layout.setVisibility(View.GONE);
		//判断是否连接网络
		
			
			try {
				boolean isExist = false;
				for (int i = 0; i < MainTabActivity.listConsultmsgs.size(); i++) {
					if(MainTabActivity.listConsultmsgs.get(i).mobilecookie.equals(mobilecookiefromconsult))
					{
						isExist = true;
						consultmsgCurrent = MainTabActivity.listConsultmsgs.get(i);
					}
				}
				if(!isExist)
				{
					if(IsNetWorkAvailable.isAvailable(getActivity()))
					{
						getuserinfo();
					}
					else 
					{
						IsNetWorkAvailable.setNetWork(getActivity(), getActivity());
					}
				}
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			nameTextView = (EditText) v.findViewById(R.id.nametext);
			phoneTextView = (EditText) v.findViewById(R.id.phonetext);
			appointmentCheckBox=(CheckBox)v.findViewById(R.id.appointment);
			qQTextView = (EditText) v.findViewById(R.id.qqtext);
			heimingdanCheckBox = (CheckBox)v.findViewById(R.id.heimingdan);
			//获取控件设置属性值
			submitButton = (Button)v.findViewById(R.id.submit_user_info);
			backButton = (Button)v.findViewById(R.id.chat_back);
			backButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Bundle bundle = new Bundle();
					bundle.putString("mobilecookie", mobilecookiefromconsult);
					FragmentManager manager = getFragmentManager();
					FragmentTransaction ft = manager.beginTransaction();
					ft.setCustomAnimations(R.anim.push_left_out,R.anim.push_left_zhong);
					ChatFragment fragment = new ChatFragment();
					fragment.setArguments(bundle);
					ft.replace(android.R.id.tabcontent, fragment);
					MainTabActivity.layout.setVisibility(View.GONE);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					ft.commit();
				}
			});
			submitButton.setOnClickListener(new OnClickListener() {
			
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						uploaduserinfo();
						Bundle bundle = new Bundle();
						bundle.putString("mobilecookie", mobilecookiefromconsult);
						FragmentManager manager = getFragmentManager();
						FragmentTransaction ft = manager.beginTransaction();
						ft.setCustomAnimations(R.anim.push_left_out,R.anim.push_left_zhong);
						ChatFragment fragment = new ChatFragment();
						fragment.setArguments(bundle);
						ft.replace(android.R.id.tabcontent, fragment);
						MainTabActivity.layout.setVisibility(View.GONE);
						ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
						ft.commit();
						
						
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			//设置初始值
			if(consultmsgCurrent!=null)
			{
				System.out.println("SetDuiFragment"+consultmsgCurrent.Username+","+consultmsgCurrent.tel+","+consultmsgCurrent.IM+","+consultmsgCurrent.Appoinment+","+consultmsgCurrent.isHeimingdan);
				nameTextView.setText(consultmsgCurrent.Username==null?"未指定":consultmsgCurrent.Username);
				phoneTextView.setText(consultmsgCurrent.tel==null?"180":consultmsgCurrent.tel);
				qQTextView.setText(consultmsgCurrent.IM==null?"未指定":consultmsgCurrent.IM);
        	   	appointmentCheckBox.setChecked(consultmsgCurrent.Appoinment);
        	    heimingdanCheckBox.setChecked(consultmsgCurrent.isHeimingdan);
			}
		return v;
	}
	
	//提交数据
	@Override
	public void onPause()
	{
		super.onPause();
		
		//百度统计
		StatService.onPause(this);
		
	}
	@Override
	public void onResume()
	{
		super.onResume();
		//百度统计
				StatService.onResume(this);
	}
	public void uploaduserinfo() throws JSONException, UnsupportedEncodingException
	{
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		sPhelper=new SPhelper(getActivity());
		paramMap.put("unitid", tmpMonitor.unitid);
		paramMap.put("usid", tmpMonitor.userid);
		paramMap.put("cookie", tmpMonitor.uscookie);
		paramMap.put("mobile",mobilecookiefromconsult);
		
		paramMap.put("info[tel]",phoneTextView.getText().toString());
		paramMap.put("info[username]",nameTextView.getText().toString());
		paramMap.put("info[IM]",qQTextView.getText().toString());
		paramMap.put("info[appoinment]",String.valueOf(appointmentCheckBox.isChecked()));
		paramMap.put("info[blacklist]",String.valueOf(heimingdanCheckBox.isChecked()));
		
		RequestParams params = new RequestParams(paramMap);
		MobileChatClient.put("userver/mobiledata", params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject arg0) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0);
				
				String flag;
				try {
					
					flag = (String)arg0.getString("success");
					if(flag.equals("true"))
					{
						Toast.makeText(getActivity(),"保存成功",Toast.LENGTH_SHORT).show();
						Message message = Message.obtain();
						message.what=2;
						genxinHandler.sendMessage(message);
					}else{
						Toast.makeText(getActivity(),"保存失败",Toast.LENGTH_SHORT).show();
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			
		});	
	}
	
	private Handler genxinHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what==2)
			{
				
				//userNameTextView.setText(nameTextView.getText().toString());
				if(getArguments().getBoolean("isOnline")){
					Log.i("as","ChatFragment---------------------------------"+"ConsultAdapter.isyonghu");
					//userNameTextView.setText(nameTextView.getText()+"(在线)");
				}else{
					Log.i("as","ChatFragment============================"+"ConsultAdapter.isyonghu");
					//userNameTextView.setText(nameTextView.getText()+"(离线)");
				}
				ConsultMsg consultmsg =ConsultMsg.getConsutFromUserName(mobilecookiefromconsult,MainTabActivity.monitor.userid);
				consultmsg.Username = nameTextView.getText().toString().trim();
				consultmsg.IM =qQTextView.getText().toString().trim();
				consultmsg.tel=phoneTextView.getText().toString().trim();
				consultmsg.Appoinment=appointmentCheckBox.isChecked();
				consultmsg.isHeimingdan =heimingdanCheckBox.isChecked();
				consultmsg.save();
				//更新MainTabActivity list列表数据
				for (int i = 0; i < MainTabActivity.listConsultmsgs.size(); i++) {
					ConsultMsg msgChanged =MainTabActivity.listConsultmsgs.get(i);
					if(msgChanged.mobilecookie.equals(mobilecookiefromconsult))
					{
						msgChanged.Username = nameTextView.getText().toString().trim();
						msgChanged.IM =qQTextView.getText().toString().trim();
						msgChanged.tel=phoneTextView.getText().toString().trim();
						msgChanged.Appoinment=appointmentCheckBox.isChecked();
						msgChanged.isHeimingdan =heimingdanCheckBox.isChecked();
					}
				}
			}else if (msg.what==0) 
			{
				Toast.makeText(getActivity(), "网络状况不佳，请重新获取", Toast.LENGTH_LONG).show();
			}else if(msg.what==1)
			{

					{
						System.out.println(userinfoHashMap.get("tel").toString().equals("未指定")+"===============tel");
						if(userinfoHashMap.get("username").toString().equals("未指定"))
						{
							
							//nameTextView.setHint(userinfoHashMap.get("username").toString());
						}
						else {
							nameTextView.setText(userinfoHashMap.get("username").toString());
						}
						if(userinfoHashMap.get("tel").toString().equals("未指定"))
						{
							//nameTextView.setHint(userinfoHashMap.get("tel").toString());
							System.out.println(userinfoHashMap.get("tel").toString()+"===============telstring");
						}
						else {
							phoneTextView.setText(userinfoHashMap.get("tel").toString());
						}
						setOnfocusChangedListener(nameTextView, "username");
						if(userinfoHashMap.get("QQ").toString().equals("未指定"))
						{
							//nameTextView.setHint(userinfoHashMap.get("QQ").toString());
						}
						else {
							qQTextView.setText(userinfoHashMap.get("QQ").toString());
						}
						setOnfocusChangedListener(phoneTextView, "tel");
						
						setOnfocusChangedListener(qQTextView, "IM");
						if (userinfoHashMap.get("appoinment").equals(true)) 
			           {
			        	   	appointmentCheckBox.setChecked(true);
			           }
			           else {
			        	   appointmentCheckBox.setChecked(false);
			           }
			           if (userinfoHashMap.get("blacklist").equals(true)) 
			           {
			        	   heimingdanCheckBox.setChecked(true);
			           }
			           else {
			        	   heimingdanCheckBox.setChecked(false);
			           } 	
				}
				}
				else {
					Toast.makeText(getActivity(), "获取客户失败。。。。。。", Toast.LENGTH_LONG).show();
				}


			}
		};
		

	public void setOnfocusChangedListener(final EditText editText,final String key)
	{
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				// TODO Auto-generated method stub
				
				if (hasFocus) {
					System.out.println("focused...................");
				}
				else if(!hasFocus){
					System.out.println("lost focused...................");
					//uploaduserinfo(key, editText.getText().toString());
					
				}
			}
		});
		editText.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(getActivity())
				.setPositiveButton("粘贴", new DialogInterface.OnClickListener() {
					@SuppressLint("NewApi")
					@SuppressWarnings("deprecation")
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						ClipboardManager clipboard = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE); 
						editText.setText(clipboard.getText().toString());
					}
				}).show();
				return false;
			}
		});
	}
	
	//获取客户资料
		public void getuserinfo() throws JSONException, UnsupportedEncodingException
		{


			// mobilecookiefromconsult="006600137343675160264887";
			System.out.println("into onfoucschanged...............................");
			HashMap<String, String> paramMap = new HashMap<String, String>();
			sPhelper=new SPhelper(getActivity());
			paramMap.put("unitid", tmpMonitor.unitid);
			paramMap.put("usid", tmpMonitor.userid);
			paramMap.put("uscookie", tmpMonitor.uscookie);
			paramMap.put("mobile",mobilecookiefromconsult);
			RequestParams params = new RequestParams(paramMap);
			MobileChatClient.get("userver/getMobiledata", params, new JsonHttpResponseHandler() {

				@Override
				public void onFailure(Throwable arg0) {
					// TODO Auto-generated method stub
					super.onFailure(arg0);
					Message msg=new Message();
					msg.what=0;
					genxinHandler.sendMessage(msg);
				}
				@Override
				protected Object parseResponse(String arg0) throws JSONException {
					// TODO Auto-generated method stub
					JSONObject jsonObject=new JSONObject(arg0);
					String flag = (String)jsonObject.getString("success");
					if(flag.equals("true"))
					{
						JSONObject dataJsonObject=jsonObject.getJSONObject("data");
						System.out.println("into string sucess...............getuserinfo"+dataJsonObject);
						userinfoHashMap=new HashMap<String, Object>();
						if (!dataJsonObject.getString("username").equals("")) {
							userinfoHashMap.put("username", dataJsonObject.getString("username"));
						}
						else {
							userinfoHashMap.put("username", "未指定");
						}
					//	"appoinment":"false",
						if (!dataJsonObject.getString("appoinment").equals("")) {
							userinfoHashMap.put("appoinment", dataJsonObject.getString("appoinment"));
						}
						else {
							userinfoHashMap.put("username", "false");
						}
						//userinfoHashMap.put("address", dataJsonObject.getString("address"));
						if (!dataJsonObject.getString("tel").equals("")) {
							userinfoHashMap.put("tel", dataJsonObject.getString("tel"));
						}
						else {
							userinfoHashMap.put("tel", "未指定");
						}
						//userinfoHashMap.put("tel", dataJsonObject.getString("tel"));
						//userinfoHashMap.put("email", dataJsonObject.getString("email"));
						if (!dataJsonObject.getString("IM").equals("")) {
							userinfoHashMap.put("QQ", dataJsonObject.getString("IM"));
						}
						else {
							userinfoHashMap.put("QQ", "未指定");
						}
						if(dataJsonObject.getString("appoinment").equals("true"))
						{
							userinfoHashMap.put("appoinment", true);
						}
						else {
							userinfoHashMap.put("appoinment", false);
						}
						if(dataJsonObject.getString("blacklist").equals("true"))
						{
							userinfoHashMap.put("blacklist", true);
						}
						else {
							userinfoHashMap.put("blacklist", false);
						}
						//userinfoHashMap.put("QQ", dataJsonObject.getString("IM"));
						//userinfoHashMap.put("remark", dataJsonObject.getString("remark"));
						genxinHandler.sendEmptyMessage(1);
					}
					else {
						System.out.println("into parestring, but fail...............");
					}
					return super.parseResponse(arg0);

				}

				@Override
				public void onSuccess(JSONObject arg0) {
					// TODO Auto-generated method stub
					super.onSuccess(arg0);
					try {
						System.out.println(arg0.getString("sucess")+"...............mobiledata put sucess ");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					// TODO Auto-generated method stub
					super.onFailure(arg0, arg1);
					System.out.println("jsonobject failure aaa");
				}

			});
			Log.i("sa", "你进来了SetDuiFramgent");

		}



}
