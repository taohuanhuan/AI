package app.cn.aiyouv.www.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.utils.SharedUtils;
import app.cn.aiyouv.www.widget.CircleImageView;
import app.cn.aiyouv.www.widget.CommonPicPop;
import app.cn.aiyouv.www.widget.NewDataToast;

/**
 * Created by Administrator on 2016/3/16.
 */
public class EditPersonActivity extends BaseActivity{
    private TextView save;
    private CircleImageView icon;
    private EditText nicheng,add,mima;
    private TextView xingbie;
    private Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case  1:
                    NewDataToast.makeText(EditPersonActivity.this,"修改成功").show();
                    AppManager.getAppManager().finishActivity();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_person_layout);
        findViewById(R.id.title_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getAppManager().finishActivity();
            }
        });
        save = (TextView) findViewById(R.id.save);
        icon = (CircleImageView) findViewById(R.id.icon);
        nicheng = (EditText) findViewById(R.id.nicheng);
        add = (EditText) findViewById(R.id.add);
        mima = (EditText) findViewById(R.id.mima);
        xingbie = (TextView) findViewById(R.id.xingbie);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonPicPop.showSheet(EditPersonActivity.this, new CommonPicPop.onSelect() {
                    @Override
                    public void camer() {
                        takePicture();
                    }

                    @Override
                    public void pic() {

                       setImage();
                    }
                });
            }
        });
        xingbie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(xingbie.getText().toString().equals("男")){
                    xingbie.setText("女");
                }else {
                    xingbie.setText("男");
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });
    }
    private String picFileFullName;
    //拍照
    public void takePicture(){
//    	Intent intent = new Intent(ListHandlerActivity.this,AutoTakePhoto.class);
//    	startActivityForResult(intent, type);
//

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!outDir.exists()) {
                outDir.mkdirs();
            }
            File outFile =  new File(outDir, System.currentTimeMillis() + ".jpg");
            picFileFullName = outFile.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, CAMER_TYPE);
        } else{
            NewDataToast.makeText(getApplicationContext(), "请插入SD卡").show();
        }
    }
    private final int  CAMER_TYPE = 101;
    private final int IMG_TYPE = 102;


    private final String IMAGE_TYPE = "image/*";
    private void setImage() {
        // TODO Auto-generated method stub
        // 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片
//		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
//		getAlbum.setType(IMAGE_TYPE);
//		startActivityForResult(getAlbum, TYPE);
        Intent getAlbum = new Intent(Intent.ACTION_PICK, null);
        getAlbum.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_TYPE);
        getAlbum.setType(IMAGE_TYPE);
        startActivityForResult(getAlbum, IMG_TYPE);

    }
    private void loadData(){
        RequestParams params = new RequestParams();
        SharedUtils utils = new SharedUtils(getApplicationContext(), IUV.status);
        params.put("userId",utils.getStringValue("id"));
        params.put("appKey",utils.getStringValue("key"));
        //file=，password=，cender=(说明：male：男，female：女),address
        params.put("address",add.getText().toString());
        params.put("name",nicheng.getText().toString());
        params.put("cender",xingbie.getText().toString().equals("男")?"male":"female");
        params.put("password",mima.getText().toString());
        if(picFileFullName!=null){
            if(picFileFullName.length()!=0){
                try {
                    params.put("file",new File(picFileFullName));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        AsyncHttp.post(U.URL+U.IUV_EDIT,params,new ResponHandler(EditPersonActivity.this){
            @Override
            public void onSuccess(int index, String result) {
                super.onSuccess(index, result);
                C.p(result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("type");
                    if(status.equals(U.SUCCESS)) {
                        handler.sendEmptyMessage(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//        params.put();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) { // 此处的 RESULT_OK 是系统自定义得一个常量
            Log.e("TAG->onresult", "ActivityResult resultCode error");
            return;
        }
        Bitmap bm = null;
        // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        ContentResolver resolver = getContentResolver();
        // 此处的用于判断接收的Activity是不是你想要的那个
        if(requestCode==CAMER_TYPE){
            //打开相机
            //	 final String picFileFullName = data.getStringExtra("path");
            System.out.println(picFileFullName);
            Picasso.with(EditPersonActivity.this).load(new File(picFileFullName)).into(icon);
        }else  if(requestCode==IMG_TYPE){
                //-------
            try {

                Uri originalUri = data.getData(); // 获得图片的uri
                bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                // 显得到bitmap图片
                //content4_ico.setImageBitmap(bm);
                // 这里开始的第二部分，获取图片的路径：
                String[] proj = { MediaStore.Images.Media.DATA };
                // 好像是android多媒体数据库的封装接口，具体的看Android文档
                Cursor cursor =  managedQuery(originalUri, proj,
                        null, null, null);
                // 按我个人理解 这个是获得用户选择的图片的索引值
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();
                // 最后根据索引值获取图片路径
                picFileFullName = cursor.getString(column_index);

                try
                {
                    //4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
                    if(Integer.parseInt(Build.VERSION.SDK) < 14)
                    {
                        cursor.close();
                    }
                }catch(Exception e)
                {
                }

                if(picFileFullName!=null){
                    C.p("pic---"+picFileFullName);
                    Picasso.with(EditPersonActivity.this).load(new File(picFileFullName)).into(icon);
                }
            } catch (IOException e) {
                Log.e("TAG-->Error", e.toString());
            }
        }

    }
}
