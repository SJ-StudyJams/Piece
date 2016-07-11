package com.sealiu.piece.controller.Maps;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by liuyang
 * on 2016/7/11.
 */
public class MapStateManager {
    private static final String LNG = "longitude";
    private static final String LAT = "latitude";
    private static final String ZOOM = "zoom";
    private static final String BEARING = "bearing";
    private static final String TILT = "tilt";
    private static final String MAP_TYPE = "mapType";

    private static final String PREFS_NAME = "mapCameraState";

    private SharedPreferences mapStatePrefs;

    public MapStateManager(Context context) {
        mapStatePrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveMapState(GoogleMap map) {
        SharedPreferences.Editor editor = mapStatePrefs.edit();
        CameraPosition position = map.getCameraPosition();

        editor.putFloat(LAT, (float) position.target.latitude);
        editor.putFloat(LNG, (float) position.target.longitude);
        editor.putFloat(ZOOM, position.zoom);
        editor.putFloat(BEARING, position.bearing);
        editor.putFloat(TILT, position.tilt);
        editor.putInt(MAP_TYPE, map.getMapType());

        editor.apply();
    }

    public CameraPosition getSavedCameraPosition() {
        double latitude = mapStatePrefs.getFloat(LAT, 0);
        if (latitude == 0) {
            return null;
        }
        double longitude = mapStatePrefs.getFloat(LNG, 0);
        LatLng target = new LatLng(latitude, longitude);

        float zoom = mapStatePrefs.getFloat(ZOOM, 0);
        float bearing = mapStatePrefs.getFloat(BEARING, 0);
        float tilt = mapStatePrefs.getFloat(TILT, 0);
        float map_type = mapStatePrefs.getInt(MAP_TYPE, GoogleMap.MAP_TYPE_NORMAL);

        CameraPosition position = new CameraPosition(target, zoom, tilt, bearing);
        return position;
    }
}
