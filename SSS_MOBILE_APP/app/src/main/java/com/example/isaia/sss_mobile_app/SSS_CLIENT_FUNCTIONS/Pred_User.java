package com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class Pred_User {
    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;

    public Pred_User()
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
    public String Do_The_work(String User_Name,String User_ID)
    {
        String response = "";
        File pictureFile = getOutputMediaFile();
        BufferedInputStream imageByte = null;
        try {
            imageByte = new BufferedInputStream(new FileInputStream(pictureFile));
            int ImageSize = imageByte.available();
            byte[] buffer = new byte[ImageSize];
            imageByte.read(buffer);

            sendCommand("PRED_USER");
            sendCommand(User_ID);
            sendCommand(User_Name);
            sendCommand(String.valueOf(ImageSize));
            sendCommand("Image_Name");
            out.write(buffer);
            out.flush();
            response = readResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
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
    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "22587190 - Copy"+  ".jpg");
        return mediaFile;
    }
}