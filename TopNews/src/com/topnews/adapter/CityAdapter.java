package com.topnews.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.topnews.R;
import com.topnews.bean.CityEntity;
import com.topnews.tool.DateTools;
import com.topnews.view.HeadListView;
import com.topnews.view.HeadListView.HeaderAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class CityAdapter extends BaseAdapter implements SectionIndexer,
		HeaderAdapter, OnScrollListener {
	private Context mContext;
	private ArrayList<CityEntity> cityList;
	private LayoutInflater inflater = null;
	private List<Integer> mPositions;
	private List<String> mSections;

	public CityAdapter(Context mContext, ArrayList<CityEntity> cityList) {
		this.mContext = mContext;
		this.cityList = cityList;
		inflater = LayoutInflater.from(mContext);
		initDateHead();
	}

	/* 获取头部head标签数据 */
	private void initDateHead() {
		mSections = new ArrayList<String>();
		mPositions = new ArrayList<Integer>();
		for (int i = 0; i < cityList.size(); i++) {
			if (i == 0) {
				mSections.add(String.valueOf(cityList.get(i).getPinyin()));
				mPositions.add(i);
				continue;
			}
			if (i != cityList.size()) {
				if (cityList.get(i).getPinyin() != cityList.get(i - 1).getPinyin()) {
					mSections.add(String.valueOf(cityList.get(i).getPinyin()));
					mPositions.add(i);
				}
			}
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cityList == null ? 0 : cityList.size();
	}

	@Override
	public CityEntity getItem(int position) {
		// TODO Auto-generated method stub
		if (cityList != null && cityList.size() != 0) {
			return cityList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.city_item_content, null);
			mHolder = new ViewHolder();
			mHolder.city_name = (TextView) view.findViewById(R.id.city_name);
			//header
			mHolder.layout_city_section = (LinearLayout) view.findViewById(R.id.layout_city_section);
			mHolder.city_item_section_text = (TextView) view.findViewById(R.id.city_item_section_text);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		CityEntity city = getItem(position);
		mHolder.city_name.setText(city.getName());
		//头部的相关东西
		int section = getSectionForPosition(position);
		if (getPositionForSection(section) == position) {
			mHolder.layout_city_section.setVisibility(View.VISIBLE);
//			head_title.setText(news.getDate());
			mHolder.city_item_section_text.setText(mSections.get(section));
		} else {
			mHolder.layout_city_section.setVisibility(View.GONE);
		}
		return view;
	}

	class ViewHolder {
		TextView city_name;
		//header
		LinearLayout layout_city_section;
		TextView city_item_section_text;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (view instanceof HeadListView) {
			((HeadListView) view).configureHeaderView(firstVisibleItem);
		}
	}

	@Override
	public int getHeaderState(int position) {
		// TODO Auto-generated method stub
		int realPosition = position;
		if (realPosition < 0 || position >= getCount()) {
			return HEADER_GONE;
		}
		int section = getSectionForPosition(realPosition);
		int nextSectionPosition = getPositionForSection(section + 1);
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return HEADER_PUSHED_UP;
		}
		return HEADER_VISIBLE;
	}

	@Override
	public void configureHeader(View header, int position, int alpha) {
		int realPosition = position;
		int section = getSectionForPosition(realPosition);
		String title = (String) getSections()[section];
		((TextView) header.findViewById(R.id.city_item_section_text)).setText(title);
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return mSections.toArray();
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		if (sectionIndex < 0 || sectionIndex >= mPositions.size()) {
			return -1;
		}
		return mPositions.get(sectionIndex);
	}

	@Override
	public int getSectionForPosition(int position) {
		if (position < 0 || position >= getCount()) {
			return -1;
		}
		int index = Arrays.binarySearch(mPositions.toArray(), position);
		return index >= 0 ? index : -index - 2;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
}
