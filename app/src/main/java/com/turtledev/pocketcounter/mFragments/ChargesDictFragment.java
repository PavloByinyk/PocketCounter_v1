package com.turtledev.pocketcounter.mFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.turtledev.pocketcounter.MainActivity;
import com.turtledev.pocketcounter.R;
import com.turtledev.pocketcounter.mDataBase.DBAdapterRx;
import com.turtledev.pocketcounter.mInterfaces.Callbacks;
import com.turtledev.pocketcounter.models.Charges;
import com.turtledev.pocketcounter.recyclerAdapter.ChargesDictAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by PavloByinyk.
 * Fragment that shows all charges names
 */
public class ChargesDictFragment extends AbstractFragment implements SearchView.OnQueryTextListener {

    private static final String TAG = ChargesDictFragment.class.getName();
    public static final int ADD_CHARGE_D_ITEM = 1001;
    private View view;
    private AbstractFragment mFragment;
    private ChargesDictAdapter chargesDictAdapter;
    private RecyclerView chargesDictRecyclerView;
    private List<Charges> list;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_charges_dict_layout, container, false);
        initRecyclerView();
        updateUI();
        setActionBar();
        setHasOptionsMenu(true);
        changeFAB();
        return view;
    }
    public ChargesDictFragment() {}
    private void initRecyclerView(){
        chargesDictRecyclerView = (RecyclerView) view.findViewById(R.id.rv_charge_dictionary);
        chargesDictRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    private void changeFAB(){
        FloatingActionButton fab=((MainActivity) getActivity()).getFab();
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_plus));
    }
    private void setActionBar() {
        android.support.v7.app.ActionBar ab = ((MainActivity) getActivity()).getSupportActionBar();
        ab.setTitle((getResources().getString(R.string.charges_dictionary)));
        ((MainActivity) getActivity()).getToggle().setDrawerIndicatorEnabled(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_charges_fragment, menu);
        MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(searchItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem searchItem) {
                        chargesDictAdapter.setFilter(list);
                        return true;
                    }
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem searchItem) {
                        return true;
                    }
                });
        super.onCreateOptionsMenu(menu, inflater);
    }
    private List<Charges> filter(List<Charges> list, String query) {
        query = query.toLowerCase();
        final List<Charges> filterList = new ArrayList<Charges>();
        for (Charges charges : list) {
            final String text = charges.getName().toLowerCase();
            if (text.contains(query)) {
                filterList.add(charges);
            }
        }
        return filterList;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        List<Charges> mfilterList = filter(list, newText);
        chargesDictAdapter.setFilter(mfilterList);
        return true;
    }
    @Override
    public String getTagFragment() {
        return this.TAG;
    }
    @Override
    public void work() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (mFragment == null) {
            mFragment = new AddChargesDictFragment();
        }
        mFragment.setTargetFragment(this, ADD_CHARGE_D_ITEM);
        ft.replace(this.GetContFragment(), mFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ADD_CHARGE_D_ITEM:
                    String newCharge = data.getStringExtra(AddChargesDictFragment.ADD_RESULT);
                    Toast.makeText(getActivity(), "Charges " + newCharge.toString() + " successful add", Toast.LENGTH_LONG).show();
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
    public void updateUI(){
        list= new ArrayList<>();
        DBAdapterRx.makeObservable(DBAdapterRx.getDBAdapterRx(getActivity())
                .getDBListCharges())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Charges>>() {
                    @Override
                    public void call(List<Charges> charges) {
                        list=charges;
                        if (chargesDictAdapter == null) {
                            chargesDictAdapter = new ChargesDictAdapter(list,getActivity(),(Callbacks) getActivity());
                            chargesDictRecyclerView.setAdapter(chargesDictAdapter);
                        } else {
                            chargesDictAdapter.setListCharges(list);
                            chargesDictRecyclerView.setAdapter(chargesDictAdapter);
                        }
                    }
                });
    }
}
