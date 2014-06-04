package com.topnews;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.topnews.adapter.CityAdapter;
import com.topnews.base.BaseActivity;
import com.topnews.bean.CityEntity;
import com.topnews.tool.Constants;
import com.topnews.view.HeadListView;

public class CityListActivity extends BaseActivity {
	private TextView title;
	private HeadListView mListView;
	private ArrayList<CityEntity> cityList;
	private CityAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city);
		initView();
		initData();
	}
	
	private void initView() {
		title = (TextView) findViewById(R.id.title);
		mListView = (HeadListView)findViewById(R.id.cityListView);
	}
	
	private void initData() {
		title.setText("当前城市-杭州");
		cityList = Constants.getCityList();
		mAdapter = new CityAdapter(this, cityList);
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(mAdapter);
		mListView.setPinnedHeaderView(LayoutInflater.from(this).inflate(R.layout.city_item_section, mListView, false));
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
//				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
	}
}
