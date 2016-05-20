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
import app.cn.aiyouv.www.bean.RewardBean;
import app.cn.aiyouv.www.bean.TicketsBean;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.inter.Tick;
import app.cn.aiyouv.www.widget.CircleImageView;

public class RewardAdapter extends BaseAdapter{
    private Context context;
    private Tick tick;
    public RewardAdapter(Context context, ArrayList<RewardBean> imps) {
        // TODO Auto-generated constructor stub
        this.imps = imps;
        this.context = context;
    }
    private ArrayList<RewardBean> imps;
    public void putData(ArrayList<RewardBean> imps){
        this.imps = imps;
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


    @Override
    public View getView(final int arg0, View convertView, ViewGroup arg2) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.reward_item, null);

            viewHolder = new ViewHolder();
            viewHolder.item0 = (CircleImageView) convertView.findViewById(R.id.item0);
            viewHolder.item1 = (TextView) convertView.findViewById(R.id.item1);
            viewHolder.item2 = (TextView) convertView.findViewById(R.id.item2);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load(U.URL+imps.get(arg0).getImg()).into(viewHolder.item0);
        viewHolder.item1.setText(imps.get(arg0).getName());
        viewHolder.item2.setText(imps.get(arg0).getPrice());


        return convertView;
    }
    public static class ViewHolder {
       TextView item1,item2;
        CircleImageView item0;

    }
}
