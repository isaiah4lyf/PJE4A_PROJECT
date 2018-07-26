package SSS_SERVER_FUNCTIONS;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Decrement_Images_Model_Version {
	public Decrement_Images_Model_Version()
	{
	}
	public String do_The_Work(String URL,String Model_ID)
	{
		 String address = "";
	        try {
	            final String NAMESPACE = "http://tempuri.org/";
	            final String SOAP_ACTION = "http://tempuri.org/DECREMENT_IMAGES_MODEL_VERSION";
	            final String METHOD_NAME = "DECREMENT_IMAGES_MODEL_VERSION";
	            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	            request.addProperty("user_ID",Model_ID);
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
