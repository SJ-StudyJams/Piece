package com.sealiu.piece.controller.Piece;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sealiu.piece.R;
import com.sealiu.piece.model.Piece;

import java.io.InputStream;
import java.net.URL;
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
                // create a new view
                View v1 = inflater.inflate(R.layout.piece_words, parent, false);
                viewHolder = new ViewHolder(v1);
                break;
            case 2:
                View v2 = inflater.inflate(R.layout.piece_images, parent, false);
                viewHolder = new ViewHolder2(v2);
                break;
            default:
                View v = inflater.inflate(R.layout.piece_words, parent, false);
                viewHolder = new ViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        switch (holder.getItemViewType()) {
            case 1:
                ViewHolder holder1 = (ViewHolder) holder;
                holder1.mContentTextView.setText(mDataset.get(position).getContent());
                holder1.mCreatedAtTextView.setText(mDataset.get(position).getCreatedAt());
                break;
            case 2:
                ViewHolder2 holder2 = (ViewHolder2) holder;
                holder2.mContentTextView.setText(mDataset.get(position).getContent());
                holder2.mCreatedAtTextView.setText(mDataset.get(position).getCreatedAt());
                String imageUrl = mDataset.get(position).getImage() == null ? "" : mDataset.get(position).getImage();
                if (!imageUrl.equals("")) {
                    Bitmap bitmap = downloadPic(imageUrl);
                    if (bitmap != null)
                        holder2.mImage.setImageBitmap(bitmap);
                }
                break;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type = mDataset.get(position).getType();
        if (type == 1 || type == 2) return type;
        else return 1;
    }

    private Bitmap downloadPic(final String imageUrl) {

        final Bitmap[] bitmap = new Bitmap[1];
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL(imageUrl);
                    //打开URL对应的资源输入流
                    InputStream inputStream = url.openStream();
                    //从InputStream流中解析出图片
                    bitmap[0] = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();

        return bitmap[0];
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mContentTextView;
        public TextView mCreatedAtTextView;

        public ViewHolder(View v) {
            super(v);
            mContentTextView = (TextView) v.findViewById(R.id.piece_content);
            mCreatedAtTextView = (TextView) v.findViewById(R.id.piece_createdAt);
        }
    }

    // Another type view holder
    public static class ViewHolder2 extends RecyclerView.ViewHolder {
        public TextView mContentTextView;
        public TextView mCreatedAtTextView;
        public ImageView mImage;

        public ViewHolder2(View v) {
            super(v);
            mContentTextView = (TextView) v.findViewById(R.id.piece_content);
            mCreatedAtTextView = (TextView) v.findViewById(R.id.piece_createdAt);
            mImage = (ImageView) v.findViewById(R.id.piece_image);
        }
    }
}
