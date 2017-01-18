package com.turtledev.pocketcounter.mDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.turtledev.pocketcounter.models.Charges;
import com.turtledev.pocketcounter.models.DetailCharges;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.turtledev.pocketcounter.mDataBase.DBHelper.CHARGES_TABLE_NAME;
import static com.turtledev.pocketcounter.mDataBase.DBHelper.DATABASE_NAME;
import static com.turtledev.pocketcounter.mDataBase.DBHelper.DETAIL_TABLE_NAME;

/**
 * Created by PabloByinyk.
 * My class for work with DBHelper using RX Android
 */

public class DBAdapterRx {

    private static final String TAG = DBAdapterRx.class.getName();
    private Context mContext;
    private SQLiteDatabase db;
    private static DBAdapterRx dbAdapterRx;

    public DBAdapterRx(Context AppContext) {
        this.mContext = AppContext.getApplicationContext();
        db = new DBHelper(mContext).getWritableDatabase();
    }
    //Singleton
    public static DBAdapterRx getDBAdapterRx(Context context) {
        if (dbAdapterRx == null) {
            dbAdapterRx = new DBAdapterRx(context.getApplicationContext());
        }
        return dbAdapterRx;
    }
    // Delete DB,clear all data
    public void deleteDB(){
        mContext.deleteDatabase(DATABASE_NAME);
        db.close();
        dbAdapterRx=null;
        }
    public static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(func.call());
                        } catch (Exception ex) {
                            Log.e(TAG, "Error reading from the database", ex);
                        }
                    }
                });
    }
    // delete detail charges using rx in new thread
    public void deleteDetailCharges(String data, final String rowName){
        Observable.just(data)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<String>() {
                    String whereClause = rowName;
                    @Override
                    public void call(String data) {
                        db.delete(DBHelper.DETAIL_TABLE_NAME, whereClause + " = ?", new String[] {data});
                    }
                });
    }
    public void deleteAllDetailCharges(String objectName){
        Observable.just(objectName)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<String>() {

                    @Override
                    public void call(String objectName) {
                        db.delete(DBHelper.CHARGES_TABLE_NAME, DBHelper.CHARGES_NAME + " = ?", new String[] {objectName});
                        deleteDetailCharges(objectName ,DBHelper.PARENT_CHARGES_NAME);
                    }
                });
    }
    public void insertServerDataChargesToDB(final List<Charges> list) {
        Observable.just(list)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<List<Charges>>() {

                    @Override
                    public void call(List<Charges> charges) {
                        for (int i = 0; i < charges.size(); i++) {
                            if (charges.get(i).getName() != null) {
                                db.insertWithOnConflict(CHARGES_TABLE_NAME, null, getChargesContentValues(list.get(i)), SQLiteDatabase.CONFLICT_IGNORE);
                            }
                        }
                        Log.e(TAG, "Дані загружені в бд" + Thread.currentThread().getName().toString());
                    }
                });
    }
    public ContentValues getChargesContentValues(Charges charges){
        ContentValues contenValues = new ContentValues();
        contenValues.put(DBHelper.CHARGES_NAME, charges.getName());
        contenValues.put(DBHelper.CHARGES_ID, charges.getObjectId());
        contenValues.put(DBHelper.CHARGES_USER_ID, charges.getUserId());
        return contenValues;
    }
    public void insertChargesToDB(final Charges newCharges) {
        Observable.just(newCharges)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<Charges>() {

                    @Override
                    public void call(Charges newCharges) {
                        db.insert(CHARGES_TABLE_NAME, null, getChargesContentValues(newCharges));
                    }
            });
    }
    public void insertServerDataDetailsToDB(List<DetailCharges> list) {

        Observable.just(list)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<List<DetailCharges>>() {

                    @Override
                    public void call(List<DetailCharges> detailCharges) {

                      for (int i = 0; i < detailCharges.size(); i++) {
                            db.insertWithOnConflict(DBHelper.DETAIL_TABLE_NAME, null, getDetailChargesContentValues(detailCharges.get(i)), SQLiteDatabase.CONFLICT_IGNORE);
                        }

                    }

                });
    }
    public ContentValues getDetailChargesContentValues(DetailCharges detailCharge){
        ContentValues contenValues = new ContentValues();
        contenValues.put(DBHelper.DETAIL_CHARGES_ID, String.valueOf(detailCharge.getObjectID()));
        contenValues.put(DBHelper.DETAIL_CHARGES_USER_ID, detailCharge.getUserId());
        contenValues.put(DBHelper.PARENT_CHARGES_NAME, detailCharge.getParentChargeNAme());
        contenValues.put(DBHelper.DESCRIPTION, detailCharge.getDescription());
        contenValues.put(DBHelper.TIME, detailCharge.getTime().getTime());
        contenValues.put(String.valueOf(DBHelper.SUM), detailCharge.getSum());
        return contenValues;
    }
    public void insertDetailChargeToDB(final DetailCharges detailCharges) {

        Observable.just(detailCharges)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<DetailCharges>() {

                    @Override
                    public void call(DetailCharges detailCharges) {

                        getDetailChargesContentValues(detailCharges);
                        db.insert(DBHelper.DETAIL_TABLE_NAME, null, getDetailChargesContentValues(detailCharges));

                    }


                });

    }
    public Callable<List<Charges>> getDBListCharges() {
        return new Callable<List<Charges>>() {
            @Override
            public List<Charges> call() {
                // select * from users where _id is userId
                String[] columns = {DBHelper.CHARGES_NAME, DBHelper.CHARGES_ID};
                Cursor cursor = db.query(CHARGES_TABLE_NAME, columns, null, null, null, null, null);
                ArrayList<Charges> listCharges = new ArrayList<Charges>();
                while (cursor.moveToNext()) {
                    int index1 = cursor.getColumnIndex(DBHelper.CHARGES_NAME);
                    int index2 = cursor.getColumnIndex(DBHelper.CHARGES_ID);
                    String id = cursor.getString(index2);
                    String name = cursor.getString(index1);
                    Charges charges = new Charges();
                    charges.setName(name);
                    charges.setObjectId(id);
                    listCharges.add(charges);
                }
                cursor.close();

                return listCharges;
            }
        };
    }
    private MyCursorWrapper queryDetailsChargesNameSum() {
        String[] columns = new String[] {
                DBHelper.PARENT_CHARGES_NAME,
                "sum(" + DBHelper.SUM + ") as " +DBHelper.SUM
        };
        String where = null;
        String whereArgs[] = null;
        String groupBy = DBHelper.PARENT_CHARGES_NAME;
        String having = null;
        String order = null;
        String limit = null;
        Cursor cursor = db.query(
                DETAIL_TABLE_NAME,
                columns, // Columns - null выбирает все столбцы
                null, //where
                null, //whereArgs[]
                groupBy, // groupBy
                null, // having
                null // orderBy
        );
        return new MyCursorWrapper(cursor);
    }
    public Callable<List<DetailCharges>> getDBListDetailChargesSumName() {
        return new Callable<List<DetailCharges>>() {
            @Override
            public List<DetailCharges> call() {
                // select * from users where _id is userId
                List<DetailCharges> detailList = new ArrayList<>();
                MyCursorWrapper cursor = queryDetailsChargesNameSum();
                try {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        detailList.add(cursor.getDetailChargesNameSum());
                        cursor.moveToNext();
                    }
                } finally {
                    cursor.close();
                }
                return detailList;
            }
        };
    }
    private MyCursorWrapper queryDetailsCharges(String whereClause, String[] whereArgs) {
        Cursor cursor = db.query(
                DETAIL_TABLE_NAME,
                null, // Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new MyCursorWrapper(cursor);
    }
    public Callable<List<DetailCharges>> getDBListDetailCharges(final String parentChargeName) {
        return new Callable<List<DetailCharges>>() {
            @Override
            public List<DetailCharges> call() {
                // select * from users where _id is userId
                List<DetailCharges> detailList = new ArrayList<>();
                MyCursorWrapper cursor = queryDetailsCharges(DBHelper.PARENT_CHARGES_NAME + " = ?",
                        new String[] { parentChargeName });
                try {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        detailList.add(cursor.getDetailCharges());
                        cursor.moveToNext();
                    }
                } finally {
                    cursor.close();
                }
                return detailList;
            }
        };
    }
    public List<String> dBListNames() {
        String[] columns = {DBHelper.CHARGES_NAME};
        Cursor cursor = db.query(CHARGES_TABLE_NAME, columns, null, null, null, null, null);
        ArrayList<String> listNames = new ArrayList<String>();
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(DBHelper.CHARGES_NAME);
            String name = cursor.getString(index1);
            listNames.add(name);
        }
        cursor.close();
        return listNames;
    }
}
