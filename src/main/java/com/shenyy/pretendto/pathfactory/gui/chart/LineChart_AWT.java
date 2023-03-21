package com.shenyy.pretendto.pathfactory.gui.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.category.DefaultCategoryDataset;

public class LineChart_AWT extends ApplicationFrame {
    public static void main(String[] args) throws InterruptedException {
        LineChart_AWT chart = new LineChart_AWT(
                "School Vs Years",
                "Numer of Schools vs years");

        chart.pack();
        chart.setVisible(true);
        System.out.println("==========");
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            if (i % 2 == 0) {
                chart.updateChartTest(chart.createDataset());
            } else {
                chart.updateChartTest(chart.createDataset2());
            }
        }
        System.out.println("==========");
    }

    public String applicationTitle;
    public String chartTitle;
    public String rowKey;
    public JFreeChart lineChart;
    public ChartPanel chartPanel;

    public LineChart_AWT(String applicationTitle, String chartTitle) {
        super(applicationTitle);
        lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Years", "Number of Schools",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        setContentPane(chartPanel);
    }

    public LineChart_AWT(String applicationTitle, String chartTitle, String rowKey, double[] dataArr) {
        super(applicationTitle);
        this.applicationTitle = applicationTitle;
        this.chartTitle = chartTitle;
        this.rowKey = rowKey;

        lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Generation", "Length",
                createDataset(rowKey, dataArr),
                PlotOrientation.VERTICAL,
                true, true, false);

        chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        setContentPane(chartPanel);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(15, "schools", "1970");
        dataset.addValue(30, "schools", "1980");
        dataset.addValue(60, "schools", "1990");
        dataset.addValue(120, "schools", "2000");
        dataset.addValue(240, "schools", "2010");
        dataset.addValue(300, "schools", "2014");
        return dataset;
    }

    private DefaultCategoryDataset createDataset2() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(15, "schools", "1970");
        dataset.addValue(30, "schools", "1980");
        dataset.addValue(60, "schools", "1990");
        return dataset;
    }

    private DefaultCategoryDataset createDataset(String rowKey, double[] dataArr) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Integer i = 0; i < dataArr.length; i++) {
            dataset.addValue(dataArr[i], rowKey, i);
        }
        return dataset;
    }

    public void updateChart(double[] dataArr) {
        lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Generation", "Length",
                createDataset(rowKey, dataArr),
                PlotOrientation.VERTICAL,
                true, true, false);
        chartPanel.setChart(lineChart);
        setContentPane(chartPanel);
    }

    public void updateChartTest(DefaultCategoryDataset dataset) {
        lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Generation", "Length",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        chartPanel.setChart(lineChart);
        setContentPane(chartPanel);
    }
}
