package app.cn.aiyouv.www.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.bean.TicketsBean;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.inter.Tick;
import app.cn.aiyouv.www.widget.CircleImageView;

public class PopTicketsAdapter extends BaseAdapter{
    private Context context;
    private Tick tick;
    public PopTicketsAdapter(Context context, ArrayList<TicketsBean> imps, Tick tick) {
        // TODO Auto-generated constructor stub
        this.tick = tick;
        this.imps = imps;
        this.context = context;
    }
    private ArrayList<TicketsBean> imps;
    public void putData(ArrayList<TicketsBean> imps){
        this.imps = imps;
        this.position = -1;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imps.size();
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
    public String getSelectId(){
        if(position!=-1){
            return imps.get(position).getId();
        }
        return  null;
    }
    private int position = -1;
    public void setSelect(int position){
        this.position = position;
        notifyDataSetChanged();
    }
    public int getPosition(){
        return  position;
    }
    @Override
    public View getView(final int arg0, View convertView, ViewGroup arg2) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.pop_tick_item, null);

            viewHolder = new ViewHolder();
            viewHolder.ico = (CircleImageView) convertView.findViewById(R.id.ico);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.tip = (TextView) convertView.findViewById(R.id.tip);
            viewHolder.tag = (ImageView) convertView.findViewById(R.id.tag);
            viewHolder.tick_layout = (LinearLayout) convertView.findViewById(R.id.tick_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(position==arg0){
            viewHolder.tag.setVisibility(View.VISIBLE);
        }else {
            viewHolder.tag.setVisibility(View.GONE);
        }
        viewHolder.name.setText(imps.get(arg0).getName());
        viewHolder.price.setText(imps.get(arg0).getMoney()+"元");
        viewHolder.tip.setText(imps.get(arg0).getTip());
        Picasso.with(context).load(U.URL+imps.get(arg0).getPic()).into(viewHolder.ico);
        viewHolder.tick_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                C.p("点击");
                tick.select(arg0);
            }
        });
        return convertView;
    }
    public static class ViewHolder {
       TextView name,price,tip;
        CircleImageView ico;
        ImageView tag;
        LinearLayout tick_layout;
    }
}
