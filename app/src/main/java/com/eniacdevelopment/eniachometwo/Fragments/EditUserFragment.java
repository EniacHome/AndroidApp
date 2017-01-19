package com.eniacdevelopment.eniachometwo.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.eniacdevelopment.EniacHome.DataModel.Sensor.SensorType;
import com.eniacdevelopment.EniacHome.DataModel.User.User;
import com.eniacdevelopment.EniacHome.DataModel.User.UserRole;
import com.eniacdevelopment.eniachometwo.Activities.MainActivity;
import com.eniacdevelopment.eniachometwo.R;
import com.eniacdevelopment.eniachometwo.Wrappers.UserWrapper;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

/**
 * Created by denni on 1/16/2017.
 */

public class EditUserFragment extends Fragment {

    private View view;
    private WebTarget webTarget;
    private User user, newUser;
    private EditText userNameEditText, userPasswordEditText, userFirstNameEditText, userLastNameEditText;
    private Spinner userRoleSpinner;
    private Button userCancelButton, userSaveButton;


    public EditUserFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_user, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        webTarget = ((MainActivity)getActivity()).getWebTarget();

        Bundle args = getArguments();
        UserWrapper wrapper = (UserWrapper) args.getSerializable("user");
        this.user = wrapper.user;

        SetFragmentData();

        return view;
    }

    private void SetFragmentData() {
        userNameEditText = (EditText) view.findViewById(R.id.edit_user_name);
        userPasswordEditText = (EditText) view.findViewById(R.id.edit_user_password);
        userFirstNameEditText = (EditText) view.findViewById(R.id.edit_user_first_name);
        userLastNameEditText = (EditText) view.findViewById(R.id.edit_user_last_name);
        userRoleSpinner = (Spinner) view.findViewById(R.id.edit_user_role);

        userCancelButton = (Button) view.findViewById(R.id.edit_user_cancel);
        userSaveButton = (Button) view.findViewById(R.id.edit_user_save);

        userNameEditText.setText(user.Username);
        userPasswordEditText.setText(user.PasswordHash);
        userFirstNameEditText.setText(user.Firstname);
        userLastNameEditText.setText(user.Lastname);
        userRoleSpinner.setSelection(UserRole.valueOf(user.Role.name()).ordinal());

        userCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });
        userSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUser = user;
                newUser.Username = userNameEditText.getText().toString().isEmpty() ? user.Username:userNameEditText.getText().toString();
                newUser.PasswordHash = userPasswordEditText.getText().toString().isEmpty() ? user.PasswordHash:userPasswordEditText.getText().toString();
                newUser.Firstname = userFirstNameEditText.getText().toString().isEmpty() ? user.Firstname:userFirstNameEditText.getText().toString();
                newUser.Lastname = userLastNameEditText.getText().toString().isEmpty() ? user.Lastname:userLastNameEditText.getText().toString();
                newUser.Role = UserRole.valueOf(getResources().getStringArray(R.array.user_roles_usable)[userRoleSpinner.getSelectedItemPosition()]);
                try {
                    new setUserTask().execute();
                    Toast.makeText(view.getContext().getApplicationContext(),getResources().getString(R.string.saved_changed),Toast.LENGTH_SHORT);
                } catch (ProcessingException e) {
                    Toast.makeText(view.getContext(),getResources().getString(R.string.communication_failed),Toast.LENGTH_SHORT);
                    ((MainActivity)getActivity()).onBackPressed();
                }
            }
        });
    }

    class setUserTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            webTarget.path("user").request().put(Entity.json(newUser));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ((MainActivity)getActivity()).onBackPressed();
        }
    }
}
