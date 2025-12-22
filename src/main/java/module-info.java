module alif.ngodonationmanagementsoftware {
    requires javafx.controls;
    requires javafx.fxml;


    opens alif.ngodonationmanagementsoftware to javafx.fxml;
    exports alif.ngodonationmanagementsoftware;
}