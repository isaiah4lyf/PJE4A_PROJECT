package com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS;

import com.example.isaia.sss_mobile_app.Lists_Adapters.Advert_Data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Load_More_News_Feed{
    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;

    public Load_More_News_Feed()
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
    public Advert_Data Do_The_work(String Id)
    {
        sendCommand("GET_NEWS_FEED_ABOVE_ID");
        sendCommand(Id);
        Advert_Data data = new Advert_Data();
        String First_Atri = readResponse();
        if(!First_Atri.equals("false"))
        {
            data.setId(First_Atri);
            data.setTitle(readResponse());
            data.setDescription(readResponse());
            data.setReadMoreLink(readResponse());
            data.setTitleImage(readResponse());
            data.setVideoUrl(readResponse());
            data.setImageUrl(readResponse());
            data.setUploadTime(readResponse());
        }
        else
        {
            data.setId(First_Atri);
        }

        return data;
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
