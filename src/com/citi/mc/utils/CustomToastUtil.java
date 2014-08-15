package com.citi.mc.utils;

import com.citi.mc.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToastUtil 
{
	
	Context context;
	View view;
	View layout;
	TextView text;
	public CustomToastUtil(Context context,View view)
	{
		
		this.context=context;
		this.view=view;
	//	LayoutInflater inflater = (LayoutInflater)context.getLayoutInflater();
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		 this.layout = inflater.inflate(R.layout.custom_toast, (ViewGroup)
		view.findViewById(R.id.toast_layout_root));
		  this.text = (TextView) layout.findViewById(R.id.toastText);
	}
	public void showToast(String textsString)
	{
		Toast t = new Toast(context);
		text.setText(textsString);
		t.setDuration(Toast.LENGTH_LONG);
		t.setView(layout);
		t.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
		t.show();
	}
	
}
