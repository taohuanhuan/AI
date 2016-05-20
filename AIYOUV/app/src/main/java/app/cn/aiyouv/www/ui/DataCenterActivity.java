package app.cn.aiyouv.www.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.bean.DataBean;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.utils.SharedUtils;
import app.cn.aiyouv.www.utils.TimeUtil;
import app.cn.aiyouv.www.widget.charts.PieChartView;

/**
 * Created by Administrator on 2016/4/30.
 */
public class DataCenterActivity extends BaseActivity {
    private LinearLayout layout;
    private LinearLayout.LayoutParams params;
    private Button turn_left, turn_right;
    private TextView turn_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_center_activity);
        turn_left = (Button) findViewById(R.id.turn_left);
        turn_right = (Button) findViewById(R.id.turn_right);
        turn_view = (TextView) findViewById(R.id.turn_view);
        findViewById(R.id.title_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getAppManager().finishActivity();
            }
        });
        layout = (LinearLayout) findViewById(R.id.layout);
        layout.post(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        });
        load();
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
    }

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    layout.removeAllViews();
                    params = new LinearLayout.LayoutParams((int) (layout.getMeasuredWidth() * 0.8), (int) (layout.getMeasuredHeight() * 0.8));
                    PieChartView chartView = new PieChartView(DataCenterActivity.this);
                    layout.addView(chartView, params);
                    ArrayList<DataBean> beans = (ArrayList<DataBean>) msg.obj;
                    double max = 0;
                    for (int i = 0; i < beans.size(); i++) {
                        max += Double.parseDouble(beans.get(i).getValue());
                    }
                    chartView.putData(beans, max);

                    chartView.setInit("商户数据分析", "aiyouv.cn");

                    break;
            }
        }
    };

    private void load() {
        RequestParams params = new RequestParams();
        SharedUtils utils = new SharedUtils(DataCenterActivity.this, IUV.status);
        params.put("userId", utils.getStringValue("id"));
        params.put("appKey", utils.getStringValue("key"));
        params.put("date", turn_view.getText().toString());
        C.p(U.URL + U.Xcharts + getIntent().getStringExtra("tag"));
        AsyncHttp.post(U.URL + U.Xcharts + "merchant_analysis", params, new ResponHandler(DataCenterActivity.this) {
            @Override
            public void onSuccess(int index, String result) {
                super.onSuccess(index, result);
                C.p(result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("type");
                    if (status.equals(U.SUCCESS)) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        int len = array.length();
                        ArrayList<DataBean> datas = new ArrayList<DataBean>();
                        for (int i = 0; i < len; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            DataBean bean = new DataBean();
                            bean.setKey(obj.getString("name"));
                            bean.setValue(obj.getString("money").equals("null") ? "0" : obj.getString("money"));
                            if (!bean.getValue().equals("0")) {
                                datas.add(bean);
                            }

                            C.p(bean.getKey() + bean.getValue());
                        }
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = datas;
                        handler.sendMessage(msg);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
