package com.sealiu.piece.controller.Maps;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.google.maps.android.clustering.ClusterManager;
import com.sealiu.piece.R;
import com.sealiu.piece.controller.LoginRegister.LoginActivity;
import com.sealiu.piece.controller.Piece.PiecesActivity;
import com.sealiu.piece.controller.Piece.WritePieceActivity;
import com.sealiu.piece.controller.Settings.MyPreferenceActivity;
import com.sealiu.piece.controller.User.UserActivity;
import com.sealiu.piece.controller.User.UserInfoSync;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.Piece;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.SPUtils;

import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.google.android.gms.common.api.GoogleApiClient.Builder;
import static com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import static com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        ConnectionCallbacks,
        OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "MapsActivity";
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_FINE_COARSE_LOCATION = 2;
    private static final int WRITE_PIECE_REQUEST_CODE = 3;
    private static final int ERROR = 4;

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;

    TextView displayCurrentPosition;
    TextView pieceNumberNear;
    ImageButton findMyLocationBtn;
    ImageButton hideShowMoreInfoBtn;
    LinearLayout moreInfoLayout;
    Button seeAllBtn;
    FloatingActionButton writePieceBtn;

    private RelativeLayout snackBarHolderView;
    private GoogleMap mMap;
    private Double mCurrentLatitude, mCurrentLongitude;
    private String mCurrentLocationName;

    private ClusterManager<ClusterMarkerLocation> clusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 从bmob后台同步用户信息到sp文件中存储
        UserInfoSync sync = new UserInfoSync();
        try {
            sync.getUserInfo(this, Constants.SP_FILE_NAME, SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_USERNAME, null));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGoogleApiClient = new Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        snackBarHolderView = (RelativeLayout) findViewById(R.id.content_holder);
        displayCurrentPosition = (TextView) findViewById(R.id.position_info);
        pieceNumberNear = (TextView) findViewById(R.id.piece_number_nearby);
        moreInfoLayout = (LinearLayout) findViewById(R.id.more_info_panel);

        writePieceBtn = (FloatingActionButton) findViewById(R.id.write_piece_fab);
        findMyLocationBtn = (ImageButton) findViewById(R.id.find_my_location);
        hideShowMoreInfoBtn = (ImageButton) findViewById(R.id.hide_show);
        seeAllBtn = (Button) findViewById(R.id.see_all_btn);


        writePieceBtn.setOnClickListener(this);
        findMyLocationBtn.setOnClickListener(this);
        hideShowMoreInfoBtn.setOnClickListener(this);
        seeAllBtn.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
        switch (requestCode) {
            case WRITE_PIECE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Snackbar.make(snackBarHolderView, "发送成功", Snackbar.LENGTH_LONG).show();
                } else if (resultCode == ERROR) {
                    int errorCode = data.getIntExtra("errorCode", 0);
                    Snackbar.make(snackBarHolderView, "发送失败 错误码：" + Constants.createErrorInfo(errorCode), Snackbar.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
        MapStateManager mgr = new MapStateManager(this);
        mgr.saveMapState(mMap);
        mGoogleApiClient.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu");
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
                new AlertDialog.Builder(this)
                        .setTitle("退出账户")
                        .setMessage("执行该操作后你需要重新登录")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SPUtils.putBoolean(MapsActivity.this, Constants.SP_FILE_NAME, Constants.SP_IS_LOGIN, false);
                                startActivity(new Intent(MapsActivity.this, LoginActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "Map Ready");

        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setTiltGesturesEnabled(false);

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
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        mMap.getUiSettings().setZoomControlsEnabled(false);
                        mMap.getUiSettings().setMapToolbarEnabled(false);
                        mMap.getUiSettings().setCompassEnabled(false);
                        mMap.getUiSettings().setTiltGesturesEnabled(false);
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

                        initMarker();

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
                        gotoLocation(latLng, 14, false);
                    }
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Service Connected");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                mCurrentLatitude = mLastLocation.getLatitude();
                mCurrentLongitude = mLastLocation.getLongitude();

                //初始化标记
                initMarker();
                //mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

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
                gotoLocation(latLng, 14, false);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.write_piece_fab:
                if (isValidLocation()) {
                    Intent intent = new Intent(MapsActivity.this, WritePieceActivity.class);
                    intent.putExtra("LAT", mCurrentLatitude);
                    intent.putExtra("LNG", mCurrentLongitude);
                    intent.putExtra("LOC", mCurrentLocationName);
                    startActivityForResult(intent, WRITE_PIECE_REQUEST_CODE);
                } else {
                    Snackbar.make(snackBarHolderView, "无法获取你的位置", Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.find_my_location:
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
                        float currentZoom = mMap.getCameraPosition().zoom;
                        if (currentZoom > 16 || currentZoom < 12) {
                            gotoLocation(latLng, 14, true);
                        } else {
                            gotoLocation(latLng, currentZoom, true);
                        }
                    }
                }
                break;
            case R.id.hide_show:
                Animation expand = AnimationUtils.loadAnimation(getBaseContext(), R.anim.expand_panel);
                if (moreInfoLayout.getVisibility() == View.VISIBLE) {
                    hideShowMoreInfoBtn.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    moreInfoLayout.setVisibility(View.GONE);
                } else if (moreInfoLayout.getVisibility() == View.GONE) {
                    moreInfoLayout.startAnimation(expand);
                    hideShowMoreInfoBtn.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    moreInfoLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.see_all_btn:
                if (mCurrentLatitude != null) {
                    Intent intent = new Intent(MapsActivity.this, PiecesActivity.class);
                    intent.putExtra("lat", mCurrentLatitude);
                    intent.putExtra("lng", mCurrentLongitude);
                    startActivity(intent);
                } else {
                    Snackbar.make(snackBarHolderView, "无法获取你的位置", Snackbar.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }

    /**
     * 获取指定经纬度的地理位置名称
     */
    private String getPositionName(Double latitude, Double longitude) throws IOException {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
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

    private boolean isValidLocation() {
        return (mCurrentLatitude != null && mCurrentLongitude != null && mCurrentLocationName != null);
    }

    private void initMarker() {

        clusterManager = new ClusterManager<ClusterMarkerLocation>(this, mMap);
        mMap.setOnCameraChangeListener(clusterManager);

        //因为纸条的可见范围最大为100km，所以默认为100km
        double[] llRange = Common.GetAround(mCurrentLatitude, mCurrentLongitude, 100000);

        BmobQuery<Piece> query = new BmobQuery<>();
        query.addWhereGreaterThanOrEqualTo("latitude", llRange[0])
                .addWhereLessThanOrEqualTo("latitude", llRange[2])
                .addWhereGreaterThanOrEqualTo("longitude", llRange[1])
                .addWhereLessThanOrEqualTo("longitude", llRange[3]);
        query.setLimit(100);
        query.order("-createdAt,-updatedAt");
        query.findObjects(new FindListener<Piece>() {
            @Override
            public void done(List<Piece> list, BmobException e) {
                Log.i(TAG, list.size() + "");
                int number = 0;
                for (final Piece p : list) {
                    int pr = 0;
                    switch (p.getVisibility()) {
                        case 0:
                            pr = 5000;
                            break;
                        case 1:
                            pr = 20000;
                            break;
                        case 2:
                            pr = 60000;
                            break;
                        case 3:
                            pr = 100000;
                            break;
                        default:
                    }
                    double distance = Common.GetDistance(mCurrentLatitude, mCurrentLongitude, p.getLatitude(), p.getLongitude());

                    if (distance <= pr) {
                        number++;

                        BmobQuery<User> query = new BmobQuery<>();
                        query.addWhereEqualTo("objectId", p.getAuthorID());
                        query.findObjects(new FindListener<User>() {
                            @Override
                            public void done(List<User> list, BmobException e) {
                                if (e == null) {
                                    LatLng ll = new LatLng(p.getLatitude(), p.getLongitude());
                                    clusterManager.addItem(new ClusterMarkerLocation(ll));
                                }
                            }
                        });

                    }
                }//for
                String info = "附近有 " + number + " Pieces";
                pieceNumberNear.setText(info);
            }//done
        });
    }

//    class CustomInfoWindowAdapter implements InfoWindowAdapter {
//
//        private View mContents;
//
//        CustomInfoWindowAdapter() {
//            mContents = getLayoutInflater().inflate(R.layout.info_window_w, null);
//        }
//
//        @Override
//        public View getInfoWindow(Marker marker) {
//            return null;
//        }
//
//        @Override
//        public View getInfoContents(Marker marker) {
//            String title = marker.getTitle();
//            String snippet = marker.getSnippet();
//
//            String[] contentAndDate = snippet.split("::");
//
//            TextView nickname = (TextView) mContents.findViewById(R.id.info_window_nickname);
//            TextView content = (TextView) mContents.findViewById(R.id.info_window_content);
//            TextView time = (TextView) mContents.findViewById(R.id.info_window_time);
//
//            if (title != null)
//                nickname.setText(title);
//            else
//                nickname.setText("");
//
//            if (contentAndDate.length == 2) {
//                content.setText(contentAndDate[0]);
//                time.setText(contentAndDate[1]);
//            } else {
//                content.setText("");
//            }
//            return mContents;
//        }
//    }

    @Override
    protected void onDestroy() {
        //Intent intent = new Intent(this, PieceMainService.class);
        //stopService(intent);
        super.onDestroy();
    }
}
