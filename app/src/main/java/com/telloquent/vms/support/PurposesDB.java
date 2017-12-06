package com.telloquent.vms.support;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.telloquent.vms.model.settingsmodel.Purpose;
import com.telloquent.vms.utils.ApplicationStorage;
import com.telloquent.vms.utils.DBHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Telloquent-DM6M on 9/12/2017.
 */

public class PurposesDB {
    private final static String TAG = ApplicationStorage.class.getCanonicalName();
    //Table Name
    public static final String PURPOSES = "purposes";
    //Table Field Namese
    private static final String ID = "id";
    private static final String NAME = "name";
    private DBHelper dbh;
    private Context ctx;

    public PurposesDB(Context ctx) {
        this.ctx = ctx;
        dbh = new DBHelper(ctx);
    }

    // DB Related Functions
    public static String purposesSQL() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT)", PURPOSES, ID, NAME);
    }


    public long insertPurposes(List<Purpose> mPurposeDetails) {

        SQLiteDatabase db = dbh.getWritableDatabase();
        SQLiteDatabase dbr = dbh.getReadableDatabase();
        long mTotalInsertedValues = 0;
        try {
            dbr.execSQL("DELETE FROM purposes");
            Log.i("Saurabh", "Done");
            Log.e("saurabh", "deleted");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Error");
        }
        try {
            if (mPurposeDetails != null && mPurposeDetails.size() > 0) {
                for (Purpose purpose : mPurposeDetails) {
                    ContentValues initialValues = new ContentValues();
                    initialValues.put(NAME, purpose.getName());

                    long rowId = 0;
                    rowId = db.insert(PURPOSES, null, initialValues);
                    if (rowId != 0) {
                        mTotalInsertedValues++;
                    }
                }
                Log.i(TAG, "Rows " + mTotalInsertedValues);
            } else {
                Log.i(TAG, "List Null or Empty");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           // dbh.exportDB(ctx);
            dbh.closeDB();
        }
        return mTotalInsertedValues;
    }


    public List<Purpose> getPurposeList() {
        List<Purpose> mNameList = null;
        try {
            SQLiteDatabase db = dbh.getReadableDatabase();
            String selectQuery = String.format("SELECT * FROM purposes");
            Cursor c = db.rawQuery(selectQuery, null);
            if (c == null || c.getCount() == 0) {
            } else {
                mNameList = new ArrayList<>();
                c.moveToFirst();
                int len = c.getCount();
                for (int i = 0; i < len; i++) {
                    Purpose purpose = new Purpose();
                    purpose.setName(c.getString(c.getColumnIndex(NAME)));
                    mNameList.add(purpose);
                    c.moveToNext();
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbh.close();
        }
        return mNameList;
    }
}
