package com.rosario.hp.goldrules.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rosario.hp.goldrules.Entidades.procedimiento;
import com.rosario.hp.goldrules.R;
import com.rosario.hp.goldrules.include.Constantes;
import com.rosario.hp.goldrules.include.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class reglasFragment extends Fragment {

    private ImageView imagen_regla;

    private TextView regla;
    private CircleImageView ok;
    private CircleImageView cancel;
    static String TAG = "Reglas";
    private ArrayList<procedimiento> procedimientos;
    private String ls_cod_empleado;
    private String ls_cod_maquina;
    private String procedimientoid;
    private JsonObjectRequest myRequest;
    private ArrayList<procedimiento> procmax =new ArrayList<>();

    public reglasFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_regla1, container, false);

        imagen_regla = v.findViewById(R.id.imagen_regla);

        regla = v.findViewById(R.id.text_regla);

        ok = v.findViewById(R.id.button_ok);

        cancel = v.findViewById(R.id.buttonCancelar);

        //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        //ls_cod_empleado     = settings.getString("cod_empleado","");

        ls_cod_empleado = "1";

        ls_cod_maquina = "1";

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        cantidad_en_curso();

        return v;

    }

    private void cantidad_en_curso() {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("empleado", ls_cod_empleado);
        map.put("maquina", ls_cod_maquina);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);


        // Depurando objeto Json...
        Log.d(TAG, jobject.toString());

        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), "utf-8"));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), "utf-8"));
                encodedParams.append('&');
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + "utf-8", uee);
        }

        encodedParams.setLength(Math.max(encodedParams.length() - 1, 0));

        String newURL = Constantes.GET_CANT_EN_CURSO  + "?" + encodedParams;

        Log.d("cant_curso",newURL);

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        newURL,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesa la respuesta GET_BY_ID
                                procesarRespuestaGetCantidad(response,getApplicationContext());
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
    }

    private void procesarRespuestaGetCantidad(JSONObject response, Context context) {

        try {
            // Obtener estado
            String estado = response.getString("estado");

            switch (estado) {
                case "1":
                    // Mostrar mensaje
                    JSONArray mensaje1 = response.getJSONArray("procedimiento");
                    // Parsear con Gson

                    String cantidad = "";
                    String id = "";

                    for(int i = 0; i < mensaje1.length(); i++)
                    {
                        JSONObject object = mensaje1.getJSONObject(i);

                        cantidad = object.getString("cantidad");

                        id = object.getString("id");


                    }

                    if(cantidad.equals("0")){
                        agregar_procedimiento();
                    }

                    break;

                case "2":
                    // Mostrar mensaje

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

    private void agregar_procedimiento() {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("empleado", ls_cod_empleado);
        map.put("maquina", ls_cod_maquina);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);


        // Depurando objeto Json...
        Log.d(TAG, jobject.toString());

        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), "utf-8"));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), "utf-8"));
                encodedParams.append('&');
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + "utf-8", uee);
        }

        encodedParams.setLength(Math.max(encodedParams.length() - 1, 0));

        String newURL = Constantes.INSERT_PROCEDIMIENTO + "?" + encodedParams;

        Log.d("agregar_procedimiento", newURL);

        // Actualizar datos en el servidor
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(
                myRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        newURL,
                        //jobject,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor

                                procesarRespuestaProcedimiento(response, getApplicationContext());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());

                            }
                        }

                ) {

                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                }

        );
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                5,//DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void procesarRespuestaProcedimiento(JSONObject response, Context context) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1":

                    cargarDatos_procedimientoMax(getApplicationContext());
                    break;

                case "2":
                    // Mostrar mensaje
                    Toast.makeText(
                            context,
                            mensaje,
                            Toast.LENGTH_LONG).show();

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void cargarDatos_procedimientoMax(final Context context) {
        String newURL = Constantes.GET_MAX_PROCEDIMIENTO;
        Log.d(TAG,newURL);

        VolleySingleton.getInstance(context).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        newURL,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesa la respuesta GET_BY_ID
                                procesarRespuestaGetMaxProcedimiento(response, context);
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
    }

    private Void procesarRespuestaGetMaxProcedimiento(JSONObject response, Context context) {

        try {
            // Obtener estado
            String estado = response.getString("estado");

            switch (estado) {
                case "1":
                    procmax.clear();
                    JSONArray mensaje3 = response.getJSONArray("productos");
                    // Parsear con Gson
                    for (int i = 0; i < mensaje3.length(); i++) {
                        JSONObject object = mensaje3.getJSONObject(i);
                        procedimiento Proc = new procedimiento();

                        String id = object.getString("id");

                        Proc.setId(id);

                        procmax.add(Proc);
                    }

                    procedimientoid = String.valueOf(procmax.get(0).getId());

                    break;

                case "2":
                    // Mostrar mensaje

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
        return null;
    }

}
