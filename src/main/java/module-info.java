module akak.myappp {
    requires javafx.controls;
    requires javafx.fxml;


    opens Client1 to javafx.fxml;
    exports Client1;
}