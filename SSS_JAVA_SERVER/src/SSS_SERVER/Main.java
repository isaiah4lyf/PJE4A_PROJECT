package SSS_SERVER;


import com.mathworks.engine.MatlabEngine;
import SSS_SERVER_CLASSES_FOR_MOBILE.Server_Class;


public class Main {
	private  static MatlabEngine matEng = null;
	private static String URL = "http://192.168.43.175:8080/SSS_SERVICE.asmx";
	public static void main(String[] args) {		
		try 
		{
			matEng = MatlabEngine.startMatlab();
			new Server_Class(80,matEng,URL);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
