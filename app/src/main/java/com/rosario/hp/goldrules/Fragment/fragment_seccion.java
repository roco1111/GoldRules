package com.rosario.hp.goldrules.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.rosario.hp.goldrules.R;
import com.rosario.hp.goldrules.include.Constantes;
import com.rosario.hp.goldrules.include.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class fragment_seccion extends Fragment {

    private static final String TAG = fragment_seccion.class.getSimpleName();
    private JsonObjectRequest myRequest;
    private TextView tv_titulo;
    private TextView tv_empresa;
    private TextView tv_ubicacion;
    private TextView tv_sistema;
    private TextView tv_tipo;
    private ImageView iv_seccion;
    private String maquina;
    public fragment_seccion(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_seccion, container, false);

        tv_titulo = v.findViewById(R.id.textViewTitulo);
        tv_empresa = v.findViewById(R.id.textViewEmpresa);
        tv_ubicacion = v.findViewById(R.id.textViewUbicacion);
        tv_sistema = v.findViewById(R.id.textViewSistemas);
        tv_tipo = v.findViewById(R.id.textViewTipos);
        iv_seccion = v.findViewById(R.id.imageViewSeccion);

        SharedPreferences settings1 = PreferenceManager.getDefaultSharedPreferences(getContext());
        maquina     = settings1.getString("seccion","");

        cargarDatos(getContext());

        return v;

    }

    public void cargarDatos(final Context context) {

        // Añadir parámetro a la URL del web service
        String newURL = Constantes.GET_SECCION_BY_ID + "?seccion=" + maquina;
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
                                procesarRespuestMaquina(response, context);
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

    private void procesarRespuestMaquina(JSONObject response, Context context) {

        try {
            // Obtener atributo "mensaje"
            String estado = response.getString("estado");

            switch (estado) {
                case "1":
                    // Obtener objeto "cliente"
                    JSONArray mensaje = response.getJSONArray("seccion");

                    for(int i = 0; i < mensaje.length(); i++) {
                        JSONObject object = mensaje.getJSONObject(i);

                        String nombre = object.getString("nombre");

                        tv_titulo.setText(nombre);

                        String sistema = object.getString("sistema");

                        tv_sistema.setText( sistema);

                        String empresa = object.getString("empresa");

                        tv_empresa.setText( empresa);

                        String ubicacion = object.getString("ubicacion");

                        tv_ubicacion.setText( ubicacion);

                        String tipo = object.getString("tipo_seccion");

                        tv_tipo.setText( tipo);

                        String id_tipo = object.getString("id_tipo");

                        switch (id_tipo) {
                            case "1":
                                iv_seccion.setImageDrawable(context.getResources().getDrawable(R.drawable.maquina_grande));
                                break;
                            case "2":
                                iv_seccion.setImageDrawable(context.getResources().getDrawable(R.drawable.tablero_grande));
                                break;
                            case "3":
                                iv_seccion.setImageDrawable(context.getResources().getDrawable(R.drawable.zona_grande));
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