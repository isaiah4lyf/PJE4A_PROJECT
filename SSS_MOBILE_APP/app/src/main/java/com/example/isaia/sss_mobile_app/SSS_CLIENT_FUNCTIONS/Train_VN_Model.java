package com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Train_VN_Model  {
    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;

    public Train_VN_Model()
    {
        IP_Address address = new IP_Address();
        String host = address.getIP_Address();
        String port = address.getPort();

        try
        {
            clientSocket = new Socket(host, Integer.parseInt(port));
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());
        }
        catch (Exception ex)
        {
            // txvResult.setText(ex.toString());
        }
    }
    public String Do_The_work(String User_ID)
    {
        sendCommand("TRAIN_VN_MODEL");
        sendCommand(User_ID);
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
