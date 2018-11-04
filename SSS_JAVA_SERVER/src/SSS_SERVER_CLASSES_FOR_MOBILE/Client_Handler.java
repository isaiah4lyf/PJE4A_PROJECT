package SSS_SERVER_CLASSES_FOR_MOBILE;
import java.awt.TextArea;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.mathworks.engine.MatlabEngine;

import SSS_SERVER_FUNCTIONS.Decrement_Images_Model_Version;
import SSS_SERVER_FUNCTIONS.Decrement_Model_Version_VN;
import SSS_SERVER_FUNCTIONS.Get_Device_Mac_Data_With_Mac;
import SSS_SERVER_FUNCTIONS.Insert_Accuracy_Users;
import SSS_SERVER_FUNCTIONS.Insert_Accuracy_Users_First_Version;
import SSS_SERVER_FUNCTIONS.Insert_Device_Mac;
import SSS_SERVER_FUNCTIONS.Insert_Image;
import SSS_SERVER_FUNCTIONS.Insert_User;
import SSS_SERVER_FUNCTIONS.Insert_Voice_Note;
import SSS_SERVER_FUNCTIONS.Login;
import SSS_SERVER_FUNCTIONS.Return_Accuracy_Users;
import SSS_SERVER_FUNCTIONS.Return_Accuracy_Users_First_Version;
import SSS_SERVER_FUNCTIONS.Return_Current_Num_Images;
import SSS_SERVER_FUNCTIONS.Return_Images_For_Mobile;
import SSS_SERVER_FUNCTIONS.Return_Train_Models;
import SSS_SERVER_FUNCTIONS.Return_Train_Models_VN;
import SSS_SERVER_FUNCTIONS.Return_User_With_ID;
import SSS_SERVER_FUNCTIONS.Return_Users_In_Model;
import SSS_SERVER_FUNCTIONS.Return_Users_In_Model_VN;
import SSS_SERVER_FUNCTIONS.Return_VNs_For_Mobile;
import SSS_SERVER_FUNCTIONS.Train_Images_Model;
import SSS_SERVER_FUNCTIONS.Update_Accuracy_Users;
import SSS_SERVER_FUNCTIONS.Update_Accuracy_Users_First_Version;
import SSS_SERVER_FUNCTIONS.Update_Train_Data;
import SSS_SERVER_FUNCTIONS.Update_Train_Data_VN;
import SSS_SERVER_FUNCTIONS.Update_Training_Accuracy_Users_First_Version;
import SSS_SERVER_FUNCTIONS.Update_Training_Accuracy_Users_New;
import SSS_SERVER_FUNCTIONS.Update_Training_Accuracy_Users_VN;
import SSS_SERVER_FUNCTIONS.Check_User_Name;
import SSS_SERVER_FUNCTIONS.Check_User_Name_Vision;
import SSS_SERVER_FUNCTIONS.Confirm_Number;
import SSS_SERVER_FUNCTIONS.Insert_Device_Coordinate;
public class Client_Handler implements Runnable{

	private Socket connectionToClient;
	private DataInputStream in;
	private DataOutputStream out;
	private MatlabEngine matEng;
	private String URL;
	private String Matlab_Path_train;
	private TextArea console_Like;
	private String ServerUrl = "http://smartphonesecuritysystem.dedicated.co.za:8080/SSS_JAVA_SERVER/data/SSS_VISION/";
	
	
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
							Register_User();
							processing = false;						
							break;
						case "LOGIN":
							Login();
							processing = false;	
							break;
						case "CHECK_ACCURACY":
							Check_Accuracy_For_Login();
							processing = false;
							break;
						case "COUNT_IMAGES":
							Count_Images();
							processing = false;
							break;
						case "COUNT_VN":
							Count_VN();
							processing = false;
							break;
						case "INSERT_IMAGE":
							Insert_Image();
							processing = false;
							break;
						case "INSERT_VN":
							Insert_VN();
							processing = false;
							break;
						case "TRAIN_IMAGES_MODEL":						
							Train_Images_Model();
							processing = false;
							break;			
						case "TRAIN_VN_MODEL":						
							Train_VN_Model();
							processing = false;
							break;	
						case "PRED_USER":
							Pred_User_Image();
							processing = false;
							break;
						case "TEST_IMAGE_PRED_ACCU":
							Test_Prediction_Accuracy_Images(); 
							processing = false;
							break;
						case "PRED_USER_VN":
							Pred_User_VN();
							processing = false;
							break;
						case "GET_DEVICE_MAC":
							Get_Device_Mac();
							processing = false;
							break;
						case "GET_CURRENT_NUM_IMAGES":
							Get_Current_Num_Images();
							processing = false;
							break;
						case "ADD_USER":
							Get_Current_Num_Images();
							processing = false;
							break;
						case "CHECK_USER_NAME":
							Check_User_Name();
							processing = false;
							break;
						case "CONFIRM_NUMBER":
							Confirm_Number();
							processing = false;
							break;
						case "INSERT_DEVICE_COORDINATE":
							Insert_Device_Coordinate();
							processing = false;
							break;
						case "SSS_VISION":
							SSS_Vision();
							processing = false;
							break;
						case "ADD_USER_FROM_VISION":
							Add_User_From_Vision();
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



	/// Commands Management functions
	private void Add_User_From_Vision() 
	{
		try {
			String User_Name = in.readUTF();
			Check_User_Name_Vision insert = new Check_User_Name_Vision();
			sendMessage(insert.do_The_Work(URL, User_Name));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void SSS_Vision()
	{	
		try
		{
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
				ByteToFile2 = new BufferedOutputStream(new FileOutputStream(new File("data/SSS_VISION/"+title2+".jpg")));
				ByteToFile2.write(buffer);
				ByteToFile2.flush();
				ByteToFile2.close();
				
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
				
				String model_ID2 = models_string[models_string.length - 1].split(",")[0];
				Return_Users_In_Model users2 = new Return_Users_In_Model();
				String[] users_string2 = users2.Do_The_Work(URL,model_ID2);
				int num_Models = models_string.length;
				if(users_string2.length == 1 || Integer.parseInt(models_string[users_string2.length - 1].split(",")[3]) < 11)
				{
					num_Models = models_string.length - 1;
				}
				

				int[] results = new int[num_Models];
				List<Integer> fet_Match = new ArrayList<Integer>(num_Models);
				List<Double> model_Match_Accuracy = new ArrayList<Double>(num_Models);
				
				File image2 = null;
				
				for (int i = 0; i < num_Models; i++)
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
					image2 = new File("data/SSS_VISION/"+title2+".jpg");
					System.out.println(image2.getAbsolutePath());
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
						System.out.println("Ex" + status2);
						System.out.println("Ex" + exception2);
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
						fet_Match.add(i, max_num);
						double prediction_Accuracy = matEng.getVariable("predAccuracy");
						model_Match_Accuracy.add(i, prediction_Accuracy);
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
					int max = fet_Match.get(0);
					int index = 0;
					for(int i = 0; i < fet_Match.size(); i++)
					{
						if(fet_Match.get(i) > max)
						{
							max = fet_Match.get(i);
							index = i;
						}
					}
					Return_User_With_ID user = new Return_User_With_ID();										
					String result_With_Max = user.do_The_Work(URL, String.valueOf(results[index]));
					sendMessage(ServerUrl+title2+".jpg"+","+result_With_Max.split(",")[1]+","+model_Match_Accuracy.get(index));
					//sendMessage("http://smartphonesecuritysystem.dedicated.co.za:8080/Videos/harry.jpg");
					
				}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	private void Insert_Device_Coordinate()
	{
		try {
			String Device_Mac = in.readUTF();
			String Longitude = in.readUTF();
			String Latitude = in.readUTF();
			Insert_Device_Coordinate insert = new Insert_Device_Coordinate();
			sendMessage(insert.do_The_Work(URL, Device_Mac, Longitude, Latitude));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void Confirm_Number()
	{
		try {
			String number = in.readUTF();
			System.out.println(number);
			Confirm_Number confirm = new Confirm_Number();
			sendMessage(confirm.do_The_Work(URL,"Phone number confirmation sms from Smartphone Security System.","+15312331112", number));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void Check_User_Name()
	{
		try {
			String User_Name = in.readUTF();
			Check_User_Name check = new Check_User_Name();
			sendMessage(check.do_The_Work(URL, User_Name));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void Register_User() throws Exception
	{
		String User_Name = in.readUTF();
		String email = in.readUTF();
		String password = in.readUTF();
		String Device_Mac = in.readUTF();
		Insert_User clas = new Insert_User();						
		String response = clas.do_The_Work(URL,User_Name,email,password);
		System.out.println(response);
		sendMessage(response);
		if(response.equals("true"))
		{
			File dir = new File("data/MATLAB_TRAIN_DATA/" + User_Name);
			dir.mkdir();
			File dir2 = new File("data/MATLAB_TRAIN_DATA/" + User_Name+"/MATLAB_PRED_DATA");
			dir2.mkdir();
			
			Login login_Class = new Login();
			String[] user_Details = login_Class.do_The_Work(URL, User_Name, password).split(",");
			Insert_Accuracy_Users accu_Class = new Insert_Accuracy_Users();		
			System.out.println(accu_Class.do_The_Work(URL, user_Details[0], "0", "0", "0", "0"));
			
			Insert_Accuracy_Users_First_Version accu_Class_First_Version = new Insert_Accuracy_Users_First_Version();		
			System.out.println(accu_Class_First_Version.do_The_Work(URL, user_Details[0], "0", "0", "0", "0"));
			
			Insert_Device_Mac insert_mac = new Insert_Device_Mac();
			String insert_result = insert_mac.do_The_Work(URL, user_Details[0], Device_Mac,email);
			System.out.println(insert_result);
		}	

	}
	
	private void Login()
	{
		try {
			String User_Name_login = in.readUTF();
			String password_login = in.readUTF();
			
			Login login = new Login();
			String loginString = login.do_The_Work(URL,User_Name_login,password_login);
			System.out.println(loginString);
			sendMessage(loginString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void Check_Accuracy_For_Login()
	{
		try {
			String User_ID = in.readUTF();
			Return_Accuracy_Users acc = new Return_Accuracy_Users();
			String accString = acc.do_The_Work(URL, User_ID);
			sendMessage(accString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void Count_Images()
	{

		try {
			String user_ID_count = in.readUTF();
			Return_Images_For_Mobile images_class = new Return_Images_For_Mobile();
			String[] images = images_class.Do_The_Work(URL);
			int count = 0;
			if(images != null)
			{
				for(int i = 0; i < images.length; i++)
				{
					if(user_ID_count.equals(images[i].split(",")[2]))
					{
						count++;
					}
				}
			}

			sendMessage(String.valueOf(count));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void Count_VN()
	{

		try {
			String user_ID_count_VN = in.readUTF();
			Return_VNs_For_Mobile VNS_class = new Return_VNs_For_Mobile();
			String[] VNS = VNS_class.Do_The_Work(URL);
			int count_VN = 0;
			if(VNS != null)
			{
				for(int i = 0; i < VNS.length; i++)
				{
					if(user_ID_count_VN.equals(VNS[i].split(",")[2]))
					{
						count_VN++;
					}
				}
			}

			sendMessage(String.valueOf(count_VN));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void Insert_Image()
	{

		try {
			String user_ID_ = in.readUTF();
			System.out.println(user_ID_);
			String user_name = in.readUTF();
			System.out.println(user_name);
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
				Return_Images_For_Mobile images_class = new Return_Images_For_Mobile();
				String[] images = images_class.Do_The_Work(URL);
				int count = 0;
				if(images != null)
				{
					for(int i = 0; i < images.length; i++)
					{
						if(user_ID_.equals(images[i].split(",")[2]))
						{
							count++;
						}
					}
				}
				Return_User_With_ID user = new Return_User_With_ID();
				String[] user_Tokens = user.do_The_Work(URL, user_ID_).split(",");
				Return_Users_In_Model users = new Return_Users_In_Model();
				String[] users_Array = users.Do_The_Work(URL, user_Tokens[2]);
				if(count <= 10 && users_Array.length > 1)
				{
					Insert_Image_To_First_Version(user_ID_,user_name,title,model_Name_Tokens[0],model_Name_Tokens[1],users_Array);
				}
				sendMessage("Upload Successful");
				Insert_Image insert_class = new Insert_Image();
				console_Like.append(insert_class.do_The_Work(URL,user_ID_,image.getName(),model_Name_Tokens[2])+"\n");
				
			}
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	private void Insert_Image_To_First_Version(String user_ID_, String user_name,String title, String model_Name_Tokens0, String model_Name_Tokens1,String[] users_Array)
	{

		try {

			File image = new File("data/MATLAB_TRAIN_DATA/"+user_name+"/"+title);
			matEng.eval("image_Path = '"+ image.getAbsolutePath().toString()+"'",null,null);
			matEng.eval("user_ID = "+user_ID_,null,null);
			matEng.eval("image1 = imread(image_Path);",null,null);
			matEng.eval("I1 = image1;",null,null);		
			String Matlab_Path = new File(".").getCanonicalPath() + "/data";
			int model_Version =  11;		

		
			Return_Images_For_Mobile images_class = new Return_Images_For_Mobile();
			String[] images = images_class.Do_The_Work(URL);
			boolean found_Match = false;
			for(int j = 0; j < users_Array.length; j++)
			{
				int count = 0;
				if(images != null)
				{
					int index = 0;
					for(int i = 0; i < images.length; i++)
					{
						
						if(users_Array[(users_Array.length - 1) - j].split(",")[0].equals(images[i].split(",")[2]))
						{
							index = i;
							count++;
						}
					}
					if(count >= 10)
					{
						model_Version = Integer.parseInt(images[index].split(",")[3]);
						found_Match = true;
					}
				}
				if(found_Match == true)
				{
					break;
				}
				count = 0;
			}
			System.out.println(model_Version);
			
			String ML_features_Database = Matlab_Path + "/MATLAB_TRAIN_DATA/" + model_Name_Tokens0 +"_" + model_Name_Tokens1 + "_" + model_Version+ ".mat";
			String ML_features_Database2 = Matlab_Path + "/MATLAB_TRAIN_DATA/" + model_Name_Tokens0 +"_" + model_Name_Tokens1 + "_" + model_Version+ ".mat";						
			matEng.eval("path = '"+ML_features_Database+"'",null,null);
			matEng.eval("path2 = '"+ML_features_Database2+"'",null,null);
			matEng.eval("run('" + Matlab_Path + "/MATLAB_SCRIPTS/Update_Training_Data_For_First_Version.m')",null,null);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	private void Insert_VN()
	{
		try {
			String user_ID_VN = in.readUTF();
			String user_name_VN = in.readUTF();
			String size_VN = in.readUTF();
			String tittle_VN = in.readUTF();
			
			System.out.println(user_ID_VN);
			System.out.println(user_name_VN);
			System.out.println(size_VN);
			System.out.println(tittle_VN);
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

				ByteToFile_VN = new BufferedOutputStream(new FileOutputStream(new File("data/MATLAB_TRAIN_DATA/"+user_name_VN+"/"+tittle_VN)));
				ByteToFile_VN.write(buffer);
				ByteToFile_VN.flush();
				ByteToFile_VN.close();
				

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
			
			
			if(exception3 == 1)
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
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	
	private void Train_Images_Model()
	{

		try {
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
			
			Return_User_With_ID user = new Return_User_With_ID();
			String[] user_String = user.do_The_Work(URL, user_ID).split(",");
					
			double validation_accu = matEng.getVariable("accuracy");
			Return_Users_In_Model_VN users_In_Model = new Return_Users_In_Model_VN();
			String[] users_In_Model_String = users_In_Model.Do_The_Work(URL, user_String[2]);
			
			Return_Accuracy_Users accu_Class = new Return_Accuracy_Users();
			String[] accuString = accu_Class.do_The_Work(URL, users_In_Model_String[0].split(",")[0]).split(",");
			
			Update_Training_Accuracy_Users_New update_Class = new Update_Training_Accuracy_Users_New();
			update_Class.do_The_Work(URL, user_String[2],String.valueOf(validation_accu), accuString[5]);
			
			
			Return_Accuracy_Users_First_Version accu_Class_First_Version = new Return_Accuracy_Users_First_Version();
			String[] accuString_First_Version = accu_Class_First_Version.do_The_Work(URL, users_In_Model_String[0].split(",")[0]).split(",");

			Update_Training_Accuracy_Users_First_Version update_Class_First_Version = new Update_Training_Accuracy_Users_First_Version();
			update_Class_First_Version.do_The_Work(URL,user_String[2],String.valueOf(validation_accu), accuString_First_Version[5]);
			
			
			Return_Users_In_Model users_in_model = new Return_Users_In_Model();
			String[] users_String = users_in_model.Do_The_Work(URL, user_String[2]);
			
			for(int i = 0; i < users_String.length; i++)
			{
				Return_Accuracy_Users accu_Class_each = new Return_Accuracy_Users();
				String[] accuString_each = accu_Class_each.do_The_Work(URL,  users_String[i].split(",")[0]).split(",");
				Update_Accuracy_Users update_Class_pred = new Update_Accuracy_Users();
				update_Class_pred.do_The_Work(URL, users_String[i].split(",")[0],"0", accuString_each[3], accuString_each[4], accuString_each[5]);
				
				
				Return_Accuracy_Users_First_Version accu_Class_each_First_Version = new Return_Accuracy_Users_First_Version();
				String[] accuString_each_First_Version = accu_Class_each_First_Version.do_The_Work(URL,  users_String[i].split(",")[0]).split(",");
				Update_Accuracy_Users_First_Version update_Class_pred_First_Version = new Update_Accuracy_Users_First_Version();
				update_Class_pred_First_Version.do_The_Work(URL, users_String[i].split(",")[0],"0", accuString_each_First_Version[3], accuString_each_First_Version[4], accuString_each_First_Version[5]);
				
			}
			System.out.println("Training first model version....");
			Train_Images_Model_First_Version(user_ID);
			sendMessage("Training Model..");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void Train_Images_Model_First_Version(String user_ID)
	{

		try {

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
			Return_User_With_ID user = new Return_User_With_ID();
			String[] user_Tokens = user.do_The_Work(URL, user_ID).split(",");
			Return_Users_In_Model users = new Return_Users_In_Model();
			String[] users_Array = users.Do_The_Work(URL, user_Tokens[2]);
			Return_Images_For_Mobile images_class = new Return_Images_For_Mobile();
			String[] images = images_class.Do_The_Work(URL);
			boolean found_Match = false;
			int model_Version = 11;
			for(int j = 0; j < users_Array.length; j++)
			{
				int count = 0;
				if(images != null)
				{
					int index = 0;
					for(int i = 0; i < images.length; i++)
					{
						
						if(users_Array[(users_Array.length - 1) - j].split(",")[0].equals(images[i].split(",")[2]) && !users_Array[(users_Array.length - 1) - j].split(",")[0].equals(user_ID))
						{
							index = i;
							count++;
						}
					}
					if(count >= 10)
					{
						model_Version = Integer.parseInt(images[index].split(",")[3]);
						found_Match = true;
					}
				}
				if(found_Match == true)
				{
					break;
				}
				count = 0;
			}
			System.out.println(model_Version);
			String model_Name = response_Tokens[0].split("_")[0] + "_" + response_Tokens[0].split("_")[1] + "_" + model_Version;
			String ML_features_Database_train = Matlab_Path_train + "/MATLAB_TRAIN_DATA/" + model_Name + ".mat";			
			matEng.eval("path = '"+ML_features_Database_train+"'",null,null);
			String Trained_Model_File = Matlab_Path_train + "/MATLAB_TRAINED_MODELS/" + model_Name + ".mat";
			matEng.eval("path2 = '"+Trained_Model_File+"'",null,null);
			matEng.eval("run('" + Matlab_Path_train + "/MATLAB_SCRIPTS/RUN_ESS5.m')",null,null);
			
			
			sendMessage("Training Model..");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void Train_VN_Model()
	{
		try {
			String user_ID = in.readUTF();
			Return_User_With_ID user = new Return_User_With_ID();
			String[] user_String = user.do_The_Work(URL, user_ID).split(",");
			
			Return_Train_Models_VN models_class = new Return_Train_Models_VN();
			String[] models = models_class.Do_The_Work(URL);
			
			int model_Index = 0;
			for(int i = 0; i< models.length; i++)
			{
				if(Integer.parseInt(models[i].split(",")[0]) == Integer.parseInt(user_String[3]))
				{
					model_Index = i;
				}
			}
			
			String[] model_tokens = models[model_Index].split(",");
			
	   		Return_Users_In_Model_VN users_class = new Return_Users_In_Model_VN();
			String[] users = users_class.Do_The_Work(URL, model_tokens[0]);

		
			if (Integer.parseInt(model_tokens[2])  == 4)
			{
				matEng.eval("class_1 = "+Integer.parseInt(users[0].split(",")[0]),null,null);
				matEng.eval("class_2 = "+Integer.parseInt(users[1].split(",")[0]),null,null);
				matEng.eval("class_3 = "+Integer.parseInt(users[2].split(",")[0]),null,null);
				matEng.eval("class_4 = "+Integer.parseInt(users[3].split(",")[0]),null,null);

			}
			else if (Integer.parseInt(model_tokens[2]) == 3)
			{
				matEng.eval("class_1 = "+Integer.parseInt(users[0].split(",")[0]),null,null);
				matEng.eval("class_2 = "+Integer.parseInt(users[1].split(",")[0]),null,null);
				matEng.eval("class_3 = "+Integer.parseInt(users[2].split(",")[0]),null,null);
				matEng.eval("class_4 = "+Integer.parseInt(users[2].split(",")[0]+1),null,null);

			}
			else if (Integer.parseInt(model_tokens[2]) == 2)
			{
				matEng.eval("class_1 = "+Integer.parseInt(users[0].split(",")[0]),null,null);
				matEng.eval("class_2 = "+Integer.parseInt(users[1].split(",")[0]),null,null);
				matEng.eval("class_3 = "+Integer.parseInt(users[1].split(",")[0]+1),null,null);
				matEng.eval("class_4 = "+Integer.parseInt(users[1].split(",")[0]+2),null,null);

			}
			else
			{
				matEng.eval("class_1 = "+Integer.parseInt(users[0].split(",")[0]),null,null);
				matEng.eval("class_2 = "+Integer.parseInt(users[0].split(",")[0]+2),null,null);
				matEng.eval("class_3 = "+Integer.parseInt(users[0].split(",")[0]+3),null,null);
				matEng.eval("class_4 = "+Integer.parseInt(users[0].split(",")[0]+4),null,null);

			}

			
			String ML_features_Database_train = Matlab_Path_train + "/MATLAB_TRAIN_DATA/VOICE_NOTES_DATA/" + model_tokens[1] +  "_" + model_tokens[3] + ".mat";
			
			matEng.eval("path = '"+ML_features_Database_train+"'",null,null);
			
			
			String Trained_Model_File = Matlab_Path_train + "/MATLAB_TRAINED_MODELS/SOUND_PROCESSING/" + model_tokens[1] +  "_" + model_tokens[3]  + ".mat";

			matEng.eval("path2 = '"+Trained_Model_File+"'",null,null);
			
			matEng.eval("run('" + Matlab_Path_train + "/MATLAB_SCRIPTS/SOUND_PROCESSING/RUN_QD.m')",null,null);
			
			Return_Accuracy_Users accu_Class = new Return_Accuracy_Users();
			String[] accuString = accu_Class.do_The_Work(URL, user_ID).split(",");
			
			Update_Training_Accuracy_Users_VN update_Class = new Update_Training_Accuracy_Users_VN();
			double validation_accu = matEng.getVariable("accuracy");
			String checking = update_Class.do_The_Work(URL, user_String[3],accuString[3],String.valueOf(validation_accu));
			System.out.println(checking);
			
			Return_Users_In_Model_VN users_in_model = new Return_Users_In_Model_VN();
			String[] users_String = users_in_model.Do_The_Work(URL, user_String[3]);
			
			for(int i = 0; i < users_String.length; i++)
			{
				Return_Accuracy_Users accu_Class_each = new Return_Accuracy_Users();
				String[] accuString_each = accu_Class_each.do_The_Work(URL, users_String[i].split(",")[0]).split(",");
				Update_Accuracy_Users update_Class_pred = new Update_Accuracy_Users();
				update_Class_pred.do_The_Work(URL, users_String[i].split(",")[0],accuString_each[2], accuString_each[3], "0", accuString_each[5]);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	private void Pred_User_Image()
	{
		try {
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
			
			Return_Current_Num_Images num = new Return_Current_Num_Images();
			int current_num = Integer.parseInt( num.do_The_Work(URL).split(",")[1]);
			
			Return_Images_For_Mobile images = new Return_Images_For_Mobile();
			String[] images_Strings  = images.Do_The_Work(URL);
			
			int count_Images = 0;
			for(int i = 0; i < images_Strings.length; i++)
			{
				if(user_ID2_.equals(images_Strings[i].split(",")[2]))
				{
					count_Images++;
				}
			}
			int diff = current_num - count_Images;
			if(diff > 15)
			{
				Pred_User_Image_First_Version(user_ID2_,user_name2,size_string2,title2);
			}
			else
			{
				Return_Train_Models models = new Return_Train_Models();
				String[] models_string = models.Do_The_Work(URL);
				
				int num_Models = 1;
				if(models_string.length > 1)
				{
					String model_ID = models_string[models_string.length - 1].split(",")[0];
					Return_Users_In_Model users = new Return_Users_In_Model();
					String[] users_string = users.Do_The_Work(URL,model_ID);
					
					Return_User_With_ID current_user = new Return_User_With_ID();
					String current_user_String = current_user.do_The_Work(URL, user_ID2_);
					
					if(Integer.parseInt(current_user_String.split(",")[2]) == Integer.parseInt(models_string[models_string.length - 1].split(",")[0]))
					{
						num_Models = models_string.length;
					}
					else 
					{
						if(users_string.length > 2)
						{
							num_Models = models_string.length;
						}
						else
						{
							num_Models = models_string.length - 1;
						}
					}

				}


				int[] results = new int[num_Models];
				List<Integer> fet_Match = new ArrayList<Integer>(num_Models);
				List<Double> model_Match_Accuracy = new ArrayList<Double>(num_Models);
				
				File image2 = null;
				
				for (int i = 0; i < num_Models; i++)
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
					System.out.println(image2.getAbsolutePath());
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
						System.out.println("Ex" + status2);
						System.out.println("Ex" + exception2);
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
						fet_Match.add(i, max_num);
						double prediction_Accuracy = matEng.getVariable("predAccuracy");
						model_Match_Accuracy.add(i, prediction_Accuracy);
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
					//matEng.eval("imwrite(J,Image_Name_of)",null,null);					

					Return_Accuracy_Users accu_Class = new Return_Accuracy_Users();
					String[] accuString = accu_Class.do_The_Work(URL, user_ID2_).split(",");
					List<Integer> new_Fetch_Match = fet_Match;
					int max = cal_Max_With_Acc(fet_Match,model_Match_Accuracy,accuString);
					
					if(max != 0)
					{
						String result_With_Max = "";
						for (int i = 0; i < new_Fetch_Match.size(); i++)
						{
							if (new_Fetch_Match.get(i) == max)
							{						
								Return_User_With_ID user = new Return_User_With_ID();										
								result_With_Max = user.do_The_Work(URL, String.valueOf(results[i]));
							}
						}
		
						System.out.println(result_With_Max);
						String[] results_tokens = result_With_Max.split(",");
						sendMessage(results_tokens[1]);	
					}
					else
					{
						sendMessage("Incorrect User");	
					}

					
				}
			}
			

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void Pred_User_Image_First_Version(String user_ID2_,String user_name2,String size_string2,String title2)
	{
		try {
			
			Return_Train_Models models = new Return_Train_Models();
			String[] models_string = models.Do_The_Work(URL);
			for(int i = 0; i < models_string.length; i++)
			{
				Return_Users_In_Model users = new Return_Users_In_Model();
				String[] user_String = users.Do_The_Work(URL, models_string[i].split(",")[0]);
				
				Return_Images_For_Mobile images = new Return_Images_For_Mobile();
				String[] images_Strings = images.Do_The_Work(URL);
				int count_Images = 0;
				int index = 0; 
				for(int j = 0; j < images_Strings.length; j++)
				{
					if(user_String[user_String.length-1].split(",")[0].equals(images_Strings[j].split(",")[2]))
					{
						count_Images++;
						if(count_Images == 10)
						{
							index = j;
						}
					}
				}
				models_string[i] = models_string[i].split(",")[0] + "," + models_string[i].split(",")[1] + "," + models_string[i].split(",")[2] + "," + images_Strings[index].split(",")[3];
			}
			
			int num_Models = 1;
			if(models_string.length > 1)
			{
				String model_ID = models_string[models_string.length - 1].split(",")[0];
				Return_Users_In_Model users = new Return_Users_In_Model();
				String[] users_string = users.Do_The_Work(URL,model_ID);
				
				Return_User_With_ID current_user = new Return_User_With_ID();
				String current_user_String = current_user.do_The_Work(URL, user_ID2_);
				
				if(Integer.parseInt(current_user_String.split(",")[2]) == Integer.parseInt(models_string[models_string.length - 1].split(",")[0]))
				{
					num_Models = models_string.length;
				}
				else 
				{
					if(users_string.length > 2)
					{
						num_Models = models_string.length;
					}
					else
					{
						num_Models = models_string.length - 1;
					}
				}

			}


			int[] results = new int[num_Models];
			List<Integer> fet_Match = new ArrayList<Integer>(num_Models);
			List<Double> model_Match_Accuracy = new ArrayList<Double>(num_Models);
			
			File image2 = null;
			
			for (int i = 0; i < num_Models; i++)
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
				System.out.println(image2.getAbsolutePath());
				matEng.eval("Image_Name_of = '"+ image2.getAbsolutePath().toString()+"'",null,null);
				
				int model_version = Integer.parseInt(models_string[i].split(",")[3] );
				String Trained_Model2 = Matlab_Path_train + "/MATLAB_TRAINED_MODELS/" + models_string[i].split(",")[1] + "_" +model_version+ ".mat";
				File matFile = new File(Trained_Model2);
				
				while(!matFile.exists() && model_version > 0)
				{
					System.out.println(Trained_Model2);
					model_version++;
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
					System.out.println("Ex" + status2);
					System.out.println("Ex" + exception2);
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
					fet_Match.add(i, max_num);
					double prediction_Accuracy = matEng.getVariable("predAccuracy");
					model_Match_Accuracy.add(i, prediction_Accuracy);
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
				//matEng.eval("imwrite(J,Image_Name_of)",null,null);
				Return_Accuracy_Users_First_Version accu_Class = new Return_Accuracy_Users_First_Version();
				String[] accuString = accu_Class.do_The_Work(URL, user_ID2_).split(",");
				List<Integer> new_Fetch_Match = fet_Match;
				int max = cal_Max_With_Acc(fet_Match,model_Match_Accuracy,accuString);
				
				if(max != 0)
				{
					String result_With_Max = "";
					for (int i = 0; i < new_Fetch_Match.size(); i++)
					{
						if (new_Fetch_Match.get(i) == max)
						{						
							Return_User_With_ID user = new Return_User_With_ID();										
							result_With_Max = user.do_The_Work(URL, String.valueOf(results[i]));
						}
					}
	
					System.out.println(result_With_Max);
					String[] results_tokens = result_With_Max.split(",");
					sendMessage(results_tokens[1]);	
				}
				else
				{
					sendMessage("Incorrect User");	
				}

				
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	private void Pred_User_VN()
	{
		try {
			String user_ID2_ = in.readUTF();								//User ID
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

				ByteToFile4 = new BufferedOutputStream(new FileOutputStream(new File("data/MATLAB_TRAIN_DATA/"+user_name4+"/MATLAB_PRED_DATA/"+title4)));
				ByteToFile4.write(buffer);
				ByteToFile4.flush();
				ByteToFile4.close();
				
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

			int num_Models = 1;
			if(models_string2.length > 1)
			{
				String model_ID = models_string2[models_string2.length - 1].split(",")[0];
				Return_Users_In_Model_VN users = new Return_Users_In_Model_VN();
				String[] users_string = users.Do_The_Work(URL,model_ID);
				
				Return_User_With_ID current_user = new Return_User_With_ID();
				String current_user_String = current_user.do_The_Work(URL, user_ID2_);
				
				if(Integer.parseInt(current_user_String.split(",")[2]) == Integer.parseInt(models_string2[models_string2.length - 1].split(",")[0]))
				{
					num_Models = models_string2.length;
				}
				else 
				{
					if(users_string.length > 2)
					{
						num_Models = models_string2.length;
					}
					else
					{
						num_Models = models_string2.length - 1;
					}
				}

			}
			int[] results2 = new int[models_string2.length];
			List<Integer> fet_Match2 = new ArrayList<Integer>(models_string2.length);
			List<Double> model_Match_Accuracy = new ArrayList<Double>(models_string2.length);
			
			File audio2 = null;
			
			for (int i = 0; i < num_Models; i++)
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
					fet_Match2.add(i, max_num);
					double prediction_Accuracy = matEng.getVariable("predAccuracy");
					model_Match_Accuracy.add(i, prediction_Accuracy);
				}																							
			}									
			
			double exception4 = matEng.getVariable("exception");												
			if(exception4 == 1.0)
			{
				audio2.delete();
				sendMessage("Invalid Voice Note....");
			}
			else
			{
				Return_Accuracy_Users accu_Class = new Return_Accuracy_Users();
				String[] accuString = accu_Class.do_The_Work(URL, user_ID2_).split(",");
				List<Integer> new_Fetch_Match = fet_Match2;
				int max = cal_Max_With_Acc_VN(fet_Match2,model_Match_Accuracy,accuString);
				System.out.println(accuString[5]);
				if(max != 0)
				{
					String result_With_Max = "";
					for (int i = 0; i < new_Fetch_Match.size(); i++)
					{
						if (new_Fetch_Match.get(i) == max)
						{						
							Return_User_With_ID user = new Return_User_With_ID();										
							result_With_Max = user.do_The_Work(URL, String.valueOf(results2[i]));
						}
					}
	
					System.out.println(result_With_Max);
					String[] results_tokens = result_With_Max.split(",");
					sendMessage(results_tokens[1]);	
					
					double prediction_Accuracy = matEng.getVariable("predAccuracy");
					Update_Accuracy_Users update_Class = new Update_Accuracy_Users();
					update_Class.do_The_Work(URL, accuString[1],  accuString[2], accuString[3],String.valueOf(prediction_Accuracy), accuString[5]);
				}
				else
				{
					sendMessage("Incorrect User");	
				}

				
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void Test_Prediction_Accuracy_Images() throws Exception
	{

		String[] Variables_Array = new String[5];
		Variables_Array[0] = in.readUTF();         	// User ID
		Variables_Array[1] = in.readUTF();			// User Name
		Variables_Array[2] = in.readUTF();			// Size of Image
		Variables_Array[3] = in.readUTF();			// Image Title
		
		
		System.out.println(Variables_Array[2]);
		System.out.println(Variables_Array[3]);
		BufferedOutputStream ByteToFile = null;
		
		try
		{
			System.out.println(Integer.parseInt(Variables_Array[2]));
			
			byte[] buffer = new byte[Integer.parseInt(Variables_Array[2])];
			readFully(in,buffer,0,Integer.parseInt(Variables_Array[2]));
			
			// Clearing extra bytes
			int extra = in.available();
			if (extra > 0)
			{
				byte[] buffer2 = new byte[extra];
				in.read(buffer2);
			}
			File imageInsta = new File("data/MATLAB_TRAIN_DATA/"+Variables_Array[1]+"/MATLAB_PRED_DATA/"+Variables_Array[3]+".jpg");
			if(imageInsta.exists()){
				
				ByteToFile = new BufferedOutputStream(new FileOutputStream(new File("data/MATLAB_TRAIN_DATA/"+Variables_Array[1]+"/MATLAB_PRED_DATA/"+Variables_Array[3]+"(1).jpg")));
				ByteToFile.write(buffer);
				ByteToFile.flush();
				ByteToFile.close();
			}
			else
			{
				ByteToFile = new BufferedOutputStream(new FileOutputStream(new File("data/MATLAB_TRAIN_DATA/"+Variables_Array[1]+"/MATLAB_PRED_DATA/"+Variables_Array[3]+".jpg")));
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
		
		Return_Train_Models models = new Return_Train_Models();
		String[] models_string = models.Do_The_Work(URL);
		int num_Models = 1;
		if(models_string.length > 1)
		{
			String model_ID = models_string[models_string.length - 1].split(",")[0];
			Return_Users_In_Model users = new Return_Users_In_Model();
			String[] users_string = users.Do_The_Work(URL,model_ID);
			
			Return_User_With_ID current_user = new Return_User_With_ID();
			String current_user_String = current_user.do_The_Work(URL, Variables_Array[0]);
			
			if(Integer.parseInt(current_user_String.split(",")[2]) == Integer.parseInt(models_string[models_string.length - 1].split(",")[0]))
			{
				num_Models = models_string.length;
			}
			else 
			{
				if(users_string.length > 2)
				{
					num_Models = models_string.length;
				}
				else
				{
					num_Models = models_string.length - 1;
				}
			}

		}
		
		int[] results = new int[num_Models];
		List<Integer> fet_Match = new ArrayList<Integer>(num_Models);
		List<Double> model_Match_Accuracy = new ArrayList<Double>(num_Models);
		
		File image2 = null;
		
		for (int i = 0; i < num_Models; i++)
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
			image2 = new File("data/MATLAB_TRAIN_DATA/"+Variables_Array[1]+"/MATLAB_PRED_DATA/"+Variables_Array[3]+".jpg");
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
				fet_Match.add(i, max_num);
				double prediction_Accuracy = matEng.getVariable("predAccuracy");
				model_Match_Accuracy.add(i, prediction_Accuracy);
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
			
			matEng.eval("imwrite(J,Image_Name_of)",null,null);
			Return_Accuracy_Users accu_Class = new Return_Accuracy_Users();
			String[] accuString = accu_Class.do_The_Work(URL, Variables_Array[0]).split(",");
			List<Integer> new_Fetch_Match = fet_Match;
			List<Double> new_model_Match_Accuracy = model_Match_Accuracy;
			int max = cal_Max_With_Acc_Testing(fet_Match,model_Match_Accuracy,accuString);
			System.out.println(max + "  Max here");
			if(max != 0)
			{
				String result_With_Max = "";
				for (int i = 0; i < new_Fetch_Match.size(); i++)
				{
					if (new_Fetch_Match.get(i) == max)
					{						
						Return_User_With_ID user = new Return_User_With_ID();										
						result_With_Max = user.do_The_Work(URL, String.valueOf(results[i]));
					}
				}
				System.out.println(result_With_Max);	
				
				String[] results_tokens = result_With_Max.split(",");
				if(Variables_Array[0].equals(results_tokens[0]))
				{
							
					Update_Accuracy_Users update_Class = new Update_Accuracy_Users();
					String update_String = update_Class.do_The_Work(URL, accuString[1], String.valueOf(new_model_Match_Accuracy.get(0)), accuString[3], accuString[4], accuString[5]);
					
					System.out.println(update_String);
					sendMessage(results_tokens[1] + " - " + String.valueOf(new_model_Match_Accuracy.get(0)) + "%");

				}
				else
				{
					sendMessage("Incorrect User");
				}	
			}
			else
			{
				sendMessage("Incorrect User");	
			}
			
			/*
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
			

			if(Variables_Array[0].equals(results_tokens[0]))
			{
				double prediction_Accuracy = matEng.getVariable("predAccuracy");
				sendMessage(results_tokens[1] + " - " + String.valueOf(prediction_Accuracy) + "%");
			
				Return_Accuracy_Users accu_Class = new Return_Accuracy_Users();
				String[] accuString = accu_Class.do_The_Work(URL, Variables_Array[0]).split(",");				
				Update_Accuracy_Users update_Class = new Update_Accuracy_Users();
				String update_String = update_Class.do_The_Work(URL, accuString[1], String.valueOf(prediction_Accuracy), accuString[3], accuString[4], accuString[5]);
				
				System.out.println(update_String);
			}
			else
			{
				sendMessage("Incorrect User");
			}
			*/
		}
	}
	

	
	
	
	// Other functions
	private final int readFully(DataInputStream in, byte b[], int off, int len) throws IOException {
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
	
	private int cal_Max_With_Acc(List<Integer> fetch_Match,List<Double> Model_Accuracy,String[] accuString)
	{

		if(fetch_Match.size() == 1)
		{
			double prediction_Accuracy = Model_Accuracy.get(0);
			double currentAccuracy;
			if(Integer.parseInt(accuString[2]) != 0)
			{
				currentAccuracy = Double.parseDouble(accuString[2]);
				System.out.println(currentAccuracy);
			}
			else
			{
				currentAccuracy = Double.parseDouble(accuString[3]);
				System.out.println(currentAccuracy);
			}
			if(currentAccuracy > prediction_Accuracy)
			{
				double error = currentAccuracy - prediction_Accuracy;
				if(error < 8)
				{
					return fetch_Match.get(0);
				}
				else
				{
					return 0;
				}
			}
			else
			{
				double error = prediction_Accuracy - currentAccuracy;
				System.out.println(prediction_Accuracy);
				if(error < 8)
				{
					return fetch_Match.get(0);
				}
				else
				{
					return 0;
				}
			}
			
		}
		
		if(fetch_Match.size() > 1)
		{
			int max = fetch_Match.get(0);
			for(int i = 0; i < fetch_Match.size(); i++)
			{
				if(fetch_Match.get(i) > max)
				{
					max = fetch_Match.get(i);
				}
			}
			for(int i = 0; i < fetch_Match.size(); i++)
			{
				if(fetch_Match.get(i) == max)
				{
					double prediction_Accuracy = Model_Accuracy.get(i);
					double currentAccuracy;
					if(Integer.parseInt(accuString[2]) != 0)
					{
						currentAccuracy = Double.parseDouble(accuString[2]);
						System.out.println(currentAccuracy);
					}
					else
					{
						currentAccuracy = Double.parseDouble(accuString[3]);
						System.out.println(currentAccuracy);
					}
					if(currentAccuracy > prediction_Accuracy)
					{
						double error = currentAccuracy - prediction_Accuracy;
						if(error < 8)
						{
							return fetch_Match.get(i);
						}
						else
						{
							fetch_Match.remove(i);
							Model_Accuracy.remove(i);
							return cal_Max_With_Acc(fetch_Match,Model_Accuracy,accuString);
						}
					}
					else
					{
						double error = prediction_Accuracy - currentAccuracy;
						System.out.println(prediction_Accuracy);
						if(error < 8)
						{
							return fetch_Match.get(i);
						}
						else
						{
							fetch_Match.remove(i);
							Model_Accuracy.remove(i);
							return cal_Max_With_Acc(fetch_Match,Model_Accuracy,accuString);
						}
					}
				}

			}
		}
		return 0;		
	}
	
	private int cal_Max_With_Acc_VN(List<Integer> fetch_Match,List<Double> Model_Accuracy,String[] accuString)
	{

		if(fetch_Match.size() == 1)
		{
			double prediction_Accuracy = Model_Accuracy.get(0);
			double currentAccuracy;
			if(Integer.parseInt(accuString[4]) != 0)
			{
				currentAccuracy = Double.parseDouble(accuString[4]);
				System.out.println(currentAccuracy);
			}
			else
			{
				currentAccuracy = Double.parseDouble(accuString[5]);
				System.out.println(currentAccuracy);
			}
			if(currentAccuracy > prediction_Accuracy)
			{
				double error = currentAccuracy - prediction_Accuracy;
				if(error < 8)
				{
					return fetch_Match.get(0);
				}
				else
				{
					return 0;
				}
			}
			else
			{
				double error = prediction_Accuracy - currentAccuracy;
				System.out.println(prediction_Accuracy);
				if(error < 8)
				{
					return fetch_Match.get(0);
				}
				else
				{
					return 0;
				}
			}
			
		}
		
		if(fetch_Match.size() > 1)
		{
			int max = fetch_Match.get(0);
			for(int i = 0; i < fetch_Match.size(); i++)
			{
				if(fetch_Match.get(i) > max)
				{
					max = fetch_Match.get(i);
				}
			}
			for(int i = 0; i < fetch_Match.size(); i++)
			{
				if(fetch_Match.get(i) == max)
				{
					double prediction_Accuracy = Model_Accuracy.get(i);
					double currentAccuracy;
					if(Integer.parseInt(accuString[4]) != 0)
					{
						currentAccuracy = Double.parseDouble(accuString[4]);
						System.out.println(currentAccuracy);
					}
					else
					{
						currentAccuracy = Double.parseDouble(accuString[5]);
						System.out.println(currentAccuracy);
					}
					if(currentAccuracy > prediction_Accuracy)
					{
						double error = currentAccuracy - prediction_Accuracy;
						if(error < 8)
						{
							return fetch_Match.get(i);
						}
						else
						{
							fetch_Match.remove(i);
							Model_Accuracy.remove(i);
							return cal_Max_With_Acc(fetch_Match,Model_Accuracy,accuString);
						}
					}
					else
					{
						double error = prediction_Accuracy - currentAccuracy;
						System.out.println(prediction_Accuracy);
						if(error < 8)
						{
							return fetch_Match.get(i);
						}
						else
						{
							fetch_Match.remove(i);
							Model_Accuracy.remove(i);
							return cal_Max_With_Acc(fetch_Match,Model_Accuracy,accuString);
						}
					}
				}

			}
		}
		return 0;		
	}
	
	private int cal_Max_With_Acc_Testing(List<Integer> fetch_Match,List<Double> Model_Accuracy,String[] accuString)
	{

		if(fetch_Match.size() == 1)
		{
			double prediction_Accuracy = Model_Accuracy.get(0);
			double currentAccuracy = Double.parseDouble(accuString[3]);

			if(currentAccuracy > prediction_Accuracy)
			{
				double error = currentAccuracy - prediction_Accuracy;
				if(error < 15)
				{
					return fetch_Match.get(0);
				}
				else
				{
					return 0;
				}
			}
			else
			{
				double error = prediction_Accuracy - currentAccuracy;
				System.out.println(prediction_Accuracy);
				if(error < 15)
				{
					return fetch_Match.get(0);
				}
				else
				{
					return 0;
				}
			}
			
		}
		
		if(fetch_Match.size() > 1)
		{
			int max = fetch_Match.get(0);
			for(int i = 0; i < fetch_Match.size(); i++)
			{
				if(fetch_Match.get(i) > max)
				{
					max = fetch_Match.get(i);
				}
			}
			for(int i = 0; i < fetch_Match.size(); i++)
			{
				if(fetch_Match.get(i) == max)
				{
					double prediction_Accuracy = Model_Accuracy.get(i);
					double currentAccuracy = Double.parseDouble(accuString[3]);
					if(currentAccuracy > prediction_Accuracy)
					{
						double error = currentAccuracy - prediction_Accuracy;
						if(error < 15)
						{
							return fetch_Match.get(i);
						}
						else
						{
							fetch_Match.remove(i);
							Model_Accuracy.remove(i);
							System.out.println(currentAccuracy + "This one");
							return cal_Max_With_Acc(fetch_Match,Model_Accuracy,accuString);
						}
					}
					else
					{
						double error = prediction_Accuracy - currentAccuracy;
						System.out.println(prediction_Accuracy);
						if(error < 15)
						{
							return fetch_Match.get(i);
						}
						else
						{
							fetch_Match.remove(i);
							Model_Accuracy.remove(i);
							System.out.println(currentAccuracy + "This one1");
							return cal_Max_With_Acc(fetch_Match,Model_Accuracy,accuString);
						}
					}
				}

			}
		}
		return 0;
		
	}
	private void Get_Device_Mac()
	{
		try 
		{
			String Device_Mac = in.readUTF();
			Get_Device_Mac_Data_With_Mac mac = new Get_Device_Mac_Data_With_Mac();
			String mac_Data_String = mac.do_The_Work(URL, Device_Mac);
			sendMessage(mac_Data_String);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void Get_Current_Num_Images()
	{
		try
		{
			Return_Current_Num_Images num = new Return_Current_Num_Images();
			sendMessage(num.do_The_Work(URL).split(",")[1]);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
