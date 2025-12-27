 module alif.ngodonationmanagementsoftware {
    requires javafx.controls;
    requires javafx.fxml;
    requires itextpdf;


    opens alif.ngodonationmanagementsoftware to javafx.fxml;
    exports alif.ngodonationmanagementsoftware;
}