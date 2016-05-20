package app.cn.aiyouv.www.utils;
import android.content.Context;
import android.content.SharedPreferences;
public class SharedUtils {
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	public SharedUtils(Context context, String NAME) {
		// TODO Auto-generated constructor stub
		preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		editor = preferences.edit();
	}


	/**
	 * 清空
	 */
	public void clear() {
		editor.clear();
		editor.commit();
	}

	/**
	 * 判断是否存在
	 *
	 * @param tag
	 * @return
	 */
	public boolean isHere(String tag) {
		return preferences.contains(tag);
	}

	/**
	 * 设置String类型的sharedpreferences
	 *
	 * @param key
	 * @return
	 */
	public void setStringValue(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}
	public void setIntValue(String key,int value){
		editor.putInt(key, value);
		editor.commit();
	}
	public int getIntValue(String key){
		return preferences.getInt(key, 0);
	}
	/**
	 * 获得String类型的sharedpreferences
	 *
	 * @param key
	 * @return
	 */
	public String getStringValue(String key) {
		return preferences.getString(key, "");
	}
	/**
	 * 获得金币
	 */
	public String getValue(String key) {
		return preferences.getString(key, "0");
	}

	/**
	 * 设置boolean类型的sharedpreferences
	 *
	 * @param key
	 * @return
	 */
	public void setBooleanValue(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 获得boolean类型的sharedpreferences
	 *
	 * @param key
	 * @return
	 */
	public Boolean getBooleanValue(String key) {
		// 默认为false
		return preferences.getBoolean(key, false);
	}
}
