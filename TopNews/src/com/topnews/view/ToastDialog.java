package com.topnews.view;

import android.R;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;

public class ToastDialog extends Dialog{

	public ToastDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	public ToastDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public ToastDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
//		View view = inflater.inflate(R.layout., null);
//		this.
	}
	
}
