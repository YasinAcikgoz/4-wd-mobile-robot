package yacikgoz.com.undergraduateproject;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.anastr.speedviewlib.ProgressiveGauge;

import static yacikgoz.com.undergraduateproject.DrawerActivity.vLF;
import static yacikgoz.com.undergraduateproject.DrawerActivity.vLR;
import static yacikgoz.com.undergraduateproject.DrawerActivity.vRF;
import static yacikgoz.com.undergraduateproject.DrawerActivity.vRR;

/**
 *
 * Created by yacikgoz on 11.11.2017.
 */

public class MainFragment extends Fragment implements View.OnClickListener{
    View v;
    ProgressiveGauge sLF, sRF, sRR, sLR;
    private final int duration = 1000;
    GetSpeed getSpeed;
    ImageButton playBtn,  pauseBtn, forwardBtn, reverseBtn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main, container, false);
        initilize();
        getSpeed = new GetSpeed();


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(isAdded())
        getSpeed.execute();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        getSpeed.cancel(true);
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
                StartActivity.isRunning = true;
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
    private void initilize(){


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
        sLF = v.findViewById(R.id.speedLF);
        sRR = v.findViewById(R.id.speedRR);
        sRF = v.findViewById(R.id.speedRF);
        sLR = v.findViewById(R.id.speedLR);
        sLF.setMaxSpeed(100);
        sLF.setMinSpeed(0);
        sLF.setMaxSpeed(100);
        sLF.setMinSpeed(0);
        sRR.setMaxSpeed(100);
        sRR.setMinSpeed(0);
        sLR.setMaxSpeed(100);
        sLR.setMinSpeed(0);
        sRF.setMaxSpeed(100);
        sRF.setMinSpeed(0);
    }
    class GetSpeed extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings) {
            try {
                while (!this.isCancelled()) {
                    if (DrawerActivity.stop == 1 && isAdded()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sLF.speedTo((float) vLF);
                                sRF.speedTo((float) vRF);
                                sLR.speedTo((float) vLR);
                                sRR.speedTo((float) vRR);
                                System.out.println("++++++++++++++++v1: " + vLF + " v2: " + vRF + " v3: " + vLR + " v4 " + vRR);
                            }
                        });

                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sLF.speedTo(0);
                                sRF.speedTo(0);
                                sLR.speedTo(0);
                                sRR.speedTo(0);
                                System.out.println("stop");
                            }
                        });
                    }
                    Thread.sleep(duration);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
