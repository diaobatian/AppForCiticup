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


public class ExpressionAdapter extends BaseAdapter {

	/**
	 * 这个Adpter是PNG 100+张图片的
	 */
	private PackageManager pm;
	private List<Expression> exp;
	private Context context;
	private LayoutInflater layoutInflater;
	private static final int SIZE = 35;
	public ExpressionAdapter(Context context, List<Expression>list,int page)
	{
		exp = new ArrayList<Expression>();
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		int iBegin = page * SIZE;
		int iEnd = iBegin + SIZE;
		while((iBegin < list.size())&&(iBegin < iEnd))
		{
			exp.add(list.get(iBegin));
			iBegin++;
		}
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
//		viewHolder.gifView.setGifImage(experssion.getImage());
////		viewHolder.gifView.setShowDimension(300, 300);
//		viewHolder.gifView.setGifImageType(GifImageType.COVER);
		
		
		viewHolder.imageView.setImageResource(expression.getImage());//yaoyongde....
		
		
		return convertView;
	}
	class ViewHolder
	{
		ImageView imageView;
	}
}
