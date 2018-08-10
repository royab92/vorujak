package com.example.rb.vorujak;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class TopPlayers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_players);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("VorujakPref", MODE_PRIVATE);
        String phone=pref.getString("phone",null);
        BestPlayer bp=new BestPlayer(TopPlayers.this);
        JSONObject requestjo = new JSONObject();
        try {
            requestjo.put("level",1);
            requestjo.put("AppName","addigit");
            requestjo.put("AppVersion","1");
            requestjo.put("UserId","0");
            requestjo.put("PhoneNo",phone);
            bp.TopPlayerRecieve(requestjo,new BestPlayer.RecieveTop(){
                @Override
                public void onTopRecieve(int status, String message,int[]levels,int[]scores,String[]names,int[]avatars){
                if (status==0){
                    //namayesh liste bartarinha
                }
                    else{
                    Toast.makeText(TopPlayers.this,"بروز خطا در برقراری ارتباط با سرور",Toast.LENGTH_SHORT).show();
                }
                }
            });
        }catch (JSONException e) {
            e.printStackTrace();}
    }
}
