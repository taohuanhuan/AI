package app.cn.aiyouv.www.http;


import com.loopj.android.http.AsyncHttpClient;

public class As {
	public static AsyncHttpClient client ;
	public static AsyncHttpClient getInstance(){
		if(client==null){
			client = new AsyncHttpClient();
		}
		return client;
	}

	/*public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler handler) {
		As.getInstance().setUserAgent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
		As.getInstance().setTimeout(6000);
		As.getInstance().post(url, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				System.out.println("父类");
//				CommomP.getInstance(context)
			}
			@Override
			public void onSuccess(int arg0, String arg1) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0, arg1);
				System.out.println("父类0");
			}
		});
	}*/
}
