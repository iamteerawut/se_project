package com.example.pillbox.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pillbox.activity.MedInfoActivity;
import com.example.pillbox.Medication;
import com.example.pillbox.R;

import java.util.ArrayList;



public class DisplayMedsAdapter extends RecyclerView.Adapter<DisplayMedsAdapter.DisplayMedsViewHolder> {

    private ArrayList<Medication> mMedData;
    private Context mContext;


    public DisplayMedsAdapter(ArrayList<Medication> medData, Context context){
        mMedData = medData;
        mContext = context;
    }

    @Override
    public DisplayMedsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_display_meds, parent, false);
        DisplayMedsViewHolder displayMedsViewHolder = new DisplayMedsViewHolder(view);

        return displayMedsViewHolder;
    }

    @Override
    public void onBindViewHolder(final DisplayMedsViewHolder holder, int position) {
        final String medName = mMedData.get(position).getMedName();
        final String medId = mMedData.get(position).getMedId();
        final int medMorning = mMedData.get(position).getMorning();
        final int medAfternoon = mMedData.get(position).getAfternoon();
        final int medEvening = mMedData.get(position).getEvening();
        final int medNight = mMedData.get(position).getNight();
        StringBuilder medFrequency = new StringBuilder();

        if(medMorning == 1){
            medFrequency.append(" Morning");
        } else if(medAfternoon == 1){
            medFrequency.append(" Afternoon");
        } else if(medEvening == 1){
            medFrequency.append(" Evening");
        } else if (medNight == 1){
            medFrequency.append(" Night");
        }

        holder.mTextView.setText(medName);
        holder.mTextBlock.setText(medFrequency.toString());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MedInfoActivity.class);
                intent.putExtra("MED_ID", medId);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMedData.size();
    }

    public static class DisplayMedsViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        public TextView mTextBlock;

        public DisplayMedsViewHolder(View view){
            super(view);

            mCardView = (CardView) view.findViewById(R.id.card_view);
            mTextView = (TextView) view.findViewById(R.id.tv_text);
            mTextBlock = (TextView) view.findViewById(R.id.tv_block);

        }
    }
}
