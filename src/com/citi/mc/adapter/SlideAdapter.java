package com.citi.mc.adapter;


import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class SlideAdapter extends PagerAdapter {

	private List<View>gridList;
	public SlideAdapter(Context context,List<View>list)
	{
		this.gridList = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return gridList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		((ViewPager)container).addView(gridList.get(position));
		return gridList.get(position);
	}
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager)container).removeView((View)object);
	}
}
