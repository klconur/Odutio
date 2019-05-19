package com.okb.odutio.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.okb.odutio.R;
import com.okb.odutio.fragment.LogFragment;
import com.okb.odutio.fragment.MainFragment;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationListener);
        setInitialFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_account) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setInitialFragment() {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, MainFragment.newInstance()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);
            switch (item.getItemId()) {
                case R.id.navigation_apps:
                    if (!(currentFragment instanceof MainFragment)) {
                        fragment = MainFragment.newInstance();
                        setTitle(R.string.apps);
                    }
                    break;
                case R.id.navigation_log:
                    if (!(currentFragment instanceof LogFragment)) {
                        fragment = LogFragment.newInstance();
                        setTitle(R.string.log);
                    }
                    break;
            }
            if (fragment != null) {
                final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, fragment).commit();
            }
            return true;
        }
    };
}
