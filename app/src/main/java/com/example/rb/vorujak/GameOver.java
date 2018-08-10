package com.example.rb.vorujak;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class GameOver extends AppCompatActivity {
    MediaPlayer main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
main=MediaPlayer.create(GameOver.this, R.raw.loose);
        main.start();
    }
    public void ClickShop(View v){
        Intent ins=new Intent(GameOver.this,Shop.class);
        startActivity(ins);
    }
    public void ClickExit(View v){
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        main.stop();
    }
}
