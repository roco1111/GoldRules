package com.rosario.hp.goldrules;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.provider.Settings;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.rosario.hp.goldrules.Fragment.principalFragment;
import com.rosario.hp.goldrules.include.Constantes;
import com.rosario.hp.goldrules.include.VolleySingleton;
import com.rosario.hp.goldrules.notificaciones.PushNotificationsPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_NOTIFY_NEW_PROMO = "NOTIFY_NEW_PROMO";
    private BroadcastReceiver mNotificationsReceiver;
    private PushNotificationsPresenter mPresenter;
    private int posicion;
    private String posicion_string;
    private int posicion_nue;
    private TextView username;
    private TextView mail;
    String id_firebase;
    private  String ls_username;
    private  String ls_mail;
    private CircleImageView imagen;
    private FirebaseAuth mAuth;
    StorageReference storageRef;
    private FirebaseStorage storage;
    public static final int NOTIFICATION_ID = 1;
    public static final String ARG_ARTICLES_NUMBER = "fragment_presentacion";
    private static final String TAG = "BeaconReferenceApp";


    private activity_comienzo monitoringActivity = null;
    private String cumulativeLog = "";
    @SuppressLint("InlinedApi")
    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

    @SuppressLint("InlinedApi")
    private static final int LANDSCAPE_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
    private boolean mAutoRotation = false;

    public void refreshMyData(){
        actualizar_foto();
    }


    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences settings3 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings3.edit();
        posicion_string = settings3.getString("posicion", "0");

        posicion = Integer.parseInt(posicion_string);

        id_firebase = FirebaseInstanceId.getInstance().getToken();
        actualizar_token(id_firebase);

        Fragment fragment = null;

        Bundle args1 = new Bundle();




        if (posicion != 0) {

            switch (posicion) {
                case R.id.nav_home:
                    fragment = new principalFragment();
                    args1.putInt(principalFragment.ARG_ARTICLES_NUMBER, posicion);
                    break;
                default:
                    posicion_string = String.valueOf(R.id.nav_home);

                    editor.putString("posicion",posicion_string);
                    editor.apply();

                    editor.commit();
                    fragment = new principalFragment();
                    args1.putInt(principalFragment.ARG_ARTICLES_NUMBER, R.id.nav_home);
                    break;

            }

                fragment.setArguments(args1);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, fragment)
                        .commit();

        }


    }

    public void sendNotification() {

        // Use NotificationCompat.Builder to set up our notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        //icon appears in device notification bar and right hand corner of notification
       // builder.setSmallIcon(R.drawable.icono_toolbar);

        // This intent is fired when notification is clicked
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Set the intent that will fire when the user taps the notification.
        builder.setContentIntent(pendingIntent);

        // Large icon appears on the left of the notification
        //builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icono_toolbar));

        // Content title, which appears in large type at the top of the notification
        builder.setContentTitle("Se designó un técnico a su solicitud de servicio");

        // Content text, which appears in smaller text below the title
        builder.setContentText("Pedidos de Servicio.");

        // The subtext, which appears under the text on newer devices.
        // This will show-up in the devices with Android 4.2 and above only
        builder.setSubText("Ante cualquier duda solicite información");

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void setToolbar() {


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = null;
        mAutoRotation = Settings.System.getInt(this.getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1;

        /*if (savedInstanceState != null) {
            boolean t = savedInstanceState.getBoolean("restore");
            int s = savedInstanceState.getInt("nAndroids");
        }*/
        setToolbar();

       // drawerLayout =  findViewById(R.id.drawer_layout);
        //NavigationView navigationView =  findViewById(R.id.nav_view);


        SharedPreferences settings1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        SharedPreferences.Editor editor = settings1.edit();

        //ls_username = settings1.getString("nombre","");
        //ls_mail     = settings1.getString("mail","");
        //posicion_string =  settings1.getString("posicion","");

       // imagen = navigationView.getHeaderView(0).findViewById(R.id.imageViewMensaje);

       // username =  navigationView.getHeaderView(0).findViewById(R.id.username);

       // mail =  navigationView.getHeaderView(0).findViewById(R.id.mail) ;

       // imagen.setVisibility(navigationView.getHeaderView(0).VISIBLE);

       // username.setText(ls_username);

      //  mail.setText(ls_mail);



        fragment = new principalFragment();

        Bundle args1 = new Bundle();
        args1.putInt(principalFragment.ARG_ARTICLES_NUMBER, R.id.nav_home);
        fragment.setArguments(args1);

        FirebaseMessaging.getInstance().subscribeToTopic("all");


        if(posicion_string== null){



        posicion_string = String.valueOf(R.id.nav_home);

        editor.putString("posicion",posicion_string);
        editor.apply();

        editor.commit();}

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();

        /*

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;


                        switch (menuItem.getItemId()) {

                            case R.id.nav_home:
                                fragment = new principalFragment();
                                fragmentTransaction = true;
                                break;


                            case R.id.nav_cerrar:
                                cerrar_sesion();

                                break;

                            default:
                                fragment = new principalFragment();
                                fragmentTransaction = true;
                                break;

                        }

                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                        SharedPreferences.Editor editor = settings.edit();

                        posicion = menuItem.getItemId();

                        posicion_string = String.valueOf(posicion);

                        editor.putString("posicion", posicion_string);
                        editor.apply();

                        editor.commit();

                        if (fragmentTransaction) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.main_content, fragment)
                                    .commit();

                            // menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                });*/

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        posicion_nue = Integer.parseInt(savedInstanceState.getString("posicion"));



        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        SharedPreferences.Editor editor = settings.edit();

        posicion_string = String.valueOf(posicion_nue);

        editor.putString("posicion",posicion_string);

        editor.commit();


    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        posicion_string = String.valueOf(posicion);
        savedInstanceState.putString("posicion", posicion_string);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            super.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void actualizar_foto(){
        storage = FirebaseStorage.getInstance();

        if (storageRef == null)
            storageRef = storage.getReference();

        String id_usuario = null;


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        id_usuario = settings.getString("cod_empleado","0");

        String mChild = "empleados/" + id_usuario  + ".jpg";
        final StorageReference filepath = storageRef.child(mChild);

        /*GlideApp.with(getApplicationContext())
                .load(filepath)
                .into(imagen);*/


    }

    private void actualizar_token(String token){
        // TODO: Send any registration to your app's servers.

        String id_usuario;


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        id_usuario = settings.getString("cod_empleado","0");
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("id", id_usuario);
        map.put("id_firebase", token);

        JSONObject jobject = new JSONObject(map);

        // Depurando objeto Json...
        Log.d(TAG, jobject.toString());

        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), "utf-8"));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), "utf-8")).toString();
                encodedParams.append('&');
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + "utf-8", uee);
        }

        encodedParams.setLength(Math.max(encodedParams.length() - 1, 0));

        String newURL = Constantes.UPDATE_TOKEN + "?" + encodedParams;

        // Actualizar datos en el servidor
        VolleySingleton.getInstance(getApplication()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        newURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaActualizar(response);
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
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }
    private void procesarRespuestaActualizar(JSONObject response) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {

                case "2":
                    // Mostrar mensaje
                    Toast.makeText(
                            getApplicationContext(),
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    // Enviar código de falla
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }



    private void cerrar_sesion() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Salir")
                .setMessage("Desea salir de la aplicación?")
                .setNegativeButton(android.R.string.cancel,null)
                .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("posicion", "0");
                        editor.commit();
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.signOut();
                        finish();
                        //Intent intent4 = new Intent(getApplication(),activity_inicio.class);
                        //startActivity(intent4);

                    }
                })
                .show();

    }



}
