package alif.ngodonationmanagementsoftware;

import java.io.Serializable;

public class forgetPassword implements Serializable {

    private String companyName;
    private String email;

    public forgetPassword(String companyName, String email) {
        this.companyName = companyName;
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "forgetPassword{" +
                "companyName='" + companyName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

