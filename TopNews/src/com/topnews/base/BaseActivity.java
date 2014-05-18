package com.topnews.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	/*
	 * ·µ»Ø
	 */
	public void doBack(View view){
		onBackPressed();
	}
}
