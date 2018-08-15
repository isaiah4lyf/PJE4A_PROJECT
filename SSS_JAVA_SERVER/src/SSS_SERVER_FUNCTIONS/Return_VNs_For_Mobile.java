package SSS_SERVER_FUNCTIONS;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class Return_VNs_For_Mobile {
	public Return_VNs_For_Mobile()
	{
		
	}
	
	public String[] Do_The_Work(String URL)
	{
        final String NAMESPACE = "http://tempuri.org/";
        final String SOAP_ACTION = "http://tempuri.org/RETURN_VNS_FOR_MOBILE";
        final String METHOD_NAME = "RETURN_VNS_FOR_MOBILE";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

         
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
		String[] vns = null;
         if(size > 0)
         {
        	 vns = new String[size];
             for(int i = 0; i<size; i++)
             {
            	 vns[i] = response_collection.getProperty(i).toString();
             }
         }

  
         return vns;
	}

}