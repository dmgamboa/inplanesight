package com.inplanesight.ui.leaderboards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inplanesight.R;
import com.inplanesight.models.Leaderboard;

public class LeaderboardRecyclerViewAdapter extends RecyclerView.Adapter<LeaderboardRecyclerViewAdapter.LeaderboardViewHolder> {

    Context context;
    Leaderboard[] leaderboards;

    public LeaderboardRecyclerViewAdapter(Context context, Leaderboard[] leaderboards) {
        this.context = context;
        this.leaderboards = leaderboards;
    }

    public LeaderboardRecyclerViewAdapter(Context context) {
        this.context = context;
        this.leaderboards = new Leaderboard[0];
    }

    private String toOrdinal(int i) {
        int last = i % 10;
        switch (last) {
            case 1:
                if (i != 11) {
                    return i + "st";
                }
            case 2:
                return i + "nd";
            case 3:
                return i + "rd";
            default:
                return i + "th";
        }

    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_leaderboard_row, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        Leaderboard leaderboard = leaderboards[position];
        holder.rank.setText(toOrdinal(position + 1));
        holder.name.setText(leaderboard.getUser());
        String score = "" + leaderboard.getScore();
        holder.score.setText(score);
    }

    @Override
    public int getItemCount() {
        return leaderboards.length;
    }

    public class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        TextView rank;
        TextView name;
        TextView score;


        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.leaderboardsRank);
            name = itemView.findViewById(R.id.leaderboardsName);
            score = itemView.findViewById(R.id.leaderboardsScore);
        }
    }
}
