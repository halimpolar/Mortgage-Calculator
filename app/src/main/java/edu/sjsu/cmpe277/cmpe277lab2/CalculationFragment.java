package edu.sjsu.cmpe277.cmpe277lab2;

import java.lang.*;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.app.Activity;
import android.widget.Button;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalculationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalculationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalculationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CalculationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalculationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalculationFragment newInstance(String param1, String param2) {
        CalculationFragment fragment = new CalculationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (getArguments() != null) {
        //    mParam1 = getArguments().getString(ARG_PARAM1);
        //    mParam2 = getArguments().getString(ARG_PARAM2);
        //}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_calculation, container, false);

        Button button1 = (Button) getView().findViewById(R.id.button_skip);
        button1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                //call fragment Loan Info
            }
        });

        String [] property_choice = {"House","Condominium","Townhouse"};
        Spinner s1 = (Spinner) v.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, property_choice);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s1.setAdapter(adapter1);

        String [] state_choice = {"Alabama", "California", "Alaska"};
        Spinner s2 = (Spinner) v.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, state_choice);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s2.setAdapter(adapter2);

        EditText address = (EditText) getView().findViewById(R.id.editAddress);
        EditText city = (EditText) getView().findViewById(R.id.editCity);
        EditText zipcode = (EditText) getView().findViewById(R.id.editZipCode);

        Button button2 = (Button) getView().findViewById(R.id.button_next);
        button2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // save all the data and call fragment LoanInfo
            }
        });
        return v;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

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
