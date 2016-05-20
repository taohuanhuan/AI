package app.cn.aiyouv.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import java.lang.ref.WeakReference;
import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;

/**
 * Created by Administrator on 2016/2/1.
 */
public class WelcomeActivity extends BaseActivity{
    private Welcome_Handler welcome_handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        welcome_handler = new Welcome_Handler(WelcomeActivity.this);
        time();
    }
    private void time(){
        new Thread() {
            public void run() {
                for (int i = 3; i >= 0; i--) {
                    if (i == 0) {
                        welcome_handler.sendEmptyMessage(8);
                    } else {


                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }
            };
        }.start();
    }
    private class Welcome_Handler extends Handler {
        WeakReference<WelcomeActivity> mLeakActivityRef;
        public Welcome_Handler(WelcomeActivity leakActivity) {
            mLeakActivityRef = new WeakReference<WelcomeActivity>(leakActivity);
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if(mLeakActivityRef.get()!=null){
                switch (msg.what){

                    case 8:

                        Intent intent = new Intent(WelcomeActivity.this,AdverActivity.class);
                        startActivity(intent);
                        AppManager.getAppManager().finishActivity();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
