package com.citi.mc.app;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.citi.mc.R;

@SuppressLint("NewApi")
public class EnterpriseData extends Activity{
	private ImageView iv1 = null;
	private Button bt1 = null;
	private Button bt2 = null;
		
	public  void onCreate(Bundle savedInstanceState)
	{   
		
        super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
//     SharedPreferences enterprise_data_sharedPreferences = this.getSharedPreferences("enterprise_data",MODE_WORLD_READABLE);  
//	    
//		final Editor editor = enterprise_data_sharedPreferences.edit();
//		
//	  
//	  iv1 = (ImageView)findViewById(R.id.newenterprise_sign);
//		
//			bt1 = (Button)findViewById(R.id.back);
//			bt2 = (Button)findViewById(R.id.modify);
//			final EditText enterprise_name = (EditText) this.findViewById(R.id.newenterprise_name);  
//	        final EditText contactor = (EditText) this.findViewById(R.id.newcontactor); 
//	        final EditText phone = (EditText) this.findViewById(R.id.newphone);
//	        final EditText phone_site = (EditText) this.findViewById(R.id.newphone_site);
//	        final EditText leave_wordmail = (EditText) this.findViewById(R.id.newleave_word_mail);
//	        String enterprise_name1 = enterprise_data_sharedPreferences.getString("enterprise_name", null);  
//	  	  String contactor1= enterprise_data_sharedPreferences.getString("contactor", null);  
//	  	  String phone1 = enterprise_data_sharedPreferences.getString("phone", null); 
//	  	  String phone_site1 = enterprise_data_sharedPreferences.getString("phone_site", null);
//	  	  String leave_wordmail1 = enterprise_data_sharedPreferences.getString("leave_wordmail", null); 
//	  	  enterprise_name.setText(enterprise_name1); 
//            contactor.setText(contactor1);
//	  	  phone.setText(phone1);
//	  	 phone_site.setText(phone_site1);
//	  	 leave_wordmail.setText(leave_wordmail1);
//	  	
//			bt1.setOnClickListener(new OnClickListener()
//			{   
//				
//				public void onClick(View v)
//				{
//					Intent intent = new Intent();
//					intent.setClass(Enterprise_data.this,MainTabActivity.class);
//					startActivity(intent);
//				}
//			});
//			bt2.setOnClickListener(new OnClickListener()
//			{
//				
//				@Override
//				public void onClick(View v)
//				{
//					  
//		  
//		                editor.putString("enterprise_name", enterprise_name.getText().toString());  
//		                editor.putString("contactor", contactor.getText().toString());
//		                editor.putString("phone", phone.getText().toString());
//		                editor.putString("phone_site", phone_site.getText().toString());
//		                editor.putString("leave_word_mail", leave_wordmail.getText().toString());
//					editor.commit();
//				}
//			});
//	
//	
//	
	}

	
}