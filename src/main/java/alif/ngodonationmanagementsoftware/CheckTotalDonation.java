package alif.ngodonationmanagementsoftware;

public class CheckTotalDonation {

    private String companyName;
    private String donationTime;
    private String paymentMethod;
    private double donationAmount;

    public CheckTotalDonation(String companyName,
                              String donationTime,
                              String paymentMethod,
                              double donationAmount) {
        this.companyName = companyName;
        this.donationTime = donationTime;
        this.paymentMethod = paymentMethod;
        this.donationAmount = donationAmount;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDonationTime() {
        return donationTime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public double getDonationAmount() {
        return donationAmount;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setDonationTime(String donationTime) {
        this.donationTime = donationTime;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setDonationAmount(double donationAmount) {
        this.donationAmount = donationAmount;
    }

    @Override
    public String toString() {
        return "CheckTotalDonation{" +
                "companyName='" + companyName + '\'' +
                ", donationTime='" + donationTime + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", donationAmount=" + donationAmount +
                '}';
    }
}






