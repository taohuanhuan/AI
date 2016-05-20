package app.cn.aiyouv.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.adapter.Content1Item0Adapter;
import app.cn.aiyouv.www.adapter.DianZanAdapter;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.bean.Article_List;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.utils.SharedUtils;
import app.cn.aiyouv.www.widget.DelSlideListView;
import app.cn.aiyouv.www.widget.NewDataToast;
import app.cn.aiyouv.www.widget.OnDeleteListioner;
import app.cn.aiyouv.www.widget.PullToRefreshView;

/**
 * Created by Administrator on 2016/3/6.
 */
public class DianZanActivity extends BaseActivity {
    private Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        switch (msg.what){
            case 2:
                final ArrayList<Article_List> lists = (ArrayList<Article_List>) msg.obj;
                adapter.putData(lists);
                adapter.notifyDataSetChanged();
                content_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(DianZanActivity.this, ArticleDetailActivity.class);
                        intent.putExtra("acticle_id", lists.get(i).getId());
                        intent.putExtra("title", lists.get(i).getTitle());
                        startActivity(intent);
                    }
                });
                break;
            case -11:
                NewDataToast.makeText(getApplicationContext(),"请先登录").show();
                break;
            }
        }
    };
    private OnDeleteListioner deleteListioner = new OnDeleteListioner() {
        @Override
        public boolean isCandelete(int position) {
            return true;
        }

        @Override
        public void onDelete(Object obj) {
            deleteDianZan((Article_List)obj);
        }
        @Override
        public void onBack() {

        }
    };


    private int SCREEN_WIDTH;
    private DelSlideListView content_list;
    private DianZanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dianzan_layout);
        findViewById(R.id.title_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getAppManager().finishActivity();
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SCREEN_WIDTH = dm.widthPixels;
        content_list = (DelSlideListView)findViewById(R.id.content_list);
        ArrayList<Article_List> lists = new ArrayList<Article_List>();
        adapter = new DianZanAdapter(DianZanActivity.this,SCREEN_WIDTH,lists,handler,deleteListioner);
        content_list.setAdapter(adapter);
        content_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewDataToast.makeText(getApplicationContext(),"提示").show();
            }
        });
        content_list.setDeleteListioner(deleteListioner);
        loadData(false);

    }
    private void deleteDianZan(Article_List list){
        if(IUV.iuv.length()!=0){
            RequestParams params = new RequestParams();
            SharedUtils utils = new SharedUtils(getApplicationContext(), IUV.status);
            params.put("userId",utils.getStringValue("id"));
            params.put("appKey",utils.getStringValue("key"));
            params.put("artclieId",list.getId());
            params.put("type","cancle");
            AsyncHttp.post(U.URL+U.IUV_DIANZAN,params,new ResponHandler(){
                @Override
                public void onSuccess(int index, String result) {
                    super.onSuccess(index, result);
                    C.p("删除情况" + result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("type");
                        if(status.equals(U.SUCCESS)){

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }else {
            //-11作为统一的需要登录状态
            handler.sendEmptyMessage(-11);
        }


    }
    private void loadData(boolean iscancle){
        SharedUtils utils = new  SharedUtils(getApplicationContext(), IUV.status);
        RequestParams params = new RequestParams();
        params.put("userId",utils.getStringValue("id"));
        params.put("appKey", utils.getStringValue("key"));
        if(iscancle){
            params.put("type","cancle");

        }
        AsyncHttp.post(U.URL+U.IUV_MY_DIANZAN,params,new ResponHandler(DianZanActivity.this){
            @Override
            public void onSuccess(int index, String result) {
                super.onSuccess(index, result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("type");
                    if (status.equals(U.SUCCESS)) {
                        //成功
                        JSONObject obj = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = obj.getJSONArray("content");

                        int len = jsonArray.length();
                        C.p("列表" + len);
                        ArrayList<Article_List> lists = new ArrayList<Article_List>();
                        for (int i = 0; i < len; i++) {
                            JSONObject o = jsonArray.getJSONObject(i);
                            Article_List list = new Article_List();
                            list.setImg(o.getString("image"));
                            list.setTitle(o.getString("title"));
                            list.setId(o.getString("id"));
                            list.setIntro(o.getString("intro"));
                            list.setAuthor(o.getString("author")==null?"":o.getString("author"));
                            list.setTime(o.getString("createDate"));
                            lists.add(list);
                        }

                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = lists;
                        handler.sendMessage(msg);

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
