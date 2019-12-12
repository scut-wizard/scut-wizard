package com.scut.scutwizard.ScoreHelper.DetailView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scut.scutwizard.R;
import com.scut.scutwizard.ScoreHelper.Score;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEWTYPE_PLAIN = 0, VIEWTYPE_PHOTO = 1;
    private Score mScore;

    public DetailAdapter(Score score) {
        this.mScore = score;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEWTYPE_PHOTO:
                return new PhotoViewHolder(LayoutInflater.from(parent.getContext())
                                                         .inflate(R.layout.score_detail_row_photo,
                                                                  parent,
                                                                  false));
            case VIEWTYPE_PLAIN:
            default:
                return new PlainViewHolder(LayoutInflater.from(parent.getContext())
                                                         .inflate(R.layout.score_detail_row_plain,
                                                                  parent,
                                                                  false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEWTYPE_PHOTO:
                break;
            case VIEWTYPE_PLAIN:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 11;
    }

    class PlainViewHolder extends RecyclerView.ViewHolder {

        public PlainViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
