package com.suarez.lsuarez.mi_negocio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements Fragment_1.OnFragmentInteractionListener, Fragment_2.OnFragmentInteractionListener, Fragment_3.OnFragmentInteractionListener, Fragment_4.OnFragmentInteractionListener  {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private int pageposicion = 0;
    private ImageButton nextBtn, backBtn;
    private Button finishBtn, closeBtn;
    private ImageView[] indicators;
    private ImageView indicator01, indicator02, indicator03, indicator04;

    //Texto 'Campo obligatorio'
    String campo_obligatorio = "Compo obligatorio";
    //Datos de contacto
    String  s_nombre_postulante, s_apellido_postulante,
            s_estado_civil_postulante, s_email_postulante,
            s_ocupacion_postulante, s_fecha_nacimiento_postulante,
            s_telefono_postulante, s_dni_postulante;

    //Datos de domicilio
    String  s_calle_postulante, s_altura_postulante,
            s_piso_postulante, s_dpto_postulante,
            s_provincia_postulante, s_localidad_postulante,
            s_barrio_postulante, s_cp_postulante,
            s_inquilina_propetaria_postulante, s_habita_postulante;

    //Datos de conyugue + referencias
    String  s_nombre_conyugue, s_apellido_conyugue,
            s_dni_conyugue, s_ocupacion_conyugue,
            s_nombre_apellido_referencia_uno, s_nombre_apellido_referencia_dos,
            s_domicilio_referencia_uno, s_domicilio_referencia_dos,
            s_telefono_referencia_uno, s_telefono_referencia_dos;

    //Datos de indicadora + impresion socio - economica,
    String  s_nombre_indicadora, s_cuenta_indicadora,
            s_premio_indicadora, s_zonasec_indicadora,
            s_descripcion_inmueble_postulante, s_cantidad_ambientes_postulante,
            s_opinion_personal_pv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_formulario);

        indicator01 = (ImageView) findViewById(R.id.indicator_01);
        indicator02 = (ImageView) findViewById(R.id.indicator_02);
        indicator03 = (ImageView) findViewById(R.id.indicator_03);
        indicator04 = (ImageView) findViewById(R.id.indicator_04);
        indicators = new ImageView[]{indicator01, indicator02, indicator03, indicator04};

        nextBtn = (ImageButton) findViewById(R.id.btn_next);
        finishBtn = (Button) findViewById(R.id.btn_finish);

        backBtn = (ImageButton) findViewById(R.id.btn_back);
        closeBtn = (Button) findViewById(R.id.btn_cancel);
        backBtn.setVisibility(pageposicion == 0 ? View.GONE : View.VISIBLE);
        closeBtn.setVisibility(pageposicion == 0 ? View.VISIBLE : View.GONE);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.onboarding_viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        cambiarIndicador(0);

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int posicion) {
                obtenerDatos(mViewPager);
                cambiarIndicador(posicion);
                nextBtn.setVisibility(posicion == 3 ? View.GONE : View.VISIBLE);
                finishBtn.setVisibility(posicion == 3 ? View.VISIBLE : View.GONE);

                backBtn.setVisibility(posicion == 0 ? View.GONE : View.VISIBLE);
                closeBtn.setVisibility(posicion == 0 ? View.VISIBLE : View.GONE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment newInstance(int sectionNumber, Context applicationContext) {

            Fragment fragment = null;
            switch(sectionNumber){
                case 1:
                    fragment = new Fragment_1();
                    break;
                case 2:
                    fragment = new Fragment_2();
                    break;
                case 3:
                    fragment = new Fragment_3();
                    break;
                case 4:
                    fragment = new Fragment_4();
                    break;
            }
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_main_formulario, container, false);

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int posicion) {

            return PlaceholderFragment.newInstance(posicion + 1, getApplicationContext());
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int posicion) {

            switch (posicion) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return "SECTION 4";
            }
            return null;
        }
    }

    public void onNextButton(View view) {
        pageposicion += 1;
        mViewPager.setCurrentItem(pageposicion, true);
    }

    public void onFinishButton(View view) {

        //Toast.makeText(getApplicationContext(), "Guardando..", Toast.LENGTH_SHORT).show();

    }

    public void onBack(View view) {
        pageposicion -= 1;
        mViewPager.setCurrentItem(pageposicion, true);
    }
    public void onCancelButton(View view) {
        new AlertDialog.Builder(this)
                .setTitle("FICHA DE INCORPORACION")
                .setMessage("¿ Desea salir sin guardar ?")
                .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Nada
                    }
                })
                .setNegativeButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), Splash.class);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
    }


    private void cambiarIndicador(int pageposicion) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == pageposicion ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }

    /*------------------------------------*\
             OBTENER DATOS DE FRAGMENT
    \*------------------------------------*/
    private void obtenerDatos(View view) {
        //Obtener datos de contacto :
        try {
            EditText nombre_postulante = (EditText) view.findViewById(R.id.nombre_postulante);
            EditText apellido_postulante = (EditText) view.findViewById(R.id.apellido_postulante);
            EditText fecha_nacimiento = (EditText) view.findViewById(R.id.fecha_nacimiento);
            EditText documento_postulante = (EditText) view.findViewById(R.id.documento_postulante);
            Spinner estadocivil = (Spinner) view.findViewById(R.id.estadocivil);
            EditText email_postulante = (EditText) view.findViewById(R.id.email_postulante);
            EditText telefono_postulante = (EditText) view.findViewById(R.id.telefono_postulante);
            EditText ocupacion_postulante = (EditText) view.findViewById(R.id.ocupacion_postulante);

            s_nombre_postulante = nombre_postulante.getText().toString();
            s_apellido_postulante = apellido_postulante.getText().toString();
            s_fecha_nacimiento_postulante  = fecha_nacimiento.getText().toString();
            s_dni_postulante = documento_postulante.getText().toString();
            s_estado_civil_postulante = estadocivil.getSelectedItem().toString();
            s_email_postulante = email_postulante.getText().toString();
            s_telefono_postulante = telefono_postulante.getText().toString();
            s_ocupacion_postulante = ocupacion_postulante.getText().toString();

            if(nombre_postulante.getText().toString().isEmpty())nombre_postulante.setError(campo_obligatorio);
            else fecha_nacimiento.setError(null);
            if(apellido_postulante.getText().toString().isEmpty())apellido_postulante.setError(campo_obligatorio);
            else fecha_nacimiento.setError(null);
            if(fecha_nacimiento.getText().toString().isEmpty())fecha_nacimiento.setError(campo_obligatorio);
            else fecha_nacimiento.setError(null);
            if(documento_postulante.getText().toString().isEmpty())documento_postulante.setError(campo_obligatorio);
            else fecha_nacimiento.setError(null);
            if(telefono_postulante.getText().toString().isEmpty())telefono_postulante.setError(campo_obligatorio);
            else fecha_nacimiento.setError(null);

        }catch (Exception ex){}

        //Obtener datos de domicilio :
        try {
            EditText calle_postulante = (EditText) view.findViewById(R.id.calle_postulante);
            EditText calle_numero_postulante = (EditText) view.findViewById(R.id.calle_numero_postulante);
            EditText piso_postulante = (EditText) view.findViewById(R.id.piso_postulante);
            EditText dpto_postulante = (EditText) view.findViewById(R.id.dpto_postulante);
            EditText provincia_postulante = (EditText) view.findViewById(R.id.provincia_postulante);
            EditText localidad_postulante = (EditText) view.findViewById(R.id.localidad_postulante);
            EditText barrio_postulante = (EditText) view.findViewById(R.id.barrio_postulante);
            EditText cp_postulante = (EditText) view.findViewById(R.id.cp_postulante);
            Spinner  es_inquilina_propetaria = (Spinner) view.findViewById(R.id.es_inquilina_propetaria);
            EditText habita_desde_postulante = (EditText) view.findViewById(R.id.habita_desde_postulante);

            s_calle_postulante = calle_postulante.getText().toString();
            s_altura_postulante = calle_numero_postulante.getText().toString();
            s_piso_postulante = piso_postulante.getText().toString();
            s_dpto_postulante = dpto_postulante.getText().toString();
            s_provincia_postulante = provincia_postulante.getText().toString();
            s_localidad_postulante = localidad_postulante.getText().toString();
            s_barrio_postulante = barrio_postulante.getText().toString();
            s_cp_postulante = cp_postulante.getText().toString();
            s_inquilina_propetaria_postulante = es_inquilina_propetaria.getSelectedItem().toString();
            s_habita_postulante = habita_desde_postulante.getText().toString();

            if(calle_postulante.getText().toString().isEmpty())calle_postulante.setError(campo_obligatorio);

        }catch (Exception ex){}

        //Obtener datos de conyugue y referencias :
        try {
            EditText nombre_conyugue = (EditText) view.findViewById(R.id.nombre_conyugue);
            EditText apellido_conyugue = (EditText) view.findViewById(R.id.apellido_conyugue);
            EditText documento_conyugue = (EditText) view.findViewById(R.id.documento_conyugue);
            EditText ocupacion_conyugue = (EditText) view.findViewById(R.id.ocupacion_conyugue);

            EditText nombre_uno_referencia = (EditText) view.findViewById(R.id.nombre_uno_referencia);
            EditText nombre_dos_referencia = (EditText) view.findViewById(R.id.nombre_dos_referencia);

            EditText domicilio_uno_referencia = (EditText) view.findViewById(R.id.domicilio_uno_referencia);
            EditText domicilio_dos_referencia = (EditText) view.findViewById(R.id.domicilio_dos_referencia);

            EditText telefono_uno_referencia = (EditText) view.findViewById(R.id.telefono_uno_referencia);
            EditText telefono_dos_referencia = (EditText) view.findViewById(R.id.telefono_dos_referencia);

            s_nombre_conyugue = nombre_conyugue.getText().toString();
            s_apellido_conyugue = apellido_conyugue.getText().toString();
            s_dni_conyugue = documento_conyugue.getText().toString();
            s_ocupacion_conyugue = ocupacion_conyugue.getText().toString();
            s_nombre_apellido_referencia_uno = nombre_uno_referencia.getText().toString();
            s_nombre_apellido_referencia_dos = nombre_dos_referencia.getText().toString();
            s_domicilio_referencia_uno = domicilio_uno_referencia.getText().toString();
            s_domicilio_referencia_dos = domicilio_dos_referencia.getText().toString();
            s_telefono_referencia_uno = telefono_uno_referencia.getText().toString();
            s_telefono_referencia_dos = telefono_dos_referencia.getText().toString();
        }catch (Exception ex){}

        //Datos de indicadora :
        try {
            EditText nombre_indicadora = (EditText) view.findViewById(R.id.nombre_indicadora);
            EditText cuenta_indicadora = (EditText) view.findViewById(R.id.cuenta_indicadora);
            EditText zona_seccion_indicadora = (EditText) view.findViewById(R.id.zona_seccion_indicadora);
            EditText descripcion_inmueble_postulante = (EditText) view.findViewById(R.id.descripcion_inmueble_postulante);
            EditText cantidad_ambientes_postulante = (EditText) view.findViewById(R.id.cantidad_ambientes_postulante);
            EditText opinion_pv = (EditText) view.findViewById(R.id.opinion_pv);

            s_nombre_indicadora = nombre_indicadora.getText().toString();
            s_cuenta_indicadora = cuenta_indicadora.getText().toString();
            s_premio_indicadora = zona_seccion_indicadora.getText().toString();
            s_zonasec_indicadora = descripcion_inmueble_postulante.getText().toString();
            s_descripcion_inmueble_postulante = cantidad_ambientes_postulante.getText().toString();
            s_cantidad_ambientes_postulante = cantidad_ambientes_postulante.getText().toString();
            s_opinion_personal_pv = opinion_pv.getText().toString();

            //if(opinion_pv.getText().toString().isEmpty())opinion_pv.setError(campo_obligatorio);

        }catch (Exception ex){}

    }
}
