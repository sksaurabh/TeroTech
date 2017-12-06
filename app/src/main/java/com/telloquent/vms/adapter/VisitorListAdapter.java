package com.telloquent.vms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telloquent.vms.R;
import com.telloquent.vms.model.visitorlist.Visitor;

import java.util.List;


/**
 * Created by Telloquent-DM6M on 10/4/2017.
 */

public class VisitorListAdapter extends RecyclerView.Adapter<VisitorListAdapter.MyViewHolder> {
    private Context mContext;
    private List<Visitor> mVisitors;

    public VisitorListAdapter(Context mContext, List<Visitor> mVisitors) {
        this.mContext = mContext;
        this.mVisitors = mVisitors;
    }


    @Override
    public VisitorListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.visitor_list_details, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VisitorListAdapter.MyViewHolder holder, int position) {
        holder.visitorNameList.setText(mVisitors.get(position).getName());
        holder.visitorCompanyList.setText(mVisitors.get(position).getCompanyName());
        holder.visitorEmailList.setText(mVisitors.get(position).getEmail());
        holder.visitorMobList.setText(mVisitors.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        if (mVisitors == null || mVisitors.size() == 0)
            return 0;
        return mVisitors.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView visitorNameList;
        private TextView visitorCompanyList;
        private TextView visitorEmailList;
        private TextView visitorMobList;

        public MyViewHolder(View itemView) {
            super(itemView);

            visitorNameList = (TextView) itemView.findViewById(R.id.visitor_name_list);
            visitorCompanyList = (TextView) itemView.findViewById(R.id.visitor_company_list);
            visitorEmailList = (TextView) itemView.findViewById(R.id.visitor_email_list);
            visitorMobList = (TextView) itemView.findViewById(R.id.visitor_mobile_list);
        }
    }
}
