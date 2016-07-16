package com.sealiu.piece.controller.User;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.sealiu.piece.R;
import com.sealiu.piece.controller.Piece.PieceAdapter;
import com.sealiu.piece.model.Constants;
import com.sealiu.piece.model.Piece;
import com.sealiu.piece.utils.SPUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.graphics.PorterDuff.Mode;

/**
 * 展示用户发送过的小纸条（类似朋友圈）
 * 点击用户头像进入查看页面
 */
public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "UserActivity";
    private static final String LAYOUT_MANAGER_FLAG = "LayoutManager";
    private ImageButton linear;
    private ImageButton grid;
    private ImageButton staggeredGrid;
    private FloatingActionButton fab;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int mLayoutManagerFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        linear = (ImageButton) findViewById(R.id.linear);
        grid = (ImageButton) findViewById(R.id.grid);
        staggeredGrid = (ImageButton) findViewById(R.id.staggered_grid);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_piece_recycler_view);

        fab.setOnClickListener(this);
        linear.setOnClickListener(this);
        grid.setOnClickListener(this);
        staggeredGrid.setOnClickListener(this);

        // set RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // set LayoutManager
        if (savedInstanceState != null) {
            mLayoutManagerFlag = savedInstanceState.getInt(LAYOUT_MANAGER_FLAG);
        } else {
            mLayoutManagerFlag = 1;
        }

        switch (mLayoutManagerFlag) {
            case 1:
                setLinear();
                break;
            case 2:
                setGrid();
                break;
            case 3:
                setStaggeredGrid();
                break;
        }
        // fetch data, and set adapter
        initUI();
    }

    /**
     * 初始化界面显示
     */
    private void initUI() {

        String nickName = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_NICKNAME, "");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(nickName);
        }

        String userObjectId = SPUtils.getString(this, Constants.SP_FILE_NAME, Constants.SP_USER_OBJECT_ID, "");
        if (!userObjectId.equals("")) {
            BmobQuery<Piece> query = new BmobQuery<>();
            query.addWhereEqualTo("authorID", userObjectId);
            query.setLimit(1000);
            query.order("-createdAt,-updatedAt");
            query.findObjects(new FindListener<Piece>() {
                @Override
                public void done(List<Piece> list, BmobException e) {
                    mAdapter = new PieceAdapter(list);
                    mRecyclerView.setAdapter(mAdapter);
                }//done
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                startActivity(new Intent(UserActivity.this, EditActivity.class));
                break;
            case R.id.linear:
                setLinear();
                break;
            case R.id.grid:
                setGrid();
                break;
            case R.id.staggered_grid:
                setStaggeredGrid();
                break;
            default:
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LAYOUT_MANAGER_FLAG, mLayoutManagerFlag);
    }

    public void setLinear() {
        grid.clearColorFilter();
        staggeredGrid.clearColorFilter();
        linear.setColorFilter(0xff0099cc, Mode.MULTIPLY);
        mLayoutManagerFlag = 1;
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void setGrid() {
        linear.clearColorFilter();
        staggeredGrid.clearColorFilter();
        grid.setColorFilter(0xff0099cc, Mode.MULTIPLY);
        mLayoutManagerFlag = 2;
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void setStaggeredGrid() {
        linear.clearColorFilter();
        grid.clearColorFilter();
        staggeredGrid.setColorFilter(0xff0099cc, Mode.MULTIPLY);
        mLayoutManagerFlag = 3;
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLayoutManager = new StaggeredGridLayoutManager(3, 1);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new StaggeredGridLayoutManager(2, 1);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}
