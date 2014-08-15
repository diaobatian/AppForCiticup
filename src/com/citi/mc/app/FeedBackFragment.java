package com.citi.mc.app;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mobstat.StatService;
import com.citi.mc.R;
import com.citi.mc.db.Monitor;
import com.citi.mc.utils.MobileChatClient;
import com.citi.mc.utils.MobileManagerClient;
import com.citi.mc.utils.CustomToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class FeedBackFragment extends Fragment
{

	private Button submitBtn;
//	private Button cancelFeedbackButton;
	private EditText feedbackEditText;
	private CustomToastUtil toastUtil=null;
	private View view;
	//设置软件盘自动隐藏
	private InputMethodManager mInputMethodManager;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.feedback, container,false);
		//设置MainTabActivity的flag为false
		MainTabActivity.flag = false;
		//键盘控制初始化
		mInputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		toastUtil=new CustomToastUtil(getActivity(), view);
		Button iv = (Button)view.findViewById(R.id.cancelfeedback);
		submitBtn=(Button)view.findViewById(R.id.submit);
		feedbackEditText=(EditText)view.findViewById(R.id.feedback_content);
		submitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("onclick...................");
				String content = feedbackEditText.getText().toString().trim();
				//Log.i("feedBack content", content);
				if (content.equals(null) || content.equals("")) {
					//System.out.println("content null.............");
					//toastUtil.showToast("内容为空，请输入");
					Toast.makeText(getActivity(), "内容为空，请输入", Toast.LENGTH_SHORT).show();
				}
				else {
					//System.out.println("content ......not null............");
					try {
						//隐藏软键盘
						mInputMethodManager.hideSoftInputFromWindow(arg0.getWindowToken(), 0);
						submitSuggestion();
						feedbackEditText.setText("");
						
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});
		iv.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				show(1);
			}
		});
		return view;
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
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
	private void show(int index)
	{
		FragmentManager manager = getFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.setCustomAnimations(R.anim.push_left_out,R.anim.push_left_zhong);
		Fragment fragment = (Fragment)getFragmentManager().findFragmentById(android.R.id.tabcontent);
		MainTabActivity.layout.setVisibility(View.VISIBLE);
		switch (index)
		{
		case 1:
		
			fragment = new SetFragement();
			ft.replace(android.R.id.tabcontent, fragment);
			break;
			//不知道有什么用 以前遗留下来的
		case 4:
			
			fragment = new SetFragement();
			ft.replace(android.R.id.tabcontent, fragment);
		default:
			break;
		}
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);  

		ft.addToBackStack(null);  
		ft.commit(); 
	}
	public boolean submitSuggestion() throws JSONException, UnsupportedEncodingException
	{


		HashMap<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("content",feedbackEditText.getText().toString());

		// paramMap.put("password",inputpasswordEditText.getText().toString());



		RequestParams params = new RequestParams(paramMap);

		MobileManagerClient.post("sendsuggest", params, new JsonHttpResponseHandler() {
			
			
			
			@Override
			protected Object parseResponse(String arg0) throws JSONException {
				// TODO Auto-generated method stub
				//System.out.println("parse string ........................"+arg0);
				JSONObject jsonObject=new JSONObject(arg0);
				//System.out.println("parse string ........................"+jsonObject);
				String fString = jsonObject.getString("success");
				//System.out.println(fString+"=====================>>>");
				Boolean flag=(Boolean)jsonObject.getBoolean("success");
				//System.out.println(flag+"flag====================");
				Message message = Message.obtain();
				
				if (flag.equals(true)) {
					message.what=1;
					
				}
				else {
					message.what=2;
					
				}
				feedBackHandler.sendMessage(message);
				return super.parseResponse(arg0);
			}
		});
		return true;
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		MainTabActivity.layout.setVisibility(View.VISIBLE);
	}

	private Handler feedBackHandler  = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int flag = msg.what;
			switch (flag) {
			case 1:
//				toastUtil.showToast("提交成功");
				//System.out.println("成功了啊============");
				Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
				show(1);
				break;
			case 2:
				Toast.makeText(getActivity(), "提交失败", Toast.LENGTH_SHORT).show();
				//System.out.println("失败了啊================");
				//toastUtil.showToast("提交失败");
				break;
			default:
				break;
			}
		}
		
	};
}
