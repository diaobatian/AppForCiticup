package com.citi.mc.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.citi.mc.adapter.RedirectAdapter;
import com.citi.mc.R;
import com.citi.mc.utils.MobileChatClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Redirectwindow extends PopupWindow
{
	public static PopupWindow window;
	
	private ListView redirectlistView;
	private TextView nameTextView;
	private TextView phoneTextView;
	private TextView qQTextView;
	private TextView isappoinTextView;
	private List<String>str=null;
	HashMap<String, Object> MonitorUsmap=null;
	Context context;
	View view;
	public Redirectwindow(List<String> str,Context context,View v ){
		this.context=context;
		this.str=str;
		creatredirectwindow(v);
	}
	public Redirectwindow(Context context,View v ){
		this.context=context;
	
		creatredirectwindow(v);
	}
	public Redirectwindow(HashMap<String, Object> list,Context context,View v ){
		this.context=context;
		this.MonitorUsmap=list;
		creatredirectwindow(v);
	}
    public void creatredirectwindow(View parent) { 
		
		
		//PopupWindow window=new PopupWindow();
		if(window != null){
			System.out.println("window  is  not  null ......................");
			if(!window.isShowing()){
				System.out.println("window is not showing ...........................");
				/*弹出显示   在指定的位置(parent)  最后两个参数 是相对于 x / y 轴的坐标*/
				
				window.showAsDropDown(parent.findViewById(R.id.redirct_button),-200,0); 
				//window.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, 250,180);
				 //window.dismiss();
				 }
		}
		else {
			System.out.println("window is null, so  recreat..........................");
		
			
			//LayoutInflater lay = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		//View v = lay.inflate(R.layout.redirect_window，NULL);
		
		View v = LayoutInflater.from(context).inflate(R.layout.redirect_window, null); 
		nameTextView=(TextView)v.findViewById(R.id.nametext);
		phoneTextView=(TextView)v.findViewById(R.id.phonetext);
		qQTextView=(TextView)v.findViewById(R.id.qqtext);
		if (!MonitorUsmap.isEmpty()) {
			nameTextView.setText(MonitorUsmap.get("name").toString());
			phoneTextView.setText(MonitorUsmap.get("phone").toString());
			qQTextView.setText(MonitorUsmap.get("QQ").toString());
		}
		//redirectlistView = (ListView) v.findViewById(R.id.redirect_subview); 
//		window.setContentView(parent);//设置包含视图r
	//	redirectlistView.setAdapter(new ArrayAdapter<String>(context,R.layout.newredirect_listview,strings));
	     

		//设置进场动画：
//	RedirectAdapter adapter = new RedirectAdapter(context,str); 

		
	//	RedirectAdapter adapter = new RedirectAdapter(context, mData); 
		
		
	/*	redirectlistView.setAdapter(adapter);
	
		redirectlistView.setItemsCanFocus(false); 
		redirectlistView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); */
	//	redirectlistView.setOnItemClickListener(listClickListener); 
		 window = new PopupWindow(v, 500, 360); 
		//window = new PopupWindow(v,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
		

		 window.setAnimationStyle(android.R.style.Animation_Dialog);//设置动画样式
		
		 window.update();

		// window.showAtLocation(v, Gravity.LEFT | Gravity.TOP,250,180);
		 window.showAsDropDown(parent.findViewById(R.id.redirct_button),-100,0);
		
		// 必须设置，否则获得焦点后页面上其他地方点击无响应  
		window.setBackgroundDrawable(new BitmapDrawable());  
		// 设置焦点，必须设置，否则listView无法响应  
		window.setFocusable(true);  
		// 设置点击其他地方 popupWindow消失    
		window.setOutsideTouchable(false);  
		// 显示在某个位置 
		window.setTouchable(true);
		//window.showAsDropDown(parent);
		window.setAnimationStyle(android.R.style.Animation_Dialog);
    	window.update();
    	
    	
    	window.getContentView().setFocusableInTouchMode(true);    
    	window.getContentView().setOnKeyListener(new OnKeyListener() {    
            @Override    
            public boolean onKey(View v, int keyCode, KeyEvent event) {    
                // TODO Auto-generated method stub    
                if (((keyCode == KeyEvent.KEYCODE_MENU)||(keyCode==KeyEvent.KEYCODE_BACK))&&(window.isShowing())) {    
                    window.dismiss(); 
                    return true;    
                }    
                return false;    
            }    
        });
    	window.getContentView().setOnTouchListener(new OnTouchListener(){  
		
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				//window.setFocusable(false);
				window.dismiss();
				return false;
			}  
			       
			    });	
			
		}
	}
   private List<HashMap<String, Object>> getData() {
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
 
       HashMap<String, Object> map = new HashMap<String, Object>();
       map.put("name", "qt1");
        map.put("details", 1);
        map.put("image", R.drawable.ic_launcher);
       list.add(map);
 
      map = new HashMap<String, Object>();
       map.put("name", "qt163");
       map.put("details", 0);
        map.put("image", R.drawable.ic_launcher);
      list.add(map);

  /*     map = new HashMap<String, Object>();
        map.put("name", "qt2");
        map.put("details", 0);
       map.put("image", R.drawable.ic_launcher);
       list.add(map);*/
       return list;
  }	
   
    
}