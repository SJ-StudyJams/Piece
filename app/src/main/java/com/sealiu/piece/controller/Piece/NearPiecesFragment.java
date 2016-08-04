package com.sealiu.piece.controller.Piece;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sealiu.piece.R;
import com.sealiu.piece.controller.Maps.Common;
import com.sealiu.piece.controller.viewholder.PieceViewHolderText;
import com.sealiu.piece.model.Piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by liuyang
 * on 2016/8/4.
 */

public class NearPiecesFragment extends Fragment {
    double mLat;
    double mLng;
    List<Piece> mDataset;

    private DatabaseReference mDatabase;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecycler;

    public NearPiecesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_pieces, container, false);

        mDataset = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecycler = (RecyclerView) rootView.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLat = getArguments().getDouble("lat");
        mLng = getArguments().getDouble("lng");

        // Set up Layout Manager, reverse layout
        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        mDatabase.child("pieces").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, Object> dataSnapshotValue = (HashMap<String, Object>) dataSnapshot.getValue();
                        Set<String> set = dataSnapshotValue.keySet();

                        for (String key : set) {
                            HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshotValue.get(key);

                            Double lat = (Double) map.get("latitude");
                            Double lng = (Double) map.get("longitude");
                            int vi = Integer.valueOf(map.get("visibility").toString());

                            int pr = 0;

                            switch (vi) {
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

                            double distance = Common.GetDistance(mLat, mLng, lat, lng);
                            if (distance <= pr) {
                                Piece piece = new Piece(
                                        map.get("author").toString(),
                                        map.get("uid").toString(),
                                        key,
                                        map.get("content").toString(),
                                        lat,
                                        lng,
                                        vi,
                                        Integer.valueOf(map.get("type").toString())
                                );

                                mDataset.add(piece);
                            }
                        }

                        mAdapter = new PieceAdapter(mDataset) {
                            @Override
                            public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                                // - get element from your dataset at this position
                                // - replace the contents of the view with that element
                                switch (holder.getItemViewType()) {
                                    case 1:
                                        PieceViewHolderText holderText = (PieceViewHolderText) holder;
                                        holderText.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                // launch PieceDetailActivity
                                                Intent intent = new Intent(getActivity(), PieceDetailActivity.class);
                                                intent.putExtra(PieceDetailActivity.EXTRA_POST_KEY, mDataset.get(position).pid);
                                                startActivity(intent);
                                            }
                                        });
                                        holderText.bindToPiece(mDataset.get(position));
                                        break;
                                    case 2:
                                        break;
                                    case 3:
                                        break;
                                }
                            }
                        };
                        mRecycler.setAdapter(mAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }
}
