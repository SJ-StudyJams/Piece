package com.sealiu.piece.controller.Piece;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sealiu.piece.R;
import com.sealiu.piece.model.Piece;


public class PieceDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PIECE_KEY = "piece_key";
    private final static String TAG = "PieceDetailActivity";

    TextView mContent, mAuthor, mDate, mViewCount;

    TextView pieceLinkTV;
    ImageView headPicture, pieceImageIV;
    View divider;

    private String mPieceKey;
    private DatabaseReference mPieceReference;
    private ValueEventListener mPieceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get piece key from intent
        mPieceKey = getIntent().getStringExtra(EXTRA_PIECE_KEY);

        // Initialize Database
        mPieceReference = FirebaseDatabase.getInstance().getReference()
                .child("pieces").child(mPieceKey);

        mContent = (TextView) findViewById(R.id.piece_content);
        mAuthor = (TextView) findViewById(R.id.piece_author);
        mDate = (TextView) findViewById(R.id.piece_date);
        mViewCount = (TextView) findViewById(R.id.piece_count);

        headPicture = (ImageView) findViewById(R.id.head_picture);

        pieceLinkTV = (TextView) findViewById(R.id.piece_link);
        pieceImageIV = (ImageView) findViewById(R.id.piece_image);

        divider = findViewById(R.id.divider);
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
                mViewCount.setText(String.valueOf(piece.viewCount));
                mContent.setText(piece.content);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPiece:onCancelled", databaseError.toException());
            }
        };

        mPieceReference.addValueEventListener(pieceListener);
        mPieceListener = pieceListener;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPieceListener != null) {
            mPieceReference.removeEventListener(mPieceListener);
        }
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
