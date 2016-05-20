package app.cn.aiyouv.www.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.bean.BillTableBean;
import app.cn.aiyouv.www.bean.CateImp;
import app.cn.aiyouv.www.widget.InScrollGridView;

public class BillTableAdapter extends BaseAdapter{
    private Context context;
    public BillTableAdapter(Context context, ArrayList<BillTableBean> imps) {
        // TODO Auto-generated constructor stub
        this.imps = imps;
        this.context = context;
    }
    private ArrayList<BillTableBean> imps;
    public void putData(ArrayList<BillTableBean> imps){
        this.imps = imps;
        notifyDataSetChanged();
    }
    public float all(){
     int temp = 0;
        for(int i=0;i<imps.size();i++){
         temp +=Float.parseFloat(imps.get(i).getPrice());
     }
        return  temp;
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
    public View getView(int arg0, View convertView, ViewGroup arg2) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.bill_t_item, null);

            viewHolder = new ViewHolder();
            viewHolder.item0 = (TextView) convertView.findViewById(R.id.item0);
            viewHolder.item1 = (TextView) convertView.findViewById(R.id.item1);
            viewHolder.item2 = (TextView) convertView.findViewById(R.id.item2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.item0.setText(imps.get(arg0).getName());
        viewHolder.item1.setText(imps.get(arg0).getTime());
        viewHolder.item2.setText(imps.get(arg0).getPrice());
        return convertView;
    }
    public static class ViewHolder {
        TextView item0,item1,item2;
    }
}
