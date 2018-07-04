package za.ac.uj.acsse.csc2b.practical05.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.*;

public class Server
{
	private ServerSocket	server;
	private boolean			running;
	private String Location;
	private FileWriter logFile;
	private File log;

	public Server(int port,String Location)
	{
		this.Location = Location;
	
		try
		{
			log = new File("data/server/DataFile.txt");
			logFile = new FileWriter(log);
			System.out.println("Creating server");
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
		System.out.println("Starting server");
		while (running)
		{
			try
			{
				Socket connectionToClient = server.accept();
				System.out.println("New client");
				ClientHandler handler = new ClientHandler(connectionToClient,Location,logFile,log);
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
