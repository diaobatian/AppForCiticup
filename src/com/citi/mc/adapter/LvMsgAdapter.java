package com.citi.mc.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.citi.mc.db.Lvmsg;
import com.citi.mc.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LvMsgAdapter extends BaseAdapter {

	private List<Lvmsg> list;
	private Context context;
	private LayoutInflater layoutInflater;
	public int count = 5;
	private int begin;

	public LvMsgAdapter(Context context, List<Lvmsg> list) {
		this.context = context;
		this.list = list;

		layoutInflater = LayoutInflater.from(context);
	}

	public void addMessage(Lvmsg msg) {
		list.add(msg);
		notifyDataSetChanged();
	}

	public void addMessages(List<Lvmsg> msgList) {
		list.addAll(msgList);
		notifyDataSetChanged();
	}

	public void clear() {
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
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.child_item_layout,
					null);
			viewHolder.linear01 = (LinearLayout) convertView
					.findViewById(R.id.linear01);
			viewHolder.txtview = (TextView) convertView
					.findViewById(R.id.item_name);
			viewHolder.details = (TextView) convertView
					.findViewById(R.id.item_detail);
			viewHolder.timeTextView = (TextView) convertView
					.findViewById(R.id.item_time);
			viewHolder.badyImage = (ImageView) convertView
					.findViewById(R.id.my_img);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		convertView.setTag(viewHolder);
		Lvmsg leaveMessage = null;

		leaveMessage = list.get(position);

		String jie = String.valueOf(leaveMessage.getcontact());
		Log.v("lvMsgAdapter", jie);
		String[] stra = jie.split("[:]");
		Log.v("lvMsgAdapter", stra.toString());
		int tlen = stra.length;
		if (tlen == 2)
			viewHolder.txtview.setText(stra[1]);
		else if (tlen == 1)
			viewHolder.txtview.setText(stra[0]);
		else
			viewHolder.txtview.setText("");
			
		viewHolder.details.setText(String.valueOf(leaveMessage.getcontent()));
		String time = String.valueOf(leaveMessage.getcreatedTime());
		String[] strs = time.split("[ ]");
		String[] str = strs[0].split("[-]");
		// 设定时间的模板
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 当前时间
		String date = sdf.format(new Date());
		String[] xianzai = date.split("[ ]");
		String[] year = xianzai[0].split("[-]");
		//timeLong < 60 * 60 * 24
		if(year[2].trim().equals(str[2].trim()) && year[1].trim().equals(str[1].trim()) && year[0].trim().equals(str[0].trim())) {

			viewHolder.timeTextView.setText(strs[1].substring(0, 5));

		} else if ((Integer.valueOf(year[2]) - Integer.valueOf(str[2])) == 1   && year[1].trim().equals(str[1].trim()) && year[0].trim().equals(str[0].trim())) {
			viewHolder.timeTextView.setText("昨天 ");
		} else if ((Integer.valueOf(year[2]) - Integer.valueOf(str[2])) == 2   && year[1].trim().equals(str[1].trim()) && year[0].trim().equals(str[0].trim())) {
			viewHolder.timeTextView.setText("前天 ");
		}else{
			viewHolder.timeTextView.setText(str[0]);
		}

		if(stra[0].equals("电话")){
			viewHolder.badyImage.setBackgroundResource(R.drawable.phone_selector);
		}else if(stra[0].equals("QQ")){
			viewHolder.badyImage.setBackgroundResource(R.drawable.qq_selector);
		}else if(stra[0].equals("邮箱")){
			viewHolder.badyImage.setBackgroundResource(R.drawable.mail_selector);
		}

		if (position % 2 == 0) {
			viewHolder.linear01.setBackgroundResource(R.drawable.list_view);
		} else {
			viewHolder.linear01.setBackgroundResource(R.drawable.list_view2);
		}

		return convertView;
	}

	class ViewHolder {
		ImageView badyImage;
		LinearLayout linear01;
		TextView txtview;
		TextView details;
		// ImageView lvmsgDreImageView;
		TextView timeTextView;
	}
}
