package com.sandiego.lecturascaudal.Customs;

public class Globals {

    private static Globals instance;

    private Globals() {
    }

    public static synchronized Globals getInstance() {
        if (instance == null) {
            instance = new Globals();
        }
        return instance;
    }

    private	String NAMESPACE = "https://apps.sandiego.com.gt/ws";
    private	String WSURL = "https://apps.sandiego.com.gt/ws/wsInformacionGerencial.asmx";
    private	String SOAP_METHOD = "https://apps.sandiego.com.gt/ws/";

    private String INFO_FILE = "https://dl.dropbox.com/s/eakxdw4qydgqbh9/version.txt";

    private String LINK_APP = "https://dl.dropbox.com/s/ds80q6xfqxuzbts/LecturasCaudal.apk";

    private  String CONTACTENOS = "https://www.sandiego.com.gt/contact.php?tc=emp";
    private String NAME_APP = "LecturasCaudal.apk";

    private String FECHA_BUSQUEDA="";

    private String METHOD_NAME = "";

    public String getMETHOD_NAME() {
        return METHOD_NAME;
    }

    public void setMETHOD_NAME(String METHOD_NAME) {
        this.METHOD_NAME = METHOD_NAME;
    }

    public String getNAMESPACE() {
        return NAMESPACE;
    }

    public String getWSURL() {
        return WSURL;
    }

    public String getSOAP_METHOD() {
        return SOAP_METHOD;
    }

    public String getINFO_FILE() {
        return INFO_FILE;
    }

    public String getLINK_APP() {
        return LINK_APP;
    }

    public String getNAME_APP() {
        return NAME_APP;
    }

    public String getFECHA_BUSQUEDA() {
        return FECHA_BUSQUEDA;
    }

    public void setFECHA_BUSQUEDA(String FECHA_BUSQUEDA) {
        this.FECHA_BUSQUEDA = FECHA_BUSQUEDA;
    }

    public String getCONTACTENOS() {
        return CONTACTENOS;
    }
}
