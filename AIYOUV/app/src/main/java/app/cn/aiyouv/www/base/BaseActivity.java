package app.cn.aiyouv.www.base;


import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;


/**
 * 封装Activity
 * */
public abstract class BaseActivity extends Activity {

	/**
	 * 改写父类方法，横竖屏转换不重新开始activity生命周期
	 * */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * 4.0的时候换回来
		 */
		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
		init();
	}
	/**
	 *
	 */
	private void init() {

	}
	 
	/**
	 * 重置
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// getWindow().getDecorView().setSystemUiVisibility(View.GONE);
		/**
		 * 设置为横屏
		 */

		super.onResume();
		 
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);

	}

}
