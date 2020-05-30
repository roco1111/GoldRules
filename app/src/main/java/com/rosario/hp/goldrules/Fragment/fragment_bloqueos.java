package com.rosario.hp.goldrules.Fragment;


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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.rosario.hp.goldrules.Entidades.procedimiento;
import com.rosario.hp.goldrules.R;
import com.rosario.hp.goldrules.adapter.procedimientoAdapter;
import com.rosario.hp.goldrules.include.Constantes;
import com.rosario.hp.goldrules.include.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class fragment_bloqueos extends Fragment {
    private procedimientoAdapter ProcedimientoAdapter;

    private static final String TAG = fragment_bloqueos.class.getSimpleName();
    public static final String ARG_ARTICLES_NUMBER = "principal";
    private ArrayList<procedimiento> datos;
    private RecyclerView lista;
    private TextView texto;
    private ImageView imagen;
    private String ls_empleado;
    private RecyclerView.LayoutManager lManager;
    public fragment_bloqueos(){}

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

        imagen.setVisibility(v.INVISIBLE);
        texto.setVisibility(v.INVISIBLE);

        SharedPreferences settings1 = PreferenceManager.getDefaultSharedPreferences(getContext());
        ls_empleado     = settings1.getString("cod_empleado","");

        lManager = new LinearLayoutManager(getActivity());
        lista.setLayoutManager(lManager);
        cargarAdaptador();
        return v;
    }

    public void cargarAdaptador() {
        // Petición GET

        String newURL = Constantes.GET_PROCEDIMIENTOS_EMPLEADOS + "?empleado=" + ls_empleado  ;
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

                    for(int i = 0; i < mensaje.length(); i++)
                    {JSONObject object = mensaje.getJSONObject(i);
                        procedimiento pro = new procedimiento();

                        String id = object.getString("id");

                        pro.setId(id);



                        String fecha = object.getString("fecha");

                        pro.setFecha(fecha);

                        String descripcion= object.getString("seccion_corte");

                        pro.setMaquina(descripcion);

                        String empleado= object.getString("empleado");

                        pro.setEmpleado(empleado);

                        String completo= object.getString("completo");

                        pro.setCompleto(completo);

                        String tipo_seccion = object.getString("tipo_seccion");

                        pro.setTipo_seccion(tipo_seccion);

                        String hora = object.getString("hora");

                        pro.setHora(hora);


                        datos.add(pro);

                    }

                    ProcedimientoAdapter = new procedimientoAdapter(getContext(),datos);
                    // Setear adaptador a la lista
                    lista.setAdapter(ProcedimientoAdapter);

                    break;
                case "2": // FALLIDO

                    imagen.setVisibility(getView().VISIBLE);
                    texto.setVisibility(getView().VISIBLE);

                    break;
            }

        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }

    }


}
