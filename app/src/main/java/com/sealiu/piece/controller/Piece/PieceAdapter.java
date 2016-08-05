package com.sealiu.piece.controller.Piece;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sealiu.piece.R;
import com.sealiu.piece.controller.viewholder.PieceViewHolderW;
import com.sealiu.piece.controller.viewholder.PieceViewHolderWL;
import com.sealiu.piece.controller.viewholder.PieceViewHolderWP;
import com.sealiu.piece.controller.viewholder.PieceViewHolderWPL;
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
            case 0:
                // piece_w
                View w = inflater.inflate(R.layout.piece_w, parent, false);
                viewHolder = new PieceViewHolderW(w);
                break;
            case 1:
                View wl = inflater.inflate(R.layout.piece_wl, parent, false);
                viewHolder = new PieceViewHolderWL(wl);
                break;
            case 2:
                View wp = inflater.inflate(R.layout.piece_wp, parent, false);
                viewHolder = new PieceViewHolderWP(wp);
                break;
            case 3:
                View wpl = inflater.inflate(R.layout.piece_wpl, parent, false);
                viewHolder = new PieceViewHolderWPL(wpl);
                break;
            default:
                View v = inflater.inflate(R.layout.piece_w, parent, false);
                viewHolder = new PieceViewHolderW(v);
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
        return mDataset.get(position).type;
    }

}
