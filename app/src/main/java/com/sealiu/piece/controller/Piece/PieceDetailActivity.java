package com.sealiu.piece.controller.Piece;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.sealiu.piece.R;
import com.sealiu.piece.model.Piece;
import com.sealiu.piece.utils.ImageLoader.BitmapUtils;


public class PieceDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_PIECE_KEY = "piece_key";
    private final static String TAG = "PieceDetailActivity";

    TextView mContent, mAuthor, mDate, mLikeCount, pieceLinkTV;
    ImageView headPicture, pieceImageIV, mLikeIcon;
    View divider;

    BitmapUtils mBitmapUtils;

    private DatabaseReference mDatabaseRef;
    private DatabaseReference mPiecesRef;
    private DatabaseReference mUserPieceRef;

    private String pieceKey;
    private String pieceUID;

    private ValueEventListener mPieceListener;

    private FirebaseUser mUser;

    public static void onLikeClick(DatabaseReference pieceRef, final String uid) {
        pieceRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Piece p = mutableData.getValue(Piece.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.likes.containsKey(uid)) {
                    p.likeCount -= 1;
                    p.likes.remove(uid);
                } else {
                    p.likeCount += 1;
                    p.likes.put(uid, true);
                }

                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBitmapUtils = new BitmapUtils();

        // Get piece key from intent
        pieceKey = getIntent().getStringExtra(EXTRA_PIECE_KEY);

        // Get current sign in user;
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        // Initialize Database
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mPiecesRef = mDatabaseRef.child("pieces").child(pieceKey);

        divider = findViewById(R.id.divider);
        mContent = (TextView) findViewById(R.id.piece_content);
        mAuthor = (TextView) findViewById(R.id.piece_author);
        mDate = (TextView) findViewById(R.id.piece_date);
        mLikeCount = (TextView) findViewById(R.id.piece_like_num);
        pieceLinkTV = (TextView) findViewById(R.id.piece_link);

        headPicture = (ImageView) findViewById(R.id.head_picture);
        pieceImageIV = (ImageView) findViewById(R.id.piece_image);
        mLikeIcon = (ImageView) findViewById(R.id.piece_like_icon);

        findViewById(R.id.piece_like_holder).setOnClickListener(this);

        if (mUser.getPhotoUrl() == null) {
            headPicture.setVisibility(View.GONE);
        } else {
            mBitmapUtils.disPlay(headPicture, mUser.getPhotoUrl().toString());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener pieceListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Piece piece = dataSnapshot.getValue(Piece.class);
                pieceUID = piece.uid;

                if (piece.likes.containsKey(mUser.getUid())) {
                    mLikeIcon.setImageResource(R.drawable.ic_favorite_24dp);
                } else {
                    mLikeIcon.setImageResource(R.drawable.ic_favorite_border_24dp);
                }

                mAuthor.setText(piece.author);
                mDate.setText(piece.date);
                mLikeCount.setText(String.valueOf(piece.likeCount));
                mContent.setText(piece.content);
                if (piece.url != null) {
                    pieceLinkTV.setText(piece.url);
                    divider.setVisibility(View.VISIBLE);
                    pieceLinkTV.setVisibility(View.VISIBLE);
                }
                if (piece.image != null) {
                    mBitmapUtils.disPlay(pieceImageIV, piece.image);
                    pieceImageIV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPiece:onCancelled", databaseError.toException());
            }
        };

        mPiecesRef.addValueEventListener(pieceListener);
        mPieceListener = pieceListener;
    }

    @Override
    public void onClick(View view) {

        mUserPieceRef = mDatabaseRef.child("user-pieces").child(pieceUID).child(pieceKey);

        if (view.getId() == R.id.piece_like_holder) {
            onLikeClick(mPiecesRef, mUser.getUid());
            onLikeClick(mUserPieceRef, mUser.getUid());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPieceListener != null) {
            mPiecesRef.removeEventListener(mPieceListener);
        }
    }
}
