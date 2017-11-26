package yacikgoz.com.undergraduateproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 *
 * Created by yacikgoz on 11.11.2017.
 */

public class MainFragment extends Fragment implements View.OnClickListener{
    View v;
    Button btn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main, container, false);
        initilize();

        return v;
    }

    private void initilize() {
        TextView t = v.findViewById(R.id.textView2);
        btn = v.findViewById(R.id.button2);
        btn.setOnClickListener(this);
        t.setText("Main Fragment");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button2:
                StartActivity.printWriter_socket.print('s');
                StartActivity.printWriter_socket.flush();

                break;
        }
    }
}
