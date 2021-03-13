package com.rosario.hp.goldrules.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.rosario.hp.goldrules.Entidades.secciones;
import com.rosario.hp.goldrules.R;
import com.rosario.hp.goldrules.activity_preferencias;
import com.rosario.hp.goldrules.adapter.seccionAdapter;
import com.rosario.hp.goldrules.include.Constantes;
import com.rosario.hp.goldrules.include.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class fragment_secciones extends Fragment {
    private seccionAdapter SeccionAdapter;

    private static final String TAG = fragment_secciones.class.getSimpleName();
    public static final String ARG_ARTICLES_NUMBER = "principal";
    private ArrayList<secciones> datos;
    private RecyclerView lista;
    private TextView texto;
    private ImageView imagen;
    private String ls_empresa;
    private RecyclerView.LayoutManager lManager;
    SwipeRefreshLayout swipeRefreshLayout;
    public fragment_secciones(){}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main_basico, container, false);

        setHasOptionsMenu(true);

        datos = new ArrayList<>();

        lista =  v.findViewById(R.id.reciclador);
        lista.setHasFixedSize(true);

        texto =  v.findViewById(R.id.TwEmpty);
        imagen = v.findViewById(R.id.ImEmpty);

        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);

        imagen.setVisibility(v.INVISIBLE);
        texto.setVisibility(v.INVISIBLE);

        SharedPreferences settings1 = PreferenceManager.getDefaultSharedPreferences(getContext());
        ls_empresa     = settings1.getString("empresa","");

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

        String newURL = Constantes.GET_SECCIONES + "?empresa=" + ls_empresa  ;
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

                    JSONArray mensaje = response.getJSONArray("seccion");

                    datos.clear();

                    for(int i = 0; i < mensaje.length(); i++)
                    {JSONObject object = mensaje.getJSONObject(i);
                        secciones sec = new secciones();

                        String id = object.getString("id");

                        sec.setId(id);

                        String ubicacion = object.getString("ubicacion");

                        sec.setUbicacion(ubicacion);

                        String seccion= object.getString("seccion");

                        sec.setSeccion(seccion);

                        String tipo_seccion= object.getString("tipo_seccion");

                        sec.setTipo_seccion(tipo_seccion);

                        String sistema= object.getString("sistema");

                        sec.setSistema(sistema);

                        String cod_tipo_seccion= object.getString("cod_tipo_seccion");

                        sec.setCod_tipo_seccion(cod_tipo_seccion);

                        datos.add(sec);

                    }

                    SeccionAdapter = new seccionAdapter(getContext(),datos);
                    // Setear adaptador a la lista
                    lista.setAdapter(SeccionAdapter);

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_configuracion:
                Intent intent2 = new Intent(getActivity(), activity_preferencias.class);

                getActivity().startActivity(intent2);
                break;

        }

        return super.onOptionsItemSelected(item);
    }


}
