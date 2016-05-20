package app.cn.aiyouv.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.lang.ref.WeakReference;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseFragmentActivity;

/**
 * Created by Administrator on 2016/1/14.
 */
public class Vuforia_Qr_Activity extends BaseFragmentActivity {
    private FragmentManager fragmentManager;
    private Content2_item0 content2_item0;
    private Content2_item1 content2_item1;
    private RadioGroup group;
    private RadioButton pic,qr;
    private final int CONTENT = R.id.content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vuforia_qr_layout);
        fragmentManager = getSupportFragmentManager();
        init();
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.BUTTON_BACK:
                // 点击
                break;
            case KeyEvent.KEYCODE_BACK:
                break;
            default:
                break;
        }
        return true;

    }

    private void init(){
        pic = (RadioButton) findViewById(R.id.pic);
        qr = (RadioButton) findViewById(R.id.qr);
        group = (RadioGroup) findViewById(R.id.group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.pic:
                        INDEX = 0;
                        break;
                    case R.id.qr:
                        INDEX = 1;
                        break;
                    default:
                        break;
                }
                getFragment(INDEX);

            }
        });
        pic.setChecked(true);
    }
    private boolean isFirst = true;
    @Override
    public void onResume() {
        super.onResume();
        isFirst = false;
        if(!isFirst){
            getFragment(INDEX);
        }

    }
    public void getFragment(int what) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (what) {
            case 0:

               // transaction.replace(CONTENT, content2_item0);
              if (null == content2_item0) {
                  content2_item0 = new Content2_item0();
                  transaction.add(CONTENT,content2_item0);
                } else {
                    transaction.show(content2_item0);
                }
                break;

            case 1:
                if (null == content2_item1) {
                    content2_item1 = new Content2_item1();
                    transaction.add(CONTENT,content2_item1);

                } else {
                    transaction.show(content2_item1);
                }
                break;

            default:
                break;
        }
        INDEX = what;
        transaction.commitAllowingStateLoss();
    }
    private void hideFragment(FragmentTransaction transaction){
        if(content2_item0!=null){
            transaction.hide(content2_item0);
        }
        if(content2_item1!=null){
            transaction.hide(content2_item1);
        }
    }
    private int INDEX = 0;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index", INDEX);
        outState.putBoolean("isFirst",false);
        //
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState.containsKey("index")){
            INDEX = savedInstanceState.getInt("index");
            isFirst = savedInstanceState.getBoolean("isFirst");
        }
    }
}
