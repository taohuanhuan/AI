package app.cn.aiyouv.www.adapter;


import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.bean.Article_List;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.ui.MainFragmentContent3;
import app.cn.aiyouv.www.utils.SharedUtils;

public class Content3Item0Adapter extends BaseAdapter{
    private Context context;
    private int WIDTH;
    private ArrayList<Article_List> lists;
    private MainFragmentContent3.Content3_Handler content3_handler;
    public Content3Item0Adapter(Context context, int WIDTH,ArrayList<Article_List> lists,MainFragmentContent3.Content3_Handler content3_handler) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.WIDTH = WIDTH;
        this.lists = lists;
        this.content3_handler = content3_handler;
    }
    public void putData(ArrayList<Article_List> lists){
        this.lists = lists;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lists.size();
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }
    private int process(int h,int w){
        double mix_f = (WIDTH/2) * (double) h / w;

      /*  AbsListView.LayoutParams paramsf = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                (int) mix_f);
        view.setLayoutParams(paramsf);*/
        return  (int)mix_f;
    }
    @Override
    public View getView(final int arg0, View convertView, ViewGroup arg2) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.content3_item, null);
           int height = process(210,320);
            viewHolder = new ViewHolder();
            viewHolder.item0_img = (ImageView) convertView
                    .findViewById(R.id.item0_img);
            viewHolder.item0_img.setLayoutParams(new FrameLayout.LayoutParams((WIDTH/2)-1,height-1));
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.dianzan = (CheckBox) convertView.findViewById(R.id.dianzan);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.item0_img.setText(contents.get(arg0).getApk());
        Picasso.with(context).load(U.URL+lists.get(arg0).getImg()).into(viewHolder.item0_img);
        viewHolder.title.setText(lists.get(arg0).getTitle());
        viewHolder.dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDianZan(arg0);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = lists.get(arg0);
                content3_handler.sendMessage(msg);
            }
        });
        return convertView;
    }

    /**
     * 执行点赞功能
     */
    private void loadDianZan(final int i){
        if(IUV.iuv.length()!=0){
            RequestParams params = new RequestParams();
            SharedUtils utils = new SharedUtils(context, IUV.status);
            params.put("userId",utils.getStringValue("id"));
            params.put("appKey",utils.getStringValue("key"));
            params.put("artclieId",lists.get(i).getId());
            AsyncHttp.post(U.URL+U.IUV_DIANZAN,params,new ResponHandler(){
                @Override
                public void onSuccess(int index, String result) {
                    super.onSuccess(index, result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("type");
                        if(status.equals(U.SUCCESS)){
                            //点赞成功
                            Message msg = new Message();
                            msg.what = 10;
                            lists.get(i).setIsDian(true);
                            msg.obj = lists;
                            content3_handler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }else {
            //-11作为统一的需要登录状态
            content3_handler.sendEmptyMessage(-11);
        }


    }
    public static class ViewHolder {
        public ImageView item0_img;
        public TextView title;
        public CheckBox dianzan;
    }
}
