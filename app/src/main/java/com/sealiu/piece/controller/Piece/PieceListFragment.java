package com.sealiu.piece.controller.Piece;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sealiu.piece.R;
import com.sealiu.piece.controller.viewholder.PieceViewHolderText;
import com.sealiu.piece.model.Piece;

/**
 * Created by liuyang
 * on 2016/8/4.
 */

public abstract class PieceListFragment extends Fragment {
    private static final String TAG = "PieceListFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<Piece, PieceViewHolderText> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    public PieceListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_pieces, container, false);

        // [START create_database_reference]
        // [END create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecycler = (RecyclerView) rootView.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query piecesQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<Piece, PieceViewHolderText>(Piece.class, R.layout.piece_words,
                PieceViewHolderText.class, piecesQuery) {
            @Override
            protected void populateViewHolder(final PieceViewHolderText viewHolder, Piece model, int position) {
                final DatabaseReference pieceRef = getRef(position);

                // set click listener for the whole piece view
                final String pieceKey = pieceRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // launch PieceDetailActivity
                        Intent intent = new Intent(getActivity(), PieceDetailActivity.class);
                        intent.putExtra(PieceDetailActivity.EXTRA_POST_KEY, pieceKey);
                        startActivity(intent);
                    }
                });

                // Bind Post to ViewHolder
                viewHolder.bindToPiece(model);
            }
        };

        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);
}
