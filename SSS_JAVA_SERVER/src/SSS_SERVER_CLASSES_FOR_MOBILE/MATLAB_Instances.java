package SSS_SERVER_CLASSES_FOR_MOBILE;

import java.awt.TextArea;

import com.mathworks.engine.MatlabEngine;

public class MATLAB_Instances {
	private static  MatlabEngine matEng;
	private TextArea console_Like;
	private int Engine_ID;
	private boolean Engine_In_Use;
	private boolean Engine_Ready;
	public MATLAB_Instances(){}
	public void Start_Engine()
	{
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
            		try 
            		{
            			matEng = MatlabEngine.startMatlab();
            			console_Like.append("Matlab Engine " + Engine_ID + " initialized. \n");
            			Engine_Ready = true;
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
	public boolean isEngine_Ready() {
		return Engine_Ready;
	}
	public void setEngine_Ready(boolean engine_Ready) {
		Engine_Ready = engine_Ready;
	}
	public  MatlabEngine getMatEng() {
		return matEng;}
	public  void setMatEng(MatlabEngine matEng) {
		MATLAB_Instances.matEng = matEng;
	}
	public TextArea getConsole_Like() {
		return console_Like;
	}
	public void setConsole_Like(TextArea console_Like) {
		this.console_Like = console_Like;
	}
	public int getEngine_ID() {
		return Engine_ID;
	}
	public void setEngine_ID(int engine_ID) {
		Engine_ID = engine_ID;
	}
	public boolean isEngine_In_Use() {
		return Engine_In_Use;
	}
	public void setEngine_In_Use(boolean engine_In_Use) {
		Engine_In_Use = engine_In_Use;
	}
}
