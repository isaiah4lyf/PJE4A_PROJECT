package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.mathworks.engine.MatlabEngine;
import SSS_SERVER_CLASSES_FOR_MOBILE.Server_Class;
import SSS_SERVER_FUNCTIONS.Return_Train_Models;
import SSS_SERVER_FUNCTIONS.Return_Users_In_Model;
import SSS_SERVER_FUNCTIONS.Train_Images_Model;

public class Jframe extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  static MatlabEngine matEng = null;
	private TextArea console_Like;
	private String Matlab_Path_train;
	public Jframe(String URL)
	{
		
		try 
		{
			Matlab_Path_train  = new File(".").getCanonicalPath() + "/data";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setLayout(new GridLayout(2,1));
		console_Like = new TextArea();
		console_Like.setEditable(false);
		JButton train_all_models = new JButton("Train All Models");
		train_all_models.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                    		try 
                    		{
                            	try
                            	{
                            		Return_Train_Models models_class = new Return_Train_Models();
                            		String[] models = models_class.Do_The_Work(URL);
                            		StringWriter console = new StringWriter();
                            		console_Like.append("\n");
                            		console_Like.append("Training All Models......... \n");
                            		console_Like.append("\n");
                            		
                            		for(int i = 0; i < models.length; i++)
                            		{
                            			console_Like.append("Training Model " + i+1 + "........ \n");
                                   		String[] model_tokens = models[i].split(",");
                                		Return_Users_In_Model users_class = new Return_Users_In_Model();
                                		String[] users = users_class.Do_The_Work(URL, model_tokens[0]);

                                	
                        				if (Integer.parseInt(model_tokens[2])  == 4)
                        				{
                        					matEng.eval("class_1 = "+Integer.parseInt(users[0].split(",")[0]),console,null);
                        					matEng.eval("class_2 = "+Integer.parseInt(users[1].split(",")[0]),console,null);
                        					matEng.eval("class_3 = "+Integer.parseInt(users[2].split(",")[0]),console,null);
                        					matEng.eval("class_4 = "+Integer.parseInt(users[3].split(",")[0]),console,null);

                        				}
                        				else if (Integer.parseInt(model_tokens[2]) == 3)
                        				{
                        					matEng.eval("class_1 = "+Integer.parseInt(users[0].split(",")[0]),console,null);
                        					matEng.eval("class_2 = "+Integer.parseInt(users[1].split(",")[0]),console,null);
                        					matEng.eval("class_3 = "+Integer.parseInt(users[2].split(",")[0]),null,null);
                        					matEng.eval("class_4 = "+Integer.parseInt(users[2].split(",")[0]+1),null,null);

                        				}
                        				else if (Integer.parseInt(model_tokens[2]) == 2)
                        				{
                        					matEng.eval("class_1 = "+Integer.parseInt(users[0].split(",")[0]),console,null);
                        					matEng.eval("class_2 = "+Integer.parseInt(users[1].split(",")[0]),console,null);
                        					matEng.eval("class_3 = "+Integer.parseInt(users[1].split(",")[0]+1),console,null);
                        					matEng.eval("class_4 = "+Integer.parseInt(users[1].split(",")[0]+2),console,null);

                        				}
                        				else
                        				{
                        					matEng.eval("class_1 = "+Integer.parseInt(users[0].split(",")[0]),console,null);
                        					matEng.eval("class_2 = "+Integer.parseInt(users[0].split(",")[0]+2),console,null);
                        					matEng.eval("class_3 = "+Integer.parseInt(users[0].split(",")[0]+3),console,null);
                        					matEng.eval("class_4 = "+Integer.parseInt(users[0].split(",")[0]+4),console,null);

                        				}

                        				
                        				String ML_features_Database_train = Matlab_Path_train + "/MATLAB_TRAIN_DATA/" + model_tokens[1] +  "_" + model_tokens[3] + ".mat";
                        				
                        				matEng.eval("path = '"+ML_features_Database_train+"'",console,null);
                        				
                        				
                        				String Trained_Model_File = Matlab_Path_train + "/MATLAB_TRAINED_MODELS/" + model_tokens[1] +  "_" + model_tokens[3]  + ".mat";

                        				matEng.eval("path2 = '"+Trained_Model_File+"'",console,null);
                        				
                        				matEng.eval("run('" + Matlab_Path_train + "/MATLAB_SCRIPTS/RUN_ESS5.m')",console,null);
                        				console_Like.append(console.toString());
                            		}
                     
                    				
                            	}
                            	catch(Exception ex)
                            	{
                            		console_Like.append(ex.toString());
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
                });
                thread.start();


            }
        });
		JButton train_specific_model = new JButton("Train Specific Model");
		train_specific_model.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        		
            }
        });
		JComboBox models = new JComboBox();
		JPanel menu_Panel = new JPanel();
		menu_Panel.setLayout(new GridLayout(3,1));
		menu_Panel.add(train_all_models);
		menu_Panel.add(models);
		menu_Panel.add(train_specific_model);

		
		add(console_Like);
		add(menu_Panel);
		
		
		
		
		
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
            		try 
            		{
            			matEng = MatlabEngine.startMatlab();
            			new Server_Class(80,matEng,URL,console_Like);
            		} catch (Exception e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
                }
                catch (Exception ex)
                {
                }
            }
        });
        thread.start();

	}

}
