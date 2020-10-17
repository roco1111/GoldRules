package com.rosario.hp.goldrules.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.rosario.hp.goldrules.R;
import com.rosario.hp.goldrules.WebActivity;
import com.rosario.hp.goldrules.activity_comienzo;
import com.rosario.hp.goldrules.include.Constantes;
import com.rosario.hp.goldrules.include.VolleySingleton;
import com.rosario.hp.goldrules.insertUsuario;
import com.rosario.hp.goldrules.reglas_activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class fragment_declaracion extends Fragment {

    private static final String TAG = fragment_declaracion.class.getSimpleName();
    private TextView declaracion;
    private Button aceptar;
    private String ls_declaracion;
    private String ls_cod_maquina;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.activity_declaracion, container, false);

        declaracion = v.findViewById(R.id.textDeclaracion);

        aceptar = v.findViewById(R.id.buttonAceptar);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        ls_cod_maquina = settings.getString("seccion","");

        this.aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), reglas_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        sistema_maquina(getContext());
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

                    ls_declaracion = mJsonObject.getString("texto_declaracion");

                    declaracion.setText(ls_declaracion);


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


}
