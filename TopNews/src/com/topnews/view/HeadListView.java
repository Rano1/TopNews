package com.topnews.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
/**
 * 重写的ListView,让每条新闻的时间显示
 */
public class HeadListView extends ListView {

	public interface HeaderAdapter {
		public static final int HEADER_GONE = 0;
		public static final int HEADER_VISIBLE = 1;
		public static final int HEADER_PUSHED_UP = 2;

		int getHeaderState(int position);

		void configureHeader(View header, int position, int alpha);
	}

	private static final int MAX_ALPHA = 255;

	private HeaderAdapter mAdapter;
	private View mHeaderView;
	private boolean mHeaderViewVisible;
	private int mHeaderViewWidth;
	private int mHeaderViewHeight;

	public HeadListView(Context context) {
		super(context);
	}

	public HeadListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HeadListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (mHeaderView != null) {
			mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			configureHeaderView(getFirstVisiblePosition());
		}
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mHeaderView != null) {
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
			mHeaderViewWidth = mHeaderView.getMeasuredWidth();
			mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		}
	}

	public void setPinnedHeaderView(View view) {
		mHeaderView = view;
		if (mHeaderView != null) {
			//listview的上边和下边有黑色的阴影。xml中： android:fadingEdge="none"  
			setFadingEdgeLength(0);
		}
		requestLayout();
	}

	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		mAdapter = (HeaderAdapter) adapter;
	}

	public void configureHeaderView(int position) {
		if (mHeaderView == null) {
			return;
		}
		int state = mAdapter.getHeaderState(position);
		switch (state) {
		case HeaderAdapter.HEADER_GONE: {
			mHeaderViewVisible = false;
			break;
		}

		case HeaderAdapter.HEADER_VISIBLE: {
			mAdapter.configureHeader(mHeaderView, position, MAX_ALPHA);
			if (mHeaderView.getTop() != 0) {
				mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			}
			mHeaderViewVisible = true;
			break;
		}

		case HeaderAdapter.HEADER_PUSHED_UP: {
			View firstView = getChildAt(0);
			int bottom = firstView.getBottom();
			int headerHeight = mHeaderView.getHeight();
			int y;
			int alpha;
			if (bottom < headerHeight) {
				y = (bottom - headerHeight);
				alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
			} else {
				y = 0;
				alpha = MAX_ALPHA;
			}
			mAdapter.configureHeader(mHeaderView, position, alpha);
			if (mHeaderView.getTop() != y) {
				mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
			}
			mHeaderViewVisible = true;
			break;
		}
		}
	}

	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mHeaderViewVisible) {
			drawChild(canvas, mHeaderView, getDrawingTime());
		}
	}
}
