package com.scut.scutwizard.ScoreHelper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.scut.scutwizard.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        int id = new Random().nextInt(100);
        for (Score s : mScoreList) {
            Log.d("sneezer", "adapter setData: " + id + " " + s.getId());
        }
        diffResult.dispatchUpdatesTo(ScoreRowAdapter.this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Score score = mScoreList.get(position);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            final Context mContext = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Ouch", Toast.LENGTH_SHORT).show();
                }
            });
            final XPopup.Builder builder = new XPopup.Builder(mContext).watchView(itemView);
            itemView.setOnLongClickListener(v -> {
                builder.asAttachList(new String[]{"详细信息...", "删除"}, null, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text) {
                        Toast.makeText(mContext, position + text, Toast.LENGTH_SHORT).show();
                    }
                }).show();
                return true;
            });
            itemDescription = itemView.findViewById(R.id.textView_description);
            itemDate = itemView.findViewById(R.id.textView_eventDate);
//            itemValue = itemView.findViewById(R.id.textView_value);
            itemValue = itemView.findViewById(R.id.ticker_value);
            itemValue.setCharacterLists(TickerUtils.provideNumberList(), "+-");
        }
    }
}
