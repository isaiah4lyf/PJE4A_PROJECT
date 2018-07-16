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
import SSS_SERVER_FUNCTIONS.Return_User_With_ID;
import SSS_SERVER_FUNCTIONS.Return_Users_In_Model;
import SSS_SERVER_FUNCTIONS.Train_Images_Model;
import SSS_SERVER_FUNCTIONS.Update_Train_Data;
import sun.misc.BASE64Decoder;



public class Client_Handler implements Runnable{

	private Socket			connectionToClient;

	private DataInputStream in;
	private DataOutputStream out;
	private MatlabEngine matEng;
	private String URL;
	private String Matlab_Path_train;
	
	
	public Client_Handler(Socket socketConnectionToClient,MatlabEngine matEng,String URL)
	{
		this.connectionToClient = socketConnectionToClient;
		this.matEng = matEng;
		this.URL = URL;
		try
		{
			in = new DataInputStream(connectionToClient.getInputStream());
			out = new DataOutputStream(connectionToClient.getOutputStream());
			Matlab_Path_train  = new File(".").getCanonicalPath() + "/data";
			
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
							if(response.equals("true"))
							{
								File dir = new File("data/MATLAB_TRAIN_DATA/" + User_Name);
								dir.mkdir();
							}
							processing = false;
						
							break;
						case "LOGIN":

							break;
						case "INSERT_IMAGE":
			
							processing = false;
							String user_name = in.readUTF();
							String size_string = in.readUTF();
							System.out.println(size_string);
							int size = Integer.parseInt(size_string);
							String title = in.readUTF();
							System.out.println(title);
							BufferedOutputStream ByteToFile = null;
							try{
								System.out.println(size);
								byte[] buffer = new byte[size];
								readFully(in,buffer,0,size);
								int extra = in.available();
								if (extra > 0)
								{
									byte[] buffer2 = new byte[extra];
									in.read(buffer2);
								}
								File imageInsta = new File("data/MATLAB_TRAIN_DATA/"+user_name+"/"+title+".jpg");
								if(imageInsta.exists()){
									
									ByteToFile = new BufferedOutputStream(new FileOutputStream(new File("data/MATLAB_TRAIN_DATA/"+user_name+"/"+title+"(1).jpg")));
									ByteToFile.write(buffer);
									ByteToFile.flush();
									ByteToFile.close();
								}
								else
								{
									ByteToFile = new BufferedOutputStream(new FileOutputStream(new File("data/MATLAB_TRAIN_DATA/"+user_name+"/"+title+".jpg")));
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

							File image = new File("data/MATLAB_TRAIN_DATA/"+user_name+"/"+title+".jpg");
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

							
							String ML_features_Database_train = Matlab_Path_train + "/MATLAB_TRAIN_DATA/" + response_Tokens[0] + ".mat";
							
							matEng.eval("path = '"+ML_features_Database_train+"'",null,null);
							
							
							String Trained_Model_File = Matlab_Path_train + "/MATLAB_TRAINED_MODELS/" + response_Tokens[0] + ".mat";

							matEng.eval("path2 = '"+Trained_Model_File+"'",null,null);
							matEng.eval("run('" + Matlab_Path_train + "/MATLAB_SCRIPTS/RUN_ESS5.m')",null,null);

							break;
							
						case "PRED_USER":

							Return_Train_Models models = new Return_Train_Models();
							String[] models_string = models.Do_The_Work(URL);

							int[] results = new int[models_string.length];
							int[] fet_Match = new int[models_string.length];
							
							
							
							for (int i = 0; i < models_string.length; i++)
							{
								String model_ID = models_string[i].split(",")[0];
								Return_Users_In_Model users = new Return_Users_In_Model();
								String[] users_string = users.Do_The_Work(URL,model_ID);
								System.out.println(users_string[0]);
								
								

								matEng.eval("clear all",null,null);
								
								if (users_string.length  == 4)
								{
									matEng.eval("class_1 = "+Integer.parseInt(users_string[0].split(",")[0]),null,null);
									matEng.eval("class_2 = "+Integer.parseInt(users_string[1].split(",")[0]),null,null);
									matEng.eval("class_3 = "+Integer.parseInt(users_string[2].split(",")[0]),null,null);
									matEng.eval("class_4 = "+Integer.parseInt(users_string[3].split(",")[0]),null,null);

								}
								else if (users_string.length == 3)
								{
									matEng.eval("class_1 = "+Integer.parseInt(users_string[0].split(",")[0]),null,null);
									matEng.eval("class_2 = "+Integer.parseInt(users_string[1].split(",")[0]),null,null);
									matEng.eval("class_3 = "+Integer.parseInt(users_string[2].split(",")[0]),null,null);
									matEng.eval("class_4 = "+Integer.parseInt(users_string[2].split(",")[0]+1),null,null);
		
								}
								else if (users_string.length == 2)
								{
									matEng.eval("class_1 = "+Integer.parseInt(users_string[0].split(",")[0]),null,null);
									matEng.eval("class_2 = "+Integer.parseInt(users_string[1].split(",")[0]),null,null);
									matEng.eval("class_3 = "+Integer.parseInt(users_string[1].split(",")[0]+1),null,null);
									matEng.eval("class_4 = "+Integer.parseInt(users_string[1].split(",")[0]+2),null,null);

								}
								else
								{
									matEng.eval("class_1 = "+Integer.parseInt(users_string[0].split(",")[0]),null,null);
									matEng.eval("class_2 = "+Integer.parseInt(users_string[0].split(",")[0]+2),null,null);
									matEng.eval("class_3 = "+Integer.parseInt(users_string[0].split(",")[0]+3),null,null);
									matEng.eval("class_4 = "+Integer.parseInt(users_string[0].split(",")[0]+4),null,null);

								}
								File image2 = new File("0216-bey.jpg");
								matEng.eval("Image_Name_of = '"+ image2.getAbsolutePath().toString()+"'",null,null);
								
								String Trained_Model2 = Matlab_Path_train + "/MATLAB_TRAINED_MODELS/" + models_string[i].split(",")[1] + "_" + models_string[i].split(",")[3] + ".mat";
								matEng.eval("path = '"+Trained_Model2+"'",null,null);
								matEng.eval("run('" + Matlab_Path_train + "/MATLAB_SCRIPTS/Predict_User10.m')",null,null);
								
								StringWriter output_class = new StringWriter();
								matEng.eval("max_Class",output_class,null);
								String max_class_String = output_class.toString().replaceAll("\n","");
								int max_class = Integer.parseInt(max_class_String.split("=")[1].replaceAll(" ",""));
								
								StringWriter output_num = new StringWriter();
								matEng.eval("max_Num",output_num,null);
								String max_num_String = output_num.toString().replaceAll("\n","");
								int max_num = Integer.parseInt(max_num_String.split("=")[1].replaceAll(" ",""));

								results[i] = max_class;
								fet_Match[i] = max_num;
								
			
							}
							
							int max = fet_Match[0];
							for(int i = 0; i < fet_Match.length; i++)
							{
								if(fet_Match[i] > max)
								{
									max = fet_Match[i];
								}
							}
							
							String result_With_Max = "";
							for (int i = 0; i < models_string.length; i++)
							{
								if (fet_Match[i] == max)
								{
					
									Return_User_With_ID user = new Return_User_With_ID();
									
									result_With_Max = user.do_The_Work(URL, String.valueOf(results[i]));
								}
							}
							
							System.out.println(result_With_Max);
							
							/*
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
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}


}
