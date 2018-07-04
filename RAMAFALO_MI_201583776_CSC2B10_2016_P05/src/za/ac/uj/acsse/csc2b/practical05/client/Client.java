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

public class Client extends JFrame
{
	class DisplayPanel extends JPanel
	{
		private BufferedImage image;

		
		@Override
		protected void paintComponent(Graphics g)
		{
			// Displaying the image to the panel
			super.paintComponent(g);
			g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		}

		/**
		 * @return the image
		 */
		public BufferedImage getImage()
		{
			return image;
		}

		/**
		 * @param image
		 * the image to set
		 */
		public void setImage(BufferedImage image)
		{
			this.image = image;
			this.repaint();
		}

	}

	private Socket			clientSocket;
	private OutputStream	os;
	private InputStream		is;
	private PrintWriter		txtout;
	private BufferedReader	txtin;
	private JButton			btnConnect;
	private JButton			upload;
	private JButton			retrieve;
	private JButton			Instructions;
	private JButton			ClearBox;
	private JButton			requestList;
	private JButton			btnTerminate;
	private JTextField		IdBox;
	private JTextField		NameBox;
	private DisplayPanel	imgPanel;
	private String Location;
	private JTextArea messageArea;

	public Client(String Location)
	{
		this.Location = Location;
		setTitle("	Client");
		btnConnect = new JButton("Connect");
		upload = new JButton("Upload Image");
		retrieve = new JButton("Retrieve Image");
		Instructions = new JButton("Instructions");
		ClearBox = new JButton("Clear");
		btnTerminate = new JButton("Terminate");
		requestList = new JButton("Request list");

		JLabel IdLabel = new JLabel("Picture ID");
		IdBox = new JTextField(10);
		JLabel nameLabel = new JLabel("Picture Name");
		NameBox = new JTextField(15);

		imgPanel = new DisplayPanel();

	

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 7));
		
		topPanel.add(btnConnect);
		topPanel.add(IdLabel);
		topPanel.add(IdBox);
		topPanel.add(nameLabel);
		topPanel.add(NameBox);
		topPanel.add(requestList);
		topPanel.add(retrieve);
		topPanel.add(upload);
		topPanel.add(Instructions);
		topPanel.add(ClearBox);
		topPanel.add(btnTerminate);
		
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		messageArea = new JTextArea(10,10);
		messageArea.setForeground(Color.RED);
		messageArea.setBackground(Color.CYAN);
		messageArea.setEditable(false);
		JScrollPane scroll = new JScrollPane();
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.getViewport ().setView(messageArea);
		
		panel.add(messageArea);
		panel.add(imgPanel);
		add(topPanel, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		

		upload.setEnabled(false);
		btnTerminate.setEnabled(false);
		retrieve.setEnabled(false);
		requestList.setEnabled(false);
		
		btnConnect.addActionListener(new ActionListener()
		{
			//Connecting to the server onClick
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String host =
						JOptionPane.showInputDialog(Client.this, "Hostname", "localhost");
				String port = JOptionPane.showInputDialog(Client.this, "Port", "7451");
				try
				{
					clientSocket = new Socket(host, Integer.parseInt(port));
					is = clientSocket.getInputStream();
					os = clientSocket.getOutputStream();
					txtout = new PrintWriter(clientSocket.getOutputStream());
					txtin = new BufferedReader(new InputStreamReader(is));
					sendCommand("HELLO USSR");
					
					btnConnect.setEnabled(false);
					upload.setEnabled(true);
					btnTerminate.setEnabled(true);
					retrieve.setEnabled(true);
					requestList.setEnabled(true);
					messageArea.append("Connected to the server on port 7451" + "\n");
					messageArea.append(readResponse() + "\n");
				}
				catch (NumberFormatException ex)
				{
					ex.printStackTrace();
				}
				catch (UnknownHostException ex)
				{
					ex.printStackTrace();
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}

			}
		});
		
		requestList.addActionListener(new ActionListener()
		{
			//Requesting the list of the images from the server onClick
			@Override
			public void actionPerformed(ActionEvent e)
			{
				sendCommand("LIST");
				messageArea.append("LIST" + "\n");
				StringTokenizer tokens = new StringTokenizer(readResponse(), "*.*");
				String list = tokens.nextToken();
				int numOfImag = Integer.parseInt(tokens.nextToken());
				if(numOfImag > 0){
					messageArea.append("----- There is/are " + numOfImag + " Image(s) available ------" +"\n");
					StringTokenizer tokens1 = new StringTokenizer(list, "/");
					for(int i = 1; i < numOfImag + 1; i++){
						messageArea.append(i  + ". "+ tokens1.nextToken() + "\n");
					}
				}
				else{
					messageArea.append("There are no images available." + "\n");
				}
					

			}
		});

		retrieve.addActionListener(new ActionListener()
		{
			//Retrieving the requested image onClick

			@Override
			public void actionPerformed(ActionEvent e)
			{
				
				String ID = IdBox.getText();
				String retrieveMessage = "RET " + ID;
				sendCommand(retrieveMessage);
				messageArea.append(retrieveMessage + "\n");
				
				BufferedImage image = readImage();

				imgPanel.setImage(image);
					
				IdBox.setText("");
				
				
			}

			private BufferedImage readImage()
			{
				BufferedImage image = null;
				try
				{
					image = ImageIO.read(is);
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
				}
				return image;
			}
		});
		
		upload.addActionListener(new ActionListener()
		{
			
			//Uploading the image to the server onClick
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String ID = IdBox.getText();
				String Name = NameBox.getText();
				BufferedImage image = getImage(Name,Location);
				sendCommand("SEND " + ID + " "  + Name);
				messageArea.append("SEND " + ID + " "  + Name + "\n");
				
				try
				{
					ImageIO.write(image, "BMP", os);
					messageArea.append(readResponse() + "\n");
					IdBox.setText("");
					NameBox.setText("");
					
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
					
				
				}
				

			}
			private BufferedImage getImage(String Name,String Location)
			{
				String filename = Name + ".jpg";
				BufferedImage image = null;
				try
				{
					image = ImageIO.read(new File(Location, filename));
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
					messageArea.append("Can't read image. Please reconnect and try again." + "\n");
					sendCommand("TERMINATE");
					messageArea.append(readResponse() + "\n");
					close();
					btnConnect.setEnabled(true);
					upload.setEnabled(false);
					retrieve.setEnabled(false);
					requestList.setEnabled(false);
					btnTerminate.setEnabled(false);
					

					
				}
				return image;
			}


		});

		btnTerminate.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				sendCommand("TERMINATE");
				messageArea.append(readResponse() + "\n");
				close();
				btnConnect.setEnabled(true);
				upload.setEnabled(false);
				retrieve.setEnabled(false);
				requestList.setEnabled(false);
				btnTerminate.setEnabled(false);

			}
		});
		
		Instructions.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				messageArea.append("---------- Instructions on how to use this Application ---------------" + "\n");
				messageArea.append("1. Click 'Connect' to connect to the server" + "\n");
				messageArea.append("2. Click 'Request List' to request the list of the images from the server" + "\n");
				messageArea.append("3. Type image ID on 'Picture ID Box' and click 'Retrieve image' to retrieve image from the server " + "\n");
				messageArea.append("4. Type image ID on 'Picture ID Box' and Image name on 'Picture name Box' and then click upload image to upload image to the server" + "\n");
				messageArea.append("5. Click 'Clear' to clear the message Area" + "\n");
				messageArea.append("6. Click 'Terminate' to disconnect" + "\n");
				messageArea.append("7. To upload, place the image in directory 'data/client' and in the NameBox type only the name of the picture i.e without extension" + "\n");
				messageArea.append("8. To test, use the sample images test1 and test2" + "\n"); 
				

			}
		});
		
		ClearBox.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				
				messageArea.setText("");

			}
		});
	}
	
	private void close()
	{
		if (clientSocket != null)
		{
			try
			{
				clientSocket.close();
				clientSocket = null;
			}
			catch (IOException ex)
			{
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
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
