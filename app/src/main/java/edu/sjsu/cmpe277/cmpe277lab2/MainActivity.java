package edu.sjsu.cmpe277.cmpe277lab2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CalculationFragment.OnFragmentInteractionListener, OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private DrawerLayout drawer;
    private AlertDialog dialog;
    private HashMap<String, MortgagePayment> mortgagePayments;
    private NavigationView navigationView;

    private DecimalFormat format = new DecimalFormat("#.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Fragment fragment = new CalculationFragment();
        setTitle(R.string.calculation_view);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
        getAllPayments();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment;
        if (item.getItemId() == R.id.nav_map) {
            fragment = new SupportMapFragment();
            setTitle(R.string.map_view);
            ((SupportMapFragment)fragment).getMapAsync(this);
        } else {
            fragment = new CalculationFragment();
            setTitle(R.string.calculation_view);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        getAllPayments();

        Iterator it = mortgagePayments.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, MortgagePayment> pair = (Map.Entry)it.next();

            LatLng marker = getLatLngFromAddress(pair.getValue().getPropertyInfo());

            Marker tempMarker = googleMap.addMarker(new MarkerOptions().position(marker));
            tempMarker.setTag(pair.getKey());
            googleMap.setOnMarkerClickListener(this);
        }
    }

    private void switchToEditView(String tag) {
        Fragment fragment = CalculationFragment.newInstance(tag);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    public void updateEdited(MortgagePayment updatedPayment) {
        mortgagePayments.remove(updatedPayment.getKey());
        mortgagePayments.put(updatedPayment.getKey(), updatedPayment);
        updateMortgagePayments();
    }

    private void showDeleteConfirmation(final String tag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_confirm_msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mortgagePayments.remove(tag);
                        updateMortgagePayments();
                        Fragment fragment = new SupportMapFragment();
                        ((SupportMapFragment)fragment).getMapAsync(MainActivity.this);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog temp = builder.create();
        temp.show();
    }

    private LatLng getLatLngFromAddress(PropertyInfo property) {
        LatLng location = null;
        Geocoder coder = new Geocoder(this);

        try {
            List<Address> addresses = coder.getFromLocationName(property.getLocation(), 1);
            if (addresses.size() > 0) {
                location = new LatLng(addresses.get(0).getLatitude()
                        , addresses.get(0).getLongitude());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        Log.d("starng", (String)marker.getTag());
        builder.setTitle(mortgagePayments.get(marker.getTag()).getPropertyInfo().getLocation())
                .setItems(getDisplayList(marker.getTag()), null)
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switchToEditView((String)marker.getTag());
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        showDeleteConfirmation((String)marker.getTag());
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog = builder.create();
        dialog.show();

        return true;
    }

    private CharSequence[] getDisplayList(Object tag) {
        MortgagePayment payment = mortgagePayments.get(tag);
        CharSequence[] displayList = new CharSequence[4];
        displayList[0] = "Property Type: " + payment.getPropertyInfo().getType();
        displayList[1] = "Loan Amount: $" + format.format(payment.getLoanInfo().getLoanAmount());
        displayList[2] = "APR: " + payment.getLoanInfo().getApr() + "%";
        displayList[3] = "Monthly Payment: $" + format.format(payment.getMonthlyPayment());

        return displayList;
    }

    public class Wrapper {
        private HashMap<String, MortgagePayment> map;

        public HashMap<String, MortgagePayment> getMap() {
            return map;
        }

        public void setMap(HashMap<String, MortgagePayment> map) {
            this.map = map;
        }
    }

    public void updateMortgagePayments() {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(mortgagePayments);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_payments), jsonStr);
        editor.commit();
    }

    public void savePayment(MortgagePayment newPayment) {
        mortgagePayments.put(newPayment.getKey(), newPayment);
        updateMortgagePayments();
    }

    public void getAllPayments() {
        Type type = new TypeToken<HashMap<String, MortgagePayment>>(){}.getType();
        Gson gson = new Gson();
        String jsonStr = getPreferences(Context.MODE_PRIVATE).getString(getString(R.string.saved_payments), "");
        if (jsonStr == "") {
            mortgagePayments = new HashMap<String, MortgagePayment>();
        } else {
            mortgagePayments = gson.fromJson(jsonStr, type);
        }
    }

    public MortgagePayment getMortgagePayment(String tag) {
        if (mortgagePayments.containsKey(tag)) {
            return mortgagePayments.get(tag);
        }
        return null;
    }
}
