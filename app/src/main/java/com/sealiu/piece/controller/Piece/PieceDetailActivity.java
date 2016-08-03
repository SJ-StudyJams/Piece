package com.sealiu.piece.controller.Piece;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sealiu.piece.R;


public class PieceDetailActivity extends AppCompatActivity {

    private final static String TAG = "PieceDetailActivity";
    TextView pieceContentTV;
    TextView pieceInfo;
    TextView hotCountTV, pieceLinkTV;
    ImageView headPicture, pieceImageIV;
    View divider;
    String pieceContent, pieceCreatedAt, pieceObjectId, pieceLink, pieceImage;
    String authorID, authorNickname, authorPic;
    int hotCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pieceContentTV = (TextView) findViewById(R.id.piece_content);
        pieceInfo = (TextView) findViewById(R.id.piece_info);
        hotCountTV = (TextView) findViewById(R.id.hot_count);
        pieceLinkTV = (TextView) findViewById(R.id.piece_link);
        headPicture = (ImageView) findViewById(R.id.head_picture);
        pieceImageIV = (ImageView) findViewById(R.id.piece_image);

        divider = findViewById(R.id.divider);
        initUI();

    }

    private void initUI() {
        String[] snippets = getIntent().getStringExtra("snippet").split("::");

        authorNickname = getIntent().getStringExtra("authorName");
        pieceContent = snippets[0];
        pieceCreatedAt = snippets[1];
        pieceObjectId = snippets[2];

        /*
        Log.i(TAG, pieceObjectId);
        if (!pieceObjectId.equals("") && pieceObjectId != null) {
            final BmobQuery<Piece> bmobQuery = new BmobQuery<>();
            bmobQuery.getObject(pieceObjectId, new QueryListener<Piece>() {
                @Override
                public void done(Piece piece, BmobException e) {
                    if (e == null) {
                        Log.i(TAG, "Piece 查找成功");
                        authorID = piece.getAuthorID();
                        pieceContent = piece.getContent();
                        pieceLink = piece.getUrl();
                        pieceImage = piece.getImage();
                        hotCount = piece.getViewCount();

                        //显示内容
                        pieceContentTV.setText(pieceContent);

                        //显示浏览次数
                        hotCountTV.setText(String.valueOf(++hotCount));

                        //显示图片（如果有）
                        try {
                            if (pieceImage != null && !pieceImage.equals("")) {
                                Log.i(TAG, "pieceImage: " + authorPic);
                                pieceImageIV.setVisibility(View.VISIBLE);
                                BitmapUtils bitmapUtils = new BitmapUtils();
                                bitmapUtils.disPlay(pieceImageIV, pieceImage);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        //显示链接（如果有）
                        if (pieceLink != null && !pieceLink.equals("")) {
                            divider.setVisibility(View.VISIBLE);
                            pieceLinkTV.setText(pieceLink);
                            pieceLinkTV.setVisibility(View.VISIBLE);
                        }

                        Log.i(TAG, authorID);
                        if (!authorID.equals("") && authorID != null) {
                            BmobQuery<User> query = new BmobQuery<>();
                            query.getObject(authorID, new QueryListener<User>() {
                                @Override
                                public void done(User user, BmobException e) {
                                    if (e == null) {
                                        Log.i(TAG, "User 查找成功");
                                        authorNickname = user.getNickname();
                                        authorPic = user.getPicture();

                                        //显示作者头像
                                        try {
                                            if (authorPic != null && !authorPic.equals("")) {
                                                Log.i(TAG, "authorPic: " + authorPic);
                                                BitmapUtils bitmapUtils = new BitmapUtils();
                                                bitmapUtils.disPlay(headPicture, authorPic);
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }

                                        //显示名称和发布时间
                                        Resources resources = getResources();

                                        SpannableString nicknameText = new SpannableString(authorNickname);
                                        SpannableString createdAtText = new SpannableString(pieceCreatedAt);

                                        nicknameText.setSpan(new ForegroundColorSpan(resources.getColor(R.color.colorAccent)), 0, nicknameText.length(), 0);
                                        createdAtText.setSpan(new ForegroundColorSpan(resources.getColor(R.color.disabledText)), 0, createdAtText.length(), 0);

                                        pieceInfo.setText(nicknameText + "\n" + createdAtText);
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
        */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
        Piece p = new Piece();
        p.setViewCount(hotCount);
        p.update(pieceObjectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i(TAG, "成功，浏览次数加1：" + hotCount);
                } else {
                    Log.i(TAG, "失败");
                }
            }
        });
        */
    }
}
