package za.ac.uj.acsse.csc2b.practical05.server;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.*;

import javax.imageio.ImageIO;

public class ClientHandler implements Runnable
{
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
	public ClientHandler(Socket socketConnectionToClient,String Location, FileWriter	logFile,File log)
	{
		connectionToClient = socketConnectionToClient;
		this.Location = Location;
		this.logFile = logFile;
		this.log = log;
		
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
				switch (command)
				{
				case "HELLO":
					sendMessage("HELLO " + connectionToClient.getInetAddress());
					break;
				case "RET":
					String ID = messagetokens.nextToken().toUpperCase();
					System.out.println(ID);
					FromFile = new Scanner(log);
					while(FromFile.hasNextLine()){
						String imageData = FromFile.nextLine();
						StringTokenizer tokens = new StringTokenizer(imageData, " ");
						String Idd = tokens.nextToken().toUpperCase();
						String Namee = tokens.nextToken();
						
						System.out.println(Idd);
						if(Idd.equals(ID)){
							
							System.out.println(Namee);
							BufferedImage image = getImage(Namee,Location);
							ImageIO.write(image, "BMP", os);
							
							
						}
						
						
					}
					
					break;
				case "SEND":
					String Id = messagetokens.nextToken();
					String ImageName = messagetokens.nextToken();
					BufferedImage receiveimage = null;
					try
					{
						receiveimage = ImageIO.read(is);
						File imageInsta = new File(Location, ImageName +".jpg");
						if(imageInsta.exists()){
							String ImageData = Id + " " + ImageName+"(1)";
							ImageIO.write(receiveimage,"jpg", new File("data/server/" + ImageName + "(1).jpg"));
							log(ImageData,logFile);
							sendMessage("Image( Id = " + Id + " ImageName = " + ImageName + "(1)) Successfully uploaded");
						}
						else{
							String ImageData = Id + " " + ImageName;
							ImageIO.write(receiveimage,"jpg", new File("data/server/" + ImageName +".jpg"));
							log(ImageData,logFile);
							sendMessage("Image( Id = " + Id + " ImageName = " + ImageName + ") Successfully uploaded");
						}
						
						
						
						
						
						
						// The code below is to fix problems with ImageIO.read
						// not clearing all bytes of an image.
						int extra = is.available();
						if (extra > 0)
						{
							byte[] buffer = new byte[extra];
							is.read(buffer);
							System.out.println(extra+" "+new String(buffer));
						}
					}
					catch (IOException ex)
					{
						ex.printStackTrace();
						sendMessage("Failure. Please try again later");
					}
					
					break;
				case "LIST":
					 
						if(log.exists()){
							FromFile = new Scanner(log);
							String ListTosend = "";
							int count = 0;
							while(FromFile.hasNextLine()){
								ListTosend += FromFile.nextLine() + "/";
								count += 1;
							}
							sendMessage(ListTosend + " *.*" + count);
						}
					
					
					break;
				case "TERMINATE":
					sendMessage("Bye");
					processing = false;
					break;
				default:
					sendMessage("Unknown command");
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
