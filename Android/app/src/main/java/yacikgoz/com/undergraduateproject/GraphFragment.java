package yacikgoz.com.undergraduateproject;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

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

import java.io.IOException;

import static android.content.Context.SENSOR_SERVICE;
import static yacikgoz.com.undergraduateproject.DrawerActivity.message;
import static yacikgoz.com.undergraduateproject.DrawerActivity.pls1;
import static yacikgoz.com.undergraduateproject.DrawerActivity.pls2;
import static yacikgoz.com.undergraduateproject.DrawerActivity.pls3;
import static yacikgoz.com.undergraduateproject.DrawerActivity.pls4;

/**
 *
 * Created by yacikgoz on 11.11.2017.
 */

public class GraphFragment extends Fragment implements View.OnClickListener, OnChartValueSelectedListener {


    View v;
    ImageButton playBtn,  pauseBtn, forwardBtn, reverseBtn;
    private LineChart mChartLF, mChartRF, mChartLR, mChartRR;
    private final int graphMax = 1200, graphMin = -1200;

    LineData dataLF, dataLR, dataRF, dataRR;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_graph, container, false);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        initilize();
        feedMultiple();
        return v;
    }

    private void initilize() {

        mChartLF = v.findViewById(R.id.chartLF);
        mChartRF = v.findViewById(R.id.chartRF);
        mChartLR = v.findViewById(R.id.chartLR);
        mChartRR = v.findViewById(R.id.chartRR);

        playBtn = v.findViewById(R.id.play_btn);
        pauseBtn = v.findViewById(R.id.pause_btn);
        forwardBtn = v.findViewById(R.id.forward_btn);
        reverseBtn = v.findViewById(R.id.reverse_btn);
        pauseBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        reverseBtn.setOnClickListener(this);
        forwardBtn.setOnClickListener(this);
        if(DrawerActivity.stop == 0){

            playBtn.setVisibility(View.VISIBLE);
            pauseBtn.setVisibility(View.INVISIBLE);
        } else{
            playBtn.setVisibility(View.INVISIBLE);
            pauseBtn.setVisibility(View.VISIBLE);

        }
        if( DrawerActivity.direction == 0){

            reverseBtn.setVisibility(View.VISIBLE);
            forwardBtn.setVisibility(View.INVISIBLE);
        } else{

            forwardBtn.setVisibility(View.VISIBLE);
            reverseBtn.setVisibility(View.INVISIBLE);
        }
        setmChartLF();
        setmChartLR();
        setmChartRR();
        setmChartRF();




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

        // add empty dataLF
        mChartLF.setData(data);

        // get the legend (only possible after setting dataLF)
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
        leftAxis.setAxisMaximum(graphMax);
        leftAxis.setAxisMinimum(graphMin);
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

        // add empty dataLF
        mChartLR.setData(data);

        // get the legend (only possible after setting dataLF)
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
        leftAxis.setAxisMaximum(graphMax);
        leftAxis.setAxisMinimum(graphMin);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChartLR.getAxisRight();
        rightAxis.setEnabled(false);

    }
    private void setmChartRF(){
        mChartRF.setOnChartValueSelectedListener(this);

        // enable description text
        mChartRF.getDescription().setEnabled(true);

        // enable touch gestures
        mChartRF.setTouchEnabled(true);

        // enable scaling and dragging
        mChartRF.setDragEnabled(true);
        mChartRF.setScaleEnabled(true);
        mChartRF.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChartRF.setPinchZoom(true);

        // set an alternative background color
        mChartRF.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty dataLF
        mChartRF.setData(data);

        // get the legend (only possible after setting dataLF)
        Legend l = mChartRF.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        //l.setTypeface(mTfLight);
        l.setTextColor(Color.BLACK);

        XAxis xl = mChartRF.getXAxis();
        // xl.setTypeface(mTfLight);
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);


        YAxis leftAxis = mChartRF.getAxisLeft();
        //  leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(graphMax);
        leftAxis.setAxisMinimum(graphMin);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChartRF.getAxisRight();
        rightAxis.setEnabled(false);

    }
    private void setmChartRR(){
        mChartRR.setOnChartValueSelectedListener(this);

        // enable description text
        mChartRR.getDescription().setEnabled(true);

        // enable touch gestures
        mChartRR.setTouchEnabled(true);

        // enable scaling and dragging
        mChartRR.setDragEnabled(true);
        mChartRR.setScaleEnabled(true);
        mChartRR.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChartRR.setPinchZoom(true);

        // set an alternative background color
        mChartRR.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty dataLF
        mChartRR.setData(data);

        // get the legend (only possible after setting dataLF)
        Legend l = mChartRR.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        //l.setTypeface(mTfLight);
        l.setTextColor(Color.BLACK);

        XAxis xl = mChartRR.getXAxis();
        // xl.setTypeface(mTfLight);
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);


        YAxis leftAxis = mChartRR.getAxisLeft();
        //  leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(graphMax);
        leftAxis.setAxisMinimum(graphMin);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChartRR.getAxisRight();
        rightAxis.setEnabled(false);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.play_btn:
                StartActivity.isRunning = true;
                DrawerActivity.stop = 1;
                playBtn.setVisibility(View.INVISIBLE);
                pauseBtn.setVisibility(View.VISIBLE);

            break;

            case R.id.pause_btn:
                StartActivity.isRunning = false;
                DrawerActivity.stop = 0;
                playBtn.setVisibility(View.VISIBLE);
                pauseBtn.setVisibility(View.INVISIBLE);

            break;

            case R.id.forward_btn:
                reverseBtn.setVisibility(View.VISIBLE);
                forwardBtn.setVisibility(View.INVISIBLE);
                DrawerActivity.direction = 0;

            break;

            case R.id.reverse_btn:
                forwardBtn.setVisibility(View.VISIBLE);
                reverseBtn.setVisibility(View.INVISIBLE);
                DrawerActivity.direction = 1;

            break;

        }
    }
    private void addEntry() {

        dataLF = mChartLF.getData();
        dataLR = mChartLR.getData();
        dataRR = mChartRR.getData();
        dataRF = mChartRF.getData();

        if (dataLF != null && dataLR !=null && dataRR != null && dataRF !=null) {

            ILineDataSet set = dataLF.getDataSetByIndex(0);
            ILineDataSet set2 = dataLR.getDataSetByIndex(0);
            ILineDataSet set3 = dataRR.getDataSetByIndex(0);
            ILineDataSet set4 = dataRF.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null || set2 == null || set3 == null || set4 == null) {
                set = createSet();
                set2 = createSet();
                set3 = createSet();
                set4 = createSet();
                dataLF.addDataSet(set);
                dataLR.addDataSet(set2);
                dataRR.addDataSet(set3);
                dataRF.addDataSet(set4);
            }
        //if(pls1!=0 && pls2 !=0 && pls3 != 0 && pls4 != 0 ) {

            dataLF.addEntry(new Entry(set.getEntryCount(), (float) DrawerActivity.pls1), 0);
            dataRF.addEntry(new Entry(set4.getEntryCount(), (float) DrawerActivity.pls2), 0);
            dataLR.addEntry(new Entry(set2.getEntryCount(), (float) DrawerActivity.pls3), 0);
            dataRR.addEntry(new Entry(set3.getEntryCount(), (float) DrawerActivity.pls4), 0);
            System.out.println("******** pls1: " + DrawerActivity.pls1 + " pls2: " + DrawerActivity.pls2 + " pls3: " + DrawerActivity.pls3 + " pls4: " + DrawerActivity.pls4);

      //  }
            dataLF.notifyDataChanged();
            dataLR.notifyDataChanged();

            dataRR.notifyDataChanged();
            dataRF.notifyDataChanged();

            mChartLF.notifyDataSetChanged();
            mChartLR.notifyDataSetChanged();

            mChartRR.notifyDataSetChanged();
            mChartRF.notifyDataSetChanged();
            Description description = new Description();
            description.setText("");
            mChartLF.setDescription(description);
            mChartLR.setDescription(description);
            mChartRR.setDescription(description);
            mChartRF.setDescription(description);
            // limit the number of visible entries
            mChartLF.setVisibleXRangeMaximum(5);
            mChartLR.setVisibleXRangeMaximum(5);
            mChartRR.setVisibleXRangeMaximum(5);
            mChartRF.setVisibleXRangeMaximum(5);
            // mChartLF.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChartLR.moveViewToX(dataLF.getEntryCount());
            mChartLF.moveViewToX(dataLR.getEntryCount());
            mChartRR.moveViewToX(dataRR.getEntryCount());
            mChartRF.moveViewToX(dataRF.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // mChartLF.moveViewTo(dataLF.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }
    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "pulse");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.RED);
        set.setCircleColor(Color.RED);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setColor(Color.RED);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
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
                while(StartActivity.socket.isConnected()){
                    if(isAdded()) {

                        if(getActivity()!=null && StartActivity.isRunning) {
                            getActivity().runOnUiThread(runnable);
                        }

                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        thread.start();
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
