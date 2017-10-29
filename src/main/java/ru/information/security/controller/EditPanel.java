package ru.information.security.controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Pair;
import ru.information.security.commons.UserPayload;
import ru.information.security.entity.User;
import ru.information.security.service.UserService;
import ru.information.security.utils.Errors;
import ru.information.security.utils.TableViewBuilder;

import java.net.URL;
import java.util.ResourceBundle;

public class EditPanel implements Initializable {


    @FXML
    private JFXCheckBox blocked;

    @FXML
    private JFXCheckBox constraint;

    @FXML
    private JFXTextField user;

    @FXML
    private Label error;

    @FXML
    private Label userLabel;

    @FXML
    private Label blockLabel;

    @FXML
    private Label passwordConstaint;

    private User selectedUser = UserPayload.getSelectedUser();

    private UserService userService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userService = new UserService();
        user.setText(UserPayload.getSelectedUser().getLogin());
        error.setVisible(false);
        constraint.setText("");
        constraint.setSelected(selectedUser.getPasswordConstraint());
        blocked.setText("");
        blocked.setSelected(selectedUser.isBlocked());
    }

    @FXML
    void actionSave(ActionEvent event) {
        User editUser = new User();
        Pair<Errors.Type, String> errors = userService.validateEditPanel(user.getText(), selectedUser, editUser);
        if (errors != null) {
            error.setVisible(true);
            error.setText(errors.getValue());
        } else if (selectedUser != null) {
            userService.writeToFile(editUser, selectedUser, user.getText(), blocked.isSelected(), constraint.isSelected());
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

    private void setUserPayload(boolean value) {
        blockLabel.setVisible(value);
        passwordConstaint.setVisible(value);
        blocked.setVisible(value);
        constraint.setVisible(value);
    }

}
