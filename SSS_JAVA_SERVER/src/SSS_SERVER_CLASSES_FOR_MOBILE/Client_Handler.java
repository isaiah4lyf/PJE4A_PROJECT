package SSS_SERVER_CLASSES_FOR_MOBILE;
import java.awt.TextArea;
import java.io.*;
import java.net.Socket;
import com.mathworks.engine.MatlabEngine;

import SSS_SERVER_FUNCTIONS.Decrement_Images_Model_Version;
import SSS_SERVER_FUNCTIONS.Decrement_Model_Version_VN;
import SSS_SERVER_FUNCTIONS.Insert_Image;
import SSS_SERVER_FUNCTIONS.Insert_User;
import SSS_SERVER_FUNCTIONS.Insert_Voice_Note;
import SSS_SERVER_FUNCTIONS.Return_Images_For_Mobile;
import SSS_SERVER_FUNCTIONS.Return_Train_Models;
import SSS_SERVER_FUNCTIONS.Return_Train_Models_VN;
import SSS_SERVER_FUNCTIONS.Return_User_With_ID;
import SSS_SERVER_FUNCTIONS.Return_Users_In_Model;
import SSS_SERVER_FUNCTIONS.Return_Users_In_Model_VN;
import SSS_SERVER_FUNCTIONS.Train_Images_Model;
import SSS_SERVER_FUNCTIONS.Update_Train_Data;
import SSS_SERVER_FUNCTIONS.Update_Train_Data_VN;




public class Client_Handler implements Runnable{

	private Socket			connectionToClient;

	private DataInputStream in;
	private DataOutputStream out;
	private MatlabEngine matEng;
	private String URL;
	private String Matlab_Path_train;
	private TextArea console_Like;
	
	
	public Client_Handler(Socket socketConnectionToClient,MatlabEngine matEng,String URL,TextArea console_Like)
	{
		this.connectionToClient = socketConnectionToClient;
		this.matEng = matEng;
		this.URL = URL;
		this.console_Like = console_Like;
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
		console_Like.append("Processing client commands \n");
		boolean processing = true;
		try
		{
			while (processing)
			{

				String command = in.readUTF();
				console_Like.append(command + "\n");
				try 
				{
					switch (command)
					{
						case "REG":
							String User_Name = in.readUTF();
							String email = in.readUTF();
							String password = in.readUTF();
							Insert_User clas = new Insert_User();						
							String response = clas.do_The_Work(URL,User_Name,email,password);
							System.out.println(response);
							sendMessage(response);
							if(response.equals("false"))
							{
								File dir = new File("data/MATLAB_TRAIN_DATA/" + User_Name);
								dir.mkdir();
								File dir2 = new File("data/MATLAB_TRAIN_DATA/" + User_Name+"/MATLAB_PRED_DATA");
								dir2.mkdir();			
							}				
							processing = false;						
							break;

						case "COUNT_IMAGES":
							String user_ID_count = in.readUTF();
							Return_Images_For_Mobile images_class = new Return_Images_For_Mobile();
							String[] images = images_class.Do_The_Work(URL);
							int count = 0;
							for(int i = 0; i < images.length; i++)
							{
								if(user_ID_count.equals(images[i].split(",")[2]))
								{
									count++;
								}
							}
							sendMessage(String.valueOf(count));
							processing = false;

							break;
						case "INSERT_IMAGE":
			
							processing = false;
							String user_ID_ = in.readUTF();
							String user_name = in.readUTF();
							String size_string = in.readUTF();
							System.out.println(size_string);
							int size = Integer.parseInt(size_string);
							String title = in.readUTF();
							String from_Cam = in.readUTF();
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
								File imageInsta = new File("data/MATLAB_TRAIN_DATA/"+user_name+"/"+title);
								if(imageInsta.exists()){
									
									ByteToFile = new BufferedOutputStream(new FileOutputStream(new File("data/MATLAB_TRAIN_DATA/"+user_name+"/(1)"+title)));
									ByteToFile.write(buffer);
									ByteToFile.flush();
									ByteToFile.close();
								}
								else
								{
									ByteToFile = new BufferedOutputStream(new FileOutputStream(new File("data/MATLAB_TRAIN_DATA/"+user_name+"/"+title)));
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
								
							}
							

							
							File image = new File("data/MATLAB_TRAIN_DATA/"+user_name+"/"+title);

							
							matEng.eval("image_Path = '"+ image.getAbsolutePath().toString()+"'",null,null);
							matEng.eval("user_ID = "+user_ID_,null,null);
							matEng.eval("image1 = imread(image_Path);",null,null);
							System.out.println(from_Cam);
							if(from_Cam.equals("yes"))
							{
								matEng.eval("I1 = imrotate(image1,90);",null,null);
							}
							else
							{
								matEng.eval("I1 = image1;",null,null);
							}
							Update_Train_Data Upd = new Update_Train_Data();
							String model_Name = Upd.do_The_Work(URL, user_ID_);
							String[] model_Name_Tokens = model_Name.split("_");
							String Matlab_Path = new File(".").getCanonicalPath() + "/data";
							int model_Version =  Integer.parseInt(model_Name_Tokens[2]) - 1;
							String ML_features_Database = Matlab_Path + "/MATLAB_TRAIN_DATA/" + model_Name_Tokens[0] +"_" + model_Name_Tokens[1]+ "_" + model_Version+ ".mat";
							String ML_features_Database2 = Matlab_Path + "/MATLAB_TRAIN_DATA/" + model_Name + ".mat";							
							matEng.eval("path = '"+ML_features_Database+"'",null,null);
							matEng.eval("path2 = '"+ML_features_Database2+"'",null,null);
							matEng.eval("run('" + Matlab_Path + "/MATLAB_SCRIPTS/Update_Training_Data11.m')",null,null);
							double status = matEng.getVariable("status");
							double exception = matEng.getVariable("exception");
							
							
							if(status == 1.0 || exception == 1.0)
							{
								image.delete();
								Decrement_Images_Model_Version dec = new Decrement_Images_Model_Version();
								dec.do_The_Work(URL, String.valueOf(user_ID_));
								sendMessage("Invalid Image....");
								
							}
							else
							{
								sendMessage("Upload Successful");
								Insert_Image insert_class = new Insert_Image();
								console_Like.append(insert_class.do_The_Work(URL,user_ID_,image.getName())+"\n");
								
							}
							
							processing = false;
							break;
						case "INSERT_VN":
							String user_ID_VN = in.readUTF();
							String user_name_VN = in.readUTF();
							String size_VN = in.readUTF();
							String tittle_VN = in.readUTF();
							
							System.out.println(size_VN);
							BufferedOutputStream ByteToFile_VN = null;
							try
							{
								
								int size_Int = Integer.parseInt(size_VN);
								byte[] buffer = new byte[size_Int];
								readFully(in,buffer,0,size_Int);
								int extra = in.available();
								if (extra > 0)
								{
									byte[] buffer2 = new byte[extra];
									in.read(buffer2);
								}
								File imageInsta = new File("data/MATLAB_TRAIN_DATA/"+user_name_VN+"/"+tittle_VN);
								if(imageInsta.exists()){
									
									ByteToFile_VN = new BufferedOutputStream(new FileOutputStream(new File("data/MATLAB_TRAIN_DATA/"+user_name_VN+"/(1)"+tittle_VN)));
									ByteToFile_VN.write(buffer);
									ByteToFile_VN.flush();
									ByteToFile_VN.close();
								}
								else
								{
									ByteToFile_VN = new BufferedOutputStream(new FileOutputStream(new File("data/MATLAB_TRAIN_DATA/"+user_name_VN+"/"+tittle_VN)));
									ByteToFile_VN.write(buffer);
									ByteToFile_VN.flush();
									ByteToFile_VN.close();
								}
								sendMessage("Upload Successful");
							}
							catch(IOException ex){
								ex.printStackTrace();
								sendMessage(ex.toString());
	
							}
							finally
							{
								if (ByteToFile_VN != null)
									try {
										ByteToFile_VN.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								
							}
							File audio = new File("data/MATLAB_TRAIN_DATA/"+user_name_VN+"/"+tittle_VN);

							
							matEng.eval("audio_path = '"+ audio.getAbsolutePath().toString()+"'",null,null);
							matEng.eval("user_ID = "+Integer.parseInt(user_ID_VN),null,null);


							Update_Train_Data_VN Upd2 = new Update_Train_Data_VN();
							String model_Name2 = Upd2.do_The_Work(URL, user_ID_VN);
							String[] model_Name_Tokens2 = model_Name2.split("_");
							String Matlab_Path2 = new File(".").getCanonicalPath() + "/data";
							int model_Version2 =  Integer.parseInt(model_Name_Tokens2[2]) - 1;
							
							String ML_features_Database3 = Matlab_Path2 + "/MATLAB_TRAIN_DATA/VOICE_NOTES_DATA/" + model_Name_Tokens2[0] +"_" + model_Name_Tokens2[1]+ "_" + model_Version2+ ".mat";
							String ML_features_Database4 = Matlab_Path2 + "/MATLAB_TRAIN_DATA/VOICE_NOTES_DATA/" + model_Name2 + ".mat";							
							matEng.eval("path = '"+ML_features_Database3+"'",null,null);
							matEng.eval("path2 = '"+ML_features_Database4+"'",null,null);
							matEng.eval("run('" + Matlab_Path2 + "/MATLAB_SCRIPTS/SOUND_PROCESSING/UPDATE_TRAIN_DATA.m')",null,null);
							
							double exception3 = matEng.getVariable("exception");
							
							
							if(exception3 == 1.0)
							{
								audio.delete();
								Decrement_Model_Version_VN dec = new Decrement_Model_Version_VN();
								dec.do_The_Work(URL, String.valueOf(user_ID_VN));
								sendMessage("Invalid Voice Note....");
								
							}
							else
							{
								sendMessage("Upload Successful");
								Insert_Voice_Note insert_class = new Insert_Voice_Note();
								console_Like.append(insert_class.do_The_Work(URL,user_ID_VN,audio.getName())+"\n");
								
							}
							processing = false;
							break;
						case "TRAIN_IMAGES_MODEL":
							
							String user_ID = in.readUTF();
							Train_Images_Model train_model = new Train_Images_Model();
							String[] response_Tokens = train_model.do_The_Work(URL, user_ID).split(",");
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
							processing = false;
							break;
							
						case "PRED_USER":
							
							String user_ID2_ = in.readUTF();
							String user_name2 = in.readUTF();
							String size_string2 = in.readUTF();
							System.out.println(size_string2);
							int size2 = Integer.parseInt(size_string2);
							String title2 = in.readUTF();
							System.out.println(title2);
							BufferedOutputStream ByteToFile2 = null;
							try{
								System.out.println(size2);
								byte[] buffer = new byte[size2];
								readFully(in,buffer,0,size2);
								int extra = in.available();
								if (extra > 0)
								{
									byte[] buffer2 = new byte[extra];
									in.read(buffer2);
								}
								File imageInsta = new File("data/MATLAB_TRAIN_DATA/"+user_name2+"/MATLAB_PRED_DATA/"+title2+".jpg");
								if(imageInsta.exists()){
									
									ByteToFile2 = new BufferedOutputStream(new FileOutputStream(new File("data/MATLAB_TRAIN_DATA/"+user_name2+"/MATLAB_PRED_DATA/"+title2+"(1).jpg")));
									ByteToFile2.write(buffer);
									ByteToFile2.flush();
									ByteToFile2.close();
								}
								else
								{
									ByteToFile2 = new BufferedOutputStream(new FileOutputStream(new File("data/MATLAB_TRAIN_DATA/"+user_name2+"/MATLAB_PRED_DATA/"+title2+".jpg")));
									ByteToFile2.write(buffer);
									ByteToFile2.flush();
									ByteToFile2.close();
								}
							}
							catch(IOException ex){
								ex.printStackTrace();

							}
							finally
							{
								if (ByteToFile2 != null)
									try {
										ByteToFile2.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								
							}
							
							Return_Train_Models models = new Return_Train_Models();
							String[] models_string = models.Do_The_Work(URL);

							int[] results = new int[models_string.length];
							int[] fet_Match = new int[models_string.length];
							
							File image2 = null;
							
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
								image2 = new File("data/MATLAB_TRAIN_DATA/"+user_name2+"/MATLAB_PRED_DATA/"+title2+".jpg");
								matEng.eval("Image_Name_of = '"+ image2.getAbsolutePath().toString()+"'",null,null);
								
								int model_version = Integer.parseInt(models_string[i].split(",")[3] );
								String Trained_Model2 = Matlab_Path_train + "/MATLAB_TRAINED_MODELS/" + models_string[i].split(",")[1] + "_" +model_version+ ".mat";
								File matFile = new File(Trained_Model2);
								
								while(!matFile.exists() && model_version > 0)
								{
									System.out.println(Trained_Model2);
									model_version--;
									Trained_Model2 = Matlab_Path_train + "/MATLAB_TRAINED_MODELS/" + models_string[i].split(",")[1] + "_" +model_version+ ".mat";						
									matFile = new File(Trained_Model2);
								}
								
								
								matEng.eval("path = '"+Trained_Model2+"'",null,null);
								matEng.eval("run('" + Matlab_Path_train + "/MATLAB_SCRIPTS/Predict_User10.m')",null,null);
								
								double status2 = matEng.getVariable("status");
								double exception2 = matEng.getVariable("exception");
								
								
								if(status2 == 1.0 || exception2 == 1.0)
								{
									image2.delete();
									System.out.println("here");
									break;									
								}
								else
								{
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
							}									
							double status2 = matEng.getVariable("status");
							double exception2 = matEng.getVariable("exception");												
							if(status2 == 1.0 || exception2 == 1.0)
							{
								image2.delete();
								sendMessage("Invalid Image");
							}
							else
							{
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
								String[] results_tokens = result_With_Max.split(",");
								sendMessage(results_tokens[1]);
							}

							processing = false;
							break;
						case "PRED_USER_VN":
							String user_ID4_ = in.readUTF();
							String user_name4 = in.readUTF();
							String size_string4 = in.readUTF();
							System.out.println(size_string4);
							int size4 = Integer.parseInt(size_string4);
							String title4 = in.readUTF();
							System.out.println(title4);
							BufferedOutputStream ByteToFile4 = null;
							try{
								System.out.println(size4);
								byte[] buffer = new byte[size4];
								readFully(in,buffer,0,size4);
								int extra = in.available();
								if (extra > 0)
								{
									byte[] buffer2 = new byte[extra];
									in.read(buffer2);
								}
								File imageInsta = new File("data/MATLAB_TRAIN_DATA/"+user_name4+"/MATLAB_PRED_DATA/"+title4);
								if(imageInsta.exists()){
									
									ByteToFile4 = new BufferedOutputStream(new FileOutputStream(new File("data/MATLAB_TRAIN_DATA/"+user_name4+"/MATLAB_PRED_DATA/(1)"+title4)));
									ByteToFile4.write(buffer);
									ByteToFile4.flush();
									ByteToFile4.close();
								}
								else
								{
									ByteToFile4 = new BufferedOutputStream(new FileOutputStream(new File("data/MATLAB_TRAIN_DATA/"+user_name4+"/MATLAB_PRED_DATA/"+title4)));
									ByteToFile4.write(buffer);
									ByteToFile4.flush();
									ByteToFile4.close();
								}
							}
							catch(IOException ex){
								ex.printStackTrace();

							}
							finally
							{
								if (ByteToFile4 != null)
									try {
										ByteToFile4.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								
							}
							Return_Train_Models_VN models2 = new Return_Train_Models_VN();
							String[] models_string2 = models2.Do_The_Work(URL);

							int[] results2 = new int[models_string2.length];
							int[] fet_Match2 = new int[models_string2.length];
							
							File audio2 = null;
							
							for (int i = 0; i < models_string2.length; i++)
							{
								String model_ID = models_string2[i].split(",")[0];
								Return_Users_In_Model_VN users = new Return_Users_In_Model_VN();
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
								audio2 = new File("data/MATLAB_TRAIN_DATA/"+user_name4+"/MATLAB_PRED_DATA/"+title4);
								matEng.eval("audio_path = '"+ audio2.getAbsolutePath().toString()+"'",null,null);
								
								int model_version = Integer.parseInt(models_string2[i].split(",")[3] );
								String Trained_Model2 = Matlab_Path_train + "/MATLAB_TRAINED_MODELS/SOUND_PROCESSING/" + models_string2[i].split(",")[1] + "_" +model_version+ ".mat";
								File matFile = new File(Trained_Model2);
								
								while(!matFile.exists() && model_version > 0)
								{
									System.out.println(Trained_Model2);
									model_version--;
									Trained_Model2 = Matlab_Path_train + "/MATLAB_TRAINED_MODELS/SOUND_PROCESSING/" + models_string2[i].split(",")[1] + "_" +model_version+ ".mat";						
									matFile = new File(Trained_Model2);
								}
								
								
								matEng.eval("path = '"+Trained_Model2+"'",null,null);
								matEng.eval("run('" + Matlab_Path_train + "/MATLAB_SCRIPTS/SOUND_PROCESSING/PRED_USER.m')",null,null);
								
						
								double exception4 = matEng.getVariable("exception");
								
								
								if(exception4 == 1.0)
								{
									audio2.delete();
									System.out.println("here");
									break;									
								}
								else
								{

									StringWriter output_class = new StringWriter();
									matEng.eval("max_Class",output_class,null);
									String max_class_String = output_class.toString().replaceAll("\n","");
									int max_class = Integer.parseInt(max_class_String.split("=")[1].replaceAll(" ",""));							
									StringWriter output_num = new StringWriter();
									matEng.eval("max_Num",output_num,null);
									String max_num_String = output_num.toString().replaceAll("\n","");
									int max_num = Integer.parseInt(max_num_String.split("=")[1].replaceAll(" ",""));
									results2[i] = max_class;
									fet_Match2[i] = max_num;
								}																							
							}									
							
							double exception4 = matEng.getVariable("exception");												
							if(exception4 == 1.0)
							{
								audio2.delete();
								sendMessage("Invalid Image");
							}
							else
							{
								int max = fet_Match2[0];
								for(int i = 0; i < fet_Match2.length; i++)
								{
									if(fet_Match2[i] > max)
									{
										max = fet_Match2[i];
									}
								}
								String result_With_Max = "";
								for (int i = 0; i < models_string2.length; i++)
								{
									if (fet_Match2[i] == max)
									{						
										Return_User_With_ID user = new Return_User_With_ID();										
										result_With_Max = user.do_The_Work(URL, String.valueOf(results2[i]));
									}
								}
								System.out.println(result_With_Max);	
								String[] results_tokens = result_With_Max.split(",");
								sendMessage(results_tokens[1]);
							}
							processing = false;
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
			
			close();
			
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
			out.close();
			in.close();
			console_Like.append("Closing Client Connection.... \n");
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
