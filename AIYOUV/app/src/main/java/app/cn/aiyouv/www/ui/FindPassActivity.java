package app.cn.aiyouv.www.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.widget.NewDataToast;

/**
 * Created by Administrator on 2016/5/7.
 */
public class FindPassActivity extends BaseActivity {
    private TextView register_item2;
    private EditText register_item1,register_item0;
    private Button register_item5;
    private   Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {

            super.dispatchMessage(msg);
            switch (msg.what){
                case 7:
                    register_item2.setEnabled(false);
                    register_item2.setText(msg.arg1 + "s");
                    NewDataToast.makeText(FindPassActivity.this,"短信验证码已下发!").show();
                    break;
                case 8:
                    register_item2.setEnabled(true);
                    register_item2.setText("发送验证码");
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_password);
        findViewById(R.id.title_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getAppManager().finishActivity();
            }
        });
        register_item2 = (TextView) findViewById(R.id.register_item2);
        register_item0 = (EditText) findViewById(R.id.register_item0);
        register_item1 = (EditText) findViewById(R.id.register_item1);
        register_item5 = (Button) findViewById(R.id.register_item5);
        register_item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams params = new RequestParams();
                params.put("phone",register_item0.getText().toString());
                params.put("code",register_item1.getText().toString());

                AsyncHttp.post(U.URL+U.RESET,params,new ResponHandler(FindPassActivity.this){
                    @Override
                    public void onSuccess(int index, String result) {
                        super.onSuccess(index, result);
                        C.p(result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("type");
                            if (status.equals(U.SUCCESS)) {
                                NewDataToast.makeText(FindPassActivity.this,"重置成功，请注意手机短信").show();
                            }else{
                                NewDataToast.makeText(FindPassActivity.this,jsonObject.getString("content")).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        register_item2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = register_item0.getText().toString();
                if (phone.length() == 0 || phone.length() != 11 || !phone.startsWith("1")) {
                    register_item0.setError("手机号码格式不正确");
                    register_item0.findFocus();
                }   else {

                    RequestParams params = new RequestParams();
                    params.put("phone", phone);
                    AsyncHttp.post(U.URL + U.MOD, params, new ResponHandler() {
                        @Override
                        public void onSuccess(int i, String s) {
                            super.onSuccess(i, s);
                            C.p("发送验证码" + s);
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String status = jsonObject.getString("type");
                                if (status.equals(U.SUCCESS)) {
                                    new Thread() {
                                        public void run() {
                                            for (int i = 60; i >= 0; i--) {
                                                if (i == 0) {
                                                    handler.sendEmptyMessage(8);
                                                } else {
                                                    Message msg = new Message();
                                                    msg.arg1 = i;
                                                    msg.what = 7;
                                                    handler.sendMessage(msg);

                                                    try {
                                                        Thread.sleep(1000);
                                                    } catch (InterruptedException e) {
                                                        // TODO Auto-generated catch block
                                                        e.printStackTrace();
                                                    }

                                                }
                                            }
                                        }

                                        ;
                                    }.start();
                                } else {
                                    NewDataToast.makeText(FindPassActivity.this, jsonObject.getString("content")).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });


                }
            }
        });
    }
}
