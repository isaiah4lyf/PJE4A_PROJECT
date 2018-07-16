package SSS_SERVER_FUNCTIONS;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Return_User_With_ID {
	public Return_User_With_ID()
	{
	}
	public String do_The_Work(String URL,String User_ID)
	{
		 String User_Name = "";
	        try {
	            final String NAMESPACE = "http://tempuri.org/";
	            final String SOAP_ACTION = "http://tempuri.org/RETURN_USER_WITH_ID";
	            final String METHOD_NAME = "RETURN_USER_WITH_ID";
	            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	            request.addProperty("user_ID",User_ID);
;
	            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	            envelope.dotNet = true;
	            envelope.setOutputSoapObject(request);
	            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	            androidHttpTransport.call(SOAP_ACTION, envelope);
	            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
	            User_Name = response.toString();

	        } catch (Exception e) {
	        	User_Name = e.getLocalizedMessage();
	        }
	        
	        return User_Name;
	}
}

