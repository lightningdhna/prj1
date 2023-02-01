module view {
    requires javafx.controls;
    requires javafx.fxml;
    requires AnimateFX;
    requires com.jfoenix;


    opens view to javafx.fxml;
    exports view;
}