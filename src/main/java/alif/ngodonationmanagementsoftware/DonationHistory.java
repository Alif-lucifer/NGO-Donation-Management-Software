package alif.ngodonationmanagementsoftware;

import java.io.Serializable;

public class DonationHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    private String companyName;
    private double amount;
    private String paymentMethod;
    private String donationTime;

    public DonationHistory(String companyName,
                           double amount,
                           String paymentMethod,
                           String donationTime) {
        this.companyName = companyName;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.donationTime = donationTime;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getDonationTime() {
        return donationTime;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setDonationTime(String donationTime) {
        this.donationTime = donationTime;
    }

    @Override
    public String toString() {
        return "DonationHistory{" +
                "companyName='" + companyName + '\'' +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", donationTime='" + donationTime + '\'' +
                '}';
    }
}

