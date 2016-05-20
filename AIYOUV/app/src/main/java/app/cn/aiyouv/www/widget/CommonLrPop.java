package app.cn.aiyouv.www.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.adapter.CateAdapter;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.common.Common;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.ui.CommonWeb;
import app.cn.aiyouv.www.ui.FindPassActivity;
import app.cn.aiyouv.www.ui.MainFragmentActivity;
import app.cn.aiyouv.www.ui.MainFragmentContent4;
import app.cn.aiyouv.www.ui.Vuforia_Qr_Activity;
import app.cn.aiyouv.www.utils.SharedUtils;


public class CommonLrPop  extends Dialog{

	private static CommonLrPop commonLrPop;
	public CommonLrPop(Context context) {
		super(context);
	}

	public CommonLrPop(Context context, int theme) {
		super(context, theme);
	}

	public interface onSelect {
		void onClick(Object object);
	}
	private static Handler handler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {

			super.dispatchMessage(msg);
			switch (msg.what){

				case 7:
					register_item2.setEnabled(false);
					register_item2.setText(msg.arg1 + "s");

					break;
				case 8:
					register_item2.setEnabled(true);
					register_item2.setText("发送验证码");
					break;
				default:
					break;
			}
		}
	};
	/**
	 * 删除弹出框
	 */

	private static TextView register_item2;
	private static EditText register_item0;

	 private  LinearLayout layout0,layout1;
	public static Dialog showSheet(final Activity context,
								   final onSelect actionSheetSelected,
								   final MainFragmentActivity.MainFragment_Handler mainFragment_handler, final MainFragmentContent4.Content4_Handler content4_handler) {
		C.p(context.getClassLoader().getClass().getName());
		if (commonLrPop==null) {
			commonLrPop = new CommonLrPop(context, R.style.delete_pop_style);
		}
			if (!commonLrPop.isShowing()) {
				commonLrPop = new CommonLrPop(context, R.style.delete_pop_style);
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout layout = (LinearLayout) inflater.inflate(
						R.layout.login_register_layout, null);
				final int cFullFillWidth = 10000;
				layout.setMinimumWidth(cFullFillWidth);
				final RadioGroup btns = (RadioGroup) layout.findViewById(R.id.btns);
				final LinearLayout layout0 = (LinearLayout) layout.findViewById(R.id.layout0);
				final LinearLayout layout1 = (LinearLayout) layout.findViewById(R.id.layout1);
				layout.findViewById(R.id.find).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent= new Intent(context, FindPassActivity.class);
						context.startActivity(intent);
					}
				});
				btns.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radioGroup, int i) {
						switch (i) {
							case R.id.login:
								layout0.setVisibility(View.VISIBLE);
								layout1.setVisibility(View.GONE);

								break;
							case R.id.register:
								layout0.setVisibility(View.GONE);
								layout1.setVisibility(View.VISIBLE);
								break;
							default:
								break;
						}
					}
				});
				btns.check(R.id.login);
				final EditText login_item0 = (EditText) layout.findViewById(R.id.login_item0);
				final EditText login_item1 = (EditText) layout.findViewById(R.id.login_item1);
				Button login_item2 = (Button) layout.findViewById(R.id.login_item2);
				login_item2.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View view) {
						final String name = login_item0.getText().toString();
						final String password = login_item1.getText().toString();
						RequestParams params = new RequestParams();
						params.put("phone", name);
						params.put("password", password);
						AsyncHttp.post(U.URL + U.IUV_LOGIN, params, new ResponHandler() {
							@Override
							public void onSuccess(int i, String s) {
								super.onSuccess(i, s);
								C.p("登录" + s);
								try {
									JSONObject jsonObject = new JSONObject(s);
									String status = jsonObject.getString("type");
									if (status.equals(U.SUCCESS)) {
										//
										JSONObject obj = jsonObject.getJSONObject("data");
										SharedUtils utils = new SharedUtils(context, IUV.status);
										utils.setStringValue("id", obj.getString("id"));
										utils.setStringValue("user", name);
										utils.setStringValue("pwd", password);
										utils.setStringValue("loginType",obj.getString("loginType"));
										if(obj.has("phone")){
											utils.setStringValue("photo",obj.getString("phone"));
										}

										utils.setStringValue("key",obj.getString("username"));
										IUV.iuv = utils.getStringValue("key");
										NewDataToast.makeText(context, "登录成功").show();
										C.p("登录--"+(content4_handler!=null));
										if(content4_handler!=null){
											content4_handler.sendEmptyMessage(2);
										}
										commonLrPop.dismiss();

									} else {
										NewDataToast.makeText(context, jsonObject.getString("content")).show();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}
						});
					}
				});

				//注册
				//-------------------
				register_item0 = (EditText) layout.findViewById(R.id.register_item0);
				final EditText register_item1 = (EditText) layout.findViewById(R.id.register_item1);
				register_item2 = (TextView) layout.findViewById(R.id.register_item2);
				final EditText register_item3 = (EditText) layout.findViewById(R.id.register_item3);
				final EditText register_item4 = (EditText) layout.findViewById(R.id.register_item4);
				Button register_item5 = (Button) layout.findViewById(R.id.register_item5);
				final CheckBox register_check = (CheckBox) layout.findViewById(R.id.register_check);
				TextView register_tip = (TextView) layout.findViewById(R.id.register_tip);
				register_tip.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(context, CommonWeb.class);
						intent.putExtra("title", "哎呦喂注册协议");
						intent.putExtra("file", "file:///android_asset/tip/tip.html");
						context.startActivity(intent);
					}
				});
				register_item2.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View view) {
						String phone = register_item0.getText().toString();
						if (phone.length() == 0 || phone.length() != 11 || !phone.startsWith("1")) {
							register_item0.setError("手机号码格式不正确");
							register_item0.findFocus();
						} else if (!register_check.isChecked()) {
							NewDataToast.makeText(context, "请同意注册协议").show();
						} else {

							RequestParams params = new RequestParams();
							params.put("phone", phone);
							AsyncHttp.post(U.URL + U.SEND_VAL, params, new ResponHandler() {
								@Override
								public void onSuccess(int i, String s) {
									super.onSuccess(i, s);
									C.p("发送验证码" + s);
									try {
										JSONObject jsonObject = new JSONObject(s);
										String status = jsonObject.getString("type");
										if (status.equals(U.SUCCESS)) {
											new Thread() {
												public void run() {
													for (int i = 60; i >= 0; i--) {
														if (i == 0) {
															handler.sendEmptyMessage(8);
														} else {
															Message msg = new Message();
															msg.arg1 = i;
															msg.what = 7;
															handler.sendMessage(msg);

															try {
																Thread.sleep(1000);
															} catch (InterruptedException e) {
																// TODO Auto-generated catch block
																e.printStackTrace();
															}

														}
													}
												}

												;
											}.start();
										} else {
											NewDataToast.makeText(context, jsonObject.getString("content")).show();
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}

								}
							});


						}
					}
				});
				register_item5.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View view) {
						String phone = register_item0.getText().toString();
						String code = register_item1.getText().toString();
						String author = register_item3.getText().toString();
						String password = register_item4.getText().toString();
						RequestParams params = new RequestParams();
						params.put("phone", phone);
						params.put("code", code);
						params.put("password", password);
						params.put("pristineName", author);
						AsyncHttp.post(U.URL + U.IUV_REGISTER, params, new ResponHandler() {
							@Override
							public void onSuccess(int i, String s) {
								super.onSuccess(i, s);
								try {
									JSONObject jsonObject = new JSONObject(s);
									String status = jsonObject.getString("type");
									if (status.equals(U.SUCCESS)) {
										//注册成功
										btns.check(R.id.login);
//								handler.sendEmptyMessage(6);
										NewDataToast.makeText(context, "注册成功").show();
									} else {
										NewDataToast.makeText(context, jsonObject.getString("content")).show();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}
						});

					}
				});


				Window w = commonLrPop.getWindow();
				WindowManager.LayoutParams lp = w.getAttributes();
				lp.x = 0;
				final int cMakeBottom = -1000;
				//	lp.y = cMakeBottom;
				lp.gravity = Gravity.CENTER;
				commonLrPop.onWindowAttributesChanged(lp);
				commonLrPop.setCanceledOnTouchOutside(true);

				commonLrPop.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialogInterface) {
						if (IUV.iuv.length() == 0) {
							mainFragment_handler.sendEmptyMessage(R.id.main_bottom_rtn0);
						}
						commonLrPop = null;

					}
				});
				commonLrPop.setContentView(layout);
				commonLrPop.show();

		}
		return commonLrPop;

	}

}
