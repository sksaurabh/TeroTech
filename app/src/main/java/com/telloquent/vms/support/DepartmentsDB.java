package com.telloquent.vms.support;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.telloquent.vms.model.settingsmodel.Department;
import com.telloquent.vms.utils.ApplicationStorage;
import com.telloquent.vms.utils.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Telloquent-DM6M on 9/12/2017.
 */

public class DepartmentsDB {

    private final static String TAG = ApplicationStorage.class.getCanonicalName();
    //Table Name
    public static final String DEPARTMENTS = "departments";
    //Table Field Namese
    private static final String ID = "id";
    private static final String NAME = "name";
    private DBHelper dbh;
    private Context ctx;

    public DepartmentsDB(Context ctx) {
        this.ctx = ctx;
        dbh = new DBHelper(ctx);
    }

    //DB Related Functions
    public static String createDepartmentSQL() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT)", DEPARTMENTS, ID, NAME);
    }

    public long insertDepartments(List<Department> departmentDetailsList) {
        SQLiteDatabase db = dbh.getWritableDatabase();
        long mTotalInsertedValues = 0;
        try {
            db.execSQL("DELETE FROM departments");
            Log.i(TAG, "Done");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Error");
        }
        try {
            if (departmentDetailsList.size() != 0 && departmentDetailsList != null) {
                ContentValues values = new ContentValues();
                for (int i = 0; i < departmentDetailsList.size(); i++) {
                    values.put(NAME, departmentDetailsList.get(i).getName());
                    long rowId = db.insert(DEPARTMENTS, null, values);
                    if (rowId != 0) {
                        mTotalInsertedValues++;
                    }
                }

            } else {
                Log.i(TAG, "value Null or Empty");
            }
            Log.i(TAG, "Rows " + mTotalInsertedValues);
        } catch (Exception ex) {
        } finally {
           // dbh.exportDB(ctx);
            db.close();
        }
        return mTotalInsertedValues;
    }

    public List<Department> getDepartmentList() {
        List<Department> mDepNameList = null;
        try {
            SQLiteDatabase db = dbh.getReadableDatabase();
            String selectQuery = String.format("SELECT * FROM departments");
            Cursor c = db.rawQuery(selectQuery, null);
            if (c == null || c.getCount() == 0) {
            } else {
                mDepNameList = new ArrayList<>();
                c.moveToFirst();
                int len = c.getCount();
                for (int i = 0; i < len; i++) {
                    Department department = new Department();
                    department.setName(c.getString(c.getColumnIndex(NAME)));
                    mDepNameList.add(department);
                    c.moveToNext();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbh.closeDB();
        }
        return mDepNameList;
    }
}