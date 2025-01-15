module com.example.danylozhadyk711473endassignment {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.danylozhadyk711473endassignment.controller to javafx.fxml;
    exports com.example.danylozhadyk711473endassignment.controller;
    exports com.example.danylozhadyk711473endassignment.startup;
    opens com.example.danylozhadyk711473endassignment.startup to javafx.fxml;
}