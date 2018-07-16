package com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS;

import android.os.Environment;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

public class Insert_Image {
    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;

    public Insert_Image()
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
            //txvResult.setText(ex.toString());
        }
    }
    public String Do_The_work(String User_Name)
    {
        File pictureFile = getOutputMediaFile();
        BufferedInputStream imageByte = null;
        try {
            imageByte = new BufferedInputStream(new FileInputStream(pictureFile));
            int ImageSize = imageByte.available();
            byte[] buffer = new byte[ImageSize];
            imageByte.read(buffer);



            sendCommand("INSERT_IMAGE");
            sendCommand("Ississ");
            sendCommand(String.valueOf(ImageSize));
            sendCommand("Image_Name");
            out.write(buffer);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
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
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return response;
    }
    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "0216-bey"+  ".jpg");
        return mediaFile;
    }
}
