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

public class SecurityFragment extends Fragment {

    public SecurityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_security, container, false);

        return rootView;
    }

}