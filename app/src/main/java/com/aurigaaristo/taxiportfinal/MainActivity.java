package com.aurigaaristo.taxiportfinal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.aurigaaristo.taxiportfinal.mainfragment.AccountFragment;
import com.aurigaaristo.taxiportfinal.mainfragment.HistoryFragment;
import com.aurigaaristo.taxiportfinal.mainfragment.OrdersFragment;
import com.aurigaaristo.taxiportfinal.settingsfragment.AboutFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private Fragment fragment;

    String KEY_FRAGMENT = "keyfragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }

        fragment = new OrdersFragment();

        if (getIntent().getStringExtra("x") != null){
            fragment = new AboutFragment();
        }

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, fragment).commit();
        } else {
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, KEY_FRAGMENT);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, fragment).commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.bn_orders:
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle(getString(R.string.orders));
                fragment = new OrdersFragment();
                loadFragment(fragment);
                return true;
            case R.id.bn_history:
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle(getString(R.string.history));
                fragment = new HistoryFragment();
                loadFragment(fragment);
                return true;
            case R.id.bn_account:
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle(getString(R.string.profil));
                fragment = new AccountFragment();
                loadFragment(fragment);
                return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState, KEY_FRAGMENT, fragment);
        super.onSaveInstanceState(outState);
    }
}
