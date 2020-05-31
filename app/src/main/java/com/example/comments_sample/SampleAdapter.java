package com.example.comments_sample;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SampleAdapter extends RecursiveRecyclerAdapter<SampleAdapter.ViewHolder> {

    List<DummyData> mData;

    void setData(List<DummyData> data) {
        setItems(data);
        mData = data;
    }

    @NonNull
    @Override
    public SampleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sample_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SampleAdapter.ViewHolder holder, int position) {

        super.onBindViewHolder(holder, position);
        //notifyDataSetChanged();
        position = holder.getAdapterPosition();
        DummyData d = (DummyData)getItem(position);
        Log.d("sampleadapter","pos got is "+position);
        if(!isParent(position)){
            holder.line_view.setVisibility(View.INVISIBLE);
            holder.itemView.setPadding(40*mItems.get(position).depth,0,0,0);
        }
        else{
            holder.line_view.setVisibility(View.VISIBLE);
        }
        holder.sampleTextView.setText(getItem(position).toString());
    }



    class ViewHolder extends RecyclerView.ViewHolder{


        TextView sampleTextView;
        View line_view;
        public ViewHolder(View itemView) {
            super(itemView);
            sampleTextView = (TextView) itemView.findViewById(R.id.tv1);
            line_view = (View) itemView.findViewById(R.id.line_view);
        }
    }
}
