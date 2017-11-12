package com.se.pillminder;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 */
public class PillFragment extends Fragment {

    private View rootView;

    public PillFragment() {
        // Required empty public constructor
    }

    public static PillFragment newInstance() {
        PillFragment fragment = new PillFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.rootView = inflater.inflate(R.layout.fragment_pill, container, false);

//        Button edit_pill = rootView.findViewById(R.id.edit_pill1);
//        edit_pill.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog dialog = new Dialog(PillFragment.this.getContext());
//                dialog.setContentView(R.layout.dialog_editpill);
//
//                Button ok_action = (Button) dialog.findViewById(R.id.ok_action);
//                Button cancel_action = (Button) dialog.findViewById(R.id.cancel_action);
//
//                ok_action.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//
//                cancel_action.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//            }
//        });

        Button add_pill = rootView.findViewById(R.id.add_pill);
        add_pill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(PillFragment.this.getContext());
                dialog.setContentView(R.layout.dialog_addpill);

                Button ok_action = (Button) dialog.findViewById(R.id.ok_action);
                Button cancel_action = (Button) dialog.findViewById(R.id.cancel_action);

                ok_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                cancel_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        return rootView;
//        return inflater.inflate(R.layout.fragment_pill, container, false);
    }
}

