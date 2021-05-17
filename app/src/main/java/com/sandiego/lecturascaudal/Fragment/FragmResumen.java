package com.sandiego.lecturascaudal.Fragment;


import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavAction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sandiego.lecturascaudal.Controler.LecturaCaudalControler;
import com.sandiego.lecturascaudal.Customs.Functions;
import com.sandiego.lecturascaudal.Customs.Globals;
import com.sandiego.lecturascaudal.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmResumen extends Fragment {

    View view;
    NavController navController;
    Functions util;
    LecturaCaudalControler lecturaCaudalControler;
    Globals vars = Globals.getInstance();

    public FragmResumen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragm_resumen, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        findViewsByIds();
        actions();
        getLecturas();
    }

    private void findViewsByIds(){
        util = new Functions();
        navController = Navigation.findNavController(this.view);
        if(vars.getFECHA_BUSQUEDA().isEmpty()) {
            ((TextView) this.view.findViewById(R.id.lblFecha)).setText(util.getFechaActual());
        }else{
            ((TextView) this.view.findViewById(R.id.lblFecha)).setText(vars.getFECHA_BUSQUEDA());
        }

        ((TextView) this.view.findViewById(R.id.lblFecha)).setPaintFlags(((TextView) this.view.findViewById(R.id.lblFecha)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        ((Button) this.view.findViewById(R.id.btnVerDetalle)).setPaintFlags(((Button) this.view.findViewById(R.id.btnVerDetalle)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        ((Button) this.view.findViewById(R.id.btnActualizar)).setPaintFlags(((Button) this.view.findViewById(R.id.btnActualizar)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void actions(){

        ((Button) this.view.findViewById(R.id.btnVerDetalle)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.nex_actions);
            }
        });

        ((LinearLayout) this.view.findViewById(R.id.llyFecha)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                util.getFechaDialog(getContext(),((TextView) view.findViewById(R.id.lblFecha)));
            }
        });

        ((Button) this.view.findViewById(R.id.btnActualizar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLecturas();
            }
        });

    }


    private  void getLecturas(){
        lecturaCaudalControler = new LecturaCaudalControler(getContext(),((TextView)this.view.findViewById(R.id.lblUltimaLectura)));
        vars.setFECHA_BUSQUEDA(((TextView) this.view.findViewById(R.id.lblFecha)).getText().toString());
        lecturaCaudalControler.getLecturasCaudal(vars.getFECHA_BUSQUEDA(),false);
    }

}
