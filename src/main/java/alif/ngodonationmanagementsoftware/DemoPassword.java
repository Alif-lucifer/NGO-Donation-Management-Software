package alif.ngodonationmanagementsoftware;

import java.io.Serializable;

public class DemoPassword implements Serializable {
    private static final long serialVersionUID = 1L;

    private String companyName;
    private String password;

    public DemoPassword(String companyName, String password) {
        this.companyName = companyName;
        this.password = password;
    }

    // Getters and Setters
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
