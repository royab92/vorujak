package com.example.rb.vorujak;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class QrResult extends AppCompatActivity {
    AlertDialog levelDialog;
    boolean isFABOpen=false;
    MediaPlayer mp;
    static boolean play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.errorqr);
        Intent j = getIntent();
        final String qrcode = j.getStringExtra("QR_CODE");
       // final String qrcode ="4";
        if (qrcode!=null) {
           // Toast.makeText(QrResult.this,qrcode, Toast.LENGTH_SHORT).show();
            final SharedPreferences pref = getSharedPreferences("VorujakPref", MODE_PRIVATE);
            final double lat = Double.longBitsToDouble(pref.getLong("lat", 0));
            final double longt = Double.longBitsToDouble(pref.getLong("long", 0));
            final String shopId=pref.getString("shopid",null);
            final String phone = pref.getString("phone", null);
           // final double lat =35.12345;
           // final double longt=51.12345;
          //  final String shopId="abadi12";
            Process asd = new Process(QrResult.this);
            JSONObject requestjo = new JSONObject();
            try {
                requestjo.put("latitude", lat);
                requestjo.put("longtitude", longt);
                requestjo.put("shopId", shopId);
                requestjo.put("award", true);
                requestjo.put("awardType", "optional" + ";" + qrcode);
                requestjo.put("enemy", false);
                requestjo.put("enemyType", "optional");
                requestjo.put("AppName", "addigit");
                requestjo.put("AppVersion", "1");
                requestjo.put("Userid", "0");
                requestjo.put("PhoneNo", phone);
                asd.onProcess(requestjo, new Process.ProcessResponse() {
                    @Override
                    public void getProcess( String code, String question, String answer1, String answer2, String answer3, final int correct, int StatusCode, String Message) {
                      // Toast.makeText(QrResult.this,String.valueOf(StatusCode),Toast.LENGTH_SHORT).show();
                        //Toast.makeText(QrResult.this,code,Toast.LENGTH_SHORT).show();
                        final  SharedPreferences.Editor editor=pref.edit();
                        editor.putString("awardcode",code);
                        editor.commit();
                        if(StatusCode==1||StatusCode==2||StatusCode==3||StatusCode==5){
                            Toast.makeText(QrResult.this,"بروز خطا",Toast.LENGTH_SHORT).show();
                        }
                        if(StatusCode==4){
                            ProgressBar p=(ProgressBar)findViewById(R.id.progressBar3);
                            p.setVisibility(View.INVISIBLE);
                            AlertDialog.Builder builder;
                            builder = new AlertDialog.Builder(QrResult.this);
                            builder.setTitle("خطا")
                                    .setMessage("متاسفانه جایزه ای در این مکان نیست.")
                                    .setCancelable(false)
                                    .setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent(QrResult.this, MapsActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        if(StatusCode==6){
                            ProgressBar p=(ProgressBar)findViewById(R.id.progressBar3);
                            p.setVisibility(View.INVISIBLE);
                            AlertDialog.Builder builder;
                            builder = new AlertDialog.Builder(QrResult.this);
                            builder.setTitle("خطا")
                                    .setMessage("شما قبلا در این مکان بوده اید..")
                                    .setCancelable(false)
                                    .setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent(QrResult.this, MapsActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        if (StatusCode == 0) {
                            //Toast.makeText(QrResult.this,String.valueOf(question.equals("")),Toast.LENGTH_SHORT).show();
                            if(question.equals("")&&code!=null){
                                //namayesh peygham monaseb pin sharj jayeze va enteghal be map

                                Intent w=new Intent(QrResult.this,Win.class);
                                w.putExtra("code",pref.getString("awardcode",null));
                                startActivity(w);
                                finish();

                            }
                            if (!question.equals("")) {
                                setContentView(R.layout.activity_qr_result);
                                play=false;
                                mp = MediaPlayer.create(QrResult.this, R.raw.drones);
                                if (play) {
                                    mp.start();
                                } else {
                                    mp.pause();
                                }
                                isFABOpen=false;
                                final SharedPreferences pref = getApplicationContext().getSharedPreferences("VorujakPref", MODE_PRIVATE);
                                TextView t1 = (TextView) findViewById(R.id.score);
                                t1.setText("" + pref.getInt("score",0));
                                TextView t2 = (TextView) findViewById(R.id.gold);
                                t2.setText("" + pref.getInt("gold",0));
                                final Typeface tf=Typeface.createFromAsset(getAssets(),"BYekan.ttf");
                                TextView q=(TextView) findViewById(R.id.question);
                                q.setText(question);
                                q.setTypeface(tf);
                                final Button a1=(Button)findViewById(R.id.answer1);
                                a1.setText(answer1);
                                a1.setTypeface(tf);
                              final Button a2=(Button)findViewById(R.id.answer2);
                                a2.setText(answer2);
                                a2.setTypeface(tf);
                               final Button a3=(Button)findViewById(R.id.answer3);
                                a3.setText(answer3);
                                a3.setTypeface(tf);
                                a1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        if(correct==0){
                                            a1.setBackgroundColor(getResources().getColor(R.color.green));
                                            TextView aw=(TextView)findViewById(R.id.award);
                                            aw.setText(pref.getString("awardcode",null));
                                            aw.setTypeface(tf);
                                            editor.putInt("score",pref.getInt("score",0)+80);
                                            editor.putInt("flagg",0);
                                            editor.commit();
                                            a2.setEnabled(false);
                                            a3.setEnabled(false);
                                            Process asd = new Process(QrResult.this);
                                            JSONObject requestjo = new JSONObject();
                                            try {

                                                requestjo.put("latitude", lat);
                                                requestjo.put("longtitude", longt);
                                                requestjo.put("shopId", shopId);
                                                requestjo.put("award", true);
                                                requestjo.put("awardType", "QR" + ";" + qrcode);
                                                requestjo.put("enemy", false);
                                                requestjo.put("enemyType", "optional");
                                                requestjo.put("AppName", "addigit");
                                                requestjo.put("AppVersion", "1");
                                                requestjo.put("Userid", "0");
                                                requestjo.put("PhoneNo", phone);
                                                asd.onProcess(requestjo, new Process.ProcessResponse() {
                                                    @Override
                                                    public void getProcess(final String code, String question, String answer1, String answer2, String answer3, final int correct, int StatusCode, String Message) {
                                                       // Toast.makeText(QrResult.this,"succes",Toast.LENGTH_SHORT).show();
                                                       if(StatusCode==0){
                                                        editor.putInt("flagg",1);
                                                        editor.commit();}
                                                    }
                                                });
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                        else{

                                            a1.setBackgroundColor(getResources().getColor(R.color.red));
                                            AlertDialog.Builder builder;
                                            builder = new AlertDialog.Builder(QrResult.this);
                                            builder.setTitle("خطا")
                                                    .setMessage("متاسفانه پاسخ شما اشتباه است.")
                                                    .setCancelable(false)
                                                    .setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent i = new Intent(QrResult.this, MapsActivity.class);
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .show();
                                        }
                                }});
                                a2.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        if(correct==1){
                                            a2.setBackgroundColor(getResources().getColor(R.color.green));
                                            TextView aw=(TextView)findViewById(R.id.award);
                                            aw.setText(pref.getString("awardcode",null));
                                            aw.setTypeface(tf);
                                            editor.putInt("score",pref.getInt("score",0)+80);
                                            editor.putInt("flagg",0);
                                            editor.commit();
                                            a1.setEnabled(false);
                                            a3.setEnabled(false);
                                            Process asd = new Process(QrResult.this);
                                            JSONObject requestjo = new JSONObject();
                                            try {

                                                requestjo.put("latitude", lat);
                                                requestjo.put("longtitude", longt);
                                                requestjo.put("shopId", shopId);
                                                requestjo.put("award", true);
                                                requestjo.put("awardType", "QR" + ";" + qrcode);
                                                requestjo.put("enemy", false);
                                                requestjo.put("enemyType", "optional");
                                                requestjo.put("AppName", "addigit");
                                                requestjo.put("AppVersion", "1");
                                                requestjo.put("Userid", "0");
                                                requestjo.put("PhoneNo", phone);
                                                asd.onProcess(requestjo, new Process.ProcessResponse() {
                                                    @Override
                                                    public void getProcess(final String code, String question, String answer1, String answer2, String answer3, final int correct, int StatusCode, String Message) {
                                                        //Toast.makeText(QrResult.this,"succes",Toast.LENGTH_SHORT).show();
                                                        if(StatusCode==0){
                                                        editor.putInt("flagg",1);
                                                        editor.commit();}
                                                    }
                                                });
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        else{

                                            a2.setBackgroundColor(getResources().getColor(R.color.red));
                                            AlertDialog.Builder builder;
                                            builder = new AlertDialog.Builder(QrResult.this);
                                            builder.setTitle("خطا")
                                                    .setMessage("متاسفانه پاسخ شما اشتباه است.")
                                                    .setCancelable(false)
                                                    .setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent i = new Intent(QrResult.this, MapsActivity.class);
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .show();
                                        }
                                    }
                                });
                                a3.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        if(correct==2){
                                            a3.setBackgroundColor(getResources().getColor(R.color.green));
                                            TextView aw=(TextView)findViewById(R.id.award);
                                            aw.setText(pref.getString("awardcode",null));
                                            aw.setTypeface(tf);
                                            editor.putInt("score",pref.getInt("score",0)+80);
                                            editor.putInt("flagg",0);
                                            editor.commit();
                                            a2.setEnabled(false);
                                            a1.setEnabled(false);
                                            Process asd = new Process(QrResult.this);
                                            JSONObject requestjo = new JSONObject();
                                            try {

                                                requestjo.put("latitude", lat);
                                                requestjo.put("longtitude", longt);
                                                requestjo.put("shopId", shopId);
                                                requestjo.put("award", true);
                                                requestjo.put("awardType", "QR" + ";" + qrcode);
                                                requestjo.put("enemy", false);
                                                requestjo.put("enemyType", "optional");
                                                requestjo.put("AppName", "addigit");
                                                requestjo.put("AppVersion", "1");
                                                requestjo.put("Userid", "0");
                                                requestjo.put("PhoneNo", phone);
                                                asd.onProcess(requestjo, new Process.ProcessResponse() {
                                                    @Override
                                                    public void getProcess(final String code, String question, String answer1, String answer2, String answer3, final int correct, int StatusCode, String Message) {
                                                      // Toast.makeText(QrResult.this,String.valueOf(StatusCode),Toast.LENGTH_SHORT).show();
                                                        if(StatusCode==0){
                                                        editor.putInt("flag",1);
                                                        editor.commit();
                                                    }}
                                                });
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        else{

                                            a3.setBackgroundColor(getResources().getColor(R.color.red));
                                            AlertDialog.Builder builder;
                                            builder = new AlertDialog.Builder(QrResult.this);
                                            builder.setTitle("خطا")
                                                    .setMessage("متاسفانه پاسخ شما اشتباه است.")
                                                    .setCancelable(false)
                                                    .setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent i = new Intent(QrResult.this, MapsActivity.class);
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .show();
                                        }
                                    }
                                });

                            }

                        }
                    }
                });
            }
             catch (JSONException e) {
                e.printStackTrace();
            }


        }else{
            Toast.makeText(QrResult.this,"بروز خطا در اسکن",Toast.LENGTH_SHORT).show();
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
         SharedPreferences pref = getSharedPreferences("VorujakPref", MODE_PRIVATE);
        if(!isFABOpen){
            if(pref.contains("flagg")&&pref.getInt("flagg",0)==1){
            Intent in=new Intent(QrResult.this,MapsActivity.class);
                startActivity(in);
            finish();
            }else {
                super.onBackPressed();

            }
        }else{
            closeFABMenu();
        }
    }
    public void Clickhelp(View v){
        Intent ih=new Intent(QrResult.this,Help.class);
        startActivity(ih);
        finish();
    }
    public void Clickabout(View v){
        Intent ina=new Intent(QrResult.this,About.class);
        startActivity(ina);
        finish();
    }
    public void ClickLocation(View v){
        Intent ina=new Intent(QrResult.this,MapsActivity.class);
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
