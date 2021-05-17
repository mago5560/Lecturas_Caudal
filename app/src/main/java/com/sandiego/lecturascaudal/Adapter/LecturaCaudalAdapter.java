package com.sandiego.lecturascaudal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sandiego.lecturascaudal.Class.LecturaCaudal;
import com.sandiego.lecturascaudal.R;

import java.util.ArrayList;
import java.util.List;

public class LecturaCaudalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    public List<LecturaCaudal> info;
    List<LecturaCaudal> aux;
    private final Context context;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;

    private static OnItemClickListener mItemClickListener;

    public interface OnItemClickListener{
        public void onClick(ItemAdapterViewHolder holder, int position, int clickId);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

    public LecturaCaudalAdapter(List<LecturaCaudal> info, Context context, OnItemClickListener mItemClickListener) {
        this.info = info;
        this.context = context;
        this.mItemClickListener = mItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            //Inflating header view
            View tarjeta = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_grd_datos, parent, false);
            return new HeaderViewHolder(tarjeta);
        } else if (viewType == TYPE_FOOTER) {
            //Inflating footer view
            View tarjeta = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_grd_datos, parent, false);
            return new FooterViewHolder(tarjeta);
        }

        View tarjeta = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lectura_caudal, parent, false);
        return new ItemAdapterViewHolder(tarjeta);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.headerTitle.setText("Registros Buscados");

        } else if (holder instanceof ItemAdapterViewHolder) {
            final LecturaCaudal cls = info.get(position);
            final ItemAdapterViewHolder holderItem = (ItemAdapterViewHolder) holder;
            holderItem.lblCVFecha.setText(cls.getFecha());
            holderItem.lblCVValor.setText(String.valueOf(cls.getValor()));


        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            footerHolder.footerText.setText("No se encontraron mas registros...");
        }

    }



    public void setInfo(List<LecturaCaudal> info){
        this.info = info;
        this.aux = new ArrayList<>(info);
    }

    @Override
    public int getItemCount() {

        if (info.size() == 0) {
            return 0;
        } else
            return info.size() + 1;
    }


    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if (position == 0) {
            //Se quita el Header devido a que en la lista quita el ultimo registro o en este caso el registro en la posicion 0 30/01/2019
            //return TYPE_HEADER;
        } else if (position == info.size()) {
            return TYPE_FOOTER;
        }else if(info.isEmpty()){
            return TYPE_FOOTER;
        }

        return TYPE_ITEM;
    }

    public class ItemAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView lblCVFecha,lblCVValor;
        private CardView cv;

        public ItemAdapterViewHolder(View itemView) {
            super(itemView);

            lblCVFecha = (TextView) itemView.findViewById(R.id.lblCVFecha);
            lblCVValor =(TextView) itemView.findViewById(R.id.lblCVValor);

            cv = (CardView) itemView.findViewById(R.id.cv);
            cv.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (mItemClickListener != null){
                if(v == cv) {
                    mItemClickListener.onClick(this, getLayoutPosition(), 0);
                }
            }
        }
    }


    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;

        public HeaderViewHolder(View view) {
            super(view);
            headerTitle = (TextView) view.findViewById(R.id.header_text);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView footerText;

        public FooterViewHolder(View view) {
            super(view);
            footerText = (TextView) view.findViewById(R.id.footer_text);
        }
    }
}
