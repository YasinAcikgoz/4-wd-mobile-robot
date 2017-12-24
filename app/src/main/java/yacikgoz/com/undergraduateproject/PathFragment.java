package yacikgoz.com.undergraduateproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 *
 * Created by yacikgoz on 11.11.2017.
 */

public class PathFragment extends Fragment  implements View.OnClickListener{
    View v;

    ImageButton playBtn,  pauseBtn, forwardBtn, reverseBtn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_path, container, false);
        initilize();


        return v;
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
    }
}
