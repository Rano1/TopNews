package com.topnews.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ImagePagerAdapter extends PagerAdapter {
	Context context;
	ArrayList<String> imgsUrl;
	LayoutInflater inflater = null;

	public ImagePagerAdapter(Context context, ArrayList<String> imgsUrl) {
		this.context = context;
		this.imgsUrl = imgsUrl;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return imgsUrl == null ? 0 : imgsUrl.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		return super.instantiateItem(container, position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
//		((ViewPager) container).removeView(pageViews.get(position));  
	}
}
