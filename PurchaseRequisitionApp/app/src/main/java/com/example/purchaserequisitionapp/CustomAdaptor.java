package com.example.purchaserequisitionapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdaptor extends BaseAdapter {

    private ArrayList<PerchaseRequisition> pr;
    private Context context;

    public CustomAdaptor(ArrayList<PerchaseRequisition> perchaseRequisitions, Context context){
        this.pr = perchaseRequisitions;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.pr.size();
    }

    @Override
    public PerchaseRequisition getItem(int position) {
        return pr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customAdaptorXml = convertView;
        if (customAdaptorXml == null){
            customAdaptorXml = LayoutInflater.from(context).inflate(R.layout.row, null, false);
        }

        TextView prID = customAdaptorXml.findViewById(R.id.prID);
        TextView prSubject = customAdaptorXml.findViewById(R.id.prSubject);
        TextView prGPA = customAdaptorXml.findViewById(R.id.prGPA);
        prID.setText(pr.get(position).getId());
        prSubject.setText(pr.get(position).getDate());
        prGPA.setText((pr.get(position).getApproved()));

        return customAdaptorXml;
    }
}
