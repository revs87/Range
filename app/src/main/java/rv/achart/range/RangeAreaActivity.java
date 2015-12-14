package rv.achart.range;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class RangeAreaActivity extends Activity {

    private View mChart;

    private int backgroundColor;
    private int linesColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_area);

        openChart();
    }

    private void openChart() {
        int[] x = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        int[] buy = {2200, 2500, 2700, 3000, 2800, 3500, 3700, 3800, 0, 0,
                0, 0};
        int[] sell = {2000, 2700, 2900, 2800, 2600, 3000, 3300, 3400, 0, 0,
                0, 0};
        int max = 3800; //buy
        int min = 2000; //sell
        backgroundColor = Color.parseColor("#FAFAFA");
        linesColor = Color.parseColor("#C82059");

        // Creating an XYSeries for buy
        XYSeries buySeries = new XYSeries("buy");
        // Creating an XYSeries for sell
        XYSeries sellSeries = new XYSeries("sell");
        // Adding data to buy and sell Series
        for (int i = 0; i < x.length; i++) {
            buySeries.add(i, buy[i]);
            sellSeries.add(i, sell[i]);
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(buySeries);
        dataset.addSeries(sellSeries);

        XYSeriesRenderer buyRenderer = getBuyRenderer();
        XYSeriesRenderer sellRenderer = getSellRenderer();

        XYMultipleSeriesRenderer multiRenderer = getXyMultipleSeriesRenderer(x);

        // Adding buyRenderer and sellRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to
        // multipleRenderer
        // should be same
        multiRenderer.addSeriesRenderer(buyRenderer);
        multiRenderer.addSeriesRenderer(sellRenderer);

        /* Range fill */
        XYSeriesRenderer xyRenderer = (XYSeriesRenderer) multiRenderer.getSeriesRendererAt(0);
        XYSeriesRenderer.FillOutsideLine fill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
        fill.setColor(Color.WHITE);
        xyRenderer.addFillOutsideLine(fill);

        XYSeriesRenderer xyRenderer2 = (XYSeriesRenderer) multiRenderer.getSeriesRendererAt(1);
        XYSeriesRenderer.FillOutsideLine fill2 = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
        fill2.setColor(backgroundColor);
        xyRenderer2.addFillOutsideLine(fill2);

        /* Populate */
        populateChart(dataset, multiRenderer);
    }

    private void populateChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer multiRenderer) {
        // this part is used to display graph on the xml
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart);
        // remove any views before u paint the chart
        chartContainer.removeAllViews();
        // drawing bar chart
        mChart = ChartFactory.getLineChartView(RangeAreaActivity.this, dataset,
                multiRenderer);
        // adding the view to the linearlayout
        chartContainer.addView(mChart);
    }

    @NonNull
    private XYSeriesRenderer getSellRenderer() {
        XYSeriesRenderer sellRenderer = new XYSeriesRenderer();
        sellRenderer.setColor(linesColor);
        sellRenderer.setFillPoints(true);
        sellRenderer.setLineWidth(1.5f);
        sellRenderer.setDisplayChartValues(false);
        sellRenderer.setPointStyle(PointStyle.POINT);
        sellRenderer.setStroke(BasicStroke.SOLID);
        sellRenderer.setShowLegendItem(false);
        return sellRenderer;
    }

    @NonNull
    private XYSeriesRenderer getBuyRenderer() {
        XYSeriesRenderer buyRenderer = new XYSeriesRenderer();
        buyRenderer.setColor(linesColor);
        buyRenderer.setFillPoints(true);
        buyRenderer.setLineWidth(1.5f);
        buyRenderer.setDisplayChartValues(false);
        buyRenderer.setPointStyle(PointStyle.POINT);
        buyRenderer.setStroke(BasicStroke.SOLID);
        buyRenderer.setShowLegendItem(false);
        XYSeriesRenderer.FillOutsideLine fill =
                new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.ABOVE
                );

        fill.setColor(backgroundColor);
        buyRenderer.addFillOutsideLine(fill);
        return buyRenderer;
    }

    @NonNull
    private XYMultipleSeriesRenderer getXyMultipleSeriesRenderer(int[] x) {
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);

        /***
         * Customizing graphs
         */
        // setting text size of the title
        multiRenderer.setChartTitleTextSize(0);
        // setting text size of the axis title
        multiRenderer.setAxisTitleTextSize(0);
        // setting text size of the graph lable
        multiRenderer.setLabelsTextSize(0);
        // setting zoom buttons visiblity
        multiRenderer.setZoomButtonsVisible(false);
        // setting pan enablity which uses graph to move on both axis
        multiRenderer.setPanEnabled(false, false);
        // setting click false on graph
        multiRenderer.setClickEnabled(false);
        // setting zoom to false on both axis
        multiRenderer.setZoomEnabled(false, false);
        // setting lines to display on y axis
        multiRenderer.setShowGridY(false);
        // setting lines to display on x axis
        multiRenderer.setShowGridX(false);
        // setting legend to fit the screen size
        multiRenderer.setFitLegend(false);
        // setting displaying line on grid
        multiRenderer.setShowGrid(false);
        // setting zoom to false
        multiRenderer.setZoomEnabled(false);
        // setting external zoom functions to false
        multiRenderer.setExternalZoomEnabled(false);
        // setting displaying lines on graph to be formatted(like using
        // graphics)
        multiRenderer.setAntialiasing(true);
        // setting to in scroll to false
        multiRenderer.setInScroll(false);
        // setting to set legend height of the graph
        multiRenderer.setLegendHeight(0);
        // setting x axis label align
        multiRenderer.setXLabelsAlign(Align.CENTER);
        // setting y axis label to align
        multiRenderer.setYLabelsAlign(Align.LEFT);
        // setting text style
        multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
        // setting no of values to display in y axis
        multiRenderer.setYLabels(0);
        // setting y axis max value, Since i'm using static values inside the
        // graph so i'm setting y max value to 4000.
        // if you use dynamic values then get the max y value and set here
//        multiRenderer.setYAxisMax(4000);
        // setting used to move the graph on xaxiz to .5 to the right
        multiRenderer.setXAxisMin(0);
        // setting used to move the graph on xaxiz to .5 to the right
//        multiRenderer.setXAxisMax(11);
        // setting bar size or space between two bars
        // multiRenderer.setBarSpacing(0.5);
        // Setting background color of the graph to transparent
        multiRenderer.setBackgroundColor(backgroundColor);
        // Setting margin color of the graph to transparent
        multiRenderer.setMarginsColor(backgroundColor);
        multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setScale(1.5f);
        // setting x axis point size
        multiRenderer.setPointSize(2f);
        // setting the margin size for the graph in the order top, left, bottom,
        // right
        multiRenderer.setMargins(new int[]{40, 10, 40, 10});

        for (int i = 0; i < x.length; i++) {
            multiRenderer.addXTextLabel(i, "");
        }
        return multiRenderer;
    }

}


