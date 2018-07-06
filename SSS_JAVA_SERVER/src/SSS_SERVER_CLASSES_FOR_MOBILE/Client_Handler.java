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
import SSS_SERVER_FUNCTIONS.Insert_User;
import SSS_SERVER_FUNCTIONS.Return_Train_Models;
import SSS_SERVER_FUNCTIONS.Train_Images_Model;
import SSS_SERVER_FUNCTIONS.Update_Train_Data;
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
							String User_Name = in.readUTF();
							Insert_User clas = new Insert_User();						
							String response = clas.do_The_Work(URL,User_Name);
							System.out.println(response);
							processing = false;
						
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
							/*
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
							*/
							
							File image = new File("0216-bey.jpg");
							matEng.eval("image_Path = '"+ image.getAbsolutePath().toString()+"'",null,null);
							matEng.eval("user_ID = "+64,null,null);
							Update_Train_Data Upd = new Update_Train_Data();
							String model_Name = Upd.do_The_Work(URL, "64");
							String[] model_Name_Tokens = model_Name.split("_");
							String Matlab_Path = new File(".").getCanonicalPath() + "/data";
							int model_Version =  Integer.parseInt(model_Name_Tokens[2]) - 1;
							String ML_features_Database = Matlab_Path + "/MATLAB_TRAIN_DATA/" + model_Name_Tokens[0] +"_" + model_Name_Tokens[1]+ "_" + model_Version+ ".mat";
							String ML_features_Database2 = Matlab_Path + "/MATLAB_TRAIN_DATA/" + model_Name + ".mat";							
							matEng.eval("path = '"+ML_features_Database+"'",null,null);
							matEng.eval("path2 = '"+ML_features_Database2+"'",null,null);
							matEng.eval("run('" + Matlab_Path + "/MATLAB_SCRIPTS/Update_Training_Data11.m')",null,null);

							break;
						case "TRAIN_IMAGES_MODEL":
							
							
							Train_Images_Model train_model = new Train_Images_Model();
							String[] response_Tokens = train_model.do_The_Work(URL, "64").split(",");
							
							System.out.println(response_Tokens[0]);
							
							
							if (response_Tokens.length-1  == 4)
							{
								matEng.eval("class_1 = "+Integer.parseInt(response_Tokens[1]),null,null);
								matEng.eval("class_2 = "+Integer.parseInt(response_Tokens[2]),null,null);
								matEng.eval("class_3 = "+Integer.parseInt(response_Tokens[3]),null,null);
								matEng.eval("class_4 = "+Integer.parseInt(response_Tokens[4]),null,null);

							}
							else if (response_Tokens.length-1 == 3)
							{
								matEng.eval("class_1 = "+Integer.parseInt(response_Tokens[1]),null,null);
								matEng.eval("class_2 = "+Integer.parseInt(response_Tokens[2]),null,null);
								matEng.eval("class_3 = "+Integer.parseInt(response_Tokens[3]),null,null);
								matEng.eval("class_4 = "+Integer.parseInt(response_Tokens[3]+1),null,null);
	
							}
							else if (response_Tokens.length-1 == 2)
							{
								matEng.eval("class_1 = "+Integer.parseInt(response_Tokens[1]),null,null);
								matEng.eval("class_2 = "+Integer.parseInt(response_Tokens[2]),null,null);
								matEng.eval("class_3 = "+Integer.parseInt(response_Tokens[2]+1),null,null);
								matEng.eval("class_4 = "+Integer.parseInt(response_Tokens[2]+2),null,null);

							}
							else
							{
								matEng.eval("class_1 = "+Integer.parseInt(response_Tokens[1]),null,null);
								matEng.eval("class_2 = "+Integer.parseInt(response_Tokens[1]+2),null,null);
								matEng.eval("class_3 = "+Integer.parseInt(response_Tokens[1]+3),null,null);
								matEng.eval("class_4 = "+Integer.parseInt(response_Tokens[1]+4),null,null);

							}

							String Matlab_Path_train = new File(".").getCanonicalPath() + "/data";
							String ML_features_Database_train = Matlab_Path_train + "/MATLAB_TRAIN_DATA/" + response_Tokens[0] + ".mat";
							
							matEng.eval("path = '"+ML_features_Database_train+"'",null,null);
							
							
							String Trained_Model_File = Matlab_Path_train + "/MATLAB_TRAINED_MODELS/" + response_Tokens[0] + ".mat";

							matEng.eval("path2 = '"+Trained_Model_File+"'",null,null);
							matEng.eval("run('" + Matlab_Path_train + "/MATLAB_SCRIPTS/RUN_ESS5.m')",null,null);
							//matlab.Execute("run('" + Matlab_Path + "MATLAB_SCRIPTS/RUN_ESS5.m')");


							break;
							
						case "PRED_USER":
							
							
							Return_Train_Models models = new Return_Train_Models();
							String models_string = models.Do_The_Work(URL);
							
							System.out.println(models_string);
							
							/*
							List<Trained_Model> trained_Models = (from Trained_Model in linq.Trained_Models
																  select Trained_Model).ToList();
							int[] results = new int[trained_Models.Count];
							int[] fet_Match = new int[trained_Models.Count];
							for (int i = 0; i < trained_Models.Count; i++)
							{
								List<User> users = (from User in linq.Users
													where User.Model_ID == trained_Models.ElementAt(i).Id
													select User).ToList();
								matlab.Execute("'clear all'");

								if (users.Count == 4)
								{
									matlab.PutWorkspaceData("class_1", "base", users.ElementAt(0).Id);
									matlab.PutWorkspaceData("class_2", "base", users.ElementAt(1).Id);
									matlab.PutWorkspaceData("class_3", "base", users.ElementAt(2).Id);
									matlab.PutWorkspaceData("class_4", "base", users.ElementAt(3).Id);
								}
								else if (users.Count == 3)
								{
									matlab.PutWorkspaceData("class_1", "base", users.ElementAt(0).Id);
									matlab.PutWorkspaceData("class_2", "base", users.ElementAt(1).Id);
									matlab.PutWorkspaceData("class_3", "base", users.ElementAt(2).Id);
									matlab.PutWorkspaceData("class_4", "base", users.ElementAt(2).Id + 1);
								}
								else if (users.Count == 2)
								{
									matlab.PutWorkspaceData("class_1", "base", users.ElementAt(0).Id);
									matlab.PutWorkspaceData("class_2", "base", users.ElementAt(1).Id);
									matlab.PutWorkspaceData("class_3", "base", users.ElementAt(1).Id + 1);
									matlab.PutWorkspaceData("class_4", "base", users.ElementAt(1).Id + 2);
								}
								else
								{
									matlab.PutWorkspaceData("class_1", "base", users.ElementAt(0).Id);
									matlab.PutWorkspaceData("class_2", "base", users.ElementAt(0).Id + 2);
									matlab.PutWorkspaceData("class_3", "base", users.ElementAt(0).Id + 3);
									matlab.PutWorkspaceData("class_4", "base", users.ElementAt(0).Id + 4);
								}


								matlab.PutWorkspaceData("Image_Name_of", "base", image_Name);

								string Trained_Model2 = Matlab_Path + "MATLAB_TRAINED_MODELS/" + trained_Models.ElementAt(i).Model_Name + "_" + trained_Models.ElementAt(i).Model_Version + ".mat";
								matlab.PutWorkspaceData("path", "base", Trained_Model2);
								matlab.Execute("run('" + Matlab_Path + "MATLAB_SCRIPTS/Predict_User10.m')");
								results[i] = Convert.ToInt32(Regex.Split(matlab.Execute("max_Class").Replace(" ", string.Empty), "=")[1]);
								string[] value0 = Regex.Split(matlab.Execute("max_Num").Replace(" ", string.Empty), "=");
								fet_Match[i] = Convert.ToInt32(value0[1]);
							}
							int max = fet_Match.Max();
							string result_With_Max = "";
							for (int i = 0; i < trained_Models.Count; i++)
							{
								if (fet_Match[i] == max)
								{
									User user = (from User in linq.Users
												 where User.Id == results[i]
												 select User).First();
									result_With_Max = user.User_Name;
								}
							}
							return result_With_Max;
							*/
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
