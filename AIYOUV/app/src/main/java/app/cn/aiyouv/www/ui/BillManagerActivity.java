package app.cn.aiyouv.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.utils.SharedUtils;
import app.cn.aiyouv.www.widget.NewDataToast;

/**
 * Created by Administrator on 2016/4/14.
 */
public class BillManagerActivity extends BaseActivity {
    private TextView item0,item1,item2,item3,item4,item5;
    private LinearLayout layout0,layout1,layout2,layout3,layout4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_manager_layout);
        findViewById(R.id.title_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getAppManager().finishActivity();
            }
        });
        item0 = (TextView) findViewById(R.id.item0);
        item1 = (TextView) findViewById(R.id.item1);
        item2 = (TextView) findViewById(R.id.item2);
        item3 = (TextView) findViewById(R.id.item3);
        item4 = (TextView) findViewById(R.id.item4);
        item5 = (TextView) findViewById(R.id.item5);
        layout0 = (LinearLayout) findViewById(R.id.layout0);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);
        layout4 = (LinearLayout) findViewById(R.id.layout4);
        layout0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillManagerActivity.this,BillTableActivity.class);
                intent.putExtra("tag","localMember");
                intent.putExtra("title","本店初始化会员");
                startActivity(intent);
            }
        });
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillManagerActivity.this,BillTableActivity.class);
                intent.putExtra("tag","unLocalMember");
                intent.putExtra("title","非本店初始化会员");
                startActivity(intent);
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillManagerActivity.this,BillTableActivity.class);
                intent.putExtra("tag","unLocalMerchant");
                intent.putExtra("title","初始化会员他店消费");
                startActivity(intent);
            }
        });
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillManagerActivity.this,BillTableActivity.class);
                intent.putExtra("tag","payableCommissionRatio");
                intent.putExtra("title","应付平台费用");
                startActivity(intent);
            }
        });
        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillManagerActivity.this,BillTableActivity.class);
                intent.putExtra("tag","collectMerchantRatio");
                intent.putExtra("title","应得奖励");
                startActivity(intent);
            }
        });
        load();
    }
    private Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case 1:
                    Map<String,String> values = (Map<String, String>) msg.obj;
                    item0.setText("¥"+values.get("allMoney"));
                    item1.setText("¥"+values.get("localMember"));
                    item2.setText("¥"+values.get("unLocalMember"));
                    item3.setText("¥"+values.get("unLocalMerchant"));
                    item4.setText("¥"+values.get("payableCommissionRatio"));
                    item5.setText("¥"+values.get("collectMerchantRatio"));
                    break;
            }
        }
    };
    private void load(){
        RequestParams params = new RequestParams();
        SharedUtils utils = new SharedUtils(BillManagerActivity.this, IUV.status);
        params.put("userId",utils.getStringValue("id"));
        params.put("appKey",utils.getStringValue("key"));
        AsyncHttp.post(U.URL+U.MY_BILL,params,new ResponHandler(){
            @Override
            public void onSuccess(int index, String result) {
                super.onSuccess(index, result);
                C.p(result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("type");
                    if(status.equals(U.SUCCESS)){
                        JSONObject obj = jsonObject.getJSONObject("data");
                        Map<String,String> value = new HashMap<String, String>();
                        value.put("payableCommissionRatio",obj.getString("payableCommissionRatio").equals("null")?"0":obj.getString("payableCommissionRatio"));
                        value.put("unLocalMerchant",obj.getString("unLocalMerchant").equals("null")?"0":obj.getString("unLocalMerchant"));
                        value.put("allMoney",obj.getString("allMoney").equals("null")?"0":obj.getString("allMoney"));
                        value.put("unLocalMember",obj.getString("unLocalMember").equals("null")?"0":obj.getString("unLocalMember"));
                        value.put("localMember",obj.getString("localMember").equals("null")?"0":obj.getString("localMember"));
                        value.put("collectMerchantRatio",obj.getString("collectMerchantRatio").equals("null")?"0":obj.getString("collectMerchantRatio"));
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = value;
                        handler.sendMessage(msg);
                    }else if(U.UNLOGIN.equals(status)){
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
}
