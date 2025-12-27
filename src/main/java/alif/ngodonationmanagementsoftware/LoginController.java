package alif.ngodonationmanagementsoftware;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.ArrayList;

import static alif.ngodonationmanagementsoftware.SceneSwitcher.switchTo;

public class LoginController {

    @javafx.fxml.FXML
    private ComboBox<String> userTypeCB;

    @javafx.fxml.FXML
    private TextField emailField;

    @javafx.fxml.FXML
    private PasswordField passwordField;

    // store logged-in user globally
    public static String loggedInCompany;

    // company login list
    private final ArrayList<Login> companies = new ArrayList<>();

    private static final String FILE_NAME = "companies.bin";

    // -------- NGO ADMIN CREDENTIALS --------
    private static final String ADMIN_NAME = "NGO Admin";
    private static final String ADMIN_EMAIL = "admin@gmail.org";
    private static final String ADMIN_PASSWORD = "admin1234";

    @javafx.fxml.FXML
    public void initialize() {

        // -------- Add NGO Admin to ComboBox --------
        userTypeCB.getItems().add(ADMIN_NAME);

        // -------- Hardcoded demo companies --------
        companies.add(new Login("bashundhara@gmail.com", "1111", "Bashundhara Group"));
        companies.add(new Login("beximco@gmail.com", "2222", "BEXIMCO Group"));
        companies.add(new Login("abulkhair@gmail.com", "3333", "Abul Khair Group"));
        companies.add(new Login("navana@gmail.com", "4444", "Navana Group"));
        companies.add(new Login("ananda@gmail.com", "5555", "Ananda Group"));
        companies.add(new Login("city@gmail.com", "6666", "City Group"));
        companies.add(new Login("square@gmail.com", "7777", "Square Group"));
        companies.add(new Login("akij@gmail.com", "8888", "Akij Group"));
        companies.add(new Login("pran@gmail.com", "9999", "PRAN-RFL Group"));
        companies.add(new Login("gp@gmail.com", "0000", "Grameenphone"));

        // Populate ComboBox with demo companies
        for (Login c : companies) {
            if (!userTypeCB.getItems().contains(c.getCompanyName())) {
                userTypeCB.getItems().add(c.getCompanyName());
            }
        }

        // -------- Load newly registered companies --------
        ArrayList<NewCompany> registeredCompanies = readCompaniesFromFile();
        for (NewCompany c : registeredCompanies) {
            if (!userTypeCB.getItems().contains(c.getName())) {
                userTypeCB.getItems().add(c.getName());
                companies.add(new Login(c.getEmail(), c.getPassword(), c.getName()));
            }
        }
    }

    // ---------------- LOGIN BUTTON ----------------
    @javafx.fxml.FXML
    public void loginButton(ActionEvent actionEvent) throws IOException {

        String email = emailField.getText();
        String password = passwordField.getText();
        String selectedUser = userTypeCB.getValue();

        if (email.isEmpty() || password.isEmpty() || selectedUser == null) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Please fill all fields!");
            return;
        }

        // -------- NGO ADMIN LOGIN --------
        if (selectedUser.equals(ADMIN_NAME)) {

            if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {

                loggedInCompany = ADMIN_NAME;

                showAlert(Alert.AlertType.INFORMATION,
                        "Login Successful",
                        "Welcome to NGO");

                switchTo(
                        "/alif/ngodonationmanagementsoftware/Dashboard.fxml",
                        actionEvent
                );
                return;

            } else {
                showAlert(Alert.AlertType.ERROR,
                        "Access Denied",
                        "Invalid NGO Admin credentials!");
                return;
            }
        }

        // -------- COMPANY LOGIN (RESTRICTED) --------
        for (Login company : companies) {
            if (company.getEmail().equals(email)
                    && company.getPassword().equals(password)
                    && company.getCompanyName().equals(selectedUser)) {

                showAlert(Alert.AlertType.ERROR,
                        "Access Restricted",
                        "You cannot access this option.\nOnly NGO Admin can access.");
                return;
            }
        }

        showAlert(Alert.AlertType.ERROR,
                "Login Failed",
                "Invalid email, password, or company selection!");
    }

    // ---------------- REGISTER BUTTON ----------------
    @javafx.fxml.FXML
    public void registerHereButton(ActionEvent actionEvent) throws IOException {
        switchTo(
                "/alif/ngodonationmanagementsoftware/NewCompany.fxml",
                actionEvent
        );
    }

    // ---------------- FORGET PASSWORD ----------------
    @javafx.fxml.FXML
    public void forgetPasswordButton(ActionEvent actionEvent) throws IOException {
        switchTo(
                "/alif/ngodonationmanagementsoftware/forgetPassword.fxml",
                actionEvent
        );
    }

    // ---------------- FILE READ ----------------
    private ArrayList<NewCompany> readCompaniesFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<NewCompany>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ---------------- ALERT HELPER ----------------
    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}


