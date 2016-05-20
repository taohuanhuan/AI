package app.cn.aiyouv.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.adapter.CTicketsAdapter;
import app.cn.aiyouv.www.adapter.TicketsAdapter;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.bean.TicketsBean;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.common.Common;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.inter.Tick;
import app.cn.aiyouv.www.utils.SharedUtils;
import app.cn.aiyouv.www.widget.NewDataToast;
import app.cn.aiyouv.www.widget.PullToRefreshView;

/**
 * Created by Administrator on 2016/4/11.
 */
public class TicketsCActivity extends BaseActivity {
    private PullToRefreshView refresh_layout;
    private ListView tickte_lists;
    private CTicketsAdapter ticketsAdapter;
    private Tick tick = new Tick() {
        @Override
        public void select(int position) {

        }
    };
    private Tick hide = new Tick() {
        @Override
        public void select(int position) {
            //隐藏
            show(position);
        }
    };
    private void show(int position){
        RequestParams params = new RequestParams();
        SharedUtils utils = new SharedUtils(getApplicationContext(), IUV.status);
        params.put("userId", utils.getStringValue("id"));
        params.put("appKey",utils.getStringValue("key"));
        params.put("ticketId", ticketsBeans.get(position).getId());
        params.put("visible", "show");
        AsyncHttp.post(U.URL + U.VG_TICKET, params, new ResponHandler() {
            @Override
            public void onSuccess(int index, String result) {
                super.onSuccess(index, result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("type");
                    if (status.equals(U.SUCCESS)) {
                        load( true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_ticket_manager);
        findViewById(R.id.title_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getAppManager().finishActivity();
            }
        });
        ticketsBeans = new ArrayList<TicketsBean>();
        ticketsAdapter = new CTicketsAdapter(getApplicationContext(),ticketsBeans,tick,hide);
        tickte_lists = (ListView) findViewById(R.id.tickte_lists);
        tickte_lists.setAdapter(ticketsAdapter);
        refresh_layout = (PullToRefreshView) findViewById(R.id.refresh_layout);
        refresh_layout.setOnHeaderRefreshListener(headerRefreshListener);
        refresh_layout.setOnFooterRefreshListener(footerRefreshListener);

        //
            load(true);
        tickte_lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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
                        NewDataToast.makeText(TicketsCActivity.this, "没有数据了").show();
                    }else{
                        load(false);
                    }

                }
            }, 1000);
        }
    };
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
        params.put("status","hide");
        SharedUtils utils = new SharedUtils(getApplicationContext(), IUV.status);
        params.put("userId",utils.getStringValue("id"));
        params.put("appKey",utils.getStringValue("key"));
        AsyncHttp.post(U.URL+U.IUV_TICKETS,params,new ResponHandler(TicketsCActivity.this){
            @Override
            public void onSuccess(int index, String result) {
                super.onSuccess(index, result);
                if(isLoad){
//                    ticketsBeans = null;
//                    ticketsBeans = new ArrayList<TicketsBean>();
                    ticketsBeans.clear();
                }
                C.p(result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("type");
                    if(status.equals(U.SUCCESS)){
                        //
                        JSONObject obj = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = obj.getJSONArray("content");
                        TOTAL_PAGER = obj.getInt("totalPages");
                        CURRENT_PAGE = obj.getInt("pageNumber");
                        int len = jsonArray.length();
                        for(int i=0;i<len;i++){
                            JSONObject o = jsonArray.getJSONObject(i);
                            TicketsBean bean = new TicketsBean();
                            bean.setPic(o.getString("merchantImage"));
                            bean.setName(o.getString("merchantName"));
                            bean.setId(o.getString("id"));
                            bean.setMoney(o.getString("money"));
                            bean.setStatus(o.getString("status"));
                            bean.setType(o.getString("type"));
                            bean.setTip(o.getString("tip"));
                            ticketsBeans.add(bean);
                        }
                        handler.sendEmptyMessage(1);
                    }else if(U.UNLOGIN.equals(status)){
                        NewDataToast.makeText(getApplicationContext(),jsonObject.getString("content")).show();
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
}
