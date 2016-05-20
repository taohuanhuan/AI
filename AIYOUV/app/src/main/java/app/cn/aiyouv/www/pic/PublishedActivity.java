package app.cn.aiyouv.www.pic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.common.Common;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.config.U;
import app.cn.aiyouv.www.http.AsyncHttp;
import app.cn.aiyouv.www.http.ResponHandler;
import app.cn.aiyouv.www.utils.SharedUtils;
import app.cn.aiyouv.www.widget.CommonPicPop;
import app.cn.aiyouv.www.widget.InScrollGridView;
import app.cn.aiyouv.www.widget.NewDataToast;

public class PublishedActivity extends BaseActivity {
	private EditText publish_title,publish_content;
	private ImageView recy;
	private Button publish_send;
	private InScrollGridView noScrollgridview;
	private GridAdapter adapter;
	private TextView activity_selectimg_send;
	private LinearLayout lag;
	private LocationClient mLocationClient;
	private TextView city_show;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.publish_layout);
		Init();
	}
	private void initLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(Common.LOCATIONMODE);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType(Common.COOR_COUNT);//可选，默认gcj02，设置返回的定位结果坐标系，
		option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);//可选，默认false,设置是否使用gps
		option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		mLocationClient.setLocOption(option);
	}
	private int WDITH ;
	public void Init() {
		publish_send = (Button) findViewById(R.id.publish_send);
		publish_title = (EditText) findViewById(R.id.publish_title);
		publish_content = (EditText) findViewById(R.id.publish_content);
		recy = (ImageView) findViewById(R.id.recy);
		city_show = (TextView) findViewById(R.id.city_show);
		mLocationClient= new LocationClient(PublishedActivity.this);
		initLocation();
		mLocationClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation bdLocation) {

				SharedUtils utils = new SharedUtils(PublishedActivity.this, IUV.local);
				utils.setStringValue("lng",String.valueOf(bdLocation.getLongitude()));
				utils.setStringValue("lat",String.valueOf(bdLocation.getLatitude()));

				city_show.setText(bdLocation.getAddrStr());
				city_show.setTag(bdLocation);
			}
		});

		mLocationClient.start();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		WDITH = (dm.widthPixels-3)/3;
		lag = (LinearLayout) findViewById(R.id.lag);
		noScrollgridview = (InScrollGridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		lag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2 * WDITH + 1 + 2));
		adapter = new GridAdapter(this,WDITH);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				if (arg2 == Bimp.bmp.size()) {

					CommonPicPop.showSheet(PublishedActivity.this, new CommonPicPop.onSelect() {
						@Override
						public void camer() {
							photo();
						}

						@Override
						public void pic() {
							Intent intent = new Intent(PublishedActivity.this,
									PhotoViewActivity.class);
							startActivityForResult(intent, SELECT_PHOTO);

						}
					});

				} else {
					Intent intent = new Intent(PublishedActivity.this,
							PhotoViewsActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
		activity_selectimg_send = (TextView) findViewById(R.id.activity_selectimg_send);
		activity_selectimg_send.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				List<String> list = new ArrayList<String>();				
				for (int i = 0; i < Bimp.drr.size(); i++) {
					String Str = Bimp.drr.get(i).substring( 
							Bimp.drr.get(i).lastIndexOf("/") + 1,
							Bimp.drr.get(i).lastIndexOf("."));
					list.add(FileUtils.SDPATH+Str+".JPEG");				
				}
				// 高清的压缩图片全部就在  list 路径里面了
				// 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
				// 完成上传服务器后 .........
				FileUtils.deleteDir();
			}
		});
		recy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mLocationClient.start();
			}
		});
		publish_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//这里进行发布操作
		//title=，longitude=经线，latitude=纬线，content=，
		// articleImages[0].file=,articleImages[0],title= ,articleImages[1].file,articleImages[1],title
				try {
					BDLocation location = (BDLocation)city_show.getTag();

					String title = publish_title.getText().toString();
					String content = publish_content.getText().toString();
					SharedUtils utils = new SharedUtils(getApplicationContext(), IUV.status);
					RequestParams params = new RequestParams();
					params.put("title",title);
					params.put("longitude",String.valueOf(location.getLongitude()));
					params.put("latitude",String.valueOf(location.getLatitude()));
					params.put("content",content);
					params.put("appKey",utils.getStringValue("key"));
					params.put("userId", utils.getStringValue("id"));
					C.p(String.valueOf(location.getLongitude())+"--"+utils.getStringValue("key")+"打印"+utils.getStringValue("id"));
					try {
                    for(int i=0;i<Bimp.drr.size();i++){
                        C.p("图片地址："+Bimp.drr.get(i));
                            params.put("articleImages["+i+"].file",new File(Bimp.drr.get(i)));

                    }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
					AsyncHttp.post(U.URL+U.PUBLIC_SEND,params,new ResponHandler(PublishedActivity.this){
                        @Override
                        public void onSuccess(int index, String result) {
                            super.onSuccess(index, result);
                            C.p("结果" + result);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String status = jsonObject.getString("type");
                                if(status.equals(U.SUCCESS)){
                                    //成功
                                    handler_send.sendEmptyMessage(2);
                                }else{
                                    //失败
                                    handler_send.sendEmptyMessage(3);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });
				} catch (Exception e) {
					e.printStackTrace();
					NewDataToast.makeText(PublishedActivity.this,"请稍等").show();
				}


			}
		});

	}
	Handler handler_send = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

				case 2:
					NewDataToast.makeText(getApplicationContext(),"发布成功").show();
					AppManager.getAppManager().finishActivity();
					break;
				case 3:
					NewDataToast.makeText(getApplicationContext(),"发布失败").show();
					break;
			}
			super.handleMessage(msg);
		}
	};
	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;
		private int WDITH;
		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context,int WDITH) {
			this.WDITH = WDITH;
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			return (Bimp.bmp.size() + 1);
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				holder.image.setLayoutParams(new LinearLayout.LayoutParams(WDITH,WDITH));
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
					case 2:
						NewDataToast.makeText(getApplicationContext(),"发布成功").show();
						Bimp.bmp.clear();
						Bimp.drr.clear();
						Bimp.max = 0;
						FileUtils.deleteDir();
						AppManager.getAppManager().finishActivity();
						break;
					case 3:
						NewDataToast.makeText(getApplicationContext(),"发布失败").show();
						break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = Bimp.drr.get(Bimp.max);
								System.out.println(path);
								Bitmap bm = Bimp.revitionImageSize(path);
								Bimp.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		super.onRestart();
		//
		C.p("图片重新处理");
		adapter.update();
	}


	private   final int TAKE_PICTURE = 0x000000;
	private  final int SELECT_PHOTO=0x000001;
	private String path = "";

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Common.AIYOUV_PATH, String.valueOf(System.currentTimeMillis())
				+ ".jpg");
		path = file.getPath();
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.drr.size() < 9 && resultCode == -1) {
				Bimp.drr.add(path);
			}
			break;
			case SELECT_PHOTO:
				C.p("图片返回了");
					break;
		}
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString("title", publish_title.getText().toString());
		outState.putString("content", publish_content.getText().toString());
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(savedInstanceState.containsKey("title")){
			publish_title.setText(savedInstanceState.getString("title"));
			publish_content.setText(savedInstanceState.getString("content"));
		}
	}

}
