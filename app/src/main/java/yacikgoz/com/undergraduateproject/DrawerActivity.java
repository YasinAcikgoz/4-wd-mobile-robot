package yacikgoz.com.undergraduateproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class DrawerActivity extends AppCompatActivity {

    static FragmentManager mFragmentManager;
    static FragmentTransaction mFragmentTransaction;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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

}
