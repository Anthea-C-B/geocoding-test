package com.example.anthe.geocoderexample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button show;
    TextView dets;
    final int MY_PERMISSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show = (Button) findViewById(R.id.tbnShowLoc);
        dets = (TextView) findViewById(R.id.txtvwMyLoc);


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale
                            (MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)){
                        ActivityCompat.requestPermissions
                                (MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSION);
                    }else{
                        ActivityCompat.requestPermissions
                                (MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSION);
                    }
                }else{
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try{
                        dets.setText(myLocation(location.getLatitude(),location.getLongitude()));
                    }catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Not found !",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    public void onRequestPermissionsResult(int requestCode, String[] permission, int [] grantResults){
        super.onRequestPermissionsResult(requestCode,permission,grantResults);

        switch(requestCode){
            case MY_PERMISSION:{
                if(grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission
                            (MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        try{
                            dets.setText(myLocation(location.getLatitude(),location.getLongitude()));
                        }catch(Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Not found !",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"No permission granted !",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    public String myLocation(double lat, double lon){
        String city = "";
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

        List<Address> addressLst;

        try{
            addressLst = geocoder.getFromLocation(lat,lon,1);
            if(addressLst.size() > 0){
                city = addressLst.get(0).getLocality() + " "
                        + addressLst.get(0).getAddressLine(0)+" "
                        +addressLst.get(0).getAdminArea()+ " "
                        +addressLst.get(0).getFeatureName()+" "
                        +addressLst.get(0).getPostalCode();

            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return  city;
    }
}
