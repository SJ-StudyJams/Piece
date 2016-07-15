package com.sealiu.piece.controller.Piece;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.sealiu.piece.R;
import com.sealiu.piece.controller.Maps.Common;
import com.sealiu.piece.model.Piece;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class PiecesActivity extends AppCompatActivity {

    private static final String TAG = "PiecesActivity";
    double mLat;
    double mLng;
    List<Piece> mDataset;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pieces);

        mLat = getIntent().getDoubleExtra("lat", 0);
        mLng = getIntent().getDoubleExtra("lng", 0);

        Log.i(TAG, "lat: " + mLat + "; lng: " + mLng);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.pieces_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mDataset = new ArrayList<>();
        setAdapter();
    }

    private void setAdapter() {
        double[] llRange = Common.GetAround(mLat, mLng, 100000);

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

                for (final Piece p : list) {
                    int pr = 0;
                    switch (p.getVisibility()) {
                        case 0:
                            pr = 5000;
                            break;
                        case 1:
                            pr = 20000;
                            break;
                        case 2:
                            pr = 60000;
                            break;
                        case 3:
                            pr = 100000;
                            break;
                        default:
                    }
                    double distance = Common.GetDistance(mLat, mLng, p.getLatitude(), p.getLongitude());

                    if (distance <= pr) {
                        mDataset.add(p);
                    }
                }//for
                mAdapter = new PieceAdapter(list);
                mRecyclerView.setAdapter(mAdapter);
            }//done
        });
    }
}
