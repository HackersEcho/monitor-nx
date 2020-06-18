package com.dafang.monitor.nx.product.impl.evaluate.climateAccessment.util;

import com.dafang.monitor.nx.utils.jfreechart.util.ChartUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.dafang.monitor.nx.utils.DrawUtils.imageChangeBase64;

public class JFreeChartUtils {

    private static final Log log = LogFactory.getLog(JFreeChartUtils.class);
    private static String filePath = "F:/NX_file/kmlfile";//柱状图地址

    public static void main(String[] args) {
        DefaultCategoryDataset s = createDataset();
        DefaultCategoryDataset s2 = createPolylineDataset();
        DefaultCategoryDataset s3 = createPolylineDataset2();
//		createChart("6月全区平均气温距平逐年变化1", "", "", "气温距平(℃)", s, s2);
        createLineChar("6月全区平均气温距平逐年变化", "", "", "气温距平(℃)", s);
    }

    // 生成图表主对象JFreeChart
    public static String createChart(String fileName, String chartName, String XaxisName, String YaxisName,
                                     DefaultCategoryDataset linedataset, DefaultCategoryDataset linedataset2) {
        // 创建主题样式
        ChartUtils.setChartTheme();
        // 定义图表对象
        JFreeChart chart = ChartFactory.createBarChart(chartName, XaxisName, YaxisName, linedataset);
        chart.setBorderVisible(true);//边界线条是否可见
        // 3:设置抗锯齿，防止字体显示不清楚
        ChartUtils.setAntiAlias(chart);// 抗锯齿
        // 4:对柱子进行渲染
//		ChartUtils.setBarRenderer(chart.getCategoryPlot(), false);//
        // 5:对其他部分进行渲染
        ChartUtils.setXAixs(chart.getCategoryPlot());// X坐标轴渲染
        ChartUtils.setYAixs(chart.getCategoryPlot());// Y坐标轴渲染
        chart.getLegend().setFrame(new BlockBorder(Color.WHITE));// 设置标注无边框

        // =========== echo
        // 获取图表区域对象
        CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();
        // x轴 // 分类轴网格是否可见
        categoryPlot.setDomainGridlinesVisible(false);
        // y轴 //数据轴网格是否可见
        categoryPlot.setRangeGridlinesVisible(false);
        BarRenderer barRenderer = (BarRenderer) categoryPlot.getRenderer();
        /*
         * 放折线图数据
         */
        categoryPlot.setDataset(1, linedataset2);
        // 设置折线
        LineAndShapeRenderer lineandshaperenderer = new LineAndShapeRenderer();
        lineandshaperenderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        lineandshaperenderer.setBaseShapesVisible(false); // series 点（即数据点）可见
        lineandshaperenderer.setBaseLinesVisible(true); // series 点（即数据点）间有连线可见
        lineandshaperenderer.setSeriesStroke(0, new BasicStroke(2.0F));//设置折线大小
        lineandshaperenderer.setSeriesStroke(1, new BasicStroke(2.0F));//设置折线大小
        categoryPlot.setRenderer(1, lineandshaperenderer);
        // 折线在柱面前面显示
        categoryPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        OutputStream fos_jpg = null;
        try {
            fos_jpg = new FileOutputStream(filePath + fileName + ".png");
            ChartUtilities.writeChartAsPNG(fos_jpg, chart, 550, 250);
            fos_jpg.close();
        } catch (Exception e) {
            e.getMessage();
        }
        String res = "";
        try {
            res = imageChangeBase64(filePath+fileName+".png");
            log.info("图片转换 成功");
        } catch (Exception e) {
            log.info("图片转换 出错");
        }
        return res;
    }

    // 生成数据
    public static DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset linedataset = new DefaultCategoryDataset();
        // 各曲线名称
        String series = "气温距平";
        // 横轴名称(列名称)
        linedataset.addValue(0.0, series, "1961");
        linedataset.addValue(4.2, series, "1962");
        linedataset.addValue(3.9, series, "1963");
        linedataset.addValue(1.0, series, "1964");
        linedataset.addValue(5.2, series, "1965");
        linedataset.addValue(7.9, series, "1966");
        linedataset.addValue(2.0, series, "1967");
        linedataset.addValue(9.2, series, "1968");
        linedataset.addValue(8.9, series, "1969");
        linedataset.addValue(8.9, series, "1970");
        linedataset.addValue(0.0, series, "1971");
        linedataset.addValue(4.2, series, "1972");
        linedataset.addValue(3.9, series, "1973");
        linedataset.addValue(1.0, series, "1974");
        linedataset.addValue(5.2, series, "1975");
        linedataset.addValue(7.9, series, "1976");
        linedataset.addValue(2.0, series, "1977");
        linedataset.addValue(9.2, series, "1978");
        linedataset.addValue(8.9, series, "1979");
        linedataset.addValue(8.9, series, "1980");
        linedataset.addValue(0.0, series, "1981");
        linedataset.addValue(4.2, series, "1982");
        linedataset.addValue(3.9, series, "1983");
        linedataset.addValue(1.0, series, "1984");
        linedataset.addValue(5.2, series, "1985");
        linedataset.addValue(7.9, series, "1986");
        linedataset.addValue(2.0, series, "1987");
        linedataset.addValue(9.2, series, "1988");
        linedataset.addValue(8.9, series, "1989");
        linedataset.addValue(8.9, series, "1990");
        linedataset.addValue(0.0, series, "1991");
        linedataset.addValue(4.2, series, "1992");
        linedataset.addValue(3.9, series, "1993");
        linedataset.addValue(1.0, series, "1994");
        linedataset.addValue(5.2, series, "1995");
        linedataset.addValue(7.9, series, "1996");
        linedataset.addValue(2.0, series, "1997");
        linedataset.addValue(9.2, series, "1998");
        linedataset.addValue(8.9, series, "1999");

        // 各曲线名称
        series = "气温常年值";
        // 横轴名称(列名称)
        linedataset.addValue(5.0, series, "1961");
        linedataset.addValue(5.0, series, "1962");
        linedataset.addValue(5.0, series, "1963");
        linedataset.addValue(5.0, series, "1964");
        linedataset.addValue(5.0, series, "1965");
        linedataset.addValue(5.0, series, "1966");
        linedataset.addValue(5.0, series, "1967");
        linedataset.addValue(5.0, series, "1968");
        linedataset.addValue(5.0, series, "1969");
        linedataset.addValue(5.0, series, "1970");
        linedataset.addValue(5.0, series, "1971");
        linedataset.addValue(5.0, series, "1972");
        linedataset.addValue(5.0, series, "1973");
        linedataset.addValue(5.0, series, "1974");
        linedataset.addValue(5.0, series, "1975");
        linedataset.addValue(5.0, series, "1976");
        linedataset.addValue(5.0, series, "1977");
        linedataset.addValue(5.0, series, "1978");
        linedataset.addValue(5.0, series, "1979");
        linedataset.addValue(5.0, series, "1980");
        linedataset.addValue(5.0, series, "1981");
        linedataset.addValue(5.0, series, "1982");
        linedataset.addValue(5.0, series, "1983");
        linedataset.addValue(5.0, series, "1984");
        linedataset.addValue(5.0, series, "1985");
        linedataset.addValue(5.0, series, "1986");
        linedataset.addValue(5.0, series, "1987");
        linedataset.addValue(5.0, series, "1988");
        linedataset.addValue(5.0, series, "1989");
        linedataset.addValue(5.0, series, "1990");
        linedataset.addValue(5.0, series, "1991");
        linedataset.addValue(5.0, series, "1992");
        linedataset.addValue(5.0, series, "1993");
        linedataset.addValue(5.0, series, "1994");
        linedataset.addValue(5.0, series, "1995");
        linedataset.addValue(5.0, series, "1996");
        linedataset.addValue(5.0, series, "1997");
        linedataset.addValue(5.0, series, "1998");
        linedataset.addValue(5.0, series, "1999");

        // 各曲线名称
        series = "气温斜率";
        // 横轴名称(列名称)
        linedataset.addValue(2.0, series, "1961");
        linedataset.addValue(5.0, series, "1962");
        linedataset.addValue(5.0, series, "1963");
        linedataset.addValue(5.0, series, "1964");
        linedataset.addValue(5.0, series, "1965");
        linedataset.addValue(5.0, series, "1966");
        linedataset.addValue(5.0, series, "1967");
        linedataset.addValue(5.0, series, "1968");
        linedataset.addValue(5.0, series, "1969");
        linedataset.addValue(5.0, series, "1970");
        linedataset.addValue(6.0, series, "1971");
        linedataset.addValue(5.0, series, "1972");
        linedataset.addValue(5.0, series, "1973");
        linedataset.addValue(5.0, series, "1974");
        linedataset.addValue(5.0, series, "1975");
        linedataset.addValue(5.0, series, "1976");
        linedataset.addValue(5.0, series, "1977");
        linedataset.addValue(5.0, series, "1978");
        linedataset.addValue(5.0, series, "1979");
        linedataset.addValue(5.0, series, "1980");
        linedataset.addValue(5.0, series, "1981");
        linedataset.addValue(5.0, series, "1982");
        linedataset.addValue(5.0, series, "1983");
        linedataset.addValue(5.0, series, "1984");
        linedataset.addValue(5.0, series, "1985");
        linedataset.addValue(5.0, series, "1986");
        linedataset.addValue(5.0, series, "1987");
        linedataset.addValue(5.0, series, "1988");
        linedataset.addValue(5.0, series, "1989");
        linedataset.addValue(5.0, series, "1990");
        linedataset.addValue(5.0, series, "1991");
        linedataset.addValue(5.0, series, "1992");
        linedataset.addValue(5.0, series, "1993");
        linedataset.addValue(5.0, series, "1994");
        linedataset.addValue(5.0, series, "1995");
        linedataset.addValue(5.0, series, "1996");
        linedataset.addValue(5.0, series, "1997");
        linedataset.addValue(5.0, series, "1998");
        linedataset.addValue(5.0, series, "1999");

        return linedataset;
    }

    // 生成折线数据
    public static DefaultCategoryDataset createPolylineDataset() {
        DefaultCategoryDataset linedataset = new DefaultCategoryDataset();
        // 各曲线名称
        String series = "气温常年值";
        // 横轴名称(列名称)
        linedataset.addValue(5.0, series, "1961");
        linedataset.addValue(5.0, series, "1962");
        linedataset.addValue(5.0, series, "1963");
        linedataset.addValue(5.0, series, "1964");
        linedataset.addValue(5.0, series, "1965");
        linedataset.addValue(5.0, series, "1966");
        linedataset.addValue(5.0, series, "1967");
        linedataset.addValue(5.0, series, "1968");
        linedataset.addValue(5.0, series, "1969");
        linedataset.addValue(5.0, series, "1970");
        linedataset.addValue(5.0, series, "1971");
        linedataset.addValue(5.0, series, "1972");
        linedataset.addValue(5.0, series, "1973");
        linedataset.addValue(5.0, series, "1974");
        linedataset.addValue(5.0, series, "1975");
        linedataset.addValue(5.0, series, "1976");
        linedataset.addValue(5.0, series, "1977");
        linedataset.addValue(5.0, series, "1978");
        linedataset.addValue(5.0, series, "1979");
        linedataset.addValue(5.0, series, "1980");
        linedataset.addValue(5.0, series, "1981");
        linedataset.addValue(5.0, series, "1982");
        linedataset.addValue(5.0, series, "1983");
        linedataset.addValue(5.0, series, "1984");
        linedataset.addValue(5.0, series, "1985");
        linedataset.addValue(5.0, series, "1986");
        linedataset.addValue(5.0, series, "1987");
        linedataset.addValue(5.0, series, "1988");
        linedataset.addValue(5.0, series, "1989");
        linedataset.addValue(5.0, series, "1990");
        linedataset.addValue(5.0, series, "1991");
        linedataset.addValue(5.0, series, "1992");
        linedataset.addValue(5.0, series, "1993");
        linedataset.addValue(5.0, series, "1994");
        linedataset.addValue(5.0, series, "1995");
        linedataset.addValue(5.0, series, "1996");
        linedataset.addValue(5.0, series, "1997");
        linedataset.addValue(5.0, series, "1998");
        linedataset.addValue(5.0, series, "1999");
        return linedataset;
    }
    // 生成折线数据
    public static DefaultCategoryDataset createPolylineDataset2() {
        DefaultCategoryDataset linedataset = new DefaultCategoryDataset();
        // 各曲线名称
        String series = "气温斜率";
        // 横轴名称(列名称)
        linedataset.addValue(2.0, series, "1961");
        linedataset.addValue(5.0, series, "1962");
        linedataset.addValue(5.0, series, "1963");
        linedataset.addValue(5.0, series, "1964");
        linedataset.addValue(5.0, series, "1965");
        linedataset.addValue(5.0, series, "1966");
        linedataset.addValue(5.0, series, "1967");
        linedataset.addValue(5.0, series, "1968");
        linedataset.addValue(5.0, series, "1969");
        linedataset.addValue(5.0, series, "1970");
        linedataset.addValue(6.0, series, "1971");
        linedataset.addValue(5.0, series, "1972");
        linedataset.addValue(5.0, series, "1973");
        linedataset.addValue(5.0, series, "1974");
        linedataset.addValue(5.0, series, "1975");
        linedataset.addValue(5.0, series, "1976");
        linedataset.addValue(5.0, series, "1977");
        linedataset.addValue(5.0, series, "1978");
        linedataset.addValue(5.0, series, "1979");
        linedataset.addValue(5.0, series, "1980");
        linedataset.addValue(5.0, series, "1981");
        linedataset.addValue(5.0, series, "1982");
        linedataset.addValue(5.0, series, "1983");
        linedataset.addValue(5.0, series, "1984");
        linedataset.addValue(5.0, series, "1985");
        linedataset.addValue(5.0, series, "1986");
        linedataset.addValue(5.0, series, "1987");
        linedataset.addValue(5.0, series, "1988");
        linedataset.addValue(5.0, series, "1989");
        linedataset.addValue(5.0, series, "1990");
        linedataset.addValue(5.0, series, "1991");
        linedataset.addValue(5.0, series, "1992");
        linedataset.addValue(5.0, series, "1993");
        linedataset.addValue(5.0, series, "1994");
        linedataset.addValue(5.0, series, "1995");
        linedataset.addValue(5.0, series, "1996");
        linedataset.addValue(5.0, series, "1997");
        linedataset.addValue(5.0, series, "1998");
        linedataset.addValue(5.0, series, "1999");
        return linedataset;
    }

    /**
     * 折线图
     *
     * @param chartTitle
     * @param x
     * @param y
     * @param xyDataset
     * @param charName
     * @return
     */
    public static String createLineChar(String fileName, String chartName, String x, String y,
                                        CategoryDataset xyDataset) {

        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        // 设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));
        // 设置图例的字体
        standardChartTheme.setRegularFont(new Font("黑体", Font.BOLD, 15));
        // 设置轴向的字体
        standardChartTheme.setLargeFont(new Font("黑体", Font.BOLD, 15));
        ChartFactory.setChartTheme(standardChartTheme);
        JFreeChart chart = ChartFactory.createLineChart(fileName, x, y, xyDataset, PlotOrientation.VERTICAL, true, true,
                false);
        chart.getLegend().setFrame(new BlockBorder(Color.WHITE));// 设置标注无边框
        chart.setTextAntiAlias(false);
        chart.setBackgroundPaint(Color.WHITE);
        // 设置图标题的字体重新设置title
        Font font = new Font("隶书", Font.BOLD, 25);
        TextTitle title = new TextTitle();
        title.setFont(font);
        chart.setTitle(title);
        // 设置面板字体
        Font labelFont = new Font("SansSerif", Font.BOLD, 12);
        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
        // 图例字体设置
        LegendTitle legend = chart.getLegend();
        if (legend != null) {
            legend.setItemFont(new Font("宋体", Font.BOLD, 12));
        }
        // x轴 // 分类轴网格是否可见
        categoryplot.setDomainGridlinesVisible(false);
        // y轴 //数据轴网格是否可见
        categoryplot.setRangeGridlinesVisible(false);
        categoryplot.setRangeGridlinePaint(Color.lightGray);// 虚线色彩
        categoryplot.setDomainGridlinePaint(Color.lightGray);// 虚线色彩
        categoryplot.setBackgroundPaint(Color.WHITE);
        // 设置轴和面板之间的距离
        // categoryplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
        CategoryAxis domainAxis = categoryplot.getDomainAxis();
        domainAxis.setLabelFont(labelFont);// 轴标题
        domainAxis.setVisible(true);//坐标轴标尺值是否显示
        domainAxis.setTickLabelFont(labelFont);// 轴数值
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90); // 横轴上的
        // Lable
        // 45度倾斜
        // 设置距离图片左端距离
        domainAxis.setLowerMargin(0.0);
        // 设置距离图片右端距离
        domainAxis.setUpperMargin(0.0);
        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
//		numberaxis.setAutoRangeIncludesZero(true);
        // 获得renderer 注意这里是下嗍造型到lineandshaperenderer！！
        LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot.getRenderer();
        lineandshaperenderer.setBaseShapesVisible(false); // series 点（即数据点）可见
        lineandshaperenderer.setBaseLinesVisible(true); // series 点（即数据点）间有连线可见
        lineandshaperenderer.setUseFillPaint(true);
        // 显示折点数据
//		lineandshaperenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
//		lineandshaperenderer.setBaseItemLabelsVisible(true);

        // // 设置颜色-----------------------------------------------------------
        lineandshaperenderer.setSeriesStroke(0, new BasicStroke(2.0F));//设置折线大小
        lineandshaperenderer.setSeriesPaint(0, Color.blue);
        lineandshaperenderer.setSeriesStroke(1, new BasicStroke(1.0F));
        lineandshaperenderer.setSeriesPaint(1, Color.black);
        lineandshaperenderer.setSeriesStroke(2, new BasicStroke(2.0F));
        lineandshaperenderer.setSeriesPaint(2, Color.DARK_GRAY);

        FileOutputStream fos_jpg = null;
        try {
            fos_jpg = new FileOutputStream(filePath + fileName + ".png");
//			fos_jpg = new FileOutputStream(fileName + ".png");
            ChartUtilities.writeChartAsPNG(fos_jpg, chart, 540, 200);

        } catch (Exception e) {
            e.getMessage();
        }finally {
            try {
                fos_jpg.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String res = "";
        try {
            res = imageChangeBase64(filePath+fileName+".png");
            log.info("图片转换 成功");
        } catch (Exception e) {
            log.info("图片转换 出错");
        }
        return res;
    }
    public static String createChart1(String fileName, String chartName, String x, String y,
                                      CategoryDataset linedataset, CategoryDataset linedataset2) {

        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        // 设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));
        // 设置图例的字体
        standardChartTheme.setRegularFont(new Font("黑体", Font.BOLD, 15));
        // 设置轴向的字体
        standardChartTheme.setLargeFont(new Font("黑体", Font.BOLD, 15));
        ChartFactory.setChartTheme(standardChartTheme);
        JFreeChart chart = ChartFactory.createBarChart(fileName, x, y, linedataset, PlotOrientation.VERTICAL, true, true,
                false);
        chart.setTextAntiAlias(false);
        chart.setBackgroundPaint(Color.WHITE);
        chart.getLegend().setFrame(new BlockBorder(Color.WHITE));// 设置标注无边框
//		chart.setBorderVisible(true);//边界线条是否可见
        // 设置图标题的字体重新设置title
        Font font = new Font("隶书", Font.BOLD, 25);
        TextTitle title = new TextTitle();
        title.setFont(font);
        chart.setTitle(title);
        // 设置面板字体
        Font labelFont = new Font("SansSerif", Font.BOLD, 12);
        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
        // 图例字体设置
        LegendTitle legend = chart.getLegend();
        if (legend != null) {
            legend.setItemFont(new Font("宋体", Font.BOLD, 12));
        }
        // x轴 // 分类轴网格是否可见
        categoryplot.setDomainGridlinesVisible(false);
        // y轴 //数据轴网格是否可见
        categoryplot.setRangeGridlinesVisible(false);
        categoryplot.setRangeGridlinePaint(Color.lightGray);// 虚线色彩
        categoryplot.setDomainGridlinePaint(Color.lightGray);// 虚线色彩
        categoryplot.setBackgroundPaint(Color.WHITE);
        // 设置轴和面板之间的距离
        CategoryAxis domainAxis = categoryplot.getDomainAxis();
        domainAxis.setLabelFont(labelFont);// 轴标题
        domainAxis.setVisible(true);//坐标轴标尺值是否显示
        domainAxis.setTickLabelFont(labelFont);// 轴数值
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90); // 横轴上的
        // Lable
        // 45度倾斜
        // 设置距离图片左端距离
        domainAxis.setLowerMargin(0.0);
        // 设置距离图片右端距离
        domainAxis.setUpperMargin(0.0);
        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        numberaxis.setAutoRangeIncludesZero(true);
        // 获得renderer 注意这里是下嗍造型到lineandshaperenderer！！
//		LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot.getRenderer();
//		lineandshaperenderer.setBaseShapesVisible(false); // series 点（即数据点）可见
//		lineandshaperenderer.setBaseLinesVisible(true); // series 点（即数据点）间有连线可见
//		lineandshaperenderer.setUseFillPaint(true);

        // 获取图表区域对象
        CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();
        // x轴 // 分类轴网格是否可见
        categoryPlot.setDomainGridlinesVisible(false);
        // y轴 //数据轴网格是否可见
        categoryPlot.setRangeGridlinesVisible(true);
        BarRenderer barRenderer = (BarRenderer) categoryPlot.getRenderer();
//				CategoryAxis domainAxis = categoryPlot.getDomainAxis();
//				domainAxis.setVisible(true);//坐标轴是否显示
        /*
         * 放折线图数据
         */
        categoryPlot.setDataset(1, linedataset2);
        // 设置折线
        LineAndShapeRenderer lineandshaperenderer = new LineAndShapeRenderer();
        lineandshaperenderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        lineandshaperenderer.setBaseShapesVisible(false); // series 点（即数据点）可见
        lineandshaperenderer.setBaseLinesVisible(true); // series 点（即数据点）间有连线可见
        lineandshaperenderer.setSeriesStroke(0, new BasicStroke(2.0F));//设置折线大小
        lineandshaperenderer.setSeriesStroke(1, new BasicStroke(2.0F));//设置折线大小
        categoryPlot.setRenderer(1, lineandshaperenderer);
        // 折线在柱面前面显示
        categoryPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        FileOutputStream fos_jpg = null;
        try {
            fos_jpg = new FileOutputStream(filePath + fileName + ".png");
            ChartUtilities.writeChartAsPNG(fos_jpg, chart, 540, 200);
        } catch (Exception e) {
            e.getMessage();
        }finally {
            try {
                fos_jpg.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String res = "";
        try {
            res = imageChangeBase64(filePath+fileName+".png");
            log.info("图片转换 成功");
        } catch (Exception e) {
            log.info("图片转换 出错");
        }
        return res;
    }
}
