package com.studyjams.piece.controller.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.studyjams.piece.R;
import com.studyjams.piece.model.Piece;
import com.studyjams.piece.utils.ImageLoader.BitmapUtils;

/**
 * Created by liuyang
 * on 2016/8/4.
 */

public class PieceViewHolderWPL extends RecyclerView.ViewHolder {
    public TextView authorView;
    public TextView bodyView;
    public TextView dateView;
    public ImageView picView;
    public TextView linkView;
    public TextView likeView;
    public ImageView likeIcon;
    public LinearLayout likeViewHolder;

    public PieceViewHolderWPL(View itemView) {
        super(itemView);

        authorView = (TextView) itemView.findViewById(R.id.piece_author);
        bodyView = (TextView) itemView.findViewById(R.id.piece_body);
        dateView = (TextView) itemView.findViewById(R.id.piece_date);
        linkView = (TextView) itemView.findViewById(R.id.piece_link);
        picView = (ImageView) itemView.findViewById(R.id.piece_image);
        likeView = (TextView) itemView.findViewById(R.id.piece_like_num);
        likeIcon = (ImageView) itemView.findViewById(R.id.piece_like_icon);
        likeViewHolder = (LinearLayout) itemView.findViewById(R.id.piece_like_holder);
    }

    public void bindToPiece(Piece piece, View.OnClickListener likeClickListener) {
        authorView.setText(piece.author);
        likeView.setText(String.valueOf(piece.likeCount));
        bodyView.setText(piece.content);
        dateView.setText(piece.date);
        linkView.setText(piece.url);
        BitmapUtils bitmapUtils = new BitmapUtils();
        bitmapUtils.disPlay(picView, piece.image);

        likeViewHolder.setOnClickListener(likeClickListener);
    }
}
