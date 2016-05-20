package app.cn.aiyouv.www.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.bean.LoginStatus;
import app.cn.aiyouv.www.bean.PayResult;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.common.Common;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.utils.SharedUtils;
import app.cn.aiyouv.www.utils.SignUtils;
import app.cn.aiyouv.www.widget.CommonLrPop;
import app.cn.aiyouv.www.widget.NewDataToast;

public class MainFragmentContent4 extends Fragment {
    private LinearLayout layout0, layout1, dianzan, guanzhu;
    private ImageView vip;
    private RelativeLayout layout41, layout42, layout43, layout44, layout45, layout46, layout47, layout49, layout50, layout51, layout52;
    private ImageView content4_img;
    private TextView content4_item0, content4_item1, content4_item2, content4_item3, center_tip;
    private MainFragmentActivity.MainFragment_Handler mainFragment_handler;
    public Content4_Handler content4_handler;

    public MainFragmentContent4() {

    }

    @SuppressLint("ValidFragment")
    public MainFragmentContent4(MainFragmentActivity.MainFragment_Handler mainFragment_handler) {
        this.mainFragment_handler = mainFragment_handler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_content_layout4, container, false);
        return view;
    }

    private Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    private int SCREEN_WIDTH = 0;

    private void init() {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        SCREEN_WIDTH = dm.widthPixels;
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

    private void check() {
        SharedUtils utils = new SharedUtils(activity, IUV.status);
        if (utils.getStringValue("loginType").equals(U.S)) {
            //商家
            layout50.setVisibility(View.VISIBLE);
            layout51.setVisibility(View.VISIBLE);
            layout52.setVisibility(View.VISIBLE);
            layout41.setVisibility(View.GONE);
            layout42.setVisibility(View.GONE);
            layout43.setVisibility(View.GONE);
            layout47.setVisibility(View.GONE);
            center_tip.setText("商户中心");

        } else if (utils.getStringValue("loginType").equals(U.M)) {
            //个人用户
            layout41.setVisibility(View.VISIBLE);
            layout42.setVisibility(View.VISIBLE);
            layout43.setVisibility(View.VISIBLE);
            layout47.setVisibility(View.VISIBLE);
            layout50.setVisibility(View.GONE);
            layout51.setVisibility(View.GONE);
            layout52.setVisibility(View.GONE);
            center_tip.setText("个人中心");
        }
    }

    public class Content4_Handler extends Handler {
        WeakReference<MainFragmentContent4> mLeakActivityRef;

        public Content4_Handler(MainFragmentContent4 leakActivity) {
            mLeakActivityRef = new WeakReference<MainFragmentContent4>(leakActivity);
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (mLeakActivityRef.get() != null) {
                switch (msg.what) {
                    case 1:
                        check();
                        LoginStatus login = (LoginStatus) msg.obj;
                        content4_item0.setText(login.getName());
                        content4_item1.setText(activity.getString(R.string.content4_item0_txt, login.getFensi()));
                        content4_item2.setText(login.getDianzan());
                        content4_item3.setText(login.getGuanzhu());


                        Picasso.with(getActivity()).load(U.URL + login.getImg()).into(content4_img);
//                       content4_handler.sendEmptyMessage(2);
                        break;
                    case 2:

                        loadInfo();
                        break;
                    case SDK_PAY_FLAG:

                        PayResult payResult = new PayResult((String) msg.obj);
                        /**
                         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                         * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                         * docType=1) 建议商户依赖异步通知
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (TextUtils.equals(resultStatus, "9000")) {
                            NewDataToast.makeText(activity, "支付成功").show();
                        } else {
                            // 判断resultStatus 为非"9000"则代表可能支付失败
                            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
                                NewDataToast.makeText(activity, "支付结果确认中").show();

                            } else {
                                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                NewDataToast.makeText(activity, "支付失败").show();

                            }
                        }
                        break;
                    default:

                        break;
                }
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        C.p("==");
        content4_handler = new Content4_Handler(this);
        init();
        layout1 = (LinearLayout) view.findViewById(R.id.layout1);
        layout0 = (LinearLayout) view.findViewById(R.id.layout0);
        content4_img = (ImageView) view.findViewById(R.id.content4_img);
        process(layout0, 256, 686);
        process_z(content4_img, 158, 690);
        process_z(layout1, 256, 686);
        init(view);
        layout0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, EditPersonActivity.class);
                startActivity(intent);
            }
        });
        if (IUV.iuv.length() == 0) {
            //代表未登录
            CommonLrPop.showSheet(activity, new CommonLrPop.onSelect() {
                @Override
                public void onClick(Object object) {

                }
            }, mainFragment_handler, content4_handler);
        } else {
            final SharedUtils utils = new SharedUtils(activity, IUV.status);
            content4_item0.setText(utils.getStringValue("name"));
            content4_item1.setText(activity.getString(R.string.content4_item0_txt, utils.getStringValue("fans")));
            content4_item2.setText(utils.getStringValue("dianzan"));
            content4_item3.setText(utils.getStringValue("attention"));
            Picasso.with(getActivity()).load(U.URL + utils.getStringValue("image")).into(content4_img);

            content4_handler.sendEmptyMessage(2);
        }
    }

    private void init(View view) {
        center_tip = (TextView) view.findViewById(R.id.center_tip);
        guanzhu = (LinearLayout) view.findViewById(R.id.guanzhu);
        dianzan = (LinearLayout) view.findViewById(R.id.dianzan);
        vip = (ImageView) view.findViewById(R.id.vip);
        content4_item0 = (TextView) view.findViewById(R.id.content4_item0);
        content4_item1 = (TextView) view.findViewById(R.id.content4_item1);
        content4_item2 = (TextView) view.findViewById(R.id.content4_item2);
        content4_item3 = (TextView) view.findViewById(R.id.content4_item3);
        layout41 = (RelativeLayout) view.findViewById(R.id.layout41);
        layout42 = (RelativeLayout) view.findViewById(R.id.layout42);
        layout43 = (RelativeLayout) view.findViewById(R.id.layout43);
        layout44 = (RelativeLayout) view.findViewById(R.id.layout44);
        layout45 = (RelativeLayout) view.findViewById(R.id.layout45);
        layout46 = (RelativeLayout) view.findViewById(R.id.layout46);
        layout47 = (RelativeLayout) view.findViewById(R.id.layout47);
        layout49 = (RelativeLayout) view.findViewById(R.id.layout49);
        layout50 = (RelativeLayout) view.findViewById(R.id.layout50);
        layout51 = (RelativeLayout) view.findViewById(R.id.layout51);
        layout52 = (RelativeLayout) view.findViewById(R.id.layout52);
        layout49.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MsgActivity.class);
                startActivity(intent);
            }
        });
        layout50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, BillManagerActivity.class);
                startActivity(intent);
            }
        });
        layout51.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DataCenterActivity.class);
                startActivity(intent);
            }
        });
        layout52.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, RewardListActivity.class);
                startActivity(intent);
            }
        });

        layout41.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TicketsManagerActivity.class);
                startActivity(intent);
            }
        });
        layout42.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MyEditActivity.class);
                startActivity(intent);
            }
        });
        layout43.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MyPublishActivity.class);
                startActivity(intent);
            }
        });
        layout44.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MyEwmActivity.class);
                startActivity(intent);
            }
        });
        layout45.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SettingActivity.class);
                startActivity(intent);
            }
        });
        layout47.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TicketsCActivity.class);
                startActivity(intent);
            }
        });
        dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DianZanActivity.class);
                startActivity(intent);
            }
        });
        guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, GuanZhuActivity.class);
                startActivity(intent);
            }
        });
        layout46.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, Fs_ListActivity.class);
                startActivity(intent);
            }
        });
        vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //升级VIP
                AsyncHttp.post(U.URL + U.VIP, new RequestParams(), new ResponHandler() {
                    @Override
                    public void onSuccess(int index, String result) {
                        super.onSuccess(index, result);
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("type");
                            if (status.equals(U.SUCCESS)) {
                                JSONObject obj = json.getJSONObject("data");
                                pay(obj.getString("sn"), obj.getString("price"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * 加载个人信息
     */
    public void loadInfo() {
        RequestParams params = new RequestParams();
        final SharedUtils utils = new SharedUtils(activity, IUV.status);
        params.put("userId", utils.getStringValue("id"));
        params.put("appKey", utils.getStringValue("key"));
        AsyncHttp.post(U.URL + U.IUV_LOGIN_STATUS, params, new ResponHandler() {
            @Override
            public void onSuccess(int index, String result) {
                super.onSuccess(index, result);
                C.p(result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("type");
                    if (status.equals(U.SUCCESS)) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        LoginStatus login = new LoginStatus();
                        login.setDianzan(object.getString("dianzan"));
                        login.setName(object.getString("name"));
                        login.setFensi(object.getString("fans"));
                        login.setGuanzhu(object.getString("attention"));
                        login.setImg(object.getString("image"));
                        login.setRank(object.getString("rank"));
                        utils.setStringValue("dianzan", login.getDianzan());
                        utils.setStringValue("name", login.getName());
                        utils.setStringValue("fans", login.getFensi());
                        utils.setStringValue("attention", login.getGuanzhu());
                        utils.setStringValue("image", login.getImg());
                        utils.setStringValue("rank", login.getRank());
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = login;
                        content4_handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        System.out.println("..............." + isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            if (IUV.iuv.length() != 0) {


            }
            C.p("复原");
        } else {
            //相当于Fragment的onPause
            //
            C.p("不见");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        check();
        loadInfo();
    }

    //---------------升级VIP
    // 商户PID
    public static final String PARTNER = "2088121525973609";
    // 商户收款账号
    public static final String SELLER = "1661271387@qq.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAL7Lz2c6Y2xIMNuT\n" +
            "ItcadzZMnS5OMlsDEJso3Xzs7NFVEV09n60KR5xY3WcNzaQEwbGvFWt0goXH4+LV\n" +
            "xFjotZPKpUFP6t3YBQ8/Tj3ry9pIkPk3NOzqEnrnd/gRqZGf/ycteyQM5gTuEpH0\n" +
            "w0UTQG74K+vjxrlJsmtQFkXQeejnAgMBAAECgYAOZLGwx+bYNFn7No5aS9TSc9Un\n" +
            "uR5zXzfRfLO4yObUo+Y7cnqpQy1DzjBlpdTtmq5CCUNEZ/WwwfOfGSFG08dezYCZ\n" +
            "dQEOTrpD/ROI0gIRlhT8XksyLmVQ2pLDDMoBZHmwmWpa8KuAl92YBNc7gx95JD0q\n" +
            "dX2ND497Tb3xKOUkAQJBAOGdvNXinA8RUD02QifoeQHuRdqWtlMMe2QcaZOTmOkx\n" +
            "JlYpr95AYK/ond12VG6WkNp9JiUSe3uTN2mQ9Mfx3uECQQDYfaEu7L7lgUZ/ogi7\n" +
            "3jkWPlPquYdIwhoHuiBT/GICc2+yTEHnEHMwLHHkzVYCipQwaT9k0EcRaF/0IEyJ\n" +
            "cqjHAkEA3WqNUSbU2yC1q20dXccTQqbRnSU9h6F/Uw9jOXanoWsf4X2F4Esz2E8k\n" +
            "5pvjXqibPiGGFundj7g5sHuXDsa7YQJAfaYkp8yGW0P1u9w6f3dR4MQaBnDZwPPP\n" +
            "Ec8INlaUN+Hx4ST7VvWoNTyGegpnMiBmM9bSEVwE82iF0HXylg+zPwJBAIvmJzcH\n" +
            "OlzJIG4S2t44yK3tSpQxVjc7oLPz55TuJP3yxTXzMz1ADmAcRIszBCek8rYrVnx1\n" +
            "nWzoVB7/h/Wu64Q=";
    private static final int SDK_PAY_FLAG = 3;

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(String SN, String price) {

        String orderInfo = getOrderInfo(SN, "哎呦喂VIP升级", "VIP服务(一年)", "0.01");

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                content4_handler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String SN, String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + SN + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + U.URL + "/rest/app/payment/notify/async/" + SN + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        C.p(orderInfo);
        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

}
