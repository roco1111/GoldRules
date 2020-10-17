package com.rosario.hp.goldrules.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rosario.hp.goldrules.Entidades.reglas;
import com.rosario.hp.goldrules.R;
import com.rosario.hp.goldrules.activity_regla;

import java.util.ArrayList;

public class reglaAdapter extends RecyclerView.Adapter<reglaAdapter.HolderRegla>
        implements ItemClickListener6{

    private Context context;
    private ArrayList<reglas> reglas;
    private Activity act;

    public reglaAdapter(Context context, ArrayList<reglas> reglas, Activity act) {
        this.context = context;
        this.reglas = reglas;
        this.act = act;
    }

    @Override
    public HolderRegla onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_regla,viewGroup,false);
        context = v.getContext();
        return new HolderRegla(v,this);
    }

    public void onBindViewHolder(HolderRegla holder, int position) {
        holder.titulo.setText(reglas.get(position).getDesc_regla());
        holder.descripcion.setText(reglas.get(position).getHora());
        if(reglas.get(position).getCompleto().equals("1"))
        {
            holder.completo.setImageDrawable(context.getResources().getDrawable(R.drawable.item_completo));
        }else{
            holder.completo.setImageDrawable(context.getResources().getDrawable(R.drawable.item_incompleto));
        }

        if(reglas.get(position).getObservacion().equals(""))
        {
            holder.observacion.setVisibility(View.INVISIBLE);
        }else{
            holder.observacion.setVisibility(View.VISIBLE);
        }

        actualizar_foto(reglas.get(position).getId(),holder.imagen);

    }

    private void actualizar_foto(String ls_regla, ImageView imagen){

        StorageReference storageRef = null;
        FirebaseStorage storage;

        storage = FirebaseStorage.getInstance();

        if (storageRef == null)
            storageRef = storage.getReference();

        String mChild = "reglas/" + ls_regla  + ".jpg";

        final StorageReference filepath = storageRef.child(mChild);

        clearGlideCache();

        Glide.with(context.getApplicationContext())
                .load(filepath)
                .error(R.drawable.ic_account_circle)
                .fallback(R.drawable.ic_account_circle)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .centerCrop ()
                .into(imagen);

    }



    void clearGlideCache()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                Glide.get(context.getApplicationContext()).clearDiskCache();
            }
        }.start();

        Glide.get(context.getApplicationContext()).clearMemory();
    }


    public static class HolderRegla extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView titulo;
        public ImageView completo;
        public ImageView observacion;
        public TextView descripcion;
        public  ImageView imagen;



        public ItemClickListener6 listener;



        public HolderRegla(View v, ItemClickListener6 listener) {
            super(v);
            titulo = v.findViewById(R.id.textViewTitulo);
            descripcion = v.findViewById(R.id.textViewDescripcion);
            completo = v.findViewById(R.id.imageViewCompleto);
            observacion = v.findViewById(R.id.imageViewObservaciones);
            imagen = v.findViewById(R.id.imagenSeccion);

            this.listener = listener;

            v.setOnClickListener(this);
        }
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
            Intent intent = new Intent(v.getContext(), activity_regla.class);
            v.getContext().startActivity(intent);
        }


    }

    @Override
    public int getItemCount() {
        return reglas.size();
    }
    @Override
    public void onItemClick(View view, int position) {
        String ls_regla = reglas.get(position).getNro_regla();
        String ls_observacion = reglas.get(position).getObservacion();
        String ls_desc_regla = reglas.get(position).getDesc_regla();
        String ls_completo = reglas.get(position).getCompleto();
        String ls_hora = reglas.get(position).getHora();
        String ls_id = reglas.get(position).getId();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("regla", ls_regla);
        editor.putString("observacion", ls_observacion);
        editor.putString("desc_regla", ls_desc_regla);
        editor.putString("completo", ls_completo);
        editor.putString("hora", ls_hora);
        editor.putString("idregla", ls_id);

        editor.apply();
        editor.commit();
    }



}

interface ItemClickListener6 {
    void onItemClick(View view, int position);
}
