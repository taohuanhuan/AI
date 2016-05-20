package app.cn.aiyouv.www.widget;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import app.cn.aiyouv.www.R;

/**
 * @author
 * @version 1.0
 * @created 2012-8-30
 */
@SuppressLint("ViewConstructor")
public class NewDataToast extends Toast {
	private static NewDataToast result;
	@SuppressWarnings("unused")
	private MediaPlayer mPlayer;
	@SuppressWarnings("unused")
	public static final int PHONE = 1;
	public static final int ORDER_NULL = 2;
	public static final int NULL = 0;
	public static final int ORDER_IN = 3;
	public NewDataToast(Context context) {
		super(context);
		// switch (rawid) {
		// case NULL:
		// mPlayer = MediaPlayer.create(context, R.raw.phone);
		// break;
		// case PHONE:
		// mPlayer = MediaPlayer.create(context, R.raw.phone);
		// break;
		// case ORDER_NULL:
		// mPlayer = MediaPlayer.create(context, R.raw.order_null);
		// break;
		// case ORDER_IN:
		// mPlayer = MediaPlayer.create(context, R.raw.order_in);
		// break;
		// default:
		// break;
		// }
		// mPlayer.setOnCompletionListener(new
		// MediaPlayer.OnCompletionListener(){
		// public void onCompletion(MediaPlayer mp) {
		// mp.release();
		// }
		// });
	}
	@Override
	public void show() {
		super.show();
		// if(isSound){
		// mPlayer.start();
		// }
	}

	public static NewDataToast makeText(Context context, CharSequence text) {
		if (result == null) {
			result = new NewDataToast(context);
		}
		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		View v = inflate.inflate(R.layout.new_data_toast, null);
		// v.setMinimumWidth(dm.widthPixels);//璁剧疆鎺т欢锟�锟斤拷瀹藉害涓烘墜鏈哄睆骞曞锟�
		TextView tv = (TextView) v.findViewById(R.id.new_data_toast_message);
		tv.setText(text);
		result.setView(v);
		result.setDuration(Toast.LENGTH_SHORT);
		result.setGravity(Gravity.BOTTOM, 0, (int) (dm.density * 75));
		return result;
	}
	public static void hodden() {
		
		if (result != null) {
		
			result.cancel();
		
		}
	}
}
