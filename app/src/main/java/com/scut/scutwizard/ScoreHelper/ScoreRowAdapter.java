package com.scut.scutwizard.ScoreHelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.scut.scutwizard.R;
import com.scut.scutwizard.ScoreHelper.DetailView.ScoreDetailActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ScoreRowAdapter extends RecyclerView.Adapter<ScoreRowAdapter.ViewHolder> {
    private List<Score> mScoreList;

    public ScoreRowAdapter() {
        this.mScoreList = new ArrayList<>();
    }

    public ScoreRowAdapter(List<Score> scoreList) {
        this.mScoreList = scoreList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.fragment_score_row, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    void setData(ArrayList<Score> newScores) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return mScoreList == null ? 0 : mScoreList.size();
            }

            @Override
            public int getNewListSize() {
                return newScores == null ? 0 : newScores.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                Score score = mScoreList.get(oldItemPosition);
                Score newScore = newScores.get(newItemPosition);
                return score.getId() == newScore.getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                Score score = mScoreList.get(oldItemPosition);
                Score newScore = newScores.get(newItemPosition);
                return score.equals(newScore);
            }
        }, true);
        this.mScoreList = newScores;
//        int id = new Random().nextInt(100);
//        for (Score s : mScoreList) {
//            Log.d("sneezer", "Adapter setData: " + id + " " + s.getId());
//        }
        diffResult.dispatchUpdatesTo(ScoreRowAdapter.this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Score score = mScoreList.get(position);
        holder.item = score;
        final double value = score.getValue();
        if (value >= 0)
            holder.itemValue.setTextColor(R.color.colorPrimary);
        else
            holder.itemValue.setTextColor(R.color.colorAccent);
        holder.itemValue.setText(String.format("%+.1f", value));
        holder.itemDescription.setText(score.getDescription());
        holder.itemDate.setText(String.format("%tF | %s",
                                              score.getEventDate(),
                                              score.getSpecificCategory()));
    }

    @Override
    public int getItemCount() {
        return mScoreList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemDescription, itemDate;//, itemValue;
        TickerView itemValue;
        Score      item;
        Context    mContext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            itemDescription = itemView.findViewById(R.id.textView_description);
            itemDate = itemView.findViewById(R.id.textView_eventDate);
//            itemValue = itemView.findViewById(R.id.textView_value);
            itemValue = itemView.findViewById(R.id.ticker_value);
            itemValue.setCharacterLists(TickerUtils.provideNumberList(), "+-");


            final Context mContext = itemView.getContext();

            itemView.setOnClickListener(view -> startDetailActivity());

            final XPopup.Builder popupBuilder = new XPopup.Builder(mContext).watchView(itemView);
            itemView.setOnLongClickListener(v -> {
                popupBuilder.asBottomList(String.format(mContext.getString(R.string.score_tip),
                                                        itemDescription.getText()),
                                          new String[]{mContext.getString(R.string.detail),
                                                       mContext.getString(R.string.share),
                                                       mContext.getString(R.string.delete)},
                                          (position, text) -> {
                                              switch (position) {
                                                  case 0: // info
                                                      startDetailActivity();
                                                      break;
                                                  case 1: // share
                                                      new XPopup.Builder(mContext).asLoading(
                                                              mContext.getString(R.string.loading_share))
                                                                                  .show()
                                                                                  .delayDismissWith(
                                                                                          3500,
                                                                                          () -> new XPopup.Builder(
                                                                                                  mContext)
                                                                                                  .asConfirm(
                                                                                                          mContext.getString(
                                                                                                                  R.string.forgot),
                                                                                                          mContext.getString(
                                                                                                                  R.string.no_share),
                                                                                                          "",
                                                                                                          mContext.getString(
                                                                                                                  R.string.accept),
                                                                                                          () -> {},
                                                                                                          () -> {},
                                                                                                          true)
                                                                                                  .show());
                                                      break;
                                                  case 2: // delete
                                                      new XPopup.Builder(mContext).asCustom(new DeleteScoreConfirmPopup(
                                                              mContext,
                                                              item.getId(),
                                                              item.getCategoryInt())).show();
                                              }
                                          }).show();
                return true;
            });
        }

        private void startDetailActivity() {
            Intent intent = new Intent(mContext, ScoreDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("score", item);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    }
}
