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
import com.turtledev.pocketcounter.models.DetailCharges;
import com.turtledev.pocketcounter.recyclerAdapter.ChargesAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.turtledev.pocketcounter.mFragments.AddChargeFragment.ADD_RESULT_DETAIL_CHARGE;

/**
 * Created by PavloByinyk.
 * Fragment that shows all charges in categories
 */
public class ChargesFragment extends AbstractFragment implements SearchView.OnQueryTextListener  {

    public  static  final  String TAG =ChargesFragment.class.getName();
    public static final int ADD_CHARGE_ITEM = 1002;
    private List<DetailCharges> list;
    private AbstractFragment mFragment;
    private ChargesAdapter chargesAdapter;
    private RecyclerView chargesRecyclerView;
    private View view;

    public ChargesFragment() {
        setHasOptionsMenu(true);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_charges_layout,container,false);
        initRecyclerAdapter();
        setActionBar();
        changeFAB();
        updateUI();
        return  view;
    }
    private void initRecyclerAdapter(){
        chargesRecyclerView = (RecyclerView) view.findViewById(R.id.rv_charges);
        chargesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    private void changeFAB(){
        FloatingActionButton fab=((MainActivity) getActivity()).getFab();
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_plus));
    }
    public void updateUI(){
        list= new ArrayList<>();
        DBAdapterRx.makeObservable(DBAdapterRx.getDBAdapterRx(getActivity())
                .getDBListDetailChargesSumName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<DetailCharges>>() {
                    @Override
                    public void call(List<DetailCharges> charges) {
                        list=charges;
                        if (chargesAdapter == null) {
                            chargesAdapter = new ChargesAdapter(list, (Callbacks) getActivity());
                            chargesRecyclerView.setAdapter(chargesAdapter);
                        } else {
                            chargesAdapter.setListDetailCharges(list);
                            chargesRecyclerView.setAdapter(chargesAdapter);
                        }
                    }
                });
    }
    private void setActionBar() {
        android.support.v7.app.ActionBar ab = ((MainActivity) getActivity()).getSupportActionBar();
        ab.setTitle((getResources().getString(R.string.charges)));
        ((MainActivity)getActivity()).getToggle().setDrawerIndicatorEnabled(true);
        }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_charges_fragment,menu);
        MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(searchItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem searchItem) {
                        // Do something when collapsed
                        chargesAdapter.setFilter(list);
                        return true; // Return true to collapse action view
                    }
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem searchItem) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
        super.onCreateOptionsMenu(menu, inflater);
    }
    private List<DetailCharges> filter(List<DetailCharges> list, String query) {
        query = query.toLowerCase();
        final List<DetailCharges> filterList = new ArrayList<DetailCharges>();
        for (DetailCharges charges : list) {
            final String text = charges.getParentChargeNAme().toLowerCase();
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
        List<DetailCharges> mfilterList = filter(list, newText);
        chargesAdapter.setFilter(mfilterList);
        return true;
    }
    @Override
    public String getTagFragment() {
        return TAG;
    }
    @Override
    public void work() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (mFragment == null) {
            mFragment = new AddChargeFragment();
        }
        mFragment.setTargetFragment(this, ADD_CHARGE_ITEM);
        ft.replace(this.GetContFragment(), mFragment);
        ft.addToBackStack(null);
        ft.commit();
        }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ADD_CHARGE_ITEM:
                    DetailCharges detailCharges=(DetailCharges) data.getParcelableExtra(ADD_RESULT_DETAIL_CHARGE);
                    Toast.makeText(getActivity(),detailCharges.getParentChargeNAme().toString() +"\n"+detailCharges.getSum() + "\n"+" successfull add", Toast.LENGTH_LONG).show();
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
