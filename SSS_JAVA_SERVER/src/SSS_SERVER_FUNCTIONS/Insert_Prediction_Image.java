package SSS_SERVER_FUNCTIONS;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Insert_Prediction_Image{
	public Insert_Prediction_Image()
	{
	}
	public String do_The_Work(String URL,String Image_Path,String Prediction_Correct,String User_ID)
	{
		 String address = "";
	        try {
	            final String NAMESPACE = "http://tempuri.org/";
	           
	            final String SOAP_ACTION = "http://tempuri.org/INSERT_PREDICTION_IMAGE";
	            final String METHOD_NAME = "INSERT_PREDICTION_IMAGE";
	            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	            request.addProperty("Image_Path",Image_Path);
	            request.addProperty("Prediction_Correct",Prediction_Correct);
	            request.addProperty("User_ID",User_ID);
	            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	            envelope.dotNet = true;
	            envelope.setOutputSoapObject(request);
	            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	            androidHttpTransport.call(SOAP_ACTION, envelope);
	            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
	            address = response.toString();


	        } catch (Exception e) {
	            address = e.getLocalizedMessage();
	        }
	        
	        return address;
	}
}