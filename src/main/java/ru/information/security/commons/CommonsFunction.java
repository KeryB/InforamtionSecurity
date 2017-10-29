package ru.information.security.commons;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class CommonsFunction {

    protected static final String CHANGE_PASSWORD = "Смена пароля";
    protected static final String ADD_USER = "Добавление пользователя";
    protected static final String SIGN_IN = "Авторизация";
    protected static final String USER_FORM = "Hashboard";


    protected void showModal(String path, String name, double width, double height, boolean resizable) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(path));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(name);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setMinWidth(width);
            stage.setMinHeight(height);
            stage.setResizable(resizable);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void actionCancel(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void showJfoenixDialog(String body, String heading, AnchorPane anchorPane, double layOutX, double layOutY, JFXButton jfxButton) {

        StackPane stackPane = new StackPane();
        stackPane.setLayoutX(layOutX);
        stackPane.setLayoutY(layOutY);
        anchorPane.getChildren().add(stackPane);
        JFXDialogLayout content = new JFXDialogLayout();
        Text text = new Text();
        text.setFont(new Font(18));
        text.setText(heading);
        content.setHeading(text);
        content.setBody(new Text(body));
        JFXDialog jfxDialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("Ок");
        button.setOnAction(e -> {
            if (jfxButton != null) {
                Stage stage = (Stage) jfxButton.getScene().getWindow();
                stage.close();
            }
            jfxDialog.close();
        });
        content.setActions(button);
        jfxDialog.setOverlayClose(false);
        anchorPane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            jfxDialog.close();
        });
        jfxDialog.show();
    }

}
