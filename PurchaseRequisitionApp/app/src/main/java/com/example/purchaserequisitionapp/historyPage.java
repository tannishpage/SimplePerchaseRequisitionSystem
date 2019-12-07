package com.example.purchaserequisitionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class historyPage extends AppCompatActivity {

    private ArrayList<PerchaseRequisition> myItems;
    private ListView listView;
    private CustomAdaptor arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);
        getSupportActionBar().setTitle("Purchase Requisition History");
        listView = findViewById(R.id.lvItems);
        historyPage.getPerchaseRequisitions();
        myItems = SockHandler.getInstance().getHistory();
        arrayAdapter = new CustomAdaptor(myItems, this);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               displayMessage(arrayAdapter.getItem(position).toString());
            }
        });
    }

    private void displayMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public static void getPerchaseRequisitions(){
        SockHandler s = SockHandler.getInstance();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SockHandler s = SockHandler.getInstance();
                s.getMyPrHistory();
            }
        });
        t.start();

        while (true){
            if (!(s.getHistory().isEmpty())){
                t.interrupt();
                break;
            }
        }
    }
}
