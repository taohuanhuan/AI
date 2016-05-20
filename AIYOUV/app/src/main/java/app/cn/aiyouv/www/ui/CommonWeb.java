package app.cn.aiyouv.www.ui;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.TextView;

import app.cn.aiyouv.www.R;
import app.cn.aiyouv.www.base.AppManager;
import app.cn.aiyouv.www.base.BaseActivity;

public class CommonWeb extends BaseActivity {
	private WebView commonView;
	private TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_webview);
		findViewById(R.id.title_top_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AppManager.getAppManager().finishActivity();
			}
		});
		title = (TextView) findViewById(R.id.title);
		
		commonView = (WebView) findViewById(R.id.common_web);
		commonView.getSettings().setBuiltInZoomControls(false);
		commonView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				return true;
			}
		});
		commonView.getSettings().setJavaScriptEnabled(true);
		WebViewClient webViewClient = new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
			 
					 commonView.loadUrl(url);
				
				 return true;
			}
		};
		WebChromeClient webChromeClient = new WebChromeClient(){
			public void onReceivedTitle(WebView view, String tl) {
				
			};
		};
		commonView.setWebChromeClient(webChromeClient);
		commonView.setWebViewClient(webViewClient);

		if(getIntent().hasExtra("title")){
			title.setText(getIntent().getStringExtra("title"));
		}
		if(getIntent().hasExtra("url")){
			commonView.loadUrl(getIntent().getStringExtra("url"));
		}else if (getIntent().hasExtra("content")) {
			
			commonView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			commonView.getSettings().setLoadWithOverviewMode(true);
//			tv_detail.loadData(details, "text/html", "UTF-8");
			
			Document doc_Dis = Jsoup.parse(getIntent().getStringExtra("content"));
			Elements ele_Img = doc_Dis.getElementsByTag("img");
			if (ele_Img.size() != 0) {
				for (Element e_Img : ele_Img) {
					e_Img.attr("style", "width:100%");
				}
			}
			String newHtmlContent = doc_Dis.toString();
			 commonView.loadDataWithBaseURL("", newHtmlContent, "text/html", "UTF-8",null);
		}else if(getIntent().hasExtra("file")){
			commonView.loadUrl(getIntent().getStringExtra("file"));
		}
	}
}
