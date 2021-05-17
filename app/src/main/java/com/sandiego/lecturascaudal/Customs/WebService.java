package com.sandiego.lecturascaudal.Customs;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebService extends Functions {
    String RESPUESTA_WS;
    Globals vars = Globals.getInstance();

    public String wsSanDiego(PropertyInfo[] parameter ) throws Exception{
        RESPUESTA_WS= "";
        //crate Request
        SoapObject request = new SoapObject(vars.getNAMESPACE(),vars.getMETHOD_NAME());

        //Add the Proprty to Request object
        for(int i=0;i<parameter.length;i++){
            request.addProperty(parameter[i].name,parameter[i].getValue());
        }
        request.addProperty("Token",getToken());
        //SSL connection
        SSLConnection.allowAllSSL();

        //Crea Envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        //Set output SOAP object
        envelope.setOutputSoapObject(request);

        //Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(vars.getWSURL());

        try
        {
            //Invole Web Service
            androidHttpTransport.call(vars.getSOAP_METHOD()+ vars.getMETHOD_NAME(), envelope);
            //Get the Response
            SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
            RESPUESTA_WS =  response.toString();

        }catch (Exception e){
            Log.e("wsSanDiego", e.getMessage(), e);
            //Throw e;
        }
        return  RESPUESTA_WS;
    }
}
