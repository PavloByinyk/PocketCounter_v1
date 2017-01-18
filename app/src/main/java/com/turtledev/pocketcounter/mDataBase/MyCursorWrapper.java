package com.turtledev.pocketcounter.mDataBase;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.turtledev.pocketcounter.models.DetailCharges;

import java.util.Date;

import static com.turtledev.pocketcounter.mDataBase.DBHelper.DETAIL_CHARGES_ID;
import static com.turtledev.pocketcounter.mDataBase.DBHelper.PARENT_CHARGES_NAME;

/**
 * Created by PavloByinyk.
 * Cursors for selection from DB
 */

public class MyCursorWrapper extends CursorWrapper {

    public MyCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public DetailCharges getDetailChargesNameSum() {
        String names = getString(getColumnIndex(PARENT_CHARGES_NAME));
        double sum = getDouble(getColumnIndex(DBHelper.SUM));
        DetailCharges detailCharge = new DetailCharges();
        detailCharge.setParentChargeNAme(names);
        detailCharge.setSum(sum);
        return detailCharge;
    }
    public DetailCharges getDetailCharges() {
        String names = getString(getColumnIndex(PARENT_CHARGES_NAME));
        String myObjectId = getString(getColumnIndex(DETAIL_CHARGES_ID));
        String userId = getString(getColumnIndex(DBHelper.DETAIL_CHARGES_USER_ID));
        String description = getString(getColumnIndex(DBHelper.DESCRIPTION));
        long time = getLong(getColumnIndex(DBHelper.TIME));
        double sum = getDouble(getColumnIndex(DBHelper.SUM));
        DetailCharges detailCharge = new DetailCharges();
        detailCharge.setParentChargeNAme(names);
        detailCharge.setTime(new Date(time));
        detailCharge.setDescription(description);
        detailCharge.setSum(sum);
        detailCharge.setUserId(userId);
        detailCharge.setObjectID(myObjectId);
        return detailCharge;
    }
}
