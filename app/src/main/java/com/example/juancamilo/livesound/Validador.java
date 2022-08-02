package com.example.juancamilo.livesound;

import android.content.Context;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Validador {


    public boolean Val(double impedance,double spks_number, double impedance_spk, Context context,ConstraintLayout cl){

        boolean combinacioncorrecta=true;
        final PopupWindow popUp2;
        String razon="La impedancia nominal de los altavoces y la impedancia de carga dada para el amplificador impiden una configuración que permita la máxima tranferencia de potencia.";



        if(impedance==2 && spks_number==1 && impedance_spk==4){
            combinacioncorrecta=false;
            razon="La impedancia nominal del altavoz es mayor a la impedancia de carga dada para el amplificador.";

        }

        if(impedance==2 && spks_number==1 && impedance_spk==8){
            combinacioncorrecta=false;
            razon="La impedancia nominal del altavoz es mayor a la impedancia de carga dada para el amplificador.";
        }


        if(impedance==2 && spks_number==2 && impedance_spk==2){
            combinacioncorrecta=false;
        }

        if(impedance==2 && spks_number==2 && impedance_spk==8){
            combinacioncorrecta=false;
        }

        if(impedance==2 && spks_number==4 && impedance_spk==4){
            combinacioncorrecta=false;
        }

        if(impedance==2 && spks_number==8 && impedance_spk==2){
            combinacioncorrecta=false;
        }

        if(impedance==2 && spks_number==8 && impedance_spk==8){
            combinacioncorrecta=false;
        }

        if(impedance==4 && spks_number==1 && impedance_spk==2){
            combinacioncorrecta=false;
            razon="La impedancia nominal del altavoz es menor a la impedancia de carga dada para el amplificador.";
        }

        if(impedance==4 && spks_number==1 && impedance_spk==8){
            combinacioncorrecta=false;
            razon="La impedancia nominal del altavoz es mayor a la impedancia de carga dada para el amplificador.";
        }

        if(impedance==4 && spks_number==2 && impedance_spk==4){
            combinacioncorrecta=false;
        }

        if(impedance==4 && spks_number==2 && impedance_spk==8){
            combinacioncorrecta=false;
        }

        if(impedance==4 && spks_number==4 && impedance_spk==2){
            combinacioncorrecta=false;
        }

        if(impedance==4 && spks_number==4 && impedance_spk==8){
            combinacioncorrecta=false;
        }

        if(impedance==4 && spks_number==8 && impedance_spk==4){
            combinacioncorrecta=false;
        }

        if(impedance==8 && spks_number==1 && impedance_spk==2){
            combinacioncorrecta=false;
            razon="La impedancia nominal del altavoz es menor a la impedancia de carga dada para el amplificador.";
        }

        if(impedance==8 && spks_number==1 && impedance_spk==4){
            combinacioncorrecta=false;
            razon="La impedancia nominal del altavoz es menor a la impedancia de carga dada para el amplificador.";
        }

        if(impedance==8 && spks_number==2 && impedance_spk==2){
            combinacioncorrecta=false;
        }

        if(impedance==8 && spks_number==2 && impedance_spk==8){
            combinacioncorrecta=false;
        }

        if(impedance==8 && spks_number==4 && impedance_spk==4){
            combinacioncorrecta=false;
        }

        if(impedance==8 && spks_number==8 && impedance_spk==2){
            combinacioncorrecta=false;
        }

        if(impedance==8 && spks_number==8 && impedance_spk==8){
            combinacioncorrecta=false;
        }



        if(!combinacioncorrecta) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View customView = inflater.inflate(R.layout.custom_layout, null);
            popUp2 = new PopupWindow(
                    customView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            if (Build.VERSION.SDK_INT >= 21) {
                popUp2.setElevation(5.0f);
            }

            TextView txtv = customView.findViewById(R.id.tv);

            ImageButton closeButton = customView.findViewById(R.id.ib_close);


            txtv.setText(razon);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Dismiss the popup window
                    popUp2.dismiss();
                }
            });
            popUp2.showAtLocation(cl, Gravity.CENTER, 0, 0);
        }



        return combinacioncorrecta;
    }
}
