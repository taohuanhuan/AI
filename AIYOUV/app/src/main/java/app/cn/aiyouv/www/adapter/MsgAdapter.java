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
import app.cn.aiyouv.www.bean.MsgBean;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.widget.OnDeleteListioner;

/**
 * 粉丝适配器
 */
public class MsgAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<MsgBean> lists;
    public MsgAdapter(Context context, ArrayList<MsgBean> lists) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.lists = lists;
    }
    public void putData(ArrayList<MsgBean> lists){
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

    @Override
    public View getView(final int arg0, View convertView, ViewGroup arg2) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.sg_list_item, null);

            viewHolder = new ViewHolder();

            viewHolder.item0 = (TextView) convertView
                    .findViewById(R.id.item0);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.item0.setText(lists.get(arg0).getMsg());

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
