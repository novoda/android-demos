package com.novoda.mapdemo.fragment;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class NovodaMapFragment extends SupportMapFragment {

    private static final int ZOOM_LEVEL = 16;
    private GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        View view = super.onCreateView(inflater, viewGroup, bundle);
        setUpMapIfNeeded();
        return view;
    }

    private void setUpMapIfNeeded() {
        if (map == null) {
            map = getMap();
            if (map != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        map.setMyLocationEnabled(true);
        map.animateCamera(zoomToLastKnownLatLng());
    }

    private CameraUpdate zoomToLastKnownLatLng() {
        return CameraUpdateFactory.newLatLngZoom(getLastKnownLatLng(), ZOOM_LEVEL);
    }

    private LatLng getLastKnownLatLng() {
        Location location = ((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE)).getLastKnownLocation("gps");
        return locationToLatLng(location);
    }

    private LatLng locationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

}
