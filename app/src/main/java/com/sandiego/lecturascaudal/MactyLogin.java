package com.sandiego.lecturascaudal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sandiego.lecturascaudal.Class.Login;
import com.sandiego.lecturascaudal.Controler.LoginControler;
import com.sandiego.lecturascaudal.Customs.Functions;

public class MactyLogin extends AppCompatActivity {

    Intent intent;
    Functions util;
    LoginControler loginControler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macty_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("");
        //to remove the action bar (title bar)
        getSupportActionBar().hide();
        findViewsByIds();
        actions();
        getLogin();
    }

    private void findViewsByIds() {
        util = new Functions();
        loginControler = new LoginControler(this);
        ((TextView) findViewById(R.id.lblVersion)).setText(util.getVersion(this));
    }

    private void actions() {
        ((Button) findViewById(R.id.btnIngreso)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callLogin();
            }
        });

    }

    private void getLogin(){
        if (loginControler.existLogin()) {
            Login cls = loginControler.getPreferenceLogin();
            ((EditText) findViewById(R.id.txtEmpleado)).setText(cls.getCodigoEmpleado());
            ((EditText) findViewById(R.id.txtContraseña)).setText(cls.getContraseña());
            callLogin();
        }
    }

    private void callLogin(){
        if(validaCampos()) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                util.updateAndroidSecurityProvider(this);
            }
            loginControler.getLogin(((EditText) findViewById(R.id.txtEmpleado)).getText().toString(), ((EditText) findViewById(R.id.txtContraseña)).getText().toString());
        }
    }


    private boolean validaCampos(){
        if(util.validarCampoVacio(((EditText) findViewById(R.id.txtEmpleado)))){
            return false;
        }

        if(util.validarCampoVacio(((EditText) findViewById(R.id.txtContraseña)))){
            return false;
        }

        return true;
    }

    private void goPrincipal() {
        intent = new Intent().setClass(MactyLogin.this, MactyPrincipal.class);
        startActivity(intent);
        finish();
    }

}
