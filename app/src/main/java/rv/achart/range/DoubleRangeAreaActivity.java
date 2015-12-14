package rv.achart.range;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Random;

public class DoubleRangeAreaActivity extends Activity {

    private View mChart;

    private int backgroundColor;
    private int linesColor;
    private static final double MAX_VISIBLE_X_VALUES = 20;
    private static final int MAX_LEVEL_LINES = 14;
    private static final int REFRESH_SECONDS = 2;
    private int[] x;

    private GraphicalView mChartViewBuy;
    private GraphicalView mChartViewSell;
    private XYSeries buySeries;
    private XYSeries sellSeries;

    protected Update mUpdateTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_area_doubled);

        openBuyChart();
        openSellChart();

        mUpdateTask = new Update();
        mUpdateTask.execute(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUpdateTask.cancel(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mUpdateTask.cancel(true);
    }

    @Override
    protected void onResume() {
        super.onResume();


        if(buySeries == null) {
            openBuyChart();
        } else {
            mChartViewBuy.repaint();
        }

        if (sellSeries == null) {
            openSellChart();
        } else {
            mChartViewSell.repaint();
        }

    }

    private void openBuyChart() {
        x = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        int[] buy = {2200, 2500, 2700, 3000, 2800, 3500, 3700, 3800, 0, 0,
                0, 0};
        int max = 0; //buy

        // define max value
        for (int i = 0; i < buy.length; i++) {
            if (buy[i] > max) {
                max = buy[i];
            }
        }

        backgroundColor = Color.WHITE;
        linesColor = Color.parseColor("#C82059");

        // Creating an XYSeries for lines
        XYSeries[] linesSeries = createBuyLinesSeries(max);

        // Creating an XYSeries for buy
        buySeries = new XYSeries("buy");
        for (int i = 0; i < x.length; i++) {
            buySeries.add(i, buy[i]);
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        for (int j = 0; j < MAX_LEVEL_LINES; j++) {
            dataset.addSeries(linesSeries[j]);
        }
        dataset.addSeries(buySeries);

        XYSeriesRenderer linesRenderer = getLinesRenderer();
        XYSeriesRenderer buyRenderer = getBuyRenderer();

        XYMultipleSeriesRenderer multiRenderer = getXyMultipleSeriesRenderer(x);

        // Adding buyRenderer and sellRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to
        // multipleRenderer
        // should be same
        for (int j = 0; j < MAX_LEVEL_LINES; j++) {
            multiRenderer.addSeriesRenderer(linesRenderer);
        }
        multiRenderer.addSeriesRenderer(buyRenderer);

        /* Populate */
        populateChartBuy(dataset, multiRenderer);
    }

    private void openSellChart() {
        x = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        int[] sell = {2000, 2700, 2900, 2800, 2600, 3000, 3300, 3400, 0, 0,
                0, 0};
        int max = 0; //sell
        int min = 9999999; //sell

        // define max value
        for (int i = 0; i < sell.length; i++) {
            if (sell[i] > max) {
                max = sell[i];
            }
        }

        // sell values offset
        for (int i = 0; i < sell.length; i++) {
            sell[i] -= max;
        }

        backgroundColor = Color.WHITE;
        linesColor = Color.parseColor("#C82059");

        // Creating an XYSeries for lines
        XYSeries[] linesSeries = createSellLinesSeries(max);

        // Creating an XYSeries for buy
        sellSeries = new XYSeries("sell");
        for (int i = 0; i < x.length; i++) {
            sellSeries.add(i, sell[i]);
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        for (int j = 0; j < MAX_LEVEL_LINES; j++) {
            dataset.addSeries(linesSeries[j]);
        }
        dataset.addSeries(sellSeries);

        XYSeriesRenderer linesRenderer = getLinesRenderer();
        XYSeriesRenderer sellRenderer = getSellRenderer();


        XYMultipleSeriesRenderer multiRenderer = getXyMultipleSeriesRenderer(x);

        // Adding buyRenderer and sellRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to
        // multipleRenderer
        // should be same
        for (int j = 0; j < MAX_LEVEL_LINES; j++) {
            multiRenderer.addSeriesRenderer(linesRenderer);
        }
        multiRenderer.addSeriesRenderer(sellRenderer);

        /* Populate */
        populateChartSell(dataset, multiRenderer);
    }

    @NonNull
    private XYSeries[] createSellLinesSeries(int max) {
        XYSeries[] linesSeries = new XYSeries[MAX_LEVEL_LINES];
        for (int j = 0; j < MAX_LEVEL_LINES; j++) {
            linesSeries[j] = new XYSeries("lines" + j);
            for (int i = 0; i < MAX_VISIBLE_X_VALUES + 1; i++) {
                int value = 0;
                if (max == 0) {
                    value = j;
                } else {
                    value = (max * 2) / MAX_LEVEL_LINES;
                    value = value * j;
                }
                linesSeries[j].add(i, (value * -1));
            }
        }
        return linesSeries;
    }

    @NonNull
    private XYSeries[] createBuyLinesSeries(int max) {
        XYSeries[] linesSeries = new XYSeries[MAX_LEVEL_LINES];
        for (int j = 0; j < MAX_LEVEL_LINES; j++) {
            linesSeries[j] = new XYSeries("lines" + j);
            for (int i = 0; i < MAX_VISIBLE_X_VALUES + 1; i++) {
                int value = 0;
                if (max == 0) {
                    value = j;
                } else {
                    value = (max * 2) / MAX_LEVEL_LINES;
                    value = value * j;
                }
                linesSeries[j].add(i, value);
            }
        }
        return linesSeries;
    }

    private void populateChartBuy(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer multiRenderer) {
        // this part is used to display graph on the xml
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chartBuy);
        // remove any views before u paint the chart
        chartContainer.removeAllViews();
        // drawing bar chart
        mChartViewBuy = ChartFactory.getLineChartView(DoubleRangeAreaActivity.this, dataset,
                multiRenderer);
        // adding the view to the linearlayout
        chartContainer.addView(mChartViewBuy);
    }

    private void populateChartSell(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer multiRenderer) {
        // this part is used to display graph on the xml
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chartSell);
        // remove any views before u paint the chart
        chartContainer.removeAllViews();
        // drawing bar chart
        mChartViewSell = ChartFactory.getLineChartView(DoubleRangeAreaActivity.this, dataset,
                multiRenderer);
        // adding the view to the linearlayout
        chartContainer.addView(mChartViewSell);
    }

    @NonNull
    private XYSeriesRenderer getLinesRenderer() {
        XYSeriesRenderer sellRenderer = new XYSeriesRenderer();
        sellRenderer.setColor(Color.GRAY);
        sellRenderer.setFillPoints(true);
        sellRenderer.setLineWidth(1f);
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
                new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BELOW
                );
        fill.setColor(backgroundColor);
//        fill.setFillRange(new int[]{0, x.length - 1});
        buyRenderer.addFillOutsideLine(fill);
        return buyRenderer;
    }

    @NonNull
    private XYSeriesRenderer getSellRenderer() {
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
        multiRenderer.setLabelsColor(backgroundColor);
        multiRenderer.setAxesColor(backgroundColor);

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
        multiRenderer.setShowGridX(false);
        multiRenderer.setShowGridY(false);
        multiRenderer.setGridColor(backgroundColor);
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
        multiRenderer.setXAxisMax(MAX_VISIBLE_X_VALUES);
        // setting bar size or space between two bars
        multiRenderer.setBarSpacing(0);
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
        multiRenderer.setMargins(new int[]{0, 0, 0, 0});

        for (int i = 0; i < x.length; i++) {
            multiRenderer.addXTextLabel(i, "");
        }

        return multiRenderer;
    }


    protected class Update extends AsyncTask<Context, Integer, String> {
        private int xx;
        private int yy;

        @Override
        protected String doInBackground(Context... params) {

            int i = 0;
            while (true) {
                try {
                    Thread.sleep(REFRESH_SECONDS * 1000);
                    xx = buySeries.getItemCount();
                    yy = generateRandomNum();

                    publishProgress(i);
                    i++;
                } catch (Exception e) {

                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            buySeries.add(xx, yy);
            if (mChartViewBuy != null) {
                mChartViewBuy.repaint();
            }

            sellSeries.add(xx, yy * -1);
            if (mChartViewSell != null) {
                mChartViewSell.repaint();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    private int generateRandomNum() {
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(4000);
        return randomInt;
    }
}


