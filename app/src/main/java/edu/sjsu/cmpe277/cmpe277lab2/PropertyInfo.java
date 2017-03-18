package edu.sjsu.cmpe277.cmpe277lab2;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.Serializable;

/**
 * Created by Polar on 3/13/17.
 * Class to let the user choose the property information
 */

public class PropertyInfo implements Serializable{
    private String type;
    private String address;
    private String city;
    private String zipcode;
    private String state;

    public String getLocation() {
        return address + ", " + city + ", " + state + " " + zipcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
