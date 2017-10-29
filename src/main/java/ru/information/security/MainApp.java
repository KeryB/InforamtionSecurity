package ru.information.security;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application{

    public static void main(String[] args) {
        launch(MainApp.class,args);
    }

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/LoginForm.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Авторизация");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root,722,511));
        primaryStage.setMinWidth(722);
        primaryStage.setMinHeight(511);
        primaryStage.show();
    }
}
