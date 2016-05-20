package app.cn.aiyouv.www.http;

import android.app.Activity;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import app.cn.aiyouv.www.widget.Loading;

public class ResponHandler extends AsyncHttpResponseHandler {
    private Activity context;

    public ResponHandler() {
        // TODO Auto-generated constructor stub

    }

    public ResponHandler(Activity context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        //如果需要转动的标记，使用此构造方法
    }

    @Override
    public void onStart() {
        super.onStart();
        if (context != null) {

            Loading.showSheet(context);
        }

    }

    @Override
    public void onFinish() {
        super.onFinish();
        /*if(context!=null){
            Loading.getInstance().clear();
		}*/
    }

    @Override
    public void onFailure(Throwable throwable, String s) {
        super.onFailure(throwable, s);
        if (s != null) {
            if (s.equals("can't resolve host")) {
                if (context != null) {
                    Toast.makeText(context, "无网络", Toast.LENGTH_SHORT).show();
                    Loading.clear();
                }
            }
        }

    }

    @Override
    public void onSuccess(int index, String result) {
        super.onSuccess(index, result);
        if (context != null) {
            Loading.clear();
        }
       /* if (result == null) {
            return;
        }*/
    }
}
