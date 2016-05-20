package app.cn.aiyouv.www.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Set;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.utils.SharedUtils;
import app.cn.aiyouv.www.widget.CommonExitPop;
import app.cn.aiyouv.www.widget.CommonScPop;

/**
 * Created by Administrator on 2016/3/6.
 */
public class SettingActivity extends BaseActivity {
    private TextView cache_size;
    private RelativeLayout layout_seting0,layout_seting1,layout_seting2,layout_seting3,layout_seting4;
    private String cache;
    private Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        switch (msg.what){
            case  1:
                try {
                    File file = new File(cache);
                    if(file.exists()){
                        cache_size.setText(FormetFileSize(getFileSize(file.getAbsolutePath())));
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
        }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        cache = "/data/data/"+getPackageName()+"/cache/picasso-cache/";
        layout_seting0 = (RelativeLayout) findViewById(R.id.layout_seting0);
        layout_seting1 = (RelativeLayout) findViewById(R.id.layout_seting1);
        layout_seting2 = (RelativeLayout) findViewById(R.id.layout_seting2);
        layout_seting3 = (RelativeLayout) findViewById(R.id.layout_seting3);
        layout_seting4 = (RelativeLayout) findViewById(R.id.layout_seting4);
        findViewById(R.id.title_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getAppManager().finishActivity();
            }
        });
        cache_size = (TextView) findViewById(R.id.cache_size);
        try {
            File file = new File(cache);
            if(file.exists()){
                cache_size.setText(FormetFileSize(getFileSize(file.getAbsolutePath())));
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        layout_seting2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonScPop.showSheet(SettingActivity.this, new CommonScPop.onSelect() {
                    @Override
                    public void onClick(Object object) {
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                clearAllCache();
                                handler.sendEmptyMessage(1);
                            }
                        }.start();
                    }
                },null,null);
            }
        });
        layout_seting4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonExitPop.showSheet(SettingActivity.this, new CommonExitPop.onSelect() {
                    @Override
                    public void onClick(Object object) {
                        SharedUtils utils = new SharedUtils(SettingActivity.this, IUV.status);
                        IUV.iuv = "";
                        utils.clear();
                        final SharedUtils utils1 = new SharedUtils(SettingActivity.this, IUV.cate);
                        utils1.clear();
                        AppManager.getAppManager().finishActivity();
                    }
                },null,null);
            }
        });
    }
    public long getFileSize(String path) throws Exception {
        long size = 0;
        File f = new File(path);
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            size = size + flist[i].length();
        }
        return size;
    }

    private   void clearAllCache() {
//        deleteDir(context.getCacheDir());
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//
//        }
        deleteDir(new File(cache));
    }

    private   boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
    public String FormetFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        if(fileS==0){
            return "0.00";
        }
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }

        return fileSizeString;
    }

}
