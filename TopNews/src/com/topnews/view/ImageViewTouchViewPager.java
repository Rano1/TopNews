package com.topnews.view;

import com.topnews.view.imagezoom.ImageViewTouch;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class ImageViewTouchViewPager extends ViewPager {

	public ImageViewTouchViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ImageViewTouchViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean canScroll(View view, boolean arg1, int arg2, int arg3,
			int arg4) {
		if(view instanceof ImageViewTouch){
//			return view.a(arg2);
		}
		return super.canScroll(view, arg1, arg2, arg3, arg4);
	}
}
