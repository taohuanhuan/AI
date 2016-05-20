package app.cn.aiyouv.www.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.bean.EditsBean;
import app.cn.aiyouv.www.bean.TicketsBean;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.inter.Tick;
import app.cn.aiyouv.www.widget.CircleImageView;

public class EditsAdapter extends BaseAdapter{
    private Context context;
    private Tick tick;
    public EditsAdapter(Context context, ArrayList<EditsBean> imps,Tick tick) {
        // TODO Auto-generated constructor stub
        this.imps = imps;
        this.context = context;
        this.tick = tick;
    }
    private ArrayList<EditsBean> imps;
    public void putData(ArrayList<EditsBean> imps){
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
                    R.layout.edit_item, null);

            viewHolder = new ViewHolder();
            viewHolder.tag = (ImageView) convertView.findViewById(R.id.tag);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.edit_0 = (TextView) convertView.findViewById(R.id.edit_0);
            viewHolder.edit_1 = (TextView) convertView.findViewById(R.id.edit_1);
            viewHolder.edit_2 = (TextView) convertView.findViewById(R.id.edit_2);
            viewHolder.edit_3 = (TextView) convertView.findViewById(R.id.edit_3);
            viewHolder.ico = (ImageView) convertView.findViewById(R.id.ico);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(imps.get(arg0).getName());
        viewHolder.edit_0.setText(context.getString(R.string.edit_0, imps.get(arg0).getData()));
        Picasso.with(context).load(U.URL+imps.get(arg0).getImage()).into(viewHolder.ico);
        viewHolder.edit_1.setText(context.getString(R.string.edit_1, imps.get(arg0).getAll()));
        viewHolder.edit_2.setText(context.getString(R.string.edit_2, imps.get(arg0).getMoney()));
        viewHolder.edit_3.setText(context.getString(R.string.edit_3,imps.get(arg0).getTicket()));

        if(imps.get(arg0).isVip()){
            Picasso.with(context).load(R.drawable.iuv_tag_p).into(viewHolder.tag);

        }else {
            Picasso.with(context).load(R.drawable.iuv_tag_n).into(viewHolder.tag);

        }
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             tick.select(arg0);
            }
        });
        return convertView;
    }
    public static class ViewHolder {
       TextView name,edit_0,edit_1,edit_2,edit_3;
        ImageView tag,ico;
        ImageView delete;
    }
}
