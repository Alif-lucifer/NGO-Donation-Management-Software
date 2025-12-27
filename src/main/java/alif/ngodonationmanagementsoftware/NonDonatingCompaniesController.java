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
import java.util.*;

import static alif.ngodonationmanagementsoftware.SceneSwitcher.switchTo;

public class NonDonatingCompaniesController {

    @FXML private TableView<NonDonatingCompany> tableView;
    @FXML private TableColumn<NonDonatingCompany,String> companyColumn;
    @FXML private TableColumn<NonDonatingCompany,String> emailColumn;
    @FXML private TableColumn<NonDonatingCompany,String> phoneColumn;
    @FXML private TableColumn<NonDonatingCompany,String> typeColumn;

    private ObservableList<NonDonatingCompany> dataList = FXCollections.observableArrayList();
    private static final String COMPANY_FILE = "companies.bin";

    @FXML
    public void initialize(){
        companyColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCompanyName()));
        emailColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmail()));
        phoneColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPhone()));
        typeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getType()));
    }

    @FXML
    public void showList(ActionEvent event){
        dataList.clear();

        for(NewCompany c : getAllCompanies()){
            String fileName = c.getName().replaceAll("\\s+","_")+".bin";
            File f = new File(fileName);
            if(!f.exists() || f.length()==0){ // only non-donating
                dataList.add(new NonDonatingCompany(c.getName(),c.getEmail(),c.getPhone(),c.getType()));
            }
        }

        tableView.setItems(dataList);
    }

    @FXML
    public void downloadList(ActionEvent event){
        if(dataList.isEmpty()){
            showAlert("No Data","Nothing to download");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Non-Donating Companies");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files","*.pdf"));
        chooser.setInitialFileName("NonDonatingCompanies_"+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+".pdf");
        Stage stage = (Stage)tableView.getScene().getWindow();
        File file = chooser.showSaveDialog(stage);
        if(file==null) return;

        try{
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc,new FileOutputStream(file));
            doc.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD,16);
            Paragraph title = new Paragraph("Non-Donating Companies",titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph("Generated on: "+new Date()));
            doc.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3,3,2,2});

            addHeaderCell(table,"Company Name");
            addHeaderCell(table,"Email");
            addHeaderCell(table,"Phone");
            addHeaderCell(table,"Type");

            for(NonDonatingCompany c: dataList){
                table.addCell(c.getCompanyName());
                table.addCell(c.getEmail());
                table.addCell(c.getPhone());
                table.addCell(c.getType());
            }

            doc.add(table);
            doc.close();
            showAlert("Success","PDF saved successfully!");
        } catch (Exception e){
            e.printStackTrace();
            showAlert("Error","PDF generation failed!");
        }
    }

    private void addHeaderCell(PdfPTable table,String text){
        PdfPCell cell = new PdfPCell(new Phrase(text,FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5);
        table.addCell(cell);
    }

    @FXML
    public void backToDashboard(ActionEvent event){
        try{
            switchTo("/alif/ngodonationmanagementsoftware/Dashboard.fxml",event);
        }catch (IOException e){
            e.printStackTrace();
            showAlert("Error","Cannot return to dashboard!");
        }
    }

    private ArrayList<NewCompany> getAllCompanies(){
        ArrayList<NewCompany> list = new ArrayList<>();
        // demo
        list.add(new NewCompany("Bashundhara Group","bashundhara@gmail.com","0123456789","Textile"));
        list.add(new NewCompany("BEXIMCO Group","beximco@gmail.com","0123456788","Pharma"));
        list.add(new NewCompany("Abul Khair Group","abulkhair@gmail.com","0123456787","Food"));
        list.add(new NewCompany("Navana Group","navana@gmail.com","0123456786","Automobile"));
        list.add(new NewCompany("Ananda Group","ananda@gmail.com","0123456785","Food"));
        list.add(new NewCompany("City Group","city@gmail.com","0123456784","Conglomerate"));
        list.add(new NewCompany("Square Group","square@gmail.com","0123456783","Pharma"));
        list.add(new NewCompany("Akij Group","akij@gmail.com","0123456782","Textile"));
        list.add(new NewCompany("PRAN-RFL Group","pran@gmail.com","0123456781","Food"));
        list.add(new NewCompany("Grameenphone","gp@gmail.com","0123456780","Telecom"));
        // new companies
        list.addAll(readCompaniesFromFile());
        return list;
    }

    private ArrayList<NewCompany> readCompaniesFromFile(){
        File file = new File(COMPANY_FILE);
        if(!file.exists()) return new ArrayList<>();
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            return (ArrayList<NewCompany>) ois.readObject();
        }catch(Exception e){ e.printStackTrace(); return new ArrayList<>();}
    }

    private void showAlert(String title,String msg){
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}

