package app.cn.aiyouv.www.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.inter.Tick;
import app.cn.aiyouv.www.utils.SharedUtils;


public class CommonRelyPop {
	 public CommonRelyPop(){

	 }
	/**
	 * 删除弹出框
	 */
	  Dialog dlg;
	private Handler handler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what){
				case 1:
					if(dlg!=null){
						dlg.dismiss();
					}

					break;
			}
		}
	};
	public   Dialog showSheet(final Activity context,
								 final String parentId,final String articleId, final Tick ok) {
		  dlg = new Dialog(context, R.style.delete_pop_style);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.common_relay, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		ImageView btn_face = (ImageView) layout.findViewById(R.id.btn_face);
		final View faceView =  layout.findViewById(R.id.ll_facechoose);
		final EditText msg_et = (EditText) layout.findViewById(R.id.msg_et);
		final Button send = (Button) layout.findViewById(R.id.send_btn);
		msg_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					send.setEnabled(true);
				} else {
					send.setEnabled(false);
				}
			}
		});
		send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				RequestParams params = new RequestParams();
				SharedUtils utils = new SharedUtils(context, IUV.status);
				params.put("userId",utils.getStringValue("id"));
				params.put("appKey",utils.getStringValue("key"));
				params.put("content",msg_et.getText().toString());
				params.put("parentId",parentId);
				params.put("articleId", articleId);
				AsyncHttp.post(U.URL+U.REPLY_,params,new ResponHandler(context){
					@Override
					public void onSuccess(int index, String result) {
						C.p(result);
						super.onSuccess(index, result);
						try {
							JSONObject jsonObject = new JSONObject(result);
							String status = jsonObject.getString("type");
							if(status.equals(U.SUCCESS)){
								//此处的0是无用的，共用的接口
								ok.select(0);
                                handler.sendEmptyMessage(1);
                            }else if(status.equals(U.ERROR)){
								NewDataToast.makeText(context,jsonObject.getString("content")).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

			}
		});
		msg_et.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (faceView.getVisibility() == View.VISIBLE) {
					faceView.setVisibility(View.GONE);
				}
			}
		});
		msg_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean b) {
				if (faceView.getVisibility() == View.VISIBLE) {
					faceView.setVisibility(View.GONE);
				}

			}
		});
		btn_face.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				if (faceView.getVisibility() == View.VISIBLE) {
					faceView.setVisibility(View.GONE);
				} else {
					/*if(getWindow().getAttributes().softInputMode== WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED){
						imm.hideSoftInputFromWindow(msg_et.getWindowToken(), 0);
					}*/
					faceView.setVisibility(View.VISIBLE);
				}
			}
		});




		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.gravity = Gravity.CENTER;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

}
