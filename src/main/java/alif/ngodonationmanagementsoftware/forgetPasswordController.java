package alif.ngodonationmanagementsoftware;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    }

    @FXML
    public void sendRequest(ActionEvent event) {
        String company = companyCB.getValue();
        String email = emailField.getText().trim();

        if (company == null || company.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required!");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid email format!");
            return;
        }

        forgetPassword request = new forgetPassword(company, email);

        ArrayList<forgetPassword> list = readRequests();
        list.add(request);
        writeRequests(list);

        showAlert(Alert.AlertType.INFORMATION,
                "Success",
                "Password reset request sent successfully!");

        companyCB.setValue(null);
        emailField.clear();
    }

    @FXML
    public void backToLogin(ActionEvent event) throws IOException {
        switchTo("/alif/ngodonationmanagementsoftware/Login.fxml", event);
    }

    // ------------------- FILE HANDLING -------------------
    private void writeRequests(ArrayList<forgetPassword> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save request!");
        }
    }

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

