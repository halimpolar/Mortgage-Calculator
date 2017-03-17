package edu.sjsu.cmpe277.cmpe277lab2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_calculation));

        initTestData();
    }

    private void initTestData() {
        PropertyInfo test = new PropertyInfo();
        test.setType("House");
        test.setAddress("1 Washington Sq");
        test.setCity("San Jose");
        test.setState("CA");
        test.setZipcode(95192);
        MortgagePayment temp = new MortgagePayment();
        temp.setPropertyInfo(test);
        temp.setLoanInfo(new LoanInfo(2000000, 3330.33, 0.2, 15));

        PropertyInfo test2 = new PropertyInfo();
        test2.setType("Apartment");
        test2.setAddress("383 Lafayette Street");
        test2.setCity("New York");
        test2.setState("NY");
        test2.setZipcode(10003);
        MortgagePayment temp2 = new MortgagePayment();
        temp2.setPropertyInfo(test2);
        temp2.setLoanInfo(new LoanInfo(5000000, 4000, 0.52, 30));

        mortgagePayments = new HashMap<String, MortgagePayment>();
        mortgagePayments.put(temp.getKey(), temp);
        mortgagePayments.put(temp2.getKey(), temp2);
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

        Iterator it = mortgagePayments.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, MortgagePayment> pair = (Map.Entry)it.next();

            LatLng marker = getLatLngFromAddress(pair.getValue().getPropertyInfo());

            Marker tempMarker = googleMap.addMarker(new MarkerOptions().position(marker));
            tempMarker.setTag(pair.getKey());
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker));

            googleMap.setOnMarkerClickListener(this);
        }
    }

    private void switchToEditView(String tag) {

    }

    private void showDeleteConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_confirm_msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
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
                        showDeleteConfirmation();
                        dialog.dismiss();
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
        displayList[1] = "Loan Amount: " + payment.getLoanInfo().getLoanAmount();
        displayList[2] = "APR: " + payment.getLoanInfo().getApr();
        displayList[3] = "Monthly Payment: " + payment.getMonthlyPayment();

        return displayList;
    }
}
