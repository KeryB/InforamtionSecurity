package ru.information.security.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import ru.information.security.commons.CommonsFunction;
import ru.information.security.commons.UserPayload;
import ru.information.security.entity.Role;
import ru.information.security.entity.User;

import java.util.List;

public class TableViewBuilder extends CommonsFunction {

    private static TableView<User> tableView;

    public void createUserTable(AnchorPane anchorPane){
        tableView.setPrefHeight(434);
        tableView.setPrefWidth(503);
        tableView.setLayoutX(188);
        tableView.setLayoutY(48);

        tableView.setEditable(true);
        createTableColumns();

        tableView.setOnMouseClicked(event->{
            if (event.getClickCount() == 2) {
                UserPayload.setSelectedUser(tableView.getSelectionModel().getSelectedItem());
                showEditPanel(tableView);
            }
        });

        anchorPane.getChildren().add(tableView);
    }

    private void showEditPanel(TableView<User> tableView) {
        showModal("/fxml/EditPanel.fxml", "Редактирование пользователя",500,300, false);
    }

    private void createTableColumns(){

        TableColumn<User, String> login = new TableColumn<>(UserUtils.LOGIN);
        login.setCellValueFactory(new PropertyValueFactory<User,String>("login"));

        TableColumn<User, String> isBlocked = new TableColumn<>(UserUtils.IS_BLOCKED);
        isBlocked.setCellValueFactory(new PropertyValueFactory<User,String>("isBLockedTable"));

        TableColumn<User, String> constrains = new TableColumn<>(UserUtils.CONSTRAINS);
        constrains.setCellValueFactory(new PropertyValueFactory<User,String>("passwordConstraintTable"));

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(login,isBlocked,constrains);
    }

    public static void addTableItems(){
        ObservableList<User> tableList = FXCollections.observableArrayList();

        UserPayload.getUserList().forEach(e->{
            if(e.getRole() != Role.admin.ordinal()){
                tableList.add(e);
            }
        });
        tableView.setItems(tableList);
    }

    public void createTableView(){
        tableView = new TableView<>();
    }

    public TableView<User> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<User> tableView) {
        this.tableView = tableView;
    }
}
