package SSS_SERVER_CLASSES_FOR_MOBILE;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.*;

import javax.imageio.ImageIO;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

import SSS_SERVER_FUNCTIONS.Insert_Image;
import sun.misc.BASE64Decoder;



public class Client_Handler implements Runnable{

	private Socket			connectionToClient;
	private OutputStream	os;
	private InputStream		is;
	private PrintWriter		txtout;
	private BufferedReader	txtin;
	private DataInputStream in;
	private DataOutputStream out;
	private MatlabEngine matEng;
	private String URL;
	private ServerSocket	server;
	
	
	
	public Client_Handler(Socket socketConnectionToClient,MatlabEngine matEng,String URL,ServerSocket server)
	{
		this.connectionToClient = socketConnectionToClient;
		this.matEng = matEng;
		this.URL = URL;
		this.server = server;
		try
		{
			os = connectionToClient.getOutputStream();
			is = connectionToClient.getInputStream();
			in = new DataInputStream(connectionToClient.getInputStream());
			out = new DataOutputStream(connectionToClient.getOutputStream());
			
			//txtin = new BufferedReader(new InputStreamReader(connectionToClient.getInputStream()));
			//txtout = new PrintWriter(os);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
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
				//byte[] buffer_String = new byte[1024];
				
				String command = in.readUTF();

				System.out.println(command);
				try 
				{
					switch (command)
					{
						case "REG":
							
							
							
	
							/*
							StringWriter output = new StringWriter();
							matEng.eval("run('C:/Users/isaia/PJE4A_PROJECT/SSS_JAVA_SERVER/src/SSS_SERVER/Matlab_Sripts/Test.m');",output,null);
							String Var = matEng.getVariable("y").toString();
							sendMessage(Var);
							//System.out.println(output);
							Insert_Image clas = new Insert_Image();
							
							String response = clas.do_The_Work(URL);
							System.out.println(response);
							processing = false;
							*/
							
							break;
						case "LOGIN":

							break;
						case "INSERT_IMAGE":
							processing = false;
							String size_string = in.readUTF();
							System.out.println(size_string);
							int size = Integer.parseInt(size_string);
							String title = in.readUTF();
							System.out.println(title);
							BufferedOutputStream ByteToFile = null;
							try{
								System.out.println(size);
								byte[] buffer = new byte[size+300];
								readFully(in,buffer,0,size+300);
								int extra = in.available();
								if (extra > 0)
								{
									byte[] buffer2 = new byte[extra];
									in.read(buffer2);
								}
								File imageInsta = new File("57g.jpg");
								if(imageInsta.exists()){
									
									ByteToFile = new BufferedOutputStream(new FileOutputStream(new File("iu(1).jpg")));
									ByteToFile.write(buffer);
									ByteToFile.flush();
									ByteToFile.close();
								}
								else
								{
									ByteToFile = new BufferedOutputStream(new FileOutputStream(new File("57g.jpg")));
									ByteToFile.write(buffer);
									ByteToFile.flush();
									ByteToFile.close();
								}
							}
							catch(IOException ex){
								ex.printStackTrace();

							}
							finally
							{
								if (ByteToFile != null)
									try {
										ByteToFile.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								close();
							}
							
							break;
						case "LOGOFF":

							break;
						
					}
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
			}
		}
		catch (Exception ex)
		{
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		finally
		{
			
			//close();
			
		}
	}

	public final int readFully(DataInputStream in, byte b[], int off, int len) throws IOException {
	    if (len < 0)
	        throw new IndexOutOfBoundsException();
	    int n = 0;
	    while (n < len) {
	        int count = in.read(b, off + n, len - n);
	        if (count < 0)
	            break;
	        n += count;
	    }
	    return n;
	}
	/**
	 * @param coords
	 * @return
	 */
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
	
	private static void clearInput(InputStream is) throws IOException
	{
		int extra = is.available();
		if (extra > 0)
		{
			byte[] buffer = new byte[extra];
			is.read(buffer);
		}
	}
}
