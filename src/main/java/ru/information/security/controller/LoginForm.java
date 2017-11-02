package ru.information.security.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.information.security.commons.CommonsFunction;
import ru.information.security.commons.UserPayload;
import ru.information.security.service.UserService;
import ru.information.security.utils.Errors;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class LoginForm extends CommonsFunction implements Initializable {

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton login;

    @FXML
    private JFXTextField user;

    @FXML
    private Label requiredLogin;

    @FXML
    private Label requiredPassword;

    @FXML
    private Label error;

    @FXML
    private JFXButton changePassword;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXSpinner spinner;

    private UserService userService;

    private UserPayload userPayload;

    private int count = 3;

    public void initialize(URL location, ResourceBundle resources) {

        userService = new UserService();
        userPayload = new UserPayload();

        spinner.setVisible(false);
        spinner.setRadius(10);
    }


    @FXML
    void loginAction(ActionEvent event) {

        spinner.setVisible(true);


        anchorPane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            requiredPassword.setVisible(false);
            requiredLogin.setVisible(false);
            error.setVisible(false);
        });

        user.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if (!requiredLogin.getText().isEmpty()) {
                requiredLogin.setVisible(false);
            }
        });

        CompletableFuture.supplyAsync(() -> userService.validateUserPayload(password.getText(), user.getText()))
                .thenAccept(accessUser -> Platform.runLater(() -> {
                    if (accessUser.getErrors() != null) {
                        accessUser.getErrors().forEach((key, value) -> {
                            if (key == Errors.Type.password) {
                                requiredPassword.setVisible(true);
                            } else if (key == Errors.Type.login) {
                                requiredLogin.setVisible(true);
                            } else {
                                error.setVisible(true);
                                if (count == 0) {
                                    actionCancel(event);
                                }
                                showJfoenixDialog("У вас осталось " + count-- + " попытки", "Ошибка", anchorPane, 146, 150, null);
                            }
                        });
                        spinner.setVisible(false);
                    }
                    if (accessUser.getUser() != null) {
                        cancelPanel(event);
                        showController();
                    }
                }));

    }

    private void cancelPanel(ActionEvent actionEvent) {
        actionCancel(actionEvent);
    }

    private void showController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/UserForm.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setMinWidth(694);
            stage.setMinHeight(496);
            stage.show();

            UserForm userForm = fxmlLoader.getController();
            userForm.checkOnEmptyPassword();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
