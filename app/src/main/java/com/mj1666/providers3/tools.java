package com.mj1666.providers3;

import com.google.android.gms.maps.GoogleMap;

public class tools {

    public static GoogleMap configActivityMaps(GoogleMap googleMap) {
        // set map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Enable / Disable zooming controls
        googleMap.getUiSettings().setZoomControlsEnabled(false);

        // Enable / Disable Compass icon
        googleMap.getUiSettings().setCompassEnabled(true);
        // Enable / Disable Rotate gesture
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        // Enable / Disable zooming functionality
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);

        return googleMap;
    }

}