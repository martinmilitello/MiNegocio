package com.suarez.lsuarez.mi_negocio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends Activity {

    private static final int SPLASH_TIME_OUT = 2000;
    DB_Login myDb;
    public String cliente_log, nombre_log, zona_log, tipo_log;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Context mContext = this;

        ImageView logoImg = findViewById(R.id.company_logo);
        TextView textView = findViewById(R.id.splash_text);

        myDb = new DB_Login(this);

        AlphaAnimation animation = new AlphaAnimation(1.3f, 0.0f);
        animation.setDuration(SPLASH_TIME_OUT);
        animation.setStartOffset(80);
        animation.setFillAfter(true);
        logoImg.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Cursor res = myDb.getAllData();

                if(res.getCount() == 0) {
                    Intent intent = new Intent(Splash.this, LoginActivity.class);
                    startActivity(intent);
                }else {
                    StringBuffer buffer = new StringBuffer();
                    while (res.moveToNext()) {
                        cliente_log = res.getString(0);
                        //buffer.append("Dni :" + res.getString(1) + "\n");
                        nombre_log = res.getString(2);
                        //buffer.append("Foto :" + res.getString(3) + "\n");
                        zona_log = res.getString(4);
                        tipo_log = "ZDRE";
                    }
                    Intent intent = new Intent(Splash.this, MainIntro.class);
                    intent.putExtra("cliente_log", cliente_log);
                    intent.putExtra("nombre_log", nombre_log);
                    intent.putExtra("tipo_log", tipo_log);
                    intent.putExtra("zona_log", zona_log);
                    startActivity(intent);
                }

                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onBackPressed() {
    }
}