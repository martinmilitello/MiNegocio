package com.suarez.lsuarez.mi_negocio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DniActivity extends AppCompatActivity {
    public final String ruta_fotos = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/misfotos/";
    public File file = new File(ruta_fotos);
    public Button botonFoto;
    private String imagen_frente = "";
    public ImageView dni_imagen, img_back;
    public TextView text_dni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dni);

        dni_imagen = (ImageView) findViewById(R.id.imagen_dni);
        text_dni = (TextView) findViewById(R.id.text_dni);

        //Traer

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                imagen_frente = bundle.getString("imagen_frente");
                File file = new File(imagen_frente);
                long length = file.length();
                Toast.makeText(getApplicationContext(), imagen_frente, Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
            }
        }


        if(!imagen_frente.isEmpty())
        {
            dni_imagen.setImageResource(R.drawable.dni_dorso);
        }

        img_back = (ImageView) findViewById(R.id.btn_back);

        /*String filepath = Environment.getExternalStorageDirectory() + "/file.png";
        File file = new File(filepath);
        long length = file.length();*/

        botonFoto = (Button) findViewById(R.id.btn_dni);
        file.mkdirs();
        botonFoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String file = ruta_fotos + getCode() + ".jpg";
                File mi_foto = new File(file);
                try {
                    mi_foto.createNewFile();
                    //Toast.makeText(getApplicationContext(), mi_foto.toString(), Toast.LENGTH_SHORT).show();
                } catch (IOException ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }
                //
                Uri uri = Uri.fromFile(mi_foto);
                //Abre la camara para tomar la foto
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //Guarda imagen
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                cameraIntent.putExtra("imagen_frente", mi_foto.toString());
                //Retorna a la actividad
                startActivityForResult(cameraIntent, 100);


                dni_imagen.setImageResource(R.drawable.dni_dorso);
                text_dni.setText("TOMAR FOTO DORSO D.N.I");


            }
        });

    }

    @SuppressLint("SimpleDateFormat")
    private String getCode()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date() );
        String photoCode = "dni_" + date;
        return photoCode;
    }

    public void onBackDni(View view) {
        Intent intent = new Intent(DniActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
