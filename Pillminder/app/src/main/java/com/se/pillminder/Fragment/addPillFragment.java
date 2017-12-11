package com.se.pillminder.Fragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.se.pillminder.R;
import com.se.pillminder.model.MultiSelectionSpinner;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link addPillFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link addPillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addPillFragment extends Fragment {
    private String mPrevActivity;
    private Toolbar mToolbar;
    private EditText mMedName;
    private EditText mDosage;
    private EditText mAmount;
    private Spinner mUnitsSpinner;
    private Spinner mBeforeFoodSpinner;
    private MultiSelectionSpinner mFreqSpinner;

    private SQLiteDatabase mMedListDb;

    private OnFragmentInteractionListener mListener;

    public addPillFragment() {
        // Required empty public constructor
    }


    public static addPillFragment newInstance(String param1, String param2) {
        addPillFragment fragment = new addPillFragment();
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
        return inflater.inflate(R.layout.fragment_add_pill, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        mMedName = (EditText) view.findViewById(R.id.med_name);
        mDosage = (EditText) view.findViewById(R.id.dosage);
        mAmount = (EditText) view.findViewById(R.id.amount);

        String[] array = {"Morning", "Afternoon", "Evening", "Night"};
        mFreqSpinner = (MultiSelectionSpinner) view.findViewById(R.id.frequency_spinner);
        mFreqSpinner.setItems(array);
        mFreqSpinner.setSelection(new int[]{0});
        mUnitsSpinner = (Spinner) view.findViewById(R.id.units_spinner);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
