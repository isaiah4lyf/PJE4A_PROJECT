package SSS_SERVER_FUNCTIONS;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Train_Images_Model {
	public Train_Images_Model()
	{
	}
	public String do_The_Work(String URL,String User_ID)
	{
		 String address = "";
	        try {
	            final String NAMESPACE = "http://tempuri.org/";
	           
	            final String SOAP_ACTION = "http://tempuri.org/TRAIN_IMAGES_MODEL";
	            final String METHOD_NAME = "TRAIN_IMAGES_MODEL";
	            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	            request.addProperty("user_ID",User_ID);

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
