package app.cn.aiyouv.www.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
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
import app.cn.aiyouv.www.utils.TimeUtil;
import app.cn.aiyouv.www.widget.charts.BarChart3DView;

/**
 * Created by Administrator on 2016/4/29.
 */
public class BillXchartsActivity extends BaseActivity {
    private TextView title;
    private LinearLayout content;
    private  BarChart3DView chart3DView;
    private Button turn_left,turn_right;
    private TextView turn_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_xchart_layout);
        findViewById(R.id.title_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getAppManager().finishActivity();
            }
        });
        content = (LinearLayout) findViewById(R.id.content);
        title = (TextView) findViewById(R.id.title);
        turn_left = (Button) findViewById(R.id.turn_left);
        turn_right = (Button) findViewById(R.id.turn_right);
        turn_view = (TextView) findViewById(R.id.turn_view);
        title.setText(getIntent().getStringExtra("title"));
          chart3DView = new BarChart3DView(BillXchartsActivity.this);
        turn_view.setText(TimeUtil.getDate_(System.currentTimeMillis()));
        turn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turn_view.setText(TimeUtil.getNextDay(turn_view.getText().toString(), true));
                load();
            }
        });
        turn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turn_view.setText(TimeUtil.getNextDay(turn_view.getText().toString(), false));
                load();
            }
        });
        content.post(new Runnable() {
            @Override
            public void run() {

                handler.sendEmptyMessage(0);
            }
        });
        load();
    }
    private  String TYPE;
    private void load(){
        RequestParams params = new RequestParams();
        SharedUtils utils = new SharedUtils(BillXchartsActivity.this, IUV.status);
        params.put("userId",utils.getStringValue("id"));
        params.put("appKey",utils.getStringValue("key"));
        params.put("date", turn_view.getText().toString());
          TYPE  = getIntent().getStringExtra("tag");
        if(getIntent().getStringExtra("tag").equals("collectMerchantRatio")){
            //同一个接口
            TYPE = "payableCommissionRatio";
        }
        C.p(U.URL+U.Xcharts+getIntent().getStringExtra("tag"));
        AsyncHttp.post(U.URL+U.Xcharts+TYPE,params,new ResponHandler(BillXchartsActivity.this){
            @Override
            public void onSuccess(int index, String result) {
                super.onSuccess(index, result);
                C.p(result);
                try {
                    JSONObject jsonObject =new JSONObject(result);
                    String status = jsonObject.getString("type");
                    if(status.equals(U.SUCCESS)){
                        JSONObject object = jsonObject.getJSONObject("data");
                        Map<String,String> map = new HashMap<String, String>();
                        if(TYPE.equals("payableCommissionRatio")){
                            map.put("payableCommissionRatio", object.getString("payableCommissionRatio").equals("null") ? "0" : object.getString("payableCommissionRatio"));
                            map.put("collectMerchantRatio", object.getString("collectMerchantRatio").equals("null") ? "0" : object.getString("collectMerchantRatio"));
                        }else{
                            map.put("unLocalMerchant",object.getString("unLocalMerchant").equals("null")?"0":object.getString("unLocalMerchant"));
                            map.put("unLocalMember", object.getString("unLocalMember").equals("null") ? "0" : object.getString("unLocalMember"));
                            map.put("localMember", object.getString("localMember").equals("null") ? "0" : object.getString("localMember"));
                        }

                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = map;
                        handler.sendMessage(msg);

                        /*
                        {"type":"success","content":"操作成功","data":{"unLocalMerchant":null,"unLocalMember":null,"localMember":null}}
                         */
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case 0:
                    content.addView(chart3DView, new LinearLayout.LayoutParams((int) (content.getMeasuredWidth() * 0.8), (int) (content.getMeasuredHeight() * 0.8)));

                    break;
                case 1:
                    content.removeAllViews();
                    chart3DView = new BarChart3DView(BillXchartsActivity.this);
                    content.addView(chart3DView, new LinearLayout.LayoutParams((int) (content.getMeasuredWidth() * 0.8), (int) (content.getMeasuredHeight() * 0.8)));
                    Map<String,String> map = (Map<String, String>) msg.obj;
                    if(TYPE.equals("payableCommissionRatio")){
                        double payableCommissionRatio =Double.parseDouble(map.get("payableCommissionRatio"));
                        double collectMerchantRatio = Double.parseDouble(map.get("collectMerchantRatio"));
                        double max = 0;
                        if(payableCommissionRatio>collectMerchantRatio){
                            max = payableCommissionRatio;
                        }else{
                            max = collectMerchantRatio;
                        }
                        int step = (int) ((max+10)/4);
                        chart3DView.setInitTwo(getIntent().getStringExtra("title"),"aiyouv.cn",max+10,0,step,"元",payableCommissionRatio,collectMerchantRatio,"平台佣金","商户佣金");
                    }else {
                        //本商户他店消费
                        double unLocalMerchant =Double.parseDouble(map.get("unLocalMerchant"));
                        double unLocalMember = Double.parseDouble(map.get("unLocalMember"));
                        double localMember = Double.parseDouble(map.get("localMember"));
                        double max = 0;
                        if(unLocalMerchant>unLocalMember){
                            max = unLocalMerchant;
                            if(unLocalMerchant>localMember){
                                max = unLocalMerchant;
                            }else{
                                max = localMember;
                            }
                        }else{
                            max = unLocalMember;
                            if(unLocalMember>localMember){
                                max = unLocalMember;
                            }else{
                                max = localMember;
                            }
                        }
                        int step = (int) ((max+10)/4);
                        chart3DView.setInit(getIntent().getStringExtra("title"),"aiyouv.cn",max+10,0,step,"元",localMember,unLocalMember,unLocalMerchant,"本店初始会员","非本店初始会员","初始会员他店消费");
                    }



                    break;
            }
        }
    };

}
