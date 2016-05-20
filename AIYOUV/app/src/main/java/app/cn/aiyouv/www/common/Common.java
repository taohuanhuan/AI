package app.cn.aiyouv.www.common;

import android.os.Environment;

import com.baidu.location.LocationClientOption;
import com.qualcomm.vuforia.DataSet;

public class Common {
	public static final String VIEW_NUM = "5";
	public static final String LOCATIONNAME = "aiyouv_localtion";
	public static final String SD_CARD=Environment
			.getExternalStorageDirectory().getAbsolutePath();
	public static  final String AIYOUV_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/AIYOUV/";
	public static final String URL_SECRET = "";
	public static final String AIYOUV_CACHE="VQ/";//高通
	public static final String AIYOUV_INDEX = "INDEX";

	public static final String ADVER_CACHE="ADVER/";//广告、
	public static final String BAIDU_CACHE = "BAIDU/";//百度
	public static final String VUFORIA_DOWNLOAD="vuforia_download";
	public static final int MAX_IMAGES = 6;//选择图片的最大数值
	//------------地址定位
	public static final String COOR_COUNT = "gcj02";
	public static final LocationClientOption.LocationMode LOCATIONMODE = LocationClientOption.LocationMode.Hight_Accuracy;
	public static final String WX_ID = "wx41cac4bfc027fd3e";

}
