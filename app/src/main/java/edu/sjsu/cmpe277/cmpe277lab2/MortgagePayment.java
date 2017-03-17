package edu.sjsu.cmpe277.cmpe277lab2;

import java.util.UUID;

/**
 * Created by starn on 3/16/2017.
 */

public class MortgagePayment {
    private PropertyInfo propertyInfo;
    private LoanInfo loanInfo;
    private String key;

    private double monthlyPayment;

    public MortgagePayment() {
        key = UUID.randomUUID().toString();
    }

    public String getKey() {
        return key;
    }

    public double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public void setPropertyInfo(PropertyInfo propertyInfo) {
        this.propertyInfo = propertyInfo;
    }

    public void setLoanInfo(LoanInfo loanInfo) {
        this.loanInfo = loanInfo;
    }

    public PropertyInfo getPropertyInfo() {
        return propertyInfo;
    }

    public LoanInfo getLoanInfo() {
        return loanInfo;
    }
}
