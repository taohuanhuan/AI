/**
 * Copyright 2014  XCL-Charts
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @version 1.0
 */
package app.cn.aiyouv.www.widget.charts;

import java.util.ArrayList;

import org.xclcharts.chart.PieChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotLegend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import app.cn.aiyouv.www.bean.DataBean;
import app.cn.aiyouv.www.common.C;
import app.cn.aiyouv.www.widget.charts.CommonView;

/**
 * @ClassName
 * @Description  饼图
 */
public class PieChartView extends PieView {

	private String TAG = "PieChart01View";
	private PieChart chart = new PieChart();
	private ArrayList<PieData> chartData = new ArrayList<PieData>();
	private int mSelectedID = -1;


	public PieChartView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	public PieChartView(Context context, AttributeSet attrs){
		super(context, attrs);
		initView();
	}

	public PieChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView()
	{
		chartRender();
		chartAnimation();
		//綁定手势滑动事件
//			new Thread(this).start();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//图所占范围大小
		chart.setChartRange(w, h);
	}
	public void setInit(String title,String tip){
		chart.setTitle(title);
		chart.addSubtitle(tip);
	}
	public   int getColor(){

		return (int) ( Math.random() * 200);
	}
	public void  putData(ArrayList<DataBean> beans,double max){
		for(int i=0;i<beans.size();i++){
			try {
				double x =   Double.parseDouble( beans.get(i).getValue())/max*100;
				C.p("-->"+x);
				chartData.add(new PieData(beans.get(i).getKey(),beans.get(i).getKey()+"["+beans.get(i).getValue()+"元]" ,  x,Color.rgb(getColor(), getColor(), getColor())));
			}catch (Exception e){

			}
		}
	}
	private void chartRender()
	{
		try {

			//设置绘图区默认缩进px值
			int [] ltrb = getPieDefaultSpadding();
//			float right = DensityUtil.dip2px(getContext());
			chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

			//设置起始偏移角度(即第一个扇区从哪个角度开始绘制)
			//chart.setInitialAngle(90);	

			//标签显示(隐藏，显示在中间，显示在扇区外面)
			chart.setLabelStyle(XEnum.SliceLabelStyle.INSIDE);
			chart.getLabelPaint().setColor(Color.WHITE);
			//标题
			chart.setTitleVerticalAlign(XEnum.VerticalAlign.BOTTOM);
			//chart.setDataSource(chartData);

			//激活点击监听
			chart.ActiveListenItemClick();
			chart.showClikedFocus();

			//设置允许的平移模式
			//chart.enablePanMode();
			//chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);	

			//显示图例
			PlotLegend legend = chart.getPlotLegend();
			legend.show();
			legend.setType(XEnum.LegendType.ROW);
			legend.setHorizontalAlign(XEnum.HorizontalAlign.CENTER);
			legend.setVerticalAlign(XEnum.VerticalAlign.BOTTOM);
			legend.showBox();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}



	@Override
	public void render(Canvas canvas) {
		try{
			chart.render(canvas);
		} catch (Exception e){
			Log.e(TAG, e.toString());
		}
	}




	private void chartAnimation()
	{
		try {

			chart.setDataSource(chartData);
			int count = 360 / 10;

			for(int i=1;i<count ;i++)
			{
//	          		Thread.sleep(100);

				chart.setTotalAngle(10 * i);

				//激活点击监听
				if(count - 1 == i)
				{
					chart.setTotalAngle(360);

					chart.ActiveListenItemClick();
					//显示边框线，并设置其颜色
					chart.getArcBorderPaint().setColor(Color.YELLOW);
					chart.getArcBorderPaint().setStrokeWidth(1);
				}

				postInvalidate();
			}

		}
		catch(Exception e) {

		}

	}


}
