package alif.ngodonationmanagementsoftware;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import com.itextpdf.text.pdf.PdfWriter;
import javafx.stage.Stage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


import static alif.ngodonationmanagementsoftware.SceneSwitcher.switchTo;

public class CheckTotalDonationController {

    @FXML private TableView<CheckTotalDonation> tableView;
    @FXML private TableColumn<CheckTotalDonation, String> nameColumn;
    @FXML private TableColumn<CheckTotalDonation, String> donationTimeColumn;
    @FXML private TableColumn<CheckTotalDonation, String> paymentMethodColumn;
    @FXML private TableColumn<CheckTotalDonation, Double> donationAmountColumn;
    @FXML private Button downloadButton;

    private static final String COMPANY_FILE = "companies.bin";

    private final ObservableList<CheckTotalDonation> donationList =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        donationTimeColumn.setCellValueFactory(new PropertyValueFactory<>("donationTime"));
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        donationAmountColumn.setCellValueFactory(new PropertyValueFactory<>("donationAmount"));
    }

    @FXML
    public void showButtonClicked(ActionEvent event) {
        donationList.clear();

        for (NewCompany c : getAllCompanies()) {
            donationList.add(new CheckTotalDonation(
                    c.getName(),
                    c.getDonationTime(),
                    c.getPaymentMethod(),
                    c.getDonationAmount()
            ));
        }

        tableView.setItems(donationList);
    }

    @FXML
    public void totalButtonClicked(ActionEvent event) {
        double total = donationList.stream()
                .mapToDouble(CheckTotalDonation::getDonationAmount)
                .sum();

        showAlert("Total Donation", "Total amount: " + total);
    }

    @FXML
    public void downloadButtonClicked(ActionEvent event) {

        if (donationList == null || donationList.isEmpty()) {
            showAlert("No Data", "Nothing to download");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Donation Report");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        chooser.setInitialFileName(
                "Donation_Report_" +
                        new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) +
                        ".pdf"
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = chooser.showSaveDialog(stage);
        if (file == null) return;

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // -------- TITLE --------
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph("NGO Donation Audit Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Generated on: " + new Date()));
            document.add(new Paragraph(" "));

            // -------- TABLE --------
            PdfPTable table = new PdfPTable(4); // 4 columns
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3, 3, 3, 2});

            // -------- HEADER --------
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            addHeaderCell(table, "Company Name", headerFont);
            addHeaderCell(table, "Donation Date", headerFont);
            addHeaderCell(table, "Payment Method", headerFont);
            addHeaderCell(table, "Amount", headerFont);

            // -------- DATA ROWS --------
            double total = 0;

            for (CheckTotalDonation d : donationList) {
                table.addCell(d.getCompanyName());
                table.addCell(d.getDonationTime());
                table.addCell(d.getPaymentMethod());
                table.addCell(String.valueOf(d.getDonationAmount()));
                total += d.getDonationAmount();
            }

            document.add(table);

            // -------- TOTAL --------
            document.add(new Paragraph(" "));
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            document.add(new Paragraph("Total Donation: " + total, totalFont));

            document.close();
            showAlert("Success", "PDF saved successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "PDF generation failed");
        }
    }



    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(8);
        table.addCell(cell);
    }



    @FXML
    public void backToDashboard(ActionEvent event) throws IOException {
        switchTo("/alif/ngodonationmanagementsoftware/Dashboard.fxml", event);
    }

    // -------- DATA SOURCE --------
    private ArrayList<NewCompany> getAllCompanies() {

        ArrayList<NewCompany> list = new ArrayList<>();

        // Demo companies (PAST dates only)
        list.add(new NewCompany("Bashundhara Group", 0, "N/A", "N/A"));
        list.add(new NewCompany("BEXIMCO Group", 0, "N/A", "N/A"));
        list.add(new NewCompany("Abul Khair Group", 0, "N/A", "N/A"));
        list.add(new NewCompany("Navana Group", 0, "N/A", "N/A"));
        list.add(new NewCompany("Ananda Group", 0, "N/A", "N/A"));
        list.add(new NewCompany("City Group", 0, "N/A", "N/A"));
        list.add(new NewCompany("Square Group", 0, "N/A", "N/A"));
        list.add(new NewCompany("Akij Group", 0, "N/A", "N/A"));
        list.add(new NewCompany("PRAN-RFL Group", 0, "N/A", "N/A"));
        list.add(new NewCompany("Grameenphone", 0, "N/A", "N/A"));


        // File companies
        list.addAll(readCompaniesFromFile());

        return list;
    }

    private ArrayList<NewCompany> readCompaniesFromFile() {
        File file = new File(COMPANY_FILE);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {

            return (ArrayList<NewCompany>) ois.readObject();

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
