package app.cn.aiyouv.www.adapter;


import android.content.Context;
import android.os.Handler;
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
import app.cn.aiyouv.www.bean.Fs_Bean;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.widget.OnDeleteListioner;

/**
 * 粉丝适配器
 */
public class FXAdapter extends BaseAdapter{
    private Context context;
    private int WIDTH;
    private Handler handler;
    private ArrayList<Fs_Bean> lists;
    private OnDeleteListioner onDeleteListioner;
    public FXAdapter(Context context, ArrayList<Fs_Bean> lists, Handler handler, OnDeleteListioner onDeleteListioner) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.onDeleteListioner = onDeleteListioner;
        this.handler = handler;
        this.lists = lists;
    }
    public void putData(ArrayList<Fs_Bean> lists){
        this.lists = lists;
        notifyDataSetChanged();
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
                    R.layout.fx_list_item, null);

            viewHolder = new ViewHolder();
            viewHolder.ico = (ImageView) convertView
                    .findViewById(R.id.ico);
            viewHolder.item0 = (TextView) convertView
                    .findViewById(R.id.item0);
            viewHolder.delete_action = (TextView) convertView
                    .findViewById(R.id.delete_action);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.item0.setText(lists.get(arg0).getName());
        Picasso.with(context).load(U.URL+lists.get(arg0).getPic()).into(viewHolder.ico);
        viewHolder.delete_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteListioner != null) {
                    onDeleteListioner.onDelete(lists.get(arg0));
                }
            }
        });
        return convertView;
    }
    public static class ViewHolder {
        public ImageView ico;
        public TextView item0,delete_action;
    }
    private String format(long date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String d = format.format(date);
        return	d;
    }
}
