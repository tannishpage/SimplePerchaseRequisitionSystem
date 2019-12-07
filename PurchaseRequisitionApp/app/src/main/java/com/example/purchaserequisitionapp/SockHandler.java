package com.example.purchaserequisitionapp;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

public class SockHandler {

    private static SockHandler instance;
    private boolean loggedin = false;
    private Socket mainSocket = null;
    private DataOutputStream out;
    private BufferedReader input;
    private boolean sent;
    public byte EOPR = 4;
    private ArrayList<PerchaseRequisition> history = new ArrayList<PerchaseRequisition>();
    private SockHandler(){}

    public static SockHandler getInstance(){
        if (instance == null){
            instance = new SockHandler();
        }
        return instance;
    }

    public boolean getLoggedin(){
        return loggedin;
    }

    public Socket getMainSocket(){
        return mainSocket;
    }

    public boolean getSent(){
        return sent;
    }

    public ArrayList<PerchaseRequisition> getHistory(){
        return history;
    }

    public void connect(String ipAddress, int port){
        try {
            mainSocket = new Socket(ipAddress, port);
            out = new DataOutputStream(mainSocket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(mainSocket.getInputStream()));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void login(String username, String password){
        if (mainSocket != null){
            try{
                out.write(String.format("LOGIN %s %s", username, password).getBytes("UTF-8"));
                out.flush();
                String b = input.readLine();
                if (b.compareTo("SUCCESS") == 0){
                    loggedin = true;
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void sendPrData(ArrayList<String> pr){
        sent = false;
        if (mainSocket != null){
            System.out.println("here1");
            try{
                out.write(String.format("SENDPRDATA %s", pr.get(0)).getBytes("UTF-8"));
                out.flush();
                System.out.println("here2");
                String b = input.readLine();
                System.out.println(b);
                if (b.compareTo("READY") == 0){
                    for (String data : pr.subList(1, pr.size())){
                        System.out.println("here3");
                        out.write(String.format("%s\n", data).getBytes("UTF-8"));
                        out.flush();
                    }
                    out.write(EOPR);
                    out.flush();
                }
                if (input.readLine().compareTo("RECEIVED") == 0){
                    sent = true;
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void getMyPrHistory(){
        history = new ArrayList<PerchaseRequisition>();
        ArrayList<String> allMyPR;
        if (mainSocket != null){
            try {
                out.write("GETPR *".getBytes("UTF-8"));
                while (true){
                    allMyPR = new ArrayList<String>();
                    for (int i = 0; i <= 12; i++) {
                        String data = input.readLine();
                        if (i == 0){
                            allMyPR.add(data);
                        }

                        if (i == 5){
                            allMyPR.add(data);
                        }

                        if (i == 12){
                            allMyPR.add(data);
                        }
                    }
                    history.add(new PerchaseRequisition(allMyPR.get(0), allMyPR.get(1), allMyPR.get(2)));
                    input.readLine();
                }

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
