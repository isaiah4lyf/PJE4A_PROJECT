package SSS_SERVER;


import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import GUI.Jframe;

public class Main {
	private static String URL = "http://smartphonesecuritysystem.dedicated.co.za:8080/SSS_SERVICE.asmx";
	public static void main(String[] args) {		
		Jframe frame = new Jframe(URL);
		frame.pack();
		frame.setTitle("SmartPhone Security System");
		frame.setSize(800,400);
		frame.setResizable(false);
		frame.setLocation(250,200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.pack();
		frame.setVisible(true);
		Image icon = null;
		try 
		{
			icon = ImageIO.read(new File("download.jpg"));}
		catch (IOException e)
		{e.printStackTrace();}
		
		frame.setIconImage(icon);
	}
}
