package com.telloquent.vms.support;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.google.gson.Gson;
import com.telloquent.vms.model.settingsmodel.Theme;
import com.telloquent.vms.model.settingsmodel.Value;
import com.telloquent.vms.utils.ApplicationStorage;
import com.telloquent.vms.utils.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Telloquent-DM6M on 9/12/2017.
 */

public class ThemeValuesDB {
    private final static String TAG = ApplicationStorage.class.getCanonicalName();
    //Table Name
    public static final String THEME_VALUES = "theme_values";

    //Table Field Namese
    private static final String ID = "id";
    private static final String IDENTIFIER = "identifier";
    private static final String VALUES = "VALUE";
    private DBHelper dbh;
    private Context ctx;

    public ThemeValuesDB(Context ctx) {
        this.ctx = ctx;
        dbh = new DBHelper(ctx);
    }


    // DB Related Functions
    public static String createThemeValuesSQL() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s VARCHAR,%S VARCHAR)", THEME_VALUES, ID, IDENTIFIER, VALUES);
    }


    public long insertTheme(List<Theme> themeList) {

        SQLiteDatabase db = dbh.getWritableDatabase();
        long mTotalInsertedValues = 0;
        try {
            db.execSQL("DELETE FROM theme_values");
            Log.i(TAG, "Done");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Error");
        }
        try {
            if (themeList.size() != 0 && themeList != null) {
                ContentValues values = new ContentValues();
                for (int i = 0; i < themeList.size(); i++) {
                    values.put(IDENTIFIER, themeList.get(i).getIdentifier());
                    Gson gson = new Gson();
                    String inputString = gson.toJson(themeList.get(i).getValue());
                    values.put(VALUES, inputString);

                    long rowId = db.insert(THEME_VALUES, null, values);
                    if (rowId != 0) {
                        mTotalInsertedValues++;
                    }
                }
            }
            Log.i(TAG, "Rows " + mTotalInsertedValues);
        } catch (Exception ex) {
        } finally {
            dbh.exportDB(ctx);
            db.close();
        }
        return mTotalInsertedValues;
    }

    public List<Theme> getThemeList() {
        List<Theme> mThemeList = null;
        try {
            SQLiteDatabase db = dbh.getReadableDatabase();
            String selectQuery = String.format("SELECT * FROM theme_values");
            Cursor c = db.rawQuery(selectQuery, null);
            Log.e("saurabh", "" + c.getCount());
            if (c == null || c.getCount() == 0) {
            } else {
                mThemeList = new ArrayList<>();

                c.moveToFirst();
                int len = c.getCount();
                for (int i = 0; i < len; i++) {
                    Theme theme = new Theme();

                    theme.setIdentifier(c.getString(c.getColumnIndex(IDENTIFIER)));
                    theme.setValue(new Gson().fromJson(c.getString(c.getColumnIndex(VALUES)),Value.class));
                    Log.e(TAG, "getThemeList: "+ c.getString(c.getColumnIndex(VALUES)));
                    mThemeList.add(theme);

                    //str.add(c.getString(c.getColumnIndex(VALUES)));
                    c.moveToNext();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mThemeList;
    }




}
