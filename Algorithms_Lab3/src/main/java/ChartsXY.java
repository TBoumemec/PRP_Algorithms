import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
 
public class ChartsXY {

	private static ChartsXY charts;
	private static ArrayList<XYSeriesCollection> datasetsAnalog = new ArrayList<XYSeriesCollection>();
	private static CombinedDomainXYPlot plot;
	private static JFreeChart chart;
	private static JFrame frame;
	private static XYSeries tempSeries;
	public static double timeStep = 0.00025;
	private static boolean fl = true;

	@SuppressWarnings("deprecation")
	public ChartsXY() {
		plot = new CombinedDomainXYPlot(new NumberAxis("Time, s"));

		chart = new JFreeChart(plot);
		chart.setBorderPaint(Color.black);
		chart.setBorderVisible(true);
		chart.setBackgroundPaint(Color.white);

		frame = new JFrame("Optimizer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new ChartPanel(chart));
		frame.setSize(1024, 768);
		frame.show();
	}

	public static void createAnalogChart(String name, int number) {
		if (charts == null) charts = new ChartsXY();

		XYSeriesCollection dataset = new XYSeriesCollection();
		NumberAxis rangeAxis = new NumberAxis(name);
		rangeAxis.setAutoRangeIncludesZero(false);
		XYPlot subplot = new XYPlot(dataset, null, rangeAxis, new StandardXYItemRenderer());
		subplot.setBackgroundPaint(Color.BLACK);
		plot.add(subplot);
		subplot.setWeight(7);
		datasetsAnalog.add(dataset);
		addSeries("Za", 0);
		addSeries("Zb", 0);
		addSeries("Zc", 0);
		addProtectiveField("Уставка", 0, 145);

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, false);
		renderer.setSeriesShapesVisible(0, true);
		subplot.setRenderer(renderer);
	}


	public static void addSeries(String name, int chartNumber) {
		XYSeries series = new XYSeries(name);
		series.add(0, 0);
		datasetsAnalog.get(chartNumber).addSeries(series);
	}

	public static void addProtectiveField(String name, int chartNumber, double flag) {
		XYSeries series = new XYSeries(name);
		series.add(flag, 0);
		datasetsAnalog.get(chartNumber).addSeries(series);

		double step = 1;
		double x, y;
		for (y = 0, x = flag - step; x < flag; y = calcY(y, flag, step), x = calcX(y, flag)) {
			series.add(x, y);

		}

	}

	private static double calcY(double mean, double flag, double step) {

		if (mean < flag & fl) {
			return mean + step;
		}
		else if(mean > -flag){
			fl = false;
			return mean - step;
		}
		fl = true;
		return mean + step;
	}

	private static double calcX(double y, double flag){
		double mean = Math.sqrt(Math.pow(flag, 2) - Math.pow(y, 2));
		if (fl) return mean;
		return -mean;
	}
 
  public static void addAnalogData(int chart, int series, double r, double x){
	  tempSeries = (XYSeries) datasetsAnalog.get(chart).getSeries().get(series);
	  tempSeries.add(r, x);
  }
  
}