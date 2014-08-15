package com.citi.mc.app;


/**
 * 
 * 留言界面
 */
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mobstat.StatService;
import com.citi.mc.R;
import com.citi.mc.adapter.LvMsgAdapter;
import com.citi.mc.db.Lvmsg;
import com.citi.mc.utils.MobileManagerClient;
import com.citi.mc.utils.IsNetWorkAvailable;
import com.citi.mc.utils.PullToRefreshListView;
import com.citi.mc.utils.PullToRefreshListView.OnRefreshListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
/*
 * 留言列表的Fragment
 * refactor 
 * author:lianma 
 * time :2013.11.7
 * 
 * version:1.1
 * changelog:
 * 暂时去掉异步请求工具
 * bug resolver：解决点击未读留言数字变动的问题
 * 留言消息获取机制的改变，直接从网络获取
 * */
@SuppressLint("NewApi")
public class LeaveMsgFragment extends ListFragment  {

	
	private ListView listView;
	public static LvMsgAdapter leaveMessageAdapter;
	public  List<Lvmsg>list = null;
	public Context context;
	public Activity activity;
	private View view=null;
    private TextView netWorkTextView = null;
    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = getActivity();
		activity = getActivity();
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 view = inflater.inflate(R.layout.activity_main, container, false);
		//设置MainTabActivity的flag为true
		MainTabActivity.flag = true;
		
		System.out.println(MainTabActivity.listLeaveMessage+"leavemessagefragment");
		leaveMessageAdapter =new LvMsgAdapter(context, MainTabActivity.listLeaveMessage);
		listView =(ListView)view.findViewById(android.R.id.list);
		listView.setAdapter(leaveMessageAdapter);
		
		if(MainTabActivity.layout.getVisibility()==View.GONE)
		{
			MainTabActivity.layout.setVisibility(View.VISIBLE);
		}
		System.out.println("show2Createview()程序运行时间： "+(System.currentTimeMillis()-MainTabActivity.testtime)+"ms");
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		// TODO Auto-generated method stub
		if(IsNetWorkAvailable.isAvailable(activity))
		{
			if(netWorkTextView!=null)
			{
				netWorkTextView.setVisibility(View.GONE);
			}
		}
	
		
	}
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
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		Bundle bundle = new Bundle();
		try 
		{
			System.out.println("====================>");
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.setCustomAnimations(R.anim.push_left_in,R.anim.push_left_in1);
			bundle.putString("msgid",MainTabActivity.listLeaveMessage.get(position).getmId());
			bundle.putString("head_msg", MainTabActivity.listLeaveMessage.get(position).getcontent());
			bundle.putString("time", MainTabActivity.listLeaveMessage.get(position).getcreatedTime());
			//跳转到留言处理界面
			SolveLeaveFragment solveLeaveFragment = new SolveLeaveFragment();
			//v.setBackgroundResource(R.color.light_gray);
			solveLeaveFragment.setArguments(bundle);
			MainTabActivity.layout.setVisibility(View.GONE);
			ft.replace(android.R.id.tabcontent,solveLeaveFragment);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);  
			ft.addToBackStack(null);  
			ft.commit();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}