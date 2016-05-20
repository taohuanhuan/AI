package app.cn.aiyouv.www.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.bean.DetailPics;
import app.cn.aiyouv.www.bean.MsgBean;
import app.cn.aiyouv.www.config.U;

/**
 * 详情图片
 */
public class ImagesAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<DetailPics> lists;
    public ImagesAdapter(Context context, ArrayList<DetailPics> lists) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.lists = lists;
    }
    public void putData(ArrayList<DetailPics> lists){
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
                    R.layout.detail_pic_item, null);

            viewHolder = new ViewHolder();

            viewHolder.item0 = (ImageView) convertView
                    .findViewById(R.id.item0);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load(U.URL+lists.get(arg0).getPic()).into( viewHolder.item0);
        return convertView;
    }
    public static class ViewHolder {
        public ImageView item0;
    }
    private String format(long date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String d = format.format(date);
        return	d;
    }
}
