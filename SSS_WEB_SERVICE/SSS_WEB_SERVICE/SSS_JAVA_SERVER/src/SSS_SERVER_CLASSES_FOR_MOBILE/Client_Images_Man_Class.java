package SSS_SERVER_CLASSES_FOR_MOBILE;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Client_Images_Man_Class implements Runnable{

	private Socket			connectionToClient;
	private ServerSocket	server;
	private OutputStream	os;
	private InputStream		is;
	private BufferedInputStream img;

	
	
	public Client_Images_Man_Class(ServerSocket	server)
	{
		try
		{
		
			connectionToClient = server.accept();
			img = new BufferedInputStream(connectionToClient.getInputStream());

		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
	@Override
	public void run()
	{
		BufferedOutputStream ByteToFile = null;
		try{

			byte[] buffer = new byte[75808];
			img.read(buffer);
			int extra = img.available();
			if (extra > 0)
			{
				byte[] buffer2 = new byte[extra];
				img.read(buffer);
				System.out.println(extra+" "+new String(buffer2));
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
			//close();
		}
	}
	/**
	 * @param coords
	 * @return
	 */
	private void close()
	{
		try
		{
			//connectionToClient.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}