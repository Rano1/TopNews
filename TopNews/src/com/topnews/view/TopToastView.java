package com.topnews.view;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
/** 
 * 头部有新咨询的Toast提示
 * */
public class TopToastView {
	/** 
	 * 初始化Toast提示，并show
	 * */
	public static void initToast(Context context,String top_hint,int img_res){
		Toast topToast = Toast.makeText(context, top_hint, Toast.LENGTH_LONG);
		topToast.setGravity(Gravity.CENTER|Gravity.TOP, 0, 0);
		LinearLayout ll_bg = (LinearLayout) topToast.getView();
		ImageView img_bg = new ImageView(context);
		img_bg.setImageResource(img_res);
		ll_bg.addView(img_bg , 0);
		topToast.show();
	}
}
