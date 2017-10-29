package ru.information.security.commons;

import ru.information.security.entity.User;
import ru.information.security.utils.Errors;

import java.util.Map;

public class AccessUser {

    private User user;

    public enum Type{
        success,
        error
    }

    private Map<Errors.Type,String> errors;

    public void setErrors(Map<Errors.Type,String> errors){
        this.errors = errors;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Map<Errors.Type, String> getErrors() {
        return errors;
    }
}
