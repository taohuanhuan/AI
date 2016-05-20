package app.cn.aiyouv.www.adapter;


import android.content.Context;
import android.os.Handler;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.bean.Article_List;
import app.cn.aiyouv.www.bean.ReplyBean;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.inter.Tick;
import app.cn.aiyouv.www.utils.FaceConversionUtil;


public class ReplyAdapter extends BaseExpandableListAdapter{
	private Context context;
	private int WIDTH;
	private ArrayList<ReplyBean> sers;
	private Tick tick;
	public ReplyAdapter(Context context,ArrayList<ReplyBean> sers,Tick tick) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.sers = sers;
		this.tick = tick;
	}
	public void putData(ArrayList<ReplyBean> sers){
		this.sers = sers;
		notifyDataSetChanged();
	}
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return sers.get(arg0).getReplyBeans().get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}
	private int process(View view,int h,int w){
		double mix_f = WIDTH * (double) h / w;

		AbsListView.LayoutParams paramsf = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				(int) mix_f);
		view.setLayoutParams(paramsf);
		return  (int)mix_f;
	}
	@Override
	public View getChildView(final int arg0, final int arg1, boolean arg2, View convertView,
			ViewGroup arg4) {
		// TODO Auto-generated method stub
		final CViewHolder viewHolder;
		if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.reply_title_c, null);

			viewHolder = new CViewHolder();
			viewHolder.ico = (ImageView) convertView.findViewById(R.id.ico);
			viewHolder.tag = (TextView) convertView.findViewById(R.id.tag);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.content = (TextView) convertView.findViewById(R.id.content);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (CViewHolder) convertView.getTag();
		}
		Picasso.with(context).load(U.URL+sers.get(arg0).getReplyBeans().get(arg1).getIco()).into(viewHolder.ico);
		viewHolder.tag.setText(sers.get(arg0).getReplyBeans().get(arg1).getName());
		viewHolder.time.setText(sers.get(arg0).getReplyBeans().get(arg1).getTime());
		SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context, sers.get(arg0).getReplyBeans().get(arg1).getContent());
		viewHolder.content.setText(spannableString);
		return convertView;
	}
	private String format(long date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String d = format.format(date);
		return	d;
	}
	class CViewHolder{
		public TextView tag;
		public ImageView ico;
		public TextView time,content;
	}
	class ViewHolder{
		public TextView tag,reply;
		public ImageView ico;
		public TextView time,content;

	}
	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return sers.get(arg0).getReplyBeans().size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return sers.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return sers.size();
	}



	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(final int arg0, boolean arg1, View convertView, ViewGroup arg3) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		final ViewHolder viewHolder;
		if (convertView == null) {
		
				convertView = LayoutInflater.from(context).inflate(
						R.layout.reply_title, null);
			viewHolder = new ViewHolder();
			viewHolder.ico = (ImageView) convertView.findViewById(R.id.ico);
			viewHolder.reply = (TextView) convertView.findViewById(R.id.reply);
			viewHolder.tag = (TextView) convertView.findViewById(R.id.tag);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.content = (TextView) convertView.findViewById(R.id.content);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Picasso.with(context).load(U.URL+sers.get(arg0).getIco()).into(viewHolder.ico);
		viewHolder.tag.setText(sers.get(arg0).getName());
		viewHolder.time.setText(sers.get(arg0).getTime());
		SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context, sers.get(arg0).getContent());
		viewHolder.content.setText(spannableString);
		viewHolder.reply.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				tick.select(Integer.parseInt(sers.get(arg0).getReplyId()));
			}
		});
		return convertView;
	
	}
	private void isChildSelectable() {
		// TODO Auto-generated method stub

	}	
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	} 
	
}
