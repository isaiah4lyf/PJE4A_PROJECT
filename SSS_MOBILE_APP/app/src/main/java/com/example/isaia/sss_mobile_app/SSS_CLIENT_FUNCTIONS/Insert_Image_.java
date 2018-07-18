package com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class Insert_Image_ {
    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;

    public Insert_Image_()
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
    public String Do_The_work(String User_Name,String User_ID,String Image_Name)
    {
        File pictureFile = getOutputMediaFile(Image_Name);
        BufferedInputStream imageByte = null;
        try {
            imageByte = new BufferedInputStream(new FileInputStream(pictureFile));
            int ImageSize = imageByte.available();
            byte[] buffer = new byte[ImageSize];
            imageByte.read(buffer);
            String name = pictureFile.getName();


            sendCommand("INSERT_IMAGE");
            sendCommand(User_ID);
            sendCommand(User_Name);
            sendCommand(String.valueOf(ImageSize));
            sendCommand(name);
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
    private static File getOutputMediaFile(String Image_Name){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                Image_Name);
        return mediaFile;
    }
}
