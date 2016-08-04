package com.sealiu.piece.controller.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sealiu.piece.R;
import com.sealiu.piece.controller.LoginRegister.IndexActivity;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.utils.ImageLoader.BitmapUtils;
import com.sealiu.piece.utils.SPUtils;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = "UserActivity";

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        TextView displayName = (TextView) findViewById(R.id.display_name);
        ImageView userPhoto = (ImageView) findViewById(R.id.user_photo);


        // Get the currently signed-in user
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            displayName.setText(user.getDisplayName());
            BitmapUtils bitmapUtils = new BitmapUtils();
            bitmapUtils.disPlay(userPhoto, user.getPhotoUrl().toString());
        }

        // AuthStateChange Listener
        new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser u = firebaseAuth.getCurrentUser();
                if (u == null) {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    SPUtils.clear(UserActivity.this, Constants.SP_FILE_NAME);
                    startActivity(new Intent(UserActivity.this, IndexActivity.class));
                    finish();
                }
            }
        };

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.my_pieces_fragment_container);

        if (fragment == null) {
            fragment = new MyPiecesFragment();
            fm.beginTransaction()
                    .add(R.id.my_pieces_fragment_container, fragment)
                    .commit();
        }
    }
}
