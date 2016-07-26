package com.sealiu.piece.controller.Piece;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sealiu.piece.R;
import com.sealiu.piece.controller.User.UserInfoSync;
import com.sealiu.piece.model.LoginUser;
import com.sealiu.piece.model.Piece;
import com.sealiu.piece.utils.ImageLoader.BitmapUtils;

import java.io.File;
import java.io.FileNotFoundException;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class WritePieceActivity extends AppCompatActivity implements
        UrlFragment.UrlListener, View.OnClickListener {

    private static final String TAG = "WritePieceActivity";
    private static final int ERROR = 4;
    private static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 111;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 222;
    ImageButton addLinkBtn;
    ImageButton addImageBtn;
    ImageButton linkDeleteBtn;
    CardView linkCard;
    TextView linkContent;
    ImageButton imageDeleteBtn;
    CardView imageCard;
    ImageView imagePreview;
    TextView imagePath;
    Uri previewUri;
    private String realPath;
    private Double mLatitude, mLongitude;
    private String mLocationName;
    private TextView myLocationTV;
    private ImageView headPictureIV;
    private TextView nickNameTV;
    private Spinner visibilitySpinner;
    private EditText pieceContentET;
    private NestedScrollView snackBarHolderView;
    private LoginUser loginUser;
    private boolean isNotAskAgain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_piece);

        loginUser = UserInfoSync.getLoginInfo(WritePieceActivity.this);

        snackBarHolderView = (NestedScrollView) findViewById(R.id.layout_holder);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.write_piece);

        myLocationTV = (TextView) findViewById(R.id.my_location_name);
        nickNameTV = (TextView) findViewById(R.id.user_nickname);
        headPictureIV = (ImageView) findViewById(R.id.user_head_picture);
        visibilitySpinner = (Spinner) findViewById(R.id.visibility);
        pieceContentET = (EditText) findViewById(R.id.piece_content);

        linkCard = (CardView) findViewById(R.id.link_card);
        linkContent = (TextView) findViewById(R.id.link_content);
        linkDeleteBtn = (ImageButton) findViewById(R.id.delete_link);

        imageCard = (CardView) findViewById(R.id.image_card);
        imagePreview = (ImageView) findViewById(R.id.image_preview);
        imagePath = (TextView) findViewById(R.id.image_path);
        imageDeleteBtn = (ImageButton) findViewById(R.id.delete_image);

        addImageBtn = (ImageButton) findViewById(R.id.add_image_btn);
        addLinkBtn = (ImageButton) findViewById(R.id.add_link_btn);

        addImageBtn.setOnClickListener(this);
        addLinkBtn.setOnClickListener(this);

        linkDeleteBtn.setOnClickListener(this);
        imageDeleteBtn.setOnClickListener(this);
        initUI();

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
                // 发送编写的Piece

                String objectId = loginUser.getObjectId();
                String pieceContent = pieceContentET.getText().toString();
                int visibilityPosition = visibilitySpinner.getSelectedItemPosition();

                if (pieceContent.equals("")) {
                    Snackbar.make(snackBarHolderView, "请填写内容", Snackbar.LENGTH_LONG).show();
                    break;
                }

                final Piece piece = new Piece(objectId, pieceContent, mLatitude, mLongitude, visibilityPosition);
                piece.setType(1);

                //设置分享链接
                if (linkCard.getVisibility() == View.VISIBLE) {
                    piece.setUrl(linkContent.getText().toString());
                    piece.setType(2);
                }

                //设置分享图片
                if (imageCard.getVisibility() == View.VISIBLE) {
                    piece.setImage(realPath);
                    piece.setType(3);
                }

                Log.i(TAG, "用户ID：" + piece.getAuthorID() +
                        "; 可见范围：" + piece.getVisibility() +
                        "; 纸条内容：" + piece.getContent() +
                        "; url：" + piece.getUrl() +
                        "; image：" + piece.getImage()
                );

                //保存到bmob后台
                piece.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra("errorCode", e.getErrorCode());
                            setResult(ERROR, intent);
                        }
                        finish();
                    }
                });

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0, len = permissions.length; i < len; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED &&
                    android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                boolean showRationale = shouldShowRequestPermissionRationale(permission);
                if (!showRationale) {
                    // 用户拒绝了带有“不再询问”的权限申请
                    // ...
                    isNotAskAgain = true;
                } else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
                    // 用户第一次拒绝了权限申请
                    // 向用户解释我们为什么要申请这个权限
                    showRationale(permission, R.string.permission_denied_storage);
                }
            }
        }
    }

    /**
     * 初始化界面显示（头像，昵称，位置信息）
     */
    private void initUI() {

        mLatitude = getIntent().getDoubleExtra("LAT", 0);
        mLongitude = getIntent().getDoubleExtra("LNG", 0);
        mLocationName = getIntent().getStringExtra("LOC");

        String detailPosition = mLocationName + " (" + mLatitude + " ," + mLongitude + ")";
        myLocationTV.setText(detailPosition);

        String nickName = loginUser.getNickname();
        String headPicture = loginUser.getAvatar();
        String username = loginUser.getUsername();


        if (nickName != null && !nickName.equals("")) {
            nickNameTV.setText(nickName);
        } else {
            nickNameTV.setText(username);
        }

        try {
            if (headPicture != null) {
                BitmapUtils bitmapUtils = new BitmapUtils();
                bitmapUtils.disPlay(headPictureIV, headPicture);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUrlPositiveClick(String url) {
        Log.i(TAG, url);
        linkCard.setVisibility(View.VISIBLE);
        linkContent.setText(url);
    }

    @Override
    public void onUrlNegativeClick() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_image_btn:
                //启动相册
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    File outputImage = new File(Environment.getExternalStorageDirectory(), "chooseImage.jpg");

                    previewUri = Uri.fromFile(outputImage);

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, previewUri);
                    if (intent.resolveActivity(getPackageManager()) != null)
                        startActivityForResult(intent, 1);
                    else
                        Snackbar.make(snackBarHolderView, "没有相册程序", Snackbar.LENGTH_LONG).show();
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                    } else {
                        Snackbar.make(snackBarHolderView, "你拒绝了存储空间权限申请", Snackbar.LENGTH_LONG)
                                .setAction("授予权限", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, 123);
                                    }
                                }).show();
                    }
                    if (isNotAskAgain) {
                        Snackbar.make(snackBarHolderView, "你拒绝了存储空间权限申请", Snackbar.LENGTH_LONG)
                                .setAction("授予权限", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, 123);
                                    }
                                }).show();
                    }
                }
                break;
            case R.id.add_link_btn:
                new UrlFragment().show(getSupportFragmentManager(), "url");
                break;
            case R.id.delete_link:
                linkCard.setVisibility(View.GONE);
                break;
            case R.id.delete_image:
                imageCard.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(data.getData(), "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra("crop", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, previewUri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, 2);
                    } else {
                        Snackbar.make(snackBarHolderView, "没有裁剪图片程序", Snackbar.LENGTH_LONG).show();
                    }
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    String path = previewUri.toString();

                    imagePath.setText(path.substring(7, path.length()));
                    try {
                        uploadPicture(imagePath.getText().toString());
                        Bitmap bitmap = BitmapFactory
                                .decodeStream(getContentResolver().openInputStream(previewUri));
                        imagePreview.setImageBitmap(bitmap);
                        imageCard.setVisibility(View.VISIBLE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void uploadPicture(String path) {
        final ProgressDialog progressDialog = new ProgressDialog(snackBarHolderView.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("图片上传中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        final BmobFile bmobFile = new BmobFile(new File(path));

        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                realPath = bmobFile.getFileUrl();
                progressDialog.dismiss();
                Snackbar.make(snackBarHolderView, "上传成功 ", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onProgress(Integer value) {
                // TODO Auto-generated method stub
                progressDialog.setProgress(value);
            }
        });
    }

    private void showRationale(String permission, int permissionDenied) {

        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(permission)
                .setMessage(getString(permissionDenied) + "。请重新授权！")
                .setPositiveButton("重新授权", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(WritePieceActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                    }
                })
                .setNegativeButton("仍然拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }
}
