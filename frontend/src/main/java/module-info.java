module be.treep.frontend {
    requires javafx.controls;
    requires javafx.fxml;


    opens be.treep.frontend to javafx.fxml;
    exports be.treep.frontend;
}