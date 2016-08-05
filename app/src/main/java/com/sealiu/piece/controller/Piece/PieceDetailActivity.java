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
import com.google.firebase.database.ValueEventListener;
import com.sealiu.piece.R;
import com.sealiu.piece.model.Piece;
import com.sealiu.piece.utils.ImageLoader.BitmapUtils;


public class PieceDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PIECE_KEY = "piece_key";
    private final static String TAG = "PieceDetailActivity";

    TextView mContent, mAuthor, mDate, mLikeCount, pieceLinkTV;
    ImageView headPicture, pieceImageIV;
    View divider;

    BitmapUtils mBitmapUtils;

    private DatabaseReference mPiecesRef;
    private ValueEventListener mPieceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBitmapUtils = new BitmapUtils();

        // Get piece key from intent
        String pieceKey = getIntent().getStringExtra(EXTRA_PIECE_KEY);

        // Initialize Database
        mPiecesRef = FirebaseDatabase.getInstance().getReference()
                .child("pieces").child(pieceKey);

        divider = findViewById(R.id.divider);
        mContent = (TextView) findViewById(R.id.piece_content);
        mAuthor = (TextView) findViewById(R.id.piece_author);
        mDate = (TextView) findViewById(R.id.piece_date);
        mLikeCount = (TextView) findViewById(R.id.piece_link);
        pieceLinkTV = (TextView) findViewById(R.id.piece_link);

        headPicture = (ImageView) findViewById(R.id.head_picture);
        pieceImageIV = (ImageView) findViewById(R.id.piece_image);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getPhotoUrl() != null) {
            mBitmapUtils.disPlay(headPicture, user.getPhotoUrl().toString());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener pieceListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Piece piece = dataSnapshot.getValue(Piece.class);

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
    protected void onDestroy() {
        super.onDestroy();

        if (mPieceListener != null) {
            mPiecesRef.removeEventListener(mPieceListener);
        }
    }
}
