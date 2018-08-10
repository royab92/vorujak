package com.example.rb.vorujak;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class TakeAward extends AppCompatActivity {
    MediaPlayer main;
    SharedPreferences pref;
    int award;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_award);
        pref=getApplicationContext().getSharedPreferences("VorujakPref",0);
        SharedPreferences.Editor editor=pref.edit();
        award=pref.getInt("closeAward",0);
        main= MediaPlayer.create(TakeAward.this, R.raw.spell);
        main.start();
        int typ=0;
        typ=pref.getInt("atype"+award,0);
        int po=pref.getInt("potion",0);
        int go=pref.getInt("gold",0);
        int ar=pref.getInt("armor",0);
        int fi=pref.getInt("fire",0);
        count=pref.getInt("awardcount",0);
        ImageView ima=(ImageView)findViewById(R.id.awa);
        if(typ==0)
        {ima.setImageResource(R.drawable.potion);
        editor.putInt("potion",(po+1));
        editor.apply();
        }
        if(typ==1)
        {ima.setImageResource(R.drawable.armor);
            editor.putInt("armor",(ar+1));
            editor.apply();}
        if(typ==2)
        {ima.setImageResource(R.drawable.gold);
            editor.putInt("gold",(go+1));
            editor.apply();}
        if(typ==3)
        {ima.setImageResource(R.drawable.fire);
            editor.putInt("star",(fi+1));
            editor.apply();}
        count=count-1;
        editor.putInt("awardcount",count);
        editor.remove("alat"+award);
        editor.remove("along"+award);
        editor.remove("atype"+award);
        for(int i=award;i<count;i++)
        {
            editor.putLong("alat"+i,pref.getLong("alat"+(i+1),0));
            editor.putLong("along"+i,pref.getLong("along"+(i+1),0));
            editor.putInt("atype"+i,pref.getInt("atype"+(i+1),0));
        }
        editor.apply();
    }
    protected void onPause() {
        super.onPause();
        main.stop();
    }
    public void Clickback(View v){
        Intent inte=new Intent(TakeAward.this,MapsActivity.class);
        startActivity(inte);
        finish();
    }
}
