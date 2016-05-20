package app.cn.aiyouv.www.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.adapter.RewardAdapter;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.bean.RewardBean;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.common.Common;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.utils.SharedUtils;
import app.cn.aiyouv.www.utils.TimeUtil;
import app.cn.aiyouv.www.widget.NewDataToast;
import app.cn.aiyouv.www.widget.PullToRefreshView;

/**
 * Created by Administrator on 2016/4/20.
 */
public class RewardListActivity extends BaseActivity {
    private Button turn_left,turn_right;
    private ListView tickte_lists;
    private PullToRefreshView refresh_layout;
    private RewardAdapter rewardAdapter;
    private TextView turn_view;
    private String nowDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reward_list_activity);
        findViewById(R.id.title_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getAppManager().finishActivity();
            }
        });
        turn_view = (TextView) findViewById(R.id.turn_view);
        turn_left = (Button) findViewById(R.id.turn_left);
        turn_right = (Button) findViewById(R.id.turn_right);
        refresh_layout = (PullToRefreshView) findViewById(R.id.refresh_layout);
        refresh_layout.setOnHeaderRefreshListener(headerRefreshListener);
        refresh_layout.setOnFooterRefreshListener(footerRefreshListener);
        tickte_lists = (ListView) findViewById(R.id.tickte_lists);
        ticketsBeans = new ArrayList<RewardBean>();
        rewardAdapter = new RewardAdapter(getApplicationContext(),ticketsBeans);
        tickte_lists.setAdapter(rewardAdapter);
        nowDate = TimeUtil.getSelectDate(System.currentTimeMillis());
        turn_view.setText(nowDate);
        turn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turn_view.setText(TimeUtil.getNextDate(turn_view.getText().toString(), false));
                load(true);
            }
        });
        turn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turn_view.setText(TimeUtil.getNextDate(turn_view.getText().toString(),true));
                load(true);
            }
        });
        load(true);
    }
    private Handler handler = new Handler(){
        @Override
        public void dispatchMessage(android.os.Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case 0:
                    break;
                case 1:
                    refresh_layout.onHeaderRefreshComplete();
                    refresh_layout.onFooterRefreshComplete();
                    rewardAdapter.putData(ticketsBeans);
                    break;
            }
        }
    };
    private ArrayList<RewardBean> ticketsBeans;
    private int CURRENT_PAGE = 1;
    private int TOTAL_PAGER = -1;
    private void load( final boolean isLoad){
        RequestParams params = new RequestParams();
        params.put("pageNumber",String.valueOf(CURRENT_PAGE));
        params.put("pageSize", Common.VIEW_NUM);
        SharedUtils utils = new SharedUtils(getApplicationContext(), IUV.status);
        params.put("userId", utils.getStringValue("id"));
        params.put("appKey", utils.getStringValue("key"));
        params.put("date",turn_view.getText().toString());
        AsyncHttp.post(U.URL + U.MY_REWARD_LIST, params, new ResponHandler(RewardListActivity.this) {
            @Override
            public void onSuccess(int index, String result) {
                super.onSuccess(index, result);
                if (isLoad) {
                    ticketsBeans.clear();
//                    ticketsBeans = null;
//                    ticketsBeans = new ArrayList<RewardBean>();
                }
                C.p("数据"+result);
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
                            RewardBean bean = new RewardBean();
                            bean.setName(o.getString("memberName"));
                            bean.setPrice(o.getString("money"));
                            bean.setImg(o.getString("memberImage"));
                            ticketsBeans.add(bean);
                        }
                        handler.sendEmptyMessage(1);
                    } else if (U.UNLOGIN.equals(status)) {
                        NewDataToast.makeText(getApplicationContext(), jsonObject.getString("content")).show();
                        SharedUtils utils = new SharedUtils(getApplicationContext(), IUV.status);
                        IUV.iuv = "";
                        utils.clear();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }
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
                        NewDataToast.makeText(RewardListActivity.this, "没有数据了").show();
                    }else{
                        load(false);
                    }

                }
            }, 1000);
        }
    };
}
