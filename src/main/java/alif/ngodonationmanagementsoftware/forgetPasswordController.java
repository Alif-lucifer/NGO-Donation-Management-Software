package alif.ngodonationmanagementsoftware;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static alif.ngodonationmanagementsoftware.SceneSwitcher.switchTo;

public class forgetPasswordController {

    @FXML
    private ComboBox<String> companyCB;

    @FXML
    private TextField emailField;

    private static final String FILE_NAME = "forget_password_requests.bin";
    private static final String COMPANIES_FILE = "companies.bin";

    // NGO Admin
    private static final String ADMIN_NAME = "NGO Admin";
    private static final String ADMIN_EMAIL = "admin@gmail.org";

    @FXML
    public void initialize() {
        // -------- Add NGO Admin --------
        companyCB.getItems().add(ADMIN_NAME);

        // -------- Hardcoded demo companies --------
        ArrayList<String> demoCompanies = new ArrayList<>();
        demoCompanies.add("Bashundhara Group");
        demoCompanies.add("BEXIMCO Group");
        demoCompanies.add("Abul Khair Group");
        demoCompanies.add("Navana Group");
        demoCompanies.add("Ananda Group");
        demoCompanies.add("City Group");
        demoCompanies.add("Square Group");
        demoCompanies.add("Akij Group");
        demoCompanies.add("PRAN-RFL Group");
        demoCompanies.add("Grameenphone");

        for (String c : demoCompanies) {
            if (!companyCB.getItems().contains(c)) {
                companyCB.getItems().add(c);
            }
        }

        // -------- Load newly registered companies from file --------
        ArrayList<NewCompany> registeredCompanies = readCompaniesFromFile();
        for (NewCompany c : registeredCompanies) {
            if (!companyCB.getItems().contains(c.getName())) {
                companyCB.getItems().add(c.getName());
            }
        }

        // -------- Pre-select company if there is a pending request --------
        ArrayList<forgetPassword> requests = readRequests();
        if (!requests.isEmpty()) {
            forgetPassword lastRequest = requests.get(requests.size() - 1);
            companyCB.setValue(lastRequest.getCompanyName());
            emailField.setText(lastRequest.getEmail());
        }
    }

    @FXML
    public void openPasswordChangeScene(ActionEvent event) {
        String company = companyCB.getValue();
        String email = emailField.getText().trim();

        if (company == null || company.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Company and Email must be selected!");
            return;
        }

        // -------- ADMIN SPECIAL CHECK --------
        if (company.equalsIgnoreCase(ADMIN_NAME)) {
            if (!email.equalsIgnoreCase(ADMIN_EMAIL)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Admin email is incorrect!");
                return;
            }

            PasswordChangeController.selectedCompanyName = ADMIN_NAME;
            try {
                switchTo("/alif/ngodonationmanagementsoftware/PasswordChange.fxml", event);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Cannot open Password Change scene!");
            }
            return;
        }

        // -------- Combine demo companies + registered companies --------
        ArrayList<NewCompany> allCompanies = new ArrayList<>();
        allCompanies.add(new NewCompany("Bashundhara Group", "bashundhara@gmail.com", "", "N/A", "0123456789", "Textile"));
        allCompanies.add(new NewCompany("BEXIMCO Group", "beximco@gmail.com", "", "N/A", "0123456788", "Pharma"));
        allCompanies.add(new NewCompany("Abul Khair Group", "abulkhair@gmail.com", "", "N/A", "0123456787", "Food"));
        allCompanies.add(new NewCompany("Navana Group", "navana@gmail.com", "", "N/A", "0123456786", "Automobile"));
        allCompanies.add(new NewCompany("Ananda Group", "ananda@gmail.com", "", "N/A", "0123456785", "Food"));
        allCompanies.add(new NewCompany("City Group", "city@gmail.com", "", "N/A", "0123456784", "Conglomerate"));
        allCompanies.add(new NewCompany("Square Group", "square@gmail.com", "", "N/A", "0123456783", "Pharma"));
        allCompanies.add(new NewCompany("Akij Group", "akij@gmail.com", "", "N/A", "0123456782", "Textile"));
        allCompanies.add(new NewCompany("PRAN-RFL Group", "pran@gmail.com", "", "N/A", "0123456781", "Food"));
        allCompanies.add(new NewCompany("Grameenphone", "gp@gmail.com", "", "N/A", "0123456780", "Telecom"));

        allCompanies.addAll(readCompaniesFromFile());

        // -------- Find matching company by name and email --------
        NewCompany matchedCompany = allCompanies.stream()
                .filter(c -> c.getName().equalsIgnoreCase(company) && c.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);

        if (matchedCompany == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Company and email do not match our records!");
            return;
        }

        try {
            // Pass selected company to PasswordChangeController
            PasswordChangeController.selectedCompanyName = matchedCompany.getName();

            switchTo("/alif/ngodonationmanagementsoftware/PasswordChange.fxml", event);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot open Password Change scene!");
        }
    }

    @FXML
    public void backToLogin(ActionEvent event) throws IOException {
        switchTo("/alif/ngodonationmanagementsoftware/login.fxml", event);
    }

    // ------------------- FILE HANDLING -------------------
    private ArrayList<forgetPassword> readRequests() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<forgetPassword>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
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

    // ------------------- VALIDATION -------------------
    private boolean isValidEmail(String email) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, email);
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
