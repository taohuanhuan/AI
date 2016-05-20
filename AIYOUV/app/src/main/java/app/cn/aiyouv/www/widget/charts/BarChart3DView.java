package app.cn.aiyouv.www.widget.charts;


import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import org.xclcharts.chart.BarChart3D;
import org.xclcharts.chart.BarData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @ClassName
 * @Description  3D柱形图例子
 * @author  朱超
 */
public class BarChart3DView extends CommonView {

	private String TAG = "Bar3DChartView";
	private BarChart3D chart = new BarChart3D();
	//标签轴
	private List<String> chartLabels = new LinkedList<String>();
	//数据轴
	private List<BarData> BarDataset = new LinkedList<BarData>();
	Paint mPaintToolTip = new Paint(Paint.ANTI_ALIAS_FLAG);

	public BarChart3DView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub				
		initView();
	}

	public BarChart3DView(Context context, AttributeSet attrs){
		super(context, attrs);
		initView();
	}

	public BarChart3DView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView()
	{

		chartRender();
		//綁定手势滑动事件
		//this.bindTouch(this,chart);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//图所占范围大小
		chart.setChartRange(w,h);
	}
	/**
	 * 重新初始化
	 * @param title
	 * @param tip
	 * @param max
	 * @param min
	 * @param steps
	 * @param lab 标尺单位
	 * @param one 第一个数据
	 * @param two 第二个数据
	 * @param oneTip 第一个数据名字
	 * @param twoTip 第二个数据名字
	 */
	public void setInit(String title,String tip,double max,double min,double steps,final String lab,double one,double two,double three,String oneTip,String twoTip,String threeTip){
		chart.setTitle(title);
		chart.addSubtitle(tip);
		chart.getDataAxis().setAxisMax(max);
		chart.getDataAxis().setAxisMin(min);
		chart.getDataAxis().setAxisSteps(steps);
		chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {

			@Override
			public String textFormatter(String value) {
				// TODO Auto-generated method stub							
				Double tmp = Double.parseDouble(value);
				DecimalFormat df = new DecimalFormat("#0");
				String label = df.format(tmp).toString();
				return (label + lab);
			}

		});
		chartDataSet(one, two, three, oneTip, twoTip, threeTip);
	}
	public void setInitTwo(String title,String tip,double max,double min,double steps,final String lab,double one,double two,String oneTip,String twoTip){
		chart.setTitle(title);
		chart.addSubtitle(tip);
		chart.getDataAxis().setAxisMax(max);
		chart.getDataAxis().setAxisMin(min);
		chart.getDataAxis().setAxisSteps(steps);
		chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {

			@Override
			public String textFormatter(String value) {
				// TODO Auto-generated method stub
				Double tmp = Double.parseDouble(value);
				DecimalFormat df = new DecimalFormat("#0");
				String label = df.format(tmp).toString();
				return (label + lab);
			}

		});
		chartDataSetTwo(one, two, oneTip, twoTip);
	}
	private void chartRender()
	{
		try {
			//设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....	
			int [] ltrb = getBarLnDefaultSpadding();
			chart.setPadding( DensityUtil.dip2px(getContext(), 50),ltrb[1],
					ltrb[2], ltrb[3] + DensityUtil.dip2px(getContext(), 25) );
			//显示边框
			chart.showRoundBorder();
			//数据源			
			chart.setDataSource(BarDataset);
			chart.setCategories(chartLabels);
			//坐标系
			//chart.getCategoryAxis().setAxisTickLabelsRotateAngle(-45f);
			//隐藏轴线和tick
			chart.getDataAxis().hideAxisLine();
			//chart.getDataAxis().setTickMarksVisible(false);
			//标题
			chart.setTitleAlign(XEnum.HorizontalAlign.RIGHT);
			//背景网格
			chart.getPlotGrid().showHorizontalLines();
			chart.getPlotGrid().showVerticalLines();
			chart.getPlotGrid().showEvenRowBgColor();
			chart.getPlotGrid().showOddRowBgColor();
			//定义数据轴标签显示格式		
			chart.getDataAxis().setTickLabelRotateAngle(-45);
			chart.getDataAxis().getTickMarksPaint().
					setColor(Color.rgb(186, 20, 26));
			//设置标签轴标签交错换行显示
			chart.getCategoryAxis().setLabelLineFeed(XEnum.LabelLineFeed.EVEN_ODD);
			//定义标签轴标签显示格式
			chart.getCategoryAxis().setLabelFormatter(new IFormatterTextCallBack(){

				@Override
				public String textFormatter(String value) {
					// TODO Auto-generated method stub									
					return value;
				}

			});
			//定义柱形上标签显示格式
			chart.getBar().setItemLabelVisible(true);
			chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
				@Override
				public String doubleFormatter(Double value) {
					// TODO Auto-generated method stub
					DecimalFormat df=new DecimalFormat("#0.00");
					String label = df.format(value).toString();
					return label;
				}});

			//激活点击监听
			chart.ActiveListenItemClick();
			//仅能横向移动
			chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);
			//扩展横向显示范围
			//	chart.getPlotArea().extWidth(200f);
			//标签文字与轴间距
			chart.getCategoryAxis().setTickLabelMargin(5);
			//不使用精确计算，忽略Java计算误差
			chart.disableHighPrecision();
			// 设置轴标签字体大小
			chart.getDataAxis().getTickLabelPaint().setTextSize(26);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void chartDataSet(double one,double two,double three,String oneTip,String twoTip,String threeTip)
	{
		//标签对应的柱形数据集
		List<Double> dataSeriesA= new LinkedList<Double>();
		dataSeriesA.add(one);
		dataSeriesA.add(0d);
		dataSeriesA.add(0d);

		List<Double> dataSeriesC= new LinkedList<Double>();
		dataSeriesC.add(0d);
		dataSeriesC.add(two);
		dataSeriesC.add(0d);
		List<Double> dataSeriesD= new LinkedList<Double>();
		dataSeriesD.add(0d);
		dataSeriesD.add(0d);
		dataSeriesD.add(three);
		BarDataset.clear();
		BarDataset.add(new BarData(oneTip, dataSeriesA, Color.rgb(252, 210, 9)));
		BarDataset.add(new BarData(twoTip,dataSeriesC,Color.rgb(55, 144, 206)));
		BarDataset.add(new BarData(twoTip, dataSeriesD, Color.rgb(55, 12, 206)));
		chartLabels.clear();
		chartLabels.add(oneTip);
		chartLabels.add(twoTip);
		chartLabels.add(threeTip);

	}
	private void chartDataSetTwo(double one,double two,String oneTip,String twoTip)
	{
		//标签对应的柱形数据集
		List<Double> dataSeriesA= new LinkedList<Double>();
		dataSeriesA.add(one);
		dataSeriesA.add(0d);

		List<Double> dataSeriesC= new LinkedList<Double>();
		dataSeriesC.add(0d);
		dataSeriesC.add(two);

		BarDataset.clear();
		BarDataset.add(new BarData(oneTip, dataSeriesA, Color.rgb(252, 210, 9)));
		BarDataset.add(new BarData(twoTip,dataSeriesC,Color.rgb(55, 144, 206)));
		chartLabels.clear();
		chartLabels.add(oneTip);
		chartLabels.add(twoTip);

	}
	@Override
	public void render(Canvas canvas) {
		try{
			chart.render(canvas);
		} catch (Exception e){
			Log.e(TAG, e.toString());
		}
	}



}
