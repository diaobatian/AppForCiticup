package com.citi.mc.app;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.citi.mc.R;
import com.citi.mc.adapter.RedirectAdapter;
import com.citi.mc.db.Monitor;
import com.citi.mc.utils.MobileManagerClient;
import com.citi.mc.utils.IsNetWorkAvailable;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("CommitTransaction")
public class LoginActivity extends Activity {
	// 判断是否进入登录界面
	public static int isexitCount = 0;
	// 按钮的朝向
	private int btndirect = 1;
	private Button btnLogin = null;
	private String flag;
	private EditText inputpasswordEditText;
	private EditText inputemailEditText;
	private SPhelper sPhelper = null;
	private CheckBox autosavepasswoBox;
	public static Monitor inloginMonitor;
	private List<String> emaiList;
	private ListView emailliListView;
	public View email_window_view;
	private ImageView moreEmailImageView;
	// private PopupWindow inputEmail_childlistWindow;
	private PopupWindow window;
	private Context context;
	// 获得屏幕宽度
	public static int screenWidth;
	private DisplayMetrics dm;
	// 生成的cookie,以时刻生成的毫秒数（相当于GUID）
	public static String randomcookie;
	// 登录提示
	private ProgressBar loginpProgressBar;
	private TextView progressTextView;
	View tableView;
	// 设置页面压缩
	private Handler getMonitorinfoHandler = null;
	// 设置是否移动的标志位
	private Boolean isScroll = false;
	private ScrollView mScrollView = null;
	private ProgressDialog loginDialog = null;
	// 设置软件盘自动隐藏
	private InputMethodManager mInputMethodManager;
	private LinearLayout.LayoutParams linearParams;
	private OnClickListener btnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent1 = new Intent();
			intent1.setClass(LoginActivity.this, FindbackKey.class);
			startActivity(intent1);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		context = this;
		btndirect = 0;
		// 获取屏幕密度
		dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		// 初始化scrollView
		mScrollView = (ScrollView) findViewById(R.id.scroll_view);
		email_window_view = LayoutInflater.from(context).inflate(
				R.layout.email_window, null);
		btnLogin = (Button) this.findViewById(R.id.btnlogin);
		sPhelper = new SPhelper(context);
		emaiList = new ArrayList<String>();
		
		inputemailEditText = (EditText) findViewById(R.id.login_et_momoid);
		 // 取控件mGrid当前的布局参数
		linearParams = (LinearLayout.LayoutParams) inputemailEditText.getLayoutParams();
		linearParams.width = screenWidth-190;// 当控件的高强制设成75象素
		inputemailEditText.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件mGrid2
		moreEmailImageView = (ImageView) findViewById(R.id.get_email);
		autosavepasswoBox = (CheckBox) findViewById(R.id.autosavepassword);
		// 登录提醒
		loginpProgressBar = (ProgressBar) findViewById(R.id.loginProgressBar);
		tableView = (View) findViewById(R.id.progressTable);
		tableView.setVisibility(View.GONE);
		progressTextView = (TextView) findViewById(R.id.progressText);
		// 备注
		loginpProgressBar.setIndeterminate(false);

		if (sPhelper.getAutoSavePassword() != null) {
			if (sPhelper.getAutoSavePassword().equals("true")) {
				autosavepasswoBox.setChecked(true);
			} else {
				autosavepasswoBox.setChecked(false);
			}
		}
		if (sPhelper.getSongState() == null) {
			sPhelper.setSongState("true");
		}
		if (sPhelper.getToggleState() == null) {
			sPhelper.setToggleState("true");
		}
		autosavepasswoBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (((CheckBox) v).isChecked()) {
					System.out.println("自动保存============〉");
					sPhelper.setAutoSavePassword("true");

				} else {
					sPhelper.setAutoSavePassword("false");
				}

			}
		});
		inputpasswordEditText = (EditText) findViewById(R.id.login_et_pwd);
		// 初始化键盘控件
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		inputemailEditText.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		inputpasswordEditText.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				judgement();
				// 设置键盘隐藏
				mInputMethodManager.hideSoftInputFromWindow(
						arg0.getWindowToken(), 0);

			}

		});

		if (Monitor.getAll().size() != 0
				&& Monitor.getMonitorpreLogin() != null) {

			emaiList = Monitor.getAllEmail();

			inputemailEditText.setText(Monitor.getMonitorpreLogin().email);
			if ((Monitor.getMonitorpreLogin().password != null)
					&& autosavepasswoBox.isChecked()) {
				inputpasswordEditText
						.setText(Monitor.getMonitorpreLogin().password);

				if (isexitCount == 0) {
					//不自动登录了
//					btnLogin.performClick();
				}
			}

			moreEmailImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// moreEmailImageView.setBackgroundResource(drawable.dropup);
					//moreEmailImageView.setImageResource(R.drawable.dropup);
					emailliListView = (ListView) email_window_view
							.findViewById(R.id.redirect_subview);
					// List<String> subemailList=new ArrayList<String>();
					// subemailList=emaiList.remove(inputemailEditText.getText().toString());
					RedirectAdapter adapter = new RedirectAdapter(context,
							emaiList);
					emailliListView.setAdapter(adapter);
					creatredirectwindow(v);
					if (window != null) {
						window.setOnDismissListener(new OnDismissListener() {
							public void onDismiss() {
								moreEmailImageView
										.setImageResource(R.drawable.downarrow);
							}
						}

						);
					}
					emailliListView.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// 获得选中项的HashMap对象
									String subemail = (String) emailliListView
											.getItemAtPosition(arg2);
									inputemailEditText.setText(subemail);
									if (autosavepasswoBox.isChecked() == true) {
										inputpasswordEditText.setText(Monitor
												.getMonitorFromEmail(subemail).password);
									}
									if (window != null && window.isShowing()) {
										window.dismiss();
									}

								}

							});
					// inputEmail_childlistWindow.
				}
			});
		} else {
			Toast.makeText(context, "无帐号", Toast.LENGTH_SHORT).show();
		}
		TextView forgetPasswordTextView = (TextView) findViewById(R.id.forgget_key);
		forgetPasswordTextView.setOnClickListener(btnListener);
		getMonitorinfoHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub

				if (msg.what == 0) {
					System.out.println("into msg0000000000000000000000000000");
					tableView.setVisibility(View.GONE);
					loginDialog.dismiss();
					Toast.makeText(context, "网络状况不佳，重新登录", Toast.LENGTH_SHORT)
							.show();
				} else if (msg.what == 1) {
					tableView.setVisibility(View.GONE);
					loginDialog.dismiss();
					Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 2) {
					tableView.setVisibility(View.GONE);
					loginDialog.dismiss();
					Toast.makeText(context, "登录超时", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 3) {
					tableView.setVisibility(View.GONE);
					loginDialog.dismiss();
					Toast.makeText(context, "请输入邮箱", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 4) {
					tableView.setVisibility(View.GONE);
					// 登录框消失
					loginDialog.dismiss();
					Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 5) {
					tableView.setVisibility(View.GONE);
					loginDialog.dismiss();
					Toast.makeText(context, "用户名或者密码错误", Toast.LENGTH_SHORT)
							.show();
				}
			}

		};
		//百度统计
		

	}

	// 登录判断
	public void judgement() {
		try {

			// 网络连接判断 

			if (IsNetWorkAvailable.isAvailable(LoginActivity.this)) {

				if (inputemailEditText.getText().toString().equals("")) {
					System.out.println("111111111111111111");
					Message msg = new Message();
					//3 请输入邮箱
					msg.what = 3;
					getMonitorinfoHandler.sendMessage(msg);
				} else if (inputpasswordEditText.getText().toString()
						.equals("")) {
					System.out.println("2222222222222222222");
					Message msg = new Message();
					//4请输入密码
					msg.what = 4;
					getMonitorinfoHandler.sendMessage(msg);
				} else {
					System.out.println("33333333333333333");
					loginMobileChat();
				}

				tableView.setVisibility(View.GONE);
				// loginpProgressBar.setVisibility(View.VISIBLE);
				// progressTextView.setVisibility(View.VISIBLE);
				// 隐藏进度条
				loginpProgressBar.setProgress(0);
				loginDialog = new ProgressDialog(LoginActivity.this);
				// 设置样式
				loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				loginDialog.setMessage("正在登录，请稍候...");

				loginDialog.show();

			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						LoginActivity.this);
				builder.setIcon(android.R.drawable.ic_dialog_alert);
				builder.setTitle("网络状态");
				builder.setMessage("当前无可用网络，请设置");
				builder.setPositiveButton("设置",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								startActivity(new Intent(
										android.provider.Settings.ACTION_SETTINGS));
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				builder.create().show();
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
   
	/** 返回键点击触发，回到桌面 */
	@Override
	public void onBackPressed() {
		isexitCount = 0;
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean loginMobileChat() throws JSONException,
			UnsupportedEncodingException {

		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("email", inputemailEditText.getText().toString());
		paramMap.put("password", inputpasswordEditText.getText().toString());
		RequestParams params = new RequestParams(paramMap);
		//
		if (Monitor.getMonitorFromEmail(inputemailEditText.getText().toString()) != null)
		{
			if (Monitor.getMonitorFromEmail(inputemailEditText.getText()
					.toString()).uscookie != null)
			{
				//取得保存在本地的cookie
				randomcookie = Monitor.getMonitorFromEmail(inputemailEditText
						.getText().toString()).uscookie;
			} 
			else 
			{
				// 上传一个cookie函数（如果没有就生成一个cookie）
				randomcookie = getUniqueueUscookie();
			}
		} 
		else 
		{
			//此邮箱用户第一次登录 为本次登录生成cookie
			randomcookie = getUniqueueUscookie();
		}
		//服务器验证账户名密码
		MobileManagerClient.post("login", params, new JsonHttpResponseHandler() {
          //API http://loopj.com/android-async-http/doc/com/loopj/android/http/JsonHttpResponseHandler.html
			@Override
			protected Object parseResponse(String arg0) throws JSONException {
				// TODO Auto-generated method stub
				//System.out.println("fnregkrtjhb" + arg0);
				return super.parseResponse(arg0);

			}

			@Override
			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				//2登录超时
				msg.what = 2;
				getMonitorinfoHandler.sendMessage(msg);

			}

			@Override
			public void onSuccess(JSONObject timeline) {
				System.out.println(timeline);

				try {
					flag = (String) timeline.getString("success");
					if (flag.equals("true")) {
						// 第一次登录
						if (Monitor.getMonitorpreLogin() != null) {
							Monitor tmpMonitor = Monitor.getMonitorpreLogin();
							tmpMonitor.islogin = 0;
							tmpMonitor.save();
						}
						if (Monitor.getMonitorFromEmail(inputemailEditText
								.getText().toString()) == null) {
							inloginMonitor = new Monitor();

							inloginMonitor.email = inputemailEditText.getText()
									.toString();
							inloginMonitor.islogin = 1;

							if (autosavepasswoBox.isChecked()) {
								inloginMonitor.password = inputpasswordEditText
										.getText().toString();
							}
							Monitor.saveMonitor(inloginMonitor);
						} else {
							if (Monitor.getMonitorpreLogin() != null) {
								Monitor tmpMonitor = Monitor
										.getMonitorpreLogin();
								tmpMonitor.islogin = 0;
								tmpMonitor.save();
							}
							inloginMonitor = Monitor
									.getMonitorFromEmail(inputemailEditText
											.getText().toString());
							// inloginMonitor.email=inputemailEditText.getText().toString();
							inloginMonitor.islogin = 1;

							if (autosavepasswoBox.isChecked()) {
								inloginMonitor.password = inputpasswordEditText
										.getText().toString();
							}
							inloginMonitor.save();
						}

						if (inloginMonitor.userid == null) {
							try {
								//获得客服信息
								MobileChat();
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							Message msg = new Message();
							//1登录成功
							msg.what = 1;
							getMonitorinfoHandler.sendMessage(msg);

							isexitCount = 0;
							Intent intent = new Intent();
							intent.setClass(LoginActivity.this,
									MainTabActivity.class);
							//转到主菜单
							startActivity(intent);
							overridePendingTransition(R.anim.zoomin,
									R.anim.zoomout);
							LoginActivity.this.finish();
						}
						Monitor monitor = Monitor
								.getMonitorFromEmail(inputemailEditText
										.getText().toString());
						monitor.realcookie = randomcookie;
						monitor.save();
					} else {
						System.out.println("false");
						Message msg = new Message();
						//5用户名或者密码错误
						msg.what = 5;
						getMonitorinfoHandler.sendMessage(msg);

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
		return true;
	}

	//获得客服信息
	public boolean MobileChat() throws JSONException,
			UnsupportedEncodingException {
		sPhelper = new SPhelper(this);
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("mc_type", "android");
		RequestParams params = new RequestParams(paramMap);

		MobileManagerClient.post("uchat", params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub
				super.onFailure(arg0);
				Message msgMessage = new Message();
				msgMessage.what = 0;
				getMonitorinfoHandler.sendMessage(msgMessage);
			}

			@Override
			public void onSuccess(JSONObject timeline) {
				System.out.println(timeline);

				try {
					Monitor newMonitor = inloginMonitor;
					newMonitor.unitid = (String) timeline.getString("unitid");
					newMonitor.uscookie = (String) timeline.getString("uscookie");
					newMonitor.userid = (String) timeline.getString("usid");
					newMonitor.username = (String) timeline.getString("usname");
					newMonitor.welcome_msg = (String) timeline.getString("welcome_msg");
					// 保存数据到数据库
					newMonitor.save();

					isexitCount = 0;
					Message msg = new Message();
					//1登录成功
					msg.what = 1;
					getMonitorinfoHandler.sendMessage(msg);

					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, MainTabActivity.class);

					startActivity(intent);
					LoginActivity.this.finish();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

		return true;
	}

	// 判断点击邮箱按钮是否显示登录过用户
	@SuppressWarnings("deprecation")
	public void creatredirectwindow(View parent) {

		if (window != null) {
			if (!window.isShowing()) {
				
				/* 弹出显示 在指定的位置(parent) 最后两个参数 是相对于 x / y 轴的坐标 */
				window.showAsDropDown(inputemailEditText, -4000, 0);
			}

		} else {
			
			window = new PopupWindow(email_window_view, 600, 1000); // WindowManager.LayoutParams.WRAP_CONTENT
			window.setAnimationStyle(android.R.style.Animation_Dialog);// 设置动画样式
			window.update();

			window.showAsDropDown(inputemailEditText, -5500, 0);
			// 必须设置，否则获得焦点后页面上其他地方点击无响应
			window.setBackgroundDrawable(new BitmapDrawable());
			// 设置焦点，必须设置，否则listView无法响应
			window.setFocusable(true);
			// 设置点击其他地方 popupWindow消失
			window.setOutsideTouchable(false);
			// 显示在某个位置
			window.setTouchable(true);
			window.setAnimationStyle(android.R.style.Animation_Dialog);
			window.update();
			window.getContentView().setFocusableInTouchMode(true);
			window.getContentView().setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if (((keyCode == KeyEvent.KEYCODE_MENU) || (keyCode == KeyEvent.KEYCODE_BACK))
							&& (window.isShowing())) {
						moreEmailImageView
								.setImageResource(R.drawable.downarrow);
						window.dismiss();
						return true;
					}
					return false;
				}
			});
			window.getContentView().setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					moreEmailImageView.setImageResource(R.drawable.downarrow);
					window.dismiss();
					return false;
				}

			});

		}
	}
	@Override
	public void onResume()
	{
		super.onResume();
		//百度统计
		StatService.onResume(this);
		//
	}
	@Override
	public void onPause()
	{
		super.onPause();
		StatService.onPause(this);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// LoginActivity.this.finish();
	}

	// 生成cookie
	private static String getUniqueueUscookie() {
		return "006600" + System.currentTimeMillis()
				+ Math.round(Math.random() * 89999 + 10000);
	}
}