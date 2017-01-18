package com.turtledev.pocketcounter;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.turtledev.pocketcounter.mDataBase.DBAdapterRx;
import com.turtledev.pocketcounter.mFragments.AbstractFragment;
import com.turtledev.pocketcounter.mFragments.ChargesDictFragment;
import com.turtledev.pocketcounter.mFragments.ChargesFragment;
import com.turtledev.pocketcounter.mFragments.DetailFragment;
import com.turtledev.pocketcounter.mHelpers.MyHelper;
import com.turtledev.pocketcounter.mInterfaces.Callbacks;

import static com.turtledev.pocketcounter.mHelpers.MyHelper.clearLoginResponseData;

/**
 * Created by PavloByinyk.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Callbacks {

    private ChargesDictFragment chargeDictFragment;
    private ChargesFragment chargesFragment;
    private AbstractFragment fragment;
    private FragmentManager fm;
    private FloatingActionButton fab;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setSupportActionBar(toolbar);
        initNavigationView();
        initTabs();
        fabOnclick();
        initToggle();
        tabLayoutListener();
        initFirstFragment();
        // check if there was a fragment. If not , add a new chargeDictFragment
        if (savedInstanceState == null) {
            fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, chargeDictFragment, chargeDictFragment.getTagFragment());
            ft.commit();
        }
    }
    // init two first fragments
    private void initFirstFragment(){
        chargeDictFragment = new ChargesDictFragment();
        chargesFragment = new ChargesFragment();
        fragment = chargeDictFragment;
    }
    // init ActionBarDrawerToggle with DrawerLayout
    private void initToggle(){
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }
    private void tabLayoutListener(){
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        clearBackStack();
                        showFragment(chargesFragment);
                        navigationView.getMenu().getItem(0).setChecked(true);
                        break;
                    case 1:
                        clearBackStack();
                        showFragment(chargeDictFragment);
                        navigationView.getMenu().getItem(1).setChecked(true);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    // fab click call fragments method work()
    private void fabOnclick(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                fragment = (AbstractFragment) fm.findFragmentById(fragment.GetContFragment());
                fragment.work();
            }
        });
    }
    private void findViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }
    private void initNavigationView(){
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        ((TextView)header.findViewById(R.id.nav_view_tv_name)).setText( getResources().getString(R.string.nav_view_name , MyHelper.getLoginResponseData(this).getName().toString()));
        ((TextView)header.findViewById(R.id.nav_view_tv_email)).setText(MyHelper.getLoginResponseData(this).getEmail().toString());
    }
    private void initTabs() {
        tabLayout = (TabLayout) findViewById(R.id.viewpagertab);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_launcher_charges_dict));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_launcher_charge));
        tabLayout.getTabAt(1).select();
    }
    @Override
    public void onBackPressed() {
        FragmentTransaction ft = fm.beginTransaction();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
        ft.commit();
    }
    public void clearBackStack(){
       fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_charges) {
            showFragment(chargesFragment);
            tabLayout.getTabAt(0).select();
        } else if (id == R.id.nav_charDictionary) {
            showFragment(chargeDictFragment);
            tabLayout.getTabAt(1).select();
        } else if (id == R.id.nav_logout) {
            logOutPress();
        } else if (id == R.id.nav_help) {}
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    // replace fragments in container
    public void showFragment(AbstractFragment frag) {
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(frag.GetContFragment(), frag, frag.getTagFragment());
        ft.commit();
    }
    // clear all data ( DB , User info) get back on LoginActivity
    public void logOutPress() {
        clearLoginResponseData(this);
        DBAdapterRx.getDBAdapterRx(this).deleteDB();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    // finish this Activity
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
    public FloatingActionButton getFab() {
        return fab;
    }
    public ActionBarDrawerToggle getToggle() {
        return toggle;
    }
    @Override
    public void onChargesSelected(String name) {
        Fragment newDetail = DetailFragment.newInstance(name);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, newDetail)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void updateUiInFragment() {
        fragment = (AbstractFragment) fm.findFragmentById(fragment.GetContFragment());
        fragment.updateUI();
    }
}
