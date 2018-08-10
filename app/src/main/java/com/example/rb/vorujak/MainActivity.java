package com.example.rb.vorujak;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import lal.adhish.gifprogressbar.GifView;

import static android.R.attr.permission;

public class MainActivity extends AppCompatActivity {
    protected int gold;
    protected int score;
    protected int armor;
    protected int potion;
    protected int stars;
    protected int life;
    int flag=0;
    private static final int MyPermissionRequest=1;
   SharedPreferences pref;
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT>=23){
            String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA};
            if(!hasPermissions(this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(MainActivity.this,PERMISSIONS,MyPermissionRequest);
            }
            else{
                //granted
                //  go to maps
            }

        }


       pref=getSharedPreferences("VorujakPref",MODE_PRIVATE);
   if(pref.contains("activate")&&(pref.getBoolean("activate",false))) {
       phone=pref.getString("phone", null);
       AppConfig apg = new AppConfig(MainActivity.this);
       JSONObject requestjo = new JSONObject();
       try {
           requestjo.put("AppName", "addigit");
           requestjo.put("AppVersion", "1");
           requestjo.put("UserId", "0");
           requestjo.put("PhoneNo", phone);
           apg.signUp(requestjo, new AppConfig.Configuration() {
               @Override
               public void onSignUp(String Appname, String IosVersion, String AndroidVersion, boolean IosUpdate, boolean AndroidUpdate, String ServerTime, String IosUrl, String AndroidUrl, String[] AdTexts, String BaseAPI, String BasePay, int StatusCode, String Message) {
                   if (StatusCode == 0) {
                      // Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                       String localVersion="";
                       String remoteVersion="";
                       SharedPreferences.Editor editor=getSharedPreferences("VorujakPref",MODE_PRIVATE).edit();
                       editor.putString("serverTime",ServerTime);
                       editor.apply();
                       remoteVersion=AndroidVersion;
                       localVersion=pref.getString("localVersion",null);
                       Float lv=Float.parseFloat(localVersion);
                       Float rv=Float.parseFloat(remoteVersion);
                      // float lv=1;
                      // float rv=1;
                       if(rv>lv&&AndroidUpdate==true){
                           final String aurl=AndroidUrl;
                           //namayesh peygham baray update ejbari app
                           AlertDialog.Builder builder;
                           builder = new AlertDialog.Builder(MainActivity.this);
                           builder.setTitle("بروزرسانی بازی")
                                   .setMessage("برای استفاده از برنامه باید آخرین نسخه را دریافت کنید.")
                                   .setCancelable(false)
                                   .setPositiveButton("تایید", new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog, int which) {
                                           Intent inte=new Intent();
                                           inte.setAction(Intent.ACTION_VIEW);
                                           inte.addCategory(Intent.CATEGORY_BROWSABLE);
                                           inte.setData(Uri.parse(aurl));
                                           startActivity(inte);
                                       }
                                   })
                                   .setIcon(android.R.drawable.ic_dialog_alert)
                                   .show();

                       }

                     else {

                               RecieveUser apg = new RecieveUser(MainActivity.this);
                               JSONObject requestjo = new JSONObject();
                               try {
                                   requestjo.put("AppName", "addigit");
                                   requestjo.put("AppVersion", "1");
                                   requestjo.put("UserId", "0");
                                   requestjo.put("PhoneNo", phone);
                                   apg.DataRecieve(requestjo, new RecieveUser.Recieve() {
                                       @Override
                                       public void onDataRecieve(String Authkey, int gold, int life, int potion,int score,int level,int fire,int armor, int StatusCode, String Message) {
                                           if (StatusCode == 0) {
                                               SharedPreferences.Editor editor = getSharedPreferences("VorujakPref", MODE_PRIVATE).edit();
                                               editor.putString("AuthKey", Authkey);
                                               editor.putInt("gold", gold);
                                               editor.putInt("life", life);
                                               editor.putInt("potion", potion);
                                               editor.putInt("level",level);
                                               editor.putInt("score",score);
                                               editor.putInt("stars",fire);
                                               editor.putInt("armor",armor);
                                               editor.apply();
                                               int nlife = life;
                                               long stime = 0;
                                               String servertime = "";
                                               servertime = pref.getString("serverTime", null);
                                               //stime = Long.parseLong(servertime);
                                               //estefade az zamane server
                                              // long t1 = pref.getLong("time", 0);

                                               //  Date ltime = new Date(f.lastModified());
                                               //  long t1 = (ltime.getTime()) / 1000;
                                               Date ntime = Calendar.getInstance().getTime();
                                               long t2 = (ntime.getTime()) / 1000;
                                               long t1=t2-1000;
                                               long tt = t2 - t1;
                                               long leftt;
                                               if ((stars == 0 || nlife == 0) && tt < 900) {

                                                   //wait conditions
                                                   if (tt < 60) {
                                                       leftt = 1;
                                                   } else {
                                                       leftt = tt / 60;
                                                   }
                                                   TextView tv = (TextView) findViewById(R.id.textView1);
                                                   tv.setText("شما نمی توانید وارد شوید!لطفا" + leftt + "دقیقه دیگر امتحان نمایید.");
                                               } else {
                                                   if (stars == 0) {
                                                       stars = 30;
                                                       editor.putInt("stars", stars);
                                                       editor.apply();
                                                   }
                                                   if (nlife == 0) {
                                                       nlife = 3;
                                                       editor.putString("life", Integer.toString(nlife));
                                                       editor.apply();
                                                   }
                                                   Thread welcomeThread = new Thread() {
                                                       @Override
                                                       public void run() {
                                                           try {
                                                               super.run();
                                                               sleep(2000);  //Delay of 5 seconds
                                                           } catch (Exception e) {

                                                           } finally {
                                                               Intent i = new Intent(MainActivity.this,
                                                                       MapsActivity.class);
                                                               // i.putExtra("user", player);
                                                               startActivity(i);
                                                               finish();
                                                           }
                                                       }
                                                   };
                                                   welcomeThread.start();
                                               }
                                           } else {
                                               AlertDialog.Builder builder;
                                               builder = new AlertDialog.Builder(MainActivity.this);
                                               builder.setTitle("خطا")
                                                       .setMessage("خطا در برقراری ارتباط با سرور")
                                                       .setCancelable(true)
                                                       .setPositiveButton("تلاش مجدد", new DialogInterface.OnClickListener() {
                                                           public void onClick(DialogInterface dialog, int which) {

                                                           }
                                                       })
                                                       .setIcon(android.R.drawable.ic_dialog_alert)
                                                       .show();
                                           }
                                       }

                                   });
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }

                       }
                   }
                   else{

                   }
               }
           });
       } catch (JSONException e) {
           e.printStackTrace();
       }
   }
   else{
       Thread RegisterThread = new Thread() {
           @Override
           public void run() {
               try {
                   super.run();
                   sleep(5000);  //Delay of 5 seconds
               } catch (Exception e) {

               } finally {
                   Intent i = new Intent(MainActivity.this,
                           Register.class);
                   // i.putExtra("user", player);
                   startActivity(i);
                   finish();
               }
           }
       };
       RegisterThread.start();
   }
                    }

    public void onRequestPermissionResult(int resultcode,String permissions[],int[]grantresults){
        switch (resultcode){
            case MyPermissionRequest:
                if(grantresults.length>0&&grantresults[0]==PackageManager.PERMISSION_GRANTED){
                    //permission granted do codes
                    Toast.makeText(MainActivity.this,"permission is granted",Toast.LENGTH_SHORT).show();
                }
                else {
                    //permission denied disable app
                    Toast.makeText(MainActivity.this,"permission has not granted",Toast.LENGTH_SHORT).show();
                }

            break;
        }
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
