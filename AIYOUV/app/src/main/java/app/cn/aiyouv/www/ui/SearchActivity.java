package app.cn.aiyouv.www.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qualcomm.vuforia.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.adapter.SearchAdapter;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.bean.Article;
import app.cn.aiyouv.www.bean.Article_List;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;

/**
 * Created by Administrator on 2016/4/29.
 */
public class SearchActivity extends BaseActivity {
    private SearchAdapter searchAdapter;
    private ExpandableListView search_list;
    private EditText search_edit;
    private ImageView search_do;
    private ImageView search_del;
    private int SCREEN_WIDTH;
    private Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case 1:
                    searchAdapter.putData(sers);
                    for (int i=0; i<sers.size(); i++) {
                        search_list.expandGroup(i);
                    };
                    break;
            }
        }
    };
    private void search(){
        RequestParams params = new RequestParams();
        params.put("key",search_edit.getText().toString());
        AsyncHttp.post(U.URL+U.SEARCH,params,new ResponHandler(SearchActivity.this){
            @Override
            public void onSuccess(int index, String result) {
                super.onSuccess(index, result);
                C.p(result);
                try {
                    sers = new ArrayList<ArrayList<Article_List>>();
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("type");
                    if(status.equals(U.SUCCESS)){
                        JSONObject obj = jsonObject.getJSONObject("data");
                        JSONArray array0 = obj.getJSONArray("index");
                        sers.add( parse(array0));
                        JSONArray array1 = obj.getJSONArray("origina");
                        sers.add( parse(array1));
                        JSONArray array2 = obj.getJSONArray("dianjiaxiu");
                        sers.add( parse(array2));
                    }
                     handler.sendEmptyMessage(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private  ArrayList<Article_List> parse( JSONArray jsonArray){
        int len = jsonArray.length();
        C.p("列表" + len);
        ArrayList<Article_List> lists = new ArrayList<Article_List>();
        try {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  lists;
    }
    private  ArrayList<ArrayList<Article_List>> sers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        findViewById(R.id.title_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getAppManager().finishActivity();
            }
        });
        search_list = (ExpandableListView) findViewById(R.id.search_list);
        search_do = (ImageView) findViewById(R.id.search_do);
        search_edit = (EditText) findViewById(R.id.search_eidt);
        search_del = (ImageView) findViewById(R.id.search_del);
        sers = new ArrayList<ArrayList<Article_List>>();
        sers.add(new ArrayList<Article_List>());
        sers.add(new ArrayList<Article_List>());
        sers.add(new ArrayList<Article_List>());
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SCREEN_WIDTH = dm.widthPixels;
        searchAdapter = new SearchAdapter(SearchActivity.this,null,SCREEN_WIDTH,sers);
        search_list.setAdapter(searchAdapter);
        search_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        search_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_edit.setText("");
            }
        });
        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    if(editable.toString().length()!=0){
                        search_del.setVisibility(View.VISIBLE);
                    }else{
                        search_del.setVisibility(View.INVISIBLE);
                    }
            }
        });
    }
}
