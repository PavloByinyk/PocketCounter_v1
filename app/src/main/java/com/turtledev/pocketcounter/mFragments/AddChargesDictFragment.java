package com.turtledev.pocketcounter.mFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.turtledev.pocketcounter.MainActivity;
import com.turtledev.pocketcounter.R;
import com.turtledev.pocketcounter.mDataBase.DBAdapterRx;
import com.turtledev.pocketcounter.mHelpers.MyHelper;
import com.turtledev.pocketcounter.models.Charges;
import com.turtledev.pocketcounter.netWork.NetworkCallback;
import com.turtledev.pocketcounter.netWork.commands.InsertChargeAction;

/**
 * Created by me.
 * Fragment where you add new global charges
 */
public class AddChargesDictFragment extends AbstractFragment  {

    public static final String ADD_RESULT = "ADD_RESULT";
    private static final String TAG = AddChargesDictFragment.class.getName();
    private EditText etxCharge;
    private FloatingActionButton fab;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_charges_dict_layout, container, false);
        etxCharge = (EditText) view.findViewById(R.id.et_facd);
        setActionBar();
        changeFAB();
        return view;
    }
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
    @Override
    public void work() {
        String result = etxCharge.getText().toString();
        if(!result.equals("")) {
            putChargesOnServer(new Charges(result, MyHelper.getLoginResponseData(getActivity()).getOwnerId()));
            fab.setClickable(false);
        }else {
            Toast.makeText(getActivity(), R.string.set_all_fields,Toast.LENGTH_SHORT).show();
        }
          }
    private void putChargesOnServer(final Charges newCharges) {
        InsertChargeAction insertChargeAction=new InsertChargeAction(newCharges,
                new NetworkCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Fragment targetFragment = getTargetFragment();
                        if (targetFragment != null) {
                            Charges responseCharges = (Charges) response;
                            DBAdapterRx.getDBAdapterRx(getActivity()).insertChargesToDB(responseCharges);
                            Intent intent = new Intent();
                            intent.putExtra(ADD_RESULT, responseCharges.getName().toString());
                            targetFragment.onActivityResult(ChargesDictFragment.ADD_CHARGE_D_ITEM, Activity.RESULT_OK, intent);
                            etxCharge.setText("");
                            fab.setClickable(true);

                        }
                        getFragmentManager().popBackStack();
                    }
                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(getActivity(), R.string.some_problems_on_server,Toast.LENGTH_LONG).show();
                        etxCharge.setText("");
                        fab.setClickable(true);
                    }
                }, 2);
        insertChargeAction.run();
    }
    @Override
    public String getTagFragment() {
        return this.TAG;
    }

    @Override
    public void onStop() {
        super.onStop();
        MyHelper.closeKeyboard(getActivity());
    }
}
