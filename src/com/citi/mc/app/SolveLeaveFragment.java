package com.citi.mc.app;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.ClipboardManager;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.citi.mc.R;
import com.citi.mc.db.Lvmsg;
import com.citi.mc.db.Monitor;
import com.citi.mc.utils.MobileManagerClient;
import com.citi.mc.utils.IsNetWorkAvailable;
import com.citi.mc.utils.PhoneParser;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 留言处理界面
 * 
 * @author ChunTian
 * 
 */
@SuppressLint("NewApi")
public class SolveLeaveFragment extends Fragment implements OnGestureListener {
	private EditText solve_remarkEditText;
	private String msgidfromlist;
	private SPhelper sPhelper = null;
	private Button submitBtn,BackBtn,callBtn,emailBtn,qqBtn;
	private Monitor tmpMonitor = null;
	private int verticalMinDistance = 20;
	private int minVelocity = 0;
	// 设置软件盘自动隐藏
	private InputMethodManager mInputMethodManager;
	private TextView solveTextView,qqTextView,dianhuaTextView,solve_timeTextView,lvmsg_txt;
	private LinearLayout linear1,linear2,linear3,linear4,solve_linear;
	private GestureDetector mGestureDetector;
	final HashMap<String, String> usinfoMap = new HashMap<String, String>();
	private String[] strs;
	Lvmsg newlvmsg = new Lvmsg();

	@SuppressWarnings("static-access")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.slove_leavemsg, container, false);
		solve_remarkEditText = (EditText) view.findViewById(R.id.leave_msg_edt);
		tmpMonitor = MainTabActivity.monitor;
		submitBtn = (Button) view.findViewById(R.id.solve_submit);
		submitBtn.setEnabled(true);
		BackBtn = (Button) view.findViewById(R.id.solve_back);
		callBtn = (Button) view.findViewById(R.id.solve_call);
		emailBtn = (Button) view.findViewById(R.id.solve_email);
		qqBtn = (Button) view.findViewById(R.id.solve_qq);
		solveTextView = (TextView) view.findViewById(R.id.youjian);
		qqTextView = (TextView) view.findViewById(R.id.qq);
		dianhuaTextView = (TextView) view.findViewById(R.id.dianhua);
		lvmsg_txt = (TextView) view.findViewById(R.id.leave_msg_text);
		solve_timeTextView = (TextView) view
				.findViewById(R.id.sovle_msg_time);
		linear1 = (LinearLayout) view.findViewById(R.id.linear1);
		linear2 = (LinearLayout) view.findViewById(R.id.linear2);
		linear3 = (LinearLayout) view.findViewById(R.id.linear3);
		linear4 = (LinearLayout) view.findViewById(R.id.linear4);
		solve_linear = (LinearLayout) view.findViewById(R.id.solve_linear);
		
		// 设置MainTabActivity的flag为false
		MainTabActivity.flag = false;
		// 初始化键盘管理
		mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

		BackBtn.setOnClickListener(onClickListener);
		submitBtn.setOnClickListener(onClickListener);
		callBtn.setOnClickListener(onClickListener);
		emailBtn.setOnClickListener(onClickListener);
		solveTextView.setOnClickListener(onClickListener);
		qqBtn.setOnClickListener(onClickListener);
		qqTextView.setOnClickListener(onClickListener);
		dianhuaTextView.setOnClickListener(onClickListener);
		
		String strr = getArguments().getString("head_msg");
		String stre = getArguments().getString("time");
		strs = strr.split("[:]");
		if (strs[0].equals("电话")) {
			linear2.setVisibility(View.VISIBLE);
			dianhuaTextView.setText(strs[1]);
			System.out.println("这个是一个电话啊。。。。。。。。。。。。。。");
			
		} else if (strs[0].equals("QQ")) {
			linear1.setVisibility(View.VISIBLE);
			qqTextView.setText(strs[1]);
			
		} else if (strs[0].equals("邮箱")) {
			linear3.setVisibility(View.VISIBLE);
			solveTextView.setText(strs[1]);
			System.out.println("这个是一个邮箱啊。。。。。。。。。。。。。");
		}

		
		solve_timeTextView.setText(stre.trim().substring(5, 16));
		PhoneParser phoneParser = new PhoneParser();
		final String msg = strr;
		msgidfromlist = getArguments().getString("msgid");
		
		lvmsg_txt.setText(msg);
		
		lvmsg_txt.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				ClipboardManager copy = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
				copy.setText(null);
				copy.setText(msg+"");
				Toast.makeText(getActivity(), "复制成功", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return mGestureDetector.onTouchEvent(event);
			}
		});

		mGestureDetector = new GestureDetector((OnGestureListener) this);

		return view;
	}

	View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.solve_back:
				Bundle bundle = new Bundle();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.setCustomAnimations(R.anim.push_left_out,R.anim.push_left_zhong);
				bundle.putString("processor", "true");
				LeaveMsgFragment newLvmsgFragment = new LeaveMsgFragment();
				newLvmsgFragment.setArguments(bundle);
				//MainTabActivity.layout.setVisibility(View.VISIBLE);
				
				ft.replace(android.R.id.tabcontent, newLvmsgFragment);
				//ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null);
				ft.commit();
				break;
			case R.id.solve_submit:
				
				try {
					
					System.out.println("开始处理留言===================================");
//					IsNetWorkAvailable isNetWorkAvailable = new IsNetWorkAvailable();
					if (IsNetWorkAvailable.isAvailable(getActivity())) 
					{
						SolveLvmsgMobileChat();
						mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
					} else {
						IsNetWorkAvailable.setNetWork(getActivity(),getActivity());
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
				break;
			case R.id.solve_call:
			case R.id.dianhua:
				
				Intent callIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + strs[1]));
				startActivity(callIntent);
				
				break;
			case R.id.solve_email:
			case R.id.youjian:	
				
				fayoujian(strs[1]);
				
				break;
			case R.id.solve_qq:
			case R.id.qq:
				ClipboardManager copy = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
				copy.setText(null);
				copy.setText(strs[1]);
				Toast.makeText(getActivity(), "复制成功", Toast.LENGTH_SHORT).show();
				
				break;
			default:
				break;
			}
		}
	};
	
	
	/**
	 * 
	 * @param youjian
	 * @类名 SolveLeaveFragment.java
	 * @包名 com.example.mobiletest
	 * @作者 ChunTian  
	 * @时间 2013年11月27日 下午5:25:06
	 * @Email ChunTian1314@vip.qq.com
	 * @版本 V1.0 
	 * @功能 实现发送邮件功能
	 */
	private void fayoujian(String youjian){
		// 邮箱
		String[] reciver = new String[] {youjian};
		String[] mySbuject = new String[] { "test" };
		String myCc = "请输入主题";
		String mybody = "请输入内容";
		Intent myIntent = new Intent(
				android.content.Intent.ACTION_SEND);
		myIntent.setType("plain/text");
		myIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				reciver);
		myIntent.putExtra(android.content.Intent.EXTRA_CC, myCc);
		myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				mySbuject);
		myIntent.putExtra(android.content.Intent.EXTRA_TEXT, mybody);
		startActivity(Intent.createChooser(myIntent, "mail test"));
	}
	
	public boolean SolveLvmsgMobileChat() throws JSONException,UnsupportedEncodingException {
		sPhelper = new SPhelper(getActivity());
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("lvmsgid", msgidfromlist);
		if (solve_remarkEditText.getText().toString().trim() != solve_remarkEditText.getHint()) 
		{
			paramMap.put("remark", solve_remarkEditText.getText().toString());
		} else 
		{
			Toast.makeText(getActivity(), "请输入处理信息", Toast.LENGTH_SHORT).show();
		}
		paramMap.put("processor", "true");
		// paramMap.put("usname", usinfoMap.get("usname"));
		paramMap.put("usname", tmpMonitor.username);// "客服BOBO"
		paramMap.put("unitid", tmpMonitor.unitid);
		paramMap.put("usid", tmpMonitor.userid);
		paramMap.put("cookie", tmpMonitor.uscookie);
		RequestParams params = new RequestParams(paramMap);

		MobileManagerClient.get("userver/saveLeaveMsg", params,tmpMonitor.uscookie,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(Throwable arg0, String arg1) {
						// TODO Auto-generated method stub
						// super.onFailure(arg0, arg1);
						System.out.println("message======>ON FAILURE" + arg1);
					}

					@Override
					public void onSuccess(JSONObject arg0) {
						try {
							if (arg0.getString("success") == "true")
							{
								System.out.println("留言已处理，开始广播。。。。。。。。。。。。。");
								if (IsNetWorkAvailable.isAvailable(getActivity()))
								{
									removeFromListLeaveMessage(msgidfromlist);
									if(LeaveMsgFragment.leaveMessageAdapter!=null)
										LeaveMsgFragment.leaveMessageAdapter.notifyDataSetChanged();
//									if(MainTabActivity.listLeaveMessage.size()>0&&MainTabActivity.listLeaveMessage.size()<100)
//										MainTabActivity.rb2.setBadgeText(MainTabActivity.listLeaveMessage.size()+"");
//									else if(MainTabActivity.listLeaveMessage.size()>=100)
//									{
//										MainTabActivity.rb2.setBadgeText("...");
//									}
//									else
//									{
//										MainTabActivity.rb2.hideBadge();
//									}
									if(MainTabActivity.listLeaveMessage.size()>0)
									{
										MainTabActivity.rb2.showBadge();
									}
									else
									{
										MainTabActivity.rb2.hideBadge();
									}
									messageHandler.sendEmptyMessage(2);
								} else 
								{
									getActivity().startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
								}

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("message======>" + arg0);
					}

				});
		return true;
	}

	

	//清楚留言消息之后同步更新MainTabListActivity
	private boolean removeFromListLeaveMessage(String msgid)
	{
		System.out.println("MainTabActivity id ========="+MainTabActivity.listLeaveMessage);
		System.out.println("msgid ========="+msgid);
		boolean flag = false;
		for (int i = MainTabActivity.listLeaveMessage.size()-1; i >=0 ; i--) {
			if(MainTabActivity.listLeaveMessage.get(i).getmId().equals(msgid))
			{
				flag = true;
				MainTabActivity.listLeaveMessage.remove(i);
				break;
			}
		}
		return flag;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

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
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (e1 == null) {
			return false;
		}
		if (e2.getX() - e1.getX() > verticalMinDistance
				&& Math.abs(velocityX) > minVelocity) {

			// BackBtn.callOnClick();

			return true;
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		MainTabActivity.layout.setVisibility(View.VISIBLE);
	}

	private Handler messageHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle bundle = new Bundle();
			if (msg.what == 1) {
				Toast.makeText(getActivity(), "这是个错误的电话号码！", Toast.LENGTH_SHORT).show();
			}
			if (msg.what == 2) {
				Toast.makeText(getActivity(), "留言已处理", Toast.LENGTH_LONG).show();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.setCustomAnimations(R.anim.push_left_out,R.anim.push_left_zhong);
				bundle.putString("processor", "true");
				LeaveMsgFragment newLvmsgFragment = new LeaveMsgFragment();
				newLvmsgFragment.setArguments(bundle);
				MainTabActivity.layout.setVisibility(View.VISIBLE);
				ft.replace(android.R.id.tabcontent,newLvmsgFragment);
				//ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null);
				ft.commit();
			}
		}

	};

}
