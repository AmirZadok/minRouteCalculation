package com.finalproject3.amir.testgooglemaps;

import android.Manifest;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

//import android.support.annotation.Nullable;
//import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class GoogleMapViewFragment extends Fragment implements OnMapReadyCallback {
    public static final String TAG = GoogleMapViewFragment.class.getSimpleName();
    private View mRootView;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private ArrayList<Polyline> Polylines = new ArrayList<Polyline>();
    private ArrayList<Marker> markerPoints = new ArrayList<Marker>();
    StringBuilder placesURL = new StringBuilder();
    ArrayList<String> Route = new ArrayList<String>();
    ArrayList<deliveris> deliveriesArray = new ArrayList<deliveris>();
    int[] imageArray = new int[11];
    GPSTracker gps;
    private static final int PERMISSION_CALLBACK_CONSTANT = 105;
    private SharedPreferences permissionStatus;
    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mRootView = inflater.inflate(R.layout.googlemap_view_fragment, container, false);
        initFragment();
        return mRootView;
    }


    private void initFragment() {
        initGoogleMapFragment();
    }

    private void initGoogleMapFragment() {
        mMapView = (MapView) mRootView.findViewById(R.id.MapView);
        mMapView.onCreate(null);
        mMapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        if (mGoogleMap != null) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            checkPerm();
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setTrafficEnabled(true);

            // create class object
            gps = new GPSTracker(getActivity());
            // check if GPS enabled
            if (gps.canGetLocation()) {
                double myLocLatitude = gps.getLatitude();
                double myLocLongitude = gps.getLongitude();
                Toast.makeText(getActivity(), "Your Location is - \nLat: " + myLocLatitude + "\nLong: " + myLocLongitude, Toast.LENGTH_LONG).show();
                LatLng lng = new LatLng(myLocLatitude,myLocLongitude);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lng,10));
                // \n is for new line
//            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();

            }

            PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                    getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    // TODO: Get info about the selected place.
                    Log.d("Amir", "place adress" + place.getAddress());
                    deliveriesArray.add(new deliveris((String) place.getAddress(),"",place.getLatLng().latitude,place.getLatLng().longitude));
                    markerPoints.add(mGoogleMap.addMarker(new MarkerOptions().position(place.getLatLng()).title((String) place.getAddress())));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 10));
                }
                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                }
            });




        }
    }
    @AfterPermissionGranted(123)
    private void checkPerm() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            Toast.makeText(mMapView.getContext(), "Opening camera", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(getActivity(), "We need permissions because this and that",
                    123, perms);
        }
    }

    public void calcRoute()
    {
        imageArray[0] = R.drawable.home;
        imageArray[1] = R.drawable.number_1;
        imageArray[2] = R.drawable.number_2;
        imageArray[3] = R.drawable.number_3;
        imageArray[4] = R.drawable.number_4;
        imageArray[5] = R.drawable.number_5;
        imageArray[6] = R.drawable.number_6;
        imageArray[7] = R.drawable.number_7;
        imageArray[8] = R.drawable.number_8;
        imageArray[9] = R.drawable.number_9;
        imageArray[10] = R.drawable.number_10;



        if( deliveriesArray.size()>1)  {
            for (int i = 0; i < deliveriesArray.size(); i++) {
                placesURL.append(deliveriesArray.get(i).getPlace());
                placesURL.append("|");
            }
            String placesString;
            placesString = placesURL.toString().replaceAll("\\s+","");
//            Log.d("Amir", "Amir" + placesString);
            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + placesString + "&destinations=" + placesString + "&mode=driving&language=fr-FR&avoid=tolls&key=Need to add key";
            Log.d("JSON", "array_rows:" + url);
            placesString = "";
            placesURL.setLength(0);

            try {
                Route = new GeoTask(getActivity()).execute(url).get();
//                Log.d("JSON", "the route::" + Route);
//                Log.d("JSON", "markerPoints:" + );
                for (int i=0 ; i<Route.size();i++)
                {
//                    Log.d("JSON", "markerPoints:" +i+ markerPoints.get(i).getTitle());
//                    Log.d("JSON", "deliveryArray:" +i+ deliveriesArray.get(i).getPlace());
                    markerPoints.get(Integer.parseInt(Route.get(i))).setIcon(BitmapDescriptorFactory.fromResource(imageArray[i]));
                    markerPoints.get(Integer.parseInt(Route.get(i))).setSnippet(deliveriesArray.get(Integer.parseInt(Route.get(i))).getOrder());
                    markerPoints.get(Integer.parseInt(Route.get(i))).showInfoWindow();
                    markerPoints.get(Integer.parseInt(Route.get(i))).setAlpha(0.9F);
//                    if (i != (Route.size()-1))
//                    Polylines.add(i, mGoogleMap.addPolyline(new PolylineOptions()
//                            .add(placesArray.get(Integer.parseInt(Route.get(i))).getLatLng(), placesArray.get(Integer.parseInt(Route.get(i+1))).getLatLng())
//                            .width(5)
//                            .color(Color.BLUE)));
//                    else
//                        Polylines.add(i, mGoogleMap.addPolyline(new PolylineOptions()
//                                .add(placesArray.get(Integer.parseInt(Route.get(i))).getLatLng(), placesArray.get(0).getLatLng())
//                                .width(5)
//                                .color(Color.BLUE)));

                  //  if (i != (Route.size()-1) )
                  //  {
                   //     Polyline line = mGoogleMap.addPolyline(new PolylineOptions()
                    //            .add(placesArray.get(Integer.parseInt(Route.get(i))).getLatLng(), placesArray.get(Integer.parseInt(Route.get(i) + 1)).getLatLng())
                    //            .width(5)
                     //           .color(Color.RED));
                //    }

                }



                markerPoints.get(Integer.parseInt(Route.get(0))).showInfoWindow();



            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


        }

    }


    public void calcRouteForPerson(String places) {
//
try {
//    Log.d("JSON", "places:" +places);
    JSONObject root = new JSONObject(places);
//   Log.d("JSON", "go throw root:" +root);
    JSONArray autoplacesArray=root.getJSONArray("autoplaces");
//   Log.d("JSON", "go throw autoplaces:" +autoplacesArray);
//

    for (int i=0 ; i<autoplacesArray.length() ; i++) {

        JSONObject autoPlaceElement = autoplacesArray.getJSONObject(i);
        deliveriesArray.add(new deliveris(autoPlaceElement.optString("title").toString(),autoPlaceElement.optString("order").toString()
                                            ,Double.parseDouble(autoPlaceElement.optString("lat").toString()),Double.parseDouble(autoPlaceElement.optString("lng").toString())));

        markerPoints.add(mGoogleMap.addMarker(new MarkerOptions().position
                (new LatLng(Double.parseDouble(autoPlaceElement.optString("lat").toString()),Double.parseDouble(autoPlaceElement.optString("lng").toString())))
                .title(autoPlaceElement.optString("title").toString())
                .snippet(autoPlaceElement.optString("order").toString())));
    }
//   calcRoute();

}catch (Exception e) {
           e.printStackTrace(); }

//        }

    }

    public ArrayList<deliveris> getDeliveriesArray() {
        return deliveriesArray;
    }

    public void setDeliveriesArray(ArrayList<deliveris> deliveriesArray) {
        this.deliveriesArray = deliveriesArray;

    }

    public ArrayList<Marker> getMarkerPoints() {
        return markerPoints;
    }

    public void setMarkerPoints(ArrayList<Marker> markerPoints) {
        this.markerPoints = markerPoints;
    }
}




