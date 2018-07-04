package SSS_SERVER_CLASSES_FOR_MOBILE;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.*;

import javax.imageio.ImageIO;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;



public class Client_Handler implements Runnable{

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
	
	public Client_Handler(Socket socketConnectionToClient,String Location, FileWriter	logFile,File log,MatlabEngine matEng)
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

		try
		{
			while (processing)
			{
				String message = txtin.readLine();
				StringTokenizer messagetokens = new StringTokenizer(message, " ");
				String command = messagetokens.nextToken().toUpperCase();
				System.out.println(message);
				StringWriter output = new StringWriter();
				try 
				{
					matEng.eval("run('C:/Users/isaia/PJE4A_PROJECT/SSS_JAVA_SERVER/src/SSS_SERVER/Matlab_Sripts/Test.m');",output,null);
					String Var = matEng.getVariable("y").toString();
					sendMessage(Var);
					//System.out.println(output);
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
			}
		}
		catch (IOException ex)
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
