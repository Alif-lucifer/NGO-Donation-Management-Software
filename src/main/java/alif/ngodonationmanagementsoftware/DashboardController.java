package alif.ngodonationmanagementsoftware;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.io.IOException;

import static alif.ngodonationmanagementsoftware.SceneSwitcher.switchTo;

public class DashboardController {

    @javafx.fxml.FXML
    public void logout(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Thank you for using the system!");
        alert.showAndWait();

        switchTo("/alif/ngodonationmanagementsoftware/login.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void checkDonation(ActionEvent actionEvent) throws IOException {
        switchTo("/alif/ngodonationmanagementsoftware/CheckTotalDonation.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void companyDetails(ActionEvent actionEvent) throws IOException {
        switchTo("/alif/ngodonationmanagementsoftware/CompanyDetails.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void backToLogin(ActionEvent actionEvent) throws IOException {
        switchTo("/alif/ngodonationmanagementsoftware/login.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void listNotDonated(ActionEvent actionEvent) throws IOException {
        switchTo("/alif/ngodonationmanagementsoftware/NonDonatingCompanies.fxml", actionEvent);
    }
}
