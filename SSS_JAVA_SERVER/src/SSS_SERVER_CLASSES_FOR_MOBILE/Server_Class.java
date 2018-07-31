package SSS_SERVER_CLASSES_FOR_MOBILE;
import java.awt.TextArea;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import com.mathworks.engine.MatlabEngine;



public class Server_Class {
	private ServerSocket	server;
	private boolean			running;
	private MatlabEngine matEng;
	private String URL;  
	private TextArea console_Like;
	public Server_Class(int port,MatlabEngine matEng,String URL,TextArea console_Like)
	{
		this.matEng = matEng;
		this.URL = URL;
		this.console_Like = console_Like;
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
				Client_Handler handler = new Client_Handler(connectionToClient,matEng,URL,console_Like);
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
