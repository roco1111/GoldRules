package com.rosario.hp.goldrules.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.rosario.hp.goldrules.R;
import com.rosario.hp.goldrules.activity_bloqueos;
import com.rosario.hp.goldrules.activity_preferencias;
import com.rosario.hp.goldrules.declaracion;
import com.rosario.hp.goldrules.reglas_activity;

import java.io.IOException;
import java.util.List;

public class fragment_qr extends Fragment implements SurfaceHolder.Callback {

    private CameraSource cameraSource;
    private SurfaceView cameraView;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private String token = "";
    private String tokenanterior = "";
    private Button configuracion;
    private Button historial;
    private ImageButton btnlinterna;
    Camera.Parameters params;

    Camera camera;
    private CameraManager mCameraManager;
    boolean isFlash = false;
    boolean isOn = false;
    private String mCameraId;


    @Override
    public void onPause(){
        super.onPause();
        //switchFlashLight(isOn);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.activity_qr, container, false);
        cameraView = v.findViewById(R.id.camera_view);

        SurfaceHolder mHolder = cameraView.getHolder();
        mHolder.addCallback(this);



        if (camera == null) {

            camera = Camera.open();

            camera.lock();

            params = camera.getParameters();

            try {
                camera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                camera.release();
                camera = null;
            }

        }
        configuracion = v.findViewById(R.id.buttonConfiguracion);
        historial = v.findViewById(R.id.buttonHistorial);


        this.configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), activity_preferencias.class);

                getActivity().startActivity(intent2);
                //getActivity().finish();

            }
        });

        this.historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), activity_bloqueos.class);

                getActivity().startActivity(intent2);
                //getActivity().finish();

            }
        });


        initQR();

        btnlinterna = v.findViewById(R.id.imageViewFlash);


        btnlinterna.setOnClickListener(new View.OnClickListener(){

            @Override
            public  void onClick(View v) {

                switchFlashLight(isOn);

            }


        });

        return v;

    }

    public void switchFlashLight(boolean status) {

        if(status) {
            if (camera == null || params == null) {
                return;
            }
            camera.lock();
            params = camera.getParameters();

            camera.cancelAutoFocus();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.startPreview();
            camera.setParameters(params);
            camera.stopPreview();
            isOn = false;
            btnlinterna.setImageResource(R.drawable.flash_of);
        }else{
            if (camera == null || params == null) {
                return;
            }
            camera.lock();

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.cancelAutoFocus();
            //params.setAutoWhiteBalanceLock(false);
            camera.setParameters(params);
            camera.startPreview();
            isOn = true;
            btnlinterna.setImageResource(R.drawable.flash_on);
            camera.unlock();
        }


    }


    public void initQR() {

        // creo el detector qr
        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(getActivity())
                        .setBarcodeFormats(Barcode.ALL_FORMATS)
                        .build();

        // creo la camara
        cameraSource = new CameraSource
                .Builder(getActivity(), barcodeDetector)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        // listener de ciclo de vida de la camara

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                // verifico si el usuario dio los permisos para la camara
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // verificamos la version de ANdroid que sea al menos la M para mostrar
                        // el dialog de la solicitud de la camara
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                // verificamos la version de ANdroid que sea al menos la M para mostrar
                                // el dialog de la solicitud de la camara
                                if (shouldShowRequestPermissionRationale(
                                        Manifest.permission.CAMERA)) ;
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CAMERA);

                            }
                        }
                    }
                    return;
                } else {
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException ie) {
                        Log.e("CAMERA SOURCE", ie.getMessage());
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        // preparo el detector de QR
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }


            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() > 0) {

                    // obtenemos el token
                    token = barcodes.valueAt(0).displayValue.toString();

                    // verificamos que el token anterior no se igual al actual
                    // esto es util para evitar multiples llamadas empleando el mismo token
                    if (!token.equals(tokenanterior)) {

                        // guardamos el ultimo token proceado
                        tokenanterior = token;
                        Log.i("token", token);

                        SharedPreferences settings3 = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = settings3.edit();

                        editor.putString("seccion",token);
                        editor.apply();

                        editor.commit();

                        Intent intent = new Intent(getContext(), declaracion.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);

                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    synchronized (this) {
                                        wait(5000);
                                        // limpiamos el token
                                        tokenanterior = "";
                                    }
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    Log.e("Error", "Waiting didnt work!!");
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                }
            }
        });

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (camera == null) {
            camera = Camera.open();
            params = camera.getParameters();

        }

        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            camera.release();
            camera = null;
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder,int format,int width,int height)   {

        List<Camera.Size> allSizes = params.getSupportedPictureSizes();
        Camera.Size size = allSizes.get(0); // get top size
        for (int i = 0; i < allSizes.size(); i++) {
            if (allSizes.get(i).width > size.width)
                size = allSizes.get(i);
        }
//set max Picture Size
        params.setPreviewSize(size.width, size.height);




    }

}
