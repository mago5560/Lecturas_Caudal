package com.sandiego.lecturascaudal.Fragment;


import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sandiego.lecturascaudal.Adapter.LecturaCaudalAdapter;
import com.sandiego.lecturascaudal.Class.LecturaCaudal;
import com.sandiego.lecturascaudal.Controler.LecturaCaudalControler;
import com.sandiego.lecturascaudal.Customs.Functions;
import com.sandiego.lecturascaudal.Customs.Globals;
import com.sandiego.lecturascaudal.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmLecturas extends Fragment implements  LecturaCaudalAdapter.OnItemClickListener{

    View view;
    Functions util;
    Globals vars = Globals.getInstance();

    RecyclerView grdDatos;
    SwipeRefreshLayout refreshLayout;
    LecturaCaudalAdapter mAdapter;
    LecturaCaudalControler lecturaCaudalControler;
    ArrayList<LecturaCaudal> list = new ArrayList<>();

    public FragmLecturas() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragm_lecturas, container, false);
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
        if(vars.getFECHA_BUSQUEDA().isEmpty()) {
            ((TextView) this.view.findViewById(R.id.lblFecha)).setText(util.getFechaActual());
        }else{
            ((TextView) this.view.findViewById(R.id.lblFecha)).setText(vars.getFECHA_BUSQUEDA());
        }
        refreshLayout = (SwipeRefreshLayout) this.view.findViewById(R.id.swipeRefresh);
        grdDatos = (RecyclerView) this.view.findViewById(R.id.grdDatos);
        grdDatos.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        grdDatos.setLayoutManager(llm);
        list = new ArrayList<>();
        mAdapter = new  LecturaCaudalAdapter(list,getActivity(),this);
        ((TextView) this.view.findViewById(R.id.lblFecha)).setPaintFlags(((TextView) this.view.findViewById(R.id.lblFecha)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void actions(){

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLecturas();
            }
        });


        ((LinearLayout) this.view.findViewById(R.id.llyFecha)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                util.getFechaDialog(getContext(),((TextView) view.findViewById(R.id.lblFecha)));
            }
        });

    }

    private  void getLecturas(){
        lecturaCaudalControler = new LecturaCaudalControler(getActivity(), grdDatos, refreshLayout, mAdapter);
        vars.setFECHA_BUSQUEDA(((TextView) this.view.findViewById(R.id.lblFecha)).getText().toString());
        lecturaCaudalControler.getLecturasCaudal(vars.getFECHA_BUSQUEDA(),true);
        ((TextView) this.view.findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());
    }

    @Override
    public void onClick(LecturaCaudalAdapter.ItemAdapterViewHolder holder, int position, int clickId) {

    }

}
