package com.sealiu.piece.controller.User;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.sealiu.piece.controller.Piece.PieceListFragment;

/**
 * Created by liuyang
 * on 2016/8/4.
 */

public class MyPiecesFragment extends PieceListFragment {
    public MyPiecesFragment() {
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("user-pieces").child(getUid());
    }
}
