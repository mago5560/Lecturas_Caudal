package com.sandiego.lecturascaudal.Customs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

//import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
//import com.google.android.gms.common.GooglePlayServicesRepairableException;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.security.ProviderInstaller;
import com.google.android.material.snackbar.Snackbar;
import com.sandiego.lecturascaudal.BuildConfig;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Functions {

   // Globals vars = Globals.getInstance();

    public  String md5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }

        return hex.toString();
    }


    public String getToken(){
        Calendar calendar = Calendar.getInstance();
        int dia= calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH ) + 1;
        int anio = calendar.get(Calendar.YEAR);
        String token = "$anD!eg00@qq"+ dia + mes + anio;

        return md5(token);
    }

    public String getAnioActual(){
        Date date = new Date();
        DateFormat houFormat = new SimpleDateFormat("yyyy");
        String Anio = houFormat.format(date);
        return Anio;
    }

    public String getFechaHoraActual(){
        Date date = new Date();
        DateFormat houFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String FechaHora = houFormat.format(date);
        return FechaHora;
    }

    public String getFechaActual(){
        Date date = new Date();
        DateFormat houFormat = new SimpleDateFormat("dd/MM/yyyy");
        String FechaHora = houFormat.format(date);
        return FechaHora;
    }

    public String getHoraActual(){
        Date date = new Date();
        DateFormat hourFormat = new SimpleDateFormat("HH:mm");
        String Hora = hourFormat.format(date);
        return Hora;
    }

    public  int getIndexSpn(Spinner spinner, String idCodigo){
        int index= 0;
        for(int i=0;i< spinner.getCount();i++){

            if(getCodigo(spinner.getItemAtPosition(i).toString(),"-").equalsIgnoreCase(idCodigo)){
                index =  i;
                break;
            }
        }
        return index;
    }

    public String getCodeFoto(){
        Long timestamp = System.currentTimeMillis() / 1000;
        String imageName = timestamp.toString() + ".jpg";
        return imageName;
    }

    public  String getCodigo(String xDescripcion,String xSeparado){
        String [] cadena = xDescripcion.split(xSeparado);
        return cadena[0];
    }

    public  String getDescripcion(String xDescripcion,String xSeparado){
        String [] cadena = xDescripcion.split(xSeparado);
        return cadena[1];
    }

    public String getNombreImagen(String path){
        String [] cadena = path.split(File.separator);
        return cadena[cadena.length - 1];
    }

    public String getVersion( Context context) {
        int currentVersionCode= 0;
        String currentVersionName="";
        try {

            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            currentVersionCode = packageInfo.versionCode;
            currentVersionName = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AutoUpdate", "Ha habido un error con el packete :S", e);
        }
        return "Version: " +Integer.toString(currentVersionCode) + "." + currentVersionName;
    }

    public AlertDialog mensaje(String Mensaje, Activity Macty ){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Macty);
        AlertDialog alerta;
        builder.setCancelable(false);

        builder.setTitle("Mensaje del Sistema");
        builder.setMessage(Mensaje);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alerta = builder.create();
        return alerta;
    }

    public AlertDialog mensajeError(String mensaje, Activity activity ){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        AlertDialog alerta;
        builder.setCancelable(false);

        builder.setTitle("Mensaje del Sistema");
        builder.setMessage(mensaje + ".\nInformar a TI");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alerta = builder.create();
        return alerta;
    }

    public void msgSnackBar(String mensaje, Context context){
        Snackbar.make(((Activity)context).getWindow().getDecorView().getRootView(),mensaje,Snackbar.LENGTH_LONG)
                .show();
    }
    public Bitmap getBitmap(String path, Activity macty){
        Bitmap bMap = null;
        File imgexts = new File(path);
        Uri uriSaveImage;
        if (imgexts.exists()){
            try {
                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                    uriSaveImage = FileProvider.getUriForFile(macty, BuildConfig.APPLICATION_ID +".fileprovider",imgexts);
                }else{
                    uriSaveImage = Uri.fromFile(imgexts);
                }
                InputStream is = macty.getContentResolver().openInputStream(uriSaveImage);
                BufferedInputStream bis = new BufferedInputStream(is);
                bMap = BitmapFactory.decodeStream(bis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return  bMap;
    }

    public Uri getUri(String Path,Activity macty){
        File directorioImagen = new File(Path);
        Uri uri = null;
        if(directorioImagen.exists()) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                uri = FileProvider.getUriForFile(macty, BuildConfig.APPLICATION_ID + ".fileprovider", directorioImagen);
            } else {
                uri = Uri.fromFile(directorioImagen);
            }
        }
        return  uri;
    }

    /*
    public  String getPath(){
        String picturePath="";
        File file = new File(Environment.getExternalStorageDirectory(), "DCIM" + vars.getPATH_FILE_API());
        boolean isDirectoryCreated = file.exists();
        if(!isDirectoryCreated) {
            isDirectoryCreated = file.mkdirs();
        }
        if(isDirectoryCreated){
            picturePath = Environment.getExternalStorageDirectory() + File.separator + "DCIM" + vars.getPATH_FILE_API() + File.separator;
        }
        return picturePath;
    }
     */


    public String encodeImage(Bitmap bm) {
        String imgDecodableString;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        imgDecodableString = Base64.encodeToString(b, Base64.DEFAULT);

        return imgDecodableString;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public String encodeImages(String path) {
        String imgDecodableString = "";
        try{
            File imagefile = new File(path);
            FileInputStream fis = null;
            fis = new FileInputStream(imagefile);
            Bitmap bm = BitmapFactory.decodeStream(fis);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] b = baos.toByteArray();
            imgDecodableString = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Base64.de
        return imgDecodableString;

    }

    public boolean isGPSEnabled (Context mContext){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean validarCampoVacio(EditText editText){
        String Cadena = editText.getText().toString();
        if(TextUtils.isEmpty(Cadena)){
            editText.setError("Campo requerido");
            editText.requestFocus();
            return true;
        }else{
            editText.setError(null);
        }
        return false;
    }


    public String getDiferenciaFecha(Date fechaInicial, Date fechaFinal,String DatePart){

        String Resultado = "";
        double calculoDif;
        int calucloDias;
        long diferencia = fechaFinal.getTime() - fechaInicial.getTime();

        switch (DatePart) {
            case  "D":
                calculoDif =  (diferencia / 86400000);
                Resultado = String.valueOf((int) calculoDif);
                break;
            case "H":
                calculoDif = Double.valueOf(diferencia) / 3600000;
                Resultado = String.valueOf(  Double.parseDouble(new DecimalFormat("####.####").format(calculoDif)));
                break;
        }
        return  Resultado;
    }

    public boolean getFechaInicialMayor(Date fechaInicial,Date fechaFinal){
        boolean mayor = false;
        if(fechaInicial.getTime() > fechaFinal.getTime()){
            mayor = true;
        }
        return mayor;
    }

    public Date convertToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }

    public Date convertToDateTime(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }

    public String getCorrelativo(String UnitCode){
        Date date = new Date();
        DateFormat houFormat = new SimpleDateFormat("yyMMdd");
        String code = houFormat.format(date);
        return  code +  UnitCode ;
    }

    public String getCorrelativo(String UnitCode,String Fecha){
        String codigo="";
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date(dateFormat.parse(Fecha).getTime());
            DateFormat houFormat = new SimpleDateFormat("yyMMdd");
            String code = houFormat.format(date);
            codigo = code +  UnitCode ;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  codigo;
    }



    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    public void  getHoraDialog(Context context, final TextView control){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaF = (hourOfDay < 10 ) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minutoF = (minute < 10 ) ? String.valueOf(CERO + minute) : String.valueOf(minute);

                control.setText(horaF + DOS_PUNTOS + minutoF + DOS_PUNTOS +"00");
            }
        },hour,minute,true );
        timePickerDialog.setTitle("Seleccione la Hora");
        timePickerDialog.show();
    }

    public void  getHoraDialog(Context context, final EditText control){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaF = (hourOfDay < 10 ) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minutoF = (minute < 10 ) ? String.valueOf(CERO + minute) : String.valueOf(minute);

                control.setText(horaF + DOS_PUNTOS + minutoF + DOS_PUNTOS +"00");
            }
        },hour,minute,true );
        timePickerDialog.setTitle("Seleccione la Hora");
        timePickerDialog.show();
    }

    private static final String DIAGONAL = "/";
    public void getFechaDialog(Context context , final TextView control){
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH) ;
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dataPickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Se coloca +1 debido a que android coloca el mes seleccionado -1
                month +=1;
                String smonth = (month < 10 ) ? String.valueOf(CERO + month) : String.valueOf(month);
                String sdayOfMonth = (dayOfMonth < 10 ) ? String.valueOf(CERO + dayOfMonth) : String.valueOf(dayOfMonth);
                control.setText(sdayOfMonth + DIAGONAL + smonth + DIAGONAL + year );
            }
        },mYear,mMonth,mDay);
        dataPickerDialog.setTitle("Seleccione la Fecha");
        dataPickerDialog.show();
    }

    public void getFechaDialog(Context context , final EditText control){
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH) ;
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dataPickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Se coloca +1 debido a que android coloca el mes seleccionado -1 y tambien el dia para colocarle el 0 a los numeros < 10
                month +=1;
                String smonth = (month < 10 ) ? String.valueOf(CERO + month) : String.valueOf(month);
                String sdayOfMonth = (dayOfMonth < 10 ) ? String.valueOf(CERO + dayOfMonth) : String.valueOf(dayOfMonth);
                control.setText(sdayOfMonth + DIAGONAL + smonth + DIAGONAL + year );
            }
        },mYear,mMonth,mDay);
        dataPickerDialog.setTitle("Seleccione la Fecha");
        dataPickerDialog.show();
    }


    //solucion por inconveniente de SSL protocolo abortado.
    //SSLHandshake en dispositivos con versiones de Android anteriores a Android 5.0.
    public void updateAndroidSecurityProvider(Activity callingActivity) {
        /*
        try {
            ProviderInstaller.installIfNeeded(callingActivity);
        } catch (GooglePlayServicesRepairableException e) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("SecurityException", "Google Play Services not available.");
        }

         */
    }


}
