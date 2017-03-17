package edu.sjsu.cmpe277.cmpe277lab2;

import java.lang.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    private static final String MY_TAG = "CALCULATION FRAGMENT";
    private MortgageDbHelper mDbHelper;
    private SQLiteDatabase db;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner property_spinner;
    private Spinner state_spinner;
    private Spinner terms_spinner;
    private EditText address;
    private EditText city;
    private EditText zipcode;
    private EditText loan_amount;
    private EditText down_payment;
    private EditText annual_percentage;

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

        mDbHelper = new MortgageDbHelper(getContext());
        // Gets the data repository in write mode
        db = mDbHelper.getWritableDatabase();
        mDbHelper.onUpgrade(db, 1, 2);
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_calculation, container, false);

        String [] property_choice = {"House","Condominium","Townhouse"};
        property_spinner = (Spinner) v.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, property_choice);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        property_spinner.setAdapter(adapter1);

        String [] state_choice = {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};
        state_spinner = (Spinner) v.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, state_choice);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        state_spinner.setAdapter(adapter2);

        String [] terms_choice = {"15","30"};
        terms_spinner = (Spinner) v.findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, terms_choice);
        adapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        terms_spinner.setAdapter(adapter3);

        address = (EditText) v.findViewById(R.id.editAddress);
        city = (EditText) v.findViewById(R.id.editCity);
        zipcode = (EditText) v.findViewById(R.id.editZipCode);
        loan_amount = (EditText) v.findViewById(R.id.editAmount);
        down_payment = (EditText) v.findViewById(R.id.editDownpayment);
        annual_percentage = (EditText) v.findViewById(R.id.editAPR);

        //reset button
        Button button1 = (Button) v.findViewById(R.id.button_reset);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //clear all the field
            }
        });

        //calculate button
        Button button2 = (Button) v.findViewById(R.id.button_next);
        button2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                // save all the data and call fragment LoanInfo
            }
        });

        //save and calculate
        Button button3 = (Button) v.findViewById(R.id.button_save);
        button3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // save all the data into DB
                saveDataToDB();
            }
        });

        return v;
    }

    private void saveDataToDB() {
        Log.i(MY_TAG, property_spinner.getSelectedItem().toString());
        Log.i(MY_TAG, state_spinner.getSelectedItem().toString());
        Log.i(MY_TAG, terms_spinner.getSelectedItem().toString());
        Log.i(MY_TAG, address.getText().toString());
        Log.i(MY_TAG, city.getText().toString());
        Log.i(MY_TAG, zipcode.getText().toString());
        Log.i(MY_TAG, loan_amount.getText().toString());
        Log.i(MY_TAG, down_payment.getText().toString());
        Log.i(MY_TAG, annual_percentage.getText().toString());

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MortgageDbHelper.MortgageEntry.COLUMN_NAME_PROPERTY, property_spinner.getSelectedItem().toString());
        values.put(MortgageDbHelper.MortgageEntry.COLUMN_NAME_STATE, state_spinner.getSelectedItem().toString());
        values.put(MortgageDbHelper.MortgageEntry.COLUMN_NAME_TERMS, terms_spinner.getSelectedItem().toString());
        values.put(MortgageDbHelper.MortgageEntry.COLUMN_NAME_ADDRESS, address.getText().toString());
        values.put(MortgageDbHelper.MortgageEntry.COLUMN_NAME_CITY, city.getText().toString());
        values.put(MortgageDbHelper.MortgageEntry.COLUMN_NAME_ZIPCODE, zipcode.getText().toString());
        values.put(MortgageDbHelper.MortgageEntry.COLUMN_NAME_LOAN_AMOUNT, loan_amount.getText().toString());
        values.put(MortgageDbHelper.MortgageEntry.COLUMN_NAME_DOWN_PAYMENT, down_payment.getText().toString());
        values.put(MortgageDbHelper.MortgageEntry.COLUMN_NAME_ANNUAL_PERCENTAGE_RATE, annual_percentage.getText().toString());

        int mortgageCalculation = calculateTheMortgage();
        Log.i(MY_TAG, "calculation result: " + mortgageCalculation);
        values.put(MortgageDbHelper.MortgageEntry.COLUMN_NAME_CALCULATION, mortgageCalculation + "");

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(MortgageDbHelper.MortgageEntry.TABLE_NAME, null, values);
        Log.i(MY_TAG, "new row inserted to db: " + newRowId);
    }

    private int calculateTheMortgage() {
        //TODO: caculate the mortgage
        int x = Integer.parseInt(loan_amount.getText().toString());
        int y = Integer.parseInt(down_payment.getText().toString());

        return x - y;
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
