package app.cn.aiyouv.www.adapter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.bean.Article;
import app.cn.aiyouv.www.bean.Article_List;
import app.cn.aiyouv.www.config.U;


public class SearchAdapter extends BaseExpandableListAdapter{
	private Context context;
	private Handler handler;
	private int WIDTH;
	private ArrayList<ArrayList<Article_List>> sers;
	public SearchAdapter(Context context, Handler handler,int WIDTH, ArrayList<ArrayList<Article_List>> sers) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.WIDTH = WIDTH;
		this.handler = handler;
		this.sers = sers;
	}
	public void putData(ArrayList<ArrayList<Article_List>> sers){
		this.sers = sers;
		notifyDataSetChanged();
	}
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return sers.get(arg0).get(arg1);
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
		final ViewHolder viewHolder;
		if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.search_item, null);

			int height = process(convertView,196,642);
			viewHolder = new ViewHolder();
			viewHolder.item0_img = (ImageView) convertView
					.findViewById(R.id.item0_img);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.time);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.title);
			viewHolder.author = (TextView) convertView
					.findViewById(R.id.author);
			viewHolder.person = (TextView) convertView
					.findViewById(R.id.person);

			viewHolder.item0_img.setLayoutParams(new LinearLayout.LayoutParams(height-4,height-4));
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Picasso.with(context).load(U.URL+sers.get(arg0).get(arg1).getImg()).into(viewHolder.item0_img);
		viewHolder.time.setText(format(Long.parseLong(sers.get(arg0).get(arg1).getTime())));
		viewHolder.title.setText(sers.get(arg0).get(arg1).getTitle());
		viewHolder.author.setText(sers.get(arg0).get(arg1).getAuthor().equals("null")?"":sers.get(arg0).get(arg1).getAuthor());
		viewHolder.person.setText(context.getString(R.string.person, "会员原创"));

		return convertView;
	}
	private String format(long date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String d = format.format(date);
		return	d;
	}
	class ViewHolder{
		public TextView tag;
		public ImageView item0_img;
		public TextView time,title,author,person;
	}
	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return sers.get(arg0).size();
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
	public View getGroupView(int arg0, boolean arg1, View convertView, ViewGroup arg3) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		final ViewHolder viewHolder;
		if (convertView == null) {
		
				convertView = LayoutInflater.from(context).inflate(
						R.layout.sear_item, null);
			viewHolder = new ViewHolder();
			 viewHolder.tag = (TextView) convertView.findViewById(R.id.tag);
			 
			
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		switch (arg0){
			case 0:
				viewHolder.tag.setText("首页");
				break;
			case 1:
				viewHolder.tag.setText("原创");
				break;
			case 2:
				viewHolder.tag.setText("店家秀");
				break;
		}
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
