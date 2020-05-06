package Charts;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class VectorChart {

    // https://www.cs.cmu.edu/~pattis/15-1XX/15-200/lectures/linkedlists/index.html
    private static VectorChart charts; // что это
    private static ArrayList<XYSeriesCollection> datasetsAnalog = new ArrayList<XYSeriesCollection>();
    private static ArrayList<XYSeries> datasetsDiscrete = new ArrayList<XYSeries>();
    private static CombinedDomainXYPlot plot;
    private static double currentTime = 0.0;
    private static boolean lastData = false;
    static double datX, datY = 10;

    VectorChart(String name) {

        JFrame frame = new JFrame("Vector diagram"); // имя рамки
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // окончание программы при закрытии

        plot = new CombinedDomainXYPlot(new NumberAxis("Real"));
        JFreeChart chart = new JFreeChart(name, plot);

        chart.setBackgroundPaint(Color.white); // цвет фона рамки

        frame.getContentPane().add(new ChartPanel(chart)); // добавление макета графика в рамку
        frame.setSize(1280, 800);
        frame.show();
    }

    public static void createVectorChart(String name) {
        if (charts == null) charts = new VectorChart(name); // если объекта класса нет, то он создается

        XYSeriesCollection dataset = new XYSeriesCollection();
        NumberAxis rangeAxis = new NumberAxis("ImagineAxis"); // добавление легенды
        rangeAxis.setAutoRangeIncludesZero(false); // автоматический масштаб?
        XYPlot subplot = new XYPlot(dataset, null, rangeAxis, new StandardXYItemRenderer()); // маска графика
        subplot.setBackgroundPaint(Color.black); // установить сетку на задний
        plot.add(subplot); // добавление маски
        subplot.setWeight(7); // высота рамки построения графика
        datasetsAnalog.add(dataset);

        addSeries("IA");
        addSeries("IB");
        addSeries("IC");

        addSeries("Ia");
        addSeries("Ib");
        addSeries("Ic");

        addSeries("dIa");
        addSeries("dIb");
        addSeries("dIc");

    }

    public static void addSeries(String name) {
        XYSeries series = new XYSeries(name);
        series.add(0.0, 0.0);
        series.add(1, 1);
//        System.out.println(series.getDataItem(1));
//        System.out.println(series.getDataItem(0));
        datasetsAnalog.get(0).addSeries(series);
    }

    /**
     * добавление вектора на диаграмму
     * @param chart номер окна
     * @param series номер класса вектора
     * @param dataX действительная часть
     * @param dataY мнимая часть
     */
    public static void addVector(int chart, int series, double dataX, double dataY) {
        XYSeries tempSeries = (XYSeries) datasetsAnalog.get(chart).getSeries().get(series);
//        XYSeries tempSeries = new XYSeries(series);
        tempSeries.remove(0);
        tempSeries.remove(0);

        tempSeries.add(dataX, dataY);
        tempSeries.add(0,0);


    }
}
