package com.eniacdevelopment.eniachometwo.Fragments;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.eniacdevelopment.EniacHome.DataModel.Sensor.CompareMethod;
import com.eniacdevelopment.EniacHome.DataModel.Sensor.Sensor;
import com.eniacdevelopment.EniacHome.DataModel.Sensor.SensorType;
import com.eniacdevelopment.eniachometwo.Activities.MainActivity;
import com.eniacdevelopment.eniachometwo.R;
import com.eniacdevelopment.eniachometwo.Wrappers.SensorWrapper;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

/**
 * Created by denni on 1/15/2017.
 */

public class EditSensorFragment extends Fragment {

    private View view;
    private WebTarget webTarget;
    private Sensor sensor, newSensor;
    private EditText sensorNameEditText, sensorLevelEditText, sensorCompareLevel;
    private Spinner sensorTypeSpinner, sensorCompareMethod;
    private Switch sensorAlarmSwitch;
    private Button sensorCancelButton, sensorSaveButton;


    public EditSensorFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_sensor, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        webTarget = ((MainActivity)getActivity()).getWebTarget();

        Bundle args = getArguments();
        SensorWrapper wrapper = (SensorWrapper) args.getSerializable("sensor");
        this.sensor = wrapper.sensor;

        setFragmentData();

        return view;
    }

    private void setFragmentData() {
        sensorNameEditText = (EditText) view.findViewById(R.id.edit_sensor_name);
        sensorTypeSpinner = (Spinner) view.findViewById(R.id.edit_sensor_type);
        sensorAlarmSwitch = (Switch) view.findViewById(R.id.edit_sensor_alarm);
        sensorLevelEditText = (EditText) view.findViewById(R.id.edit_sensor_alarm_level);
        sensorCompareMethod = (Spinner) view.findViewById(R.id.edit_sensor_compare_method);
        sensorCompareLevel = (EditText) view.findViewById(R.id.edit_sensor_compare_level);
        sensorCancelButton = (Button) view.findViewById(R.id.edit_sensor_cancel);
        sensorSaveButton = (Button) view.findViewById(R.id.edit_sensor_save);

        sensorNameEditText.setText(sensor.Name, EditText.BufferType.NORMAL);
        sensorTypeSpinner.setSelection(sensor.SensorType.ordinal());
        sensorAlarmSwitch.setChecked(sensor.Enabled);
        sensorAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(view.getContext(),"The sensor alarm has been " + (sensorAlarmSwitch.isChecked() ? "activated":"disabled"),Toast.LENGTH_SHORT).show();
            }
        });
        sensorLevelEditText.setText(Integer.toString(sensor.Level));
        sensorCompareMethod.setSelection(sensor.CompareMethod.ordinal());
        sensorCompareLevel.setText(Integer.toString(sensor.CompareValue));
        sensorCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });
        sensorSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newSensor = sensor;
                newSensor.Name = sensorNameEditText.getText().toString().isEmpty() ? sensor.Name:sensorNameEditText.getText().toString();
                newSensor.SensorType = SensorType.valueOf(getResources().getStringArray(R.array.sensor_types)[sensorTypeSpinner.getSelectedItemPosition()]);
                newSensor.Enabled = sensorAlarmSwitch.isEnabled();
                newSensor.Level = Integer.parseInt(sensorLevelEditText.getText().toString());
                newSensor.CompareMethod = CompareMethod.valueOf(getResources().getStringArray(R.array.sensor_compare_method)[sensorCompareMethod.getSelectedItemPosition()]);
                try {
                    new setSensorTask().execute();
                    Toast.makeText(view.getContext().getApplicationContext(),getResources().getString(R.string.saved_changed),Toast.LENGTH_SHORT);
                } catch (ProcessingException e) {
                    Toast.makeText(view.getContext(),getResources().getString(R.string.communication_failed),Toast.LENGTH_SHORT);
                    ((MainActivity)getActivity()).onBackPressed();
                }
            }
        });
    }

    class setSensorTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            webTarget.path("sensor").request().put(Entity.json(newSensor));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ((MainActivity)getActivity()).onBackPressed();
        }
    }
}
