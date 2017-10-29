package ru.information.security.controller;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.octicons.OctIcon;
import de.jensd.fx.glyphs.octicons.OctIconView;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.information.security.commons.CommonsFunction;
import ru.information.security.commons.UserPayload;
import ru.information.security.entity.Role;
import ru.information.security.entity.User;
import ru.information.security.service.UserService;
import ru.information.security.utils.TableViewBuilder;
import ru.information.security.utils.UserUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class UserForm extends CommonsFunction implements Initializable {

    @FXML
    private Label typeUser;

    @FXML
    private Label loginName;

    @FXML
    private Pane leftPane;

    @FXML
    private JFXButton blockUser;

    @FXML
    private JFXButton showUsers;

    @FXML
    private ImageView userIcon;

    @FXML
    private JFXButton constraints;

    @FXML
    private JFXButton addUsers;

    @FXML
    private JFXButton changePassword;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXButton info;

    @FXML
    private JFXButton signOut;

    private UserService userService;

    private TableViewBuilder tableViewBuilder;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ChangePassword.setUserForm(this);
        userService = new UserService();
        tableViewBuilder = new TableViewBuilder();
        loginName.setText(UserPayload.getUser().getLogin());
        signOut.setOnAction(e -> {
            signOutAction(e);
        });
        if (UserPayload.getUser().getRole() == Role.admin.ordinal()) {
            typeUser.setText(UserUtils.ADMIN);
            File file = new File(UserUtils.ADMIN_ICON_PATH);
            String img = null;
            try {
                img = file.toURI().toURL().toString();
                userIcon.setImage(new Image(img));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            setAdminStaff();
        } else {
            setAdminStaff();
            if (!checkOnBlocked()) {
                setUserStaff();
                if (checkOnEmptyPassword()) {
                    Stream.of(info, signOut).forEach(e -> {
                        e.setDisable(true);
                    });
                } else {
                    Stream.of(info, signOut).forEach(e -> {
                        e.setDisable(false);
                    });
                }
            }
            typeUser.setText(UserUtils.USER);
        }
    }

    private void setUserStaff() {
        Stream.of(blockUser, showUsers, constraints, addUsers).forEach(e -> {
            e.setDisable(true);
            e.setOnMouseClicked(event -> {
                showJfoenixDialog("У вас нет доступа для этой функции", "Внимание", anchorPane, 146, 150, null);
            });
        });
    }

    private boolean checkOnEmptyPassword() {
        if (UserPayload.getUser().getPassword().isEmpty()) {
            showJfoenixDialog(UserUtils.EMPTY_PASSWORD_BODY, UserUtils.EMPTY_PASSWORD_HEADING, anchorPane, 146, 150, null);
            return true;
        }
        return false;
    }

    private boolean checkOnBlocked() {
        if (UserPayload.getUser().isBlocked()) {
            showJfoenixDialog(UserUtils.BLOCKED_USER_BODY, UserUtils.BLOCKED_USER_HEADER, anchorPane, 146, 150, blockUser);
            return true;
        }
        return false;
    }

    private void setAdminStaff() {
        anchorPane.getChildren().add(setIcons(FontAwesomeIcon.LIST_UL, 3, 129));
        anchorPane.getChildren().add(setIcons(FontAwesomeIcon.ROTATE_RIGHT, 3, 165));
        anchorPane.getChildren().add(setIcons(FontAwesomeIcon.USER_PLUS, 3, 200));
        anchorPane.getChildren().add(setIcons(FontAwesomeIcon.LOCK, 3, 235));
        anchorPane.getChildren().add(setIcons(FontAwesomeIcon.USER_TIMES, 3, 270));
        anchorPane.getChildren().add(setIcons(FontAwesomeIcon.INFO_CIRCLE, 3, 305));
    }

    private FontAwesomeIconView setIcons(FontAwesomeIcon icon, double x, double y) {
        FontAwesomeIconView fontAwesomeIconView = new FontAwesomeIconView();
        fontAwesomeIconView.setSize("16px");
        fontAwesomeIconView.setIcon(icon);
        fontAwesomeIconView.setLayoutX(x);
        fontAwesomeIconView.setLayoutY(y);
        fontAwesomeIconView.setAccessibleText("10px");
        return fontAwesomeIconView;
    }

    private void signOutAction(ActionEvent actionEvent) {
        actionCancel(actionEvent);
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/LoginForm.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setMinWidth(722);
            stage.setMinHeight(511);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        showModal("/fxml/LoginForm.fxml", SIGN_IN, 722, 511, true);
    }


    @FXML
    void actionShowUsers(ActionEvent event) {
        if (tableViewBuilder.getTableView() == null) {
            tableViewBuilder.createTableView();
            tableViewBuilder.createUserTable(anchorPane);
        }
        TableViewBuilder.addTableItems();
    }


    @FXML
    void changePasswordAction(ActionEvent event) {
        showModal("/fxml/ChangePassword.fxml", CHANGE_PASSWORD, 103, 31, false);
    }

    @FXML
    void addUsersAction(ActionEvent event) {
        showModal("/fxml/AddUser.fxml", ADD_USER, 407, 240, false);
    }


}
