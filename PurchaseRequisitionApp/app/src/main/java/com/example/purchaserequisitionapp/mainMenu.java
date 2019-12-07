package com.example.purchaserequisitionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().setTitle("Purchase Requisition Menu");
        Button historyButton = (Button) findViewById(R.id.historyBtn);
        Button createNewButton = (Button) findViewById(R.id.createBtn);
        historyButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent page1 = new Intent(mainMenu.this, historyPage.class);
                startActivity(page1);
            }
        });

        createNewButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent page1 = new Intent(mainMenu.this, CreateNewFormPage1.class);
                startActivity(page1);
            }
        });
    }
}