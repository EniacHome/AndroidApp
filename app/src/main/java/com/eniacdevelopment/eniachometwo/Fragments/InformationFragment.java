package com.eniacdevelopment.eniachometwo.Fragments;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eniacdevelopment.eniachometwo.R;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Created by denni on 1/5/2017.
 */

public class InformationFragment extends IFragment{

    Client client;
    View view;

    public InformationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_information, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
        }
        client = ClientBuilder.newClient();

        return view;
    }

    @Override
    public int getFragmentTitle() {
        return 5;
    }

    @Override
    public String getFragmentTag() {
        return "FRAGMENT_INFORMATION";
    }
}
