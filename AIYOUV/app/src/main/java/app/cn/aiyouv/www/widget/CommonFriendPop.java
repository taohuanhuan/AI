package app.cn.aiyouv.www.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.bean.Fs_Item;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.utils.SharedUtils;


public class CommonFriendPop {
	public interface onSelect {
		void onClick(Object object);
	}
	/**
	 * 删除弹出框
	 */
	private CommonFriendPop() {
	}
	private static Handler handler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
		switch (msg.what){
			case  -1:
				NewDataToast.makeText(activity,"操作失败").show();
				break;
			case 0:
				if(dlg!=null){
					dlg.dismiss();
				}
				NewDataToast.makeText(activity,"添加成功").show();
				break;
			case  1:
				final Fs_Item item = (Fs_Item) msg.obj;
				layout1.setVisibility(View.GONE);
				layout2.setVisibility(View.VISIBLE);
				name.setText(item.getName());
				Picasso.with(activity).load(U.URL+item.getPic()).into(ico);
				add.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						RequestParams params = new RequestParams();
						SharedUtils utils = new SharedUtils(activity, IUV.status);
						params.put("userId",utils.getStringValue("id"));
						params.put("appKey",utils.getStringValue("key"));
						params.put("id",item.getId());
						AsyncHttp.post(U.URL+U.IUV_ADD_FS,params,new ResponHandler(activity){
							@Override
							public void onSuccess(int index, String result) {
								super.onSuccess(index, result);
								try {
									JSONObject jsonObject = new JSONObject(result);
									String status = jsonObject.getString("type");
									if(status.equals(U.SUCCESS)) {

    								handler.sendEmptyMessage(0);
                                    }else {
										handler.sendEmptyMessage(-1);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
					}
				});
				ret.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						frind_no.setText("");
						layout1.setVisibility(View.VISIBLE);
						layout2.setVisibility(View.GONE);
					}
				});
				break;
		}
			}
	};
	 private static Dialog dlg;
	private static	LinearLayout layout1,layout2;
	private static TextView name,ret,add;
	private static CircleImageView ico;
	private static Activity activity;
	private static EditText frind_no;
	public static Dialog showSheet(final Activity context,
								   final onSelect actionSheetSelected,
								  final Object object) {
		activity = context;
		    dlg = new Dialog(context, R.style.delete_pop_style);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.common_add_friend, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		  frind_no = (EditText) layout.findViewById(R.id.frind_no);
		TextView search = (TextView) layout.findViewById(R.id.search);

		TextView cancel = (TextView) layout.findViewById(R.id.cancel);
		//------------------------------------------------------
		  ico = (CircleImageView) layout.findViewById(R.id.ico);
		  name = (TextView) layout.findViewById(R.id.name);
		  add = (TextView) layout.findViewById(R.id.add);
		  ret = (TextView) layout.findViewById(R.id.ret);
		//------
		  layout1 = (LinearLayout) layout.findViewById(R.id.layout1);
		  layout2 = (LinearLayout) layout.findViewById(R.id.layout2);

		layout1.setVisibility(View.VISIBLE);
		layout2.setVisibility(View.GONE);

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				actionSheetSelected.onClick(object);
//				dlg.dismiss();
				String no = frind_no.getText().toString();
				RequestParams params = new RequestParams();
				SharedUtils utils = new SharedUtils(context, IUV.status);
				params.put("userId",utils.getStringValue("id"));
				params.put("appKey",utils.getStringValue("key"));
				params.put("phone",no);
				AsyncHttp.post(U.URL+U.IUV_FIND_FS,params,new ResponHandler(context){
					@Override
					public void onSuccess(int index, String result) {
						super.onSuccess(index, result);
						C.p(result);
						try {
							JSONObject jsonObject = new JSONObject(result);
							String status = jsonObject.getString("type");
							if(status.equals(U.SUCCESS)) {
								Fs_Item item = new Fs_Item();
								JSONObject obj = jsonObject.getJSONObject("data");
								item.setId(obj.getString("id"));
								item.setName(obj.getString("name"));
								item.setPhone(obj.getString("userName"));
								item.setPic(obj.getString("image"));
								Message msg = new Message();
								msg.what = 1;
								msg.obj = item;
								handler.sendMessage(msg);


							}else {
								handler.sendEmptyMessage(-1);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});
			}
		});

		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				actionSheetSelected.onClick(object);
				dlg.dismiss();
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		/*lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;*/
		lp.gravity = Gravity.CENTER;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(false);


		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

}
