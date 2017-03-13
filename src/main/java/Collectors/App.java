package Collectors;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Collectors.Controllers.Controller;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/main.fxml"));
        Parent fxmlMain = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        controller.setMainStage(primaryStage);
        primaryStage.setTitle("Работа с должниками v 1.0.1 ");
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.setScene(new Scene(fxmlMain));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}