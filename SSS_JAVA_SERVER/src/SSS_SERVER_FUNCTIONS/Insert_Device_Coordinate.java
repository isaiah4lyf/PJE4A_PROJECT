package SSS_SERVER_FUNCTIONS;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Insert_Device_Coordinate {
	public Insert_Device_Coordinate()
	{
	}
	public String do_The_Work(String URL,String Device_Mac,String Longitude,String Latitude)
	{
		 String address = "";
	        try {
	            final String NAMESPACE = "http://tempuri.org/";
	            final String SOAP_ACTION = "http://tempuri.org/INSERT_DEVICE_COORDINATE";
	            final String METHOD_NAME = "INSERT_DEVICE_COORDINATE";
	            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	            request.addProperty("Device_Mac",Device_Mac);
	            request.addProperty("Longitude",Longitude);
	            request.addProperty("Latitude",Latitude);
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