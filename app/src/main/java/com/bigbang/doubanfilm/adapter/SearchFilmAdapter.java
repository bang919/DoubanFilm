package com.bigbang.doubanfilm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigbang.doubanfilm.R;
import com.bigbang.doubanfilm.bean.response.SearchResponseBean;
import com.bigbang.doubanfilm.utils.GlideUtils;
import com.bigbang.doubanfilm.widget.MyThermometer;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.ArrayList;

import static com.bigbang.doubanfilm.common.Constants.BASIC_THERMOMETER;

/**
 * Created by Administrator on 2018/2/8.
 */

public class SearchFilmAdapter extends RecyclerView.Adapter<SearchFilmAdapter.SearchHolder> {


    private ArrayList<SearchResponseBean.SubjectsBean> mSubjectsBeans;


    public void setSubjectsBeans(ArrayList<SearchResponseBean.SubjectsBean> datas) {
        mSubjectsBeans = datas;
        notifyDataSetChanged();
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_main, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, int position) {
        SearchResponseBean.SubjectsBean subjectsBean = mSubjectsBeans.get(position);
        GlideUtils.loadImage(holder.mBgImage, -1, subjectsBean.getImages().getMedium(), new CenterCrop());
        holder.mYear.setText(subjectsBean.getYear());
        holder.mStar.setText(subjectsBean.getRating().getAverage()+"");
        holder.mThermometer.setTemperature(subjectsBean.getCollect_count() / BASIC_THERMOMETER);
        holder.mTitle.setText(subjectsBean.getTitle());
    }

    @Override
    public int getItemCount() {
        return mSubjectsBeans == null ? 0 : mSubjectsBeans.size();
    }

    class SearchHolder extends RecyclerView.ViewHolder {
        ImageView mBgImage;
        TextView mYear;
        TextView mStar;
        MyThermometer mThermometer;
        TextView mTitle;

        public SearchHolder(View itemView) {
            super(itemView);
            mBgImage = itemView.findViewById(R.id.iv_bg);
            mYear = itemView.findViewById(R.id.tv_year);
            mStar = itemView.findViewById(R.id.tv_star);
            mThermometer = itemView.findViewById(R.id.thermometer);
            mTitle = itemView.findViewById(R.id.tv_title);
        }
    }
}
