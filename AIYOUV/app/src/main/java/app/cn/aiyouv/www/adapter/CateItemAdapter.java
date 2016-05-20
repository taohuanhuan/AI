package app.cn.aiyouv.www.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.bean.Cate;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.common.ConstantValues;
import app.cn.aiyouv.www.inter.Tick;
import app.cn.aiyouv.www.widget.InScrollGridView;

public class CateItemAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Cate> cates;
    private int INDEX;
    private Tick tick;
    public CateItemAdapter(Context context,ArrayList<Cate> cates,int INDEX,Tick tick) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.cates = cates;
        this.INDEX = INDEX;
        this.tick = tick;
    }

    public void putData(){
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return cates.size();
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
    private String SELECT_ID;
    public void select(String SELECT_ID){
        this.SELECT_ID = SELECT_ID;
        notifyDataSetChanged();
    }
    @Override
    public View getView(final int arg0, View convertView, ViewGroup arg2) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.cate_item, null);

            viewHolder = new ViewHolder();
            viewHolder.text_item = (TextView) convertView.findViewById(R.id.text_item);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(INDEX==0){
            if(ConstantValues.CONTENT0.equals(cates.get(arg0).getId())){
                viewHolder.text_item.setTextColor(context.getResources().getColor(R.color.red));
            }else{
                viewHolder.text_item.setTextColor(context.getResources().getColor(R.color.iuv_txt_normal));
            }
            viewHolder.text_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConstantValues.CONTENT0 = cates.get(arg0).getId();
                    C.p("点击" + ConstantValues.CONTENT0 + "--" + cates.get(arg0).getTag());
                    notifyDataSetChanged();
                    tick.select(0);
                }
            });

        }else if(INDEX==1){
            if(ConstantValues.CONTENT1.equals(cates.get(arg0).getId())){
                viewHolder.text_item.setTextColor(context.getResources().getColor(R.color.red));
            }else{
                viewHolder.text_item.setTextColor(context.getResources().getColor(R.color.iuv_txt_normal));
            }

            viewHolder.text_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConstantValues.CONTENT1 = cates.get(arg0).getId();
                    C.p("点击" + ConstantValues.CONTENT1 + "--" + cates.get(arg0).getTag());
                    notifyDataSetChanged();
                    tick.select(0);
                }
            });
        }else if(INDEX==2){
            if(ConstantValues.CONTENT2.equals(cates.get(arg0).getId())){
                viewHolder.text_item.setTextColor(context.getResources().getColor(R.color.red));
            }else{
                viewHolder.text_item.setTextColor(context.getResources().getColor(R.color.iuv_txt_normal));
            }

            viewHolder.text_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConstantValues.CONTENT2 = cates.get(arg0).getId();
                    C.p("点击" + ConstantValues.CONTENT2 + "--" + cates.get(arg0).getTag());
                    notifyDataSetChanged();
                    tick.select(0);
                }
            });
        }

        viewHolder.text_item.setText(cates.get(arg0).getTag());
        return convertView;
    }
    public static class ViewHolder {
        TextView text_item;
    }
}
