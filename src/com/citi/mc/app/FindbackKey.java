package com.citi.mc.app;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.json.JSONException;

import com.citi.mc.R;
import com.citi.mc.utils.MobileManagerClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class FindbackKey extends Activity {

	private Button bt1 = null;
	private Button bt2 = null;
	private EditText emailEditText = null;

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findback_key);
		bt1 = (Button) findViewById(R.id.back);
		bt2 = (Button) findViewById(R.id.send_email);
		bt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(FindbackKey.this, LoginActivity.class);
				startActivity(intent);
				FindbackKey.this.finish();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if(!findPassword()){
						Intent intent = new Intent();
						intent.setClass(FindbackKey.this, LoginActivity.class);
						startActivity(intent);
						FindbackKey.this.finish();
					}
					
					

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	/** 返回键点击触发，回到登录界面 */
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.setClass(FindbackKey.this, LoginActivity.class);
		startActivity(intent);
		FindbackKey.this.finish();
	}

	public boolean findPassword() throws JSONException,
			UnsupportedEncodingException {

		HashMap<String, String> paramMap = new HashMap<String, String>();
		emailEditText = (EditText) findViewById(R.id.newinput_your_email);
		paramMap.put("email", emailEditText.getText().toString());//

		RequestParams params = new RequestParams(paramMap);

		MobileManagerClient.post("password/find", params,
				new AsyncHttpResponseHandler() {

					// 获得服务器返回
					@Override
					public void onSuccess(String arg0) {
						// TODO Auto-generated method stub
						super.onSuccess(arg0);

						if (arg0.equals("no this user.")) {
							Toast.makeText(FindbackKey.this, "输入邮箱不存在，请重新输入",
									2000).show();
						} else {
							Toast.makeText(FindbackKey.this, "邮件已发送，请注意查收",
									2000).show();
						}

					}

				});
		return true;
	}

}
