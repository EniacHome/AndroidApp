package com.eniacdevelopment.eniachometwo.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.eniacdevelopment.EniacHome.DataModel.Sensor.Sensor;
import com.eniacdevelopment.eniachometwo.Fragments.IFragment;
import com.eniacdevelopment.eniachometwo.Fragments.SensorsFragment;
import com.eniacdevelopment.eniachometwo.Fragments.UserFragment;
import com.eniacdevelopment.eniachometwo.LayoutModels.UserListModel;
import com.eniacdevelopment.eniachometwo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denni on 1/16/2017.
 */

public class UserListAdapter extends ArrayAdapter<UserListModel> {
    private final List<UserListModel> list;
    private final IFragment fragment;
    private final LayoutInflater inflater;
    private TextView UserNameText, UserRoleText;

    public UserListAdapter(IFragment fragment, List<UserListModel> list) {
        super(fragment.getContext(), R.layout.user_list_row, list);
        this.fragment = fragment;
        this.list = list;
        inflater = fragment.getActivity().getLayoutInflater();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.user_list_row, null);
        }
        UserNameText = (TextView) convertView.findViewById(R.id.UserName_textField);
        UserRoleText = (TextView) convertView.findViewById(R.id.UserRole_textField);
        UserNameText.setText(list.get(position).getUserName());
        UserRoleText.setText(list.get(position).getUserRole());
        convertView.setClickable(true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((UserFragment)fragment).editSensor(list.get(position).getUser());
            }
        });
        return convertView;
    }
}
