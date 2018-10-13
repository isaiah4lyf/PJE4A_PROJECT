package SSS_SERVER_FUNCTIONS;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Update_Training_Accuracy_Users_VN {
	public Update_Training_Accuracy_Users_VN()
	{
	}
	public String do_The_Work(String URL,String Model_ID_VN, String Validation_Accuracy_Images,String Validation_Accuracy_VN)
	{
		 String address = "";
	        try 
	        {
	            final String NAMESPACE = "http://tempuri.org/";
	            final String SOAP_ACTION = "http://tempuri.org/UPDATE_TRAINING_ACCURACY_USERS_VN";
	            final String METHOD_NAME = "UPDATE_TRAINING_ACCURACY_USERS_VN";
	            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	            request.addProperty("Model_ID_VN",Model_ID_VN);
	            request.addProperty("Validation_Accuracy_Images",Validation_Accuracy_Images);
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



