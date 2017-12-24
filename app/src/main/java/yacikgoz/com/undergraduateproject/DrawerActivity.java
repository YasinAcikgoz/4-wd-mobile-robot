package yacikgoz.com.undergraduateproject;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;

import static yacikgoz.com.undergraduateproject.StartActivity.socket;

public class DrawerActivity extends AppCompatActivity implements SensorEventListener {

    static FragmentManager mFragmentManager;
    static FragmentTransaction mFragmentTransaction;
    Fragment fragment = null;
    Context context;

    public static int data, direction = 0, stop = 0;
    public static double vLR, vRR, vLF, vRF;
    public static double pls1 =0, pls2=0, pls3 = 0, pls4 = 0, plsLF = 0, plsRF = 0, plsLR = 0, plsRR = 0;
    public static String message = "";
    public static Activity activity = null;
    public static boolean serverOpen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        activity = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        context = getApplicationContext();
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        fragment = new MainFragment();
        mFragmentTransaction.replace(R.id.container, fragment).commit();

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new MainFragment();
                    mFragmentTransaction.replace(R.id.container, fragment).commit();
                    return true;
                case R.id.navigation_dashboard:
                    fragment = new GraphFragment();
                    mFragmentTransaction.replace(R.id.container, fragment).commit();
                    return true;
                case R.id.navigation_notifications:
                    fragment = new PathFragment();
                    mFragmentTransaction.replace(R.id.container, fragment).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    public void onSensorChanged(final SensorEvent event) {

        if ( event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && socket != null && socket.isConnected() /* && isRunning*/){
            data = (int) (event.values[1]);

            if (data < 0)

                data = 10 - data;

            if(data == 10 || data == 20)

                data--;

            data += 10;

            String s = "" + data + direction + stop;
            StartActivity.printWriter_socket.print(s);
          //  System.out.println("-------- " + s);
            StartActivity.printWriter_socket.flush();

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    public static void receiveDataFromServer(final Context context) {
        int count = 0;

        try {
            int charsRead;
            char[] buffer = new char[20];

            while ((charsRead = StartActivity.in.read(buffer)) != -1) {
                if(StartActivity.isRunning && serverOpen ) {
                    message = new String(buffer).substring(0, charsRead);
                  //  System.out.println("mes: " + message);

                    if (charsRead >= 20 || message.contains("e")) {
                        if (message.contains("e")) {
                            StartActivity.socket.close();
                            serverOpen = false;
                            activity.finish();

                            //onBackPressed();
                            break;
                        } else {
                            String[] arr = message.split("_");
                           /* for(String s: arr)
                                System.out.print(s + " ");*/
                            System.out.println();
                         ///   if (arr[0].length() == 4 /*&& arr[1].length() == 4 && arr[2].length() == 4 && arr[3].length() == 4*/) {
                            if(arr.length == 4){
                                pls1 = parseDoubleSafely(arr[0]) - 3000;
                                pls2 = parseDoubleSafely(arr[1]) - 3000;
                                pls3 = parseDoubleSafely(arr[2]) - 3000;
                                pls4 = parseDoubleSafely(arr[3]) - 3000;
                                plsLF += pls1;
                                plsRF += pls2;
                                plsLR += pls3;
                                plsRR += pls4;
                                if(count >= 5){
                                    vLF = plsLF*0.02;
                                    vRF = plsRF*0.02;
                                    vLR = plsLR*0.03;
                                    vRR = plsRR*0.03;
                                    plsLF = 0;
                                    plsRF = 0;
                                    plsLR = 0;
                                    plsRR = 0;
                                    count = 0;
                                }
                                message = "";
                           }
                            ++count;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void checkPulses(){

        if(pls1 > 100){
            pls1 = 100;
        } else if(pls1 < -100){
            pls1 = -100;
        }

        if(pls2 > 100){
            pls2 = 100;
        } else if(pls2 < -100){
            pls2 = -100;
        }

        if(pls3 > 100){
            pls3 = 100;
        } else if(pls3 < -100){
            pls3 = -100;
        }

        if(pls4 > 100){
            pls4 = 100;
        } else if(pls4 < -100){
            pls4 = -100;
        }
    }
    public static double parseDoubleSafely(String str) {
        double result = 0;
        try {
            result = Double.parseDouble(str);
        } catch (NullPointerException | NumberFormatException npe) {
        }
        return result;
    }
}
