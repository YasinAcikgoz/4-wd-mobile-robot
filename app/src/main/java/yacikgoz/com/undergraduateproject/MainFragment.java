package yacikgoz.com.undergraduateproject;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.IOException;

import static android.content.Context.SENSOR_SERVICE;

/**
 *
 * Created by yacikgoz on 11.11.2017.
 */

public class MainFragment extends Fragment implements View.OnClickListener, SensorEventListener,OnChartValueSelectedListener {
    View v;
    TextView t1;
    private SensorManager sensorManager;
    String message = "";
    private LineChart mChartLF;
    float pls;
    LineData data;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main, container, false);
        initilize();
        return v;
    }

    private void initilize() {
        t1 = v.findViewById(R.id.text);

     //   mChartLF = v.findViewById(R.id.chartLF);
       // setmChartLF();

        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);

        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        t1.setText("thread started");
                    }
                });
                if(StartActivity.socket != null && StartActivity.socket.isConnected()){

                    receiveDataFromServer();
                    //    feedMultiple();

                } else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            t1.setText("socket error");
                        }
                    });
                }
            }
        }).start();

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
        leftAxis.setAxisMaximum(1000);
        leftAxis.setAxisMinimum(0);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChartLF.getAxisRight();
        rightAxis.setEnabled(false);

    }

    /*private void addEntry() {

        data = mChartLF.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null ) {
                set = createSet();
            }


            data.addEntry(new Entry(set.getEntryCount(), (float) pls), 0);
            data.notifyDataChanged();

            mChartLF.notifyDataSetChanged();
            Description description = new Description();
            description.setText("pulaw chart");
            mChartLF.setDescription(description);
            // limit the number of visible entries
            mChartLF.setVisibleXRangeMaximum(10);
            // mChartLF.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChartLF.moveViewToX(data.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // mChartLF.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }*/
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

    private Thread thread;

    private void feedMultiple() {

        if (thread != null)
            thread.interrupt();

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                //addEntry();
            }
        };

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    if(isAdded()) {

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
    public void onClick(View view) {

    }
    public String receiveDataFromServer() {
        try {

            int charsRead = 0;
            int i=0;
            char[] buffer = new char[10];

            while ((charsRead = StartActivity.in.read(buffer)) != -1) {
                message += new String(buffer).substring(0, charsRead);
                if(message.contains("e")){
                    Toast.makeText(getContext(), "server closed", Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(message.contains("-")){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                   //         pls = Float.parseFloat(message.replace("-", ""));

                            t1.setText("pulse: " + message.replace("-", ""));
                            message = "";
                        }
                    });
                }
            }

            return message;
        } catch (IOException e) {
            return "Error receiving response:  " + e.getMessage();
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && StartActivity.socket != null && StartActivity.socket.isConnected()) {

            int data = (int) (event.values[1]);

            if (data < 0)

                data = 10 - data;

            if(data == 10 || data == 20)

                data--;

            data += 10;

                StartActivity.printWriter_socket.print("" + data);
                StartActivity.printWriter_socket.flush();
         //   }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
