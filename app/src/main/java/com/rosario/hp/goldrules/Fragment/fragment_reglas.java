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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.rosario.hp.goldrules.include.Constantes;
import com.rosario.hp.goldrules.include.VolleySingleton;
import com.rosario.hp.goldrules.Entidades.reglas;
import com.rosario.hp.goldrules.R;
import com.rosario.hp.goldrules.adapter.reglaAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class fragment_reglas extends Fragment {
    private reglaAdapter ReglaAdapter;

    private static final String TAG = fragment_reglas.class.getSimpleName();
    private ArrayList<reglas> datos;
    private RecyclerView lista;
    private TextView texto;
    private ImageView imagen;
    private RecyclerView.LayoutManager lManager;
    public fragment_reglas(){}
    private String procedimiento;
    private Activity act;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main_basico, container, false);



        datos = new ArrayList<>();

        act = getActivity();

        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);

        SharedPreferences settings1 = PreferenceManager.getDefaultSharedPreferences(getContext());
        procedimiento     = settings1.getString("procedimiento","");

        lista =  v.findViewById(R.id.reciclador);
        lista.setHasFixedSize(true);

        texto =  v.findViewById(R.id.TwEmpty);
        imagen = v.findViewById(R.id.ImEmpty);

        imagen.setVisibility(v.INVISIBLE);
        texto.setVisibility(v.INVISIBLE);

        lManager = new LinearLayoutManager(getActivity());
        lista.setLayoutManager(lManager);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("swipe","swipe");

                swipeRefreshLayout.setRefreshing(true);
                cargarAdaptador();
            }
        });
        cargarAdaptador();

        return v;
    }

    public void cargarAdaptador() {
        // Petición GET

        String newURL = Constantes.GET_REGLAS + "?idprocedimiento=" + procedimiento    ;
        Log.d(TAG,newURL);
        VolleySingleton.
                getInstance(getActivity()).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                newURL,
                                null,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        procesarRespuesta(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "Error Volley: " + error.toString());
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }

                        )
                );
    }
    private void procesarRespuesta(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO

                    JSONArray mensaje = response.getJSONArray("procedimiento");

                    datos.clear();

                    for(int i = 0; i < mensaje.length(); i++)
                    {JSONObject object = mensaje.getJSONObject(i);
                        reglas reg = new reglas("","");

                        String regla = object.getString("desc_regla");

                        reg.setDesc_regla(regla);

                        String nro_regla = object.getString("nro_regla");

                        reg.setNro_regla(nro_regla);

                        String completo= object.getString("completo");

                        reg.setCompleto(completo);

                        String observacion= object.getString("observacion");

                        reg.setObservacion(observacion);


                        String hora= object.getString("hora");

                        reg.setHora(hora);

                        String id_regla = object.getString("idregla");

                        reg.setId(id_regla);

                        datos.add(reg);

                    }

                    ReglaAdapter = new reglaAdapter(getContext(),datos, act);
                    // Setear adaptador a la lista
                    lista.setAdapter(ReglaAdapter);

                    break;
                case "2": // FALLIDO

                    imagen.setVisibility(getView().VISIBLE);
                    texto.setVisibility(getView().VISIBLE);


                    break;
            }
            swipeRefreshLayout.setRefreshing(false);

        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }

    }


}
