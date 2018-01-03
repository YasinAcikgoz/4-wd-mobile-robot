package yacikgoz.com.undergraduateproject;

import android.util.Log;

import static yacikgoz.com.undergraduateproject.DrawerActivity.vox;
import static yacikgoz.com.undergraduateproject.DrawerActivity.voy;

/**
 *
 * Created by yasinacikgoz on 25.12.2017.
 */


public class MockDataGenerator
        extends Thread
{
    private MyPath myPath;


    public MockDataGenerator(MyPath _My_path)
    {
        super(MockDataGenerator.class.getSimpleName());
        myPath = _My_path;
    }


    @Override
    public void run()
    {
        try{
            float val = 0;
            while(!isInterrupted()){

                float x = vox/15;
                float y = voy/15;

                myPath.add(x,y);
                Thread.sleep(1000);


              //  ++val;
//                System.out.println("val: " + val);


            }
        }
        catch(InterruptedException e){
            //
        }
    }
    private float map(float x, float in_min, float in_max, float out_min, float out_max) {
    return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
}

    public void quit()
    {
        try{
            interrupt();
            join();
        }
        catch(InterruptedException e){
            //
        }
    }
}