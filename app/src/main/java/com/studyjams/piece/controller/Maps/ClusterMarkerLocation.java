package com.studyjams.piece.controller.Maps;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by liuyang
 * on 2016/7/15.
 */
class ClusterMarkerLocation implements ClusterItem {

    private LatLng position;
    private String title;
    private String snippet;

    public ClusterMarkerLocation(LatLng ll, String title, String snippet) {
        this.position = ll;
        this.title = title;
        this.snippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public String getTitle() {
        return title;
    }

    String getSnippet() {
        return snippet;
    }
}
