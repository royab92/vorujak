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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Shop extends AppCompatActivity {
SharedPreferences pref;
    MediaPlayer mp;
    int price=0;
    int pcount=0;
    int acount=0;
    int scount=0;
    TextView sn;
    TextView pr;
    TextView pn;
    TextView an;
    TextView st;
    TextView tg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        pref=getApplicationContext().getSharedPreferences("VorujakPref",MODE_PRIVATE);
        int gold;
        gold=pref.getInt("gold",0);
        st=(TextView)findViewById(R.id.state);
        pr=(TextView)findViewById(R.id.sumPrice);
         pr.setText(price+"$");
        pn=(TextView)findViewById(R.id.pnum);
        pn.setText(""+pcount);
         sn=(TextView)findViewById(R.id.snum);
        sn.setText(scount+"");
        an=(TextView)findViewById(R.id.anum);
        an.setText(acount+"");
        tg=(TextView)findViewById(R.id.tgold);
        tg.setText(gold+"");
        mp = MediaPlayer.create(Shop.this, R.raw.shop);
        mp.start();

    }
    public void ClickBack(View v){
        Intent inte=new Intent(Shop.this,MapsActivity.class);
        startActivity(inte);
        finish();
    }
    public void ClickPotion(View v){
        MediaPlayer temp;
       temp=MediaPlayer.create(Shop.this, R.raw.pick);
        temp.start();
        price=price+300;
        pcount=pcount+1;
        pn.setText(""+pcount);
        pr.setText(""+price);
    }
    public void ClickArmor(View v){
        MediaPlayer temp;
        temp=MediaPlayer.create(Shop.this, R.raw.pick);
        temp.start();
        price=price+1000;
        acount=acount+1;
        an.setText(""+acount);
        pr.setText(""+price);
    }
    public void ClickStar(View v){
        MediaPlayer temp;
        temp=MediaPlayer.create(Shop.this, R.raw.pick);
        temp.start();
        price=price+50;
        scount=scount+1;
        sn.setText(""+scount);
        pr.setText(""+price);
    }
    public void ClickTake(View v){

        pref=getApplicationContext().getSharedPreferences("VorujakPref",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        int gold;
        gold=pref.getInt("gold",0);
        if(gold>=price) {
            //dar sorate kharid
             st.setText("succesful");
            gold=gold-price;
            tg.setText(gold+"");
            editor.putInt("gold",gold);
            editor.putInt("potion",pref.getInt("potion",0)+pcount);
            editor.putInt("armor",pref.getInt("armor",0)+acount);
            editor.putInt("stars",pref.getInt("stars",0)+scount);
            editor.putInt("awardcount", 0);
            editor.putInt("enemycount", 0);
            editor.apply();
            price=0;
            acount=0;
            pcount=0;
            scount=0;
            sn.setText("0");
            pn.setText("0");
            an.setText("0");
            pr.setText("0");
            st.setText("");
        }
        else {
            st.setText("failed");
        }

    }
    public void ClickReset(View v){
        price=0;
        acount=0;
        pcount=0;
        scount=0;
        sn.setText("0");
        pn.setText("0");
        an.setText("0");
        pr.setText("0");
        st.setText("");
    }
    protected void onPause() {
        super.onPause();
        mp.stop();
    }
    protected void onResume(){
        super.onResume();
        pref=getApplicationContext().getSharedPreferences("VorujakPref",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        int gold;
        gold=pref.getInt("gold",0);
        tg.setText(gold+"");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //update user
        pref=getApplicationContext().getSharedPreferences("VorujakPref",MODE_PRIVATE);
        String authkey=pref.getString("AuthKey",null);
        String phone=pref.getString("phone",null);
        int gold=pref.getInt("gold",0);
        int life=pref.getInt("life",0);
        int potion=pref.getInt("potion",0);
        int score=pref.getInt("score",0);
        int level=pref.getInt("level",0);
        int fire=pref.getInt("stars",0);
        int armor=pref.getInt("armor",0);
        UpdateUser upd=new UpdateUser(Shop.this);
        JSONObject requestjo = new JSONObject();
        try {
            requestjo.put("AppName", "addigit");
            requestjo.put("AppVersion", "1");
            requestjo.put("UserId", "0");
            requestjo.put("PhoneNo", phone);
            requestjo.put("AuthKey",authkey);
            requestjo.put("Gold",gold);
            requestjo.put("Life",life);
            requestjo.put("Potion",potion);
            requestjo.put("score",score);
            requestjo.put("level",level);
            requestjo.put("fireballs",fire);
            requestjo.put("armor",armor);
            upd.UpdateRecieve(requestjo,new UpdateUser.Update(){
                @Override
                public void onUpdate(int StatusCode, String Message){
                    if(StatusCode!=0){
                        Toast.makeText(Shop.this,Message,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
