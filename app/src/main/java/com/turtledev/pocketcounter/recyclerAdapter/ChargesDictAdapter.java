package com.turtledev.pocketcounter.recyclerAdapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.turtledev.pocketcounter.R;
import com.turtledev.pocketcounter.mDataBase.DBAdapterRx;
import com.turtledev.pocketcounter.mInterfaces.Callbacks;
import com.turtledev.pocketcounter.models.Charges;
import com.turtledev.pocketcounter.netWork.NetworkCallback;
import com.turtledev.pocketcounter.netWork.commands.DeleteAllChargesAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PavloByinyk.
 */
public class ChargesDictAdapter extends RecyclerView.Adapter<ChargesDictAdapter.ChargesViewHolder> {

    private Context context;
    private List<Charges> chargesList;
    private Callbacks callbacks;

    public ChargesDictAdapter(List<Charges> chargesList,Context context, Callbacks callbacks) {
        this.context=context;
        this.chargesList=chargesList;
        this.callbacks=callbacks;
    }

    @Override
    public ChargesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_charges_dict, parent, false);
        return new ChargesViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ChargesViewHolder holder, final int position) {
        Charges charge= chargesList.get(position);
        holder.bindCharge(charge);
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showPopupMenu(holder.imageButton, position);
            }
        });
    }
    @Override
    public int getItemCount() {
         return chargesList.size();
    }
    public void setListCharges(List<Charges> list){
        chargesList=list;
    }
    public void setFilter(List<Charges> newChargesList) {
        chargesList = new ArrayList<Charges>();
        chargesList.addAll(newChargesList);
        notifyDataSetChanged();
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
                         deleteAllCharges( chargesList.get(position));
                        return true;
                }
                return false;
            }
        });
        popup.show();
    }
    private void deleteAllCharges(final Charges charges) {
        DeleteAllChargesAction action = new DeleteAllChargesAction(charges,context, new NetworkCallback() {
            @Override
            public void onSuccess(Object response) {
                DBAdapterRx.getDBAdapterRx(context).deleteAllDetailCharges(charges.getName());
                callbacks.updateUiInFragment();
            }
            @Override
            public void onFailure(String error) {
                Toast.makeText(context, error.toString(),Toast.LENGTH_LONG).show();
            }
        }, 2);
        action.run();
    }

    public class ChargesViewHolder extends RecyclerView.ViewHolder {
        private TextView chargesItem;
        private CardView cardView;
        private Charges mCharge;
        private ImageButton imageButton;

        public ChargesViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            chargesItem = (TextView) itemView.findViewById(R.id.fcd_item);
            imageButton=(ImageButton)itemView.findViewById(R.id.imageButton_dictionary);
        }
        public void bindCharge(Charges charge) {
            mCharge = charge;
            chargesItem.setText(mCharge.getName().toString());

        }
    }
}


