package com.rosario.hp.goldrules;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.rosario.hp.goldrules.Fragment.ConfirmDialogFragment;
import com.rosario.hp.goldrules.Fragment.datosUsuarios;

public class insertUsuario extends AppCompatActivity
        implements ConfirmDialogFragment.ConfirmDialogListener {
    private Button btnAceptar = null;
    private Button btnCancelar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_done);
        setToolbar(); // Setear Toolbar como action bar


        Fragment fragment;
        fragment = new datosUsuarios();


        // Creación del fragmento de inserción
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, fragment)
                    .commit();

        }

        btnAceptar = findViewById(R.id.aceptar);
        btnCancelar =  findViewById(R.id.cancelar);


        btnAceptar.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                datosUsuarios datosUsuarios = (datosUsuarios)
                        getSupportFragmentManager().findFragmentById(R.id.main_content);

                if (datosUsuarios != null)

                    if (!datosUsuarios.validar()) {
                        if (datosUsuarios.compara_clave()) {
                            datosUsuarios.guardarUsuario();
                        } else {
                            Toast.makeText(
                                    getApplication(),
                                    "La clave ingresada no coincide con su confirmación",
                                    Toast.LENGTH_LONG).show();
                        }
                    }


            }
        });
        btnCancelar.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {


                finish(); // Finalizar actividad descartando cambios

            }
        });
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {

            ab.setDisplayHomeAsUpEnabled(false);
            ab.setDisplayShowHomeEnabled(false);
            ab.setDisplayShowTitleEnabled(false);
        }

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        datosUsuarios datosUsuarios = (datosUsuarios)
                getSupportFragmentManager().findFragmentByTag("datosUsuarios");

        if (datosUsuarios != null) {
            if (!datosUsuarios.camposVacios())
                datosUsuarios.guardarUsuario();


        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        datosUsuarios datosUsuarios = (datosUsuarios)
                getSupportFragmentManager().findFragmentByTag("datosUsuarios");

        if (datosUsuarios == null) {
            finish(); // Finalizar actividad descartando cambios
        }
    }
}




