package alif.ngodonationmanagementsoftware;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import static alif.ngodonationmanagementsoftware.SceneSwitcher.switchTo;

public class DonationHistoryController {

    @FXML private TableView<DonationHistory> donationTable;
    @FXML private TableColumn<DonationHistory, String> companyCol;
    @FXML private TableColumn<DonationHistory, Double> amountCol;
    @FXML private TableColumn<DonationHistory, String> paymentCol;
    @FXML private TableColumn<DonationHistory, String> timeCol;

    private final ObservableList<DonationHistory> data =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        companyCol.setCellValueFactory(
                new PropertyValueFactory<>("companyName"));
        amountCol.setCellValueFactory(
                new PropertyValueFactory<>("amount"));
        paymentCol.setCellValueFactory(
                new PropertyValueFactory<>("paymentMethod"));
        timeCol.setCellValueFactory(
                new PropertyValueFactory<>("donationTime"));

        donationTable.setItems(data);
    }

    // ---------------- SHOW HISTORY ----------------
    @FXML
    public void showButtonClicked(ActionEvent event) {

        data.clear();

        String company = LoginController.loggedInCompany;
        if (company == null) return;

        String fileName = company.replaceAll("\\s+", "_") + ".bin";
        File file = new File(fileName);

        if (!file.exists()) return;

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {

            ArrayList<DonateMoney> list =
                    (ArrayList<DonateMoney>) ois.readObject();

            for (DonateMoney d : list) {
                data.add(new DonationHistory(
                        d.getCompanyName(),
                        d.getAmount(),
                        d.getPaymentMethod(),
                        d.getDonationTime()
                ));
            }

        } catch (Exception ignored) {}
    }

    // ---------------- BACK ----------------
    @FXML
    public void backButtonClicked(ActionEvent event) throws Exception {
        switchTo(
                "/alif/ngodonationmanagementsoftware/CompanyDashboard.fxml",
                event
        );
    }
}
