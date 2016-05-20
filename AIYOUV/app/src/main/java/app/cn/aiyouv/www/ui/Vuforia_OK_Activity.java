package app.cn.aiyouv.www.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.bean.VuforiaOk;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.common.Common;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.widget.CommonNavPop;

/**
 * Created by Administrator on 2016/1/22.
 */
public class Vuforia_OK_Activity extends BaseActivity{
    private ImageView close,qr_bg;
    private VO_Handler vo_handler;
    private LinearLayout layout2,layout1,layout0,layout3;
    private TextView title;
    private LocationClient mLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vuforia_ok_layout);
        close = (ImageView) findViewById(R.id.close);
        qr_bg = (ImageView) findViewById(R.id.qr_bg);
        title = (TextView) findViewById(R.id.title);
        layout0 = (LinearLayout) findViewById(R.id.layout0);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);
        if(initDirs()){
            initNavi();
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getAppManager().finishActivity();
            }
        });

        vo_handler = new VO_Handler(Vuforia_OK_Activity.this);
        loadData();

    }


    private class VO_Handler extends Handler {
        public WeakReference<Vuforia_OK_Activity> mLeakActivityRef;

        public VO_Handler(Vuforia_OK_Activity leakActivity) {
            mLeakActivityRef = new WeakReference<Vuforia_OK_Activity>(leakActivity);
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if(mLeakActivityRef.get()!=null){
                switch (msg.what){
                    case 1:
                        final VuforiaOk ok = (VuforiaOk) msg.obj;
                        C.p("图片地址" + U.URL + ok.getImg());
                        Picasso.with(getApplicationContext()).load(U.URL + ok.getImg()).config(Bitmap.Config.RGB_565).into(qr_bg);
                        title.setText(ok.getTitle());

                        layout0.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CommonNavPop.showSheet(Vuforia_OK_Activity.this, new CommonNavPop.onSelect() {
                                    @Override
                                    public void onClick(Object object) {
                                        //开始导航
                                        LocationClientOption op = new LocationClientOption();
                                        op.setIsNeedLocationDescribe(true);
                                        mLocationClient = new LocationClient(Vuforia_OK_Activity.this);
                                        mLocationClient.setLocOption(op);
                                        mLocationClient.registerLocationListener(new BDLocationListener() {
                                            @Override
                                            public void onReceiveLocation(BDLocation location) {
                                                // TODO Auto-generated method stub
                                                double lat = location.getLatitude();
                                                double lng = location.getLongitude();
                                                C.p(lat + "--" + lng);
                                                if (BaiduNaviManager.isNaviInited()) {
                                                    routeplanToNavi(BNRoutePlanNode.CoordinateType.WGS84, ok, lng, lat);
                                                }
                                            }
                                        });
                                        mLocationClient.start();
                                    }
                                }, null, null);
                            }
                        });

                        layout1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(ok.getId0().startsWith("http://")||ok.getId0().startsWith("https://")){
                                    Intent intent = new Intent(Vuforia_OK_Activity.this,CommonWeb.class);
                                    intent.putExtra("title","详情");
                                    intent.putExtra("url",ok.getId0());
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(Vuforia_OK_Activity.this,ArticleDetailActivity.class);
                                    intent.putExtra("acticle_id",ok.getId0());
                                    startActivity(intent);
                                }
                            }
                        });
                        layout2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(ok.getId1().startsWith("http://")||ok.getId1().startsWith("https://")){
                                    Intent intent = new Intent(Vuforia_OK_Activity.this,CommonWeb.class);
                                    intent.putExtra("title","详情");
                                    intent.putExtra("url",ok.getId1());
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(Vuforia_OK_Activity.this, ArticleDetailActivity.class);
                                    intent.putExtra("acticle_id", ok.getId1());
                                    startActivity(intent);
                                }
                            }
                        });
                        layout3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(ok.getId2().startsWith("http://")||ok.getId2().startsWith("https://")){
                                    Intent intent = new Intent(Vuforia_OK_Activity.this,CommonWeb.class);
                                    intent.putExtra("title","详情");
                                    intent.putExtra("url",ok.getId2());
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(Vuforia_OK_Activity.this, ArticleDetailActivity.class);
                                    intent.putExtra("acticle_id", ok.getId2());
                                    startActivity(intent);
                                }
                            }
                        });
                        break;
                    default:

                        break;
                }
            }
        }
    }
    private void loadData(){
        RequestParams params = new RequestParams();
        params.put("flag",getIntent().getStringExtra("flag"));//getIntent().getStringExtra("flag")
        AsyncHttp.post(U.URL+U.VUFORIA_OK,params,new ResponHandler(Vuforia_OK_Activity.this){
            @Override
            public void onSuccess(int i, String s) {
                super.onSuccess(i, s);
                C.p(U.URL+U.VUFORIA_OK+"结果"+s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("type");
                    if(status.equals(U.SUCCESS)){
                        JSONObject obj = jsonObject.getJSONObject("data");
                        VuforiaOk vuforiaOk = new VuforiaOk();
                        vuforiaOk.setId0(obj.getString("articleOne"));
                        vuforiaOk.setId1(obj.getString("articleTwo"));
                        vuforiaOk.setId2(obj.getString("articleThree"));
                        vuforiaOk.setImg( obj.getString("bgImage"));
                        vuforiaOk.setLat(Double.parseDouble(obj.getString("latitude")));
                        vuforiaOk.setLng(Double.parseDouble(obj.getString("longitude")));
                        vuforiaOk.setTitle(obj.getString("title"));
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = vuforiaOk;
                        vo_handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //------------------------------Baidu
    private boolean initDirs() {

        File f = new File(Common.AIYOUV_PATH+Common.BAIDU_CACHE);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    private void routeplanToNavi(BNRoutePlanNode.CoordinateType coType,VuforiaOk ok,double startLng,double startLat) {
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        sNode = new BNRoutePlanNode(startLng,startLat,
                "", null, coType);
        eNode = new BNRoutePlanNode(ok.getLng(),ok.getLat(),
                "", null, coType);
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new VuforiaRoutePlanListener(sNode));
        }
    }
    private class VuforiaRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;
        public VuforiaRoutePlanListener(BNRoutePlanNode node){
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            Intent intent = new Intent(Vuforia_OK_Activity.this, BNGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("to_d", (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
        }
    }
    private BNOuterTTSPlayerCallback mTTSCallback = new BNOuterTTSPlayerCallback() {

        @Override
        public void stopTTS() {
            // TODO Auto-generated method stub

        }

        @Override
        public void resumeTTS() {
            // TODO Auto-generated method stub

        }

        @Override
        public void releaseTTSPlayer() {
            // TODO Auto-generated method stub

        }

        @Override
        public int playTTSText(String speech, int bPreempt) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void phoneHangUp() {
            // TODO Auto-generated method stub

        }

        @Override
        public void phoneCalling() {
            // TODO Auto-generated method stub

        }

        @Override
        public void pauseTTS() {
            // TODO Auto-generated method stub

        }

        @Override
        public void initTTSPlayer() {
            // TODO Auto-generated method stub

        }

        @Override
        public int getTTSState() {
            // TODO Auto-generated method stub
            return 0;
        }
    };
    private void initNavi() {
        BaiduNaviManager.getInstance().setNativeLibraryPath(Common.AIYOUV_PATH+Common.BAIDU_CACHE );
        BaiduNaviManager.getInstance().init(this, Common.AIYOUV_PATH, Common.BAIDU_CACHE,
                new BaiduNaviManager.NaviInitListener() {
                    @Override
                    public void onAuthResult(int status, String msg) {
                        if (0 == status) {
                            System.out.println("key校验成功!");
                        } else {
                            System.out.println("key校验失败");
                        }
                    }

                    public void initSuccess() {
                    }

                    public void initStart() {
                    }

                    public void initFailed() {
                    }
                }, null /*mTTSCallback*/);
    }
}
