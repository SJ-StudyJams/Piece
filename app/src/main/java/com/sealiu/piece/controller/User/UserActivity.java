package com.sealiu.piece.controller.User;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sealiu.piece.R;
import com.sealiu.piece.utils.ImageLoader.BitmapUtils;

import de.hdodenhof.circleimageview.CircleImageView;

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

        CircleImageView userPhoto = (CircleImageView) findViewById(R.id.user_photo);


        // Get the currently signed-in user
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            BitmapUtils bitmapUtils = new BitmapUtils();
            bitmapUtils.disPlay(userPhoto, user.getPhotoUrl().toString());
        }

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
