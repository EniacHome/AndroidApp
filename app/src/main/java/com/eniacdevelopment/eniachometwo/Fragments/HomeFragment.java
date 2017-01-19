package com.eniacdevelopment.eniachometwo.Fragments;

/**
 * Created by denni on 12/30/2016.
 */

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.eniacdevelopment.eniachometwo.Activities.MainActivity;
import com.eniacdevelopment.eniachometwo.R;

public class HomeFragment extends IFragment implements View.OnClickListener {

    private FrameLayout frameLayout;
    private int[] buttonIds = {R.id.SensorsButton, R.id.SecurityButton, R.id.SettingsButton, R.id.InformationButton, R.id.TemperatureButton, R.id.UserButton};

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for (int i = 0; i < buttonIds.length; i++) {
            frameLayout = (FrameLayout) getActivity().findViewById(buttonIds[i]);
            frameLayout.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        final View view = v;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (view.getId()) {
                    case R.id.SensorsButton:
                        ((MainActivity)getActivity()).selectItem(1);
                        break;
                    case R.id.SecurityButton:
                        ((MainActivity)getActivity()).selectItem(2);
                        break;
                    case R.id.SettingsButton:
                        ((MainActivity)getActivity()).selectItem(3);
                        break;
                    case R.id.TemperatureButton:
                        ((MainActivity)getActivity()).selectItem(4);
                        break;
                    case R.id.InformationButton:
                        ((MainActivity)getActivity()).selectItem(5);
                        break;
                    case R.id.UserButton:
                        ((MainActivity)getActivity()).selectItem(6);
                        break;
                }
            }
        }, 300);
    }

    @Override
    public int getFragmentTitle() {
        return 0;
    }

    @Override
    public String getFragmentTag() {
        return "FRAGMENT_HOME";
    }
}

