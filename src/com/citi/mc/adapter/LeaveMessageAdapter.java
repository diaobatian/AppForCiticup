package com.citi.mc.adapter;


import java.util.List;

import com.citi.mc.bean.LeaveMessage;
import com.citi.mc.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LeaveMessageAdapter extends BaseAdapter {

	private List<LeaveMessage> list;
	private Context context;
	private LayoutInflater layoutInflater;
	public  int count=5;
	private int begin;
	public LeaveMessageAdapter(Context context,List<LeaveMessage>list,int begin)
	{
		this.begin = begin;
		this.context = context;
		this.list = list;
		
		layoutInflater = LayoutInflater.from(context);
	}
	public void addMessage(LeaveMessage msg){
		list.add(msg);
		notifyDataSetChanged();
	}
	
	public void addMessages(List<LeaveMessage> msgList){
		list.addAll(msgList);
		notifyDataSetChanged();
	}
	
	public void clear(){
		list.clear();
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
			convertView = layoutInflater.inflate(R.layout.child_item_layout, null);
			viewHolder.badyImage = (ImageView)convertView.findViewById(R.id.my_img);
			viewHolder.txtview = (TextView)convertView.findViewById(R.id.item_name);
			viewHolder.details = (TextView)convertView.findViewById(R.id.item_detail);
			//viewHolder.image = (ImageView)convertView.findViewById(R.id.im);
			
		}
		else
		{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		convertView.setTag(viewHolder);
		LeaveMessage leaveMessage = null;
		if(begin==0)
		{
			leaveMessage = list.get(position);
		}
		else
		{
			leaveMessage = list.get(position+begin);
		}
		
		viewHolder.badyImage.setImageResource(leaveMessage.getBadyImage());
		//viewHolder.image.setImageResource(leaveMessage.getImage());
		viewHolder.txtview.setText(String.valueOf(leaveMessage.getTime()+" "));
		viewHolder.details.setText(String.valueOf(leaveMessage.getMessage()));

		
		
			
	
		return convertView;
	}
	class ViewHolder
	{
		ImageView badyImage;
		//ImageView image;
		TextView txtview;
		TextView details;
		
	}

}
