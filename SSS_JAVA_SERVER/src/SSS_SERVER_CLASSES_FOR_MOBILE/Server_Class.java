package SSS_SERVER_CLASSES_FOR_MOBILE;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.*;
import com.mathworks.engine.MatlabEngine;



public class Server_Class {
	private ServerSocket	server;
	private boolean			running;
	private MatlabEngine matEng;
	private String URL;  

	public Server_Class(int port,MatlabEngine matEng,String URL)
	{
		this.matEng = matEng;
		this.URL = URL;
		try
		{

			System.out.println("Creating server for mobile");
			server = new ServerSocket(port);
			running = true;
			start();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public void start()
	{
		System.out.println("Starting server for mobile");
		while (running)
		{
			try
			{
				Socket connectionToClient = server.accept();
				System.out.println("New client");
				Client_Handler handler = new Client_Handler(connectionToClient,matEng,URL);
				Thread clientThread = new Thread(handler);
				System.out.println("Starting client thread");
				clientThread.start();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

}
