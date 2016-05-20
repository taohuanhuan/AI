package app.cn.aiyouv.www.http;

import android.util.Log;

import com.loopj.android.http.RequestParams;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import app.cn.aiyouv.www.common.Common;
import app.cn.aiyouv.www.config.IUV;
import app.cn.aiyouv.www.utils.SharedUtils;

@SuppressWarnings("deprecation")
public class AsyncHttp extends As{
	
	@SuppressWarnings("unused")


	public static void get(String url, ResponHandler handler) {
		Log.i("hck", url);
		As.getInstance().setUserAgent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
		As.getInstance().setTimeout(60000);
		As.getInstance().get(url, handler);

	}
	public static void post(String url, RequestParams params,
			ResponHandler handler) {
//		As.getInstance().setUserAgent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
		As.getInstance().setTimeout(60000);

		As.getInstance().post(url, params, handler);

	}

	public static void post1(String url, Map<String,String> params,
							ResponHandler handler) {
		As.getInstance().setUserAgent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
		As.getInstance().setTimeout(6000);
		client.post(processParams(params, url), handler);

	}
	private final static String APPKEY = "0762222540";


	private static String processParams(Map<String, String> params, String url) {
		RequestParams request = new RequestParams();
		Set<String> keySet = params.keySet();
		Iterator<String> keyIterable = keySet.iterator();
		ArrayList<String> keys = new ArrayList<String>();
		while (keyIterable.hasNext()) {
			keys.add(keyIterable.next());
		}
		Collections.sort(keys);// 访问参数升序排列
		StringBuilder builder = new StringBuilder();
		builder.append(APPKEY);
		for (int i = 0; i < params.size(); i++) {
			String childKey = keys.get(i);
			String childValue = params.get(childKey);
			request.put(childKey, childValue);
			builder.append(childKey + childValue);
		}
		builder.append(Common.URL_SECRET);
		System.out.println("加密前:" + builder.toString());

		String sign = SHA1.getDigestOfString(builder.toString()).toUpperCase();
		System.out.println("sign:" + sign);
		request.put("sign", sign);
		request.put("appkey", APPKEY);
		/**
		 * 地址拼接
		 */
		params.put("sign", sign);
		params.put("appkey", APPKEY);
		Set<String> keySet1 = params.keySet();
		Iterator<String> keyIterable1 = keySet1.iterator();
		ArrayList<String> keys1 = new ArrayList<String>();
		while (keyIterable1.hasNext()) {
			keys1.add(keyIterable1.next());
		}
		Collections.sort(keys1);// 访问参数升序排列
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < params.size(); i++) {
			String childKey = keys1.get(i);
			String childValue = params.get(childKey);
			request.put(childKey, childValue);
			if (i == 0) {
				temp.append("?" + childKey + "=" + childValue);
			} else {
				temp.append("&" + childKey + "=" + childValue);
			}

		}
		System.out.println("地址" + url + temp.toString());
		return url + temp.toString();
		// return request;
	}

	@SuppressWarnings("unused")
	private void debug() {
		// 地址拼接

	}

}
