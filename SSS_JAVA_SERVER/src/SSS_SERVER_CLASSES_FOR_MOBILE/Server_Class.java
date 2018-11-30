package SSS_SERVER_CLASSES_FOR_MOBILE;
import java.awt.TextArea;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.mathworks.engine.MatlabEngine;



public class Server_Class {
	private ServerSocket	server;
	private boolean			running;
	private String URL;  
	private TextArea console_Like;
	private ArrayList<MATLAB_Instances> matlab_Instances;
	public Server_Class(int port,ArrayList<MATLAB_Instances> matlab_Instances,String URL,TextArea console_Like)
	{
		this.URL = URL;
		this.console_Like = console_Like;
		this.matlab_Instances = matlab_Instances;
		try
		{
			console_Like.append("Creating server for mobile \n");
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

		console_Like.append("Starting server for mobile \n");
		while (running)
		{
			try
			{
				Socket connectionToClient = server.accept();
				console_Like.append("New client \n");
				Client_Handler handler = new Client_Handler(connectionToClient,matlab_Instances,URL,console_Like);
				Thread clientThread = new Thread(handler);
				console_Like.append("Starting client thread \n");
				clientThread.start();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

}
