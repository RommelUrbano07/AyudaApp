package com.example.ayudaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CaseAdapter extends RecyclerView.Adapter<CaseAdapter.ExampleViewHolder> {
    private ArrayList<CaseItem> mExampleList;
//    private


//    public void setOnItemClickListener(On) {
//        mListener = listener;
//    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textView1);
            mTextView2 = itemView.findViewById(R.id.textView2);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            listener.onItemClick(position);
//                        }
//                    }
//                }
//            });
        }
    }

    public CaseAdapter(ArrayList<CaseItem> exampleList){
        mExampleList = exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.covid_case_layout, parent, false);
        CaseAdapter.ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        CaseItem currentItem = mExampleList.get(position);
        holder.mTextView1.setText(currentItem.getCaseText1());
        holder.mTextView2.setText(currentItem.getCaseText2());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

}
