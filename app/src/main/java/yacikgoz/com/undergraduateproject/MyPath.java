package yacikgoz.com.undergraduateproject;

import java.util.ArrayList;

/**
 *
 * Created by yasinacikgoz on 27.12.2017.
 */

public class MyPath {
    float x;
    float y;

   // public  static ArrayList<MyPath> paths;

    private transient OnPathChanged onPathChanged;
    public MyPath(float _x, float _y){
        x = _x;
        y = _y;
   //     paths = new ArrayList<>();
     //   paths.add(new MyPath(x,y));
    }
    public float getX(){ return  x; }
    public float getY(){ return  y; }
    private void setX(float _x){
        this.x = _x;
    }
    private void setY(float _y){
        this.y = _y;
    }

    public void add(float x, float y){
setY(y);
setX(x);
   //     paths.add(new MyPath(x,y));
        if(onPathChanged != null){
            onPathChanged.onPathChanged();
        }
    }

    public void setOnPathChanged( OnPathChanged _onPathChanged){
        onPathChanged = _onPathChanged;
    }

    public interface OnPathChanged{

        void onPathChanged();
    }
}
