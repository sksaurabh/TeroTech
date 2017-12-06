package com.telloquent.vms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.telloquent.vms.R;
import com.telloquent.vms.model.settingsmodel.Purpose;
import java.util.List;

/**
 * Created by Telloquent-DM6M on 10/16/2017.
 */

public class PurposeGridAdapter extends BaseAdapter {

    private Context mContext;
    List<Purpose> purposeList;

    public PurposeGridAdapter(Context mContext, List<Purpose> purposeList) {
        this.mContext = mContext;
        this.purposeList = purposeList;
    }

    @Override
    public int getCount() {
        if (purposeList == null || purposeList.size() == 0)
            return 0;
        return purposeList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) mContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.adapter_purposes, parent, false);
        TextView mIdPurpose = (TextView) view.findViewById(R.id.mId_purpose);
        mIdPurpose.setText(purposeList.get(position).getName());

        return view;
    }

}
