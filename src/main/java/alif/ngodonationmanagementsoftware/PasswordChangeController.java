package alif.ngodonationmanagementsoftware;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

import static alif.ngodonationmanagementsoftware.SceneSwitcher.switchTo;

public class PasswordChangeController {

    @FXML private Label nameLabel;  // Shows company name automatically
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;

    public static String selectedCompanyName = null; // received from forgetPasswordController

    private final PasswordChange model = new PasswordChange();

    @FXML
    public void initialize() {
        if (selectedCompanyName != null) {
            nameLabel.setText(selectedCompanyName);
        } else {
            nameLabel.setText(""); // blank initially
        }
    }

    @FXML
    public void changePassword(ActionEvent event) {
        String company = nameLabel.getText();
        String newPass = newPasswordField.getText().trim();
        String confirmPass = confirmPasswordField.getText().trim();

        if (company.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Company is not selected!");
            return;
        }
        if (newPass.isEmpty() || confirmPass.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Password fields cannot be empty!");
            return;
        }
        if (!newPass.equals(confirmPass)) {
            showAlert(AlertType.ERROR, "Error", "Passwords do not match!");
            return;
        }

        boolean success = model.changePassword(company, newPass);
        if (success) {
            showAlert(AlertType.INFORMATION, "Success", "Password for \"" + company + "\" changed successfully!");
            newPasswordField.clear();
            confirmPasswordField.clear();
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to change password!");
        }
    }

    @FXML
    public void backToDashboard(ActionEvent event) {
        try {
            switchTo("/alif/ngodonationmanagementsoftware/login.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Cannot go back to dashboard!");
        }
    }

    private void showAlert(AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
