package app.cn.aiyouv.www.pic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;
import app.cn.aiyouv.www.common.Common;
import app.cn.aiyouv.www.widget.InScrollGridView;
import app.cn.aiyouv.www.widget.NewDataToast;

public class PhotoSelectActivity extends BaseActivity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	private   int WDITH;

	// ArrayList<Entity> dataList;// 〃
	List<ImageItem> dataList;
	GridView gridView;
	ImageGridAdapter adapter;//
	AlbumHelper helper;
	Button bt;
	private TextView cancel;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				NewDataToast.makeText(PhotoSelectActivity.this,"最多选择9张图").show();
				break;

			default:
				break;
			}
		}
	};
	private  final int SELECT_PHOTO=0x000001;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.photo_select_layout);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		dataList = (List<ImageItem>) getIntent().getSerializableExtra(
				EXTRA_IMAGE_LIST);

		initView();
		bt = (Button) findViewById(R.id.bt);
		bt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ArrayList<String> list = new ArrayList<String>();
				Collection<String> c = adapter.map.values();
				Iterator<String> it = c.iterator();
				for (; it.hasNext();) {
					list.add(it.next());
				}

				if (Bimp.act_bool) {
					/*Intent intent = new Intent(PhotoSelectActivity.this,
							PublishedActivity.class);
					startActivity(intent);*/
					Bimp.act_bool = false;
				}
				for (int i = 0; i < list.size(); i++) {
					if (Bimp.drr.size() < Common.MAX_IMAGES) {
						Bimp.drr.add(list.get(i));
					}
				}
				Intent intent = new Intent();
				setResult(SELECT_PHOTO);
				AppManager.getAppManager().finishActivity();
			}

		});
	}

	/**
	 *
	 */
	private void initView() {
		cancel = (TextView) findViewById(R.id.cancel);
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		WDITH = (dm.widthPixels-50)/3;
		adapter = new ImageGridAdapter(PhotoSelectActivity.this, dataList,
				mHandler,WDITH);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new ImageGridAdapter.TextCallback() {
			public void onListen(int count) {
				bt.setText("完成" + "(" + count + ")");
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				// if(dataList.get(position).isSelected()){
				// dataList.get(position).setSelected(false);
				// }else{
				// dataList.get(position).setSelected(true);
				// }

				adapter.notifyDataSetChanged();
			}

		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(PhotoSelectActivity.this,
						PhotoViewActivity.class);
				startActivity(intent);
				AppManager.getAppManager().finishActivity();
			}
		});

	}
}
