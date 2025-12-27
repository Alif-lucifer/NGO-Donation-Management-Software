package alif.ngodonationmanagementsoftware;

import java.io.Serializable;

public class NonDonatingCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    private String companyName;
    private String email;
    private String phone;
    private String type;

    public NonDonatingCompany(String companyName, String email, String phone, String type) {
        this.companyName = companyName;
        this.email = email;
        this.phone = phone;
        this.type = type;
    }

    // -------- GETTERS --------
    public String getCompanyName() { return companyName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getType() { return type; }

    // -------- SETTERS --------
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return "NonDonatingCompany{" +
                "companyName='" + companyName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

