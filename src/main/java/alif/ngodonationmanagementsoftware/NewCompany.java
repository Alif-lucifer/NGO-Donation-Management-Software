package alif.ngodonationmanagementsoftware;

import java.io.Serializable;

public class NewCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    // Company basic info
    private String name;
    private String email;
    private String password;
    private String address;
    private String phone;
    private String type;

    // Donation info
    private double donationAmount;
    private String donationTime;
    private String paymentMethod;

    // ---------------- CONSTRUCTOR (REGISTER NEW COMPANY) ----------------
    public NewCompany(String name, String email, String password,
                      String address, String phone, String type) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.type = type;

        this.donationAmount = 0.0;
        this.donationTime = "N/A";
        this.paymentMethod = "N/A";
    }

    // ---------------- CONSTRUCTOR (DONATION RECORD ONLY) ----------------
    public NewCompany(String name, double donationAmount, String donationTime, String paymentMethod) {
        this.name = name;
        this.email = "";
        this.password = "";
        this.address = "";
        this.phone = "";
        this.type = "";

        this.donationAmount = donationAmount;
        this.donationTime = donationTime;
        this.paymentMethod = paymentMethod;
    }

    // ---------------- CONSTRUCTOR (BASIC INFO ONLY) ----------------
    // For non-donating companies (name, email, phone, type)
    public NewCompany(String name, String email, String phone, String type) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;

        this.password = "";
        this.address = "";
        this.donationAmount = 0.0;
        this.donationTime = "N/A";
        this.paymentMethod = "N/A";
    }

    // ---------------- GETTERS ----------------
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getType() { return type; }
    public double getDonationAmount() { return donationAmount; }
    public String getDonationTime() { return donationTime; }
    public String getPaymentMethod() { return paymentMethod; }

    // ---------------- SETTERS ----------------
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; } // password update supported
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setType(String type) { this.type = type; }
    public void setDonationAmount(double donationAmount) { this.donationAmount = donationAmount; }
    public void setDonationTime(String donationTime) { this.donationTime = donationTime; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    // ---------------- TO STRING ----------------
    @Override
    public String toString() {
        return "NewCompany{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", type='" + type + '\'' +
                ", donationAmount=" + donationAmount +
                ", donationTime='" + donationTime + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}
