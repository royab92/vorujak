package com.example.rb.vorujak;

import android.*;
import android.Manifest;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import static android.graphics.Bitmap.Config.ARGB_8888;
import static com.example.rb.vorujak.Award.awardType.none;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MIN_TIME_BW_UPDATES = 1;
    boolean canGetLocation=false;
    public Location llocation=new Location("");
    double latitude;
    double longitude;
    boolean isGpsEnabled=false;
    boolean isNetworkEnabled=false;
    protected LocationManager lm;
    private GoogleMap mMap;
    private double l1;
    private double l2;
    public int number;
    static boolean flag=true;
    boolean shouldzoom=true;
    private Location location=new Location("ll");
     int awardCount = 1;
    protected int enemyCount = 1;
    private LocationManager mService;
    private GpsStatus mStatus;
   public ArrayList<Location> al=new ArrayList<Location>();
    User player = new User();
    private double mindist = 6;
    MediaPlayer mp;
    boolean play;
    SharedPreferences pref;
    int size;
    boolean isFABOpen=false;
    boolean zoom;
   private ArrayList<Award> awards = new ArrayList<Award>();
    float zoomLevel;
GoogleApiClient mGoogleApi;
   // private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    int pow;
   private BroadcastReceiver gpsLocationReciever=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
if(intent.getAction().matches("android.location.PROVIDERS_CHANGED")){
    LocationManager locationManager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
        onMapReady(mMap);
    }
    else {
        new Handler().postDelayed(sendUpdatesToUI,10);
    }
}
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initGoogleAPIClient();
        showSettingDialog();

        pref = getApplicationContext().getSharedPreferences("VorujakPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        player.init(pref.getInt("gold", 0), pref.getInt("score", 0), pref.getInt("armor", 0), pref.getInt("potion", 0), pref.getInt("stars", 0), pref.getInt("life", 0));
        isFABOpen=false;
        TextView t1 = (TextView) findViewById(R.id.score);
        t1.setText("" + player.score);
        TextView t2 = (TextView) findViewById(R.id.gold);
        t2.setText("" + player.gold);
zoom=true;
         play=false;
        zoomLevel = 14.0f;
        mp = MediaPlayer.create(MapsActivity.this, R.raw.drones);
Button sound=(Button)findViewById(R.id.button3);
        sound.setOnClickListener(new View.OnClickListener(){
                                     @Override
                                     public void onClick(View v){
                                         if(play){
                                             mp.stop();
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
        }

        );
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

}

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
            location = getLocation();
            pref = getApplicationContext().getSharedPreferences("VorujakPref", MODE_PRIVATE);
            final SharedPreferences.Editor editor = pref.edit();
            final String phone = pref.getString("phone", null);
         //  l1=35.733157;
         //   l2=51.318088;
          // location.setLatitude(l1);
          // location.setLongitude(l2);
            if (location != null) {
                if (location.getLatitude() != 0 && location.getLongitude() != 0) {
                    mMap.clear();
                  l1 = location.getLatitude();
                    l2 = location.getLongitude();
                    editor.putLong("lat", Double.doubleToRawLongBits(l1));
                    editor.putLong("long", Double.doubleToRawLongBits(l2));
                    editor.commit();
                    LatLng start = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(start).title("موقعیت من").icon(BitmapDescriptorFactory.fromResource(R.drawable.pingp)));
                    if(!shouldzoom){mMap.moveCamera(CameraUpdateFactory.newLatLng(start));}
                    else {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, zoomLevel));
                    shouldzoom=false;}
                    if(awards.size()!=0){
                        for (int i = 0; i < awards.size(); i++) {
                            String txt = String.valueOf(awards.get(i).number);
                            Bitmap bitmap = makeBitmap(MapsActivity.this, txt);
                            double tlat = awards.get(i).alocation.getLatitude();

                            double tlong = awards.get(i).alocation.getLongitude();
                            LatLng temp = new LatLng(tlat, tlong);
                            // mMap.addMarker(new MarkerOptions().position(temp).title("award point").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                            mMap.addMarker(new MarkerOptions().position(temp).title("award point").icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

                        }
                      //  for (int h = 0; h < awards.size(); h++) {
                            awards.get(0).dista = Dist(location, awards.get(0).alocation);
                      //  }
                        double minda = awards.get(0).dista;
                        int indexda = 0;
                        /*for (int j = 0; j < awards.size(); j++) {
                            if (awards.get(j).dista <= minda) {
                                minda = awards.get(j).dista;
                                indexda = j;
                            }
                        }*/
                        boolean exist = false;
                        if (pref.contains("templat")) {
                            for (int j = 0; j < awards.size(); j++) {
                                if (awards.get(j).alocation.getLatitude() == Double.longBitsToDouble(pref.getLong("templat", 0)) && awards.get(j).alocation.getLongitude() == Double.longBitsToDouble(pref.getLong("templong", 0))) {
                                    exist = true;
                                }
                            }

                        }
                        boolean timeflag = true;
                        Date stime = Calendar.getInstance().getTime();
                        long t1 = (stime.getTime()) / 1000;
                        if (pref.contains("temptime")) {
                            Long t3 = pref.getLong("temptime", 0);
                            if (Math.abs(t3 - t1) >= 3600) {
                                timeflag = true;
                            } else {
                                timeflag = false;
                            }
                        }

                        //  editor.putString("awardtype",awards.get(indexda).atype);
                        if ((minda < mindist && !exist) || (minda < mindist && exist && timeflag)) {
                            editor.putLong("templat", Double.doubleToRawLongBits(awards.get(indexda).alocation.getLatitude()));
                            editor.putLong("templong", Double.doubleToRawLongBits(awards.get(indexda).alocation.getLongitude()));
                            Date ntime = Calendar.getInstance().getTime();
                            long t2 = (ntime.getTime()) / 1000;
                            editor.putLong("temptime", t2);
                            editor.putString("shopid",awards.get(indexda).id);
                            editor.putInt("score", pref.getInt("score", 0) + 20);
                            editor.commit();
                            PercentRelativeLayout pl=(PercentRelativeLayout)findViewById(R.id.mlayout);
                           final Snackbar snackbar=Snackbar.make(pl,"دوربین خود را باز کنید و غافلگیر شوید!",Snackbar.LENGTH_LONG)
                                    .setAction("باز کردن دوربین",new View.OnClickListener(){
                                        public void onClick(View v){
                                            Intent q = new Intent(MapsActivity.this, QrQode.class);
                                            startActivity(q);
                                            finish();
                                        }
                                    });
                            snackbar.setActionTextColor(getResources().getColor(R.color.White));
                            View sview=snackbar.getView();
                            sview.setBackgroundColor(getResources().getColor(R.color.dpurple));
                            sview.setOnTouchListener(new View.OnTouchListener() {
                                public boolean onTouch(View v, MotionEvent event) {
                                    snackbar.dismiss();
                                    return false;
                                }
                            });
                            snackbar.show();
                        }
                        new CountDownTimer(180000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                flag = false;
                            }

                            public void onFinish() {
                                flag = true;
                                GetLocation gl = new GetLocation(MapsActivity.this);
                                JSONObject requestjo = new JSONObject();
                                try {
                                    requestjo.put("latitude", l1);
                                    requestjo.put("longtitude", l2);
                                    requestjo.put("AppName", "addigit");
                                    requestjo.put("AppVersion", "1");
                                    requestjo.put("UserId", "0");
                                    requestjo.put("PhoneNo", phone);
                                   // editor.putInt("flag",1);
                                  //  editor.commit();
                                    gl.LocationRecieve(requestjo, new GetLocation.Recieve() {
                                        @Override
                                        public void onLocationRecieve(int status, String message, ArrayList<Award> shopAward, boolean[] award, String[] atypes, boolean[] enemy, String[] etypes) {
                                            if (status == 0) {
                                                // flag = true;
                                                int pow = NormalPD(player) + 2;
                                                for (int i = 0; i < award.length; i++) {
                                                    if (award[i]) {
                                                        Location templocation = new Location("");
                                                        Award tempa = new Award();
                                                        tempa = shopAward.get(i);
                                                        awards.add(i, tempa);
                                                    } else {
                                                        //enemy

                                                    }
                                                }
                                                //awardCount = award.length;

                                                for (int i = 0; i < award.length; i++) {
                                                    String txt = String.valueOf(awards.get(i).number);
                                                    Bitmap bitmap = makeBitmap(MapsActivity.this, txt);
                                                    double tlat = awards.get(i).alocation.getLatitude();

                                                    double tlong = awards.get(i).alocation.getLongitude();
                                                    LatLng temp = new LatLng(tlat, tlong);
                                                    // mMap.addMarker(new MarkerOptions().position(temp).title("award point").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                                                    mMap.addMarker(new MarkerOptions().position(temp).title("award point").icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

                                                }
                                               // for (int h = 0; h < award.length; h++) {
                                                    awards.get(0).dista = Dist(location, awards.get(0).alocation);
                                               // }
                                                double minda = awards.get(0).dista;
                                                int indexda = 0;
                                             /*   for (int j = 0; j < award.length; j++) {
                                                    if (awards.get(j).dista <= minda) {
                                                        minda = awards.get(j).dista;
                                                        indexda = j;
                                                    }
                                                }*/
                                                boolean exist = false;
                                                if (pref.contains("templat")) {
                                                    for (int j = 0; j < award.length; j++) {
                                                        if (awards.get(j).alocation.getLatitude() == Double.longBitsToDouble(pref.getLong("templat", 0)) && awards.get(j).alocation.getLongitude() == Double.longBitsToDouble(pref.getLong("templong", 0))) {
                                                            exist = true;
                                                        }
                                                    }

                                                }
                                                boolean timeflag = true;
                                                Date stime = Calendar.getInstance().getTime();
                                                long t1 = (stime.getTime()) / 1000;
                                                if (pref.contains("temptime")) {
                                                    Long t3 = pref.getLong("temptime", 0);
                                                    if (Math.abs(t3 - t1) >= 1800) {
                                                        timeflag = true;
                                                    } else {
                                                        timeflag = false;
                                                    }
                                                }

                                                //  editor.putString("awardtype",awards.get(indexda).atype);
                                                if ((minda < mindist && !exist) || (minda < mindist && exist && timeflag)) {
                                                    editor.putLong("templat", Double.doubleToRawLongBits(awards.get(indexda).alocation.getLatitude()));
                                                    editor.putLong("templong", Double.doubleToRawLongBits(awards.get(indexda).alocation.getLongitude()));
                                                    Date ntime = Calendar.getInstance().getTime();
                                                    long t2 = (ntime.getTime()) / 1000;
                                                    editor.putLong("temptime", t2);
                                                    editor.putString("shopid",awards.get(indexda).id);
                                                    editor.putInt("score", pref.getInt("score", 0) + 20);
                                                    editor.commit();
                                                    PercentRelativeLayout pl=(PercentRelativeLayout)findViewById(R.id.mlayout);
                                                 final   Snackbar snackbar=Snackbar.make(pl,"دوربین خود را باز کنید و غافلگیر شوید!",Snackbar.LENGTH_LONG)
                                                            .setAction("باز کردن دوربین",new View.OnClickListener(){
                                                                public void onClick(View v){
                                                                    Intent q = new Intent(MapsActivity.this, QrQode.class);
                                                                    startActivity(q);
                                                                    finish();
                                                                }
                                                            });
                                                    snackbar.setActionTextColor(getResources().getColor(R.color.White));
                                                    View sview=snackbar.getView();
                                                    sview.setBackgroundColor(getResources().getColor(R.color.dpurple));
                                                    sview.setOnTouchListener(new View.OnTouchListener() {
                                                        public boolean onTouch(View v, MotionEvent event) {
                                                            snackbar.dismiss();
                                                            return false;
                                                        }
                                                    });
                                                    snackbar.show();
                                                }

                                            } else {
                                                Toast.makeText(MapsActivity.this, "خطا در برقراری ارتباط با سرور", Toast.LENGTH_SHORT).show();
                                            }
                                            //WriteEA();
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                      //  Toast.makeText(MapsActivity.this,"hi",Toast.LENGTH_SHORT).show();
                    }else{
                        GetLocation gl = new GetLocation(MapsActivity.this);
                        JSONObject requestjo = new JSONObject();
                        try {
                            requestjo.put("latitude", l1);
                            requestjo.put("longtitude", l2);
                            requestjo.put("AppName", "addigit");
                            requestjo.put("AppVersion", "1");
                            requestjo.put("UserId", "0");
                            requestjo.put("PhoneNo", phone);
                            editor.putInt("flag",1);
                            editor.commit();
                            gl.LocationRecieve(requestjo, new GetLocation.Recieve() {
                                @Override
                                public void onLocationRecieve(int status, String message, ArrayList<Award> shopAward, boolean[] award, String[] atypes, boolean[] enemy, String[] etypes) {
                                    if (status == 0) {
                                        // flag = true;
                                        int pow = NormalPD(player) + 2;
                                        for (int i = 0; i < award.length; i++) {
                                            if (award[i]) {
                                                Location templocation = new Location("");
                                                Award tempa = new Award();
                                                tempa = shopAward.get(i);
                                                awards.add(i, tempa);
                                            } else {
                                                //enemy

                                            }
                                        }
                                        //awardCount = award.length;

                                        for (int i = 0; i < award.length; i++) {
                                            String txt = String.valueOf(awards.get(i).number);
                                            Bitmap bitmap = makeBitmap(MapsActivity.this, txt);
                                            double tlat = awards.get(i).alocation.getLatitude();

                                            double tlong = awards.get(i).alocation.getLongitude();
                                            LatLng temp = new LatLng(tlat, tlong);
                                            // mMap.addMarker(new MarkerOptions().position(temp).title("award point").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                                            mMap.addMarker(new MarkerOptions().position(temp).title("award point").icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

                                        }
                                      //  for (int h = 0; h < award.length; h++) {
                                            awards.get(0).dista = Dist(location, awards.get(0).alocation);
                                       // }
                                        double minda = awards.get(0).dista;
                                        int indexda = 0;
                                       /* for (int j = 0; j < award.length; j++) {
                                            if (awards.get(j).dista <= minda) {
                                                minda = awards.get(j).dista;
                                                indexda = j;
                                            }
                                        }*/
                                        boolean exist = false;
                                        if (pref.contains("templat")) {
                                            for (int j = 0; j < award.length; j++) {
                                                if (awards.get(j).alocation.getLatitude() == Double.longBitsToDouble(pref.getLong("templat", 0)) && awards.get(j).alocation.getLongitude() == Double.longBitsToDouble(pref.getLong("templong", 0))) {
                                                    exist = true;
                                                }
                                            }

                                        }
                                        boolean timeflag = true;
                                        Date stime = Calendar.getInstance().getTime();
                                        long t1 = (stime.getTime()) / 1000;
                                        if (pref.contains("temptime")) {
                                            Long t3 = pref.getLong("temptime", 0);
                                            if (Math.abs(t3 - t1) >= 1800) {
                                                timeflag = true;
                                            } else {
                                                timeflag = false;
                                            }
                                        }

                                        //  editor.putString("awardtype",awards.get(indexda).atype);
                                        if ((minda < mindist && !exist) || (minda < mindist && exist && timeflag)) {
                                            editor.putLong("templat", Double.doubleToRawLongBits(awards.get(indexda).alocation.getLatitude()));
                                            editor.putLong("templong", Double.doubleToRawLongBits(awards.get(indexda).alocation.getLongitude()));
                                            Date ntime = Calendar.getInstance().getTime();
                                            long t2 = (ntime.getTime()) / 1000;
                                            editor.putLong("temptime", t2);
                                            editor.putString("shopid",awards.get(indexda).id);
                                            editor.putInt("score", pref.getInt("score", 0) + 20);
                                            editor.commit();
                                            PercentRelativeLayout pl=(PercentRelativeLayout)findViewById(R.id.mlayout);
                                           final Snackbar snackbar=Snackbar.make(pl,"دوربین خود را باز کنید و غافلگیر شوید!",Snackbar.LENGTH_LONG)
                                                    .setAction("باز کردن دوربین",new View.OnClickListener(){
                                                        public void onClick(View v){
                                                            Intent q = new Intent(MapsActivity.this, QrQode.class);
                                                            startActivity(q);
                                                            finish();
                                                        }
                                                    });
                                            snackbar.setActionTextColor(getResources().getColor(R.color.White));
                                            View sview=snackbar.getView();
                                            sview.setBackgroundColor(getResources().getColor(R.color.dpurple));
                                            sview.setOnTouchListener(new View.OnTouchListener() {
                                                public boolean onTouch(View v, MotionEvent event) {
                                                    snackbar.dismiss();
                                                    return false;
                                                }
                                            });
                                            snackbar.show();
                                        }

                                    } else {
                                        Toast.makeText(MapsActivity.this, "خطا در برقراری ارتباط با سرور", Toast.LENGTH_SHORT).show();
                                    }
                                    //WriteEA();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }
            } else

            {

                Toast.makeText(MapsActivity.this, "خطا در موقعیت یابی", Toast.LENGTH_SHORT).show();
               // LatLng tehran = new LatLng(35.707, 51.366);
               // mMap.addMarker(new MarkerOptions().position(tehran).title("موقعیت").icon(BitmapDescriptorFactory.fromResource(R.drawable.pingp)));
               // float zoomLevel = 16.0f;
               // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tehran, zoomLevel));

            }


            }

    public Bitmap makeBitmap(Context context, String text)
    {
        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.marker);
        bitmap = bitmap.copy(ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK); // Text color
        paint.setTextSize(24 * scale); // Text size
        paint.setFakeBoldText(true);
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE); // Text shadow
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

      //  int x = bitmap.getWidth() - bounds.width() - 10; // 10 for padding from right
       int y = bounds.height()+90;
        int x=bounds.width()+70;

        canvas.drawText(text, x, y, paint);

        return  bitmap;
    }
    protected  double Dist(Location l1,Location l2){
        // double latd;
        //  double longd;

        double dist;
        dist=l1.distanceTo(l2);
        // latd=(l.getLatitude()-te.getLatitude())*111.699;
        //longd=(l.getLongitude()-te.getLongitude())*110.567;
        // dist=Math.sqrt(Math.pow((latd),2)+Math.pow((longd),2));


        return dist;
    }
    public int NormalPD(User user){
        double badkonaki=2.0;
        double gavazn=3.0;
        double gavi=4.0;
        double merikhi=5.0;
        double nakhoonak=6.0;
        double kermmarmoolaki=7.0;
        double min=Math.round((double)user.score/2000);
        min=min<2?0:min;
        min=min>5?5:min;
        double sigma;
        sigma=Math.pow((2-min),2.0)+Math.pow((3-min),2.0)+Math.pow((4-min),2.0)+Math.pow((5-min),2.0);
        sigma=sigma/4;
        double []probArr={0.0,0.0,0.0,0.0,0.0,0.0};
        probArr[4]=(1/Math.sqrt(2*3.14*sigma))*(Math.exp((Math.pow((nakhoonak-min),2.0)*(-1))/2));
        probArr[0]=(1/Math.sqrt(2*3.14*sigma))*(Math.exp((Math.pow((badkonaki-min),2.0)*(-1))/2));
        probArr[2]=(1/Math.sqrt(2*3.14*sigma))*(Math.exp((Math.pow((gavi-min),2.0)*(-1))/2));
        probArr[1]=(1/Math.sqrt(2*3.14*sigma))*(Math.exp((Math.pow((gavazn-min),2.0)*(-1))/2));
        probArr[3]=(1/Math.sqrt(2*3.14*sigma))*(Math.exp((Math.pow((merikhi-min),2.0)*(-1))/2));
        probArr[5]=(1/Math.sqrt(2*3.14*sigma))*(Math.exp((Math.pow((kermmarmoolaki-min),2.0)*(-1))/2));
        double max=probArr[0];
        int index=0;
        for(int i=0;i<6;i++){
            if(probArr[i]>=max){
                max=probArr[i];
                index=i;
            }
        }
        return index;
    }
    Award.awardType awardAppear(User user,double strn){
      double potionProb=(1000.0/(double)(user.score*user.potion*user.gold+10000))*100.0;
        double armorProb=(1000.0/(double)(user.score*user.armor*user.gold+10000))*100.0;
        double goldProb=(1000.0/(double)(user.score*user.gold+10000))*100.0;
        double starProb=0.0;
        starProb =(double)(strn/user.life+user.stars+100)*100.0;
        double [] values={potionProb,armorProb,goldProb,starProb};
        double max=values[0];
        int index=0;
        for(int i=0;i<values.length;i++){
            if(values[i]>=max){
                max=values[i];
                index=i;
            }
        }
        if(index==0) return Award.awardType.potionType;
         if(index==1) return Award.awardType.armorType;
         if(index==2) return Award.awardType.goldType;
          if(index==3) return Award.awardType.starType;
        return none;
    }
     public void Clickhelp(View v){
       Intent ih=new Intent(MapsActivity.this,Help.class);
       startActivity(ih);
      }
      public void Clickabout(View v){
          Intent ina=new Intent(MapsActivity.this,About.class);
          startActivity(ina);
      }
   // public void Sound(View v){
     /*   if(play){ mp.start();}
        else{mp.pause();}*/
  //  }



    @Override
    public void onResume(){
        super.onResume();
        pref=getApplicationContext().getSharedPreferences("VorujakPref",MODE_PRIVATE);
       final SharedPreferences.Editor editor=pref.edit();
        if(!isFABOpen){
        }else{
            closeFABMenu();
        }
      /*  play=sw.isChecked();
        if(play){ mp.start();}
        else{mp.pause();}*/
        player.armor=pref.getInt("armor",0);
        player.score=pref.getInt("score",0);
        player.gold=pref.getInt("gold",0);
        player.potion=pref.getInt("potion",0);
        player.stars=pref.getInt("stars",0);
        player.life=pref.getInt("life",0);
        //ReadEA();
        if(player.score>=10000){
            player.gold=player.gold+50;
            player.score=player.score-10000;
            editor.putInt("gold",player.gold);
            editor.putInt("score",player.score);
            editor.commit();
        }
        TextView t1 = (TextView) findViewById(R.id.score);
        t1.setText("" + player.score);
        TextView t2 = (TextView) findViewById(R.id.gold);
        t2.setText("" + player.gold);

registerReceiver(gpsLocationReciever,new IntentFilter("android.location.PROVIDERS_CHANGED"));

    }

    Award.awardType InttoType(int index) {
        if (index == 0) return Award.awardType.potionType;
        if (index == 1) return Award.awardType.armorType;
        if (index == 2) return Award.awardType.goldType;
        if (index == 3) return Award.awardType.starType;
        return none;
    }
    int TypetoInt(Award.awardType at){
        if(at== Award.awardType.potionType) return 0;
        if(at== Award.awardType.armorType) return 1;
        if(at== Award.awardType.goldType) return 2;
        if(at== Award.awardType.starType) return 3;
        return 4;
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        //update user
        pref=getApplicationContext().getSharedPreferences("VorujakPref",MODE_PRIVATE);
       // SharedPreferences.Editor editor=pref.edit();
       // editor.putInt("flag",0);
       // editor.commit();
        String authkey=pref.getString("AuthKey",null);
        String phone=pref.getString("phone",null);
        int gold=pref.getInt("gold",0);
        int life=pref.getInt("life",0);
        int potion=pref.getInt("potion",0);
        int score=pref.getInt("score",0);
        int level=pref.getInt("level",0);
        int fire=pref.getInt("stars",0);
        int armor=pref.getInt("armor",0);
       // Toast.makeText(MapsActivity.this,"destroy",Toast.LENGTH_SHORT).show();
        if(play){
            mp.pause();}
        UpdateUser upd=new UpdateUser(MapsActivity.this);
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
                        Toast.makeText(MapsActivity.this,Message,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (JSONException e) {
            e.printStackTrace();
        }
        if(gpsLocationReciever!=null){
            unregisterReceiver(gpsLocationReciever);
        }

    }
    public Location getLocation() {
        try {
            lm = (LocationManager) MapsActivity.this.getSystemService(MapsActivity.this.LOCATION_SERVICE);
            isGpsEnabled = lm.isProviderEnabled(lm.GPS_PROVIDER);
            Log.v("isGPSEnabled", "=" + isGpsEnabled);
            isNetworkEnabled = lm.isProviderEnabled(lm.NETWORK_PROVIDER);
            Log.v("isNetworkEnabled", "=" + isNetworkEnabled);
            if (!isGpsEnabled && !isNetworkEnabled) {
                Toast.makeText(MapsActivity.this, "موقعیت یاب خود را روشن نمایید!", Toast.LENGTH_LONG).show();
                }
         else {
                this.canGetLocation = true;
                if (isNetworkEnabled && !isGpsEnabled) {
                    llocation = null;
                    if(Build.VERSION.SDK_INT >= 23)
                    if (MapsActivity.this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && MapsActivity.this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    }

                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (lm != null) {
                        llocation = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
                        if (llocation != null) {
                            latitude = llocation.getLatitude();
                            longitude = llocation.getLongitude();
                        }
                    }
                }
                if (isGpsEnabled && !isNetworkEnabled) {
                    llocation = null;
                    if (llocation == null) {
                        lm.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (lm != null) {
                            llocation = lm
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (llocation != null) {
                                latitude = llocation.getLatitude();
                                longitude = llocation.getLongitude();
                            }
                        }

                    }
                }
                if(isNetworkEnabled && isGpsEnabled)
                {
                    llocation=null;
                    Criteria c=new Criteria();
                    String p=lm.getBestProvider(c,true);
                    lm.requestLocationUpdates(p,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                    if(lm!=null) {
                        llocation = lm.getLastKnownLocation(p);
                        if(llocation!=null){
                            latitude = llocation.getLatitude();
                            longitude = llocation.getLongitude();
                        }
                    }

                }
            }
        }catch (Exception e){e.printStackTrace();}
        return llocation;}
    public void stopUsingGPS(){
        if (lm!=null)
            lm.removeUpdates(MapsActivity.this);
    }
    @Override
    public void onLocationChanged(Location locationn) {
        location = getLocation();
        onMapReady(mMap);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MapsActivity.this, "موقعیت یاب خود را روشن نمایید!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        onMapReady(mMap);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
  /*  public void Topp(View v){
Intent tt=new Intent(MapsActivity.this,TopPlayers.class);
        startActivity(tt);
    }*/

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
        if(play){
        mp.pause();}
        if(!isFABOpen){
            finish();
        }else{
            closeFABMenu();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(play)
        mp.pause();
    }
    private void initGoogleAPIClient(){
        mGoogleApi=new GoogleApiClient.Builder(MapsActivity.this).addApi(LocationServices.API).build();
        mGoogleApi.connect();

    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case 0:
                switch (resultCode){
                    case RESULT_OK:
                        onMapReady(mMap);
                        break;
                    case RESULT_CANCELED:
                        break;
                }
                break;
        }
    }
private Runnable sendUpdatesToUI=new Runnable() {
    @Override
    public void run() {
        showSettingDialog();
    }
};
private void showSettingDialog(){
    LocationRequest lr=LocationRequest.create();
    lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    lr.setInterval(6000);
    lr.setFastestInterval(1000);
    LocationSettingsRequest.Builder builder=new LocationSettingsRequest.Builder().addLocationRequest(lr);
    builder.setAlwaysShow(true);
    final PendingResult<LocationSettingsResult> result=LocationServices.SettingsApi.checkLocationSettings(mGoogleApi,builder.build());
    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
        @Override
        public void onResult(@NonNull LocationSettingsResult result) {
            final Status status=result.getStatus();
            final LocationSettingsStates state=result.getLocationSettingsStates();
            switch (status.getStatusCode()){
                case LocationSettingsStatusCodes.SUCCESS:
                    location = getLocation();
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try{
                        status.startResolutionForResult(MapsActivity.this,0);
                    }catch (IntentSender.SendIntentException e){
                        e.printStackTrace();
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    break;
            }
        }
    });
}

}



