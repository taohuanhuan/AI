package app.cn.aiyouv.www.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.adapter.MessageAdapter;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.bean.Article_List;
import app.cn.aiyouv.www.bean.Fs_Bean;
import app.cn.aiyouv.www.bean.Message;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.common.Common;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.utils.SharedUtils;
import app.cn.aiyouv.www.widget.NewDataToast;
import app.cn.aiyouv.www.widget.msg.MsgListView;

/**
 * Created by Administrator on 2016/3/23.
 */
public class Chat_Activity extends BaseActivity {
    private TextView chat_title;
    private MsgListView msg_listView;
    private EditText msg_et;
    private InputMethodManager imm;
    private MessageAdapter adapter;
    private ArrayList<Message> list_Messages;
    private Fs_Bean fs_bean;
    private View faceView;
    private ImageView btn_face;
    private TextView send_btn;
    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(android.os.Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
//                    List<Message> messages = (ArrayList<Message>) msg.obj;
                    Set<Integer> set = lists.keySet();
                    Iterator iterator = set.iterator();
                    ArrayList<Message> msgs = new ArrayList<Message>();

                    while (iterator.hasNext()) {
                        msgs.add(lists.get(iterator.next()));
                    }

//                    Collections.reverse(msgs);
                    adapter.setMessage(msgs);
                    int position = adapter.getCount();
                    msg_listView.setSelection(adapter.getCount() - 1);

                    break;
                case 2:
                    recData();
                    msg_et.setText("");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main);
        findViewById(R.id.title_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                AppManager.getAppManager().finishActivity();
            }
        });
        chat_title = (TextView) findViewById(R.id.chat_title);
        fs_bean = (Fs_Bean) getIntent().getSerializableExtra("obj");
        msg_et = (EditText) findViewById(R.id.msg_et);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(msg_et.getWindowToken(), 0);
        faceView = findViewById(R.id.ll_facechoose);
        btn_face = (ImageView) findViewById(R.id.btn_face);
        chat_title.setText(getString(R.string.chat_title, fs_bean.getName()));
        send_btn = (TextView) findViewById(R.id.send_btn);
        msg_listView = (MsgListView) findViewById(R.id.msg_listView);
        list_Messages = new ArrayList<Message>();
        adapter = new MessageAdapter(getApplicationContext(), list_Messages, handler, null, false);
        //---------------
        msg_listView.setOnTouchListener(touchListener);
        msg_listView.setPullLoadEnable(false);
        msg_listView.setXListViewListener(ixListViewListener);
        msg_listView.setAdapter(adapter);
        msg_listView.setSelection(adapter.getCount() - 1);
        msg_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    send_btn.setEnabled(true);
                } else {
                    send_btn.setEnabled(false);
                }
            }
        });
        msg_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (faceView.getVisibility() == View.VISIBLE) {
                    faceView.setVisibility(View.GONE);
                }
            }
        });
        msg_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (faceView.getVisibility() == View.VISIBLE) {
                    faceView.setVisibility(View.GONE);
                }

            }
        });
        btn_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (faceView.getVisibility() == View.VISIBLE) {
                    faceView.setVisibility(View.GONE);
                } else {
                    if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
                        imm.hideSoftInputFromWindow(msg_et.getWindowToken(), 0);
                    }
                    faceView.setVisibility(View.VISIBLE);
                }
            }
        });


        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestParams params = new RequestParams();
                params.put("id", fs_bean.getId());
                SharedUtils utils = new SharedUtils(getApplicationContext(), IUV.status);
                params.put("userId", utils.getStringValue("id"));
                params.put("appKey", utils.getStringValue("key"));
                params.put("content", msg_et.getText().toString());
                AsyncHttp.post(U.URL + U.IUV_RECY_, params, new ResponHandler() {
                    @Override
                    public void onSuccess(int index, String result) {
                        super.onSuccess(index, result);
                        C.p(result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("type");
                            if (status.equals(U.SUCCESS)) {
                                handler.sendEmptyMessage(2);//发送成功
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
        });
        loadData();
    }

    private int CURRENT_PAGE = 1;
    private int TOTAL_PAGER = -1;
    private Map<Integer, Message> lists = new TreeMap<Integer, Message>();

    private void loadData() {
        RequestParams params = new RequestParams();
        params.put("id", fs_bean.getId());
        final SharedUtils utils = new SharedUtils(getApplicationContext(), IUV.status);
        params.put("userId", utils.getStringValue("id"));
        params.put("appKey", utils.getStringValue("key"));
        params.put("pageNumber", String.valueOf(CURRENT_PAGE));
        params.put("pageSize", Common.VIEW_NUM);
        AsyncHttp.post(U.URL + U.IUV_TALK_HIS, params, new ResponHandler() {
            @Override
            public void onSuccess(int index, String result) {
                super.onSuccess(index, result);
                C.p(result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("type");

                    if (status.equals(U.SUCCESS)) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        JSONArray array = object.getJSONArray("content");
                        TOTAL_PAGER = object.getInt("totalPages");
                        CURRENT_PAGE = object.getInt("pageNumber");
                        int len = array.length();

                        for (int i = 0; i < len; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            Message msg = new Message();
                            if (utils.getStringValue("id").equals(obj.getString("userId"))) {
                                msg.setIsMine(true);
                            } else {
                                msg.setIsMine(false);
                            }
                            msg.setContent(obj.getString("content"));
                            msg.setTime(obj.getLong("createDate"));
                            msg.setImg(obj.getString("userImage"));
                            msg.setUserId(obj.getString("userId"));
                            int id = obj.getInt("id");
                            if (!lists.containsKey(id)) {
                                lists.put(id, msg);
                            }
                        }

                        handler.sendEmptyMessage(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        ref();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void recData() {
        RequestParams params = new RequestParams();
        params.put("id", fs_bean.getId());
        final SharedUtils utils = new SharedUtils(getApplicationContext(), IUV.status);
        params.put("userId", utils.getStringValue("id"));
        params.put("appKey", utils.getStringValue("key"));
        params.put("pageNumber", String.valueOf(1));
        params.put("pageSize", Common.VIEW_NUM);
        AsyncHttp.post(U.URL + U.IUV_TALK_HIS, params, new ResponHandler() {
            @Override
            public void onSuccess(int index, String result) {
                super.onSuccess(index, result);
                C.p(result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("type");

                    if (status.equals(U.SUCCESS)) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        JSONArray array = object.getJSONArray("content");
                        TOTAL_PAGER = object.getInt("totalPages");
                        CURRENT_PAGE = object.getInt("pageNumber");
                        int len = array.length();

                        for (int i = 0; i < len; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            Message msg = new Message();
                            if (utils.getStringValue("id").equals(obj.getString("userId"))) {
                                msg.setIsMine(true);
                            } else {
                                msg.setIsMine(false);
                            }
                            msg.setContent(obj.getString("content"));
                            msg.setTime(obj.getLong("createDate"));
                            msg.setImg(obj.getString("userImage"));
                            msg.setUserId(obj.getString("userId"));
                            int id = obj.getInt("id");
                            if (!lists.containsKey(id)) {
                                lists.put(id, msg);
                            }
                        }

                        handler.sendEmptyMessage(1);
                    } else if (U.UNLOGIN.equals(status)) {
                        NewDataToast.makeText(getApplicationContext(), jsonObject.getString("content")).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * load new data about history of message
     */
    @Override
    protected void onResume() {
        super.onResume();

    }

    private void load() {
        int SIZE = lists.size();
        int VIEW = Integer.parseInt(Common.VIEW_NUM);
        CURRENT_PAGE = SIZE % VIEW == 0 ? (SIZE / VIEW) + 1 : SIZE / VIEW;
//            CURRENT_PAGE=CURRENT_PAGE+1;
        loadData();
    }

    private CountDownTimer countDownTimer;

    private void ref() {
        countDownTimer = new CountDownTimer(5000, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                recData();
                ref();
            }
        };
        countDownTimer.start();
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            // TODO Auto-generated method stub
            switch (view.getId()) {
                case R.id.msg_et:
                    imm.showSoftInput(msg_et, 0);
                    break;
                case R.id.msg_listView:
                    imm.hideSoftInputFromWindow(msg_et.getWindowToken(), 0);
                    break;


                default:
                    break;
            }
            return false;
        }
    };
    MsgListView.IXListViewListener ixListViewListener = new MsgListView.IXListViewListener() {

        @Override
        public void onRefresh() {
            load();
            msg_listView.stopRefresh();
                /*if(CURRENT_PAGE>TOTAL_PAGER){
                    NewDataToast.makeText(getApplicationContext(), "没有数据了").show();

                }else{

                }*/

        }

        @Override
        public void onLoadMore() {

        }
    };

}
