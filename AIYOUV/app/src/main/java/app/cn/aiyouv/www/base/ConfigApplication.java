package app.cn.aiyouv.www.base;


import android.app.Application;
import android.app.PendingIntent;
import android.app.Service;
import android.os.Environment;
import android.os.Vibrator;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;

import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.utils.SharedUtils;

import static app.cn.aiyouv.www.common.Common.AIYOUV_PATH;

/**
 * 主Application，所有百度定位SDK的接口说明请参线上文档：http://developer.baidu.com/map/loc_refer/index.html
 * 百度定位SDK官方网站：http://developer.baidu.com/map/index.php?title=android-locsdk
 */
public class ConfigApplication extends Application {
	public LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;
	protected boolean isNeedCaughtExeption = true;// 是否捕获未知异常
	public TextView mLocationResult,logMsg,cityText;
	public TextView trigger,exit;
	public Vibrator mVibrator;
	private String packgeName;
	private PendingIntent restartIntent;
	private MyUncaughtExceptionHandler uncaughtExceptionHandler;

	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(this);
		packgeName = getPackageName();
		if (isNeedCaughtExeption) {
			cauchException();
		}
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
	}
	private void cauchException() {
		System.out.println("-----------------------------------------------------");
//		Intent intent = new Intent();
//		// 参数1：包名，参数2：程序入口的activity
//		intent.setClassName(packgeName, packgeName + ".GuideActivity");
		//restartIntent = PendingIntent.getActivity(getApplicationContext(), -1, intent,
		//			Intent.FLAG_ACTIVITY_NEW_TASK);
		// 程序崩溃时触发线程
		uncaughtExceptionHandler = new MyUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
	}
	private class MyUncaughtExceptionHandler implements UncaughtExceptionHandler {
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			// 保存错误日志
			saveCatchInfo2File(ex);
			// 1秒钟后重启应用
			//AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			//mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
			// 关闭当前应用
			AppManager.getAppManager().finishAllActivity();

		}
	};
	/**
	 * 保存错误信息到文件中
	 *
	 * @return 返回文件名称
	 */
	private String saveCatchInfo2File(Throwable ex) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String sb = writer.toString();
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String time = formatter.format(new Date());
			String fileName = time + ".txt";
			System.out.println("fileName:" + fileName);
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String filePath =  AIYOUV_PATH + packgeName+ "/crash/";
				File dir = new File(filePath);
				if (!dir.exists()) {
					if (!dir.mkdirs()) {
						// 创建目录失败: 一般是因为SD卡被拔出了
						return "";
					}
				}
				System.out.println("filePath + fileName:" + filePath + fileName);
				FileOutputStream fos = new FileOutputStream(filePath + fileName);
				fos.write(sb.getBytes());
				fos.close();
				//文件保存完了之后,在应用下次启动的时候去检查错误日志,发现新的错误日志,就发送给开发者
			}
			return fileName;
		} catch (Exception e) {
			System.out.println("an error occured while writing file..." + e.getMessage());
		}
		return null;
	}

	/**
	 * 实现实时位置回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());// 单位：公里每小时
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\nheight : ");
				sb.append(location.getAltitude());// 单位：米
				sb.append("\ndirection : ");
				sb.append(location.getDirection());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\ndescribe : ");
				sb.append("gps定位成功");

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				//运营商信�?                sb.append("\noperationers : ");
				sb.append(location.getOperators());
				sb.append("\ndescribe : ");
				sb.append("网络定位成功");
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
				sb.append("\ndescribe : ");
				sb.append("离线定位成功，离线定位结果也是有效的");
			} else if (location.getLocType() == BDLocation.TypeServerError) {
				sb.append("\ndescribe : ");
				sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				sb.append("\ndescribe : ");
				sb.append("网络不同导致定位失败，请检查网络是否通畅");
			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				sb.append("\ndescribe : ");
				sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试试重启手机");
			}
			sb.append("\nlocationdescribe : ");// 位置语义化信�?            sb.append(location.getLocationDescribe());
			List<Poi> list = location.getPoiList();// POI信息
			if (list != null) {
				sb.append("\npoilist size = : ");
				sb.append(list.size());
				for (Poi p : list) {
					sb.append("\npoi= : ");
					sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
				}
			}
			logMsg(sb.toString());

			SharedUtils utils = new SharedUtils(ConfigApplication.this, IUV.local);
			utils.setStringValue("lng",String.valueOf(location.getLongitude()));
			utils.setStringValue("lat",String.valueOf(location.getLatitude()));

			showCity(location.getAddrStr(), location);
			System.out.println(sb.toString());
		}

	}
	public void showCity(String text,BDLocation location){
		try {
			if(cityText!=null){
				System.out.println("定位的当前城市是"+text);
				cityText.setText(text);
				cityText.setTag(location);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 显示请求字符
	 */
	public void logMsg(String str) {
		try {
			if (mLocationResult != null)
				mLocationResult.setText(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
