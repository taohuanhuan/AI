package app.cn.aiyouv.www.adapter;

import java.util.List;
import com.squareup.picasso.Picasso;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import app.cn.aiyouv.www.R;

/**
 * 
 ******************************************
 * @文件名称	:  FaceAdapter.java
 * @文件描述	: 表情填充器
 ******************************************
 */
public class FaceAdapter extends BaseAdapter {

    private List<String> data;

    private LayoutInflater inflater;

    private int size=0;
    private Context context;
    public FaceAdapter(Context context, List<String> list) {
    	this.context = context;
        this.inflater=LayoutInflater.from(context);
        this.data=list;
        this.size=list.size();
    }

    @Override
    public int getCount() {
        return this.size;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView == null) {
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_face, null);
            viewHolder.iv_face=(ImageView)convertView.findViewById(R.id.item_iv_face);
            convertView.setTag(viewHolder);
        } else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        if(data.get(position).equals("face_del_icon")) {
            convertView.setBackgroundDrawable(null);
            viewHolder.iv_face.setImageResource(R.drawable.face_del_icon);
        } else if(TextUtils.isEmpty(data.get(position))) {
            convertView.setBackgroundDrawable(null);
            viewHolder.iv_face.setImageDrawable(null);
        } else {
            viewHolder.iv_face.setTag("^"+data.get(position)+"^");
            Picasso.with(context).load("file:///android_asset/"+data.get(position)+".gif").into(viewHolder.iv_face);
//				viewHolder.iv_face.setImageBitmap(BitmapFactory.decodeStream(context.getAssets().open(data.get(position)+".gif")));
        }

        return convertView;
    }

    class ViewHolder {

        public  ImageView iv_face;
    }
}