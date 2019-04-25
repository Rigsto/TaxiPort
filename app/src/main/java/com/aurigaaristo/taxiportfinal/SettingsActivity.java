package com.aurigaaristo.taxiportfinal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aurigaaristo.taxiportfinal.settingsfragment.AboutFragment;
import com.aurigaaristo.taxiportfinal.settingsfragment.PasswordFragment;
import com.aurigaaristo.taxiportfinal.settingsfragment.ProfileFragment;

public class SettingsActivity extends AppCompatActivity {
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getIntent() != null){
            String type = getIntent().getStringExtra("type");
            switch (type){
                case "profile":
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    break;
                case "pass":
                    fragment = new PasswordFragment();
                    loadFragment(fragment);
                    break;
                case "about":
                    fragment = new AboutFragment();
                    loadFragment(fragment);
                    break;
            }
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_settings, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
