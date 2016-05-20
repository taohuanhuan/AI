package app.cn.aiyouv.www.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.adapter.TicketsAdapter;
import app.cn.aiyouv.www.bean.TicketsBean;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.common.Common;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.inter.Tick;
import app.cn.aiyouv.www.utils.SharedUtils;


public class CommonSRewardPop {


	/**
	 * 删除弹出框
	 */
	private Context context;
	public CommonSRewardPop(Context context) {
		this.context = context;

	}
	class Input extends BaseInputConnection {

		public Input(View targetView, boolean fullEditor) {
			super(targetView, fullEditor);
		}

		@Override
		public boolean commitText(CharSequence text, int newCursorPosition) {
			C.p(text.toString());
		return super.commitText(text, newCursorPosition);

		}
	}
	private void load(String articleId){
		RequestParams params = new RequestParams();

		SharedUtils utils = new SharedUtils(context, IUV.status);
		params.put("userId",utils.getStringValue("id"));
		params.put("appKey",utils.getStringValue("key"));
		params.put("articleId",articleId);
		params.put("money",item0.getText().toString());
		AsyncHttp.post(U.URL+U.Send_Reward_,params,new ResponHandler(){
			@Override
			public void onSuccess(int index, String result) {
				super.onSuccess(index, result);
			C.p(result);
				try {
					JSONObject jsonObject = new JSONObject(result);
					String status = jsonObject.getString("type");
					if(status.equals(U.SUCCESS)){
                        NewDataToast.makeText(context,"打赏成功!").show();
                    }else if(status.equals(U.ERROR)){
                        NewDataToast.makeText(context,jsonObject.getString("content")).show();
                    }
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private EditText item0;
	private LinearLayout re;
	PullToRefreshView refresh_layout;
	private Tick tick = new Tick() {
		@Override
		public void select(int position) {
			ticketsAdapter.setSelect(position);

		}
	};
	private TicketsAdapter ticketsAdapter;
	public Dialog showSheet(final String articleId) {

		final Dialog dlg = new Dialog(context, R.style.delete_pop_style);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.common_s_reward_pop, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		re = (LinearLayout) layout.findViewById(R.id.re);
		item0 = (EditText) layout.findViewById(R.id.item0);
		TextView send = (TextView) layout.findViewById(R.id.send);


		TextView mCancel = (TextView) layout.findViewById(R.id.cancel);
		send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
					//articleId,appKey,userId,money
				load(articleId);
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
		lp.gravity = Gravity.CENTER;
//		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(false);
		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

}
