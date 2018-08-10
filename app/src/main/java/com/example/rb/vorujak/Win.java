package com.example.rb.vorujak;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Win extends AppCompatActivity {
boolean isFABOpen=false;
    MediaPlayer mp;
    static boolean play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        Intent j = getIntent();
         String code = j.getStringExtra("code");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("VorujakPref", MODE_PRIVATE);
        TextView t1 = (TextView) findViewById(R.id.score);
        t1.setText("" + pref.getInt("score",0));
        TextView t2 = (TextView) findViewById(R.id.gold);
        t2.setText("" + pref.getInt("gold",0));
         Typeface tf=Typeface.createFromAsset(getAssets(),"BYekan.ttf");
        TextView t3=(TextView)findViewById(R.id.textView3);
        t3.setTypeface(tf);
        TextView aw=(TextView)findViewById(R.id.award);
        aw.setText(code);
        aw.setTypeface(tf);
        play=false;
        mp = MediaPlayer.create(Win.this, R.raw.drones);
        if (play) {
            mp.start();
        } else {
            mp.pause();
        }
    }
    public void fab(View v){
        if(!isFABOpen){
            showFABMenu();
        }else{
            closeFABMenu();
        }
    }
    private void showFABMenu(){
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        isFABOpen=true;
        fab1.animate().translationY(-200);
        fab2.animate().translationY(-400);
        fab3.animate().translationY(-600);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.up);
    }

    private void closeFABMenu(){
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.down);
    }

    @Override
    public void onBackPressed() {
        if(!isFABOpen){
            this.onBackPressed();
            Intent in=new Intent(Win.this,MapsActivity.class);
            startActivity(in);
            finish();
        }else{
            closeFABMenu();
        }
    }
    public void Clickhelp(View v){
        Intent ih=new Intent(Win.this,Help.class);
        startActivity(ih);
        finish();
    }
    public void Clickabout(View v){
        Intent ina=new Intent(Win.this,About.class);
        startActivity(ina);
        finish();
    }
    public void ClickLocation(View v){
        Intent ina=new Intent(Win.this,MapsActivity.class);
        startActivity(ina);
        finish();
    }
    public void sound(View v){
        if(play){
            mp.pause();
            Button b=(Button)findViewById(R.id.button3);
            b.setAlpha(0.5f);
            play=false;
        }else {
            mp.start();
            Button b=(Button)findViewById(R.id.button3);
            b.setAlpha(1f);
            play=true;
        }
    }
    protected void onPause() {
        super.onPause();
        if(play)
            mp.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(play)
            mp.pause();
    }
}
