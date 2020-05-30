package com.rosario.hp.goldrules.Fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rosario.hp.goldrules.Entidades.reglas;
import com.rosario.hp.goldrules.R;
import com.rosario.hp.goldrules.adapter.reglaAdapter;


import java.util.ArrayList;

public class fragment_regla extends Fragment {
    private reglaAdapter ReglaAdapter;

    private static final String TAG = fragment_regla.class.getSimpleName();

    private ArrayList<reglas> datos;
    private RecyclerView lista;
    private TextView titulo;
    private TextView observacion;
    private TextView desc_regla;
    private TextView hora;
    private String ls_hora;
    private ImageView imagen;
    private ImageView item;
    public fragment_regla(){}
    private String regla;
    private String ls_completo;
    private String ls_observacion;
    private String ls_desc_regla;
    private Activity act;
    StorageReference storageRef;
    private FirebaseStorage storage;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_regla, container, false);

        datos = new ArrayList<>();

        act = getActivity();

        SharedPreferences settings1 = PreferenceManager.getDefaultSharedPreferences(getContext());
        regla     = settings1.getString("regla","");
        ls_observacion = settings1.getString("observacion","");
        ls_desc_regla = settings1.getString("desc_regla","");
        ls_completo = settings1.getString("completo","");
        ls_hora = settings1.getString("hora","");

        imagen = v.findViewById(R.id.imageViewRegla);

        titulo = v.findViewById(R.id.textViewTitulo);

        observacion = v.findViewById(R.id.textViewObservacion);

        desc_regla = v.findViewById(R.id.textViewDesc);

        item = v.findViewById(R.id.imageViewitem);

        hora = v.findViewById(R.id.textHora);


        titulo.setText("Regla Nro: " + regla);

        desc_regla.setText(ls_desc_regla);

        observacion.setText(ls_observacion);

        hora.setText("Hora: " + ls_hora);

        if(ls_completo.equals("1")) {
            item.setImageDrawable(getContext().getResources().getDrawable(R.drawable.item_completo));
        }else{
            item.setImageDrawable(getContext().getResources().getDrawable(R.drawable.item_incompleto));
        }

        actualizar_foto(regla,imagen);


        return v;
    }

    private void actualizar_foto(String ls_regla, ImageView imagen){

        storage = FirebaseStorage.getInstance();

        if (storageRef == null)
            storageRef = storage.getReference();

        String mChild = "reglas/" + ls_regla  + ".jpg";
        Log.d(TAG,mChild);
        final StorageReference filepath = storageRef.child(mChild);

        clearGlideCache();

        Glide.with(getContext().getApplicationContext())
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
                Glide.get(getContext().getApplicationContext()).clearDiskCache();
            }
        }.start();

        Glide.get(getContext().getApplicationContext()).clearMemory();
    }




}
