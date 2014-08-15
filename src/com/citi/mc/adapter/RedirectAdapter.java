package com.citi.mc.adapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.citi.mc.app.SPhelper;
import com.citi.mc.R;
import com.citi.mc.R.id;
import com.citi.mc.R.layout;
import com.citi.mc.utils.MobileChatClient;
import com.citi.mc.utils.MobileManagerClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class RedirectAdapter extends BaseAdapter {

	private Context context;
	private List<HashMap<String, Object>>kefulist=null;
	private List<String> kefuinfo=null;
	private LayoutInflater layoutInflater;
	String mobilecookie;
	String targetidString;
	private SPhelper sPhelper;
	public RedirectAdapter(Context context,List<String> message)
	{
		this.context = context;
		this.kefuinfo = message;
		//System.out.println("kefulist 的长度。。。。。。。。。。。。。。。"+kefulist.size());
		this.layoutInflater = LayoutInflater.from(context);
		this.sPhelper=new SPhelper(context);
	}
	/*public RedirectAdapter(Context context, List<HashMap<String, Object>> message)
	{
		this.context = context;
		this.kefulist = message;
		this.layoutInflater = LayoutInflater.from(context);
		
	}*/
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return kefuinfo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return kefuinfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		System.out.println("into   getView .............");
		ViewHolder viewHolder = null;
		if(convertView == null)
		{
			System.out.println("in getview convertview is null.................");
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.newredirect_listview, null);
			convertView.setBackgroundResource(R.drawable.round_background);
			//viewHolder.imageView = (ImageView)convertView.findViewById(R.id.img);
			//viewHolder.line_name = (TextView)convertView.findViewById(R.id.line_name);
			viewHolder.line_detail = (TextView)convertView.findViewById(R.id.line_detail);
			//ViewHolder.kefu_btn = (Button)convertView.findViewById(R.id.item_btn);
			convertView.setTag(viewHolder);
		}
		else
		{
			System.out.println("in getview ...................................is not null");
			viewHolder = (ViewHolder)convertView.getTag();
		}
		//viewHolder.imageView.setImageResource((Integer)kefulist.get(position).get("image"));
	
		/*if (((Integer)kefulist.get(position).get("details"))==0) {
			System.out.println("in getview .......................details --0............");
			viewHolder.kefu_name.setTextColor(Color.GRAY);
			viewHolder.kefu_details.setText("离线");
			viewHolder.kefu_details.setTextColor(Color.GRAY);
			ViewHolder.kefu_btn.setVisibility(View.INVISIBLE);
		}
		else if (((Integer)kefulist.get(position).get("details"))==1) {
			System.out.println("in getview .......................details --1............");
			viewHolder.kefu_name.setTextColor(Color.GREEN);
			viewHolder.kefu_details.setText("当前在线，空闲");
			viewHolder.kefu_details.setTextColor(Color.GREEN);
			ViewHolder.kefu_btn.setVisibility(View.VISIBLE);
		}
		else if (((Integer)kefulist.get(position).get("details"))==2) {
			System.out.println("in getview .......................details --2...........");
			viewHolder.kefu_name.setTextColor(Color.RED);
			viewHolder.kefu_details.setTextColor(Color.RED);
			viewHolder.kefu_details.setText("当前在线，忙碌");
			ViewHolder.kefu_btn.setVisibility(View.VISIBLE);
		}*/
		//viewHolder.line_name.setText((String)kefulist.get(position).get("name"));
		viewHolder.line_detail.setText((String)kefuinfo.get(position));

		/*ViewHolder.kefu_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("show kefu_info.......................................");

				try {
					mobilecookie=(String)kefulist.get(position).get("mobilecookie");
					targetidString=(String)kefulist.get(position).get("_id");
					redirectMobileChat();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});*/
		return convertView;
	}

	public static class ViewHolder
	{
		//public ImageView imageView;
		//public TextView line_name;
		public TextView line_detail;
		//public static Button kefu_btn;
	}
	
	
	/*public boolean redirectMobileChat() throws JSONException,UnsupportedEncodingException

	{
		sPhelper=new SPhelper(context);
		HashMap<String, String> paramMap = new HashMap<String, String>();
		//paramMap.put("usname","客服BOBO");
		paramMap.put("unitid", sPhelper.getUnitid());
		paramMap.put("usid", sPhelper.getUsid());
		paramMap.put("uscookie", sPhelper.getUscookie());
		RequestParams params = new RequestParams(paramMap);
		MobileChatClient.get("userver/redirectServer", params,
				new JsonHttpResponseHandler() {


			@Override
			public void onFailure(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub
				//super.onFailure(arg0, arg1);
				System.out.println("message======>ON FAILURE"+arg1);
			}
			@Override
			public void onSuccess(JSONObject arg0) {

				try {
					System.out.println("转接数据。。。。。。。。。。。"+arg0);

					if (arg0.getBoolean("success")==true) {
						System.out.println("申请转接。。。。。。。。。。。。成功,开始向目标客服转接");
						try {
							redirectTargetMobileChat();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					else {
						System.out.println("转接失败。。。。。。。。。。。。");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}

		});
		return true;
	}
	public boolean redirectTargetMobileChat() throws JSONException,UnsupportedEncodingException
	{
		System.out.println("targetidString"+targetidString+"=================================>>>");
		System.out.println("mobilecookie"+mobilecookie+"=================================>>>");
		HashMap<String, String> paramMap = new HashMap<String, String>();
		//paramMap.put("usname","客服BOBO");
		paramMap.put("unitid", sPhelper.getUnitid());
		paramMap.put("usid", sPhelper.getUsid());
		paramMap.put("uscookie", sPhelper.getUscookie());
		paramMap.put("mobile", mobilecookie);
		paramMap.put("target",targetidString);
		RequestParams params = new RequestParams(paramMap);
		MobileChatClient.get("userver/redirect", params,
				new JsonHttpResponseHandler() {

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(JSONObject arg0) {

				try {
					System.out.println("转接目标客服json数据。。。。。。。。"+arg0);
					if (arg0.getBoolean("success")==true) {
						System.out.println("转接目标客服成功。。。。。。。。");
						
						if(Redirectwindow.window!=null&&Redirectwindow.window.isShowing())
						{
							Redirectwindow.window.dismiss();
						}
						
						FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
						FragmentTransaction ft = fragmentManager.beginTransaction();
						//					TextView txt = (TextView)txt.getText().toString()
						//bundle.putString("msgid",list.get(arg2).);
						ConsultFragment newconsultFragment = new ConsultFragment();
						
						//newconsultFragment.setArguments(bundle);
						ft.replace(android.R.id.tabcontent,newconsultFragment);
						ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);  
						ft.commit();
						LinearLayout layout = (LinearLayout)MainTabActivity.layout;
						if(layout!=null)
						{
							layout.setVisibility(View.VISIBLE);
						}
					}
					else {
						Toast.makeText(context, "转接失败", Toast.LENGTH_LONG);
						System.out.println("转接目标客服失败。。。。。。。。。。。。");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}

		});
		return true;
	}*/
}

