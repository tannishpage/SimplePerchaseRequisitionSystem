package com.example.purchaserequisitionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    final public String ipAddress = "192.168.0.37";
    final public int port = 5000;
    public Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (Button) findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view){
                EditText uname = findViewById(R.id.uname);
                EditText pwd = findViewById(R.id.pwdBox);
                String username = uname.getText().toString();
                String password = pwd.getText().toString();
                if (username.isEmpty() || password.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter login info",
                            Toast.LENGTH_SHORT).show();
                }else {
                    MainActivity.this.login(username, password);
                    SockHandler s = SockHandler.getInstance();
                    System.out.println(s.getLoggedin());
                    if (s.getLoggedin()){
                        Intent page1 = new Intent(MainActivity.this, mainMenu.class);
                        startActivity(page1);
                    }else{
                        Toast.makeText(MainActivity.this,
                                "Incorrect username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        getSupportActionBar().setTitle("Login");
    }

    private void login(final String username, final String password){
        SockHandler s = SockHandler.getInstance();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SockHandler s = SockHandler.getInstance();
                if (s.getMainSocket() == null) {
                    s.connect(MainActivity.this.ipAddress, MainActivity.this.port);
                }
                s.login(username, password);
            }
        });
        t.start();

        while (true){
            if (s.getLoggedin()){
                t.interrupt();
                break;
            }
        }
    }

}
