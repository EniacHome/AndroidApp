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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.eniacdevelopment.EniacHome.DataModel.Sensor.Sensor;
import com.eniacdevelopment.EniacHome.DataModel.Sensor.SensorType;
import com.eniacdevelopment.eniachometwo.Activities.MainActivity;
import com.eniacdevelopment.eniachometwo.Adapters.SensorListAdapter;
import com.eniacdevelopment.eniachometwo.LayoutModels.SensorListModel;
import com.eniacdevelopment.eniachometwo.R;
import com.eniacdevelopment.eniachometwo.Wrappers.SensorWrapper;

import java.util.ArrayList;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

public class SensorsFragment extends IFragment implements SwipeRefreshLayout.OnRefreshListener {

    SparseArray<SensorListModel> groups = new SparseArray<SensorListModel>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ExpandableListView listView;
    private SensorListAdapter adapter;
    private WebTarget webTarget;
    View view;

    public SensorsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sensors, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        webTarget = ((MainActivity)getActivity()).getWebTarget();

        createData();

        listView = (ExpandableListView) view.findViewById(R.id.SensorsList);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.SwipeRefreshSensors);
        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    private void createData() {
        try {
            new GetSensorsTask().execute();
        } catch (ProcessingException e) {
            Toast.makeText(view.getContext(),getResources().getString(R.string.communication_failed),Toast.LENGTH_SHORT);
            ((MainActivity)getActivity()).onBackPressed();
        }
    }

    public void editSensor(Sensor sensor) {
        Bundle bundle = new Bundle();
        SensorWrapper wrapper = new SensorWrapper();
        wrapper.sensor = sensor;
        bundle.putSerializable("sensor",wrapper);
        Fragment editFragment = new EditSensorFragment();
        editFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, editFragment).addToBackStack(null).commit();
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
                createData();
                listView.setAdapter(adapter);
            }
        },2000);
    }

    @Override
    public int getFragmentTitle() {
        return 1;
    }

    @Override
    public String getFragmentTag() {
        return "FRAGMENT_SENSORS";
    }

    class GetSensorsTask extends AsyncTask<Void,Void,Iterable<Sensor>> {

        @Override
        protected Iterable<Sensor> doInBackground(Void... params) {

            try {
                Response response =  webTarget.path("sensor").request().get();
                return response.readEntity(new GenericType<Iterable<Sensor>>(){});
            } catch (RuntimeException e) {
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(SensorsFragment.this.getActivity(),getString(R.string.get_data),Toast.LENGTH_SHORT);
        }

        @Override
        protected void onPostExecute(Iterable<Sensor> sensors) {
            for(SensorType type : SensorType.values()) {
                if (type == SensorType.Unknown)
                    continue;

                SensorListModel group = new SensorListModel(getResources().getStringArray(R.array.sensor_types_usable)[type.ordinal()]);

                for (Sensor sensor : sensors){
                    if(sensor.SensorType == type) {
                        group.sensors.add(sensor);
                    }
                }

                groups.append(type.ordinal(), group);
            }
//            if (!(groups.size() > 0)) {
//                Toast.makeText((MainActivity)getActivity(),getString(R.string.communication_failed),Toast.LENGTH_SHORT);
//            }

            adapter = new SensorListAdapter(SensorsFragment.this,groups);
            listView.setAdapter(adapter);
        }
    }
}

