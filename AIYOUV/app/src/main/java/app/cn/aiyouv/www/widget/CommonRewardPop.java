package app.cn.aiyouv.www.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.adapter.PopTicketsAdapter;
import app.cn.aiyouv.www.adapter.TicketsAdapter;
import app.cn.aiyouv.www.bean.Fs_Bean;
import app.cn.aiyouv.www.bean.TicketsBean;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.common.Common;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.inter.Tick;
import app.cn.aiyouv.www.utils.SharedUtils;


public class CommonRewardPop {

	private PullToRefreshView.OnHeaderRefreshListener headerRefreshListener = new PullToRefreshView.OnHeaderRefreshListener() {
		@Override
		public void onHeaderRefresh(PullToRefreshView view) {
			refresh_layout.postDelayed(new Runnable() {

				@Override
				public void run() {


					CURRENT_PAGE = 1;
					load(true);
				}
			}, 1000);
		}
	};
	private PullToRefreshView.OnFooterRefreshListener footerRefreshListener = new PullToRefreshView.OnFooterRefreshListener() {
		@Override
		public void onFooterRefresh(PullToRefreshView view) {
			refresh_layout.postDelayed(new Runnable() {

				@Override
				public void run() {

					CURRENT_PAGE = CURRENT_PAGE+1;
					if(CURRENT_PAGE>TOTAL_PAGER){
						handler.sendEmptyMessage(1);
						NewDataToast.makeText(context, "没有数据了").show();
					}else{
						load(false);
					}

				}
			}, 1000);
		}
	};
	/**
	 * 删除弹出框
	 */
	private Context context;
	public CommonRewardPop(Context context) {
		this.context = context;
	}
	private Handler handler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what){
				case 1:
					refresh_layout.onHeaderRefreshComplete();
					refresh_layout.onFooterRefreshComplete();
					ticketsAdapter.putData(ticketsBeans);
					break;
			}
		}
	};

	private ArrayList<TicketsBean> ticketsBeans;
	private int CURRENT_PAGE = 1;
	private int TOTAL_PAGER = -1;
	private void load( final boolean isLoad){
		RequestParams params = new RequestParams();
		params.put("pageNumber",String.valueOf(CURRENT_PAGE));
		params.put("pageSize", Common.VIEW_NUM);
		params.put("status","no");
		SharedUtils utils = new SharedUtils(context, IUV.status);
		params.put("userId",utils.getStringValue("id"));
		params.put("appKey",utils.getStringValue("key"));
		AsyncHttp.post(U.URL + U.IUV_TICKETS, params, new ResponHandler() {
			@Override
			public void onSuccess(int index, String result) {
				super.onSuccess(index, result);
				if (isLoad) {
					ticketsBeans = null;
					ticketsBeans = new ArrayList<TicketsBean>();
				}
				C.p(result);
				try {
					JSONObject jsonObject = new JSONObject(result);
					String status = jsonObject.getString("type");
					if (status.equals(U.SUCCESS)) {
						//
						JSONObject obj = jsonObject.getJSONObject("data");
						JSONArray jsonArray = obj.getJSONArray("content");
						TOTAL_PAGER = obj.getInt("totalPages");
						CURRENT_PAGE = obj.getInt("pageNumber");
						int len = jsonArray.length();
						for (int i = 0; i < len; i++) {
							JSONObject o = jsonArray.getJSONObject(i);
							TicketsBean bean = new TicketsBean();
							bean.setPic(o.getString("merchantImage"));
							bean.setName(o.getString("merchantName"));
							bean.setId(o.getString("id"));
							bean.setMoney(o.getString("money"));
							bean.setTip(o.getString("tip"));
							ticketsBeans.add(bean);
						}
						handler.sendEmptyMessage(1);
					} else if (U.UNLOGIN.equals(status)) {
						NewDataToast.makeText(context, jsonObject.getString("content")).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}


			}
		});
	}
	PullToRefreshView refresh_layout;
	private Tick tick = new Tick() {
		@Override
		public void select(int position) {
			ticketsAdapter.setSelect(position);

		}
	};
	private PopTicketsAdapter ticketsAdapter;
	public Dialog showSheet(final String articleId) {

		final Dialog dlg = new Dialog(context, R.style.delete_pop_style);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.common_reward_pop, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		  refresh_layout = (PullToRefreshView) layout.findViewById(R.id.refresh_layout);
		refresh_layout.setOnHeaderRefreshListener(headerRefreshListener);
		refresh_layout.setOnFooterRefreshListener(footerRefreshListener);
		ListView tickte_lists = (ListView) layout.findViewById(R.id.tickte_lists);
		ticketsBeans = new ArrayList<TicketsBean>();
		ticketsAdapter = new PopTicketsAdapter(context,ticketsBeans,tick);
		tickte_lists.setAdapter(ticketsAdapter);
		TextView send = (TextView) layout.findViewById(R.id.send);

		load(true);
		TextView mCancel = (TextView) layout.findViewById(R.id.cancel);
		send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (ticketsAdapter != null) {
					RequestParams params = new RequestParams();
					SharedUtils utils = new SharedUtils(context, IUV.status);
					params.put("userId", utils.getStringValue("id"));
					params.put("appKey", utils.getStringValue("key"));
					params.put("articleId", articleId);
					params.put("ticketId", ticketsAdapter.getSelectId());
					AsyncHttp.post(U.URL+U.Send_Reward_User,params,new ResponHandler(){
						@Override
						public void onSuccess(int index, String result) {
							super.onSuccess(index, result);

							try {
								JSONObject jsonObject = new JSONObject(result);
								String status = jsonObject.getString("type");
								if (status.equals(U.SUCCESS)) {
									NewDataToast.makeText(context,"打赏成功").show();
								}else if(U.UNLOGIN.equals(status)){
									NewDataToast.makeText(context,jsonObject.getString("content")).show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				}else{
					NewDataToast.makeText(context,"请选择券").show();
				}
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
