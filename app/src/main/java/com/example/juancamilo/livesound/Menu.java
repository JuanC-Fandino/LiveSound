package com.example.juancamilo.livesound;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    Button btn0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btn0 = findViewById(R.id.button);
        Typeface font = Typeface.createFromAsset(getAssets(), "font.ttf");
        btn0.setTypeface(font);

    }

    public void onClick(View v) {

        int ID = v.getId();

        if (ID == R.id.button) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }
    }


}
