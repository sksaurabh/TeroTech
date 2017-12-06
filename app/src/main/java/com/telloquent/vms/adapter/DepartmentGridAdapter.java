package com.telloquent.vms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.telloquent.vms.R;
import com.telloquent.vms.model.settingsmodel.Department;
import java.util.List;

/**
 * Created by Telloquent-DM6M on 10/16/2017.
 */

public class DepartmentGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<Department> departmentsList;

    public DepartmentGridAdapter(Context mContext, List<Department> departmentsList) {
        this.mContext = mContext;
        this.departmentsList = departmentsList;
    }


    @Override
    public int getCount() {
        if (departmentsList == null || departmentsList.size() == 0)
            return 0;
        return departmentsList.size();
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
        final View view = inflater.inflate(R.layout.adapter_department, parent, false);
        TextView departmentName= (TextView) view.findViewById(R.id.name_department);
        departmentName.setText(departmentsList.get(position).getName());

        return view;
    }
}
