package com.eniacdevelopment.eniachometwo.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.eniacdevelopment.eniachometwo.DrawerItemCustomAdapter;
import com.eniacdevelopment.eniachometwo.Fragments.*;
import com.eniacdevelopment.eniachometwo.LayoutModels.DataModel;
import com.eniacdevelopment.eniachometwo.R;

public class MainActivity extends AppCompatActivity {

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private IFragment fragment = null;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private final int Home = 0;

    private IFragment[] ListOfFragments = {new HomeFragment(), new SensorsFragment(), new SecurityFragment(), new SettingsFragment(),
            new TemperatureFragment(), new InformationFragment(), new UserFragment()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();

        DataModel[] drawerItem = new DataModel[ListOfFragments.length];

        drawerItem[0] = new DataModel(R.drawable.ic_home, "Home");
        drawerItem[1] = new DataModel(R.drawable.ic_sensors, getString(R.string.sensors));
        drawerItem[2] = new DataModel(R.drawable.ic_locked, getString(R.string.security));
        drawerItem[3] = new DataModel(R.drawable.ic_settings, getString(R.string.settings));
        drawerItem[4] = new DataModel(R.drawable.ic_temperature, getString(R.string.temperature));
        drawerItem[5] = new DataModel(R.drawable.ic_information_rotate, getString(R.string.information));
        drawerItem[6] = new DataModel(R.drawable.ic_user, getString(R.string.user));
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