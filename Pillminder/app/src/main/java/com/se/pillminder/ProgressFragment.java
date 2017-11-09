package com.se.pillminder;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProgressFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_progress, container, true);

        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        return rootView;
    }
}


