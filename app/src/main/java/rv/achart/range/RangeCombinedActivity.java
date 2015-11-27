package rv.achart.range;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.CombinedXYChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.RangeBarChart;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class RangeCombinedActivity extends Activity {

    private GraphicalView mChart;

    private String[] mMonth = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_combined);
        openChart();
    }

    private void openChart() {
        int[] x = {0, 1, 2, 3, 4, 5, 6, 7};
        int[] income = {2000, 2500, 2700, 3000, 2800, 3500, 3700, 3800};
        int[] expense = {2200, 2700, 2900, 2800, 2600, 3000, 3300, 3400};

        // Creating an  XYSeries for Income
        XYSeries incomeSeries = new XYSeries("Income");
        // Creating an  XYSeries for Expense
        XYSeries expenseSeries = new XYSeries("Expense");
        // Adding data to Income and Expense Series
        for (int i = 0; i < x.length; i++) {
            incomeSeries.add(x[i], income[i]);
            expenseSeries.add(x[i], expense[i]);
        }

        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Income Series to the dataset
        dataset.addSeries(incomeSeries);
        // Adding Expense Series to dataset
        dataset.addSeries(expenseSeries);

        // Creating XYSeriesRenderer to customize incomeSeries
        XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
        incomeRenderer.setColor(Color.WHITE);
        incomeRenderer.setPointStyle(PointStyle.CIRCLE);
        incomeRenderer.setFillPoints(true);
        incomeRenderer.setLineWidth(2);
        incomeRenderer.setDisplayChartValues(true);

        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
        expenseRenderer.setColor(Color.YELLOW);
        expenseRenderer.setPointStyle(PointStyle.CIRCLE);
        expenseRenderer.setFillPoints(true);
        expenseRenderer.setLineWidth(2);
        expenseRenderer.setDisplayChartValues(true);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setChartTitle("Income vs Expense Chart");
        multiRenderer.setXTitle("Year 2012");
        multiRenderer.setYTitle("Amount in Dollars");
        multiRenderer.setZoomButtonsVisible(true);
        multiRenderer.setBarSpacing(4);
        for (int i = 0; i < x.length; i++) {
            multiRenderer.addXTextLabel(i, mMonth[i]);
        }

        // Adding incomeRenderer and expenseRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
        // should be same
        multiRenderer.addSeriesRenderer(incomeRenderer);
        multiRenderer.addSeriesRenderer(expenseRenderer);

        // Getting a reference to LinearLayout of the RangeCombinedActivity Layout
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);

        // Specifying chart types to be drawn in the graph
        // Number of data series and number of types should be same
        // Order of data series and chart type will be same
//        String[] types = new String[]{LineChart.TYPE, BarChart.TYPE};
        CombinedXYChart.XYCombinedChartDef typesDefs[] = new CombinedXYChart.XYCombinedChartDef[2];
        typesDefs[0] = new CombinedXYChart.XYCombinedChartDef(LineChart.TYPE, 0, 1);
        typesDefs[1] = new CombinedXYChart.XYCombinedChartDef(RangeBarChart.TYPE, 0, 1);

        // Creating a combined chart with the chart types specified in types array
        mChart = (GraphicalView) ChartFactory.getCombinedXYChartView(getBaseContext(), dataset, multiRenderer, typesDefs);

        multiRenderer.setClickEnabled(true);
        multiRenderer.setSelectableBuffer(10);

        mChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SeriesSelection seriesSelection = mChart.getCurrentSeriesAndPoint();

                if (seriesSelection != null) {
                    int seriesIndex = seriesSelection.getSeriesIndex();
                    String selectedSeries = "Income";
                    if (seriesIndex == 0)
                        selectedSeries = "Income";
                    else
                        selectedSeries = "Expense";
                    // Getting the clicked Month
                    String month = mMonth[(int) seriesSelection.getXValue()];
                    // Getting the y value
                    int amount = (int) seriesSelection.getValue();
                    Toast.makeText(
                            getBaseContext(),
                            selectedSeries + " in " + month + " : " + amount,
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

        // Adding the Combined Chart to the LinearLayout
        chartContainer.addView(mChart);
    }

}