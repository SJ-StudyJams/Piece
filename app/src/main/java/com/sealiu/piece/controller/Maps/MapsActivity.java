package com.sealiu.piece.controller.Maps;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.sealiu.piece.R;
import com.sealiu.piece.controller.LoginRegister.LoginActivity;
import com.sealiu.piece.controller.Piece.PieceDetailActivity;
import com.sealiu.piece.controller.Piece.PiecesActivity;
import com.sealiu.piece.controller.Piece.WritePieceActivity;
import com.sealiu.piece.controller.Settings.MyPreferenceActivity;
import com.sealiu.piece.controller.User.UserActivity;
import com.sealiu.piece.controller.User.UserInfoSync;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.Piece;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.SPUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.ValueEventListener;

import static com.google.android.gms.common.api.GoogleApiClient.Builder;
import static com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import static com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import static com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import static com.google.maps.android.clustering.ClusterManager.OnClusterItemClickListener;
import static com.google.maps.android.clustering.ClusterManager.OnClusterItemInfoWindowClickListener;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        ConnectionCallbacks,
        OnConnectionFailedListener,
        View.OnClickListener,
        OnClusterItemClickListener<ClusterMarkerLocation>,
        OnClusterItemInfoWindowClickListener<ClusterMarkerLocation> {

    private static final String TAG = "MapsActivity";
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_FINE_COARSE_LOCATION = 2;
    private static final int WRITE_PIECE_REQUEST_CODE = 3;
    private static final int ERROR = 4;
    private static final int REQUEST_PERMISSION_SETTING = 5;

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;

    TextView displayCurrentPosition;
    TextView pieceNumberNear;
    ImageButton findMyLocationBtn;
    ImageButton hideShowMoreInfoBtn;
    LinearLayout moreInfoLayout;
    Button seeAllBtn;
    FloatingActionButton writePieceBtn;
    SharedPreferences SP;
    BmobRealTimeData data = new BmobRealTimeData();
    private RelativeLayout snackBarHolderView;
    private GoogleMap mMap;
    private Double mCurrentLatitude, mCurrentLongitude;
    private String mCurrentLocationName;
    private ClusterManager<ClusterMarkerLocation> clusterManager;
    private ClusterMarkerLocation clickedClusterItem;

    private ShowcaseView showcaseView;
    private int counter = 0;

    public static Intent newIntent(Context context) {
        return new Intent(context, MapsActivity.class);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 从bmob后台同步用户信息到sp文件中存储
        try {
            UserInfoSync.getUserInfo(this, Constants.SP_FILE_NAME, SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_USER_OBJECT_ID, null));
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

        initRealTimeData();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (SPUtils.getBoolean(this, Constants.SP_FILE_NAME, Constants.FIRST_RUN, true)) {
            SPUtils.putBoolean(this, Constants.SP_FILE_NAME, Constants.FIRST_RUN, false);
            counter = 0;
            firstLaunch();
        }
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();
        mGoogleApiClient.connect();
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
                clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new CustomInfoWindowAdapter());

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
                                BmobUser.logOut();
                                SPUtils.clear(MapsActivity.this, Constants.SP_FILE_NAME);
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
    protected void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
        MapStateManager mgr = new MapStateManager(this);
        mgr.saveMapState(mMap);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.write_piece_fab:
                if (mCurrentLatitude != null && mCurrentLongitude != null && mCurrentLocationName != null) {
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

    private void initMarker() {

        Log.i(TAG, "initMarker");
        clusterManager = new ClusterManager<>(this, mMap);
        clusterManager.setOnClusterItemInfoWindowClickListener(this);
        clusterManager.setOnClusterItemClickListener(this);

        mMap.setOnCameraChangeListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
        mMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
        mMap.setOnInfoWindowClickListener(clusterManager);

        clusterManager.setRenderer(new MyIconRender(this, mMap, clusterManager));

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
                            pr = 50;
                            break;
                        case 1:
                            pr = 100;
                            break;
                        case 2:
                            pr = 500;
                            break;
                        case 3:
                            pr = 2000;
                            break;
                        case 4:
                            pr = 5000;
                            break;
                        case 5:
                            pr = 20000;
                            break;
                        case 6:
                            pr = 60000;
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
                                    ClusterMarkerLocation item = new ClusterMarkerLocation(ll,
                                            list.get(0).getNickname(),
                                            p.getContent() + "::" + p.getCreatedAt() + "::" + p.getObjectId());
                                    clusterManager.addItem(item);
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

    @Override
    public boolean onClusterItemClick(ClusterMarkerLocation item) {
        clickedClusterItem = item;
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(ClusterMarkerLocation item) {
        //Snackbar.make(snackBarHolderView, item.getTitle() + item.getSnippet(), Snackbar.LENGTH_LONG).show();
        //打开纸条的详情页
        Intent intent = new Intent(this, PieceDetailActivity.class);
        intent.putExtra("authorName", item.getTitle());
        intent.putExtra("snippet", item.getSnippet());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        //Intent intent = new Intent(this, PieceMainService.class);
        //stopService(intent);
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showNotification(String title, String content) {
        Intent intent = MapsActivity.newIntent(this);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_insert_drive_file_black_24dp)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setLights(Color.BLUE, 3000, 3000);

        SP = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isVoice = SP.getBoolean("pref_notification_voice_key", true);

        Log.i(TAG, "#### isVoice:" + isVoice);
        if (isVoice) {
            String ringtone = SP.getString("pref_ringtone_key", null);
            Log.i(TAG, "#### ringtone:" + ringtone);
            if (ringtone != null) {
                Uri soundUri = Uri.parse(ringtone);
                builder.setSound(soundUri);
            }

            boolean isVibrate = SP.getBoolean("pref_vibrate_key", true);
            Log.i(TAG, "#### isVibrate:" + isVibrate);
            if (isVibrate) {
                builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            }

        }

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification);
    }

    private void initRealTimeData() {
        data.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                Log.d(TAG, "连接成功:" + data.isConnected());
                if (data.isConnected())
                    data.subTableUpdate("Piece");
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                Log.i(TAG, "(" + jsonObject.optString("action") + ")" + "数据：" + jsonObject);
                JSONObject jsonData = jsonObject.optJSONObject("data");
                String authorID = jsonData.optString("authorID");
                if (!User.getCurrentUser().getObjectId().equals(authorID)) {
                    boolean flag = false;
                    String pieceContent = jsonData.optString("content");
                    Double pieceLat = jsonData.optDouble("latitude");
                    Double pieceLng = jsonData.optDouble("longitude");
                    Integer visibility = jsonData.optInt("visibility");
                    Double distance = Common.GetDistance(pieceLat, pieceLng, mCurrentLatitude, mCurrentLongitude);

                    switch (visibility) {
                        case 0:
                            if (distance <= 50)
                                flag = true;
                            break;
                        case 1:
                            if (distance <= 100)
                                flag = true;
                            break;
                        case 2:
                            if (distance <= 500)
                                flag = true;
                            break;
                        case 3:
                            if (distance <= 2000)
                                flag = true;
                            break;
                        case 4:
                            if (distance <= 5000)
                                flag = true;
                            break;
                        case 5:
                            if (distance <= 20000)
                                flag = true;
                            break;
                        case 6:
                            if (distance <= 60000)
                                flag = true;
                            break;
                    }

                    if (flag)
                        showNotification("附近有新的 Piece", pieceContent);
                }
            }
        });
    }

    //首次启动，引导动画
    private void firstLaunch() {

        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);

        Button customButton = (Button) getLayoutInflater().inflate(R.layout.view_custom_button, null);
        MultiEventListener multiEventListener = new MultiEventListener(new ShakeButtonListener(customButton));

        showcaseView = new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setTarget(new ViewTarget(findViewById(R.id.find_my_location)))
                .setContentTitle(getString(R.string.location))
                .setContentText(getString(R.string.location_info))
                .setStyle(R.style.CustomShowcaseTheme)
                .setShowcaseEventListener(multiEventListener)
                .replaceEndButton(customButton)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (counter) {
                            case 0:
                                showcaseView.setShowcase(new ViewTarget(hideShowMoreInfoBtn), true);
                                showcaseView.setContentTitle(getString(R.string.more_info));
                                showcaseView.setContentText(getString(R.string.more_info_text));
                                break;
                            case 1:
                                showcaseView.setShowcase(new ViewTarget(writePieceBtn), true);
                                showcaseView.setContentTitle(getString(R.string.write_piece));
                                showcaseView.setContentText(getString(R.string.write_piece_text));
                                break;
                            case 2:
                                showcaseView.setTarget(Target.NONE);
                                showcaseView.setContentTitle(getString(R.string.app_name));
                                showcaseView.setContentText(getString(R.string.welcome));
                                showcaseView.setButtonText(getString(R.string.close));
                                break;
                            case 3:
                                showcaseView.hide();
                                break;
                        }
                        counter++;
                    }
                })
                .build();
        showcaseView.setButtonPosition(lps);
    }

    class CustomInfoWindowAdapter implements InfoWindowAdapter {

        private View mContents;

        CustomInfoWindowAdapter() {
            mContents = getLayoutInflater().inflate(R.layout.info_window_w, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            TextView nickname = (TextView) mContents.findViewById(R.id.info_window_nickname);
            TextView content = (TextView) mContents.findViewById(R.id.info_window_content);
            TextView time = (TextView) mContents.findViewById(R.id.info_window_time);

            if (clickedClusterItem != null) {
                String[] snippets = clickedClusterItem.getSnippet().split("::");

                nickname.setText(clickedClusterItem.getTitle());
                content.setText(snippets[0]);
                time.setText(snippets[1]);
            }
            return mContents;
        }
    }

    private class ShakeButtonListener extends SimpleShowcaseEventListener {
        private final Button button;

        public ShakeButtonListener(Button button) {
            this.button = button;
        }

        @Override
        public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {
            int translation = getResources().getDimensionPixelOffset(R.dimen.touch_button_wobble);
            ViewCompat.animate(button)
                    .translationXBy(translation)
                    .setInterpolator(new WobblyInterpolator(3));
        }
    }

    private class WobblyInterpolator implements Interpolator {

        private final double CONVERT_TO_RADS = 2 * Math.PI;
        private final int cycles;

        public WobblyInterpolator(int cycles) {
            this.cycles = cycles;
        }

        @Override
        public float getInterpolation(float proportion) {
            double sin = Math.sin(cycles * proportion * CONVERT_TO_RADS);
            return (float) sin;
        }

    }

    public class MyIconRender extends DefaultClusterRenderer<ClusterMarkerLocation> {

        public MyIconRender(Context context, GoogleMap map, ClusterManager<ClusterMarkerLocation> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<ClusterMarkerLocation> cluster, MarkerOptions markerOptions) {
            super.onBeforeClusterRendered(cluster, markerOptions);
        }

        @Override
        protected void onBeforeClusterItemRendered(ClusterMarkerLocation item, MarkerOptions markerOptions) {
            BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker1);
            markerOptions.icon(markerDescriptor);
        }
    }
}
