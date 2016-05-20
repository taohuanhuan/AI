package app.cn.aiyouv.www.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseFragmentActivity;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.common.Common;
import app.cn.aiyouv.www.common.ConstantValues;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.pic.PublishedActivity;
import app.cn.aiyouv.www.utils.SharedUtils;
import app.cn.aiyouv.www.widget.CommonLrPop;
import app.cn.aiyouv.www.widget.NewDataToast;

public class MainFragmentActivity extends BaseFragmentActivity {
    private FragmentManager fragmentManager;
    private MainFragmentContent0 fragmentContent0;
    private MainFragmentContent1 fragmentContent1;
    private MainFragmentContent2 fragmentContent2;
    private MainFragmentContent3 fragmentContent3;
    private MainFragmentContent4 fragmentContent4;
    private int INDEX = ConstantValues.FRAGMENTCONTENT0;
    private RadioGroup main_bottom_rtns;
    private RadioButton main_bottom_rtn0, main_bottom_rtn1, main_bottom_rtn2;
    private RadioButton click;
    private final int CONTENT = R.id.main_content;
    private MainFragment_Handler mainFragment_handler;
    private long exitTime = 0;
    private LocationClient mLocationClient;

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(Common.LOCATIONMODE);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(Common.COOR_COUNT);//可选，默认gcj02，设置返回的定位结果坐标系，
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) // System.currentTimeMillis()无论何时调用，肯定大于2000
        {
           // NewDataToast.makeText(getApplicationContext(), "再按一次退出程序").show();
            Toast.makeText(getApplicationContext(),"再按一次退出程序~~",Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            AppManager.getAppManager().finishAllActivity();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        /*	if(IUV.iuv.length()==0&&main_bottom_rtns.getCheckedRadioButtonId()==R.id.main_bottom_rtn4){
                main_bottom_rtns.check(R.id.main_bottom_rtn0);
			}else {
			}*/
            exit();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_activity);
        mainFragment_handler = new MainFragment_Handler(MainFragmentActivity.this);
        init();
        initLayout();
        mLocationClient = new LocationClient(MainFragmentActivity.this);
        initLocation();
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {

                SharedUtils utils = new SharedUtils(MainFragmentActivity.this, IUV.local);
                utils.setStringValue("lng", String.valueOf(bdLocation.getLongitude()));
                utils.setStringValue("lat", String.valueOf(bdLocation.getLatitude()));
            }
        });

        mLocationClient.start();
    }

    public class MainFragment_Handler extends Handler {
        WeakReference<MainFragmentActivity> mLeakActivityRef;

        public MainFragment_Handler(MainFragmentActivity leakActivity) {
            mLeakActivityRef = new WeakReference<MainFragmentActivity>(leakActivity);
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (mLeakActivityRef.get() != null) {
                switch (msg.what) {
                    case 0:

                        break;
                    case R.id.main_bottom_rtn0:
                        main_bottom_rtn0.setChecked(true);
//						main_bottom_rtns.check(R.id.main_bottom_rtn0);
                        break;
                    default:

                        break;
                }
            }
        }
    }

    private void initLayout() {
        main_bottom_rtns = (RadioGroup) findViewById(R.id.main_bottom_rtns);
        main_bottom_rtn0 = (RadioButton) findViewById(R.id.main_bottom_rtn0);
        main_bottom_rtn1 = (RadioButton) findViewById(R.id.main_bottom_rtn1);
        main_bottom_rtn2 = (RadioButton) findViewById(R.id.main_bottom_rtn2);
        click = (RadioButton) findViewById(R.id.click);
        main_bottom_rtns.setOnCheckedChangeListener(listener);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IUV.iuv.length() == 0) {
                    CommonLrPop.showSheet(MainFragmentActivity.this, null, mainFragment_handler, null);
                } else {
                    Intent intent = new Intent(MainFragmentActivity.this, PublishedActivity.class);
                    startActivity(intent);
                }

            }
        });
        main_bottom_rtn0.setChecked(true);

    }

    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup arg0, int arg1) {
            // TODO Auto-generated method stub
            getFragment(arg1);
        }
    };

    private void init() {

        fragmentManager = getSupportFragmentManager();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getFragment(INDEX);
		 if(IUV.iuv.length()==0){
			  relogin();
		 }
       


    }

    private void relogin() {
        final SharedUtils utils = new SharedUtils(getApplicationContext(), IUV.status);
        final RequestParams params = new RequestParams();
        final String name = utils.getStringValue("user");
        final String password = utils.getStringValue("pwd");
        params.put("phone", utils.getStringValue("user"));
        params.put("password", utils.getStringValue("pwd"));
        AsyncHttp.post(U.URL + U.IUV_LOGIN, params, new ResponHandler() {
            @Override
            public void onSuccess(int i, String s) {
                super.onSuccess(i, s);
                C.p("登录" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("type");
                    if (status.equals(U.SUCCESS)) {
                        //
                        JSONObject obj = jsonObject.getJSONObject("data");
                        if (obj.has("phone")) {
                            utils.setStringValue("photo", obj.getString("phone"));
                        }
                        utils.setStringValue("id", obj.getString("id"));
                        utils.setStringValue("user", name);
                        utils.setStringValue("pwd", password);
                        utils.setStringValue("loginType", obj.getString("loginType"));
                        utils.setStringValue("key", obj.getString("username"));
                        IUV.iuv = utils.getStringValue("key");
                    } else {
                        IUV.iuv = "";
                        utils.clear();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void getFragment(int what) {
        System.out.println("加载");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//		hideFragment(transaction);
        switch (what) {
            case R.id.main_bottom_rtn0:
            case ConstantValues.FRAGMENTCONTENT1:
                fragmentContent1 = new MainFragmentContent1();
                transaction.replace(CONTENT, fragmentContent1);

				/*if (null == fragmentContent0) {

				} else {
					transaction.show(fragmentContent0);
				}*/
                break;
            case R.id.main_bottom_rtn1:
            case ConstantValues.FRAGMENTCONTENT0:
                fragmentContent0 = new MainFragmentContent0();
                transaction.replace(CONTENT, fragmentContent0);


				/*if (null == fragmentContent1) {
					fragmentContent1 = new MainFragmentContent1();
					transaction.replace(CONTENT, fragmentContent1);
				} else {
					transaction.show(fragmentContent1);
				}*/
                break;
            case R.id.main_bottom_rtn2:
            case ConstantValues.FRAGMENTCONTENT2:

                fragmentContent2 = new MainFragmentContent2();
                transaction.replace(CONTENT, fragmentContent2);
				/*if (null == fragmentContent2) {
					fragmentContent2 = new MainFragmentContent2();
					transaction.replace(CONTENT, fragmentContent2);
				} else {
					transaction.show(fragmentContent2);
				}*/
                break;
            case R.id.main_bottom_rtn3:
            case ConstantValues.FRAGMENTCONTENT3:
                fragmentContent3 = new MainFragmentContent3();
                transaction.replace(CONTENT, fragmentContent3);

				/*if (null == fragmentContent3) {
					fragmentContent3 = new MainFragmentContent3();
					transaction.replace(CONTENT, fragmentContent3);
				} else {
					transaction.show(fragmentContent3);
				}*/
                break;
            case R.id.main_bottom_rtn4:
            case ConstantValues.FRAGMENTCONTENT4:
                fragmentContent4 = new MainFragmentContent4(mainFragment_handler);
                transaction.replace(CONTENT, fragmentContent4);
				/*if (null == fragmentContent4) {
					fragmentContent4 = new MainFragmentContent4();
					transaction.replace(CONTENT, fragmentContent4);
				} else {
					transaction.show(fragmentContent4);
				}*/
                break;
            default:
                break;
        }
        INDEX = what;
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // 自己记录fragment的位置,防止activity被系统回收时，fragment错乱的问题
        // super.onSaveInstanceState(outState);
        outState.putInt("index", INDEX);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // super.onRestoreInstanceState(savedInstanceState);
        INDEX = savedInstanceState.getInt("index");
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (fragmentContent0 != null) {
            transaction.hide(fragmentContent0);
        }
        if (fragmentContent1 != null) {
            transaction.hide(fragmentContent1);
        }
        if (fragmentContent2 != null) {
            transaction.hide(fragmentContent2);
        }
        if (fragmentContent3 != null) {
            transaction.hide(fragmentContent3);
        }
        if (fragmentContent4 != null) {
            transaction.hide(fragmentContent4);
        }

    }

}
