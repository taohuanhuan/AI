package app.cn.aiyouv.www.ui;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.adapter.BillTableAdapter;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.bean.BillTableBean;
import app.cn.aiyouv.www.bean.TicketsBean;
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
 * Created by Administrator on 2016/4/16.
 */
public class BillTableActivity extends BaseActivity {
    private TextView start_date,end_date;
    private TextView txt,xchart;
    private BillTableAdapter tableAdapter;
    private TextView cate;
    private TextView search;
    private ListView content0_list;
    private PullToRefreshView refresh_layout;
    private int CLICK = -1;
    private PullToRefreshView.OnHeaderRefreshListener headerRefreshListener = new PullToRefreshView.OnHeaderRefreshListener() {
        @Override
        public void onHeaderRefresh(PullToRefreshView view) {
            CURRENT_PAGE = 1;
            load(false);
        }
    };
    private PullToRefreshView.OnFooterRefreshListener footerRefreshListener = new PullToRefreshView.OnFooterRefreshListener() {
        @Override
        public void onFooterRefresh(PullToRefreshView view) {
            CURRENT_PAGE = CURRENT_PAGE+1;
            if(CURRENT_PAGE>TOTAL_PAGER){
                handler.sendEmptyMessage(1);
                NewDataToast.makeText(BillTableActivity.this, "没有数据了").show();
            }else{
                load(true);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_table_layout);
        findViewById(R.id.title_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getAppManager().finishActivity();
            }
        });
        billTableBeans = new ArrayList<BillTableBean>();
        tableAdapter = new BillTableAdapter(getApplicationContext(),billTableBeans);
        content0_list= (ListView) findViewById(R.id.content0_list);
        xchart = (TextView) findViewById(R.id.xchart);
        content0_list.setAdapter(tableAdapter);
        refresh_layout = (PullToRefreshView) findViewById(R.id.refresh_layout);
        refresh_layout.setOnFooterRefreshListener(footerRefreshListener);
        refresh_layout.setOnHeaderRefreshListener(headerRefreshListener);
        start_date = (TextView) findViewById(R.id.start_date);
        txt = (TextView) findViewById(R.id.txt);
        end_date = (TextView) findViewById(R.id.end_date);
        cate = (TextView) findViewById(R.id.cate);
        cate.setText(getIntent().getStringExtra("title"));
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int monthOfYear=calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
        start_date.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
        end_date.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
        search = (TextView) findViewById(R.id.search);
        final DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if(CLICK==0){
                    //
                    start_date.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                }else {
                    end_date.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        xchart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillTableActivity.this,BillXchartsActivity.class);
                intent.putExtra("title",getIntent().getStringExtra("title"));
                intent.putExtra("tag",getIntent().getStringExtra("tag"));
                startActivity(intent);
            }
        });
        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CLICK = 0;
                dialog.show();
            }
        });
        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CLICK = 1;
                dialog.show();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             load(false);
            }
        });
   }
    private Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        switch (msg.what){
            case 1:
                tableAdapter.putData(billTableBeans);
                refresh_layout.onHeaderRefreshComplete();
                refresh_layout.onFooterRefreshComplete();
                txt.setText("总计："+tableAdapter.getCount()+" 次    "+tableAdapter.all()+" 元");
                break;
        }
        }
    };
    private ArrayList<BillTableBean> billTableBeans;
    private int CURRENT_PAGE = 1;
    private int TOTAL_PAGER = -1;
    private void load(final boolean isNext){
        RequestParams params = new RequestParams();
        SharedUtils utils = new SharedUtils(BillTableActivity.this, IUV.status);
        params.put("userId",utils.getStringValue("id"));
        params.put("appKey",utils.getStringValue("key"));
        params.put("pageNumber",String.valueOf(CURRENT_PAGE));
        params.put("pageSize", Common.VIEW_NUM);
        params.put("startDate",start_date.getText().toString());
        params.put("endDate", end_date.getText().toString());
        final String TAG = getIntent().getStringExtra("tag");
        AsyncHttp.post(U.URL+U.MY_BILL_LIST+TAG,params,new ResponHandler(BillTableActivity.this){
            @Override
            public void onSuccess(int index, String result) {
                super.onSuccess(index, result);
                if(!isNext){
                    billTableBeans = null;
                    billTableBeans = new ArrayList<BillTableBean>();
                }
                C.p(result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status  = jsonObject.getString("type");
                    if(status.equals(U.SUCCESS)){
                        JSONObject job = jsonObject.getJSONObject("data");
                        JSONObject obj = job.getJSONObject("pageInfo");
                        TOTAL_PAGER = obj.getInt("totalPages");
                        CURRENT_PAGE = obj.getInt("pageNumber");
                        JSONArray jsonArray = obj.getJSONArray("content");
                        int len = jsonArray.length();
                        for(int i=0;i<len;i++){
                            JSONObject o = jsonArray.getJSONObject(i);
                            BillTableBean bean = new BillTableBean();
                            bean.setName(o.getString("memberName"));
                            if(TAG.equals("payableCommissionRatio")){
                                bean.setPrice(o.getString("commissionRatio"));
                            }else if(TAG.equals("collectMerchantRatio")){
                                bean.setPrice(o.getString("merchantRatioOut"));
                            }else{
                                bean.setPrice(o.getString("money"));
                            }
                            bean.setTime(TimeUtil.getDate(o.getLong("createDate")));
                            billTableBeans.add(bean);
                        }

                        handler.sendEmptyMessage(1);
                    }else if(status.equals(U.UNLOGIN)){


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
