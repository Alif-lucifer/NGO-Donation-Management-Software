package alif.ngodonationmanagementsoftware;

import java.io.*;
import java.util.ArrayList;

public class PasswordChange {

    private static final String COMPANIES_FILE = "companies.bin";
    private static final String D_PASSWORD_FILE = "DPassword.bin";
    private static final String A_PASSWORD_FILE = "APassword.bin"; // Admin password file

    private static final String ADMIN_NAME = "NGO Admin";

    // Hardcoded demo companies
    private static final String[][] DEMO_COMPANIES = {
            {"Bashundhara Group", "bashundhara@gmail.com", "", "N/A", "0123456789", "Textile"},
            {"BEXIMCO Group", "beximco@gmail.com", "", "N/A", "0123456788", "Pharma"},
            {"Abul Khair Group", "abulkhair@gmail.com", "", "N/A", "0123456787", "Food"},
            {"Navana Group", "navana@gmail.com", "", "N/A", "0123456786", "Automobile"},
            {"Ananda Group", "ananda@gmail.com", "", "N/A", "0123456785", "Food"},
            {"City Group", "city@gmail.com", "", "N/A", "0123456784", "Conglomerate"},
            {"Square Group", "square@gmail.com", "", "N/A", "0123456783", "Pharma"},
            {"Akij Group", "akij@gmail.com", "", "N/A", "0123456782", "Textile"},
            {"PRAN-RFL Group", "pran@gmail.com", "", "N/A", "0123456781", "Food"},
            {"Grameenphone", "gp@gmail.com", "", "N/A", "0123456780", "Telecom"}
    };

    // ================= DEMO COMPANIES =================
    public boolean changeDemoPassword(String companyName, String newPassword) {
        ArrayList<DemoPassword> demoPasswords = readDemoPasswords();
        boolean found = false;
        for (DemoPassword dp : demoPasswords) {
            if (dp.getCompanyName().equalsIgnoreCase(companyName)) {
                dp.setPassword(newPassword);
                found = true;
                break;
            }
        }
        if (!found) {
            demoPasswords.add(new DemoPassword(companyName, newPassword));
        }
        return writeDemoPasswords(demoPasswords);
    }

    public ArrayList<DemoPassword> readDemoPasswords() {
        File file = new File(D_PASSWORD_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<DemoPassword>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private boolean writeDemoPasswords(ArrayList<DemoPassword> demoPasswords) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(D_PASSWORD_FILE))) {
            oos.writeObject(demoPasswords);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= ADMIN PASSWORD =================
    public boolean changeAdminPassword(String newPassword) {
        return writeAdminPassword(newPassword);
    }

    public String readAdminPassword() {
        File file = new File(A_PASSWORD_FILE);
        if (!file.exists()) return "admin1234"; // default admin password
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (String) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return "admin1234";
        }
    }

    private boolean writeAdminPassword(String password) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(A_PASSWORD_FILE))) {
            oos.writeObject(password);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= REAL COMPANIES =================
    public boolean changeRealCompanyPassword(String companyName, String newPassword) {
        ArrayList<NewCompany> companies = readCompaniesFromFile();
        for (NewCompany c : companies) {
            if (c.getName().equalsIgnoreCase(companyName)) {
                c.setPassword(newPassword);
                writeCompaniesToFile(companies);
                return true;
            }
        }
        return false;
    }

    private ArrayList<NewCompany> readCompaniesFromFile() {
        File file = new File(COMPANIES_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<NewCompany>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void writeCompaniesToFile(ArrayList<NewCompany> companies) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(COMPANIES_FILE))) {
            oos.writeObject(companies);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= UTILITY =================
    public boolean changePassword(String companyName, String newPassword) {
        if (companyName.equalsIgnoreCase(ADMIN_NAME)) {
            return changeAdminPassword(newPassword);
        } else if (isDemoCompany(companyName)) {
            return changeDemoPassword(companyName, newPassword);
        } else {
            return changeRealCompanyPassword(companyName, newPassword);
        }
    }

    private boolean isDemoCompany(String name) {
        for (String[] demo : DEMO_COMPANIES) {
            if (demo[0].equalsIgnoreCase(name)) return true;
        }
        return false;
    }
}
