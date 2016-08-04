package com.sealiu.piece.controller.Piece;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sealiu.piece.R;
import com.sealiu.piece.controller.viewholder.PieceViewHolderText;
import com.sealiu.piece.model.Piece;

import java.util.List;

/**
 * Created by liuyang
 * on 2016/7/15.
 */
public class PieceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Piece> mDataset;

    public PieceAdapter(List<Piece> list) {
        mDataset = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 1:
                // piece_words
                View v1 = inflater.inflate(R.layout.piece_words, parent, false);
                viewHolder = new PieceViewHolderText(v1);
                break;
            default:
                View v = inflater.inflate(R.layout.piece_words, parent, false);
                viewHolder = new PieceViewHolderText(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type = mDataset.get(position).type;
        if (type == 1 || type == 2 || type == 3) return type;
        else return 1;
    }

}
