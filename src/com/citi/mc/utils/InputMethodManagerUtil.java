package com.citi.mc.utils;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class InputMethodManagerUtil 
{
	private static InputMethodManager mInputMethodManager;
	Context context;
	private EditText editText;
/*	public InputMethodManagerUtil(Context context,EditText editText)
	{
		this.context=context;
		this.editText=editText;
		
	}*/
	
	public  static void setOnfocusListener(Context context,EditText editText) {
		mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		editText.setOnFocusChangeListener(new OnFocusChangeListener(){
			public void onFocusChange(View arg0, boolean arg1) {
				if(arg0.isFocused()){
					mInputMethodManager.showSoftInput(arg0, 0);
				}
				else{
					mInputMethodManager.hideSoftInputFromWindow(arg0.getWindowToken(), 0);
				}
			}});
	}
	 
	

}