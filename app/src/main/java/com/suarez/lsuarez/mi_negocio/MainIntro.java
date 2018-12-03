package com.suarez.lsuarez.mi_negocio;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MainIntro extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    public static int RESULT_LOAD_IMAGE = 1;
    public String cliente_log, nombre_log, tipo_log, zona_log, foto_perfil;
    private SearchView editsearch;
    public TextView nombre_apellido;
    public ImageView ocultar;
    private DB_Login myDb;
    public int tempCont = 0;
    public boolean flag_visible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        myDb = new DB_Login(this);

        /*------------------------------------*\
             BOTON OCULTAR / MOESTRAR LISTA
        \*------------------------------------*/

        ocultar = (ImageView)  findViewById(R.id.ocultar);
        ocultar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                flag_visible = !flag_visible;
                ocultar.setImageResource(flag_visible == true ? R.drawable.ic_down : R.drawable.ic_up);
                findViewById(R.id.portada).setVisibility(flag_visible == true ? View.GONE : View.VISIBLE);
                findViewById(R.id.portada_color).setVisibility(flag_visible == true ? View.GONE : View.VISIBLE);
                findViewById(R.id.photo).setVisibility(flag_visible == true ? View.GONE : View.VISIBLE);
                findViewById(R.id.nombre_apellido).setVisibility(flag_visible == true ? View.GONE : View.VISIBLE);
                findViewById(R.id.tipo_cliente).setVisibility(flag_visible == true ? View.GONE : View.VISIBLE);
                findViewById(R.id.altas_numero).setVisibility(flag_visible == true ? View.GONE : View.VISIBLE);
                findViewById(R.id.altas_texto).setVisibility(flag_visible == true ? View.GONE : View.VISIBLE);
                findViewById(R.id.visitas_numero).setVisibility(flag_visible == true ? View.GONE : View.VISIBLE);
                findViewById(R.id.visitas_texto).setVisibility(flag_visible == true ? View.GONE : View.VISIBLE);
                findViewById(R.id.zona).setVisibility(flag_visible == true ? View.GONE : View.VISIBLE);
                findViewById(R.id.zona_texto).setVisibility(flag_visible == true ? View.GONE : View.VISIBLE);
                findViewById(R.id.imageView_options).setVisibility(flag_visible == true ? View.GONE : View.VISIBLE);
            }

        });
        /*------------------------------------*\
                      CAJA BUSCAR
        \*------------------------------------*/
        editsearch = (SearchView) findViewById(R.id.search);

        int id = editsearch.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) editsearch.findViewById(id);
        textView.setTextColor(Color.WHITE);

        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

          @Override
          public boolean onQueryTextSubmit(String query) {
              editsearch.setVisibility(View.INVISIBLE);
              editsearch.setFocusable(true);
              return false;
          }

          @Override
          public boolean onQueryTextChange(String newText) {
              return false;
          }
      });

        /*------------------------------------*\
                  TRAER DATOS DE LOGIN
        \*------------------------------------*/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                nombre_log  = bundle.getString("nombre_log");
                tipo_log    = bundle.getString("tipo_log");
                zona_log    = bundle.getString("zona_log");
                cliente_log = bundle.getString("cliente_log");

                TextView nombre_apellido_t =  (TextView)findViewById(R.id.nombre_apellido);
                nombre_apellido_t.setText(convierte(nombre_log));

                TextView tipo_cliente_t =  (TextView)findViewById(R.id.tipo_cliente);

                switch (tipo_log) {
                    case "ZDEM":
                        tipo_cliente_t.setText("Empleado / a");
                        break;
                    case "ZDPR":
                        tipo_cliente_t.setText("Promotora");
                        break;
                    case "ZDLI":
                        tipo_cliente_t.setText("Lider");
                        break;
                    default:
                        tipo_cliente_t.setText(tipo_log);
                        break;
                }

                TextView zona_t =  (TextView)findViewById(R.id.zona);
                zona_t.setText(zona_log);

            }catch (Exception ex) {}

        }
        final ImageView photo = (ImageView)findViewById(R.id.photo);

        /* MENU */
        final ImageView imageView_options = (ImageView)findViewById(R.id.imageView_options);
        imageView_options.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        /*------------------------------------*\
                  CARGO FOTO DE PERFIL
        \*------------------------------------*/

        Picasso.with(this).load(R.drawable.logo_profile)
                .transform(new CropCircleTransformation())
                .into((ImageView) findViewById(R.id.photo));

        Cursor res = myDb.getAllData();

        if(res.getCount() != 0){
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                foto_perfil = res.getString(3);
            }
            if(!foto_perfil.isEmpty())

            Picasso.with(this).load(foto_perfil)
                    .transform(new CropCircleTransformation())
                    .into((ImageView) findViewById(R.id.photo));

        }

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(open, RESULT_LOAD_IMAGE);

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Espere..", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent intent = new Intent(MainIntro.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ListView resultsListView = (ListView) findViewById(R.id.lv);

        HashMap<String, String> listaMain = new HashMap<>();

        listaMain.put("NOMBRE APELLIDO 1"+"(201818)", "ENVIADO");
        listaMain.put("NOMBRE APELLIDO 2"+"(201818)", "ENVIADO");
        listaMain.put("NOMBRE APELLIDO 3"+"(201818)", "ENVIADO");
        listaMain.put("NOMBRE APELLIDO 4"+"(201817)", "GUARDADO");
        listaMain.put("NOMBRE APELLIDO 5"+"(201816)", "NO ENVIADO");
        listaMain.put("NOMBRE APELLIDO 6"+"(201816)", "NO ENVIADO");
        listaMain.put("NOMBRE APELLIDO 7"+"(201816)", "GUARDADO");
        listaMain.put("NOMBRE APELLIDO 9"+"(201818)", "ENVIADO");

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.mylist_panel,
                new String[]{"N", "EL", "I", "EV", "EVS"},
                new int[]{R.id.text1, R.id.text2,R.id.icon, R.id.estado_violetta, R.id.estado_semaforo});
        Iterator it = listaMain.entrySet().iterator();

        while (it.hasNext()) {
            HashMap<String, String> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry) it.next();
            resultsMap.put("N", pair.getKey().toString());
            resultsMap.put("EL", pair.getValue().toString());

            switch(pair.getValue().toString()) {
                case "ENVIADO":
                    resultsMap.put("I", String.valueOf(R.drawable.enviado));

                    if(tempCont <= 1){
                        resultsMap.put("EV", "ACEPTADO");
                        resultsMap.put("EVS", String.valueOf(R.drawable.circle_verde));
                    }else if(tempCont == 2){
                        resultsMap.put("EV", "OBSERVADO");
                        resultsMap.put("EVS", String.valueOf(R.drawable.circle_amar));
                    }else if(tempCont == 3){
                        resultsMap.put("EV", "RECHAZADO");
                        resultsMap.put("EVS", String.valueOf(R.drawable.circle_rojo));
                    }
                    tempCont ++;
                    break;
                case "GUARDADO":
                    resultsMap.put("I", String.valueOf(R.drawable.save));
                    //resultsMap.put("EV", "RECHAZADO");
                    resultsMap.put("EVS", String.valueOf(R.drawable.circle_nara));
                    break;
                case "NO ENVIADO":
                    resultsMap.put("I", String.valueOf(R.drawable.edit));
                    //resultsMap.put("EV", "RECHAZADO");
                    resultsMap.put("EVS", String.valueOf(R.drawable.circle_nara));
                    break;
            }

            //if (pair.getValue().toString() > 8) {
            //    resultsMap.put("I", String.valueOf(R.drawable.enviado));
            //} else {
            //    resultsMap.put("I", String.valueOf(R.drawable.edit));
            //}
            listItems.add(resultsMap);
            //Toast.makeText(getApplicationContext(),  String.valueOf(pair.getValue().toString().length()), Toast.LENGTH_LONG).show();
        }
        resultsListView.setAdapter(adapter);

        //Al mantener presionado sobre un item

        resultsListView.setOnItemLongClickListener (new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Lon", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            //Toast.makeText(getApplicationContext(), cliente_log, Toast.LENGTH_SHORT).show();
            myDb = new DB_Login(this);
            myDb.updateFoto(cliente_log, data.getData().toString());

            Picasso.with(this).load(data.getData())
                    .transform(new CropCircleTransformation())
                    .into((ImageView) findViewById(R.id.photo));

            //Picasso.with(this).load(data.getData())
            //        .transform(new BorderedCircleTransform(10, Color.WHITE))
            //        .into((ImageView) findViewById(R.id.photo));
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.uno:
                Toast.makeText(this, "Version ...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.dos:
                myDb.deleteData (cliente_log);
                Intent intent = new Intent(MainIntro.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getBaseContext(), "Cerrando sesión..", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.buscar:
                editsearch.setVisibility(View.VISIBLE);
                editsearch.requestFocus();
                return true;
            default:
                return false;
        }
    }

    public void showPopup(View v)
    {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_main);
        popup.show();
    }

    public static String convierte(String string) {
        if (string == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        StringTokenizer st = new StringTokenizer(string," ");
        while (st.hasMoreElements()) {
            String ne = (String)st.nextElement();
            if (ne.length()>0) {
                builder.append(ne.substring(0, 1).toUpperCase());
                builder.append(ne.substring(1).toLowerCase()); //agregado
                builder.append(' ');
            }
        }
        return builder.toString();
    }

}
