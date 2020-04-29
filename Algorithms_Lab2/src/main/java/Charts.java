import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYStepAreaRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Class for graphic visualisation current data and protection logic
 */
public class Charts {

	// https://www.cs.cmu.edu/~pattis/15-1XX/15-200/lectures/linkedlists/index.html
	private static Charts charts; // что это
	private static ArrayList<XYSeriesCollection> datasetsAnalog = new ArrayList<XYSeriesCollection>();
	private static ArrayList<XYSeries> datasetsDiscrete = new ArrayList<XYSeries>();
	private static CombinedDomainXYPlot plot;
	private static XYSeries tempSeries;
	private static double currentTime = 0.0;
	private static boolean lastData = false;

	private Charts() {

		JFrame frame = new JFrame("Protection signals"); // имя рамки
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // окончание программы при закрытии

		plot = new CombinedDomainXYPlot(new NumberAxis("Time, sec"));
		JFreeChart chart = new JFreeChart("Protection signals", plot);

		chart.setBackgroundPaint(Color.white); // цвет фона рамки

		frame.getContentPane().add(new ChartPanel(chart)); // добавление макета графика в рамку
		frame.setSize(1280, 800);
		frame.show();
	}

	/**
	 * Добавляет аналоговый график
	 *
	 * @param name   - Имя графика
	 * @param number - Порядковый номер
	 */
	static void createAnalogChart(String name, int number) {
		if (charts == null) charts = new Charts(); // если объекта класса нет, то он создается

		XYSeriesCollection dataset = new XYSeriesCollection();
		NumberAxis rangeAxis = new NumberAxis(name); // добавление легенды
		rangeAxis.setAutoRangeIncludesZero(false); // автоматический масштаб?
		XYPlot subplot = new XYPlot(dataset, null, rangeAxis, new StandardXYItemRenderer()); // маска графика
		subplot.setBackgroundPaint(Color.black); // установить сетку на задний
		plot.add(subplot); // добавление маски
		subplot.setWeight(7); // высота рамки построения графика
		datasetsAnalog.add(dataset);

		addSeries(name + " (sv)", number, 0);
		addSeries(name + " (rms)", number, 1);
		addSeries(name + " Уставка", number, 2);
	}

	/**
	 * Добавляет дискретный график
	 *
	 * @param name   - Имя графика
	 * @param number - Порядковый номер
	 */
	public static void createDiscreteChart(String name, int number) {
		if (charts == null) charts = new Charts();

		XYSeriesCollection dataset = new XYSeriesCollection();
		NumberAxis rangeAxis = new NumberAxis(name);
		rangeAxis.setAutoRangeIncludesZero(false);
		XYPlot subplot = new XYPlot(dataset, null, rangeAxis, new StandardXYItemRenderer());
		plot.add(subplot);
		subplot.setWeight(1);
		XYStepAreaRenderer xysteparearenderer = new XYStepAreaRenderer(2);
		subplot.setRenderer(xysteparearenderer);
		XYSeries series = new XYSeries(name);
		datasetsDiscrete.add(series);
		dataset.addSeries(series);
	}


	/**
	 * Добавляет в указанный аналоговый график новый сигнал
	 *
	 * @param name        - Имя аналогового сигнала
	 * @param chartNumber - Порядковый номер сигнала
	 * @param number      - Порядковый номер
	 */
	public static void addSeries(String name, int chartNumber, int number) {
		XYSeries series = new XYSeries(name);
		series.add(0.0, 0.0);
		datasetsAnalog.get(chartNumber).addSeries(series);
	}

	/**
	 * Строит Аналоговый сигнал на графике
	 *
	 * @param chart  - Порядковый номер графика
	 * @param series - Порядковый номер сигнала
	 * @param data   - Добавляемое значение (double)
	 */
	public static void addAnalogData(int chart, int series, double data) {
		tempSeries = (XYSeries) datasetsAnalog.get(chart).getSeries().get(series);
		// Шаг дискретизации при 80 т. за период; 0.001 при 20 т. за период
		double timeStep = 0.025;
		currentTime = tempSeries.getMaxX() + timeStep;
		tempSeries.add(currentTime, data);
	}

	/**
	 * Строит Дискретный сигнал на графике
	 *
	 * @param chart - Порядковый номер дисретного сигнала
	 * @param data  - Добавляемый значение (true/false)
	 */
	public static void addDiscreteData(int chart, boolean data) {
		tempSeries = (XYSeries) datasetsDiscrete.get(chart);
		if (!tempSeries.isEmpty()) lastData = tempSeries.getY(tempSeries.getItemCount() - 1).doubleValue() == 1;
		if (!lastData && data) tempSeries.add(currentTime, data ? 2.0 : 0.0);
		if (lastData && !data) tempSeries.add(currentTime, data ? 2.0 : 0.0);
	}

}