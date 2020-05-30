package com.rosario.hp.goldrules.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import com.rosario.hp.goldrules.activity_reglas;
import com.rosario.hp.goldrules.include.Constantes;
import com.rosario.hp.goldrules.include.VolleySingleton;
import com.rosario.hp.goldrules.R;

import com.rosario.hp.goldrules.activity_seccion;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class fragment_bloqueo extends Fragment {

    private static final String TAG = fragment_bloqueo.class.getSimpleName();
    private JsonObjectRequest myRequest;
    private TextView tv_titulo;
    private TextView tv_fecha;
    private TextView tv_bloqueo;
    private TextView tv_tiempo;
    private TextView tv_maquina;
    private ImageView iv_completo;
    private String procedimiento;
    private String ls_cod_empleado;
    private String idMaquina;
    private RelativeLayout iv_maquina;
    private RelativeLayout iv_reglas;
    public fragment_bloqueo(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_bloqueo, container, false);

        tv_titulo = v.findViewById(R.id.textViewTitulo);
        tv_fecha = v.findViewById(R.id.textViewFecha);
        tv_tiempo = v.findViewById(R.id.textViewTiempo);
        tv_maquina = v.findViewById(R.id.textViewMaquina);
        iv_completo = v.findViewById(R.id.imageViewCompleto);
        iv_maquina = v.findViewById(R.id.seccion);
        iv_reglas = v.findViewById(R.id.reglas);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        procedimiento     = settings.getString("procedimiento","");

        tv_titulo.setText("Bloqueo Nro:" + procedimiento);

        cargarDatos(getContext());

        this.iv_reglas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(v.getContext(), activity_reglas.class);
                v.getContext().startActivity(intent);

            }
        });

        this.iv_maquina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences settings = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("seccion", idMaquina);
                editor.apply();

                Intent intent = new Intent(v.getContext(), activity_seccion.class);
                v.getContext().startActivity(intent);
                editor.commit();

            }
        });



        return v;

    }

    public void cargarDatos(final Context context) {

        // Añadir parámetro a la URL del web service
        String newURL = Constantes.GET_BY_PROCEDIMIENTO_ID + "?id=" + procedimiento;
        Log.d(TAG,newURL);

        // Realizar petición GET_BY_ID
        VolleySingleton.getInstance(context).addToRequestQueue(
                myRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        newURL,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar respuesta Json
                                procesarRespuestaDatos(response, context);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());

                            }
                        }
                )
        );
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                5,//DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void procesarRespuestaDatos(JSONObject response, Context context) {

        try {
            // Obtener atributo "mensaje"
            String estado = response.getString("estado");

            switch (estado) {
                case "1":
                    // Obtener objeto "cliente"
                    JSONArray mensaje = response.getJSONArray("procedimiento");

                    for(int i = 0; i < mensaje.length(); i++) {
                        JSONObject object = mensaje.getJSONObject(i);




                        idMaquina = object.getString("idseccion");


                        String maquina = object.getString("seccion_corte");
                        tv_maquina.setText( maquina);

                        String fecha = object.getString("fecha");
                        tv_fecha.setText(fecha);

                        String tiempo = object.getString("comienzo");
                        tv_tiempo.setText( tiempo);

                        String completo = object.getString("completo");
                        switch (completo){
                            case "0":
                                iv_completo.setImageDrawable(getContext().getResources().getDrawable(R.drawable.curso));
                                break;
                            case "1":
                                iv_completo.setImageDrawable(getContext().getResources().getDrawable(R.drawable.completo));
                                break;
                            case "2":
                                iv_completo.setImageDrawable(getContext().getResources().getDrawable(R.drawable.incompleto));
                                break;
                            case "3":
                                iv_completo.setImageDrawable(getContext().getResources().getDrawable(R.drawable.cancelado));
                                break;

                        }

                    }

                    break;

                case "2":
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(
                            context,
                            mensaje2,
                            Toast.LENGTH_LONG).show();

                    break;


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}