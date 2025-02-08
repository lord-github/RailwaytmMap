package com.bbstudios.railwaytm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.UrlTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {
    private static GoogleMap mMap;
    Double lat=40.00,lonn=60.00;
    Button showme,dell;
List<String> stans,regions,statelist;
List<Double> latd,lond;
String welady="";
RecyclerView recyclerView,welalist;
List<stansid> stansids,s2;
EditText reggi;
List<wels> welsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
FileUtils.copyJsonFromAssetsToFiles(MainActivity.this,"locations.json");
        statelist=new ArrayList<>();
        reggi=  findViewById(R.id.reggi);
        welsList=new ArrayList<>();
        welalist=findViewById(R.id.wels);
        welsList.add(new wels("ALL","Ählisi"));
        welsList.add(new wels("BN","Balkan"));
        welsList.add(new wels("AH","Ahal"));
        welsList.add(new wels("MR","Mary"));
        welsList.add(new wels("LB","Lebap"));
        welsList.add(new wels("DZ","Daşoguz"));
        welsList.add(new wels("AG","Aşgabat"));
        welalist.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.HORIZONTAL,false));
        welalist.setAdapter(new welAda(welsList,MainActivity.this));
        recyclerView=findViewById(R.id.sanawstan);
        stansids=new ArrayList<>();
        stans=new ArrayList<>();
        regions=new ArrayList<>();
        latd=new ArrayList<>();
        lond=new ArrayList<>();
        slist();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        dell=findViewById(R.id.del);
        showme=findViewById(R.id.showme);
        showme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray locations = FileUtils.readLocations(MainActivity.this);
                mMap.clear();
                Log.e("TAG", locations.toString() );
                PolylineOptions polygonOptions=new PolylineOptions().color(R.color.y2).width(4);
                for (int i = 0; i < locations.length(); i++){
//                    polygonOptions.add(new LatLng(locations.get(i).getAsDouble("lat"),))
                    try {
                        JSONObject location = locations.getJSONObject(i);
                        double lat = location.getDouble("lat");
                        double lon = location.getDouble("lon");
                        String state = location.getString("state");
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lon), 16));
                polygonOptions.add(new LatLng(lat,lon));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                mMap.addPolyline(polygonOptions);


            }
        });
dell.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        FileUtils.deleteLastRecord(MainActivity.this);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }




    }
});

readJsonFromAssets(MainActivity.this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        Log.e("TAG", "Yuklendi" );
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.railmap    ));

            if (!success) {
                Log.e("MapStyle", "Не удалось применить стиль");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapStyle", "Стиль не найден: ", e);
        }
//addRailwayTileOverlay();
//        LatLng startLocation = new LatLng( 39.936125, 59.143657); // Берлин
//
//         Перемещаем камеру
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 6));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Log.e("MAP_CLICK", "Координаты: "+latLng.latitude+"-"+latLng.longitude );


            }
        });
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(35.0, 52.0)); // Южная, Западная точка
        builder.include(new LatLng(42.0, 67.0)); // Северная, Восточная точка

        LatLngBounds bounds = builder.build();

        // Ограничение движения камеры в этих границах
        mMap.setLatLngBoundsForCameraTarget(bounds);

        // Перемещаем камеру, чтобы сразу отобразить ограниченную область
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.0, 59.0), 6)); // Центр Туркменистана

        mMap.setMaxZoomPreference(16); // Максимальный уровень зума
        mMap.setMinZoomPreference(6);


    }


    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        Log.d("MAP_LONG_CLICK", "Координаты: " + latLng.latitude + ", " + latLng.longitude);

        // Добавляем маркер на карту
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Выбрано место"));

    FileUtils.saveLocation(this,(latLng.latitude),
            (latLng.longitude),"LB");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

    }

    public  void readJsonFromAssets(Context context) {
        try {
            // Чтение файла из assets
            InputStream is = context.getAssets().open("tk.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Конвертация в строку
            String json = new String(buffer, "UTF-8");

            // Парсинг JSON
            JSONArray jsonArray = new JSONArray(json);
stans.clear();;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject station = jsonArray.getJSONObject(i);
                String name = station.getString("name");
                double lat = station.getDouble("lat");
                double lon = station.getDouble("lon");
                String region = station.getString("region");
           stans.add(new String(name));
           regions.add(region);
           latd.add(lat);
           lond.add(lon);
 stansids.add(new stansid(region,name,lat,lon));
 recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
 recyclerView.setAdapter(new sAdapter(stansids,MainActivity.this));
            }


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
public BroadcastReceiver receiver_wel=new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
   welady=intent.getStringExtra("ady");
  s2=new ArrayList<>();
  s2.clear();;
if (mMap!=null){
  mMap.clear();}
  if (!welady.equals("ALL")){
      yol(welady);
        s2.add(new stansid("","Ählisi",0.0,0.0));
      for (int i = 0; i < stansids.size(); i++) {
            if (stansids.get(i).region.equals(welady)){
                s2.add(stansids.get(i));
            }
        }
      recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
      recyclerView.setAdapter(new sAdapter(s2,MainActivity.this));

  } else {
      yol(welady);
      s2.clear();
      s2.add(new stansid("","Ählisi",0.0,0.0));
      for (int i = 0; i < stansids.size(); i++) {
          s2.add(stansids.get(i));
      }
      recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
      recyclerView.setAdapter(new sAdapter(s2,MainActivity.this));

  }
    }
};

    public BroadcastReceiver mystans=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          String ady=intent.getStringExtra("ady");

            BitmapDescriptor bitmapDescriptor,b2;
            Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.ff);
            Bitmap img2 = BitmapFactory.decodeResource(getResources(), R.drawable.razy);
            bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(img);
            b2 = BitmapDescriptorFactory.fromBitmap(img2);
            mMap.clear();
            yol(welady);
            if (!ady.equals("Ählisi")){
            for (int i = 0; i <stansids.size() ; i++) {
                if (stansids.get(i).ady.equals(ady)){
                    if (stansids.get(i).lat>0.0) {
                        Double l1 = stansids.get(i).lat;
                        Double l2 = stansids.get(i).lon;
                        if (!stansids.get(i).ady.contains("razýez")){
                        mMap.addMarker(new MarkerOptions().position(new LatLng(l1, l2)).title(ady).icon(bitmapDescriptor));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(l1, l2)).title(ady).icon(b2));

                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(l1, l2), 7));
                    } else {
                        Toast.makeText(getApplicationContext(),"Staniýanyň ýerleşýän ýeri barada maglumat ýok",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
                for (int i = 0; i <s2.size() ; i++) {
                    if (s2.get(i).lat>0.0) {
                        Double l1 = s2.get(i).lat;
                        Double l2 = s2.get(i).lon;
                        if (!s2.get(i).ady.contains("razýez")){
                            mMap.addMarker(new MarkerOptions().position(new LatLng(l1, l2)).title(s2.get(i).ady).icon(bitmapDescriptor));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(l1, l2)).title(s2.get(i).ady).icon(b2));
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(l1, l2), 6));
                    }
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(receiver_wel,new IntentFilter("custom-welayat"));
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(mystans,new IntentFilter("custom-stan"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(receiver_wel);
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(mystans);
    }

    private void addRailwayTileOverlay() {
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {
                try {
                    // Используем OSM тайлы, которые показывают железные дороги
                    Log.e("TAG", "getTileUrl: "+new URL(String.format("https://tile.openstreetmap.org/%d/%d/%d.png", zoom, x, y)) );
                    return new URL(String.format("https://tile.openstreetmap.org/%d/%d/%d.png", zoom, x, y));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }));
    }

public void slist(){
    JSONArray locations = FileUtils.readLocations(MainActivity.this);
    statelist.clear();
    for (int i = 0; i < locations.length(); i++){
        try {
            JSONObject location = locations.getJSONObject(i);
            double lat = location.getDouble("lat");
            double lon = location.getDouble("lon");
            String state = location.getString("state");
    if (!statelist.contains(state)){
        statelist.add(state);
    }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


}

public void yol(String wel){
    JSONArray locations = FileUtils.readLocations(MainActivity.this);
  String su="";
  if (!wel.equals("ALL")){
    for (int j = 0; j <statelist.size() ; j++) {
        if (statelist.get(j).toLowerCase(Locale.ROOT).contains(wel.toLowerCase(Locale.ROOT))){
    su=statelist.get(j).toLowerCase(Locale.ROOT);
            PolylineOptions polygonOptions=new PolylineOptions().color(R.color.y2).width(18);
    for (int i = 0; i < locations.length(); i++){
        try {
            JSONObject location = locations.getJSONObject(i);
            double lat = location.getDouble("lat");
            double lon = location.getDouble("lon");
            String state = location.getString("state").toLowerCase(Locale.ROOT);
            if (state.equals(su)){
//                Log.e("TAG", ""+su+"|"+state );
                polygonOptions.add(new LatLng(lat,lon));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    mMap.addPolyline(polygonOptions.color(getResources().getColor(R.color.y2)));
    }
    }
} else {
      for (int j = 0; j <statelist.size() ; j++) {
              su=statelist.get(j).toLowerCase(Locale.ROOT);
              PolylineOptions polygonOptions=new PolylineOptions().color(R.color.y2).width(18);
              for (int i = 0; i < locations.length(); i++){
                  try {
                      JSONObject location = locations.getJSONObject(i);
                      double lat = location.getDouble("lat");
                      double lon = location.getDouble("lon");
                      String state = location.getString("state").toLowerCase(Locale.ROOT);
                      if (state.equals(su)){
                          polygonOptions.add(new LatLng(lat,lon)).color(getResources().getColor(R.color.y2));
                      }
                  } catch (JSONException e) {
                      throw new RuntimeException(e);
                  }
              }
              mMap.addPolyline(polygonOptions);
      }
  }
    }

    public void terr(View view){
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.railmap    ));

            if (!success) {
                Log.e("MapStyle", "Не удалось применить стиль");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapStyle", "Стиль не найден: ", e);
        }
    }
    public void sate(View view){
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }


    public void copyJsonToCache(Context context, String fileName) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            // Открытие файла из assets
            inputStream = context.getAssets().open(fileName);

            // Создание файла в папке кэша
            File cacheFile = new File(context.getCacheDir(), fileName);
            outputStream = new FileOutputStream(cacheFile);

            // Буфер для чтения и записи
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            System.out.println("Файл успешно скопирован в кэш: " + cacheFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Закрытие потоков
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void keymir(View view){
        Toast.makeText(getApplicationContext(),"\uD83D\uDD11\uD83C\uDF0E\uD83D\uDE86",Toast.LENGTH_LONG).show();
    }
}