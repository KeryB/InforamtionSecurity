package ru.information.security.service;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import ru.information.security.commons.AccessUser;
import ru.information.security.commons.UserPayload;
import ru.information.security.commons.Validate;
import ru.information.security.entity.Role;
import ru.information.security.entity.User;
import ru.information.security.utils.Errors;
import ru.information.security.utils.FileWorker;
import ru.information.security.utils.PasswordDecoder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService {

    private AccessUser accessUser;

    private List<User> userList = UserPayload.getUserList();

    private Validate validate = new Validate();

    public AccessUser validateUserPayload(String password, String login) {
        accessUser = new AccessUser();

        Map<Errors.Type, String> errors = validate.validLogin(password, login);
        if (!errors.isEmpty()) {
            accessUser.setErrors(errors);
            return accessUser;
        }

        userList = FileWorker.readFromFile();

        UserPayload.setUserList(userList);

        for (User user : userList) {

            if (user.getLogin().equals(login) && PasswordDecoder.checkPassword(password, user.getPassword())) {
                accessUser.setUser(user);
                UserPayload.setUser(user);
                return accessUser;
            }
        }

        if (accessUser.getUser() == null) {
            Map<Errors.Type, String> errorMap = new HashMap<>();
            errorMap.put(Errors.Type.loginOrPasswordIncorrect, Errors.LOGIN_OR_PASSWORD_IS_NOT_VALID);
            accessUser.setErrors(errorMap);
        }

        return accessUser;
    }

    public void writeToFile(User editUser, User user, String login, boolean selected, boolean constraintSelected) {
        editUser.setLogin(login);
        editUser.setPassword(user.getPassword());
        editUser.setRole(user.getRole());
        editUser.setBlocked(selected);
        editUser.setPasswordConstraint(constraintSelected);
        userList.remove(user);
        userList.add(editUser);
        FileWorker.writeToFile(userList);

    }

    public void createNewUser(String login) {
        User user = new User();
        user.setRole(Role.user.ordinal());
        user.setBlocked(false);
        user.setPasswordConstraint(false);
        user.setLogin(login);
        user.setPassword("");
        userList.add(user);

        FileWorker.writeToFile(userList);
    }

    public void changePassword(User curUser, String password) {

        for (User user : userList) {
            if (user.getLogin().equals(curUser.getLogin())) {
                user.setPassword(PasswordDecoder.hashPassword(password));
                break;
            }
        }
        FileWorker.writeToFile(userList);
    }

    public User checkUserOnBlocked() {
        return null;
    }

    public Pair<Errors.Type, String> validateChangePassword(String passwordText, User currentUser) {

        if (passwordText.isEmpty()) {
            return new Pair<>(Errors.Type.MatchPassword, Errors.FIELD_IS_REQUIRED);
        }
        if (!PasswordDecoder.checkPassword(passwordText, currentUser.getPassword())) {
            return new Pair<>(Errors.Type.MatchPassword, Errors.MATCH_PASSWORD);
        }
        return null;
    }

    public Pair<Errors.Type, String> validateChangePassword(String text, String passwordText, User currentUser) {


        if(currentUser.getPasswordConstraint()){
            Pattern pattern = Pattern.compile("(\\w)*");
            Matcher matcher = pattern.matcher(passwordText);
            if(!matcher.matches()){
                return new Pair<>(Errors.Type.passwordConstraint, Errors.PASSWORD_CONSTRAINT);
            }
        }

        if (text.isEmpty()) {
            return new Pair<>(Errors.Type.MatchPassword, Errors.FIELD_IS_REQUIRED);
        }

        if (passwordText.isEmpty()) {
            return new Pair<>(Errors.Type.SAME_PASSWORD, Errors.FIELD_IS_REQUIRED);
        }

        if (PasswordDecoder.checkPassword(passwordText, currentUser.getPassword())) {
            return new Pair<>(Errors.Type.SAME_PASSWORD, Errors.SAME_PASSWORD);
        }

        if (!passwordText.equals(text)) {
            return new Pair<>(Errors.Type.MatchPassword, Errors.MATCH_PASSWORD);
        }

        return null;

    }

    public Pair<Errors.Type, String> validateAddUser(String text) {

        if (text.isEmpty()) {
            return new Pair<>(Errors.Type.login, Errors.FIELD_IS_REQUIRED);
        }

        for (User curUser : userList) {
            if (curUser.getLogin().equals(text)) {
                return new Pair<>(Errors.Type.login, Errors.USER_ALREADY_EXIST);
            }
        }

        return null;
    }

    public Pair<Errors.Type, String> validateEditPanel(String text, User current, User editUser) {

        if (text.isEmpty()) {
            return new Pair<>(Errors.Type.login, Errors.FIELD_IS_REQUIRED);
        }

        if (current.getLogin().equals(text)) {
            return null;
        } else {
            for (User curUser : userList) {
                if (curUser.getLogin().equals(text)) {
                    return new Pair<>(Errors.Type.login, Errors.USER_ALREADY_EXIST);
                }
            }
        }
        return null;
    }


//    private  getPayloadClaims(){
//
//    }
}
