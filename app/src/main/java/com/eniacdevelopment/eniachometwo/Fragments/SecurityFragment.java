package com.eniacdevelopment.eniachometwo.Fragments;

/**
 * Created by denni on 1/4/2017.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.eniacdevelopment.EniacHome.DataModel.Alarm.AlarmStatus;
import com.eniacdevelopment.eniachometwo.Activities.MainActivity;
import com.eniacdevelopment.eniachometwo.R;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

public class SecurityFragment extends IFragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private WebTarget webTarget;
    Switch alarmSwitch;
    EditText alarmLevelEditText;


    public SecurityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_security, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        webTarget = ((MainActivity)getActivity()).getWebTarget();

        initFragmentData();

        return view;
    }

    private void initFragmentData() {
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.SwipeRefreshSecurity);
        swipeRefreshLayout.setOnRefreshListener(this);
        alarmSwitch = (Switch) view.findViewById(R.id.alarm_switch);
        alarmLevelEditText = (EditText) view.findViewById(R.id.alarm_level);
        new getAlarmDataTask().execute();
    }

    @Override
    public int getFragmentTitle() {
        return 2;
    }

    @Override
    public String getFragmentTag() {
        return "FRAGMENT_SECURITY";
    }

    @Override
    public void onPause() {
        super.onPause();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(false);
            swipeRefreshLayout.destroyDrawingCache();
            swipeRefreshLayout.clearAnimation();
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                new getAlarmDataTask().execute();
            }
        },2000);
    }

    class getAlarmDataTask extends AsyncTask<Void,Void,AlarmStatus> {

        @Override
        protected AlarmStatus doInBackground(Void... params) {
            try {
                Response response = webTarget.path("alarm").request().get();
                return response.readEntity(AlarmStatus.class);
            } catch (RuntimeException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final AlarmStatus status) {
            if (status != null) {
                alarmSwitch.setChecked(status.Enabled);
                alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (alarmSwitch.isChecked()) {
                            String alarmLevel = alarmLevelEditText.getText().toString().isEmpty() ? String.valueOf(status.Level) : alarmLevelEditText.getText().toString();
                            webTarget.path("alarm").path("enable").path(alarmLevel).request().get();
                        } else {
                            webTarget.path("alarm").path("disable").request().get();
                        }

                    }
                });
                alarmLevelEditText.setText(String.valueOf(status.Level));
            } else {
                Toast.makeText(getContext(),getString(R.string.communication_failed),Toast.LENGTH_LONG);
            }
        }
    }
}