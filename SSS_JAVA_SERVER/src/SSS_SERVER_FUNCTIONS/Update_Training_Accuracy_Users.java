package SSS_SERVER_FUNCTIONS;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Update_Training_Accuracy_Users {
	public Update_Training_Accuracy_Users()
	{
	}
	public String do_The_Work(String URL,String Model_ID, String Validation_Accuracy_Images,String Validation_Accuracy_VN)
	{
		 String address = "";
	        try 
	        {
	            final String NAMESPACE = "http://tempuri.org/";
	            final String SOAP_ACTION = "http://tempuri.org/UPDATE_TRAINING_ACCURACY_USERS";
	            final String METHOD_NAME = "UPDATE_TRAINING_ACCURACY_USERS";
	            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	            request.addProperty("Model_ID",Model_ID);
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


