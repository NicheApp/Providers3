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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class electrician extends Fragment implements OnMapReadyCallback {
    Location currentLocation;
    private AWSAppSyncClient mAWSAppSyncClient;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private BottomSheetBehavior bottomSheetBehavior;
    public View view;
   public location loc;
    public int items;
    LinearLayout phonecall;
public Marker markerworker;
TextView workername;
  public   List<location> locationList;

    TextView  name,address,jobs,rating,phone;
    RatingBar ratingBar;
    ImageButton phonenumber;
    String[] profiledata;
    String alldata,number;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view=inflater.inflate(R.layout.samebottom,null);
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
        workername=view.findViewById(R.id.worker);
        address=view.findViewById(R.id.addressnum);
        ratingBar=view.findViewById(R.id.ratingnum);
        phone=view.findViewById(R.id.phonenum);
        jobs=view.findViewById(R.id.orders);
        phonecall=view.findViewById(R.id.phonecall);
        initComponent();

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
                    supportMapFragment.getMapAsync(electrician.this);
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
if (locationList.get(i).getOccupation().equals("electrician")) {
    createMarker(Double.parseDouble(locationList.get(i).getLatitude()), Double.parseDouble(locationList.get(i).getLongitude()), locationList.get(i).getName(), googleMap, locationList.get(i).getPhoneNo(),locationList.get(i).getJobs(),locationList.get(i).getAdress());
} }
       googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
           @Override
           public boolean onMarkerClick(Marker marker) {
               bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
               alldata=marker.getTitle();
               profiledata=alldata.split("@");
               workername.setText(profiledata[0]);
               address.setText(profiledata[1]);
               jobs.setText("Jobs: "+profiledata[3]);
               number=profiledata[2];
               phone.setText(number);
         /*    //  rating.setText("Rating: 4/5");
//change made here----------------------------------------------------------------------------------------

               phonecall.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent i=new Intent(Intent.ACTION_DIAL);
                       i.setData(Uri.parse("tel:"+number));
                       startActivity(i);

                   }
               });*/
 //change made here-------------------------------------------------------------------------
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
                .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_electrician))

        );



    }
    private void initComponent() {
        // get the bottom sheet view
        LinearLayout llBottomSheet = (LinearLayout) view.findViewById(R.id.bottom_sheet);

        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        // change the state of the bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        ((FloatingActionButton) view.findViewById(R.id.fab_directions)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        });
    }
}