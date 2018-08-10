package com.example.isaia.sss_mobile_app.Other;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.IP_Address;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Simple_Client{

    private Socket			clientSocket;
    private Socket			clientSocket2;
    private OutputStream	os;
    private InputStream		is;
    private PrintWriter		txtout;
    private BufferedReader	txtin;
    private DataOutputStream img;

    public Simple_Client(TextView txvResult)
    {
        IP_Address address = new IP_Address();
        String host = address.getIP_Address();
        String port = address.getPort();

        try
        {
            //Text
            clientSocket = new Socket(host, Integer.parseInt(port));
            is = clientSocket.getInputStream();
            os = clientSocket.getOutputStream();
            txtout = new PrintWriter(clientSocket.getOutputStream());
            txtin = new BufferedReader(new InputStreamReader(is));
            img = new DataOutputStream(clientSocket.getOutputStream());





            File pictureFile = getOutputMediaFile();
            BufferedInputStream imageByte = new BufferedInputStream(new FileInputStream(pictureFile));
            int ImageSize = imageByte.available();
            byte[] buffer = new byte[ImageSize];
            imageByte.read(buffer);



            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "MyCameraApp");

            sendCommand("INSERT_IMAGE");
            sendCommand(String.valueOf(ImageSize));
            sendCommand("Image_Name");

            //String readLine = readResponse();
            //txvResult.setText(readLine);


            //Binary/Images
           // clientSocket2 = new Socket(host, Integer.parseInt(port2));

            img.write(buffer);
            img.flush();


        }
        catch (Exception ex)
        {
           txvResult.setText(ex.toString());
        }

    }
    protected void sendCommand(String message)
    {

        try {
            img.writeUTF(message);
            img.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private static File getOutputMediaFile(){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "Tt"+  ".jpg");


        return mediaFile;
    }

}
