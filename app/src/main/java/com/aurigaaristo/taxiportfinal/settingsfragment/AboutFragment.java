package com.aurigaaristo.taxiportfinal.settingsfragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aurigaaristo.taxiportfinal.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {
    public AboutFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_about, container, false);
    }

}
