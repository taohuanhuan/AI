package app.cn.aiyouv.www.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.bean.Advert;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.common.Common;
import app.cn.aiyouv.www.common.ConstantValues;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.download.DownloadListener;
import app.cn.aiyouv.www.download.DownloadTask;
import app.cn.aiyouv.www.download.DownloadTaskManager;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.utils.SharedUtils;

/**
 * Created by Administrator on 2016/1/23.
 */
public class AdverActivity extends BaseActivity {
    private ImageView image;
    private TextView timer;
    private DownloadTaskManager taskManager;
    private SharedUtils utils;
    private Advert_Handler vd_handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adver_layout);
    image = (ImageView) findViewById(R.id.image);
        timer = (TextView) findViewById(R.id.timer);

        utils = new SharedUtils(getApplicationContext(),"advert");
        C.p(utils.getStringValue("url"));
        if(utils.getStringValue("url").length()!=0){
            Picasso.with(getApplicationContext()).load(new File(utils.getStringValue("url"))).config(Bitmap.Config.RGB_565).into(image);
        }
        loadData();
        vd_handler = new Advert_Handler(AdverActivity.this);

        time();
    }
    private void time(){
        new Thread() {
            public void run() {
                for (int i = 3; i >= 0; i--) {
                    if (i == 0) {
                        vd_handler.sendEmptyMessage(8);
                    } else {
                        Message msg = new Message();
                        msg.arg1 = i;
                        msg.what = 7;
                        vd_handler.sendMessage(msg);

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

    private class Advert_Handler extends Handler{
        WeakReference<AdverActivity> mLeakActivityRef;
        public Advert_Handler(AdverActivity leakActivity) {
            mLeakActivityRef = new WeakReference<AdverActivity>(leakActivity);
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if(mLeakActivityRef.get()!=null){
                switch (msg.what){
                    case 1:
                        C.p("第二次"+utils.getStringValue("url"));
                        //下载成功就进行数据
                        Picasso.with(getApplicationContext()).load(new File(utils.getStringValue("url"))).config(Bitmap.Config.RGB_565).into(image);
                        break;
                    case 2:
                        Advert advert = (Advert) msg.obj;
                        down(U.URL+advert.getPath());
                        break;
                    case 7:
                        timer.setText(msg.arg1 + "s");
                        break;
                    case 8:
                        timer.setText("0s");
                        Intent intent = new Intent(AdverActivity.this,MainFragmentActivity.class);
                        startActivity(intent);
                        AppManager.getAppManager().finishActivity();
                        break;
                    default:
                        break;
                }
            }
        }
    }
    private void loadData(){
        RequestParams params = new RequestParams();
        params.put("flag", ConstantValues.INDEX);
        AsyncHttp.post(U.URL+U.ADVERT,params,new ResponHandler(AdverActivity.this){
            @Override
            public void onSuccess(int i, String s) {
                super.onSuccess(i, s);
                if (s != null){
                    try {
                        C.p((s == null) + "内容" + s);
                        JSONObject jsonObject = new JSONObject(s);
                        String status = jsonObject.getString("type");
                        if (status.equals(U.SUCCESS)) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            int len = jsonArray.length();
                            if (len == 1) {
                                Advert advert = new Advert();
                                JSONObject obj = jsonArray.getJSONObject(0);
                                advert.setPath(obj.getString("path"));
                                advert.setUrl(obj.getString("url"));
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = advert;
                                vd_handler.sendMessage(msg);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
            }
        });
    }


    /**
     * 下载资源文件
     * @param url
     * @param version
     */
    private void down(String url){
        File file = new File(Common.AIYOUV_PATH+Common.ADVER_CACHE+Common.AIYOUV_INDEX);
        if(!file.exists()){
            file.mkdirs();
        }
        taskManager = DownloadTaskManager.getInstance(getApplicationContext());

        String name = url.substring(
                url.lastIndexOf("/") + 1,
                url.lastIndexOf("."));
        String fullName = url.substring(url.lastIndexOf("/") + 1,
                url.length());
        final DownloadTask task = new DownloadTask(url, Common.AIYOUV_PATH+Common.ADVER_CACHE+Common.AIYOUV_INDEX, fullName, name, null);

        taskManager.registerListener(task, new DownloadListener() {
            @Override
            public void onDownloadFinish(String filepath) {
                taskManager.deleteDownloadTask(task);

                utils.setStringValue("url", filepath);
                vd_handler.sendEmptyMessage(1);
//                taskManager.deleteDownloadTaskFile(task);
            }

            @Override
            public void onDownloadStart() {

            }

            @Override
            public void onDownloadPause() {

            }

            @Override
            public void onDownloadStop() {

            }

            @Override
            public void onDownloadFail() {

            }

            @Override
            public void onDownloadProgress(int finishedSize, int totalSize, int speed) {

            }
        });
        taskManager.startDownload(task);
    }

}
