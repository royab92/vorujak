package com.example.rb.vorujak;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        TextView txt=(TextView)findViewById(R.id.matn);
        Typeface tf=Typeface.createFromAsset(getAssets(),"BYekan.ttf");
        txt.setTypeface(tf);
        TextView title=(TextView)findViewById(R.id.textView2);
        title.setTypeface(tf);

    }

}
