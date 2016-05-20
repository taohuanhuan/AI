package app.cn.aiyouv.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.adapter.ImagesAdapter;
import app.cn.aiyouv.www.adapter.ReplyAdapter;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.bean.Article;
import app.cn.aiyouv.www.bean.DetailPics;
import app.cn.aiyouv.www.bean.ReplyBean;
import app.cn.aiyouv.www.bean.VuforiaOk;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.common.Common;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.inter.Tick;
import app.cn.aiyouv.www.utils.SharedUtils;
import app.cn.aiyouv.www.utils.TimeUtil;
import app.cn.aiyouv.www.widget.CommonNavPop;
import app.cn.aiyouv.www.widget.CommonRelyPop;
import app.cn.aiyouv.www.widget.CommonRewardPop;
import app.cn.aiyouv.www.widget.CommonSRewardPop;
import app.cn.aiyouv.www.widget.CommonSharedPop;
import app.cn.aiyouv.www.widget.InScrollEListView;
import app.cn.aiyouv.www.widget.InScrollListView;
import app.cn.aiyouv.www.widget.NewDataToast;

/**
 * Created by Administrator on 2016/1/25.
 */
public class ArticleDetailActivity extends BaseActivity {
    private WebView commonView;
    private ReplyAdapter replyAdapter;
    private TextView title,time,reading;
    private ImageView dianzan;
    private TextView cate;
    private Button bottom0,bottom1,bottom2,bottom3,bottom4;
    private LocationClient mLocationClient;
    private LinearLayout layout3,layout4;
    private InScrollEListView reply_list;
    private RelativeLayout more;
    private InScrollListView pics_list;
    private ArrayList<ReplyBean> replys;
    private ArticleDetail_Handler articleDetail_handler;
    private int CURRENT_PAGE = 1;
    private int TOTAL_PAGER = -1;
    private Tick tick = new Tick() {
        @Override
        public void select(int position) {
            CommonRelyPop pop = new CommonRelyPop();
            pop.showSheet(ArticleDetailActivity.this,String.valueOf(position),getIntent().getStringExtra("acticle_id"),ok);
        }
    };
    private Tick ok = new Tick() {
        @Override
        public void select(int position) {
            loadReplyList(true);
        }
    };
    private void loadReplyList(final boolean reload){
        RequestParams params = new RequestParams();
        params.put("articleId",getIntent().getStringExtra("acticle_id"));
        params.put("pageNumber",String.valueOf(CURRENT_PAGE));
        params.put("pageSize", Common.VIEW_NUM);

        AsyncHttp.post(U.URL+U.RELIY_LIST,params,new ResponHandler(){
            @Override
            public void onSuccess(int index, String result) {
                super.onSuccess(index, result);
                try {
                    if(reload){
                        replys.clear();
                        // replys = new ArrayList<ReplyBean>();
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("type");
                    if(status.equals(U.SUCCESS)){
                        JSONObject json = jsonObject.getJSONObject("data");
                        TOTAL_PAGER = json.getInt("totalPages");
                        CURRENT_PAGE = json.getInt("pageNumber");
                        JSONArray jsonArray = json.getJSONArray("content");
                        int len  = jsonArray.length();
                        C.p("文章"+len);
                        for(int i=0;i<len;i++){
                            JSONObject obj = jsonArray.getJSONObject(i);
                            ReplyBean bean = new ReplyBean();
                            bean.setTime(TimeUtil.getTime(obj.getLong("createDate")));
                            bean.setIco(obj.getString("memberImage"));
                            bean.setName(obj.getString("memberName"));
                            bean.setContent(obj.getString("content"));
                            bean.setReplyId(obj.getString("id"));
                            JSONArray array = obj.getJSONArray("replyReview");
                            int jen = array.length();
                            ArrayList<ReplyBean> rps = new ArrayList<ReplyBean>();
                            for(int j=0;j<jen;j++){
                                JSONObject o = array.getJSONObject(j);
                                ReplyBean b = new ReplyBean();
                                b.setTime(TimeUtil.getTime(o.getLong("createDate")));
                                b.setIco(o.getString("memberImage"));
                                b.setName(o.getString("memberName"));
                                b.setContent(o.getString("content"));
                                b.setReplyId(o.getString("id"));
                                rps.add(b);
                            }
                            bean.setReplyBeans(rps);
                            replys.add(bean);
                        }
                        handler.sendEmptyMessage(-1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail_activity);
        bottom0 = (Button) findViewById(R.id.bottom0);
        bottom1 = (Button) findViewById(R.id.bottom1);
        bottom2 = (Button) findViewById(R.id.bottom2);
        bottom3 = (Button) findViewById(R.id.bottom3);
        bottom4 = (Button) findViewById(R.id.bottom4);
        more = (RelativeLayout) findViewById(R.id.more);
        pics_list = (InScrollListView) findViewById(R.id.pics_list);
        replys = new ArrayList<ReplyBean>();


        replyAdapter = new ReplyAdapter(ArticleDetailActivity.this,replys,tick);
        reply_list = (InScrollEListView) findViewById(R.id.reply_list);
        reply_list.setAdapter(replyAdapter);
        title = (TextView) findViewById(R.id.title);
        time = (TextView) findViewById(R.id.time);
        cate = (TextView) findViewById(R.id.cate);
        reading = (TextView) findViewById(R.id.reading);
        dianzan = (ImageView) findViewById(R.id.dianzan);
        layout3 = (LinearLayout) findViewById(R.id.layout3);
        layout4 = (LinearLayout) findViewById(R.id.layout4);
        if(getIntent().hasExtra("tip")){
            SharedUtils utils = new SharedUtils(ArticleDetailActivity.this, IUV.status);
            if(getIntent().getStringExtra("tip").equals("YC")){
                if(utils.getStringValue("loginType").length()!=0){
                    //存在登录才进行
                    layout3.setVisibility(View.VISIBLE);
                    layout4.setVisibility(View.GONE);
                }
            }else if(getIntent().getStringExtra("tip").equals("DJX")){
                layout3.setVisibility(View.GONE);
                layout4.setVisibility(View.VISIBLE);
            }
        }
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //加载更多数据
                CURRENT_PAGE = CURRENT_PAGE + 1;
                if (CURRENT_PAGE > TOTAL_PAGER) {
                    handler.sendEmptyMessage(-1);
                    NewDataToast.makeText(ArticleDetailActivity.this, "没有数据了").show();
                } else {
                    loadReplyList(false);
                }
            }
        });
        init();
        cate.setText(getIntent().getStringExtra("title"));
        articleDetail_handler = new ArticleDetail_Handler(ArticleDetailActivity.this);
        loadData();
        bottom0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonRelyPop pop = new CommonRelyPop();
                pop.showSheet(ArticleDetailActivity.this, "", getIntent().getStringExtra("acticle_id"),ok);

            }
        });
        bottom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonSharedPop.showSheet(ArticleDetailActivity.this, getIntent().getStringExtra("acticle_id"));
            }
        });
        bottom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadGz();
            }
        });
        dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDianZan();
            }
        });
        loadReplyList(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void routeplanToNavi(BNRoutePlanNode.CoordinateType coType,Article ok,double startLng,double startLat) {
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        sNode = new BNRoutePlanNode(startLng,startLat,
                "", null, coType);
        eNode = new BNRoutePlanNode(ok.getLongitude(),ok.getLatitude(),
                "", null, coType);
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new ArticleDetailPlanListener(sNode));
        }
    }
    private class ArticleDetailPlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;
        public ArticleDetailPlanListener(BNRoutePlanNode node){
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            Intent intent = new Intent(ArticleDetailActivity.this, BNGuideActivity.class);
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

    private Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case -1:
                    replyAdapter.putData(replys);
                    for (int i=0; i<replys.size(); i++) {
                        reply_list.expandGroup(i);
                    };
                    break;
                case 0:
                    NewDataToast.makeText(getApplicationContext(),"关注失败").show();
                    break;
                case 1:
                    NewDataToast.makeText(getApplicationContext(),"关注成功").show();
                    break;
                case 2:
                    NewDataToast.makeText(getApplicationContext(),"点赞成功").show();
                    break;
                case 3:
                    NewDataToast.makeText(getApplicationContext(),"点赞失败").show();
                    break;
            }
        }
    };
    private void loadDianZan() {
        if (IUV.iuv.length() != 0) {
            RequestParams params = new RequestParams();
            SharedUtils utils = new SharedUtils(getApplicationContext(), IUV.status);
            params.put("userId", utils.getStringValue("id"));
            params.put("appKey", utils.getStringValue("key"));
            params.put("artclieId",getIntent().getStringExtra("acticle_id"));
            AsyncHttp.post(U.URL + U.IUV_DIANZAN, params, new ResponHandler() {
                @Override
                public void onSuccess(int index, String result) {
                    super.onSuccess(index, result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("type");
                        if (status.equals(U.SUCCESS)) {
                            //点赞成功
                            handler.sendEmptyMessage(2);
                        }else{
                            handler.sendEmptyMessage(3);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } else {
            //-11作为统一的需要登录状态
            NewDataToast.makeText(getApplicationContext(), "请先登录").show();
        }
    }
    private void loadGz(){
        if(IUV.iuv.length()!=0){
            RequestParams params = new RequestParams();
            SharedUtils utils = new SharedUtils(getApplicationContext(), IUV.status);
            params.put("userId",utils.getStringValue("id"));
            params.put("appKey",utils.getStringValue("key"));
            params.put("artclieId",getIntent().getStringExtra("acticle_id"));
            AsyncHttp.post(U.URL+U.IUV_GUANZHU,params,new ResponHandler(){
                @Override
                public void onSuccess(int index, String result) {
                    super.onSuccess(index, result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("type");
                        if(status.equals(U.SUCCESS)){
                            //点赞成功
                            handler.sendEmptyMessage(1);
                        }else{
                            handler.sendEmptyMessage(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }else{
            NewDataToast.makeText(getApplicationContext(),"请先登录").show();
        }
    }
    private String format(long date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String d = format.format(date);

        return	d;

    }
    public class ArticleDetail_Handler extends Handler {
        WeakReference<ArticleDetailActivity> mLeakActivityRef;

        public ArticleDetail_Handler(ArticleDetailActivity leakActivity) {
            mLeakActivityRef = new WeakReference<ArticleDetailActivity>(leakActivity);
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if(mLeakActivityRef.get()!=null){
                switch (msg.what){
                    case 1:
                        final Article article = (Article) msg.obj;
                        ImagesAdapter adapter = new ImagesAdapter(ArticleDetailActivity.this,article.getDetailPicses());
                        pics_list.setAdapter(adapter);
                        title.setText(article.getTitle());
                        long t = Long.parseLong(article.getTime());
                        reading.setText("阅读:"+article.getHits());
                        time.setText(format(t));
//                        setArticleView(article.getContent());
                        Document doc_Dis = Jsoup.parse(article.getContent());
                        Elements ele_Img = doc_Dis.getElementsByTag("img");
                        if (ele_Img.size() != 0) {
                            for (Element e_Img : ele_Img) {
                                e_Img.attr("style", "width:100%");
                            }
                        }
                        String newHtmlContent = doc_Dis.toString();
                        commonView.loadDataWithBaseURL("", newHtmlContent, "text/html", "UTF-8", null);
                        bottom3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedUtils utils = new SharedUtils(ArticleDetailActivity.this, IUV.status);
                                if(utils.getStringValue("loginType").equals(U.M)){
                                    //商家的打赏界面
                                    CommonRewardPop pop = new CommonRewardPop(ArticleDetailActivity.this);
                                    pop.showSheet(article.getId());
                                }else if(utils.getStringValue("loginType").equals(U.S)){

                                    CommonSRewardPop sRewardPop = new CommonSRewardPop(ArticleDetailActivity.this);
                                    sRewardPop.showSheet(article.getId());

                                }



                            }
                        });
                        bottom4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CommonNavPop.showSheet(ArticleDetailActivity.this, new CommonNavPop.onSelect() {
                                    @Override
                                    public void onClick(Object object) {
                                        //开始导航
                                        LocationClientOption op = new LocationClientOption();
                                        op.setIsNeedLocationDescribe(true);
                                        mLocationClient = new LocationClient(ArticleDetailActivity.this);
                                        mLocationClient.setLocOption(op);
                                        mLocationClient.registerLocationListener(new BDLocationListener() {

                                            @Override
                                            public void onReceiveLocation(BDLocation location) {
                                                // TODO Auto-generated method stub
                                                double lat = location.getLatitude();
                                                double lng = location.getLongitude();
                                                C.p(lat + "--" + lng);
                                                if (BaiduNaviManager.isNaviInited()) {
                                                    routeplanToNavi(BNRoutePlanNode.CoordinateType.WGS84, article, lng, lat);
                                                }
                                            }
                                        });
                                        mLocationClient.start();
                                    }
                                }, null, null);
                            }
                        });
                        break;
                    default:

                        break;
                }
            }
        }
    }
    private void init(){
        findViewById(R.id.title_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getAppManager().finishActivity();
            }
        });
        commonView = (WebView) findViewById(R.id.article_web);
        commonView.getSettings().setBuiltInZoomControls(false);
        commonView.getSettings().setSupportZoom(true);
        commonView.getSettings().setJavaScriptEnabled(true);
        commonView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        WebViewClient webViewClient = new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub

                commonView.loadUrl(url);

                return true;
            }
        };
        WebChromeClient webChromeClient = new WebChromeClient(){
            public void onReceivedTitle(WebView view, String tl) {

            };
        };
        commonView.setWebChromeClient(webChromeClient);
        commonView.setWebViewClient(webViewClient);
        commonView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        commonView.getSettings().setLoadWithOverviewMode(true);
//			tv_detail.loadData(details, "text/html", "UTF-8");


    }
    private void setArticleView(String content){
        Document doc_Dis = Jsoup.parse(content);
        Elements ele_Img = doc_Dis.getElementsByTag("img");
        if (ele_Img.size() != 0) {
            for (Element e_Img : ele_Img) {
                e_Img.attr("style", "width:100%");
            }
        }
        String newHtmlContent = doc_Dis.toString();
        commonView.loadDataWithBaseURL("", newHtmlContent, "text/html", "UTF-8", null);
    }
    /**
     * 加载数据
     */
    private void loadData(){
        RequestParams params = new RequestParams();
        params.put("id",getIntent().getStringExtra("acticle_id"));
        AsyncHttp.post(U.URL+U.ARTICLE_DETAIL,params,new ResponHandler(ArticleDetailActivity.this){
            @Override
            public void onSuccess(int i, String s) {
                super.onSuccess(i, s);
                C.p(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("type");
                    if(status.equals(U.SUCCESS)){
                        Article article = new Article();
                        JSONObject obj = jsonObject.getJSONObject("data");
                        article.setId(obj.getString("id"));
                        article.setTitle(obj.getString("title"));
                        article.setAuthor(obj.getString("author"));
                        article.setContent(obj.getString("content"));
                        article.setTime(obj.getString("createDate"));
                        article.setHits(obj.getString("hits"));
                        article.setLatitude(obj.getDouble("latitude"));
                        article.setLongitude(obj.getDouble("longitude"));

                        JSONArray array = obj.getJSONArray("articleImages");
                        int len = array.length();
                        ArrayList<DetailPics> detailPicses = new ArrayList<DetailPics>();
                        for(int j=0;j<len;j++){
                            JSONObject pic = array.getJSONObject(j);
                            DetailPics pics = new DetailPics();
                            pics.setPic(pic.getString("source"));
                            detailPicses.add(pics);
                        }
                        article.setDetailPicses(detailPicses);
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = article;
                        articleDetail_handler.sendMessage(msg);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
