package app.cn.aiyouv.www.adapter;



import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.bean.Article_List;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.ui.MainFragmentContent0;
import app.cn.aiyouv.www.utils.Distance;
import app.cn.aiyouv.www.utils.SharedUtils;

public class Content0Item0Adapter extends BaseAdapter{
    private Context context;
    private int WIDTH;
    private ArrayList<Article_List> lists;
    private MainFragmentContent0.Content0_Handler content0_handler;
    private SharedUtils utils;
    public Content0Item0Adapter(Context context,int WIDTH,MainFragmentContent0.Content0_Handler content0_handler,ArrayList<Article_List> lists) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.WIDTH = WIDTH;
        this.content0_handler = content0_handler;
        this.lists = lists;
        utils = new SharedUtils(context, IUV.local);
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
        return lists.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }
    private void process(View view,int h,int w){
        double mix_f = WIDTH * (double) h / w;
        AbsListView.LayoutParams paramsf = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                (int) mix_f);
        view.setLayoutParams(paramsf);
    }
    @Override
    public View getView(final int arg0, View convertView, ViewGroup arg2) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.content0_item, null);
            process(convertView,280,638);
            viewHolder = new ViewHolder();
            viewHolder.item0_img = (ImageView) convertView
                    .findViewById(R.id.item0_img);
     //       viewHolder.intro = (TextView) convertView.findViewById(R.id.intro);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.dis = (TextView) convertView.findViewById(R.id.dis);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.item0_img.setText(contents.get(arg0).getApk());
        Picasso.with(context).load(U.URL+lists.get(arg0).getImg()).into(viewHolder.item0_img);
        double lat = Double.parseDouble(utils.getStringValue("lat").length()==0?"0":utils.getStringValue("lat"));
        double lng = Double.parseDouble(utils.getStringValue("lng").length()==0?"0":utils.getStringValue("lng"));
        double at = Distance.GetDistance(lng,lat,lists.get(arg0).getLng(),lists.get(arg0).getLat());
        if(at>1000){
            viewHolder.dis.setText(context.getString(R.string.dis_g, String
                    .valueOf(Math.floor(at/1000))));
        }else {
            viewHolder.dis.setText(context.getString(R.string.dis, String
                    .valueOf(at)));
        }

        viewHolder.title.setText(lists.get(arg0).getTitle());
       // viewHolder.intro.setText(lists.get(arg0).getIntro());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = lists.get(arg0);
                content0_handler.sendMessage(msg);
            }
        });
        return convertView;
    }
    public static class ViewHolder {
        public ImageView item0_img;
        public TextView title;//intro;
        public TextView dis;
    }
}
