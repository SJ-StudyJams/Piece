package com.sealiu.piece.controller.Maps;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by liuyang
 * on 2016/7/15.
 */
public class ClusterMarkerLocation implements ClusterItem {

    private LatLng position;

    public ClusterMarkerLocation(LatLng ll) {
        this.position = ll;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }
}
