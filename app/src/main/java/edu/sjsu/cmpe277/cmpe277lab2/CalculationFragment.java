package edu.sjsu.cmpe277.cmpe277lab2;

import java.lang.*;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

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
    private static final String ARG_PARAM = "updateTag";

    private Spinner property_spinner;
    private Spinner state_spinner;
    private Spinner terms_spinner;
    private EditText address;
    private EditText city;
    private EditText zipcode;
    private EditText property_price;
    private EditText down_payment;
    private EditText annual_percentage;
    private TextView result;
    private String savedTag;

    private boolean isEditMode = false;

    private OnFragmentInteractionListener mListener;

    public CalculationFragment() {
        // Required empty public constructor
    }

    public static CalculationFragment newInstance(String tag) {
        CalculationFragment fragment = new CalculationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, tag);
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
        View v = inflater.inflate(R.layout.fragment_calculation, container, false);

        String [] property_choice = {"House","Condominium","Townhouse"};
        property_spinner = (Spinner) v.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, property_choice);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        property_spinner.setAdapter(adapter1);

        final String [] state_choice = {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};
        state_spinner = (Spinner) v.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, state_choice);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        state_spinner.setAdapter(adapter2);

        String [] terms_choice = {"15","30"};
        terms_spinner = (Spinner) v.findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, terms_choice);
        adapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        terms_spinner.setAdapter(adapter3);

        result = (TextView) v.findViewById(R.id.display_result);
        address = (EditText) v.findViewById(R.id.editAddress);
        city = (EditText) v.findViewById(R.id.editCity);
        zipcode = (EditText) v.findViewById(R.id.editZipCode);
        property_price = (EditText) v.findViewById(R.id.editAmount);
        down_payment = (EditText) v.findViewById(R.id.editDownpayment);
        annual_percentage = (EditText) v.findViewById(R.id.editAPR);

        if (getArguments() != null) {
            isEditMode = true;
            editMode(getArguments().getString(ARG_PARAM));
        }

        //reset button
        Button button1 = (Button) v.findViewById(R.id.button_reset);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //clear all the field
                state_spinner.setSelection(4);
                terms_spinner.setSelection(0);
                property_spinner.setSelection(0);
                address.setText("");
                city.setText("");
                zipcode.setText("");
                property_price.setText("");
                down_payment.setText("");
                annual_percentage.setText("");
            }
        });

        //calculate button
        Button button2 = (Button) v.findViewById(R.id.button_next);
        button2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (property_price.getText().toString().length() <= 0) {
                    // toast
                    Toast.makeText(getContext(), "Please enter the loan amount", Toast.LENGTH_LONG).show();
                }

                else if (down_payment.getText().toString().length() <= 0) {
                    // toast
                    Toast.makeText(getContext(), "Please enter the down payment", Toast.LENGTH_LONG).show();
                }

                else if (annual_percentage.getText().toString().length() <= 0) {
                    // toast
                    Toast.makeText(getContext(), "Please enter the annual percentage", Toast.LENGTH_LONG).show();
                }

                else {
                    result.setText("Your Monthly Mortgage Payment Is: $" + calculateTheMortgage());
                }
            }
        });

        //save and calculate
        Button button3 = (Button) v.findViewById(R.id.button_save);
        button3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Geocoder coder = new Geocoder(getActivity());
                List<Address> addresses = null;
                String addressString = address.getText().toString() + ", "
                        + city.getText().toString() + ", "
                        + state_spinner.getSelectedItem().toString() + " "
                        + zipcode.getText().toString();
                try {
                    addresses = coder.getFromLocationName(addressString, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (address.getText().toString().length() <= 0 || addresses == null || addresses.size() == 0) {
                    // toast
                    Toast.makeText(getContext(), "Please enter a valid address", Toast.LENGTH_LONG).show();
                }

                else if (city.getText().toString().length() <= 0) {
                    // toast
                    Toast.makeText(getContext(), "Please enter a valid city", Toast.LENGTH_LONG).show();
                }

                else if (zipcode.getText().toString().length() <= 0 || zipcode.getText().toString().length() > 5) {
                    // toast
                    Toast.makeText(getContext(), "Please enter a valid zipcode", Toast.LENGTH_LONG).show();
                }

                else if (property_price.getText().toString().length() <= 0) {
                    // toast
                    Toast.makeText(getContext(), "Please enter the loan amount", Toast.LENGTH_LONG).show();
                }

                else if (down_payment.getText().toString().length() <= 0) {
                    // toast
                    Toast.makeText(getContext(), "Please enter the down payment", Toast.LENGTH_LONG).show();
                }

                else if (annual_percentage.getText().toString().length() <= 0) {
                    // toast
                    Toast.makeText(getContext(), "Please enter the annual percentage", Toast.LENGTH_LONG).show();
                }

                else {
                    // save all the data into DB
                    result.setText("Your Monthly Mortgage Payment Is: $" + calculateTheMortgage());
                    saveDataToDB();
                }

            }
        });

        return v;
    }

    public void editMode(String tag) {
        MortgagePayment payment = ((MainActivity)getActivity()).getMortgagePayment(tag);

        property_spinner.setSelection(getValueIndex(payment.getPropertyInfo().getType(), property_spinner));
        address.setText(payment.getPropertyInfo().getAddress());
        city.setText(payment.getPropertyInfo().getCity());
        state_spinner.setSelection(getValueIndex(payment.getPropertyInfo().getState(), state_spinner));
        zipcode.setText(payment.getPropertyInfo().getZipcode());
        property_price.setText(Double.toString(payment.getLoanInfo().getPropertyPrice()));
        down_payment.setText(Double.toString(payment.getLoanInfo().getDownPayment()));
        annual_percentage.setText(Double.toString(payment.getLoanInfo().getApr()));
        terms_spinner.setSelection(getValueIndex(Integer.toString(payment.getLoanInfo().getTerms()), terms_spinner));
        savedTag = tag;
    }

    private int getValueIndex(String value, Spinner spinner) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (value.equals(spinner.getItemAtPosition(i).toString())) {
                return i;
            }
        }
        return 0;
    }

    private void saveDataToDB() {
        PropertyInfo propInfo = new PropertyInfo();
        propInfo.setType(property_spinner.getSelectedItem().toString());
        propInfo.setAddress(address.getText().toString());
        propInfo.setCity(city.getText().toString());
        propInfo.setState(state_spinner.getSelectedItem().toString());
        propInfo.setZipcode(zipcode.getText().toString());
        LoanInfo loanInfo = new LoanInfo(Double.parseDouble(property_price.getText().toString()),
                Double.parseDouble(down_payment.getText().toString()),
                Double.parseDouble(annual_percentage.getText().toString()),
                Integer.parseInt(terms_spinner.getSelectedItem().toString()));
        MortgagePayment temp = new MortgagePayment();
        temp.setPropertyInfo(propInfo);
        temp.setLoanInfo(loanInfo);

        temp.setMonthlyPayment(calculateTheMortgage());
        if (isEditMode) {
            temp.setKey(savedTag);
            ((MainActivity)getActivity()).updateEdited(temp);
            Toast.makeText(getContext(), "Payment updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            ((MainActivity)getActivity()).savePayment(temp);
            Toast.makeText(getContext(), "Payment saved successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    private double calculateTheMortgage() {
        double principal = Float.parseFloat(property_price.getText().toString()) -
                Float.parseFloat(down_payment.getText().toString());
        double yearly_interest = Float.parseFloat(annual_percentage.getText().toString());
        double monthly_interest = yearly_interest/1200;
        double num_months = Float.parseFloat(terms_spinner.getSelectedItem().toString());
        double constant = Math.pow((1 + monthly_interest),(num_months * 12));
        double monthly_payment = ((principal * monthly_interest * constant) / (constant - 1));
        return Math.round(monthly_payment);
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
        void onFragmentInteraction(Uri uri);
    }
}
