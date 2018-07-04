import  za.ac.uj.acsse.csc2b.practical05.client.Simple_Client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class ClientMain extends JFrame {
	
	public static void main(String[] args)
	{
		/*
		Client clientFrame = new Client("data/client");
		clientFrame.setSize(1500, 600);
		clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientFrame.setLocationRelativeTo(null);
		clientFrame.setVisible(true);
		*/
		Simple_Client client = new Simple_Client();

	}
}
