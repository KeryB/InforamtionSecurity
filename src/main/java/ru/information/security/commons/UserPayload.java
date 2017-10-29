package ru.information.security.commons;


import ru.information.security.entity.User;
import ru.information.security.service.UserService;

import java.util.List;

public class UserPayload {

    private static User currentUser;

    private static List<User> userList;

    private static User selectedUser;

    public static User getUser() {
        return currentUser;
    }

    public static void setUser(User user) {
        UserPayload.currentUser = user;
    }

    public static List<User> getUserList() {
        return userList;
    }

    public static void setUserList(List<User> userList) {
        UserPayload.userList = userList;
    }

    public static User getSelectedUser() {
        return selectedUser;
    }

    public static void setSelectedUser(User selectedUser) {
        UserPayload.selectedUser = selectedUser;
    }
}
