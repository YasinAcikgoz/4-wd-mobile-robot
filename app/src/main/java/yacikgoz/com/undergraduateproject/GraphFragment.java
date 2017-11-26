package yacikgoz.com.undergraduateproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

/**
 *
 * Created by yacikgoz on 11.11.2017.
 */

public class GraphFragment extends Fragment implements
        OnChartValueSelectedListener {
    View v;
    double a = 0;
    private LineChart mChartLF, mChartRF, mChartLR, mChartRR;
    LineData data, data2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_graph, container, false);
        initilize();
        feedMultiple();
        return v;
    }
    private void initilize(){
        mChartLF = v.findViewById(R.id.chartLF);
        mChartLR = v.findViewById(R.id.chartLR);
        mChartRR = v.findViewById(R.id.chartRR);
        mChartRF = v.findViewById(R.id.chartRF);
        setmChartLF();
        setmChartLR();
    }
    private void setmChartLF(){
        mChartLF.setOnChartValueSelectedListener(this);

        // enable description text
        mChartLF.getDescription().setEnabled(true);

        // enable touch gestures
        mChartLF.setTouchEnabled(true);

        // enable scaling and dragging
        mChartLF.setDragEnabled(true);
        mChartLF.setScaleEnabled(true);
        mChartLF.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChartLF.setPinchZoom(true);

        // set an alternative background color
        mChartLF.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChartLF.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mChartLF.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        //l.setTypeface(mTfLight);
        l.setTextColor(Color.BLACK);

        XAxis xl = mChartLF.getXAxis();
        // xl.setTypeface(mTfLight);
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);


        YAxis leftAxis = mChartLF.getAxisLeft();
        //  leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(1);
        leftAxis.setAxisMinimum(-1);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChartLF.getAxisRight();
        rightAxis.setEnabled(false);

    }
    private void setmChartLR(){
        mChartLR.setOnChartValueSelectedListener(this);

        // enable description text
        mChartLR.getDescription().setEnabled(true);

        // enable touch gestures
        mChartLR.setTouchEnabled(true);

        // enable scaling and dragging
        mChartLR.setDragEnabled(true);
        mChartLR.setScaleEnabled(true);
        mChartLR.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChartLR.setPinchZoom(true);

        // set an alternative background color
        mChartLR.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChartLR.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mChartLR.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        //l.setTypeface(mTfLight);
        l.setTextColor(Color.BLACK);

        XAxis xl = mChartLR.getXAxis();
        // xl.setTypeface(mTfLight);
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);


        YAxis leftAxis = mChartLR.getAxisLeft();
        //  leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(1);
        leftAxis.setAxisMinimum(-1);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChartLR.getAxisRight();
        rightAxis.setEnabled(false);

    }
    private void addEntry() {

        data = mChartLF.getData();
        data2 = mChartLR.getData();

        if (data != null && data2!=null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            ILineDataSet set2 = data.getDataSetByIndex(1);
            ILineDataSet set3 = data2.getDataSetByIndex(0);
            ILineDataSet set4 = data2.getDataSetByIndex(1);
            // set.addEntry(...); // can be called as well

            if (set == null || set2 == null) {
                set = createSet();
                set2 = createSet2();
                set3 = createSet();
                set4 = createSet2();
                data.addDataSet(set);
                data.addDataSet(set2);
                data2.addDataSet(set3);
                data2.addDataSet(set4);
            }
            double c = Math.cos(a);
            double s = Math.sin(a);

            data.addEntry(new Entry(set.getEntryCount(), (float) s), 0);
            data.addEntry(new Entry(set2.getEntryCount(), (float) c), 1);
            data2.addEntry(new Entry(set3.getEntryCount(), (float) s), 0);
            data2.addEntry(new Entry(set4.getEntryCount(), (float) c), 1);
            data.notifyDataChanged();
            data2.notifyDataChanged();

            mChartLF.notifyDataSetChanged();
            mChartLR.notifyDataSetChanged();
            Description description = new Description();
            description.setText("sin-cos chart");
            mChartLF.setDescription(description);
            mChartLR.setDescription(description);
            // limit the number of visible entries
            mChartLF.setVisibleXRangeMaximum(5);
            mChartLR.setVisibleXRangeMaximum(5);
            // mChartLF.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChartLR.moveViewToX(data.getEntryCount());
            mChartLF.moveViewToX(data2.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // mChartLF.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }
    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "SIN");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.TRANSPARENT);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(false);
        return set;
    }
    private LineDataSet createSet2() {

        LineDataSet set = new LineDataSet(null, "COS");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.RED);
        set.setCircleColor(Color.TRANSPARENT);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(Color.RED);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(false);
        return set;
    }

    private Thread thread;

    private void feedMultiple() {

        if (thread != null)
            thread.interrupt();

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                addEntry();
            }
        };

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    if(isAdded()) {
                        ++a;
                        if(a == 20) {
                            a = 0;
                            break;
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if(getActivity()!=null)
                            getActivity().runOnUiThread(runnable);
                    }
                }
            }
        });

        thread.start();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    @Override
    public void onPause() {
        super.onPause();

        if (thread != null) {
            thread.interrupt();
        }
    }
}
