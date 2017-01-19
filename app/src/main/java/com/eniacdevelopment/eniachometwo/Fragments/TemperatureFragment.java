package com.eniacdevelopment.eniachometwo.Fragments;

/**
 * Created by denni on 1/4/2017.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.eniacdevelopment.EniacHome.DataModel.Sensor.Sensor;
import com.eniacdevelopment.EniacHome.DataModel.Sensor.SensorType;
import com.eniacdevelopment.eniachometwo.Activities.MainActivity;
import com.eniacdevelopment.eniachometwo.Adapters.TemperatureListAdapter;
import com.eniacdevelopment.eniachometwo.LayoutModels.TemperatureListModel;
import com.eniacdevelopment.eniachometwo.R;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

public class TemperatureFragment extends IFragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private WebTarget webTarget;
    private ArrayAdapter<TemperatureListModel> adapter;

    public TemperatureFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_temperature, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        webTarget = ((MainActivity)getActivity()).getWebTarget();

        createData();

        listView = (ListView) view.findViewById(R.id.TemperatureList);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.SwipeRefreshTemperatures);
        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    private void createData() {
        try {
            new GetTemperaturesTask().execute();
        } catch (ProcessingException e) {
            Toast.makeText(view.getContext(),getResources().getString(R.string.communication_failed),Toast.LENGTH_SHORT);
            ((MainActivity)getActivity()).onBackPressed();
        }
    }

    @Override
    public int getFragmentTitle() {
        return 4;
    }

    @Override
    public String getFragmentTag() {
        return "FRAGMENT_TEMPERATURE";
    }

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
                createData();
            }
        },2000);
    }

    class GetTemperaturesTask extends AsyncTask<Void,Void,Iterable<Sensor>> {

        @Override
        protected Iterable<Sensor> doInBackground(Void... params) {
            try {
                Response response = webTarget.path("sensor").path("type").path(SensorType.TemperatureSensor.name()).request().get();
                return response.readEntity(new GenericType<Iterable<Sensor>>(){});
            } catch (RuntimeException e) {
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(Iterable<Sensor> sensors) {
            List<TemperatureListModel> list = new ArrayList<TemperatureListModel>();
            for(Sensor sensor: sensors) {
                list.add(new TemperatureListModel(sensor));
            }
            adapter = new TemperatureListAdapter(TemperatureFragment.this,
                    list);
            listView.setAdapter(adapter);
        }
    }
}
