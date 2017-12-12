package com.example.pillbox.model;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pillbox.R;

/**
 * Created by luishengjie on 5/6/17.
 */

public class ReportFragment extends Fragment {
    // This method creates a new instance of the PillBoxFragment
    public static ReportFragment newInstance(){
        ReportFragment fragment = new ReportFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);
        ProgressBar _progressBar = (ProgressBar)rootView.findViewById (R.id.circularProgressBar);
        _progressBar.setProgress( 30 );
        ((TextView) rootView.findViewById(R.id.textView1)).setText("20%");


        return rootView;
    }
}


