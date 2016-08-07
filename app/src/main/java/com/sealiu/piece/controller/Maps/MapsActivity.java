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
import android.support.v4.view.ViewCompat;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.sealiu.piece.R;
import com.sealiu.piece.controller.BaseActivity;
import com.sealiu.piece.controller.LoginRegister.IndexActivity;
import com.sealiu.piece.controller.LoginRegister.LinkAccountFragment;
import com.sealiu.piece.controller.Piece.PieceDetailActivity;
import com.sealiu.piece.controller.Piece.PiecesActivity;
import com.sealiu.piece.controller.Piece.WritePieceActivity;
import com.sealiu.piece.controller.Settings.MyPreferenceActivity;
import com.sealiu.piece.controller.User.UserActivity;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.utils.SPUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.google.android.gms.common.api.GoogleApiClient.Builder;
import static com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import static com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import static com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import static com.google.maps.android.clustering.ClusterManager.OnClusterItemClickListener;
import static com.google.maps.android.clustering.ClusterManager.OnClusterItemInfoWindowClickListener;

public class MapsActivity extends BaseActivity implements
        OnMapReadyCallback,
        ConnectionCallbacks,
        OnConnectionFailedListener,
        View.OnClickListener,
        OnClusterItemClickListener<ClusterMarkerLocation>,
        OnClusterItemInfoWindowClickListener<ClusterMarkerLocation>,
        LinkAccountFragment.LinkAccountListener {

    private static final String TAG = "MapsActivity";
    private static final int WRITE_PIECE_REQUEST_CODE = 3;

    private static final int FINE_LOCATION_PERMS = 201;

    private static final int GOOGLE_LINK = 9001;

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
    private RelativeLayout snackBarHolderView;
    private GoogleMap mMap;
    private Double mCurrentLatitude, mCurrentLongitude;
    private String mCurrentLocationName;
    private ClusterManager<ClusterMarkerLocation> clusterManager;
    private ClusterMarkerLocation clickedClusterItem;

    private ShowcaseView showcaseView;
    private int counter = 0;

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private CallbackManager mCallbackManager;
    private LoginManager mLoginManager;

    public static Intent newIntent(Context context) {
        return new Intent(context, MapsActivity.class);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Log.d(TAG, "User:" + mUser.getDisplayName() + "\nEmail:" +
                mUser.getEmail() + "\nUid:" +
                mUser.getUid() + "\nProviderID:" +
                mUser.getProviderId() + "\nphotoUrl:" +
                mUser.getPhotoUrl() + "\nproviders:" +
                mUser.getProviders()
        );

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    finish();
                    startActivity(new Intent(MapsActivity.this, IndexActivity.class));
                }
                // ...
            }
        };

        // facebook callback manager
        mCallbackManager = CallbackManager.Factory.create();
        mLoginManager = LoginManager.getInstance();
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
        mAuth.addAuthStateListener(mAuthListener);
        mGoogleApiClient.connect();
    }

    @AfterPermissionGranted(FINE_LOCATION_PERMS)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "Map Ready");

        mMap = googleMap;
        String perm = Manifest.permission.ACCESS_FINE_LOCATION;
        if (!EasyPermissions.hasPermissions(this, perm)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_fine_location),
                    FINE_LOCATION_PERMS, perm);
            return;
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
    }

    @AfterPermissionGranted(FINE_LOCATION_PERMS)
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Service Connected");

        String perm = Manifest.permission.ACCESS_FINE_LOCATION;
        if (!EasyPermissions.hasPermissions(this, perm)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_fine_location),
                    FINE_LOCATION_PERMS, perm);
            return;
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
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
                displayCurrentPosition.setText(mCurrentLocationName);
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

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(snackBarHolderView, R.string.network_error,
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.user_menu_title:
                if (mUser.getProviders().size() != 0) {
                    startActivity(new Intent(MapsActivity.this, UserActivity.class));
                } else {
                    // anonymous sign in
                    anonymousLimit();
                }
                return true;
            case R.id.settings_menu_title:
                startActivity(new Intent(MapsActivity.this, MyPreferenceActivity.class));
                return true;
            case R.id.switch_menu_title:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.sign_out))
                        .setMessage(getString(R.string.sign_out_message))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                signOut();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void anonymousLimit() {
        /*
        匿名登录后连接Google/facebook账户存在问题：
        连接成功后：
        - FirebaseUser.getDisplayName = null
        - FirebaseUser.getPhotoUrl = null
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LinkAccountFragment linkAccountFragment = new LinkAccountFragment();
                linkAccountFragment.show(getSupportFragmentManager(), "LINK_ACCOUNT");
            }
        })
         */
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.anonymous_limit))
                .setMessage(getString(R.string.anonymous_limit_message))
                .show();
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // FB sign out
        LoginManager.getInstance().logOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                    }
                }
        );
    }

    @Override
    public void onStop() {
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
                if (mUser.getProviders().size() == 0) {
                    anonymousLimit();
                } else if (mCurrentLatitude != null && mCurrentLongitude != null && mCurrentLocationName != null) {
                    Intent intent = new Intent(MapsActivity.this, WritePieceActivity.class);
                    intent.putExtra("LAT", mCurrentLatitude);
                    intent.putExtra("LNG", mCurrentLongitude);
                    intent.putExtra("LOC", mCurrentLocationName);
                    startActivityForResult(intent, WRITE_PIECE_REQUEST_CODE);
                } else {
                    Snackbar.make(snackBarHolderView, getString(R.string.location_error), Snackbar.LENGTH_LONG)
                            .setAction(R.string.location, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    findMyLocationBtn.performClick();
                                }
                            }).show();
                }
                break;
            case R.id.find_my_location:

                if (mLastLocation != null) {
                    mCurrentLatitude = mLastLocation.getLatitude();
                    mCurrentLongitude = mLastLocation.getLongitude();

                    try {
                        mCurrentLocationName = getPositionName(mCurrentLatitude, mCurrentLongitude);
                        displayCurrentPosition.setText(mCurrentLocationName);
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
                    Snackbar.make(snackBarHolderView, getString(R.string.location_error), Snackbar.LENGTH_LONG)
                            .setAction(R.string.location, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    findMyLocationBtn.performClick();
                                }
                            }).show();
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
                    Snackbar.make(snackBarHolderView, getString(R.string.send_success), Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(snackBarHolderView, getString(R.string.send_fail), Snackbar.LENGTH_SHORT).show();
                }
                break;
            case GOOGLE_LINK:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    GoogleSignInAccount account = result.getSignInAccount();
                    Log.i(TAG, "google sign in success, idtoken:" + account.getIdToken());
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    linkAccount(credential, "google");
                } else {
                    Log.d(TAG, "Google Sign In failed");
                }
                break;
            default:
                mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void linkAccount(final AuthCredential credential, final String USER_TYPE) {
        showProgressDialog();
        mUser.linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        Log.d(TAG, "linkWithCredential:onComplete:" + task.isSuccessful());

                        String linkResult = "";
                        if (!task.isSuccessful()) {
                            linkResult = task.getException().getLocalizedMessage();
                            Log.d(TAG, linkResult);
                        } else {
                            linkResult = getString(R.string.link_count_success);
                            FirebaseUser user = task.getResult().getUser();
                            Log.d(TAG, "User:" + user.getDisplayName() + "\nEmail:" +
                                    user.getEmail() + "\nUid:" +
                                    user.getUid() + "\nProviderID:" +
                                    user.getProviderId() + "\nphotoUrl:" +
                                    user.getPhotoUrl() + "\nproviders:" +
                                    user.getProviders()
                            );

                            // write database, add user
                            IndexActivity.writeNewUser(mDatabase, user.getUid(), user.getDisplayName(), user.getEmail(), user.getPhotoUrl(), USER_TYPE);
                        }
                        Snackbar.make(snackBarHolderView, linkResult, Snackbar.LENGTH_LONG).show();
                    }
                });
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

        mDatabase.child("pieces").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int number = 0;

                if (dataSnapshot != null) {
                    HashMap<String, Object> dataSnapshotValue = (HashMap<String, Object>) dataSnapshot.getValue();
                    if (dataSnapshotValue != null) {
                        Set<String> set = dataSnapshotValue.keySet();

                        if (set != null) {
                            for (String key : set) {
                                Log.i(TAG, key);
                                HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshotValue.get(key);
                                Log.d(TAG, "author: " + map.get("author"));
                                Double lat = (Double) map.get("latitude");
                                Double lng = (Double) map.get("longitude");

                                int pr = 0;

                                switch (Integer.valueOf(map.get("visibility").toString())) {
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

                                double distance = Common.GetDistance(mCurrentLatitude, mCurrentLongitude, lat, lng);
                                if (distance <= pr) {
                                    number++;
                                    LatLng ll = new LatLng(lat, lng);
                                    ClusterMarkerLocation item = new ClusterMarkerLocation(ll,
                                            map.get("author").toString(),
                                            map.get("content") + "::" + map.get("date") + "::" + key);
                                    clusterManager.addItem(item);
                                }
                            }
                        }
                    }
                }

                String info = getString(R.string.nearby) + " " + number + " " +
                        getString(R.string.pieces);
                pieceNumberNear.setText(info);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onClusterItemClick(ClusterMarkerLocation item) {
        clickedClusterItem = item;
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(ClusterMarkerLocation item) {
        String[] snippets = item.getSnippet().split("::");

        //打开纸条的详情页
        Intent intent = new Intent(this, PieceDetailActivity.class);
        intent.putExtra(PieceDetailActivity.EXTRA_PIECE_KEY, snippets[2]);
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

    @Override
    public void onGoogleClick() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_LINK);
    }

    @Override
    public void onFBClick() {
        Collection<String> collection = new ArrayList<>();
        collection.add("email");
        collection.add("public_profile");
        mLoginManager.logInWithReadPermissions(this, collection);

        mLoginManager.registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken token = loginResult.getAccessToken();
                        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
                        linkAccount(credential, "facebook");
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                }
        );
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

        ShakeButtonListener(Button button) {
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

        WobblyInterpolator(int cycles) {
            this.cycles = cycles;
        }

        @Override
        public float getInterpolation(float proportion) {
            double sin = Math.sin(cycles * proportion * CONVERT_TO_RADS);
            return (float) sin;
        }

    }

    public class MyIconRender extends DefaultClusterRenderer<ClusterMarkerLocation> {

        MyIconRender(Context context, GoogleMap map, ClusterManager<ClusterMarkerLocation> clusterManager) {
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
