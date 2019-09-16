package com.rosario.hp.goldrules;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;

import android.view.MenuItem;

import com.rosario.hp.goldrules.Fragment.fragment_presentacion;

public class activity_comienzo extends AppCompatActivity {

    private int posicion;
    private String posicion_string;
    private int posicion_nue;

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences settings3 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        posicion_string = settings3.getString("posicion", "0");

        posicion = Integer.parseInt(posicion_string);

        Fragment fragment = null;

        Bundle args1 = new Bundle();


        if (posicion != 0) {

            switch (posicion) {
                case R.id.nav_ingreso:
                    fragment = new fragment_presentacion();
                    args1.putInt(fragment_presentacion.ARG_ARTICLES_NUMBER, posicion);
                    break;


            }


            fragment.setArguments(args1);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, fragment)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        posicion_string = String.valueOf(posicion);
        savedInstanceState.putString("posicion", posicion_string);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_main_inicial);

        //drawerLayout =  findViewById(R.id.drawer_layout);
        //NavigationView navigationView =  findViewById(R.id.nav_view);

        Fragment fragment = null;
        fragment = new fragment_presentacion();


        Bundle args1 = new Bundle();
        args1.putInt(fragment_presentacion.ARG_ARTICLES_NUMBER, R.id.nav_ingreso);
        fragment.setArguments(args1);

        //navigationView.setItemIconTintList(null);

        SharedPreferences settings1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        SharedPreferences.Editor editor = settings1.edit();

        posicion_string = String.valueOf(R.id.nav_ingreso);

        editor.putString("posicion",posicion_string);
        editor.apply();

        editor.commit();



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
