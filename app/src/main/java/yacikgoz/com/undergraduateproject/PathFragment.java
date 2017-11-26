package yacikgoz.com.undergraduateproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * Created by yacikgoz on 11.11.2017.
 */

public class PathFragment extends Fragment {
    View v;
    PathView pathView;
    int path;
    private Thread thread;
    int a=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        v = inflater.inflate(R.layout.fragment_path, container, false);
  //      initilize();


        return new PathView(getContext());
    }

    private void initilize() {
        pathView = v.findViewById(R.id.path_view);
    }
}
