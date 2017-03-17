package edu.sjsu.cmpe277.cmpe277lab2;

import java.io.Serializable;

/**
 * Created by Polar on 3/13/17.
 * Class for the loan information
 */

public class LoanInfo implements Serializable{
    private double propertyPrice;
    private double downPayment;
    private double loanAmount;
    private double apr;
    private int terms;

    public LoanInfo(double propertyPrice, double downPayment, double apr, int terms) {
        this.propertyPrice = propertyPrice;
        this.downPayment = downPayment;
        this.loanAmount = propertyPrice - downPayment;
        this.apr = apr;
        this.terms = terms;
    }

    public int getTerms() {
        return terms;
    }

    public void setTerms(int terms) {
        this.terms = terms;
    }

    public double getPropertyPrice() {
        return propertyPrice;
    }

    public void setPropertyPrice(double propertyPrice) {
        this.propertyPrice = propertyPrice;
    }

    public double getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(double downPayment) {
        this.downPayment = downPayment;
    }

    public double getApr() {
        return apr;
    }

    public void setApr(double apr) {
        this.apr = apr;
    }

    public double getLoanAmount() {
        return loanAmount;
    }
}
