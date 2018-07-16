package SSS_SERVER_FUNCTIONS;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class Return_Users_In_Model {
	public Return_Users_In_Model()
	{
	}
	
	public String[] Do_The_Work(String URL,String Model_ID)
	{
        final String NAMESPACE = "http://tempuri.org/";
        final String SOAP_ACTION = "http://tempuri.org/RETURN_USERS_IN_MODEL";
        final String METHOD_NAME = "RETURN_USERS_IN_MODEL";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("Model_ID",Model_ID);
         SoapSerializationEnvelope envelope_collection = new SoapSerializationEnvelope(SoapEnvelope.VER11);
         envelope_collection.dotNet = true;
         envelope_collection.setOutputSoapObject(request);
         HttpTransportSE androidHttpTransport_collection = new HttpTransportSE(URL);
         try {
			androidHttpTransport_collection.call(SOAP_ACTION, envelope_collection);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         SoapObject response_collection = null;
		try {
			response_collection = (SoapObject)envelope_collection.getResponse();
		} catch (SoapFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int size = response_collection.getPropertyCount();
		String[] users = null;
         if(size > 0)
         {
        	 users = new String[size];
             for(int i = 0; i<size; i++)
             {
            	 users[i] = response_collection.getProperty(i).toString();
             }
         }

  
         return users;
	}
}