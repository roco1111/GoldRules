package com.rosario.hp.goldrules.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rosario.hp.goldrules.Entidades.procedimiento;
import com.rosario.hp.goldrules.MainQR;
import com.rosario.hp.goldrules.R;
import com.rosario.hp.goldrules.activity_secciones;
import com.rosario.hp.goldrules.include.Constantes;
import com.rosario.hp.goldrules.include.VolleySingleton;
import com.rosario.hp.goldrules.reglas_activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class reglasFragment extends Fragment {

    private ImageView imagen_regla;

    private TextView regla;
    private Button ok;
    private Button cancel;
    static String TAG = "Reglas";
    private ArrayList<procedimiento> procedimientos;
    private String ls_cod_empleado;
    private String ls_cod_maquina;
    private String ls_sistema;
    private String ls_regla;
    private String ls_id_regla;
    private String ls_desc_regla;
    private String procedimientoid;
    private String mail;
    private String ls_tipo_regla;
    private String ls_nom_sistema;
    private String ls_nom_seccion;
    private String ls_tipo_sistema;
    private String ls_texto_observacion;
    private String ls_texto_regla;
    private String ls_tipo_lectura;
    private TextView titulo;
    private TextView texto_regla;
    private EditText observaciones;
    private TextView titulo_observaciones;
    StorageReference storageRef;
    private FirebaseStorage storage;
    private JsonObjectRequest myRequest;
    private ArrayList<procedimiento> procmax =new ArrayList<>();
    boolean lb_terminar;

    public reglasFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_regla1, container, false);

        final Context context = this.getContext();

        imagen_regla = v.findViewById(R.id.imagen_regla);

        regla = v.findViewById(R.id.text_regla);

        titulo = v.findViewById(R.id.titulo_regla);

        observaciones = v.findViewById(R.id.observaciones);

        ok = v.findViewById(R.id.buttonOk);

        cancel = v.findViewById(R.id.buttonCancelar);

        titulo_observaciones = v.findViewById(R.id.titulo_observaciones);

        texto_regla = v.findViewById(R.id.texto_regla);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        ls_cod_empleado     = settings.getString("cod_empleado","");
        mail = settings.getString("mail","");
        ls_tipo_lectura     = settings.getString("tipo_lectura","");
        ls_cod_maquina = settings.getString("seccion","");

        Log.d("maquina",ls_cod_maquina);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar_regla(context);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelar_procedimiento(context);
            }
        });



        sistema_maquina(context);


        return v;

    }

    private void sistema_maquina(final Context context) {

        String newURL = Constantes.GET_SECCION_BY_ID  + "?seccion=" + ls_cod_maquina;

        Log.d("sistema_seccion",newURL);

        VolleySingleton.getInstance(context).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        newURL,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesa la respuesta GET_BY_ID
                                procesarRespuestaSistema_maquina(response,context);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley Maq: " + error.getMessage());
                            }
                        }
                )
        );
    }

    private void procesarRespuestaSistema_maquina(JSONObject response, Context context) {

        try {
            // Obtener estado
            String estado = response.getString("estado");

            switch (estado) {
                case "1":
                    // Mostrar mensaje


                    JSONArray mensaje1 = response.getJSONArray("seccion");
                    // Parsear con Gson
                    JSONObject mJsonObject = mensaje1.getJSONObject(0);

                    ls_sistema = mJsonObject.getString("idsistema");

                    ls_tipo_regla = mJsonObject.getString("tipo_regla");

                    ls_nom_sistema = mJsonObject.getString("sistema");

                    ls_nom_seccion = mJsonObject.getString("nombre");

                    ls_tipo_sistema = mJsonObject.getString("tipo_sistema");



                    ((reglas_activity) getActivity()).setActionBarTitle(ls_tipo_regla);

                    cantidad_en_curso(context);

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

    private void cantidad_en_curso(final Context context) {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("empleado", ls_cod_empleado);
        map.put("seccion", ls_cod_maquina);

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

        VolleySingleton.getInstance(context).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        newURL,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesa la respuesta GET_BY_ID
                                procesarRespuestaGetCantidad(response,context);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley cant curso: " + error.getMessage());
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
                    JSONObject mensaje1 = response.getJSONObject("procedimiento");
                    // Parsear con Gson

                    String cantidad = mensaje1.getString("cantidad");

                    procedimientoid = mensaje1.getString("id");


                    if(cantidad.equals("0")){
                        agregar_procedimiento(context);
                    }else {

                       regla_minima(context);

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

    private void regla_minima(final Context context) {

        String newURL = Constantes.GET_MIN_REGLA  + "?idprocedimiento=" + procedimientoid;

        Log.d("cant_curso",newURL);

        VolleySingleton.getInstance(context).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        newURL,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesa la respuesta GET_BY_ID
                                procesarRespuestaRegla_minima(response,context);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley Maq: " + error.getMessage());
                            }
                        }
                )
        );
    }

    private void procesarRespuestaRegla_minima(JSONObject response, Context context) {

        try {
            // Obtener estado
            String estado = response.getString("estado");

            switch (estado) {
                case "1":
                    // Mostrar mensaje
                    JSONObject mensaje1 = response.getJSONObject("idregla");
                    // Parsear con Gson
                    if(mensaje1.getString("idregla").equals("0")){
                        terminar_procedimiento(context);
                        lb_terminar = true;

                    }else{

                        ls_regla = mensaje1.getString("nro_regla");
                        ls_desc_regla = mensaje1.getString("desc_regla");

                        ls_texto_observacion = mensaje1.getString("texto_observacion");

                        titulo.setText("Regla Nro: " + ls_regla);

                        ls_id_regla = mensaje1.getString("idregla");

                        String ls_regla_max = mensaje1.getString("maxregla");

                        if(Double.valueOf(ls_regla_max).equals(Double.valueOf(ls_id_regla)))
                        {
                            ok.setText("Terminar Procedimiento");
                        }

                        if( ! ls_texto_observacion.equals("null") && ! ls_texto_observacion.equals("")){
                        titulo_observaciones.setText(ls_texto_observacion);}

                        ls_texto_regla = mensaje1.getString("texto_regla");

                        if( ! ls_texto_regla.equals("null")){
                            texto_regla.setVisibility(View.VISIBLE);
                            texto_regla.setText(ls_texto_regla);}
                        else{
                            texto_regla.setVisibility(View.INVISIBLE);
                        }

                        observaciones.setText("");

                        regla.setText(ls_desc_regla);
                        actualizar_imagen_regla();
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

    private void agregar_procedimiento(final Context context) {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("idempleado", ls_cod_empleado);
        map.put("idseccion", ls_cod_maquina);

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
        VolleySingleton.getInstance(context).addToRequestQueue(
                myRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        newURL,
                        //jobject,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor

                                procesarRespuestaProcedimiento(response, context);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley insert: " + error.getMessage());

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

                    cargarDatos_procedimientoMax(context);
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

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("empleado", ls_cod_empleado);
        map.put("seccion", ls_cod_maquina);

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


        String newURL = Constantes.GET_MAX_PROCEDIMIENTO + "?" + encodedParams;;
        Log.d(TAG,newURL);

        VolleySingleton.getInstance(context).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
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
                                Log.d(TAG, "Error Volley max: " + error.getMessage());
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
                    JSONObject mensaje3 = response.getJSONObject("procedimiento");
                    // Parsear con Gson

                    procedimientoid= mensaje3.getString("id");
                    agregar_procedimiento_regla(context);

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

    private void agregar_procedimiento_regla(final Context context) {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("idprocedimiento", procedimientoid);
        map.put("idsistema", ls_sistema);

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

        String newURL = Constantes.INSERT_PROCEDIMIENTO_REGLA + "?" + encodedParams;

        Log.d("agregar_proc_regla", newURL);

        // Actualizar datos en el servidor
        VolleySingleton.getInstance(context).addToRequestQueue(
                myRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        newURL,
                        //jobject,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor

                                procesarRespuestaProcedimientoRegla(response, context);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error insert proc: " + error.getMessage());

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

    private void procesarRespuestaProcedimientoRegla(JSONObject response, Context context) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1":

                    regla_minima(context);

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

       private void actualizar_regla(final Context context) {

        String ls_observaciones;

        ls_observaciones = observaciones.getText().toString();

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("idprocedimiento", procedimientoid);
        map.put("idregla", ls_id_regla);
        map.put("observaciones", ls_observaciones);

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

        String newURL = Constantes.UPDATE_REGLA + "?" + encodedParams;

        Log.d("actualizar_proc_regla", newURL);

        // Actualizar datos en el servidor
        VolleySingleton.getInstance(context).addToRequestQueue(
                myRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        newURL,
                        //jobject,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor

                                procesarRespuestaActualizarRegla(response, context);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, " act regla: " + error.getMessage());

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

    private void procesarRespuestaActualizarRegla(JSONObject response, Context context) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1":

                    Toast.makeText(
                            context,
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    sistema_maquina(context);

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

    private void actualizar_regla_cancelar(final Context context) {

        String ls_observaciones;

        ls_observaciones = observaciones.getText().toString();

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("idprocedimiento", procedimientoid);
        map.put("idregla", ls_id_regla);
        map.put("observaciones", ls_observaciones);

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

        String newURL = Constantes.UPDATE_REGLA_CANCELAR + "?" + encodedParams;

        Log.d("actualizar_proc_regla", newURL);

        // Actualizar datos en el servidor
        VolleySingleton.getInstance(context).addToRequestQueue(
                myRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        newURL,
                        //jobject,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor

                                procesarRespuestaActualizarReglaCancelar(response, context);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, " act regla cancelar: " + error.getMessage());

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

    private void procesarRespuestaActualizarReglaCancelar(JSONObject response, Context context) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1":

                    Toast.makeText(
                            context,
                            mensaje,
                            Toast.LENGTH_LONG).show();

                    break;

                case "2":
                    // Mostrar mensaje
                    Toast.makeText(
                            context,
                            mensaje,
                            Toast.LENGTH_LONG).show();

                    break;
            }

        Intent mainIntent;
            if(ls_tipo_lectura.equals("0")) {
                mainIntent = new Intent().setClass(
                        getActivity(), activity_secciones.class);
            }else{
        mainIntent = new Intent().setClass(
                getActivity(), MainQR.class);}
        startActivity(mainIntent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void cancelar_procedimiento(final Context context) {

       String newURL = Constantes.CANCELAR_PROCEDIMIENTO + "?id=" + procedimientoid;

        Log.d("actualizar_proc_regla", newURL);

        // Actualizar datos en el servidor
        VolleySingleton.getInstance(context).addToRequestQueue(
                myRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        newURL,
                        //jobject,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor

                                procesarRespuestaCancelar(response, context);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, " act regla cnacelar proc: " + error.getMessage());

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

    private void procesarRespuestaCancelar(JSONObject response, Context context) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1":

                    actualizar_regla_cancelar(context) ;

                    break;

                case "2":
                    // Mostrar mensaje
                    Toast.makeText(
                            context,
                            mensaje,
                            Toast.LENGTH_LONG).show();

                    break;
            }

            Intent mainIntent;
            if(ls_tipo_lectura.equals("0")) {
                mainIntent = new Intent().setClass(
                        getActivity(), activity_secciones.class);
            }else{
                mainIntent = new Intent().setClass(
                        getActivity(), MainQR.class);}
            startActivity(mainIntent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void terminar_procedimiento(final Context context) {


        String newURL = Constantes.TERMINAR_PROCEDIMIENTO + "?id=" + procedimientoid;

        Log.d("actualizar_proc_regla", newURL);

        // Actualizar datos en el servidor
        VolleySingleton.getInstance(context).addToRequestQueue(
                myRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        newURL,
                        //jobject,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor

                                procesarRespuestaTerminar(response, context);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "terminar Proc: " + error.getMessage());

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

    private void procesarRespuestaTerminar(JSONObject response, Context context) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1":

                    regla.setText(R.string.terminado);

                    Toast.makeText(
                            context,
                            mensaje,
                            Toast.LENGTH_LONG).show();

                    break;

                case "2":
                    // Mostrar mensaje
                    Toast.makeText(
                            context,
                            mensaje,
                            Toast.LENGTH_LONG).show();

                    break;
            }

            mail_procedimiento(context);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void actualizar_imagen_regla(){
        storage = FirebaseStorage.getInstance();

        if (storageRef == null)
            storageRef = storage.getReference();

        String mChild = "reglas/" + ls_id_regla  + ".jpg";
        Log.d(TAG,mChild);
        final StorageReference filepath = storageRef.child(mChild);

        clearGlideCache();

        Glide.with(getActivity().getApplicationContext())
                .load(filepath)
                .placeholder(R.drawable.ic_photo_default)
                .error(R.drawable.ic_photo_default)
                .fallback(R.drawable.ic_photo_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)

                .centerCrop ()
                .into(imagen_regla);

    }

    public void mail_procedimiento(final Context context){

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("mail", mail);
        map.put("seccion", ls_nom_seccion);
        map.put("sistema", ls_nom_sistema);
        map.put("procedimiento", procedimientoid);

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

        String newURL = Constantes.MAIL_PROCEDIMIENTO + "?" + encodedParams ;

        Log.d("mail",newURL);

        VolleySingleton.getInstance(context).addToRequestQueue(
                myRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        newURL,
                        //jobject,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor

                                procesarMail(response, context);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, " mail proc: " + error.getMessage());

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

    private void procesarMail(JSONObject response, Context context) {
        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1":



                    Toast.makeText(
                            context,
                            mensaje,
                            Toast.LENGTH_LONG).show();

                    Intent mainIntent;
                    if(ls_tipo_lectura.equals("0")) {
                        mainIntent = new Intent().setClass(
                                getActivity(), activity_secciones.class);
                    }else{
                        mainIntent = new Intent().setClass(
                                getActivity(), MainQR.class);}
                    startActivity(mainIntent);

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

    void clearGlideCache()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                Glide.get(getActivity().getApplicationContext()).clearDiskCache();
            }
        }.start();

        Glide.get(getActivity().getApplicationContext()).clearMemory();
    }


}
