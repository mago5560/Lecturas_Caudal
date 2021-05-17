package com.sandiego.lecturascaudal.Controler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import com.sandiego.lecturascaudal.Class.Login;
import com.sandiego.lecturascaudal.Customs.Functions;
import com.sandiego.lecturascaudal.Customs.Globals;
import com.sandiego.lecturascaudal.Customs.WebService;
import com.sandiego.lecturascaudal.MactyLogin;
import com.sandiego.lecturascaudal.MactyPrincipal;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

public class LoginControler extends Functions {

    SharedPreferences sharedPreferencesLogin;
    static final String PreferencesLogin = "Login";
    Context context;
    Globals vars = Globals.getInstance();
    TaskGetLogin taskGetLogin;
    JSONArray jsonArray;
    JSONObject jsonObject;
    WebService ws ;


    public LoginControler(Context context) {
        this.context = context;
        ws = new WebService();
        sharedPreferencesLogin = context.getSharedPreferences(PreferencesLogin, Context.MODE_PRIVATE);
    }

    public boolean existLogin() {
        Login cls = getPreferenceLogin();
        if (cls.getIdUsuario() > 0) {
            return true;
        }
        return false;
    }



    public String getIniciales(){
        String iniciales="";
        String[] letras = sharedPreferencesLogin.getString("nombre", "").split(" ");
        if(letras.length > 1) {
            iniciales = letras[0].substring(0, 1) + letras[1].substring(0, 1);
        }else{
            iniciales = letras[0].substring(0, 1) + letras[0].substring(1, 2);
        }
        return iniciales;
    }

    public boolean loginVencido(){
        String fechaInicial = sharedPreferencesLogin.getString("fechaingreso", "");
        if(!fechaInicial.isEmpty()) {
            int resultFecha = Integer.valueOf(getDiferenciaFecha(
                    convertToDate(fechaInicial)
                    , convertToDate(getFechaActual())
                    , "D"));
            if (resultFecha <= 15) {
                return false;
            }
        }
        return true;
    }

    public Login getPreferenceLogin() {
        Login cls = new Login();
        cls.setIdUsuario(sharedPreferencesLogin.getInt("idusuario", 0));
        cls.setCodigoEmpleado(sharedPreferencesLogin.getString("codigoempleado", ""));
        cls.setContraseña(sharedPreferencesLogin.getString("contraseña", ""));
        cls.setUsuario(sharedPreferencesLogin.getString("usuario", ""));
        cls.setNombre(sharedPreferencesLogin.getString("nombre", ""));
        cls.setFechaIngreso(sharedPreferencesLogin.getString("fechaingreso", ""));
        return cls;
    }

    public void cerrarSesion() {
        SharedPreferences.Editor editor = sharedPreferencesLogin.edit();
        editor.putInt("idusuario", 0);
        editor.putString("codigoempleado", "");
        editor.putString("contraseña", "");
        editor.putString("usuario", "");
        editor.putString("nombre", "");
        editor.putString("fechaingreso", "");
        editor.commit();
        Intent intent = new Intent().setClass(context, MactyLogin.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    private  void setPreferencesLogin(String CodigoUsuario,String Contraseña){
        SharedPreferences.Editor editor = sharedPreferencesLogin.edit();
        editor.putString("codigoempleado",CodigoUsuario);
        editor.putString("contraseña", Contraseña);
        editor.commit();
    }

    private void setSharedPreferencesLogin(Login cls){
        SharedPreferences.Editor editor = sharedPreferencesLogin.edit();
        editor.putInt("idusuario", cls.getIdUsuario());
        editor.putString("usuario", cls.getUsuario());
        editor.putString("nombre", cls.getNombre());
        editor.putString("fechaingreso", cls.getFechaIngreso());
        editor.commit();
        Intent intent = new Intent().setClass(context, MactyPrincipal.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public void getLogin(String CodigoUsuario,String Contraseña) {
        setPreferencesLogin(CodigoUsuario,Contraseña);
        vars.setMETHOD_NAME("LogInIOS_IG");
        // <editor-fold defaultstate="collapsed" desc="(Set Property's)">
        int tamProInfo = 2;
        PropertyInfo propertyInfo[] = new PropertyInfo[tamProInfo];
        tamProInfo = 0;

        propertyInfo[tamProInfo] = new PropertyInfo();
        propertyInfo[tamProInfo].setName("Usuario");
        propertyInfo[tamProInfo].setValue(CodigoUsuario);
        propertyInfo[tamProInfo].setType(String.class);
        tamProInfo++;

        propertyInfo[tamProInfo] = new PropertyInfo();
        propertyInfo[tamProInfo].setName("Identificacion");
        propertyInfo[tamProInfo].setValue(md5(Contraseña));
        propertyInfo[tamProInfo].setType(String.class);
        tamProInfo++;

        // </editor-fold>
        taskGetLogin = new TaskGetLogin();
        taskGetLogin.execute(propertyInfo);
    }


    private class TaskGetLogin extends AsyncTask<PropertyInfo, Integer,String>{
        Login cls;
        ProgressDialog progressDialog;
        String respuesta = "";
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cls = new Login();
            progressDialog = ProgressDialog.show(context, "Consultando con servidor", "Este proceso puede tardar unos minutos, favor espere....", true, false);
        }

        @Override
        protected String doInBackground(PropertyInfo... propertyInfos) {
            try {
                respuesta = ws.wsSanDiego(propertyInfos);
                if (respuesta != "") {
                    jsonArray = new JSONArray(respuesta);
                    jsonObject = jsonArray.getJSONObject(0);
                    cls.setIdUsuario(jsonObject.getInt("idUsuario"));
                    cls.setUsuario(jsonObject.getString("Usuario"));
                    cls.setNombre(jsonObject.getString("Nombre"));
                    cls.setFechaIngreso(getFechaHoraActual());
                    setSharedPreferencesLogin(cls);
                }
            }catch (Exception ex){
                Log.e("Exec",ex.getMessage(),ex);
                error = ex.getMessage();
            }

            return respuesta;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (s.isEmpty()) {
                if (!error.isEmpty()) {
                    mensaje(error + ".\nFavor de volver a intentar", ((Activity) context)).show();
                } else {
                    mensaje("No se encontro respuesta del server y/o usuario no tiene accesos.\nFavor de volver a intentar", ((Activity) context)).show();
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("exec",String.valueOf(values));
        }
    }





}

