package com.eniacdevelopment.eniachometwo.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eniacdevelopment.eniachometwo.Fragments.IFragment;
import com.eniacdevelopment.eniachometwo.LayoutModels.TemperatureListModel;
import com.eniacdevelopment.eniachometwo.R;

import java.util.List;

/**
 * Created by denni on 1/17/2017.
 */

public class TemperatureListAdapter extends ArrayAdapter<TemperatureListModel>{
    private final List<TemperatureListModel> list;
    private final IFragment fragment;
    private final LayoutInflater inflater;
    private TextView TemperatureNameText, TemperatureStatusText;

    public TemperatureListAdapter(IFragment fragment, List<TemperatureListModel> list) {
        super(fragment.getContext(), R.layout.temperatur_list_row, list);
        this.fragment = fragment;
        this.list = list;
        inflater = fragment.getActivity().getLayoutInflater();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.temperatur_list_row, null);
        }
        TemperatureNameText = (TextView) convertView.findViewById(R.id.TemperatureSensorName);
        TemperatureStatusText = (TextView) convertView.findViewById(R.id.TemperatureStatus);
        TemperatureNameText.setText(list.get(position).getSensorName());
        TemperatureStatusText.setText(Double.toString(list.get(position).getTemperature()));
        return convertView;
    }
}
