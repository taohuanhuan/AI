package app.cn.aiyouv.www.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.bean.Cate;
import app.cn.aiyouv.www.bean.CateImp;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.common.ConstantValues;
import app.cn.aiyouv.www.inter.Tick;
import app.cn.aiyouv.www.widget.InScrollGridView;

public class CateAdapter extends BaseAdapter{
    private Context context;

    public CateAdapter(Context context,ArrayList<CateImp> imps) {
        // TODO Auto-generated constructor stub
        this.imps = imps;
        this.context = context;
    }
    private ArrayList<CateImp> imps;
    private int INDEX;
    public void putData(ArrayList<CateImp> imps,int INDEX){
        this.imps = imps;
        this.INDEX = INDEX;
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
    private Tick tick = new Tick() {
        @Override
        public void select(int position) {
            notifyDataSetChanged();
        }
    };
    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.cate_view, null);

            viewHolder = new ViewHolder();
            viewHolder.cate_view = (InScrollGridView) convertView.findViewById(R.id.cate_view);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(imps.get(arg0).getTag());
        final CateItemAdapter itemAdapter = new CateItemAdapter(context,imps.get(arg0).getCates(),INDEX,tick);
        viewHolder.cate_view.setAdapter(itemAdapter);

      /*  if(INDEX==0){

                    viewHolder.cate_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            ConstantValues.CONTENT0 =  imps.get(i).getId();
                            C.p("点击"+imps.get(i).getId()+"--"+imps.get(i).getTag());
                            itemAdapter.notifyDataSetChanged();

                        }
                    });
        }else if(INDEX==1){
            viewHolder.cate_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ConstantValues.CONTENT1 =  imps.get(i).getId();
                    itemAdapter.notifyDataSetChanged();
                }
            });
        }else if(INDEX==2){
            viewHolder.cate_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ConstantValues.CONTENT2 =  imps.get(i).getId();
                    itemAdapter.notifyDataSetChanged();
                }
            });
        }*/
        return convertView;
    }
    public static class ViewHolder {
        InScrollGridView cate_view;
        TextView title;
    }
}
