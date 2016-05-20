package app.cn.aiyouv.www.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.adapter.CateAdapter;
import app.cn.aiyouv.www.bean.Cate;
import app.cn.aiyouv.www.bean.CateImp;
import app.cn.aiyouv.www.common.ConstantValues;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.inter.Tick;
import app.cn.aiyouv.www.ui.SearchActivity;
import app.cn.aiyouv.www.utils.SharedUtils;


public class CommonCatePop {
	public interface onSelect {
		void onClick(Object object);
	}
	/**
	 * 删除弹出框
	 */
	private CommonCatePop() {
	}
	private static Handler handler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
		switch (msg.what){
			case 1:
				ArrayList<ArrayList<CateImp>> lists = (ArrayList<ArrayList<CateImp>>) msg.obj;
				adapter.putData(lists.get(msg.arg1), msg.arg1);

				break;
			default:
				break;
		}
			}
	};
	private static void loadData(final int idx){
		AsyncHttp.get(U.URL+U.IUV_CATE,new ResponHandler(){
			@Override
			public void onSuccess(int index, String s) {
				super.onSuccess(index, s);
				try {
					JSONObject jsonObject = new JSONObject(s);
					String status = jsonObject.getString("type");
					if(status.equals(U.SUCCESS)){
						//开始解析
						JSONArray jsonArray = jsonObject.getJSONArray("data");
						int len = jsonArray.length();
						ArrayList<ArrayList<CateImp>> lists = new ArrayList<ArrayList<CateImp>>();
						for(int i=0;i<len;i++){
							JSONObject object = jsonArray.getJSONObject(i);
							JSONArray array = object.getJSONArray("children");
							int jen = array.length();
							ArrayList<CateImp> imps = new ArrayList<CateImp>();
							for(int j=0;j<jen;j++){
								CateImp imp = new CateImp();

								JSONObject obj = array.getJSONObject(j);
								imp.setId(obj.getString("id"));
								imp.setTag(obj.getString("name"));
								JSONArray ay = obj.getJSONArray("children");
								int yen = ay.length();
								ArrayList<Cate> cates = new ArrayList<Cate>();

								Cate cte = new Cate();
								cte.setId(obj.getString("id"));
								cte.setTag("全部");
								cates.add(cte);
								for(int y=0;y<yen;y++){
									JSONObject o = ay.getJSONObject(y);
									Cate cate = new Cate();
									cate.setId(o.getString("id"));
									cate.setTag(o.getString("name"));
									cates.add(cate);
								}
								imp.setCates(cates);
								imps.add(imp);
							}
							lists.add(imps);
						}
						Message msg = new Message();
						msg.what = 1;
						msg.arg1 = idx;
						msg.obj  = lists;
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	private static CateAdapter adapter;
	private static InScrollListView cate_list;
	public static Dialog showSheet(final Context context,
								   final onSelect actionSheetSelected,
								   OnCancelListener cancelListener,final Object object, final int index, final Tick tick) {
		final Dialog dlg = new Dialog(context, R.style.delete_pop_style);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.cate_layout, null);
		layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		  cate_list = (InScrollListView) layout.findViewById(R.id.cate_list);
		ArrayList<CateImp> imps = new ArrayList<CateImp>();
		  adapter = new CateAdapter(context,imps);
		cate_list.setAdapter(adapter);
		TextView vuforia_btn = (TextView) layout.findViewById(R.id.vuforia_btn);
		TextView all = (TextView) layout.findViewById(R.id.all);
		final CheckBox cate0 = (CheckBox) layout.findViewById(R.id.cate0);
		final CheckBox cate1 = (CheckBox) layout.findViewById(R.id.cate1);
		final CheckBox cate2 = (CheckBox) layout.findViewById(R.id.cate2);
		final CheckBox cate3 = (CheckBox) layout.findViewById(R.id.cate3);
		final CheckBox cate4 = (CheckBox) layout.findViewById(R.id.cate4);
		final SharedUtils utils = new SharedUtils(context, IUV.cate);
		final Button ok = (Button) layout.findViewById(R.id.ok);
		if(utils.getIntValue("cate_id")!=0){
			switch (utils.getIntValue("cate_id")){
				case R.id.cate0:
					cate0.setChecked(true);
					break;
				case R.id.cate1:
					cate1.setChecked(true);
					break;
				case R.id.cate2:
					cate2.setChecked(true);
					break;
				case R.id.cate3:
					cate3.setChecked(true);
					break;
			}
		}
		cate0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					cate1.setChecked(false);
					cate2.setChecked(false);
					cate3.setChecked(false);
					cate4.setChecked(false);
					utils.setStringValue("cate_name", "");
					utils.setIntValue("cate_id", cate0.getId());
					utils.setStringValue("cate_tag",cate0.getTag().toString());
				}

			}
		});
		cate1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					cate0.setChecked(false);
					cate2.setChecked(false);
					cate3.setChecked(false);
					cate4.setChecked(false);
					utils.setStringValue("cate_name", "");
					utils.setIntValue("cate_id", cate1.getId());
					utils.setStringValue("cate_tag", cate1.getTag().toString());
				}

			}
		});
		cate2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					cate1.setChecked(false);
					cate0.setChecked(false);
					cate3.setChecked(false);
					cate4.setChecked(false);
					utils.setStringValue("cate_name", "");
					utils.setIntValue("cate_id", cate2.getId());
					utils.setStringValue("cate_tag", cate2.getTag().toString());
				}
			}
		});
		cate3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					cate1.setChecked(false);
					cate2.setChecked(false);
					cate0.setChecked(false);
					cate4.setChecked(false);
					utils.setStringValue("cate_name", "");
					utils.setIntValue("cate_id", cate3.getId());
					utils.setStringValue("cate_tag",cate3.getTag().toString());
				}

			}
		});
		cate4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					cate1.setChecked(false);
					cate2.setChecked(false);
					cate0.setChecked(false);
					cate3.setChecked(false);
					utils.setStringValue("cate_name", "");
					utils.setIntValue("cate_id", cate4.getId());
					utils.setStringValue("cate_tag",cate4.getTag().toString());
				}
			}
		});

		vuforia_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				dlg.dismiss();
				/*
				加载数据
				 */
				Intent intent = new Intent(context, SearchActivity.class);
				context.startActivity(intent);

			}
		});
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				dlg.dismiss();
			}
		});
		dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialogInterface) {
				tick.select(0);
			}
		});
		all.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(index==0){
					ConstantValues.CONTENT0 = "1";
				}else if(index==1){
					ConstantValues.CONTENT1 = "2";
				}else if(index==2){
					ConstantValues.CONTENT2 = "3";
				}
				adapter.notifyDataSetChanged();
			}
		});
		loadData(index);
		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.TOP;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);

		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

}
