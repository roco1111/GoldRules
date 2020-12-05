package com.rosario.hp.goldrules.adapter;

import android.app.Activity;
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
import com.rosario.hp.goldrules.Entidades.secciones;
import com.rosario.hp.goldrules.R;
import com.rosario.hp.goldrules.declaracion;

import java.util.ArrayList;

public class seccionAdapter extends RecyclerView.Adapter<seccionAdapter.HolderSeccion>
        implements ItemClickListener4{

    private Context context;
    private ArrayList<secciones> secciones;


    public seccionAdapter(Context context, ArrayList<secciones> secciones) {
        this.context = context;
        this.secciones = secciones;
    }

    @Override
    public HolderSeccion onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_seccion,viewGroup,false);
        context = v.getContext();
        return new HolderSeccion(v,this);
    }

    public void onBindViewHolder(HolderSeccion holder, int position) {
        holder.titulo.setText(secciones.get(position).getSeccion());
        holder.ubicacion.setText(secciones.get(position).getUbicacion());
        holder.tipo_seccion.setText(secciones.get(position).getTipo_seccion());
        holder.sistema.setText(secciones.get(position).getSistema());

        String seccion = secciones.get(position).getCod_tipo_seccion();

        switch (seccion) {
            case "1":
                holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.item_maquina));
                break;
            case "2":
                holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.item_tablero));
                break;
            case "3":
                holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.zona));
                break;
        }

    }


    public static class HolderSeccion extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView titulo;
        public TextView ubicacion;
        public TextView tipo_seccion;
        public TextView sistema;
        public  ImageView imagen;
        public ItemClickListener4 listener;


        public HolderSeccion(View v, ItemClickListener4 listener) {
            super(v);
            titulo = v.findViewById(R.id.textViewTitulo);
            ubicacion = v.findViewById(R.id.textViewUbicacion);
            tipo_seccion = v.findViewById(R.id.textViewTipoSeccion);
            sistema = v.findViewById(R.id.textViewSistema);
            imagen = v.findViewById(R.id.imagenSeccion);

            this.listener = listener;

            v.setOnClickListener(this);
        }
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
            Intent intent = new Intent(v.getContext(), declaracion.class);
            v.getContext().startActivity(intent);
        }


    }

    @Override
    public int getItemCount() {
        return secciones.size();
    }

    @Override
    public void onItemClick(View view, int position) {
        String ls_seccion = secciones.get(position).getId();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("seccion", ls_seccion);
        editor.apply();
        editor.commit();
    }

}

interface ItemClickListener4 {
    void onItemClick(View view, int position);
}
