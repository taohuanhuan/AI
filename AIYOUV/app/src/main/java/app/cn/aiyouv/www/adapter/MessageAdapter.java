package app.cn.aiyouv.www.adapter;

import java.io.File;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.os.Handler;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.bean.Message;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.utils.FaceConversionUtil;
import app.cn.aiyouv.www.utils.SharedUtils;
import app.cn.aiyouv.www.utils.TimeUtil;

public class MessageAdapter extends BaseAdapter {
	private Context context;
	private List<Message> list_Messages;
	private Handler handler;
	private String hyid;
	private SharedUtils utils = null;
	private boolean flag;

	public MessageAdapter(Context context, List<Message> list_Messages,
			  Handler handler, String hyid, boolean flag) {
		super();
		this.context = context;
		this.list_Messages = list_Messages;
		this.handler = handler;
		this.hyid = hyid;
		this.flag = flag;
		utils = new SharedUtils(context, IUV.status);
	}

	/**
	 * �Ƴ�
	 */
	public void removeHeadMsg() {
		if (list_Messages.size() - 10 > 10) {
			for (int i = 0; i < 10; i++) {
				list_Messages.remove(i);
			}
			notifyDataSetChanged();
		}
	}

	/**
	 * 
	 * @param msg
	 */
	public void setMessage(List<Message> list_Messages) {
		this.list_Messages = list_Messages;
		notifyDataSetChanged();
	}

	/*
	 * ������Ϣ
	 * 
	 * @param msg
	 */
	public void upDateMsg(Message msg) {
		list_Messages.add(msg);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list_Messages.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list_Messages.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int index, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Message message = list_Messages.get(index);
	boolean isreceiver = message.isMine();
		final ViewHolder holder;
		if (view == null || view.getTag(R.drawable.ic_launcher + index) == null) {
			holder = new ViewHolder();
			if (!isreceiver) {
				view = LinearLayout.inflate(context, R.layout.chat_item_left,
						null);

			} else {
				view = LinearLayout.inflate(context, R.layout.chat_item_right,
						null);
			}

			holder.group_name = (TextView) view.findViewById(R.id.group_name);
			holder.icon = (ImageView) view.findViewById(R.id.icon);
			holder.message_time = (TextView) view
					.findViewById(R.id.message_time);

			holder.message_Text = (TextView) view.findViewById(R.id.message);

			view.setTag(R.drawable.ic_launcher + index);
		} else {
			holder = (ViewHolder) view.getTag(R.drawable.ic_launcher + index);
		}

		Picasso.with(context).load(U.URL+list_Messages.get(index).getImg()).error(R.drawable.fs_ico).into(holder.icon);

		SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context, list_Messages.get(index).getContent());

		holder.message_Text.setText(spannableString);
		holder.message_time.setText(TimeUtil.getChatTime(list_Messages.get(
				index).getTime()));
		return view;
	}

	/**
	 *
	 * @param type
	 * @param view
	 */

	public class ViewHolder {
		private TextView message_Text;
		private TextView message_time;
		private ImageView icon;
		private TextView group_name;
	}
}
