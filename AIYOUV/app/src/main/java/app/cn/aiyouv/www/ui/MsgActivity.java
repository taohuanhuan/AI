package app.cn.aiyouv.www.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.adapter.MsgAdapter;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.bean.MsgBean;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.common.Common;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.utils.SharedUtils;
import app.cn.aiyouv.www.widget.NewDataToast;
import app.cn.aiyouv.www.widget.PullToRefreshView;

/**
 * Created by Administrator on 2016/5/2.
 */
public class MsgActivity extends BaseActivity {
    private PullToRefreshView refresh_layout;
    private ListView msgList;
    private MsgAdapter msgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_layout);
        findViewById(R.id.title_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getAppManager().finishActivity();
            }
        });
        msgList = (ListView) findViewById(R.id.msg_lists);
        refresh_layout = (PullToRefreshView) findViewById(R.id.refresh_layout);
        refresh_layout.setOnHeaderRefreshListener(headerRefreshListener);
        refresh_layout.setOnFooterRefreshListener(footerRefreshListener);
        msgBeans = new ArrayList<MsgBean>();
        msgAdapter = new MsgAdapter(MsgActivity.this, msgBeans);
        msgList.setAdapter(msgAdapter);
        load(true);
    }

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    refresh_layout.onHeaderRefreshComplete();
                    refresh_layout.onFooterRefreshComplete();
                    msgAdapter.putData(msgBeans);
                    break;
            }
        }
    };
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

                    CURRENT_PAGE = CURRENT_PAGE + 1;
                    if (CURRENT_PAGE > TOTAL_PAGER) {
                        handler.sendEmptyMessage(1);
                        NewDataToast.makeText(MsgActivity.this, "没有数据了").show();
                    } else {
                        load(false);
                    }

                }
            }, 1000);
        }
    };
    private ArrayList<MsgBean> msgBeans;
    private int CURRENT_PAGE = 1;
    private int TOTAL_PAGER = -1;

    private void load(final boolean reload) {
        RequestParams params = new RequestParams();
        params.put("pageNumber", String.valueOf(CURRENT_PAGE));
        params.put("pageSize", Common.VIEW_NUM);
        SharedUtils utils = new SharedUtils(getApplicationContext(), IUV.status);
        params.put("userId", utils.getStringValue("id"));
        params.put("appKey", utils.getStringValue("key"));
        AsyncHttp.post(U.URL + U.MES, params, new ResponHandler() {
            @Override
            public void onSuccess(int index, String result) {
                super.onSuccess(index, result);
                C.p(result);
                try {
                    if (reload) {
                        msgBeans.clear();
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("type");
                    if (status.equals(U.SUCCESS)) {
                        JSONObject json = jsonObject.getJSONObject("data");
                        TOTAL_PAGER = json.getInt("totalPages");
                        CURRENT_PAGE = json.getInt("pageNumber");
                        JSONArray array = json.getJSONArray("content");
                        int len = array.length();
                        for (int i = 0; i < len; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            MsgBean bean = new MsgBean();
                            bean.setMsg(obj.getString("title"));
                            msgBeans.add(bean);
                        }
                        handler.sendEmptyMessage(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
