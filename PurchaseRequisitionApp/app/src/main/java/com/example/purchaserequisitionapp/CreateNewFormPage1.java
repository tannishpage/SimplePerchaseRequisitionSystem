package com.example.purchaserequisitionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class CreateNewFormPage1 extends AppCompatActivity {

    EditText username;
    EditText employeeID;
    EditText deparment;
    EditText productName;
    EditText unit;
    EditText quantity;
    EditText ppu;
    EditText totalPrice;
    EditText vendor;
    String ID;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_form_page1);
        getSupportActionBar().setTitle("Purchase Requisition Form");
        username = findViewById(R.id.userNameTxt);
        employeeID = findViewById(R.id.employeeNumberTxt);
        deparment = findViewById(R.id.departmentTxt);
        productName = findViewById(R.id.itemNameTxt);
        unit = findViewById(R.id.unitTxt);
        quantity = findViewById(R.id.quantityTxt);
        ppu = findViewById(R.id.ppuTxt);
        totalPrice = findViewById(R.id.totalPriceTxt);
        vendor = findViewById(R.id.vendorTxt);
        Button createBtn = findViewById(R.id.nextBtn);

        Random rand = new Random();
        this.ID = Integer.toString(rand.nextInt(50000));
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Date date = new Date();
        this.date = dateFormat.format(date);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameS = username.getText().toString();
                String employeeIDS = employeeID.getText().toString();
                String deparmentS = deparment.getText().toString();
                String productNameS = productName.getText().toString();
                String unitS = unit.getText().toString();
                String quantityS = quantity.getText().toString();
                String ppuS = ppu.getText().toString();
                String totalPriceS = totalPrice.getText().toString();
                String vendorS = vendor.getText().toString();

                if (usernameS.isEmpty() || employeeIDS.isEmpty() || deparmentS.isEmpty()
                    || productNameS.isEmpty() || unitS.isEmpty() || ppuS.isEmpty()
                    || totalPriceS.isEmpty() || vendorS.isEmpty()){

                    Toast.makeText(CreateNewFormPage1.this, "Please complete all fields",
                            Toast.LENGTH_SHORT).show();
                } else{
                    ArrayList<String> pr = new ArrayList<String>(Arrays.asList(ID, usernameS, "Something", employeeIDS,
                            deparmentS, CreateNewFormPage1.this.date, productNameS, unitS, quantityS,
                            ppuS, totalPriceS, vendorS, "PENDING"));
                    CreateNewFormPage1.send(pr);
                    finish();
                }
            }
        });
    }

    public static void send(final ArrayList<String> pr){
        SockHandler s = SockHandler.getInstance();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SockHandler s = SockHandler.getInstance();
                s.sendPrData(pr);
            }
        });
        t.start();

        while (true){
            if (s.getSent()){
                t.interrupt();
                break;
            }
        }
    }
}
