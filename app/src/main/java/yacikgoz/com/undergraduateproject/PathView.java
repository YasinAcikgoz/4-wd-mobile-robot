package yacikgoz.com.undergraduateproject;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class PathView extends View implements MyPath.OnPathChanged{

    private Paint mLinePaint;
    public MyPath mPath;
    public final float density = getContext().getResources().getDisplayMetrics().density;
    Path path;
    Point[]coor;
    int count = 0;

    public PathView(Context context) {
        this(context,null);
    }

    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public void setPath(MyPath path)
    {
        if(mPath != null){
            mPath.setOnPathChanged(null);
        }
        mPath = path;
        if(path != null){
            path.setOnPathChanged(this);
        }
        onPathChanged();
    }

    private void init() {
        coor = new Point[1000];
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setStrokeWidth(density * 2);
        mLinePaint.setColor(getResources().getColor(R.color.colorAccent));

        float x = PathFragment.w / 2;
        float y = (PathFragment.h - density*100);
        path = new Path();
        coor[0] = new Point(x,y);

    }
    class Point {

        public float x, y;



        Point(float _x, float _y){

            x = _x;

            y = _y;

        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

      //  System.out.println("ONDRAW");
        final MyPath path = mPath;
        if(path == null){
            return;
        }
        final float x = path.getX();
        final float y = path.getY();
        this.path.addCircle(coor[0].x, coor[0].y, 5, Path.Direction.CCW);
        this.path.moveTo(coor[0].x, coor[0].y);
       // System.out.println("start: " + coor[0].x + " " + coor[0].y);

        ++count;
       // System.out.println("-----x: " + x + " y: " + y);
        coor[count] = new Point(x+coor[count -1 ].x,y+coor[count -1 ].y);


        for(int i=0; i<count; ++i){
           // Log.d("addEntry", "x: " + coor[i].x + " y: " + coor[i].y + " i: " + i);
           /* float midX            = coor[i-1].x +((coor[i].x - coor[i-1].x ) / 2);
            float midY            = coor[i-1].y +((coor[i].y - coor[i-1].y ) / 2);
            float xDiff         = midX - coor[i-1].x;
            float yDiff         = midY - coor[i-1].y;
            double angle        = (Math.atan2(yDiff, xDiff) * (180 / Math.PI)) - 90;
            double angleRadians = Math.toRadians(angle);
            float pointX        = (float) (midX + 50 * Math.cos(angleRadians));
            float pointY        = (float) (midY + 50 * Math.sin(angleRadians));
            this.path.cubicTo(coor[i-1].x,coor[i-1].y,pointX, pointY, coor[i].x, coor[i].y);*/
           this.path.lineTo(coor[i].x, coor[i].y);

        }
        canvas.drawPath(this.path, mLinePaint);

    }

    @Override
    public void onPathChanged() {
        ViewCompat.postInvalidateOnAnimation(this);
    }
}