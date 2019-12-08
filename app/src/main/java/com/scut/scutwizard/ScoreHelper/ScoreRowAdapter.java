package com.scut.scutwizard.ScoreHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.scut.scutwizard.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScoreRowAdapter extends RecyclerView.Adapter<ScoreRowAdapter.ViewHolder> {
    private List<Score> mScoreList;

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
            itemDescription = itemView.findViewById(R.id.textView_description);
            itemDate = itemView.findViewById(R.id.textView_eventDate);
//            itemValue = itemView.findViewById(R.id.textView_value);
            itemValue = itemView.findViewById(R.id.ticker_value);
            itemValue.setCharacterLists(TickerUtils.provideNumberList(), "+-");
        }
    }
}
