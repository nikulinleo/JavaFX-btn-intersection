module com.example.buttondrug {
    requires transitive javafx.controls;
    requires javafx.fxml;


    opens com.example.buttondrug to javafx.fxml;
    exports com.example.buttondrug;
}