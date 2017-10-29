package ru.information.security.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Pair;
import ru.information.security.service.UserService;
import ru.information.security.utils.Errors;
import ru.information.security.utils.TableViewBuilder;

import java.net.URL;
import java.util.ResourceBundle;

public class AddUser implements Initializable{

    @FXML
    private JFXTextField login;

    @FXML
    private Label error;

    private UserService userService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userService = new UserService();
        error.setVisible(false);
    }

    @FXML
    void actionSave(ActionEvent event) {
        Pair<Errors.Type, String> errors = userService.validateAddUser(login.getText());
        if(errors != null) {
            if (errors.getKey() == Errors.Type.login) {
                error.setVisible(true);
                error.setText(errors.getValue());
            }
        } else {
            userService.createNewUser(login.getText());
            TableViewBuilder.addTableItems();
            actionCancel(event);
        }
    }

    @FXML
    void actionCancel(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
