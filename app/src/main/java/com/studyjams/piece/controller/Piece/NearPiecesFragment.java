package com.studyjams.piece.controller.Piece;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.studyjams.piece.R;
import com.studyjams.piece.controller.Maps.Common;
import com.studyjams.piece.controller.viewholder.PieceViewHolderW;
import com.studyjams.piece.controller.viewholder.PieceViewHolderWL;
import com.studyjams.piece.controller.viewholder.PieceViewHolderWP;
import com.studyjams.piece.controller.viewholder.PieceViewHolderWPL;
import com.studyjams.piece.model.Piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by liuyang
 * on 2016/8/4.
 */

public class NearPiecesFragment extends Fragment {

    private static final String TAG = "NearPiecesFragment";

    double mLat;
    double mLng;
    List<Piece> mDataset;

    FirebaseUser mUser;
    TextView empty;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecycler;
    private DatabaseReference mDatabase;
    private DatabaseReference mPieceRef;

    public NearPiecesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_pieces, container, false);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        empty = (TextView) rootView.findViewById(R.id.empty);

        mDataset = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mPieceRef = mDatabase.child("pieces");

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

        ValueEventListener pieceListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDataset.clear();
                if (dataSnapshot != null) {
                    HashMap<String, Object> dataSnapshotValue = (HashMap<String, Object>) dataSnapshot.getValue();
                    if (dataSnapshotValue != null) {
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
                                        Integer.valueOf(map.get("type").toString()),
                                        map.get("date").toString()
                                );
                                piece.likeCount = Integer.valueOf(map.get("likeCount").toString());

                                Set<String> pk = map.keySet();
                                if (pk.contains("likes"))
                                    piece.likes = (Map<String, Boolean>) map.get("likes");

                                if (pk.contains("url"))
                                    piece.url = map.get("url").toString();

                                if (pk.contains("image"))
                                    piece.image = map.get("image").toString();

                                mDataset.add(piece);
                            }
                        }
                    } else {
                        empty.setVisibility(View.VISIBLE);
                    }
                }

                mAdapter = new PieceAdapter(mDataset) {
                    @Override
                    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

                        final Piece piece = mDataset.get(position);

                        final DatabaseReference globalPieceRef = mPieceRef.child(piece.pid);
                        final DatabaseReference userPieceRef = mDatabase.child("user-pieces")
                                .child(piece.uid).child(piece.pid);

                        Log.i(TAG, "likeCount:" + piece.likeCount);

                        // - get element from your dataset at this position
                        // - replace the contents of the view with that element
                        switch (holder.getItemViewType()) {
                            case 0:
                                PieceViewHolderW holderW = (PieceViewHolderW) holder;
                                if (piece.likes.containsKey(mUser.getUid())) {
                                    holderW.likeIcon.setImageResource(R.drawable.ic_favorite_24dp);
                                } else {
                                    holderW.likeIcon.setImageResource(R.drawable.ic_favorite_border_24dp);
                                }
                                holderW.bindToPiece(piece, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        onLikeClicked(globalPieceRef);
                                        onLikeClicked(userPieceRef);
                                    }
                                });
                                break;
                            case 1:
                                PieceViewHolderWL holderWL = (PieceViewHolderWL) holder;
                                if (piece.likes.containsKey(mUser.getUid())) {
                                    holderWL.likeIcon.setImageResource(R.drawable.ic_favorite_24dp);
                                } else {
                                    holderWL.likeIcon.setImageResource(R.drawable.ic_favorite_border_24dp);
                                }
                                holderWL.bindToPiece(piece, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        onLikeClicked(globalPieceRef);
                                        onLikeClicked(userPieceRef);
                                    }
                                });
                                break;
                            case 2:
                                PieceViewHolderWP holderWP = (PieceViewHolderWP) holder;
                                if (piece.likes.containsKey(mUser.getUid())) {
                                    holderWP.likeIcon.setImageResource(R.drawable.ic_favorite_24dp);
                                } else {
                                    holderWP.likeIcon.setImageResource(R.drawable.ic_favorite_border_24dp);
                                }
                                holderWP.bindToPiece(piece, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        onLikeClicked(globalPieceRef);
                                        onLikeClicked(userPieceRef);
                                    }
                                });
                                break;
                            case 3:
                                PieceViewHolderWPL holderWPL = (PieceViewHolderWPL) holder;
                                if (piece.likes.containsKey(mUser.getUid())) {
                                    holderWPL.likeIcon.setImageResource(R.drawable.ic_favorite_24dp);
                                } else {
                                    holderWPL.likeIcon.setImageResource(R.drawable.ic_favorite_border_24dp);
                                }
                                holderWPL.bindToPiece(piece, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        onLikeClicked(globalPieceRef);
                                        onLikeClicked(userPieceRef);
                                    }
                                });
                                break;
                        }

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // launch PieceDetailActivity
                                Intent intent = new Intent(getActivity(), PieceDetailActivity.class);
                                intent.putExtra(PieceDetailActivity.EXTRA_PIECE_KEY, piece.pid);
                                startActivity(intent);
                            }
                        });
                    }
                };
                mRecycler.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mPieceRef.addValueEventListener(pieceListener);
    }

    private void onLikeClicked(DatabaseReference pieceRef) {
        Log.d(TAG, "onLikeClicked");
        PieceDetailActivity.onLikeClick(pieceRef, mUser.getUid());
    }

}
