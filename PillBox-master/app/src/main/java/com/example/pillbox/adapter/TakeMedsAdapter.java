package com.example.pillbox.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pillbox.R;
import com.example.pillbox.TakeMedication;
import com.example.pillbox.activity.TakeMedActivity;

import java.util.ArrayList;

/**
 * Provide a binding from TakeMedication data in PillBoxFragment to CardView: card_take_meds displayed within RecyclerView.
 * Created by luishengjie on 23/6/17.
 */

public class TakeMedsAdapter extends RecyclerView.Adapter<TakeMedsAdapter.TakeMedsViewHolder> {
    private ArrayList<TakeMedication> mMedData;
    private Context mContext;

    public TakeMedsAdapter(ArrayList<TakeMedication> medData, Context context){
        mMedData = medData;
        mContext = context;
    }

    @Override
    public TakeMedsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_take_meds, parent, false);
        TakeMedsViewHolder takeMedsViewHolder = new TakeMedsViewHolder(view);
        return takeMedsViewHolder;
    }

    @Override
    public void onBindViewHolder(final TakeMedsViewHolder holder, int position) {
        final int medId = mMedData.get(position).getMedId();
        final String medName = mMedData.get(position).getMedName();
        final String medTimeOfDay = mMedData.get(position).getTimeOfDay().toUpperCase();
        if (medTimeOfDay.equals("MORNING")){
            holder.mImageView.setBackgroundResource(R.drawable.ic_action_morning);
        }
        if (medTimeOfDay.equals("AFTERNOON")){
            holder.mImageView.setBackgroundResource(R.drawable.ic_action_afternoon);
        }
        if (medTimeOfDay.equals("EVENING")){
            holder.mImageView.setBackgroundResource(R.drawable.ic_action_evening);
        }
        if (medTimeOfDay.equals("NIGHT")){
            holder.mImageView.setBackgroundResource(R.drawable.ic_action_night);
        }
        holder.mTextView.setText(medTimeOfDay);
        holder.mTextBlock.setText(medName);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),TakeMedActivity.class);
                intent.putExtra("MED_ID", medId);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMedData.size();
    }

    public static class TakeMedsViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        public TextView mTextBlock;
        public ImageView mImageView;

        public TakeMedsViewHolder(View view){
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.iv_image);
            mCardView = (CardView) view.findViewById(R.id.card_view);
            mTextView = (TextView) view.findViewById(R.id.tv_text);
            mTextBlock = (TextView) view.findViewById(R.id.tv_block);

        }
    }
}
