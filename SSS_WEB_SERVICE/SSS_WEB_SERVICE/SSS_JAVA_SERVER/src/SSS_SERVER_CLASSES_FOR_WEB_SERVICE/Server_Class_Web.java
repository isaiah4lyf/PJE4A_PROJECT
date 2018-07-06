package SSS_SERVER_CLASSES_FOR_WEB_SERVICE;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.mathworks.engine.MatlabEngine;



public class Server_Class_Web {
	private ServerSocket	server;
	private boolean			running;
	private String Location;
	private FileWriter logFile;
	private File log;
	private MatlabEngine matEng;

	public Server_Class_Web(int port,String Location,MatlabEngine matEng)
	{
		this.Location = Location;
		this.matEng = matEng;
	
		try
		{
			log = new File("data/server/DataFile.txt");
			logFile = new FileWriter(log);
			System.out.println("Creating server for web service");
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
		System.out.println("Starting server for web service");
		while (running)
		{
			try
			{
				Socket connectionToClient = server.accept();
				System.out.println("New client");
				Client_Handler_Web handler = new Client_Handler_Web(connectionToClient,Location,logFile,log,matEng);
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
