package edu.sjsu.cmpe277.cmpe277lab2;

/**
 * Created by Polar on 3/13/17.
 * Class for the loan information
 */

public class LoanInfo {
    private double propertyPrice;
    private double downPayment;
    private double apr;

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

    private int terms;
}
