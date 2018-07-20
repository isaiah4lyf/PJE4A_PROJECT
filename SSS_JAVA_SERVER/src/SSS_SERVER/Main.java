package SSS_SERVER;

import java.io.File;

import com.mathworks.engine.MatlabEngine;
import SSS_SERVER_CLASSES_FOR_MOBILE.Server_Class;
import SSS_SERVER_FUNCTIONS.Return_Train_Models;

public class Main {
	private  static MatlabEngine matEng = null;
	private static String URL = "http://10.254.116.132:8080/SSS_SERVICE.asmx";
	public static void main(String[] args) {		
		try 
		{
			matEng = MatlabEngine.startMatlab();

			/*
			File image = new File("IMG_20180720_120923.jpg");
			matEng.eval("file = imread('"+image.getAbsolutePath().toString()+"');",null,null);
			
			matEng.eval("image1 = imrotate(file,90);",null,null);
			matEng.eval("load('webcamsSceneReconstruction.mat');",null,null);
			matEng.eval("image = undistortImage(image1,stereoParams.CameraParameters1);",null,null);
			matEng.eval("faceDetector = vision.CascadeObjectDetector;",null,null);
			matEng.eval("face1 = step(faceDetector,image);",null,null);
			matEng.eval("I = imcrop(image1,face1);",null,null);
			matEng.eval("im=rgb2gray(I);",null,null);
			matEng.eval("J = imresize(im, 5);",null,null);
			matEng.eval("ptsOriginal = detectSURFFeatures(J);",null,null);
			matEng.eval("imwrite(J,'"+image.getAbsolutePath().toString()+"');",null,null);
			matEng.eval("imshow(J); hold on;",null,null);
			matEng.eval("plot(ptsOriginal.selectStrongest(100));",null,null);
			*/
			
			new Server_Class(80,matEng,URL);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
