package ru.information.security.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.util.Pair;
import ru.information.security.commons.CommonsFunction;
import ru.information.security.commons.UserPayload;
import ru.information.security.entity.User;
import ru.information.security.service.UserService;
import ru.information.security.utils.Errors;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class ChangePassword extends CommonsFunction implements Initializable {

    @FXML
    private JFXPasswordField confirm;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton save;

    @FXML
    private Label passwordNotMatch;

    @FXML
    private Label samePassword;

    @FXML
    private Label newPasswordLabel;

    @FXML
    private Label confirmLabel;

    @FXML
    private AnchorPane anchorPane;

    private Label label = new Label("Введите текущий пароль");

    private User currentUser;

    private UserService userService;

    private boolean isCurrentPassword = false;

    private static UserForm userForm;

    private static boolean isSaveClicked = false;

    private static ActionEvent event;

    public static void setUserForm(UserForm userForm){
        ChangePassword.userForm = userForm;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userService = new UserService();
        currentUser = UserPayload.getUser();
        passwordNotMatch.setVisible(false);
        samePassword.setVisible(false);

        if (!currentUser.getPassword().isEmpty()) {
            Stream.of(confirm, newPasswordLabel, confirmLabel, passwordNotMatch, samePassword).forEach(e -> {
                e.setVisible(false);
            });

            label.setFont(new Font(13));
            label.setLayoutX(10);
            label.setLayoutY(80);
            save.setText("Далее");
            anchorPane.getChildren().add(label);
            isCurrentPassword = true;
        }
        if(currentUser.getPasswordConstraint()){
            showJfoenixDialog("У вас обнаружено парольное ограничение. Пароль может состоять только из букв и цифр", "Внимание",
                    anchorPane, 50,40,null);
        }

        anchorPane.setOnMouseClicked(e->{
            samePassword.setVisible(false);
            passwordNotMatch.setVisible(false);
        });
    }


    @FXML
    void actionSave(ActionEvent event) {

        if (isCurrentPassword) {
            Pair<Errors.Type, String> error = userService.validateChangePassword(password.getText(), currentUser);
            if(error != null){
                samePassword.setVisible(true);
                samePassword.setText(error.getValue());
            } else {
                isCurrentPassword = false;
                Stream.of(confirm, newPasswordLabel, confirmLabel, passwordNotMatch, samePassword).forEach(e -> {
                    e.setVisible(true);
                });

                save.setText("Сохранить");
                label.setVisible(false);
                samePassword.setVisible(false);
                password.setText("");
            }
        } else {
            Pair<Errors.Type, String> error = userService.validateChangePassword(confirm.getText(), password.getText(), currentUser);

            if(currentUser.getPasswordConstraint()){
                password.setLabelFloat(true);
                password.setPromptText("Введите любую букву или цифру");
            }

            if (error != null) {

                if(error.getKey() == Errors.Type.passwordConstraint){
                    samePassword.setVisible(true);
                    samePassword.setText(Errors.PASSWORD_CONSTRAINT);
                }

                if (error.getKey() == Errors.Type.SAME_PASSWORD) {
                    samePassword.setVisible(true);
                    samePassword.setText(error.getValue());
                }
                if (error.getKey() == Errors.Type.MatchPassword) {
                    passwordNotMatch.setVisible(true);
                    passwordNotMatch.setText(error.getValue());
                }
            } else {
                userService.changePassword(currentUser, password.getText());
                isSaveClicked = true;
                actionCancel(event);
            }
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        setEvent(event);
        isSaveClicked = false;
        actionCancel(event);
    }

    public static boolean isIsSaveClicked() {
        return isSaveClicked;
    }

    public static ActionEvent getEvent() {
        return event;
    }

    public static void setEvent(ActionEvent event) {
        ChangePassword.event = event;
    }

}
