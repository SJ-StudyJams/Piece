package com.studyjams.piece.controller.Piece;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.studyjams.piece.R;

import java.util.Set;

public class PiecesActivity extends AppCompatActivity {

    private static final String TAG = "PiecesActivity";
    double mLat;
    double mLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pieces);

        mLat = getIntent().getDoubleExtra("lat", 0);
        mLng = getIntent().getDoubleExtra("lng", 0);

        Log.i(TAG, "lat: " + mLat + "; lng: " + mLng);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> piecesSet = SP.getStringSet("pref_piece_nearby_key", null);
        if (piecesSet == null || piecesSet.contains("5")) {
            // 广告
            Log.d(TAG, "AD!");
            AdView mAdView = (AdView) findViewById(R.id.adView);
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        Bundle bundle = new Bundle();
        bundle.putDouble("lat", mLat);
        bundle.putDouble("lng", mLng);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.near_pieces_fragment_container);

        if (fragment == null) {
            fragment = new NearPiecesFragment();
            fragment.setArguments(bundle);

            fm.beginTransaction()
                    .add(R.id.near_pieces_fragment_container, fragment)
                    .commit();
        }
    }
}
