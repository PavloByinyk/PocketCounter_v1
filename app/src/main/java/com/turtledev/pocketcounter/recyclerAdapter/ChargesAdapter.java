package com.turtledev.pocketcounter.recyclerAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.turtledev.pocketcounter.R;
import com.turtledev.pocketcounter.mInterfaces.Callbacks;
import com.turtledev.pocketcounter.models.DetailCharges;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by PavloByinyk.
 */
public class ChargesAdapter extends RecyclerView.Adapter<ChargesAdapter.ChargesViewHolder> {

    private List<DetailCharges> chargesList;
    private Callbacks callbacks;

    public ChargesAdapter(List<DetailCharges> mChargesList, Callbacks callbacks) {
        this.chargesList=mChargesList;
        this.callbacks=callbacks;
    }
    public void setListDetailCharges(List<DetailCharges> list){
        chargesList=list;
    }
    @Override
    public ChargesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_fragment_charges, parent, false);
        return new ChargesViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ChargesViewHolder holder, int position) {
        DetailCharges charge= chargesList.get(position);
        holder.bindCharge(charge);
    }
    @Override
    public int getItemCount() {
        return chargesList.size();
    }
    public void setFilter(List<DetailCharges> newChargesList) {
        chargesList = new ArrayList<DetailCharges>();
        chargesList.addAll(newChargesList);
        notifyDataSetChanged();
    }

    public  class ChargesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RelativeLayout rl;
        private TextView chargesItem;
        private TextView chargesItemTotal;
        private DetailCharges mCharge;

        ChargesViewHolder(View itemView) {
            super(itemView);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            chargesItemTotal = (TextView) itemView.findViewById(R.id.tv_fc_total);
            chargesItem = (TextView) itemView.findViewById(R.id.tv_fc_name);
            itemView.setOnClickListener(this);
        }
        public void bindCharge(DetailCharges charge) {
            mCharge = charge;
            chargesItem.setText(charge.getParentChargeNAme().toString());
            chargesItemTotal.setText(String.valueOf(charge.getSum()) + " $");
        }
        @Override
        public void onClick(View v) {
            callbacks.onChargesSelected(mCharge.getParentChargeNAme().toString());
        }
    }
}
