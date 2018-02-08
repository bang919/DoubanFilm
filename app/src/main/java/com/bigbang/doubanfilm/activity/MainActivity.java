package com.bigbang.doubanfilm.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        mSearchRecyclerView.setAdapter(mSearchFilmAdapter);
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
            SearchRequestBean searchRequestBean = new SearchRequestBean();
            searchRequestBean.setTag(mTagEt.getText().toString());
            searchRequestBean.setCount(500);
            mPresenter.search(searchRequestBean, Integer.valueOf(mYearAfter.getText().toString()), Integer.valueOf(mYearBefore.getText().toString()),
                    Integer.valueOf(mScoreAfter.getText().toString()), Integer.valueOf(mScoreBefore.getText().toString()), Float.valueOf(mHotEt.getText().toString()));
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
