package app.cn.aiyouv.www.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.common.C;


public class Loading extends Dialog{
	/**
	 * 删除弹出框
	 */
	public static Loading commomP;
	private Context context = null;
	public Loading(Context context) {
		super(context);
		this.context = context;
	}
	public Loading(Context context,int theme) {
		super(context,theme);
		this.context = context;
	}


	public static synchronized void showSheet(Context context) {
		if(commomP==null){
			commomP =   new Loading(context, R.style.delete_pop_style);
		}

//		dlg = new Dialog(context, R.style.delete_pop_style);

		commomP.setCanceledOnTouchOutside(true);
		commomP.setContentView(R.layout.loading);
		commomP.getWindow().getAttributes().gravity = Gravity.CENTER;
		commomP.show();
		//return commomP;
	}
	public static synchronized void clear(){
		if(commomP!=null){
			C.p("取消加载");
			commomP.dismiss();
			commomP=null;
		}
	}
}
