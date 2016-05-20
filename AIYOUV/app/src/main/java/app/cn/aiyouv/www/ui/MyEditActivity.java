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
import app.cn.aiyouv.www.adapter.EditsAdapter;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.bean.EditsBean;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.inter.Tick;
import app.cn.aiyouv.www.utils.SharedUtils;
import app.cn.aiyouv.www.widget.CommonDeletePop;
import app.cn.aiyouv.www.widget.NewDataToast;
import app.cn.aiyouv.www.widget.PullToRefreshView;

/**
 * Created by Administrator on 2016/4/12.
 */
public class MyEditActivity extends BaseActivity {
    private ListView edits_lists;
    private PullToRefreshView refresh_layout;
    private int CURRENT_PAGE = 1;
    private int TOTAL_PAGER = -1;
    private EditsAdapter editsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_edit);
        findViewById(R.id.title_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getAppManager().finishActivity();
            }
        });
        refresh_layout = (PullToRefreshView) findViewById(R.id.refresh_layout);
        refresh_layout.setOnHeaderRefreshListener(listHeadListener);
        refresh_layout.setOnFooterRefreshListener(listFootListener);
        edits_lists = (ListView) findViewById(R.id.edits_lists);
        editsBeans = new ArrayList<EditsBean>();
        editsAdapter = new EditsAdapter(getApplicationContext(),editsBeans,tick);
        edits_lists.setAdapter(editsAdapter);
        load(true);
    }
    private Tick tick = new Tick() {
        @Override
        public void select(int position) {
            CommonDeletePop.showSheet(MyEditActivity.this, new CommonDeletePop.onSelect() {
                @Override
                public void onClick(int object) {
                    delete(object);
                }
            },null,position);
        }
    };
    private void delete(int index){
        SharedUtils utils = new SharedUtils(getApplicationContext(), IUV.status);
        RequestParams params = new RequestParams();
        params.put("userId",utils.getStringValue("id"));
        params.put("appKey", utils.getStringValue("key"));
        params.put("id", editsBeans.get(index).getId());
        AsyncHttp.post(U.URL + U.DELETE_EDIT, params, new ResponHandler(MyEditActivity.this) {
            @Override
            public void onSuccess(int index, String result) {
                super.onSuccess(index, result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("type");
                    if (status.equals(U.SUCCESS)) {
                        load(true);
                    }else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 关于 handler
     */
    private Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
                switch (msg.what){
                    case 1:
                        editsAdapter.putData(editsBeans);
                        break;
                }
        }
    };
    private ArrayList<EditsBean> editsBeans;
    private void load(final boolean falg){
        SharedUtils utils = new SharedUtils(getApplicationContext(), IUV.status);
        RequestParams params = new RequestParams();
        params.put("userId",utils.getStringValue("id"));
        params.put("appKey", utils.getStringValue("key"));
        AsyncHttp.post(U.URL + U.IUV_EDITS, params, new ResponHandler() {
            @Override
            public void onSuccess(int index, String result) {
                super.onSuccess(index, result);
                C.p(result);
                if (falg) {
                    editsBeans = null;
                    editsBeans = new ArrayList<EditsBean>();
                }
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("type");
                    if (status.equals(U.SUCCESS)) {
                        JSONObject obj = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = obj.getJSONArray("content");
                        TOTAL_PAGER = obj.getInt("totalPages");
                        CURRENT_PAGE = obj.getInt("pageNumber");
                        int len = jsonArray.length();
                        for (int i = 0; i < len; i++) {
                            JSONObject o = jsonArray.getJSONObject(i);
                            EditsBean bean = new EditsBean();
                            bean.setMoney(o.getString("money"));
                            bean.setId(o.getString("id"));
                            bean.setName(o.getString("merchantName"));
                            bean.setAll(o.getString("allMoney"));
                            bean.setData(o.getString("modifyDate"));
                            bean.setImage(o.getString("merchantImage"));
                            bean.setIsVip(o.getBoolean("isVip"));
                            bean.setTicket(o.getString("ticketMoney"));
                            editsBeans.add(bean);
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


    /**
     * 顶部下拉刷新状态
     */
    private PullToRefreshView.OnHeaderRefreshListener listHeadListener = new PullToRefreshView.OnHeaderRefreshListener() {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {
            // TODO Auto-generated method stub
            refresh_layout.postDelayed(new Runnable() {

                @Override
                public void run() {
                    refresh_layout.onHeaderRefreshComplete();
                    CURRENT_PAGE = 1;
                    load(true);
                }
            }, 1000);
        }
    };
    /**
     *
     */
    private PullToRefreshView.OnFooterRefreshListener listFootListener = new PullToRefreshView.OnFooterRefreshListener() {

        @Override
        public void onFooterRefresh(PullToRefreshView view) {
            // TODO Auto-generated method stub
            refresh_layout.postDelayed(new Runnable() {

                @Override
                public void run() {
                    refresh_layout.onFooterRefreshComplete();
                    CURRENT_PAGE=CURRENT_PAGE+1;
                    if(CURRENT_PAGE>TOTAL_PAGER){
                        NewDataToast.makeText(getApplicationContext(), "没有数据了").show();
                    }else{
                        load(false);
                    }
                }
            }, 1000);
        }
    };
}
