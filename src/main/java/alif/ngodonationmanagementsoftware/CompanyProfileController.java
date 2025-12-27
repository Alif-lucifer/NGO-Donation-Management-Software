package alif.ngodonationmanagementsoftware;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class CompanyProfileController {

    @FXML
    private Label nameLabel;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField addressField;

    private NewCompany loggedInCompany;

    private static final String DEMO_FILE = "DCompany.bin";

    // ===== Demo Companies default list =====
    private ArrayList<NewCompany> getDefaultDemoCompanies() {
        ArrayList<NewCompany> list = new ArrayList<>();
        list.add(new NewCompany("Bashundhara Group", "bashundhara@gmail.com", "", "N/A", "0123456789", "Textile"));
        list.add(new NewCompany("BEXIMCO Group", "beximco@gmail.com", "", "N/A", "0123456788", "Pharma"));
        list.add(new NewCompany("Abul Khair Group", "abulkhair@gmail.com", "", "N/A", "0123456787", "Food"));
        list.add(new NewCompany("Navana Group", "navana@gmail.com", "", "N/A", "0123456786", "Automobile"));
        list.add(new NewCompany("Ananda Group", "ananda@gmail.com", "", "N/A", "0123456785", "Food"));
        list.add(new NewCompany("City Group", "city@gmail.com", "", "N/A", "0123456784", "Conglomerate"));
        list.add(new NewCompany("Square Group", "square@gmail.com", "", "N/A", "0123456783", "Pharma"));
        list.add(new NewCompany("Akij Group", "akij@gmail.com", "", "N/A", "0123456782", "Textile"));
        list.add(new NewCompany("PRAN-RFL Group", "pran@gmail.com", "", "N/A", "0123456781", "Food"));
        list.add(new NewCompany("Grameenphone", "gp@gmail.com", "", "N/A", "0123456780", "Telecom"));
        return list;
    }

    private ArrayList<NewCompany> readDemoCompaniesFromFile() {
        File file = new File(DEMO_FILE);
        if (!file.exists()) return getDefaultDemoCompanies();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<NewCompany>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return getDefaultDemoCompanies();
        }
    }

    private void writeDemoCompaniesToFile(ArrayList<NewCompany> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DEMO_FILE))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isDemoCompany(NewCompany company) {
        for (NewCompany c : getDefaultDemoCompanies()) {
            if (c.getName().equalsIgnoreCase(company.getName())) return true;
        }
        return false;
    }

    private void updateDemoCompany(NewCompany company) {
        ArrayList<NewCompany> demoList = readDemoCompaniesFromFile();
        boolean found = false;
        for (int i = 0; i < demoList.size(); i++) {
            if (demoList.get(i).getName().equalsIgnoreCase(company.getName())) {
                demoList.set(i, company);
                found = true;
                break;
            }
        }
        if (!found) demoList.add(company);
        writeDemoCompaniesToFile(demoList);
    }

    // ===== File Handling for Real Companies =====
    private ArrayList<NewCompany> readCompaniesFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("companies.bin"))) {
            return (ArrayList<NewCompany>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void writeCompaniesToFile(ArrayList<NewCompany> companies) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("companies.bin"))) {
            oos.writeObject(companies);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private NewCompany getCompanyByName(String name) {
        ArrayList<NewCompany> companies = readCompaniesFromFile();
        for (NewCompany company : companies) {
            if (company.getName().equalsIgnoreCase(name)) return company;
        }
        return null;
    }

    // ===== Initialize =====
    @FXML
    public void initialize() {
        String companyName = LoginController.loggedInCompany;
        if (companyName == null) {
            showAlert("Error", "No company logged in!");
            return;
        }

        // Load company
        loggedInCompany = getCompanyByName(companyName);
        if (loggedInCompany == null) {
            ArrayList<NewCompany> demoList = readDemoCompaniesFromFile();
            for (NewCompany c : demoList) {
                if (c.getName().equalsIgnoreCase(companyName)) {
                    loggedInCompany = c;
                    break;
                }
            }
        }

        if (loggedInCompany != null) {
            nameLabel.setText(loggedInCompany.getName());
            phoneField.setText(loggedInCompany.getPhone());
            addressField.setText(loggedInCompany.getAddress());
        } else {
            showAlert("Error", "Company profile not found!");
        }
    }

    // ===== Save =====
    @FXML
    private void saveButtonClicked(ActionEvent event) {
        if (loggedInCompany == null) {
            showAlert("Error", "No company loaded to update!");
            return;
        }

        loggedInCompany.setPhone(phoneField.getText().trim());
        loggedInCompany.setAddress(addressField.getText().trim());

        String companyName = loggedInCompany.getName();

        if (isDemoCompany(loggedInCompany)) {
            updateDemoCompany(loggedInCompany);
        } else {
            ArrayList<NewCompany> companies = readCompaniesFromFile();
            boolean found = false;
            for (int i = 0; i < companies.size(); i++) {
                if (companies.get(i).getName().equalsIgnoreCase(companyName)) {
                    companies.set(i, loggedInCompany);
                    found = true;
                    break;
                }
            }
            if (found) writeCompaniesToFile(companies);
        }

        showAlert("Profile Updated", "Profile of \"" + companyName + "\" updated successfully!");
        phoneField.clear();
        addressField.clear();
    }

    // ===== Back =====
    @FXML
    private void backButton(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("CompanyDashboard.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Cannot open dashboard!");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
