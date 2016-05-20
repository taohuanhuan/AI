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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.bean.Article_List;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.ui.MainFragmentContent1;

public class Content1Item0Adapter extends BaseAdapter{
    private Context context;
    private int WIDTH;
    private MainFragmentContent1.Content1_Handler content1_handler;
    private ArrayList<Article_List> lists;
    public Content1Item0Adapter(Context context, int WIDTH,ArrayList<Article_List> lists, MainFragmentContent1.Content1_Handler content1_handler) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.WIDTH = WIDTH;
        this.content1_handler = content1_handler;
        this.lists = lists;
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
    private int process(View view,int h,int w){
        double mix_f = WIDTH * (double) h / w;

        AbsListView.LayoutParams paramsf = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                (int) mix_f);
        view.setLayoutParams(paramsf);
        return  (int)mix_f;
    }
    @Override
    public View getView(final int arg0, View convertView, ViewGroup arg2) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.content1_item, null);
           int height = process(convertView,196,642);
            viewHolder = new ViewHolder();
            viewHolder.item0_img = (ImageView) convertView
                    .findViewById(R.id.item0_img);
            viewHolder.time = (TextView) convertView
                    .findViewById(R.id.time);
            viewHolder.title = (TextView) convertView
                    .findViewById(R.id.title);
            viewHolder.author = (TextView) convertView
                    .findViewById(R.id.author);
            viewHolder.person = (TextView) convertView
                    .findViewById(R.id.person);
            viewHolder.hits = (TextView) convertView.findViewById(R.id.hits);
            viewHolder.item0_img.setLayoutParams(new LinearLayout.LayoutParams(height-4,height-4));

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.item0_img.setText(contents.get(arg0).getApk());
        Picasso.with(context).load(U.URL+lists.get(arg0).getImg()).into(viewHolder.item0_img);
        viewHolder.time.setText(format(Long.parseLong(lists.get(arg0).getTime())));
        viewHolder.title.setText(lists.get(arg0).getTitle());
        viewHolder.author.setText(lists.get(arg0).getAuthor().equals("null")?"":lists.get(arg0).getAuthor());
        viewHolder.person.setText(context.getString(R.string.person, "会员原创"));
        viewHolder.hits.setText(context.getString(R.string.content1_item0_txt,lists.get(arg0).getHit()));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = lists.get(arg0);
                content1_handler.sendMessage(msg);
            }
        });
        return convertView;
    }
    public static class ViewHolder {
        public ImageView item0_img;
        public TextView time,title,author,person,hits;
    }
    private String format(long date){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd");
        String d = format.format(date);

        return	d;

    }
}
