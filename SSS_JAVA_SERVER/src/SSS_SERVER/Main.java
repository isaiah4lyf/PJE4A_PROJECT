package SSS_SERVER;

import java.io.StringWriter;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;

import SSS_SERVER_CLASSES_FOR_MOBILE.Server_Class;
import SSS_SERVER_CLASSES_FOR_WEB_SERVICE.Send_Packets_UDP;
import SSS_SERVER_CLASSES_FOR_WEB_SERVICE.Server_Class_Web;

public class Main {
	private  static MatlabEngine matEng = null;
	private static String URL;
	public static void main(String[] args) {
		
		try {
			matEng = MatlabEngine.startMatlab();
			
			StringWriter output = new StringWriter();
		    Thread thread = new Thread(new Runnable() {

	            @Override
	            public void run() {


	                try {
	                	Server_Class server = new Server_Class(8080,"data/server",matEng,URL);
	        			

	                }
	                catch (Exception ex)
	                {
	                    
	                }

	            }
	        });
	        thread.start();
		    Thread thread_web = new Thread(new Runnable() {

	            @Override
	            public void run() {


	                try {
	        			//Server_Class server_web = new Server_Class(4343,"data/server",matEng);
	                	//Send_Packets_UDP up = new Send_Packets_UDP();
	                }
	                catch (Exception ex)
	                {
	                    
	                }

	            }
	        });
		    thread_web.start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
}
