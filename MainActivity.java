package com.finalproject3.amir.testgooglemaps;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private GoogleMapViewFragment mGoogleMapViewFragment;
    String username="";
    String places="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
         username = intent.getStringExtra("username");
         places = intent.getStringExtra("places");
//        Log.d("username", "array_rows:" + username + places);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    private void initActivity() {
        initGoogleMapViewFragment();
    }

    private void initGoogleMapViewFragment() {

        mGoogleMapViewFragment = new GoogleMapViewFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.FragmentsContainer, mGoogleMapViewFragment, GoogleMapViewFragment.TAG);
        fragmentTransaction.addToBackStack(GoogleMapViewFragment.TAG);
        fragmentTransaction.commit();

        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.autoRoute:
                GoogleMapViewFragment map = (GoogleMapViewFragment) getFragmentManager().findFragmentByTag(mGoogleMapViewFragment.TAG);
                if (map != null) {
//                   Toast.makeText(this, places , Toast.LENGTH_SHORT).show();
                    map.calcRouteForPerson(places);
                }
                return true;



            case R.id.PlaceList:
                Toast.makeText(this, "placeList", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this , ListViewCheckboxesActivity.class);
//                MainActivity.this.startActivity(intent);
                listViewCheckboxFragment fr = new listViewCheckboxFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.add(R.id.FragmentsContainer, fr );
                fragmentTransaction.addToBackStack(fr.TAG);
                fragmentTransaction.commit();

                fm.executePendingTransactions();

                return true;

            case R.id.CalculateRoute:
                map = (GoogleMapViewFragment) getFragmentManager().findFragmentByTag(mGoogleMapViewFragment.TAG);
                if (map != null) {
                    map.calcRoute();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}





