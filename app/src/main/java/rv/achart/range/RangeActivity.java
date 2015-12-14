package rv.achart.range;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.RangeBarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

import rv.achart.range.objects.CurrencyPairRate;

/**
 * Created by Rui Vieira on 25/11/2015.
 */
public class RangeActivity extends Activity {


    private List<CurrencyPairRate> currencyPairRatesList;
    private LinearLayout chartLyt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_range);

        currencyPairRatesList = new ArrayList<>();

        mockValues();

        chartLyt = (LinearLayout) findViewById(R.id.chart);
        openChart();
    }

    private CurrencyPairRate[] mockValues() {
        currencyPairRatesList.add(new CurrencyPairRate("2.2344", "2.1324"));
        currencyPairRatesList.add(new CurrencyPairRate("2.2321", "2.1345"));
        currencyPairRatesList.add(new CurrencyPairRate("2.2311", "2.1323"));
        currencyPairRatesList.add(new CurrencyPairRate("2.2123", "2.1834"));
        currencyPairRatesList.add(new CurrencyPairRate("2.2233", "2.1634"));
        currencyPairRatesList.add(new CurrencyPairRate("2.2534", "2.1324"));
        currencyPairRatesList.add(new CurrencyPairRate("2.2687", "2.0324"));
        currencyPairRatesList.add(new CurrencyPairRate("2.2875", "1.8345"));
        currencyPairRatesList.add(new CurrencyPairRate("2.2456", "1.7543"));
        currencyPairRatesList.add(new CurrencyPairRate("2.1233", "2.1124"));

        CurrencyPairRate[] currencyPairRatesArray = new CurrencyPairRate[currencyPairRatesList.size()];
        currencyPairRatesList.toArray(currencyPairRatesArray);

        return currencyPairRatesArray;
    }

    private void openChart() {
        // Now we create the renderer
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(2);
        renderer.setColor(Color.RED);
        // Include low and max value
        renderer.setDisplayBoundingPoints(false);
        // we add point markers
        renderer.setPointStyle(PointStyle.POINT);
//        renderer.setPointStrokeWidth(0);


        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);

//        // We want to avoid black border
//        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
//        // Disable Pan on two axis
//        mRenderer.setPanEnabled(false, false);
//        mRenderer.setYAxisMax(35);
//        mRenderer.setYAxisMin(0);
//        mRenderer.setShowGrid(true); // we show the grid

        XYSeries incomeSeries = new XYSeries("getHigh");
        XYSeries expenseSeries = new XYSeries("getLow");
        for (int i = 0; i < currencyPairRatesList.size(); i++) {
            incomeSeries.add(i, Double.valueOf(currencyPairRatesList.get(i).getHigh()));
            expenseSeries.add(i, Double.valueOf(currencyPairRatesList.get(i).getLow()));
        }
        XYMultipleSeriesDataset dataset1 = new XYMultipleSeriesDataset();
        XYMultipleSeriesDataset dataset2 = new XYMultipleSeriesDataset();
        dataset1.addSeries(incomeSeries);
        dataset2.addSeries(expenseSeries);

        XYSeriesRenderer.FillOutsideLine fill =
                new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BELOW
                );

        fill.setColor(Color.MAGENTA);
        // the minimum and maximum index values for the fill range
        fill.setFillRange(new int[] {1, 5});
        renderer.addFillOutsideLine(fill);


        GraphicalView chartView = ChartFactory.getTimeChartView(this, dataset1, mRenderer, null);
//        GraphicalView chartView = ChartFactory.getLineChartView(this, dataset1, mRenderer);
//        GraphicalView chartView = ChartFactory.getCombinedXYChartView(this, dataset1, mRenderer);
        chartLyt.addView(chartView, 0);
    }
}
