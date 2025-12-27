package alif.ngodonationmanagementsoftware;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.io.IOException;

import static alif.ngodonationmanagementsoftware.SceneSwitcher.switchTo;

public class CompanyDashboardController {

    @FXML
    public void initialize() throws IOException {
    }

    @FXML
    public void donateButton(ActionEvent actionEvent) throws IOException {
        switchTo(
                "/alif/ngodonationmanagementsoftware/CollectDonation.fxml",
                actionEvent
        );
    }

    @FXML
    public void viewDonationHistory(ActionEvent actionEvent) throws IOException {
        switchTo(
                "/alif/ngodonationmanagementsoftware/DonationHistory.fxml",
                actionEvent
        );
    }

    @FXML
    public void companyProfile(ActionEvent actionEvent) throws IOException {
        switchTo(
                "/alif/ngodonationmanagementsoftware/CompanyDetails.fxml",
                actionEvent
        );
    }

    @FXML
    public void logout(ActionEvent actionEvent) throws IOException {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Thank you for supporting our NGO!");
        alert.showAndWait();

        LoginController.loggedInCompany = null;

        switchTo(
                "/alif/ngodonationmanagementsoftware/Login.fxml",
                actionEvent
        );
    }
}
