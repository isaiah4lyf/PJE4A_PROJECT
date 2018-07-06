package com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS;


import android.widget.TextView;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;

import java.net.Socket;

public class Insert_User {
    private Socket clientSocket;
    private Socket			clientSocket2;

    private DataOutputStream out;
    private DataInputStream in;
    private String Exception_String;

    public Insert_User()
    {
        String host = "192.168.0.137";
        String port = "8080";
        try
        {
            //Text
            clientSocket = new Socket(host, Integer.parseInt(port));
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());
        }
        catch (Exception ex)
        {
           // txvResult.setText(ex.toString());
        }
    }
    public String Do_The_work(String User_Name)
    {
        sendCommand("REG");
        sendCommand(User_Name);
        return readResponse();
    }

    protected void sendCommand(String message)
    {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String readResponse()
    {
        String response  = "";
        try
        {
            response = in.readUTF();
        }
        catch (Exception ex)
        {
            response = ex.toString();
        }
        return response;
    }
}