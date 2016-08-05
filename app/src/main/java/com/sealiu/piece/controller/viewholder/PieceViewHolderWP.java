package com.sealiu.piece.controller.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sealiu.piece.R;
import com.sealiu.piece.model.Piece;
import com.sealiu.piece.utils.ImageLoader.BitmapUtils;

/**
 * Created by liuyang
 * on 2016/8/4.
 */

public class PieceViewHolderWP extends RecyclerView.ViewHolder {
    public TextView authorView;
    public TextView numView;
    public TextView bodyView;
    public TextView dateView;
    public ImageView picView;

    public PieceViewHolderWP(View itemView) {
        super(itemView);

        authorView = (TextView) itemView.findViewById(R.id.piece_author);
        numView = (TextView) itemView.findViewById(R.id.piece_num_view);
        bodyView = (TextView) itemView.findViewById(R.id.piece_body);
        dateView = (TextView) itemView.findViewById(R.id.piece_date);
        picView = (ImageView) itemView.findViewById(R.id.piece_image);
    }

    public void bindToPiece(Piece piece) {
        authorView.setText(piece.author);
        numView.setText(String.valueOf(piece.viewCount));
        bodyView.setText(piece.content);
        dateView.setText(piece.date);
        BitmapUtils bitmapUtils = new BitmapUtils();
        bitmapUtils.disPlay(picView, piece.image);
    }
}
