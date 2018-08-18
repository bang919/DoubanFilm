package com.bigbang.doubanfilm.activity;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.bigbang.doubanfilm.R;
import com.bigbang.doubanfilm.adapter.SearchFilmAdapter;
import com.bigbang.doubanfilm.bean.request.SearchRequestBean;
import com.bigbang.doubanfilm.bean.response.SearchResponseBean;
import com.bigbang.doubanfilm.common.BaseActivity;
import com.bigbang.doubanfilm.presenter.MainPresenter;
import com.bigbang.doubanfilm.utils.ToastUtils;
import com.bigbang.doubanfilm.view.MainView;
import com.trello.rxlifecycle2.navi.NaviLifecycle;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView, View.OnClickListener {

    private RecyclerView mSearchRecyclerView;
    private SearchFilmAdapter mSearchFilmAdapter;
    private EditText mTagEt;
    private EditText mYearAfter;
    private EditText mYearBefore;
    private EditText mScoreAfter;
    private EditText mScoreBefore;
    private EditText mHotEt;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter initPresenter() {
        return new MainPresenter(this, this, NaviLifecycle.createActivityLifecycleProvider(this));
    }


    @Override
    protected void initView() {
        mSearchRecyclerView = findViewById(R.id.recycler_main_search);
        mTagEt = findViewById(R.id.et_tag);
        mYearAfter = findViewById(R.id.et_year_after);
        mYearBefore = findViewById(R.id.et_year_before);
        mScoreAfter = findViewById(R.id.et_score_after);
        mScoreBefore = findViewById(R.id.et_score_before);
        mHotEt = findViewById(R.id.et_hot);

        mSearchRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mSearchFilmAdapter = new SearchFilmAdapter();
        mSearchRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = 10;
                outRect.right = 10;
            }
        });
        mSearchRecyclerView.setAdapter(mSearchFilmAdapter);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int position = viewHolder.getAdapterPosition();
                int targetPosition = target.getAdapterPosition();
                mSearchFilmAdapter.notifyItemMoved(position, targetPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {
                    mSearchFilmAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(mSearchRecyclerView);
    }

    @Override
    protected void initData() {
        requestData();
    }

    @Override
    protected void initEvent() {
        findViewById(R.id.search_btn).setOnClickListener(this);
    }

    private void requestData() {
        if (!TextUtils.isEmpty(mTagEt.getText().toString())) {
            mSearchFilmAdapter.setSubjectsBeans(null);
            SearchRequestBean searchRequestBean = new SearchRequestBean();
            searchRequestBean.setTag(mTagEt.getText().toString());
            searchRequestBean.setCount(800);
//            mPresenter.search(searchRequestBean, Integer.valueOf(mYearAfter.getText().toString()), Integer.valueOf(mYearBefore.getText().toString()),
//                    Float.valueOf(mScoreAfter.getText().toString()), Float.valueOf(mScoreBefore.getText().toString()), Float.valueOf(mHotEt.getText().toString()));
            mPresenter.getTop250();
        }
    }

    @Override
    public void onSearchResponse(SearchResponseBean searchResponseBean) {
        mSearchFilmAdapter.setSubjectsBeans(searchResponseBean.getSubjects());
    }

    @Override
    public void onSearchError(String error) {
        ToastUtils.showShort(error);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_btn:
                requestData();
                break;
        }
    }
}
