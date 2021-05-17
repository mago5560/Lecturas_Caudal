package com.sandiego.lecturascaudal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.sandiego.lecturascaudal.Controler.LoginControler;
import com.sandiego.lecturascaudal.Customs.Functions;
import com.sandiego.lecturascaudal.Customs.Globals;

public class MactyPrincipal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    ActionBarDrawerToggle toggle;
    View navHeader;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Globals vars = Globals.getInstance();

    LoginControler loginControler;
    Functions util = new Functions();

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macty_principal);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        findViewxByIds();
        actions();
        existLogin();
    }


    private void findViewxByIds() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Lecturas Caudal");
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loginControler = new LoginControler(this);

    }

    private void actions() {

    }

    private void existLogin() {

        if (!loginControler.existLogin()) {
            intent = new Intent().setClass(this, MactyLogin.class);
            startActivity(intent);
            finish();
        } else if (loginControler.loginVencido()) {
            intent = new Intent().setClass(this, MactyLogin.class);
            startActivity(intent);
            finish();
        } else {
            setNavHeader();
        }
    }

    private void setNavHeader() {
        navHeader = navigationView.getHeaderView(0);
        ((TextView) navHeader.findViewById(R.id.lblInicialesMenu)).setText(loginControler.getIniciales());
        ((TextView) navHeader.findViewById(R.id.lblVersion)).setText(util.getVersion(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        toggle.syncState();
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        toggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_contactanos:
                Contactanos();
                break;
            case R.id.nav_actualizacion:
                DescargarActualizacionApp();
                break;
            case R.id.nav_compartir_api:
                CompartirAplicacion();
                break;

            case R.id.mnucerrarsesion:
                loginControler.cerrarSesion();
                break;
            default:
                util.msgSnackBar("Opcion en mantenimiento", this);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void CompartirAplicacion() {
        intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, vars.getLINK_APP());
        startActivity(Intent.createChooser(intent, "Compartir " + vars.getNAME_APP()));
    }

    private void DescargarActualizacionApp() {
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(vars.getLINK_APP()));
        startActivity(intent);
    }

    private void Contactanos() {
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(vars.getCONTACTENOS()));
        startActivity(intent);
    }

}
