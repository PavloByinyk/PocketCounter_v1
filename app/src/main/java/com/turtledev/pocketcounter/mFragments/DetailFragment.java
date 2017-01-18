package com.turtledev.pocketcounter.mFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.turtledev.pocketcounter.MainActivity;
import com.turtledev.pocketcounter.R;
import com.turtledev.pocketcounter.mDataBase.DBAdapterRx;
import com.turtledev.pocketcounter.mDataBase.DBHelper;
import com.turtledev.pocketcounter.mInterfaces.Callbacks;
import com.turtledev.pocketcounter.models.DetailCharges;
import com.turtledev.pocketcounter.netWork.NetworkCallback;
import com.turtledev.pocketcounter.netWork.commands.DeleteDetailChargesAction;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by PavloByinyk.
 * Fragment thar show all selected charges ( Adapter and Holder here as inner classes )
 */
public class DetailFragment extends AbstractFragment  {

    public  static  final  String TAG =DetailFragment.class.getName();
    private static final String DETAIL_NAME = "charge_name";
    private List<DetailCharges> list;
    private RecyclerView recyclerView;
    private DetailAdapter detaileAdapter;
    private String name;
    private View view;

    public static DetailFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putSerializable(DETAIL_NAME, name);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = (String) getArguments().getSerializable(DETAIL_NAME);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_detail_rv,container,false);
        initRecyclerView();
        setActionBar();
        updateUI();
        hideFab();
        return view;
    }
    //show-hide FAB
    private void hideFab() {
        FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFab();
        if (floatingActionButton.getVisibility() == View.VISIBLE) {
            floatingActionButton.hide();
        }else {
            floatingActionButton.show();
        }
    }
    private void initRecyclerView(){
        recyclerView= (RecyclerView) view.findViewById(R.id.rv_detail);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    @Override
    public void onPause() {
        super.onPause();
        hideFab();
    }
    private void setActionBar() {
        android.support.v7.app.ActionBar ab = ((MainActivity) getActivity()).getSupportActionBar();
        ab.setTitle((getResources().getString(R.string.charges)) + ": " +name );
        ActionBarDrawerToggle toggle=((MainActivity)getActivity()).getToggle();
        toggle.setHomeAsUpIndicator(R.mipmap.ic_keyboard_backspace);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }
    public void updateUI(){
        list= new ArrayList<>();
        DBAdapterRx.makeObservable(DBAdapterRx.getDBAdapterRx(getActivity())
                .getDBListDetailCharges(name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<DetailCharges>>() {
                    @Override
                    public void call(List<DetailCharges> charges) {
                        if(!charges.isEmpty()) {
                            list = charges;
                            if (detaileAdapter == null) {
                                detaileAdapter = new DetailAdapter(list, (Callbacks) getActivity());
                                recyclerView.setAdapter(detaileAdapter);
                            } else {
                                detaileAdapter.setDetailListCharges(list);
                                recyclerView.setAdapter(detaileAdapter);
                            }
                        }else{
                            getFragmentManager().popBackStack();
                        }
                    }
                });
    }

    private class DetailHolder extends RecyclerView.ViewHolder{
        private TextView tvName,tvSum,tvDescription;
        private TextView btnTime;
        private ImageButton imageButton;

        public DetailHolder(View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.tvD_name);
            tvSum=(TextView)itemView.findViewById(R.id.tvD_sum);
            tvDescription=(TextView)itemView.findViewById(R.id.tvD_description);
            tvDescription.setMovementMethod(new ScrollingMovementMethod());
            btnTime=(TextView)itemView.findViewById(R.id.tv_Time);
            imageButton=(ImageButton)itemView.findViewById(R.id.imageButton_detail);
        }
        public void bindDetailCharge(DetailCharges detailCharge) {
            tvName.setText(detailCharge.getParentChargeNAme().toString());
            tvSum.setText(String.valueOf(detailCharge.getSum())+" $");
            tvDescription.setText(detailCharge.getDescription().toString());
            btnTime.setText(detailCharge.getTime().toString());
        }
    }

    private class DetailAdapter extends RecyclerView.Adapter<DetailHolder> {
        private List<DetailCharges> list;
        Callbacks callbacks;

        public DetailAdapter(List<DetailCharges> list, Callbacks callbacks) {
            this.list = list;
            this.callbacks = callbacks;
        }
        @Override
        public DetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            View v=inflater.inflate(R.layout.item_fragment_charges_detail,parent,false);
            return new DetailHolder(v);
        }
        @Override
        public void onBindViewHolder(final DetailHolder holder, final int position) {
            DetailCharges detailCharge=list.get(position);
            holder.bindDetailCharge(detailCharge);
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(holder.imageButton, position);
                }
            });
        }
        private void showPopupMenu(View view,final int position){
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.popup_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_item_delete:
                            deleteCharges( list.get(position));
                            return true;
                    }
                    return false;
                }
            });
            popup.show();
        }
        private void deleteCharges(final DetailCharges detailCharges) {
            DeleteDetailChargesAction action = new DeleteDetailChargesAction(detailCharges,getActivity(), new NetworkCallback() {
                @Override
                public void onSuccess(Object response) {
                    DBAdapterRx.getDBAdapterRx(getActivity()).deleteDetailCharges(detailCharges.getObjectID(), DBHelper.DETAIL_CHARGES_ID);
                    callbacks.updateUiInFragment();
             }
                @Override
                public void onFailure(String error) {
                    Toast.makeText(getActivity(), error.toString(),Toast.LENGTH_LONG).show();
                }
            }, 2);
            action.run();
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
        public void setDetailListCharges(List<DetailCharges> list){
            this.list=list;
        }
    }
}
