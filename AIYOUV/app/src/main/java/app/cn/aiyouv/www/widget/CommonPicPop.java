package app.cn.aiyouv.www.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import app.cn.aiyouv.www.R;


public class CommonPicPop {
	public interface onSelect {
		void camer();
		void pic();
	}
	/**
	 * 删除弹出框
	 */
	private CommonPicPop() {
	}

	public static Dialog showSheet(Context context,
								   final onSelect actionSheetSelected) {
		final Dialog dlg = new Dialog(context, R.style.delete_pop_style);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.common_pic_pop, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		TextView title = (TextView) layout.findViewById(R.id.title);
		TextView mContent = (TextView) layout.findViewById(R.id.content);
		TextView mCancel = (TextView) layout.findViewById(R.id.cancel);
		title.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				actionSheetSelected.camer();
			}
		});
		mContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				actionSheetSelected.pic();
				dlg.dismiss();
			}
		});

		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				actionSheetSelected.onClick(object);
				dlg.dismiss();
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(false);


		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

}
