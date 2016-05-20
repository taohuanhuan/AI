package app.cn.aiyouv.www.widget;

public interface OnDeleteListioner {
	public abstract boolean isCandelete(int position);
	public abstract void onDelete(Object ID);
	public abstract void onBack();
}
