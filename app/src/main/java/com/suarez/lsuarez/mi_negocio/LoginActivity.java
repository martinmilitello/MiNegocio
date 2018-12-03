package com.suarez.lsuarez.mi_negocio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.cuenta) EditText cuenta;
    @BindView(R.id.dni) EditText dni;
    @BindView(R.id.btn_login) Button loginButton;
    //@BindView(R.id.ayuda) TextView ayuda;

    String nombre_log, tipo_log, zona_log;
    DB_Login myDb;

    RelativeLayout rellay1, rellay2;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);

        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash


       myDb = new DB_Login(this);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //if(isNetworkConnected())
                //{
                    login();
                //}else{Toast.makeText(getApplicationContext(), "OLVIDE!", Toast.LENGTH_SHORT).show();}
            }
        });

        /*ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "OLVIDE!", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (!imm.isAcceptingText()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }

        //Toast.makeText(getBaseContext(), "Conectando...", Toast.LENGTH_LONG).show();
        getWebsite();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Ingreso invalido", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        if (cuenta.getText().toString().isEmpty() || cuenta.getText().length() < 5) {
            cuenta.setError("Cuenta invalida");
            valid = false;
        } else {
            cuenta.setError(null);
        }

        if (dni.getText().toString().isEmpty() || dni.getText().length() < 5) {
            dni.setError("Dni invalido");
            valid = false;
        } else {
            dni.setError(null);
        }

        return valid;
    }

    private void getWebsite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Document doc = Jsoup.connect("http://repartidores.violettacosmeticos.com.ar:4443/WS_login.php?cliente="+cuenta.getText().toString()+"&dni="+dni.getText().toString()).get();
                    String title = doc.title();
                    builder.append(title);
                } catch (IOException e) {}

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String[] miniArray = builder.toString().split(";");
                        if(!miniArray[0].isEmpty())
                        {
                            Toast.makeText(LoginActivity.this, miniArray[0], Toast.LENGTH_SHORT).show();
                        }
                        else
                        {   nombre_log  = miniArray[1];
                            tipo_log    = miniArray[2];
                            zona_log    = miniArray[3];

                            if(tipo_log.equals("ZDPR") || tipo_log.equals("ZDEM")) {

                            Intent intent = new Intent(LoginActivity.this, MainIntro.class);


                            intent.putExtra("cliente_log", cuenta.getText().toString());
                            intent.putExtra("nombre_log", nombre_log);
                            intent.putExtra("tipo_log", tipo_log);
                            intent.putExtra("zona_log", zona_log.substring(0, 3));

                                boolean isInserted = myDb.insertData
                                        (Integer.valueOf(cuenta.getText().toString()),
                                                Integer.valueOf(dni.getText().toString()),
                                                nombre_log,
                                                "",
                                                zona_log.substring(0, 3));


                                if (isInserted != true)
                                    Toast.makeText(getApplicationContext(), "Error al guardar login en base de datos", Toast.LENGTH_LONG).show();

                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), "Cliente no valido", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
            }
        }).start();
    }

    public String[] getCursor(Cursor cur){
        Cursor cursor = cur;
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("name")));
            cursor.moveToNext();
        }
        cursor.close();
        return names.toArray(new String[names.size()]);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if((info == null || !info.isConnected() || !info.isAvailable())) {
            return  false;
        } else {
            return true;
        }
    }
}