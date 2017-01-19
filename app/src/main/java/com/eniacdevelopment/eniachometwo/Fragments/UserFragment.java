package com.eniacdevelopment.eniachometwo.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.eniacdevelopment.EniacHome.DataModel.User.User;
import com.eniacdevelopment.eniachometwo.Activities.MainActivity;
import com.eniacdevelopment.eniachometwo.Adapters.UserListAdapter;
import com.eniacdevelopment.eniachometwo.LayoutModels.UserListModel;
import com.eniacdevelopment.eniachometwo.R;
import com.eniacdevelopment.eniachometwo.Wrappers.UserWrapper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 * Created by denni on 1/5/2017.
 */

public class UserFragment extends IFragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private WebTarget webTarget;
    private ListView listView;
    private ArrayAdapter<UserListModel> adapter;

    public UserFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        webTarget = ((MainActivity)getActivity()).getWebTarget();

        createData();

        listView = (ListView) view.findViewById(R.id.UsersList);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.SwipeRefreshUsers);
        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    private void createData() {
        try {
            new GetUsersTask().execute();
        } catch (ProcessingException e) {
            Toast.makeText(view.getContext(),getResources().getString(R.string.communication_failed),Toast.LENGTH_SHORT);
            ((MainActivity)getActivity()).onBackPressed();
        }
    }

    public void editSensor(User user) {
        Bundle bundle = new Bundle();
        UserWrapper wrapper = new UserWrapper();
        wrapper.user = user;
        bundle.putSerializable("user",wrapper);
        Fragment editFragment = new EditUserFragment();
        editFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, editFragment).addToBackStack(null).commit();
    }

    @Override
    public int getFragmentTitle() {
        return 6;
    }

    @Override
    public String getFragmentTag() {
        return "FRAGMENT_USER";
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
            }
        },2000);
    }

    class GetUsersTask extends AsyncTask<Void,Void,Iterable<User>> {

        @Override
        protected Iterable<User> doInBackground(Void... params) {
            try {
                Response response = webTarget.path("user").request().get();
                return response.readEntity(new GenericType<Iterable<User>>(){});
            } catch (RuntimeException e) {
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(Iterable<User> users) {
            List<UserListModel> list = new ArrayList<UserListModel>();
            for(User user: users) {
                list.add(new UserListModel(user));
            }
            adapter = new UserListAdapter(UserFragment.this,
                    list);
            listView.setAdapter(adapter);
        }
    }
}
