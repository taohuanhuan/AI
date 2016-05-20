package app.cn.aiyouv.www.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.adapter.Content3Item0Adapter;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.bean.Advert;
import app.cn.aiyouv.www.bean.Article_List;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.common.Common;
import app.cn.aiyouv.www.common.ConstantValues;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.inter.Tick;
import app.cn.aiyouv.www.utils.SharedUtils;
import app.cn.aiyouv.www.widget.CommonCatePop;
import app.cn.aiyouv.www.widget.GridViewWithHeaderAndFooter;
import app.cn.aiyouv.www.widget.NewDataToast;
import app.cn.aiyouv.www.widget.PullToRefreshView;
import app.cn.aiyouv.www.widget.imageslider.Animations.DescriptionAnimation;
import app.cn.aiyouv.www.widget.imageslider.Indicators.PagerIndicator;
import app.cn.aiyouv.www.widget.imageslider.SliderLayout;
import app.cn.aiyouv.www.widget.imageslider.SliderTypes.BaseSliderView;
import app.cn.aiyouv.www.widget.imageslider.SliderTypes.TextSliderView;


public class MainFragmentContent3 extends Fragment implements View.OnClickListener {
    private GridViewWithHeaderAndFooter content3_list;
    private PullToRefreshView refresh;
    private Content3Item0Adapter content3Item0Adapter;
    private int SCREEN_WIDTH;
    private SliderLayout mDemoSlider;
    private View silder_layout;
    private LayoutInflater mInflater;
    private Content3_Handler content3_handler;
    private View right_btn, left_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_content_layout3, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        content3_list = (GridViewWithHeaderAndFooter) view.findViewById(R.id.content3_list);
        refresh = (PullToRefreshView) view.findViewById(R.id.refresh_layout);
        right_btn = view.findViewById(R.id.right_btn);
        left_btn = view.findViewById(R.id.left_btn);
        right_btn.setOnClickListener(this);
        left_btn.setOnClickListener(this);
        refresh.setOnHeaderRefreshListener(listHeadListener);
        refresh.setOnFooterRefreshListener(listFootListener);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        SCREEN_WIDTH = dm.widthPixels;
        content3_handler = new Content3_Handler(MainFragmentContent3.this);
    }

    /**
     * 顶部菜单栏 扫图和菜单栏的具体点击事件
     */
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.left_btn:
                Intent i = new Intent(getActivity(), Vuforia_Qr_Activity.class);
                startActivity(i);
                AppManager.getAppManager().finishActivity();
                break;

            case R.id.right_btn:
                CommonCatePop.showSheet(getActivity(), new CommonCatePop.onSelect() {
                    @Override
                    public void onClick(Object object) {

                    }
                }, null, null, ConstantValues.CATE0, tick);
                break;
        }
    }
    /**
     * 顶部下拉刷新状态
     */
    private PullToRefreshView.OnHeaderRefreshListener listHeadListener = new PullToRefreshView.OnHeaderRefreshListener() {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {
            // TODO Auto-generated method stub
            refresh.postDelayed(new Runnable() {

                @Override
                public void run() {

                    CURRENT_PAGE = 1;
                    loadList(false);
                }
            }, 1000);
        }
    };
    /**
     *
     */
    private PullToRefreshView.OnFooterRefreshListener listFootListener = new PullToRefreshView.OnFooterRefreshListener() {

        @Override
        public void onFooterRefresh(PullToRefreshView view) {
            // TODO Auto-generated method stub
            refresh.postDelayed(new Runnable() {

                @Override
                public void run() {

                    CURRENT_PAGE = CURRENT_PAGE + 1;
                    if (CURRENT_PAGE > TOTAL_PAGER) {
                        content3_handler.sendEmptyMessage(2);
                        NewDataToast.makeText(getActivity(), "没有数据了").show();
                    } else {
                        loadList(true);
                    }
                }
            }, 1000);
        }
    };
    private BaseSliderView.OnSliderClickListener sliderClickListener = new BaseSliderView.OnSliderClickListener() {
        @Override
        public void onSliderClick(BaseSliderView slider) {
            Intent intent = new Intent(getActivity(), CommonWeb.class);
            intent.putExtra("title", slider.getTile());
            intent.putExtra("url", slider.getPath());
            startActivity(intent);
        }
    };


    public class Content3_Handler extends Handler {
        WeakReference<MainFragmentContent3> mLeakActivityRef;

        public Content3_Handler(MainFragmentContent3 leakActivity) {
            mLeakActivityRef = new WeakReference<MainFragmentContent3>(leakActivity);
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (mLeakActivityRef.get() != null) {
                switch (msg.what) {
                    case 10:
                        NewDataToast.makeText(getActivity(), "点赞成功").show();
                        ArrayList<Article_List> dlists = (ArrayList<Article_List>) msg.obj;
                        content3Item0Adapter.putData(dlists);
                        content3Item0Adapter.notifyDataSetChanged();
                        break;
                    case -11:
                        NewDataToast.makeText(getActivity(), "请先登录").show();
                        break;
                    case 1:
                        Article_List articleList = (Article_List) msg.obj;
                        Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                        intent.putExtra("acticle_id", articleList.getId());
                        intent.putExtra("title", articleList.getTitle());
                        intent.putExtra("tip", "DJX");
                        startActivity(intent);
                        break;
                    case 2:
                        refresh.onHeaderRefreshComplete();
                        refresh.onFooterRefreshComplete();
                        content3Item0Adapter.putData(lists);
                        content3Item0Adapter.notifyDataSetChanged();
                        break;
                    case 3:
                        onLoad();
                        break;
                    case 0:
                        ArrayList<Advert> adverts = (ArrayList<Advert>) msg.obj;
                        for (int i = 0; i < adverts.size(); i++) {
                            TextSliderView textSliderView = new TextSliderView(getActivity());
                            textSliderView
                                    .description(adverts.get(i).getContent())
                                    .image(U.URL + adverts.get(i).getPath()).setTitle(adverts.get(i).getTitle()).setPath(adverts.get(i).getUrl())
                                    .setOnSliderClickListener(sliderClickListener);
                            //添加要传递的数据
                            textSliderView.getBundle()
                                    .putString("extra", String.valueOf(i));
                            mDemoSlider.addSlider(textSliderView);

                        }

                        setSlider();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private int CURRENT_PAGE = 1;
    private int TOTAL_PAGER = -1;
    private ArrayList<Article_List> lists;

    private void loadList(final boolean isContinue) {
        String cate = ConstantValues.CONTENT2;
        RequestParams params = new RequestParams();
        params.put("pageNumber", String.valueOf(CURRENT_PAGE));
        params.put("pageSize", Common.VIEW_NUM);
        final SharedUtils utils = new SharedUtils(getActivity(), IUV.cate);
        if (utils.getStringValue("cate_tag").length() != 0) {
            params.put("orders[0].property", utils.getStringValue("cate_tag"));
        }
        C.p("------------------------" + utils.getStringValue("cate_tag"));
        AsyncHttp.post(U.URL + U.IUV_LIST + cate, params, new ResponHandler() {
            @Override
            public void onStart() {
                super.onStart();
                C.p("开始");
            }

            @Override
            public void onSuccess(int j, String s) {
                super.onSuccess(j, s);
                C.p("返回" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("type");
                    if (status.equals(U.SUCCESS)) {
                        //成功
                        JSONObject obj = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = obj.getJSONArray("content");
                        TOTAL_PAGER = obj.getInt("totalPages");
                        CURRENT_PAGE = obj.getInt("pageNumber");
                        int len = jsonArray.length();
                        if (!isContinue) {
                            lists.clear();
//                            lists = new ArrayList<Article_List>();
                        }
                        C.p("列表" + len);
                        for (int i = 0; i < len; i++) {
                            JSONObject o = jsonArray.getJSONObject(i);
                            Article_List list = new Article_List();
                            list.setImg(o.getString("image"));
                            list.setTitle(o.getString("title"));
                            list.setId(o.getString("id"));
                            list.setIntro(o.getString("intro"));
                            list.setIsDian(o.getBoolean("isRecommend"));
                            lists.add(list);
                        }

                        content3_handler.sendEmptyMessage(2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadData() {
        RequestParams params = new RequestParams();
        params.put("flag", ConstantValues.DRX);

        AsyncHttp.post(U.URL + U.ADVERT, params, new ResponHandler(getActivity()) {
            @Override
            public void onSuccess(int j, String s) {
                super.onSuccess(j, s);
                C.p("广告" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("type");
                    if (status.equals(U.SUCCESS)) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        int len = jsonArray.length();
                        ArrayList<Advert> adverts = new ArrayList<Advert>();
                        for (int i = 0; i < len; i++) {
                            Advert advert = new Advert();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            advert.setPath(obj.getString("path"));
                            advert.setUrl(obj.getString("url"));
                            advert.setTitle(obj.getString("title"));
                            advert.setContent(obj.getString("content"));
                            advert.setId(obj.getString("id"));
                            adverts.add(advert);
                        }
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = adverts;
                        content3_handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setSlider() {
       /* HashMap<String,String> file_maps = new HashMap<String, String>();
        file_maps.put("世界杯-A","http://www.aiyouv.cn/data/upload/564be93b77a74.png");
        file_maps.put("世界杯-A","http://www.aiyouv.cn/data/upload/564be93b77a74.png");



        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // 初始化幻灯片页面
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setOnSliderClickListener(sliderClickListener);

            //添加要传递的数据
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }*/


//      幻灯片切换方式
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
//      指示符位置
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//      定义指示器样式
//      mDemoSlider.setCustomIndicator(your view);
//      幻灯片循环
//      mDemoSlider.startAutoCycle();
//      停止循环
        mDemoSlider.stopAutoCycle();
//      设置指示器的显示与否
        mDemoSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
//      设置幻灯片的转化时间
//      mDemoSlider.setSliderTransformDuration(5000, null);
//      用来自定义幻灯片标题的显示方式
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
//      幻灯片切换时间
        mDemoSlider.setDuration(3000);

    }

    private void process(View view, int h, int w) {
        double mix_f = SCREEN_WIDTH * (double) h / w;
        FrameLayout.LayoutParams paramsf = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) mix_f);
        view.setLayoutParams(paramsf);
    }

    private Tick tick = new Tick() {
        @Override
        public void select(int position) {
            loadList(false);
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mInflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonCatePop.showSheet(getActivity(), new CommonCatePop.onSelect() {
                    @Override
                    public void onClick(Object object) {

                    }
                }, null, null, ConstantValues.CATE2, tick);

            }
        });
        silder_layout = mInflater.inflate(R.layout.common_slider3_layout_, null);
        mDemoSlider = (SliderLayout) silder_layout.findViewById(R.id.slider);
        content3_list.addHeaderView(silder_layout);
        process(silder_layout, 404, 640);
        // process(silder_layout, 200, 630);
        loadData();
//        loadData();
        lists = new ArrayList<Article_List>();
        content3Item0Adapter = new Content3Item0Adapter(getActivity(), SCREEN_WIDTH, lists, content3_handler);
        content3_list.setAdapter(content3Item0Adapter);
//        content3_list.setPullRefreshEnable(true);
//        content3_list.setPullLoadEnable(true);
//        content3_list.setXListViewListener(listener);

        loadList(false);
    }

    private void onLoad() {
//        content3_list.stopRefresh();
//        content3_list.stopLoadMore();
//        content3_list.setRefreshTime("刚刚");
    }
}
