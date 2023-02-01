package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

    public static void main(String... args){
        launch();
    }
    @Override
    public void start(Stage stage) throws Exception {

        stage.setResizable(false);
        Scene scene = new Scene(new MainPage().getRoot());
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }
}
