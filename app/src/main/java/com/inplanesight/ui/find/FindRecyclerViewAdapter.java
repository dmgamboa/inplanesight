package com.inplanesight.ui.find;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inplanesight.R;
import com.inplanesight.models.Hunt;

public class FindRecyclerViewAdapter extends RecyclerView.Adapter<FindRecyclerViewAdapter.FindViewHolder> {

    Context context;
    Hunt[] hunts;

    public FindRecyclerViewAdapter(Context context, Hunt[] hunts) {
        this.context = context;
        this.hunts = hunts;
    }

    @NonNull
    @Override
    public FindViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_find_carousel, parent, false);
        return new FindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindViewHolder holder, int position) {
        Hunt hunt = hunts[position];
    }

    @Override
    public int getItemCount() {
        return hunts.length;
    }

    public class FindViewHolder extends RecyclerView.ViewHolder {

        ImageView imageContainer;

        public FindViewHolder(@NonNull View itemView) {
            super(itemView);
            imageContainer = itemView.findViewById(R.id.findImg);
        }
    }
}
