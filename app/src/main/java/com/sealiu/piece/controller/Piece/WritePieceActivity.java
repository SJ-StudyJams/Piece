package com.sealiu.piece.controller.Piece;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sealiu.piece.R;
import com.sealiu.piece.model.Piece;
import com.sealiu.piece.model.User;
import com.sealiu.piece.utils.ImageLoader.BitmapUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class WritePieceActivity extends AppCompatActivity implements
        PickPicFragment.PickPicListener,
        UrlFragment.UrlListener, View.OnClickListener {

    private static final String TAG = "WritePieceActivity";
    private static final String REQUIRED = "Required";

    private static final int STORAGE_PERMS = 101;
    private static final int TAKE_PICTURE = 102;
    private static final int ALBUM_CHOOSE = 103;
    private static final int CROP_PIC = 104;

    private static final String KEY_FILE_URI = "key_file_uri";
    private static final String KEY_DOWNLOAD_URL = "key_download_url";
    private static final String KEY_LINK = "key_link";

    private TextView imagePath, linkContent, myLocationTV, nickNameTV;
    private CardView imageCard, linkCard;
    private ImageView headPictureIV, imagePreview;
    private EditText pieceContentET;
    private Spinner visibilitySpinner;
    private ProgressDialog mProgressDialog;
    private NestedScrollView snackBarHolderView;

    private Double mLatitude, mLongitude;

    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;

    private Uri mFileUri = null;
    private String mFileName;
    private Uri mDownloadUrl;

    private String mShareLink = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_piece);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.write_piece);

        snackBarHolderView = (NestedScrollView) findViewById(R.id.layout_holder);

        myLocationTV = (TextView) findViewById(R.id.my_location_name);
        nickNameTV = (TextView) findViewById(R.id.user_nickname);
        headPictureIV = (ImageView) findViewById(R.id.user_head_picture);
        visibilitySpinner = (Spinner) findViewById(R.id.visibility);
        pieceContentET = (EditText) findViewById(R.id.piece_content);

        linkCard = (CardView) findViewById(R.id.link_card);
        linkContent = (TextView) findViewById(R.id.link_content);

        imageCard = (CardView) findViewById(R.id.image_card);
        imagePreview = (ImageView) findViewById(R.id.image_preview);
        imagePath = (TextView) findViewById(R.id.image_path);

        findViewById(R.id.add_image_btn).setOnClickListener(this);
        findViewById(R.id.add_link_btn).setOnClickListener(this);
        findViewById(R.id.delete_link).setOnClickListener(this);
        findViewById(R.id.delete_image).setOnClickListener(this);

        if (savedInstanceState != null) {
            mFileUri = savedInstanceState.getParcelable(KEY_FILE_URI);
            mDownloadUrl = savedInstanceState.getParcelable(KEY_DOWNLOAD_URL);
            mShareLink = savedInstanceState.getString(KEY_LINK);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mLatitude = getIntent().getDoubleExtra("LAT", 0);
        mLongitude = getIntent().getDoubleExtra("LNG", 0);
        String mLocationName = getIntent().getStringExtra("LOC");

        myLocationTV.setText(mLocationName);

        nickNameTV.setText(user.getDisplayName());
        BitmapUtils bitmapUtils = new BitmapUtils();
        if (user.getPhotoUrl() == null) {
            headPictureIV.setVisibility(View.GONE);
        } else {
            bitmapUtils.disPlay(headPictureIV, user.getPhotoUrl().toString());
        }

        updateUI();
    }

    private void updateUI() {
        if (mDownloadUrl != null) {
            imagePath.setText(mFileName);

            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mFileUri));
                imagePreview.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            BitmapUtils bitmapUtils = new BitmapUtils();
            bitmapUtils.disPlay(imagePreview, mDownloadUrl.toString());
            imageCard.setVisibility(View.VISIBLE);

        } else {
            mFileName = null;
            imageCard.setVisibility(View.GONE);
        }

        if (mShareLink != null) {
            linkContent.setText(mShareLink);
            linkCard.setVisibility(View.VISIBLE);
        } else {
            linkCard.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_FILE_URI, mFileUri);
        outState.putParcelable(KEY_DOWNLOAD_URL, mDownloadUrl);
        outState.putString(KEY_LINK, mShareLink);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_image_btn:
                PickPicFragment pickPicFragment = new PickPicFragment();
                pickPicFragment.show(getSupportFragmentManager(), "PICK_PIC");
                break;
            case R.id.add_link_btn:
                new UrlFragment().show(getSupportFragmentManager(), "url");
                break;
            case R.id.delete_link:
                mShareLink = null;
                updateUI();
                break;
            case R.id.delete_image:
                mFileName = null;
                mDownloadUrl = null;
                updateUI();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK && mFileUri != null) {
                    Log.d(TAG, "mFileUri:" + mFileUri);
                    uploadFromUri(mFileUri);
                }
                break;
            case ALBUM_CHOOSE:
                if (resultCode == RESULT_OK && mFileUri != null) {
                    Log.d(TAG, "mFileUri:" + mFileUri);

                    uploadFromUri(data.getData());
                    /*
                    Intent cropPicIntent = new Intent("com.android.camera.action.CROP");
                    cropPicIntent.setDataAndType(data.getData(), "image/*");
                    cropPicIntent.putExtra("scale", true);
                    cropPicIntent.putExtra("crop", true);

                    cropPicIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);

                    if (cropPicIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(cropPicIntent, CROP_PIC);
                    } else {
                        Snackbar.make(snackBarHolderView,
                                getString(R.string.crop_pic_intent_error),
                                Snackbar.LENGTH_LONG).show();
                    }
                    */
                }
                break;
            case CROP_PIC:
                if (resultCode == RESULT_OK && mFileUri != null) {
                    // uploadFromUri(mFileUri);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadFromUri(Uri fileUri) {
        // Get a reference to store file at photos/<FILENAME>.jpg
        final StorageReference photoRef = mStorageRef.child(user.getUid())
                .child(fileUri.getLastPathSegment());

        showProgressDialog(getString(R.string.uploading));

        // Upload file to Firebase Storage
        photoRef.putFile(fileUri)
                .addOnProgressListener(this, new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        String format = taskSnapshot.getBytesTransferred() + "/" + taskSnapshot.getTotalByteCount();
                        mProgressDialog.setProgressNumberFormat(format);
                    }
                })
                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        hideProgressDialog();
                        mFileName = taskSnapshot.getMetadata().getName();
                        mDownloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                        updateUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressDialog();
                        mFileName = null;
                        mDownloadUrl = null;
                        updateUI();
                    }
                });
    }

    @AfterPermissionGranted(STORAGE_PERMS)
    @Override
    public void onCameraClick() {
        // Check that we have permission to read images from external storage.
        String perm = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (!EasyPermissions.hasPermissions(this, perm)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage),
                    STORAGE_PERMS, perm);
            return;
        }
        // Choose file storage location, must be listed in res/xml/file_paths.xml
        File dir = new File(Environment.getExternalStorageDirectory() + "/photos");
        File file = new File(dir, UUID.randomUUID().toString() + ".jpg");
        try {
            // Create directory if it does not exist.
            if (!dir.exists()) {
                dir.mkdir();
            }
            boolean created = file.createNewFile();
            Log.d(TAG, "file.createNewFile:" + file.getAbsolutePath() + ":" + created);
        } catch (IOException e) {
            Log.e(TAG, "file.createNewFile" + file.getAbsolutePath() + ":FAILED", e);
        }

        // Create content:// URI for file, required since Android N
        // See: https://developer.android.com/reference/android/support/v4/content/FileProvider.html

        mFileUri = FileProvider.getUriForFile(this, "com.sealiu.piece.fileprovider", file);

        // Create and launch the intent
        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);

        if (takePicIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicIntent, TAKE_PICTURE);
        } else {
            Snackbar.make(snackBarHolderView,
                    getString(R.string.take_pic_intent_error),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    @AfterPermissionGranted(STORAGE_PERMS)
    @Override
    public void onAlbumClick() {
        // Check that we have permission to read images from external storage.
        String perm = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (!EasyPermissions.hasPermissions(this, perm)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage),
                    STORAGE_PERMS, perm);
            return;
        }

        // Choose file storage location, must be listed in res/xml/file_paths.xml
        File dir = new File(Environment.getExternalStorageDirectory() + "/photos");
        File file = new File(dir, UUID.randomUUID().toString() + ".jpg");
        try {
            // Create directory if it does not exist.
            if (!dir.exists()) {
                dir.mkdir();
            }
            boolean created = file.createNewFile();
            Log.d(TAG, "file.createNewFile:" + file.getAbsolutePath() + ":" + created);
        } catch (IOException e) {
            Log.e(TAG, "file.createNewFile" + file.getAbsolutePath() + ":FAILED", e);
        }

        mFileUri = FileProvider.getUriForFile(this, "com.sealiu.piece.fileprovider", file);

        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setType("image/*");
        albumIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);

        if (albumIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(albumIntent, ALBUM_CHOOSE);
        } else {
            Snackbar.make(snackBarHolderView,
                    getString(R.string.album_intent_error),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUrlPositiveClick(String url) {
        Log.i(TAG, "onUrlPositiveClick" + url);
        mShareLink = url;
        updateUI();
    }

    @Override
    public void onUrlNegativeClick() {
        Log.i(TAG, "onUrlNegativeClick");
    }

    private void showProgressDialog(String content) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(content);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write_piece, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_send_piece:
                String lFlag = linkCard.getVisibility() == View.VISIBLE ? "1" : "0";
                String pFlag = imageCard.getVisibility() == View.VISIBLE ? "1" : "0";

                submitPiece(Integer.valueOf(pFlag + lFlag, 2));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submitPiece(final int type) {
        final String content = pieceContentET.getText().toString();

        // content is required
        if (TextUtils.isEmpty(content)) {
            pieceContentET.setError(REQUIRED);
            return;
        }
        final int visibility = visibilitySpinner.getSelectedItemPosition();

        final String userId = user.getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if (user == null) {
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(WritePieceActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            writeNewPiece(userId, user.username, content, visibility, type);
                        }

                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                }
        );
    }

    private void writeNewPiece(String userId, String username, String content, int visibility, int type) {
        String key = mDatabase.child("pieces").push().getKey();

        Piece piece = new Piece(username, userId, content, mLatitude, mLongitude, visibility, type);
        DateFormat df = new SimpleDateFormat("MM-dd HH:mm");
        piece.date = df.format(new Date());

        switch (type) {
            case 1:
                piece.url = mShareLink;
                break;
            case 2:
                piece.image = mDownloadUrl.toString();
                break;
            case 3:
                piece.url = mShareLink;
                piece.image = mDownloadUrl.toString();
                break;
            default:
                break;
        }

        Map<String, Object> pieceValues = piece.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/pieces/" + key, pieceValues);
        childUpdates.put("/user-pieces/" + userId + "/" + key, pieceValues);
        mDatabase.updateChildren(childUpdates);
    }
}
