package com.sandiego.lecturascaudal.Controler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sandiego.lecturascaudal.Adapter.LecturaCaudalAdapter;
import com.sandiego.lecturascaudal.Class.LecturaCaudal;
import com.sandiego.lecturascaudal.Class.Login;
import com.sandiego.lecturascaudal.Customs.Functions;
import com.sandiego.lecturascaudal.Customs.Globals;
import com.sandiego.lecturascaudal.Customs.WebService;
import com.sandiego.lecturascaudal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;

public class LecturaCaudalControler extends Functions {

    Context context;
    Globals vars = Globals.getInstance();
    WebService ws = new WebService();
    JSONArray jsonArray;
    JSONObject jsonObject;

    RecyclerView grdDatos;
    SwipeRefreshLayout refreshLayout;
    LecturaCaudalAdapter mAdapter;


    ArrayList<LecturaCaudal> list = new ArrayList<>();
    TaskGetLecturasCaudal taskGetLecturasCaudal;

    LoginControler loginControler;


    double TotalValor = 0.0;
    String UltimaLectura = "";
    boolean Detalle = false;

    TextView lblUltimaLectura;
    LecturaCaudal cls;

    public LecturaCaudalControler(Context context,TextView lblUltimaLectura) {
        this.context = context;
        this.lblUltimaLectura = lblUltimaLectura;
    }


    public LecturaCaudalControler(Context context, RecyclerView grdDatos, SwipeRefreshLayout refreshLayout, LecturaCaudalAdapter mAdapter) {
        this.context = context;
        this.grdDatos = grdDatos;
        this.refreshLayout = refreshLayout;
        this.mAdapter = mAdapter;
    }

    public double getTotalValor() {
        return TotalValor;
    }

    public String getUltimaLectura() {
        return UltimaLectura;
    }

    public void getLecturasCaudal(String Fecha,boolean Detalle) {
        String CodigoUsuario="",Contraseña="";
        this.Detalle = Detalle;

        vars.setMETHOD_NAME("Get_InformacionDeCaudal");
        loginControler = new LoginControler(context);
        if (loginControler.existLogin()) {
            Login cls = loginControler.getPreferenceLogin();
            CodigoUsuario = cls.getCodigoEmpleado();
            Contraseña = cls.getContraseña();
        }

        // <editor-fold defaultstate="collapsed" desc="(Set Property's)">
        int tamProInfo = 3;
        PropertyInfo propertyInfo[] = new PropertyInfo[tamProInfo];
        tamProInfo = 0;

        propertyInfo[tamProInfo] = new PropertyInfo();
        propertyInfo[tamProInfo].setName("Fecha");
        propertyInfo[tamProInfo].setValue(Fecha);
        propertyInfo[tamProInfo].setType(String.class);
        tamProInfo++;

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

        taskGetLecturasCaudal = new TaskGetLecturasCaudal();
        taskGetLecturasCaudal.execute(propertyInfo);
    }


    private class TaskGetLecturasCaudal extends AsyncTask<PropertyInfo, Integer, String> {
        ProgressDialog progressDialog;
        String respuesta = "";
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cls = new LecturaCaudal();
            progressDialog = ProgressDialog.show(context, "Consultando con servidor", "Este proceso puede tardar unos minutos, favor espere....", true, false);
        }

        @Override
        protected String doInBackground(PropertyInfo... propertyInfos) {
            try {
                respuesta = ws.wsSanDiego(propertyInfos);

            } catch (JSONException e) {
                e.printStackTrace();
                error = e.getMessage();
            } catch (Exception ex) {
                Log.e("Exec", ex.getMessage(), ex);
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
            if(taskGetLecturasCaudal != null ) {
                taskGetLecturasCaudal.cancel(true);
            }

            if (!s.isEmpty()) {
                viewResultConsulta(s);
            }else{
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
            Log.d("exec", String.valueOf(values));
        }
    }

    private void viewResultConsulta(String respuesta){
        try {

            if (respuesta != "") {

                list = new ArrayList<>();
                jsonArray = new JSONArray(respuesta);
                if(this.Detalle) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        cls = new LecturaCaudal();
                        cls.setFecha(jsonObject.getString("Fecha"));
                        cls.setValor(jsonObject.getString("Valor"));

                        list.add(cls);
                    }
                    mAdapter.setInfo(list);
                    grdDatos.setAdapter(mAdapter);
                    if (list.isEmpty()) {
                        msgSnackBar("No se encontraron registros", ((Activity) context));
                    }
                    refreshLayout.setRefreshing(false);
                }else{
                    jsonObject = jsonArray.getJSONObject(0);
                    lblUltimaLectura.setText(jsonObject.getString("Valor"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Log.e("Exec", ex.getMessage(), ex);
        }

    }


}
