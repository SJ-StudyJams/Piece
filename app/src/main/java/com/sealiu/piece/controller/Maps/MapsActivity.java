package com.sealiu.piece.controller.Maps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sealiu.piece.R;
import com.sealiu.piece.controller.LoginRegister.LoginActivity;
import com.sealiu.piece.controller.Piece.WritePieceActivity;
import com.sealiu.piece.controller.Settings.MyPreferenceActivity;
import com.sealiu.piece.controller.User.UserActivity;
import com.sealiu.piece.controller.User.UserInfoSync;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.utils.SPUtils;

import java.io.IOException;
import java.util.List;

import static com.google.android.gms.common.api.GoogleApiClient.Builder;
import static com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import static com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        ConnectionCallbacks,
        OnConnectionFailedListener {

    private static final String TAG = "MapsActivity";
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_FINE_COARSE_LOCATION = 2;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    TextView displayCurrentPosition;
    private NestedScrollView snackBarHolderView;
    private GoogleMap mMap;
    private Double mCurrentLatitude, mCurrentLongitude;
    private String mCurrentLocationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 从bmob后台同步用户信息到sp文件中存储
        UserInfoSync sync = new UserInfoSync();
        try {
            sync.getUserInfo(this, Constants.SP_FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }

        snackBarHolderView = (NestedScrollView) findViewById(R.id.content_holder);
        displayCurrentPosition = (TextView) findViewById(R.id.position_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGoogleApiClient = new Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        FloatingActionButton writePiectBtn = (FloatingActionButton) findViewById(R.id.write_piece_fab);
        writePiectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionsMenu floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.floating_actions_menu);
                floatingActionsMenu.collapse();

                if (isValidLocation()) {
                    Intent intent = new Intent(MapsActivity.this, WritePieceActivity.class);
                    intent.putExtra("LAT", mCurrentLatitude);
                    intent.putExtra("LNG", mCurrentLongitude);
                    intent.putExtra("LOC", mCurrentLocationName);
                    startActivity(intent);
                } else {
                    Snackbar.make(snackBarHolderView, "无法确定你的位置", Snackbar.LENGTH_LONG)
                            .setAction("获取位置信息", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                            == PackageManager.PERMISSION_GRANTED) {
                                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                                mGoogleApiClient);
                                        if (mLastLocation != null) {
                                            mCurrentLatitude = mLastLocation.getLatitude();
                                            mCurrentLongitude = mLastLocation.getLongitude();

                                            try {
                                                mCurrentLocationName = getPositionName(mCurrentLatitude, mCurrentLongitude);
                                                String detailPosition = mCurrentLocationName + " (" + mCurrentLatitude + " ," + mCurrentLongitude + ")";
                                                displayCurrentPosition.setText(detailPosition);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            LatLng latLng = new LatLng(mCurrentLatitude, mCurrentLongitude);
                                            gotoLocation(latLng, 15, false);
                                        }
                                    }
                                }
                            }).show();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MapStateManager mgr = new MapStateManager(this);
        mgr.saveMapState(mMap);
        mGoogleApiClient.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings_menu_title:
                startActivity(new Intent(MapsActivity.this, MyPreferenceActivity.class));
                break;
            case R.id.user_menu_title:
                startActivity(new Intent(MapsActivity.this, UserActivity.class));
                break;
            case R.id.switch_menu_title:
                SPUtils.putBoolean(this, Constants.SP_FILE_NAME, Constants.SP_IS_LOGIN, false);
                startActivity(new Intent(MapsActivity.this, LoginActivity.class));
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Snackbar.make(snackBarHolderView, "Map Ready",
                Snackbar.LENGTH_LONG).show();

        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(false);

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_REQUEST_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted!
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    }
                }
                break;
            case PERMISSIONS_REQUEST_FINE_COARSE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted!

                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                            mGoogleApiClient);
                    if (mLastLocation != null) {
                        mCurrentLatitude = mLastLocation.getLatitude();
                        mCurrentLongitude = mLastLocation.getLongitude();

                        try {
                            mCurrentLocationName = getPositionName(mCurrentLatitude, mCurrentLongitude);
                            String detailPosition = mCurrentLocationName + " (" + mCurrentLatitude + " ," + mCurrentLongitude + ")";
                            displayCurrentPosition.setText(detailPosition);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    MapStateManager mgr = new MapStateManager(this);
                    CameraPosition position = mgr.getSavedCameraPosition();

                    if (position != null) {
                        CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
                        mMap.moveCamera(update);
                    } else {
                        LatLng latLng = new LatLng(mCurrentLatitude, mCurrentLongitude);
                        gotoLocation(latLng, 15, false);
                    }
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Snackbar.make(snackBarHolderView, "成功连接 Google API",
                Snackbar.LENGTH_LONG).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                mCurrentLatitude = mLastLocation.getLatitude();
                mCurrentLongitude = mLastLocation.getLongitude();

                try {
                    mCurrentLocationName = getPositionName(mCurrentLatitude, mCurrentLongitude);
                    String detailPosition = mCurrentLocationName + " (" + mCurrentLatitude + " ," + mCurrentLongitude + ")";
                    displayCurrentPosition.setText(detailPosition);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            MapStateManager mgr = new MapStateManager(this);
            CameraPosition position = mgr.getSavedCameraPosition();

            if (position != null) {
                CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
                mMap.moveCamera(update);
            } else {
                LatLng latLng = new LatLng(mCurrentLatitude, mCurrentLongitude);
                gotoLocation(latLng, 15, false);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, PERMISSIONS_REQUEST_FINE_COARSE_LOCATION);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(snackBarHolderView, "连接 Google API 失败",
                Snackbar.LENGTH_LONG).show();
    }


    /**
     * 获取指定经纬度的地理位置名称
     */
    private String getPositionName(Double latitude, Double longtitude) throws IOException {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = geocoder.getFromLocation(latitude, longtitude, 1);
        if (addresses.size() == 0)
            return null;
        else return addresses.get(0).getLocality();
    }

    /**
     * 将镜头移动至指定地点，指定缩放等级，并指定是否使用动画
     */
    private void gotoLocation(LatLng ll, float zoom, boolean animate) {
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        if (animate)
            mMap.animateCamera(update);
        else
            mMap.moveCamera(update);
    }

    /**
     * 将镜头移动至指定地点，指定缩放等级，指定是否使用动画，并在指定位置创建一个maker
     */
    private void gotoLocation(LatLng ll, float zoom, boolean animate, String makerTitle) {
        gotoLocation(ll, zoom, animate);
        mMap.addMarker(new MarkerOptions().position(ll).title(makerTitle));
    }

    private boolean isValidLocation() {
        return (mCurrentLatitude != null && mCurrentLongitude != null && mCurrentLocationName != null);
    }
}
