package com.eniacdevelopment.eniachometwo.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.eniacdevelopment.EniacHome.DataModel.Sensor.Sensor;
import com.eniacdevelopment.EniacHome.DataModel.Sensor.SensorType;
import com.eniacdevelopment.eniachometwo.Adapters.DrawerItemCustomAdapter;
import com.eniacdevelopment.eniachometwo.Fragments.*;
import com.eniacdevelopment.eniachometwo.LayoutModels.NavItemModel;
import com.eniacdevelopment.eniachometwo.LayoutModels.SensorListModel;
import com.eniacdevelopment.eniachometwo.R;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

public class MainActivity extends AppCompatActivity {

    public final static String WEBTARGET_INTENT_KEY = "Webtarget";

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private IFragment fragment = null;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private final int Home = 0;
    private WebTarget webTarget;
    SparseArray<SensorListModel> groups = new SparseArray<SensorListModel>();
    private Client client;
    SharedPreferences sharedPref;

    private IFragment[] ListOfFragments = {new HomeFragment(), new SensorsFragment(), new SecurityFragment(), new SettingsFragment(),
            new TemperatureFragment(), new InformationFragment(), new UserFragment()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        sharedPref = getSharedPreferences("Preferences", 0);
        loadSettings();

        client = (Client)ClientBuilder.newClient().register(JacksonJsonProvider.class);

        loadClientSettings();

        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();

        NavItemModel[] drawerItem = new NavItemModel[ListOfFragments.length];

        drawerItem[0] = new NavItemModel(R.drawable.ic_home, "Home");
        drawerItem[1] = new NavItemModel(R.drawable.ic_sensors, getString(R.string.sensors));
        drawerItem[2] = new NavItemModel(R.drawable.ic_locked, getString(R.string.security));
        drawerItem[3] = new NavItemModel(R.drawable.ic_settings, getString(R.string.settings));
        drawerItem[4] = new NavItemModel(R.drawable.ic_temperature, getString(R.string.temperature));
        drawerItem[5] = new NavItemModel(R.drawable.ic_information_rotate, getString(R.string.information));
        drawerItem[6] = new NavItemModel(R.drawable.ic_user, getString(R.string.user));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();

        fragment = new HomeFragment();
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, fragment.getFragmentTag()).commit();
            mDrawerList.setItemChecked(Home, true);
            mDrawerList.setSelection(Home);
            setTitle(mNavigationDrawerItemTitles[Home]);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    private void loadSettings() {
        if (!sharedPref.contains("Ip-Address") || !sharedPref.contains("Port-Number")) {
            SharedPreferences.Editor editor = sharedPref.edit();
            if (!sharedPref.contains("Ip-Address"))
                editor.putString("Ip-Address", "127.0.0.1").apply();
            if (!sharedPref.contains("Port-Number"))
                editor.putString("Port-Number", "9090").apply();
        }
    }

    public void loadClientSettings() {
        String ipAddress = null;
        String portNumber = null;
        String defaultValue = null;

        defaultValue = getResources().getString(R.string.default_ip);
        ipAddress = sharedPref.getString("Ip-Address", defaultValue);
        defaultValue = getResources().getString(R.string.default_port);
        portNumber = sharedPref.getString("Port-Number", defaultValue);

        this.webTarget = client.target("http://" + ipAddress + ":" + portNumber +"/service/");
    }


    public void onButtonClick(View view) {
    }

    public SharedPreferences getSettings() {
        return sharedPref;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    public void selectItem(int position) {
        fragment = ListOfFragments[position];


        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, fragment.getFragmentTag()).addToBackStack(null).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            if (fragment == getSupportFragmentManager().findFragmentByTag("FRAGMENT_HOME")){
                finish();
            } else {
                super.onBackPressed();
                IFragment prevFragment = (IFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
                mDrawerList.setItemChecked(prevFragment.getFragmentTitle(), true);
                mDrawerList.setSelection(prevFragment.getFragmentTitle());
                setTitle(mNavigationDrawerItemTitles[prevFragment.getFragmentTitle()]);
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    public WebTarget getWebTarget() {
        return webTarget;
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }
}