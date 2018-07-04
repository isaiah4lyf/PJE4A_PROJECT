package za.ac.uj.acsse.csc2b.practical05.client;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.*;
import java.awt.*;
import java.util.*;


public class Simple_Client{

	private Socket			clientSocket;
	private OutputStream	os;
	private InputStream		is;
	private PrintWriter		txtout;
	private BufferedReader	txtin;


	public Simple_Client()
	{
		String host = "192.168.0.137";
		String port = "80";
		try
		{
			clientSocket = new Socket(host, Integer.parseInt(port));
			is = clientSocket.getInputStream();
			os = clientSocket.getOutputStream();
			txtout = new PrintWriter(clientSocket.getOutputStream());
			txtin = new BufferedReader(new InputStreamReader(is));
			sendCommand("HELLO USSR");
			String readLine = readResponse();
			System.out.println(readLine);
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}
	protected void sendCommand(String message)
	{
		txtout.println(message);
		txtout.flush();
	}

	private String readResponse()
	{	
		String response  = ""; 
		try
		{
			response = txtin.readLine();
			System.out.println(response);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return response;
		
	}
}
