package alif.ngodonationmanagementsoftware;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DonateMoney implements Serializable {

    private static final long serialVersionUID = 1L;

    private String companyName;
    private double amount;
    private String paymentMethod;
    private String donationTime;

    public DonateMoney(String companyName, double amount, String paymentMethod) {
        this.companyName = companyName;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.donationTime =
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                        .format(new Date());
    }

    // -------- GETTERS --------
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

    @Override
    public String toString() {
        return "DonateMoney{" +
                "companyName='" + companyName + '\'' +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", donationTime='" + donationTime + '\'' +
                '}';
    }
}

