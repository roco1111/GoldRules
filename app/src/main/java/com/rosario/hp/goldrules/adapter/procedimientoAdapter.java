package com.rosario.hp.goldrules.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rosario.hp.goldrules.R;
import com.rosario.hp.goldrules.Entidades.procedimiento;
import com.rosario.hp.goldrules.activity_bloqueo;

import java.util.ArrayList;

public class procedimientoAdapter extends RecyclerView.Adapter<procedimientoAdapter.HolderProcedimiento>
        implements ItemClickListener5{

    private Context context;
    private ArrayList<procedimiento> Procedimientos;

    public procedimientoAdapter(Context context, ArrayList<procedimiento> Procedimientos) {
        this.context = context;
        this.Procedimientos = Procedimientos;
    }

    @Override
    public HolderProcedimiento onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_bloqueo,viewGroup,false);
        context = v.getContext();
        return new HolderProcedimiento(v,this);
    }

    public void onBindViewHolder(HolderProcedimiento holder, int position) {

        holder.titulo.setText("Procedimiento Nro: " + Procedimientos.get(position).getId());
        holder.fecha.setText(Procedimientos.get(position).getFecha());
        holder.descripcion.setText(Procedimientos.get(position).getMaquina() + " - " + Procedimientos.get(position).getSistema());
        holder.hora.setText(Procedimientos.get(position).getHora());
        if(Procedimientos.get(position).getCompleto().equals("1")){
            holder.icono.setImageDrawable(context.getResources().getDrawable(R.drawable.completo_item));
        }else{
            holder.icono.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_ok_item));
        }

        String seccion = Procedimientos.get(position).getTipo_seccion();

        switch (seccion) {
            case "1":
                holder.icono_seccion.setImageDrawable(context.getResources().getDrawable(R.drawable.item_maquina));
                break;
            case "2":
                holder.icono_seccion.setImageDrawable(context.getResources().getDrawable(R.drawable.item_tablero));
                break;
            case "3":
                holder.icono_seccion.setImageDrawable(context.getResources().getDrawable(R.drawable.zona));
                break;
        }

    }

    public static class HolderProcedimiento extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView titulo;
        public TextView fecha;
        public TextView descripcion;
        public TextView hora;
        public ImageView icono;
        public ImageView icono_seccion;

        public ItemClickListener5 listener;
        public HolderProcedimiento(View v, ItemClickListener5 listener) {
            super(v);
            titulo = v.findViewById(R.id.textViewTitulo);
            fecha = v.findViewById(R.id.textViewFecha);
            descripcion = v.findViewById(R.id.textViewDescripcion);
            hora = v.findViewById(R.id.textViewHora);
            icono = v.findViewById(R.id.imageViewIcono);
            icono_seccion = v.findViewById(R.id.imageViewSeccion);

            this.listener = listener;

            v.setOnClickListener(this);
        }
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
            Intent intent = new Intent(v.getContext(), activity_bloqueo.class);
            v.getContext().startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return Procedimientos.size();
    }


    @Override
    public void onItemClick(View view, int position) {
        String ls_procedimiento = Procedimientos.get(position).getId();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("procedimiento", ls_procedimiento);
        editor.apply();
        editor.commit();
    }
}

interface ItemClickListener5 {
    void onItemClick(View view, int position);
}
