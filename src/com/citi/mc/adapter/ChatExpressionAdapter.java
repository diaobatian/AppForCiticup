package com.citi.mc.adapter;

import java.util.ArrayList;
import java.util.List;

import com.citi.mc.bean.Expression;
import com.citi.mc.R;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.IInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


//导入giftview包

public class ChatExpressionAdapter extends BaseAdapter {

	/**
	 * 这个Adpter是PNG 35张图片的
	 */
	private PackageManager pm;
	private List<Expression> exp;
	private Context context;
	private LayoutInflater layoutInflater;
	private static final int SIZE = 20;
	public ChatExpressionAdapter(Context context, List<Expression>list)
	{
		
		exp = new ArrayList<Expression>();
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return exp.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if(convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.image, null);
//			viewHolder.gifView = (GiftView)convertView.findViewById(R.id.image_view);
			viewHolder.imageView = (ImageView)convertView.findViewById(R.id.image_view);
			
		}
		else
		{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		convertView.setTag(viewHolder);
		Expression expression = exp.get(position);
		
		
		viewHolder.imageView.setImageResource(expression.getImage());//yaoyongde....
		
		return convertView;
	}
	class ViewHolder
	{
		ImageView imageView;
	}

}
