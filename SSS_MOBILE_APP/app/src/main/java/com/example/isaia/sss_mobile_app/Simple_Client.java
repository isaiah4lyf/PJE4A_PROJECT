package com.example.isaia.sss_mobile_app;

import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.net.Socket;


public class Simple_Client{

    private Socket			clientSocket;
    private OutputStream	os;
    private InputStream		is;
    private PrintWriter		txtout;
    private BufferedReader	txtin;


    public Simple_Client(TextView txvResult)
    {
        String host = "192.168.0.137";
        String port = "8080";

        try
        {
            clientSocket = new Socket(host, Integer.parseInt(port));
            is = clientSocket.getInputStream();
            os = clientSocket.getOutputStream();
            txvResult.setText("Hello");
            txtout = new PrintWriter(clientSocket.getOutputStream());
            txtin = new BufferedReader(new InputStreamReader(is));
            sendCommand("HELLO USSR");
            String readLine = readResponse();
            txvResult.setText(readLine);

        }
        catch (Exception ex)
        {
            txvResult.setText(ex.toString());
        }

    }
    protected void sendCommand(String message)
    {
        txtout.println(message);
        txtout.flush();
    }

    private String readResponse()
    {
        String response  = "";
        try
        {
            response = txtin.readLine();
            System.out.println(response);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return response;

    }
}
