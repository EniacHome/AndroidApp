package com.eniacdevelopment.eniachometwo.Fragments;

/**
 * Created by denni on 1/4/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.eniacdevelopment.eniachometwo.Activities.MainActivity;
import com.eniacdevelopment.eniachometwo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

public class SettingsFragment extends IFragment {

    private View view;
    EditText settingsIpAddressEditText, settingsPortNumberEditText;
    Button settingsCancelButton, settingsSaveButton;
    SharedPreferences sharedPref;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);


        initFragmentData();

        return view;
    }

    private void initFragmentData() {
        String defaultValue;

        sharedPref = ((MainActivity)getActivity()).getSettings();

        settingsIpAddressEditText = (EditText) view.findViewById(R.id.settings_ip_address_editText);
        settingsPortNumberEditText = (EditText) view.findViewById(R.id.settings_port_number_editText);

        defaultValue = getResources().getString(R.string.default_ip);
        settingsIpAddressEditText.setText(sharedPref.getString("Ip-Address", defaultValue));
        defaultValue = getResources().getString(R.string.default_port);
        settingsPortNumberEditText.setText(sharedPref.getString("Port-Number", defaultValue));

        settingsCancelButton = (Button) view.findViewById(R.id.settings_cancel);
        settingsCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });
        settingsSaveButton = (Button) view.findViewById(R.id.settings_save);
        settingsSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPref.edit();
                if (!settingsIpAddressEditText.getText().toString().isEmpty())
                    editor.putString("Ip-Address", settingsIpAddressEditText.getText().toString()).apply();
                if (!settingsPortNumberEditText.getText().toString().isEmpty())
                    editor.putString("Port-Number", settingsPortNumberEditText.getText().toString()).apply();

                ((MainActivity)getActivity()).loadClientSettings();
                ((MainActivity)getActivity()).onBackPressed();
            }
        });

    }

    @Override
    public int getFragmentTitle() {
        return 3;
    }

    @Override
    public String getFragmentTag() {
        return "FRAGMENT_SETTINGS";
    }

}
