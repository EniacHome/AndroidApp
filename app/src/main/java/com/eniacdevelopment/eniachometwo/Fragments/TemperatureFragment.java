package com.eniacdevelopment.eniachometwo.Fragments;

/**
 * Created by denni on 1/4/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eniacdevelopment.eniachometwo.R;

public class TemperatureFragment extends Fragment {

    public TemperatureFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_temperature, container, false);

        return rootView;
    }

}
