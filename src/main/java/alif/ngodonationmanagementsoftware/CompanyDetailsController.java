package alif.ngodonationmanagementsoftware;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static alif.ngodonationmanagementsoftware.SceneSwitcher.switchTo;

public class CompanyDetailsController {

    @FXML private TableView<CompanyDetails> tableView;
    @FXML private TableColumn<CompanyDetails, String> nameColumn;
    @FXML private TableColumn<CompanyDetails, String> emailColumn;
    @FXML private TableColumn<CompanyDetails, String> phoneColumn;
    @FXML private TableColumn<CompanyDetails, String> typeColumn;

    private ObservableList<CompanyDetails> companyList = FXCollections.observableArrayList();

    private static final String COMPANY_FILE = "companies.bin";
    private static final String DEMO_FILE = "DCompany.bin";

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        emailColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmail()));
        phoneColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPhone()));
        typeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getType()));
    }

    // ===== Show Button =====
    @FXML
    public void showButtonClicked(ActionEvent event) {
        companyList.clear();
        ArrayList<NewCompany> allCompanies = getAllCompanies();

        for (NewCompany c : allCompanies) {
            companyList.add(new CompanyDetails(c.getName(), c.getEmail(), c.getPhone(), c.getType()));
        }

        tableView.setItems(companyList);
    }

    // ===== Download PDF =====
    @FXML
    public void downloadButtonClicked(ActionEvent event) {
        if (companyList.isEmpty()) {
            showAlert("No Data", "Nothing to download");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Company Details");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        chooser.setInitialFileName("CompanyDetails_" +
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf");

        Stage stage = (Stage) tableView.getScene().getWindow();
        File file = chooser.showSaveDialog(stage);
        if (file == null) return;

        try {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(file));
            doc.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph("Company Details", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph("Generated on: " + new Date()));
            doc.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3, 3, 2, 2});

            addHeaderCell(table, "Company Name");
            addHeaderCell(table, "Email");
            addHeaderCell(table, "Phone");
            addHeaderCell(table, "Type");

            for (CompanyDetails c : companyList) {
                table.addCell(c.getName());
                table.addCell(c.getEmail());
                table.addCell(c.getPhone());
                table.addCell(c.getType());
            }

            doc.add(table);
            doc.close();
            showAlert("Success", "PDF saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "PDF generation failed!");
        }
    }

    private void addHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5);
        table.addCell(cell);
    }

    // ===== Back Button =====
    @FXML
    public void backButton(ActionEvent event) throws IOException {
        switchTo("/alif/ngodonationmanagementsoftware/Dashboard.fxml", event);
    }

    // ===== Load all companies (demo + real) =====
    private ArrayList<NewCompany> getAllCompanies() {
        ArrayList<NewCompany> list = new ArrayList<>();

        // Load demo companies from DCompany.bin if exists
        list.addAll(readDemoCompanies());

        // Load real companies
        list.addAll(readCompaniesFromFile());

        return list;
    }

    // ===== Real companies =====
    private ArrayList<NewCompany> readCompaniesFromFile() {
        File file = new File(COMPANY_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<NewCompany>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ===== Demo companies =====
    private ArrayList<NewCompany> readDemoCompanies() {
        File file = new File(DEMO_FILE);
        if (!file.exists()) return getDefaultDemoCompanies(); // fallback default demo list
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<NewCompany>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return getDefaultDemoCompanies();
        }
    }

    private ArrayList<NewCompany> getDefaultDemoCompanies() {
        ArrayList<NewCompany> demo = new ArrayList<>();
        demo.add(new NewCompany("Bashundhara Group", "bashundhara@gmail.com", "", "N/A", "0123456789", "Textile"));
        demo.add(new NewCompany("BEXIMCO Group", "beximco@gmail.com", "", "N/A", "0123456788", "Pharma"));
        demo.add(new NewCompany("Abul Khair Group", "abulkhair@gmail.com", "", "N/A", "0123456787", "Food"));
        demo.add(new NewCompany("Navana Group", "navana@gmail.com", "", "N/A", "0123456786", "Automobile"));
        demo.add(new NewCompany("Ananda Group", "ananda@gmail.com", "", "N/A", "0123456785", "Food"));
        demo.add(new NewCompany("City Group", "city@gmail.com", "", "N/A", "0123456784", "Conglomerate"));
        demo.add(new NewCompany("Square Group", "square@gmail.com", "", "N/A", "0123456783", "Pharma"));
        demo.add(new NewCompany("Akij Group", "akij@gmail.com", "", "N/A", "0123456782", "Textile"));
        demo.add(new NewCompany("PRAN-RFL Group", "pran@gmail.com", "", "N/A", "0123456781", "Food"));
        demo.add(new NewCompany("Grameenphone", "gp@gmail.com", "", "N/A", "0123456780", "Telecom"));
        return demo;
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
