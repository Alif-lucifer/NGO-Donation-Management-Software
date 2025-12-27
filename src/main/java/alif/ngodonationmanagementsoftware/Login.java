package alif.ngodonationmanagementsoftware;

import java.io.Serializable;

public class Login implements Serializable {

    private String email;
    private String password;
    private String companyName;

    public Login(String email, String password, String companyName) {
        this.email = email;
        this.password = password;
        this.companyName = companyName;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getCompanyName() { return companyName; }

    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    @Override
    public String toString() {
        return "Login{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}





