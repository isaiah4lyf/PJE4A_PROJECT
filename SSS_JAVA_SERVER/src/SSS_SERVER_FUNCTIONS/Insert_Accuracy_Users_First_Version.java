package SSS_SERVER_FUNCTIONS;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Insert_Accuracy_Users_First_Version {
	public Insert_Accuracy_Users_First_Version()
	{
	}
	public String do_The_Work(String URL,String User_ID, String Prediction_Accuracy_Images, String Validation_Accuracy_Images, String Prediction_Accuracy_VN, String Validation_Accuracy_VN)
	{
		 String address = "";
	        try 
	        {
	            final String NAMESPACE = "http://tempuri.org/";
	            final String SOAP_ACTION = "http://tempuri.org/INSERT_ACCURACY_USERS_FIRST_VERSION";
	            final String METHOD_NAME = "INSERT_ACCURACY_USERS_FIRST_VERSION";
	            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	            request.addProperty("User_ID",User_ID);
	            request.addProperty("Prediction_Accuracy_Images",Prediction_Accuracy_Images);
	            request.addProperty("Validation_Accuracy_Images",Validation_Accuracy_Images);
	            request.addProperty("Prediction_Accuracy_VN",Prediction_Accuracy_VN);
	            request.addProperty("Validation_Accuracy_VN",Validation_Accuracy_VN);

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