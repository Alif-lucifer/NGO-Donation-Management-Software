package alif.ngodonationmanagementsoftware;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static alif.ngodonationmanagementsoftware.SceneSwitcher.switchTo;

public class DonateMoneyController {

    @FXML private Label companyNameLabel;
    @FXML private TextField amountField;
    @FXML private ComboBox<String> paymentMethodCB;

    private DonateMoney lastDonation;

    // ---------------- INITIALIZE ----------------
    @FXML
    public void initialize() {

        // show logged-in company
        companyNameLabel.setText(LoginController.loggedInCompany);

        paymentMethodCB.setItems(FXCollections.observableArrayList(
                "Cash",
                "Bank Transfer",
                "Mobile Payment"
        ));
    }

    // ---------------- DONATE ----------------
    @FXML
    public void donateButtonClicked(ActionEvent event) {

        String company = LoginController.loggedInCompany;
        String amountText = amountField.getText();
        String paymentMethod = paymentMethodCB.getValue();

        if (company == null || company.isEmpty()) {
            showAlert("Error", "No company logged in!");
            return;
        }

        if (amountText.isEmpty() || paymentMethod == null) {
            showAlert("Error", "Please fill all fields!");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid donation amount!");
            return;
        }

        DonateMoney donation =
                new DonateMoney(company, amount, paymentMethod);

        lastDonation = donation;

        saveDonationToCompanyFile(donation);

        showAlert(
                "Thank You!",
                "Thank you for your donation!\n\n" +
                        "Company: " + company +
                        "\nAmount: " + amount
        );

        amountField.clear();
        paymentMethodCB.setValue(null);
    }

    // ---------------- SAVE PER COMPANY BIN ----------------
    private void saveDonationToCompanyFile(DonateMoney donation) {

        String fileName = donation.getCompanyName()
                .replaceAll("\\s+", "_") + ".bin";

        ArrayList<DonateMoney> list = new ArrayList<>();

        File file = new File(fileName);
        if (file.exists()) {
            try (ObjectInputStream ois =
                         new ObjectInputStream(new FileInputStream(file))) {
                list = (ArrayList<DonateMoney>) ois.readObject();
            } catch (Exception ignored) {}
        }

        list.add(donation);

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(list);
        } catch (IOException e) {
            showAlert("Error", "Failed to save donation!");
        }
    }

    // ---------------- DOWNLOAD RECEIPT ----------------
    @FXML
    public void downloadReceiptButtonClicked(ActionEvent event) {

        if (lastDonation == null) {
            showAlert("Error", "Please donate first!");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Donation Receipt");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        chooser.setInitialFileName(
                "Donation_Receipt_" +
                        new SimpleDateFormat("yyyyMMdd_HHmmss")
                                .format(new Date()) + ".pdf"
        );

        Stage stage =
                (Stage) ((Node) event.getSource()).getScene().getWindow();

        File file = chooser.showSaveDialog(stage);
        if (file == null) return;

        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(file));
            doc.open();

            doc.add(new Paragraph(
                    "NGO Donation Receipt",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)
            ));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Company: " +
                    lastDonation.getCompanyName()));
            doc.add(new Paragraph("Amount: " +
                    lastDonation.getAmount()));
            doc.add(new Paragraph("Payment Method: " +
                    lastDonation.getPaymentMethod()));
            doc.add(new Paragraph("Donation Time: " +
                    lastDonation.getDonationTime()));

            doc.close();

            showAlert("Success", "Receipt downloaded successfully!");

        } catch (Exception e) {
            showAlert("Error", "Failed to generate receipt!");
        }
    }

    // ---------------- BACK ----------------
    @FXML
    public void backToDashboard(ActionEvent event) throws IOException {
        switchTo(
                "/alif/ngodonationmanagementsoftware/CompanyDashboard.fxml",
                event
        );
    }

    // ---------------- ALERT ----------------
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

