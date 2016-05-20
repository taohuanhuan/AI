package app.cn.aiyouv.www.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.weibo.sdk.android.api.ActivityInvokeAPI;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.common.Common;
import app.cn.aiyouv.www.common.Util;


public class CommonSharedPop {
	private static IWXAPI api;
	public interface onSelect {
		void onClick(Object object);
	}
	/**
	 * 删除弹出框
	 */
	private CommonSharedPop() {
	}
	private static String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}
	private static void sharedWx(Activity context,String url){
		api = WXAPIFactory.createWXAPI(context, Common.WX_ID, false);
		api.registerApp(Common.WX_ID);
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = url;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = "我发你一个软件,看看呗!";
		msg.description = "介绍";
		Bitmap thumb = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_launcher);

		msg.thumbData = Util.bmpToByteArray(thumb, true);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;
		boolean flag = api.sendReq(req);
		if(thumb!=null&&!thumb.isRecycled()){
			thumb.recycle();
			thumb= null;
		}
	}
	private static void softshareWxFriend(Activity context,String url) {
		api = WXAPIFactory.createWXAPI(context, Common.WX_ID, false);
		api.registerApp(Common.WX_ID);
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = url;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = "我发你一个软件,看看呗!";
//		msg.title = "ni"+"我发你一个软件,看看呗!";
		msg.description ="";
		Bitmap thumb = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_launcher);
		msg.thumbData = Util.bmpToByteArray(thumb, true);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		boolean flag = api.sendReq(req);
		System.out.println(flag+"-->"+msg.thumbData);
	}
	public static Dialog showSheet(final Activity context,
								  final String url) {
		final Dialog dlg = new Dialog(context, R.style.delete_pop_style);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.common_shared_pop, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		LinearLayout mContent = (LinearLayout) layout.findViewById(R.id.content);
		TextView mCancel = (TextView) layout.findViewById(R.id.cancel);

		LinearLayout shared0 = (LinearLayout) mContent.findViewById(R.id.shared0);
		final LinearLayout shared1 = (LinearLayout) mContent.findViewById(R.id.shared1);
		LinearLayout shared2 = (LinearLayout) mContent.findViewById(R.id.shared2);

		shared0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sharedWx(context,url);
			}
		});
		shared1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				softshareWxFriend(context,url);
			}
		});
		shared2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				try {
					ActivityInvokeAPI.openSendWeibo(context, url);
				} catch (ActivityNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					NewDataToast.makeText(context, "未安装新浪微博").show();
				}
			}
		});
		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				actionSheetSelected.onClick(object);
				dlg.dismiss();
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(false);


		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

}
