package ru.information.security.entity;

import org.codehaus.jackson.annotate.JsonIgnore;

public class User {

    public User(int role, String login, String password, boolean isBlocked, boolean passwordConstraint) {
        this.password = password;
        this.role = role;
        this.login = login;
        this.isBlocked = isBlocked;
        this.passwordConstraint = passwordConstraint;
    }

    public enum Key{
        login,
        password,
        role
    }

    public User() {
    }

    private int role;

    private String login;

    private String password;

    private boolean passwordConstraint;

    private boolean isBlocked;

    @JsonIgnore
    private String isBLockedTable;
    @JsonIgnore
    private String passwordConstraintTable;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public String getIsBLockedTable() {
        return Boolean.toString(isBlocked);
    }

    public String getPasswordConstraintTable() {
        return Boolean.toString(passwordConstraint);
    }

    public boolean getPasswordConstraint() {
        return passwordConstraint;
    }

    public void setPasswordConstraint(boolean passwordConstraint) {
        this.passwordConstraint = passwordConstraint;
    }

}
