package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.mathworks.engine.MatlabEngine;

import SSS_SERVER_CLASSES_FOR_MOBILE.MATLAB_Instances;
import SSS_SERVER_CLASSES_FOR_MOBILE.Server_Class;
import SSS_SERVER_FUNCTIONS.Insert_Num_Images;
import SSS_SERVER_FUNCTIONS.Return_Accuracy_Users;
import SSS_SERVER_FUNCTIONS.Return_Accuracy_Users_First_Version;
import SSS_SERVER_FUNCTIONS.Return_Current_Num_Images;
import SSS_SERVER_FUNCTIONS.Return_Train_Models;
import SSS_SERVER_FUNCTIONS.Return_Train_Models_VN;
import SSS_SERVER_FUNCTIONS.Return_User_With_ID;
import SSS_SERVER_FUNCTIONS.Return_Users_In_Model;
import SSS_SERVER_FUNCTIONS.Return_Users_In_Model_VN;
import SSS_SERVER_FUNCTIONS.Train_Images_Model;
import SSS_SERVER_FUNCTIONS.Update_Accuracy_Users;
import SSS_SERVER_FUNCTIONS.Update_Accuracy_Users_First_Version;
import SSS_SERVER_FUNCTIONS.Update_Num_Images;
import SSS_SERVER_FUNCTIONS.Update_Training_Accuracy_Users_First_Version;
import SSS_SERVER_FUNCTIONS.Update_Training_Accuracy_Users_New;

public class Jframe extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextArea console_Like;
	private String Matlab_Path_train;
	private String URL;
	private ArrayList<MATLAB_Instances> matlab_Instances;
	public Jframe(String URL)
	{
		this.URL = URL;
		try 
		{
			Matlab_Path_train  = new File(".").getCanonicalPath() + "/data";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setLayout(new GridLayout(1,1));
		console_Like = new TextArea();
		console_Like.setEditable(false);
		add(console_Like);

		//Start Server Thread
		new Thread(new Runnable() {
            @Override
            public void run() {
                try {
            		try 
            		{
            			matlab_Instances = new ArrayList<MATLAB_Instances>();
            			for(int i = 0; i< 10; i++)
            			{
                			MATLAB_Instances instance = new MATLAB_Instances();
                			instance.setConsole_Like(console_Like);
                			instance.setEngine_In_Use(false);
                			instance.setEngine_Ready(false);
                			instance.setEngine_ID(i);
                			instance.Start_Engine();
                			matlab_Instances.add(instance);
            			}
            			boolean instances_Ready = false;
            			while(instances_Ready == false)
            			{
            				boolean eng_running = true;
            				for(int i = 0; i < matlab_Instances.size(); i++)
            				{
            					if(matlab_Instances.get(i).isEngine_Ready() == false)
            					{
            						eng_running = false;
            					}
            				}
            				if(eng_running == true)
            				{
            					instances_Ready = true;
            				}
            			}
            			new Server_Class(8060,matlab_Instances,URL,console_Like);
            		} catch (Exception e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
                }
                catch (Exception ex)
                {
                }
            }
        }).start();
        
        //Update Daily Images Thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
            		try 
            		{
            			Insert_Num_Images insert_Num = new Insert_Num_Images();
            			String insert_String = insert_Num.do_The_Work(URL, "10");
            			System.out.println(insert_String);
            			while(true)
            			{
            				Return_Current_Num_Images num = new Return_Current_Num_Images();
            				System.out.println(num.do_The_Work(URL));
            				
                			System.out.println("Upload data");
                			int update_time = 24*60*60*1000;
                			Thread.sleep(update_time);
                			Update_Num_Images update_Num = new Update_Num_Images();
                			String update_String = update_Num.do_The_Work(URL, "3");
                			System.out.println(update_String);
            			}

            		} catch (Exception e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
                }
                catch (Exception ex)
                {
                }
            }
        }).start();
        
        //Images Training Thread
        new Thread(new Runnable() {
        	@Override
        	public void run()
        	{
    			while(true)
    			{
            		try
            		{
        				Thread.sleep(1*24*60*60*1000);
        				Return_Train_Models models = new Return_Train_Models();
        				String[] models_string = models.Do_The_Work(URL);
        				
        				for(int j = 0; j < models_string.length; j++)
        				{
                			boolean instances_Ready = false;
                			while(instances_Ready == false)
                			{
                				boolean eng_running = true;
                				for(int i = 0; i < matlab_Instances.size(); i++)
                				{
                					if(matlab_Instances.get(i).isEngine_Ready() == false)
                					{
                						eng_running = false;
                					}
                				}
                				if(eng_running == true)
                				{
                					instances_Ready = true;
                				}
                			}
            				boolean avail_Eng_Found = false;
            				int engi_ID = 0;
            				while(avail_Eng_Found == false)
            				{
            					for(int i = 0; i < matlab_Instances.size(); i++)
            					{
            						if(matlab_Instances.get(i).isEngine_In_Use() == false)
            						{
            							matlab_Instances.get(i).setEngine_In_Use(true);
            							engi_ID = i;
            							avail_Eng_Found = true;
            							break;
            						}
            					}
            				} 		

            				String model_ID = models_string[j].split(",")[0];
            				MatlabEngine matEng = matlab_Instances.get(engi_ID).getMatEng();
            				Train_Images_Model(model_ID,matEng,engi_ID);
        				}

            			
            		}
            		catch(Exception ex)
            		{
            			ex.printStackTrace();
            		}
    			}
        	}
        }).start();
	}
	
	
	private void Train_Images_Model(String model_ID,MatlabEngine matEng,int engi_ID)
	{
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				try 
				{
					Return_Users_In_Model users_in_model = new Return_Users_In_Model();
					String[] users_String = users_in_model.Do_The_Work(URL,model_ID);
					String user_ID = users_String[0].split(",")[0];
    				console_Like.append("Training Model " + model_ID + "...." + "\n");
				
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
					long startTime = System.currentTimeMillis();
					matEng.eval("run('" + Matlab_Path_train + "/MATLAB_SCRIPTS/RUN_ESS5.m')",null,null);
					long endTime = System.currentTimeMillis();

					
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
    				matlab_Instances.get(engi_ID).setEngine_In_Use(false);
    				console_Like.append("Model " + model_ID + " Finished Training with validation accuracy of "+ validation_accu + "% and took "+ ((endTime - startTime)/1000) + " Seconds....\n");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
