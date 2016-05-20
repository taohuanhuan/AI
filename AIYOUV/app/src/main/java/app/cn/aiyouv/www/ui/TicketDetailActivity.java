package app.cn.aiyouv.www.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.bean.LoginStatus;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.utils.SharedUtils;
import app.cn.aiyouv.www.widget.NewDataToast;

/**
 * Created by Administrator on 2016/3/6.
 */
public class TicketDetailActivity extends BaseActivity {
    private int SCREEN_WIDTH = 0;
    private LinearLayout layout0, layout1;
    private ImageView content4_img;
    private ImageView erweima;
    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    Map<String, String> values = (Map<String, String>) msg.obj;
                    content4_item0.setText(values.get("name"));
                    content4_item1.setText(getString(R.string.content4_item0_txt, ""));
                    content4_item2.setText("");
                    content4_item3.setText("");
                    Picasso.with(getApplicationContext()).load(U.URL + values.get("merchantImage")).into(content4_img);
                    tip.setText(values.get("tip"));
                    price.setText(values.get("money") + "元");
                    code.setText("优惠码:" + values.get("verificationCode"));
                    createImage(values.get("verificationCode"));
                    break;

            }
        }
    };
    private TextView content4_item0, content4_item1, content4_item2, content4_item3, price, tip, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket_detail_layout);
        init();
        findViewById(R.id.title_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getAppManager().finishActivity();
            }
        });
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout0 = (LinearLayout) findViewById(R.id.layout0);
        price = (TextView) findViewById(R.id.price);
        code = (TextView) findViewById(R.id.code);
        tip = (TextView) findViewById(R.id.tip);
        content4_img = (ImageView) findViewById(R.id.content4_img);
        process(layout0, 256, 686);
        process_z(content4_img, 158, 690);
        process_z(layout1, 256, 686);
//        process_z(erweima,424,640);

        load();
    }

    private void load() {
        RequestParams params = new RequestParams();
        SharedUtils utils = new SharedUtils(getApplicationContext(), IUV.status);
        params.put("userId", utils.getStringValue("id"));
        params.put("appKey", utils.getStringValue("key"));
        params.put("id", getIntent().getStringExtra("tick_id"));
        AsyncHttp.post(U.URL + U.IUV_Q_DETAIL, params, new ResponHandler() {
            @Override
            public void onSuccess(int index, String result) {
                super.onSuccess(index, result);
                C.p(result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("type");
                    if (status.equals(U.SUCCESS)) {
                        JSONObject obj = jsonObject.getJSONObject("data");
                        JSONObject o = obj.getJSONObject("ticket");
                        Map<String, String> values = new HashMap<String, String>();
                        values.put("verificationCode", o.getString("verificationCode"));
                        values.put("name", o.getString("merchantName"));
                        values.put("merchantImage", o.getString("merchantImage"));
                        values.put("tip", o.getString("tip"));
                        values.put("money", o.getString("money"));
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = values;
                        handler.sendMessage(msg);


                    } else if (U.UNLOGIN.equals(status)) {
                        NewDataToast.makeText(getApplicationContext(), jsonObject.getString("content")).show();
                        SharedUtils utils = new SharedUtils(getApplicationContext(), IUV.status);
                        IUV.iuv = "";
                        utils.clear();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void process_z(View view, int h, int w) {
        double mix_f = SCREEN_WIDTH * (double) h / w;
        LinearLayout.LayoutParams paramsf = new LinearLayout.LayoutParams((int) mix_f,
                (int) mix_f);
        view.setLayoutParams(paramsf);
    }

    private void process(View view, int h, int w) {
        double mix_f = SCREEN_WIDTH * (double) h / w;
        LinearLayout.LayoutParams paramsf = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                (int) mix_f);
        view.setLayoutParams(paramsf);
    }

    private void init() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SCREEN_WIDTH = dm.widthPixels;
        erweima = (ImageView) findViewById(R.id.erweima);
        content4_item0 = (TextView) findViewById(R.id.content4_item0);
        content4_item1 = (TextView) findViewById(R.id.content4_item1);
        content4_item2 = (TextView) findViewById(R.id.content4_item2);
        content4_item3 = (TextView) findViewById(R.id.content4_item3);

    }

    private void createImage(String formatValue) {
        try {
            // 需要引入core包
            int width = (int) (SCREEN_WIDTH * (double) 424 / 640);
            int QR_WIDTH = width;
            int QR_HEIGHT = width;
            QRCodeWriter writer = new QRCodeWriter();

            if (formatValue == null || "".equals(formatValue)
                    || formatValue.length() < 1) {
                return;
            }

            // 把输入的文本转为二维码
            BitMatrix martix = writer.encode(formatValue,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);
            // 二维码的code
            System.out.println("w:" + martix.getWidth() + "h:"
                    + martix.getHeight());

            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 0);
            BitMatrix bitMatrix = new QRCodeWriter().encode(formatValue,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }

                }
            }
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);

            erweima.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
