package alif.ngodonationmanagementsoftware;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static alif.ngodonationmanagementsoftware.SceneSwitcher.switchTo;

public class NewCompanyController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField; // new field
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField typeField;

    @FXML
    private Button submitButton;
    @FXML
    private Button backButton;

    private static final String FILE_NAME = "companies.bin";

    @FXML
    public void submitButton(ActionEvent event) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim(); // get confirm password
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();
        String type = typeField.getText().trim();

        // Validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                address.isEmpty() || phone.isEmpty() || type.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required!");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid email format!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match!");
            return;
        }
        if (password.length() != 4) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password must be exactly 4 characters!");
            return;
        }
        if (!phone.matches("\\d{11}")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Phone number must be exactly 11 digits!");
            return;
        }

        NewCompany newCompany = new NewCompany(name, email, password, address, phone, type);

        ArrayList<NewCompany> list = readCompanies();
        list.add(newCompany);
        writeCompanies(list);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Company registered successfully!");

        // Clear fields
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        addressField.clear();
        phoneField.clear();
        typeField.clear();
    }

    @FXML
    public void backButton(ActionEvent event) throws IOException {
        switchTo("/alif/ngodonationmanagementsoftware/Login.fxml", event);
    }

    private void writeCompanies(ArrayList<NewCompany> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save company!");
        }
    }

    private ArrayList<NewCompany> readCompanies() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<NewCompany>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

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
