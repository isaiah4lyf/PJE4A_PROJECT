package SSS_SERVER_CLASSES_FOR_WEB_SERVICE;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import com.mathworks.engine.MatlabEngine;

public class Client_Handler_Web implements Runnable{

	private Socket			connectionToClient;
	private OutputStream	os;
	private InputStream		is;
	private PrintWriter		txtout;
	private PrintWriter  ToFile;
	private Scanner FromFile;
	private File file;
	private BufferedReader	txtin;
	private String Location;
	private FileWriter	logFile;
	private FileReader readFile;
	private File log;
	private MatlabEngine matEng;
	
	public Client_Handler_Web(Socket socketConnectionToClient,String Location, FileWriter	logFile,File log,MatlabEngine matEng)
	{
		connectionToClient = socketConnectionToClient;
		this.Location = Location;
		this.logFile = logFile;
		this.log = log;
		this.matEng = matEng;
		
		try
		{
			os = connectionToClient.getOutputStream();
			is = connectionToClient.getInputStream();

			
			txtin = new BufferedReader(new InputStreamReader(connectionToClient.getInputStream()));
			txtout = new PrintWriter(os);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	private void close()
	{
		try
		{
			connectionToClient.close();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	private void sendMessage(String message)
	{
		txtout.println(message);
		txtout.flush();
	}

	@Override
	public void run()
	{
		System.out.println("Processing client commands");
		boolean processing = true;
		//StringWriter output = new StringWriter();
		try 
		{
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try
		{
			while (processing)
			{
			
	
				
			}
		}
		catch (Exception ex)
		{
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		finally
		{
			
			close();
			
		}
	}

	/**
	 * @param coords
	 * @return
	 */
	private BufferedImage getImage(String ID,String Location)
	{
		String filename = ID + ".jpg";
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(new File(Location, filename));
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return image;
	}
	
	synchronized void log(String message, FileWriter logFile)
	{
		try
		{
			logFile.write(message);
			logFile.write("\r\n");
			logFile.flush();
		}
		catch (IOException ex)
		{
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}


}
