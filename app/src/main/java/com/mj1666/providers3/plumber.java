package com.mj1666.providers3;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.amazonaws.amplify.generated.graphql.ListTodosQuery;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class plumber extends Fragment implements OnMapReadyCallback {
    Location currentLocation;
    private AWSAppSyncClient mAWSAppSyncClient;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    public location loc;
    public int items;
    public Marker markerworker;
    public   List<location> locationList;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_plumber,null);
        locationList=new ArrayList<location>();
        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getContext())
                .awsConfiguration(new AWSConfiguration(getContext()))
                .build();
        mAWSAppSyncClient.query(ListTodosQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(todosCallback);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        fetchLocation(locationList);

        return view;

    }
    private void fetchLocation(List<location> locationList) {
        if (ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    //SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    SupportMapFragment supportMapFragment=(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(plumber.this);
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        Marker marker=  googleMap.addMarker(new MarkerOptions().position(latLng).title("I am here!"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        marker.showInfoWindow();

        marker.showInfoWindow();


        for(int i = 0 ; i < locationList.size() ; i++) {
            if (locationList.get(i).getOccupation().equals("plumber")) {
                createMarker(Double.parseDouble(locationList.get(i).getLatitude()), Double.parseDouble(locationList.get(i).getLongitude()), locationList.get(i).getName(), googleMap, locationList.get(i).getPhoneNo(),locationList.get(i).getJobs(),locationList.get(i).getAdress());
            } }
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent profileintent=new Intent(getActivity(),profile.class);
                profileintent.putExtra("profile",marker.getTitle().toString());
                startActivity(profileintent);
                return true;
            }
        });

    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResid){
        Drawable vectorDrawable = ContextCompat.getDrawable(context,vectorResid);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation(locationList);
                }
                break;
        }
    }
    private GraphQLCall.Callback<ListTodosQuery.Data> todosCallback = new GraphQLCall.Callback<ListTodosQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListTodosQuery.Data> response) {
            Log.i("Results", response.data().listTodos().items().toString());
            items=     response.data().listTodos().items().size();
            Log.i("size",items+"");
            for(int i=0;i<items;i++) {
                loc=new location( response.data().listTodos().items().get(i).name(),response.data().listTodos().items().get(i).Latitude(),response.data().listTodos().items().get(i).Longitude(),response.data().listTodos().items().get(i).PhoneNo(),response.data().listTodos().items().get(i).Address(),response.data().listTodos().items().get(i).Occupation(),3);
                locationList.add(i,loc);
            }
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("ERROR", e.toString());
        }
    };
    public void createMarker(double latitude, double longitude,  String name, GoogleMap googleMap, String PhoneNo, int jobs,  String address) {

        markerworker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))

                .title(name + "@" + address + "@" + PhoneNo + "@" + jobs).snippet("press the icon to call")
                .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_plumber))

        );



    }
}