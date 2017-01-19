package com.eniacdevelopment.eniachometwo.Adapters;

/**
 * Created by denni on 1/6/2017.
 */

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.eniacdevelopment.EniacHome.DataModel.Sensor.Sensor;
import com.eniacdevelopment.eniachometwo.Fragments.IFragment;
import com.eniacdevelopment.eniachometwo.Fragments.SensorsFragment;
import com.eniacdevelopment.eniachometwo.LayoutModels.SensorListModel;
import com.eniacdevelopment.eniachometwo.R;

import java.security.acl.Group;

public class SensorListAdapter extends BaseExpandableListAdapter {

    private final SparseArray<SensorListModel> groups;
    public LayoutInflater inflater;
    public IFragment fragment;

    public SensorListAdapter(IFragment fragment, SparseArray<SensorListModel> groups) {
        this.fragment = fragment;
        this.groups = groups;
        inflater = fragment.getActivity().getLayoutInflater();
    }

    @Override
    public Sensor getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).sensors.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final Sensor sensor = getChild(groupPosition, childPosition);
        final String sensorName = sensor.Name;
        TextView text = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_details, null);
        }
        text = (TextView) convertView.findViewById(R.id.textView1);
        text.setText(sensorName);
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SensorsFragment)fragment).editSensor(sensor);
//                Toast.makeText(fragment.getActivity(), sensor.Id,
//                        Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).sensors.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_group, null);
        }
        SensorListModel group = (SensorListModel) getGroup(groupPosition);
        ((CheckedTextView) convertView).setText(group.sensorType.toString());
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}