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
                            		StringWriter console = new StringWriter();
                            		matEng.eval("class_1 = "+Integer.parseInt("3"),console,null);
                    				String user_ID = "103";
                    				Train_Images_Model train_model = new Train_Images_Model();
                    				String[] response_Tokens = train_model.do_The_Work(URL, user_ID).split(",");
                    				System.out.println(response_Tokens[0]);
                    				if (response_Tokens.length-1  == 4)
                    				{
                    					matEng.eval("class_1 = "+Integer.parseInt(response_Tokens[1]),console,null);
                    					matEng.eval("class_2 = "+Integer.parseInt(response_Tokens[2]),console,null);
                    					matEng.eval("class_3 = "+Integer.parseInt(response_Tokens[3]),console,null);
                    					matEng.eval("class_4 = "+Integer.parseInt(response_Tokens[4]),console,null);

                    				}
                    				else if (response_Tokens.length-1 == 3)
                    				{
                    					matEng.eval("class_1 = "+Integer.parseInt(response_Tokens[1]),console,null);
                    					matEng.eval("class_2 = "+Integer.parseInt(response_Tokens[2]),console,null);
                    					matEng.eval("class_3 = "+Integer.parseInt(response_Tokens[3]),null,null);
                    					matEng.eval("class_4 = "+Integer.parseInt(response_Tokens[3]+1),null,null);

                    				}
                    				else if (response_Tokens.length-1 == 2)
                    				{
                    					matEng.eval("class_1 = "+Integer.parseInt(response_Tokens[1]),console,null);
                    					matEng.eval("class_2 = "+Integer.parseInt(response_Tokens[2]),console,null);
                    					matEng.eval("class_3 = "+Integer.parseInt(response_Tokens[2]+1),console,null);
                    					matEng.eval("class_4 = "+Integer.parseInt(response_Tokens[2]+2),console,null);

                    				}
                    				else
                    				{
                    					matEng.eval("class_1 = "+Integer.parseInt(response_Tokens[1]),console,null);
                    					matEng.eval("class_2 = "+Integer.parseInt(response_Tokens[1]+2),console,null);
                    					matEng.eval("class_3 = "+Integer.parseInt(response_Tokens[1]+3),console,null);
                    					matEng.eval("class_4 = "+Integer.parseInt(response_Tokens[1]+4),console,null);

                    				}

                    				
                    				String ML_features_Database_train = Matlab_Path_train + "/MATLAB_TRAIN_DATA/" + response_Tokens[0] + ".mat";
                    				
                    				matEng.eval("path = '"+ML_features_Database_train+"'",console,null);
                    				
                    				
                    				String Trained_Model_File = Matlab_Path_train + "/MATLAB_TRAINED_MODELS/" + response_Tokens[0] + ".mat";

                    				matEng.eval("path2 = '"+Trained_Model_File+"'",console,null);
                    				console_Like.append("Training All Models......... \n");
                    				matEng.eval("run('" + Matlab_Path_train + "/MATLAB_SCRIPTS/RUN_ESS5.m')",console,null);
                    				console_Like.append(console.toString());
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
