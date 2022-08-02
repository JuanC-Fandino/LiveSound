package com.example.juancamilo.livesound;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    Button btn1,btn2;
    Context mContext;
    PopupWindow popUp;

    Spinner sItems = null,sItems2 = null,sItems3 = null;

    Integer[] speakers_number = new Integer[]{1,2,4,8};

    Integer[] impedance_value = new Integer[]{2,4,8};
    Integer spks_number, impedance, impedance_spk,potencia_total,potenciareq;
    Double SPLxdis,SPL1m;
    EditText sensibilidad, distancia, potenciaamp,potenciaspk;
    PhotoView iv ;
    TextView tv1,tv2,tv3;
    Boolean se_cumplen_condiciones,combinacioncorrecta;
    String strNivelPotencia,strNivelPotenciaSpk,strSensibilidad,strDistancia,answer1,answer2;
    ConstraintLayout constraintLayout;
    Animator mCurrentAnimator;
    int mShortAnimationDuration,imagen;
    ScrollView scroll;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.calculate);
        btn2 = findViewById(R.id.calculate2);
        iv = findViewById(R.id.imageView);
        tv1 = findViewById(R.id.textView11); //A 1 metro
        tv2 = findViewById(R.id.textView10);  //A determinada distancia
        tv3 = findViewById(R.id.textView9); // Titulo Imagen
        scroll = findViewById(R.id.sv);

        distancia = findViewById(R.id.editText5);
        sensibilidad = findViewById(R.id.editText2);
        potenciaamp = findViewById(R.id.editText4);
        potenciaspk = findViewById(R.id.editText);
        constraintLayout = findViewById(R.id.cl);
        linear = findViewById(R.id.mylin);


        mContext = getApplicationContext();
        populateSpinner();

        Typeface font = Typeface.createFromAsset(getAssets(), "font.ttf");
        btn1.setTypeface(font);
        btn2.setTypeface(font);
    }

    public void onClick(View v) {

        int ID = v.getId();

        if (ID == R.id.imageView) {
            //zoomImageFromThumb(iv, imagen);
            mShortAnimationDuration = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

        }

        if (ID == R.id.calculate2) {

            scroll.setVisibility(View.VISIBLE);
            linear.setVisibility(View.GONE);
            tv1.setVisibility(View.INVISIBLE);
            tv2.setVisibility(View.INVISIBLE);
            tv3.setVisibility(View.INVISIBLE);
            iv.setVisibility(View.INVISIBLE);
            btn2.setVisibility(View.INVISIBLE);

        }

        if (ID == R.id.calculate) {

            strNivelPotencia = potenciaamp.getText().toString();
            strNivelPotenciaSpk = potenciaspk.getText().toString();
            strSensibilidad = sensibilidad.getText().toString();
            strDistancia = distancia.getText().toString();



            if(TextUtils.isEmpty(strNivelPotencia)) {
                potenciaamp.setError("La potencia no puede estar vacía");
                se_cumplen_condiciones=false;
                return;
            } else {se_cumplen_condiciones= true;}

            if(TextUtils.isEmpty(strNivelPotenciaSpk)) {
                potenciaspk.setError("La potencia no puede estar vacía");
                se_cumplen_condiciones=false;
                return;
            } else {se_cumplen_condiciones= true;}

            if(TextUtils.isEmpty(strSensibilidad)) {
                sensibilidad.setError("La sensibilidad no puede estar vacía");
                se_cumplen_condiciones=false;
                return;
            } else {se_cumplen_condiciones= true;}

            if(TextUtils.isEmpty(strDistancia)) {
                distancia.setError("La distancia no puede estar vacía");
                se_cumplen_condiciones=false;
                return;
            } else {se_cumplen_condiciones= true;}



            if(se_cumplen_condiciones) {

                Validador vAL = new Validador();
                combinacioncorrecta = vAL.Val(impedance,spks_number,impedance_spk,MainActivity.this,constraintLayout);

                if (combinacioncorrecta) {

                    potencia_total = spks_number*(Integer.parseInt(strNivelPotenciaSpk));
                    potenciareq = ((potencia_total*20)/100)+potencia_total;

                    if(potenciareq>(Integer.parseInt(strNivelPotencia))){

                        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                        View customView = inflater.inflate(R.layout.custom_layout,null);
                        popUp = new PopupWindow(
                                customView,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        if(Build.VERSION.SDK_INT>=21){
                            popUp.setElevation(5.0f);
                        }

                        TextView txtv = customView.findViewById(R.id.tv);
                        ImageButton closeButton = customView.findViewById(R.id.ib_close);


                        txtv.setText(getString(R.string.errorpotencia)+String.valueOf(potenciareq)+" W");
                        closeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                popUp.dismiss();
                            }
                        });
                                popUp.showAtLocation(constraintLayout,Gravity.CENTER,0,0);
                    }

                    if(spks_number==1){
                        SPL1m = Double.parseDouble(sensibilidad.getText().toString()) + (10*Math.log10(Double.parseDouble(potenciaamp.getText().toString())));
                        SPLxdis = SPL1m -  (20*Math.log10( Double.parseDouble(distancia.getText().toString()) ));
                        answer1 = String.format(Locale.getDefault(),"%.2f dB", SPL1m);
                        answer2 = String.format(Locale.getDefault(),"%.2f dB", SPLxdis);
                    }

                    if(spks_number==2){
                        SPL1m = Double.parseDouble(sensibilidad.getText().toString()) + (10*Math.log10(Double.parseDouble(potenciaamp.getText().toString())));
                        SPL1m = 20*Math.log10(Math.pow(10,SPL1m/20)+Math.pow(10,SPL1m/20));

                        SPLxdis = SPL1m -  (20*Math.log10( Double.parseDouble(distancia.getText().toString()) ));
                        //SPLxdis = 20*Math.log10(Math.pow(10,SPLxdis/20)+Math.pow(10,SPLxdis/20));

                        answer1 = String.format(Locale.getDefault(),"%.2f dB", SPL1m);
                        answer2 = String.format(Locale.getDefault(),"%.2f dB", SPLxdis);
                    }

                    if(spks_number==4){
                        SPL1m = Double.parseDouble(sensibilidad.getText().toString()) + (10*Math.log10(Double.parseDouble(potenciaamp.getText().toString())));
                        SPL1m = 20*Math.log10(Math.pow(10,SPL1m/20)+Math.pow(10,SPL1m/20)+Math.pow(10,SPL1m/20)+Math.pow(10,SPL1m/20));

                        SPLxdis = SPL1m -  (20*Math.log10( Double.parseDouble(distancia.getText().toString()) ));
                        //SPLxdis = 20*Math.log10(Math.pow(10,SPLxdis/20)+Math.pow(10,SPLxdis/20)+Math.pow(10,SPLxdis/20)+Math.pow(10,SPLxdis/20));

                        answer1 = String.format(Locale.getDefault(),"%.2f dB", SPL1m);
                        answer2 = String.format(Locale.getDefault(),"%.2f dB", SPLxdis);
                    }

                    if(spks_number==8){
                        SPL1m = Double.parseDouble(sensibilidad.getText().toString()) + (10*Math.log10(Double.parseDouble(potenciaamp.getText().toString())));
                        SPL1m = 20*Math.log10(Math.pow(10,SPL1m/20)+Math.pow(10,SPL1m/20)+Math.pow(10,SPL1m/20)+Math.pow(10,SPL1m/20)+Math.pow(10,SPL1m/20)+Math.pow(10,SPL1m/20)+Math.pow(10,SPL1m/20)+Math.pow(10,SPL1m/20));

                        SPLxdis = SPL1m -  (20*Math.log10( Double.parseDouble(distancia.getText().toString()) ));
                        //SPLxdis = 20*Math.log10(Math.pow(10,SPLxdis/20)+Math.pow(10,SPLxdis/20)+Math.pow(10,SPLxdis/20)+Math.pow(10,SPLxdis/20)+Math.pow(10,SPLxdis/20)+Math.pow(10,SPLxdis/20)+Math.pow(10,SPLxdis/20)+Math.pow(10,SPLxdis/20));

                        answer1 = String.format(Locale.getDefault(),"%.2f dB", SPL1m);
                        answer2 = String.format(Locale.getDefault(),"%.2f dB", SPLxdis);
                    }


                    tv1.setText("SPL a 1 m: "+answer1);
                    tv2.setText("SPL a "+ distancia.getText().toString()+" m: "+answer2);
                    tv1.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.VISIBLE);
                    tv3.setVisibility(View.VISIBLE);


                    if (impedance == 2) {
                        if (spks_number==1 && impedance_spk==2) {
                            iv.setVisibility(View.VISIBLE);
                            imagen=R.drawable.i212;
                            iv.setImageResource(R.drawable.i212);
                        }
                        if (spks_number==2 && impedance_spk==4) {
                            iv.setVisibility(View.VISIBLE);
                            imagen=R.drawable.i224;
                            iv.setImageResource(R.drawable.i224);
                        }
                        if (spks_number==4 && impedance_spk==2) {
                            iv.setVisibility(View.VISIBLE);
                            imagen=R.drawable.i242;
                            iv.setImageResource(R.drawable.i242);
                        }
                        if (spks_number==4 && impedance_spk==8) {
                            iv.setVisibility(View.VISIBLE);
                            imagen=R.drawable.i248;
                            iv.setImageResource(R.drawable.i248);
                        }
                        if (spks_number==8 && impedance_spk==4) {
                            iv.setVisibility(View.VISIBLE);
                            imagen=R.drawable.i284;
                            iv.setImageResource(R.drawable.i284);
                        }
                    }


                    if (impedance == 4) {
                        if (spks_number==1 && impedance_spk==4) {
                            iv.setVisibility(View.VISIBLE);
                            imagen=R.drawable.i414;
                            iv.setImageResource(R.drawable.i414);
                        }
                        if (spks_number==2 && impedance_spk==2) {
                            iv.setVisibility(View.VISIBLE);
                            imagen=R.drawable.i422;
                            iv.setImageResource(R.drawable.i422);
                        }
                        if (spks_number==2 && impedance_spk==8) {
                            iv.setVisibility(View.VISIBLE);
                            imagen=R.drawable.i428;
                            iv.setImageResource(R.drawable.i428);
                        }
                        if (spks_number==4 && impedance_spk==4) {
                            imagen=R.drawable.i444;
                            iv.setVisibility(View.VISIBLE);
                            iv.setImageResource(R.drawable.i444);
                        }
                        if (spks_number==8 && impedance_spk==2) {
                            iv.setVisibility(View.VISIBLE);
                            imagen=R.drawable.i482;
                            iv.setImageResource(R.drawable.i482);
                            //dudoso oso
                        }
                        if (spks_number==8 && impedance_spk==8) {
                            iv.setVisibility(View.VISIBLE);
                            imagen=R.drawable.i488;
                            iv.setImageResource(R.drawable.i488);
                            //dudoso oso
                        }
                    }

                    if (impedance == 8) {
                        if (spks_number==1 && impedance_spk==8) {
                            iv.setVisibility(View.VISIBLE);
                            imagen=R.drawable.i818;
                            iv.setImageResource(R.drawable.i818);
                        }
                        if (spks_number==2 && impedance_spk==4) {
                            iv.setVisibility(View.VISIBLE);
                            imagen=R.drawable.i824;
                            iv.setImageResource(R.drawable.i824);
                        }
                        if (spks_number==4 && impedance_spk==2) {
                            iv.setVisibility(View.VISIBLE);
                            imagen=R.drawable.i842;
                            iv.setImageResource(R.drawable.i842);
                        }
                        if (spks_number==4 && impedance_spk==8) {
                            iv.setVisibility(View.VISIBLE);
                            imagen=R.drawable.i848;
                            iv.setImageResource(R.drawable.i848);
                        }
                        if (spks_number==8 && impedance_spk==4) {
                            iv.setVisibility(View.VISIBLE);
                            imagen=R.drawable.i884;
                            iv.setImageResource(R.drawable.i884);
                        }
                    }
                    btn2.setVisibility(View.VISIBLE);
                    linear.setVisibility(View.VISIBLE);
                    scroll.setVisibility(View.GONE);


                }


            }


        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        if (parent.getId() == R.id.spinner) {
            spks_number = Integer.parseInt(parent.getItemAtPosition(pos).toString());
        }

        if (parent.getId() == R.id.spinneramp) {
            impedance = Integer.parseInt(parent.getItemAtPosition(pos).toString());
        }

        if (parent.getId() == R.id.spnimpedancespk) {
            impedance_spk = Integer.parseInt(parent.getItemAtPosition(pos).toString());
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    private void populateSpinner (){

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,speakers_number);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,impedance_value);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sItems = findViewById(R.id.spinner);
        sItems.setAdapter(adapter);
        sItems.setOnItemSelectedListener(this);

        sItems2 = findViewById(R.id.spinneramp);
        sItems2.setAdapter(adapter2);
        sItems2.setOnItemSelectedListener(this);

        sItems3 = findViewById(R.id.spnimpedancespk);
        sItems3.setAdapter(adapter2);
        sItems3.setOnItemSelectedListener(this);

    }

/*    private void zoomImageFromThumb(final View thumbView, int imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);
        expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.cl)

                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }*/




}
