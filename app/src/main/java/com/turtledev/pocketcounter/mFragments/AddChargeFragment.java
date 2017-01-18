package com.turtledev.pocketcounter.mFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.turtledev.pocketcounter.MainActivity;
import com.turtledev.pocketcounter.R;
import com.turtledev.pocketcounter.mDataBase.DBAdapterRx;
import com.turtledev.pocketcounter.mHelpers.MyHelper;
import com.turtledev.pocketcounter.models.DetailCharges;
import com.turtledev.pocketcounter.netWork.NetworkCallback;
import com.turtledev.pocketcounter.netWork.commands.InsertDetailChargeAction;

/**
 * Created by me.
 *  Fragment where you add new detail charge
 */
public class AddChargeFragment extends AbstractFragment {

    public static final String ADD_RESULT_DETAIL_CHARGE = "ADD_RESULT_DETAIL_CHARGE";
    private static final String TAG = AddChargeFragment.class.getName();
    private View view;
    private Spinner spinner;
    private ArrayAdapter<String> arrayAdapter;
    private EditText  etxSum,etDescription;
    private FloatingActionButton fab;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_cgarges_layout, container, false);
        initViews();
        setActionBar();
        initSpinner();
        changeFAB();
        return view;
    }
    private void initViews(){
        etxSum = (EditText) view.findViewById(R.id.et_fac_sum);
        etDescription = (EditText) view.findViewById(R.id.et_description);
    }
    // set current icon on FAB
    private void changeFAB(){
        fab=((MainActivity) getActivity()).getFab();
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_ok));

    }
    private void setActionBar() {
        ActionBar ab = ((MainActivity) getActivity()).getSupportActionBar();
        ab.setTitle((getResources().getString(R.string.add_charges)));
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
    private void initSpinner(){
        spinner = (Spinner) view.findViewById(R.id.et_fac_name);
        arrayAdapter=new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item, DBAdapterRx.getDBAdapterRx(getActivity()).dBListNames());
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }
    @Override
    public void work() {
        Fragment targetFragment = getTargetFragment();
        String check = etxSum.getText().toString();
            if (check == null || check.isEmpty()) {
                Toast.makeText(getActivity(), R.string.set_all_fields, Toast.LENGTH_SHORT).show();
            } else {
                if (targetFragment != null) {
                    double sum = Double.parseDouble(check);
                    String spinnerSelect = spinner.getSelectedItem().toString();
                    String description=etDescription.getText().toString();
                    DetailCharges detailCharge=new DetailCharges(spinnerSelect,description,sum, MyHelper.getLoginResponseData(getActivity()).getOwnerId());
                    putDetailChargesOnServer(detailCharge);
                    fab.setClickable(false);



                }
            }
    }
    // send Detail charges on server and insert it to local db
    private void putDetailChargesOnServer( final DetailCharges detailCharge) {
        InsertDetailChargeAction insertDetailChargeAction=new InsertDetailChargeAction(detailCharge,
                new NetworkCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Fragment targetFragment = getTargetFragment();
                        if (targetFragment != null) {
                            DetailCharges responseDetailCharges = (DetailCharges) response;
                            DBAdapterRx.getDBAdapterRx(getActivity()).insertDetailChargeToDB(responseDetailCharges);
                            Intent intent = new Intent();
                            intent.putExtra(ADD_RESULT_DETAIL_CHARGE, responseDetailCharges);
                            targetFragment.onActivityResult(ChargesFragment.ADD_CHARGE_ITEM, Activity.RESULT_OK, intent);
                            etxSum.setText("");
                            etDescription.setText("");
                            fab.setClickable(true);
                        }
                        getFragmentManager().popBackStack();
                    }
                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(getActivity(),"Some problems with sending data to server",Toast.LENGTH_LONG).show();
                        fab.setClickable(true);
                    }
                }, 2);
        insertDetailChargeAction.run();
    }
    @Override
    public String getTagFragment() {
        return this.TAG;
    }

//    private void hideThisKeyboard(){
////        MyHelper.closeKeyboard(getActivity(), etxSum);
////        MyHelper.closeKeyboard(getActivity(), etDescription);
//        View view = getActivity().getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
 //   }

    @Override
    public void onStop() {
        super.onStop();
        MyHelper.closeKeyboard(getActivity());
    }
}
