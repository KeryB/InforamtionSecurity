package ru.information.security.commons;

import ru.information.security.entity.User;
import ru.information.security.utils.Errors;

import java.util.HashMap;
import java.util.Map;

public class Validate {

    public Map<Errors.Type, String> validLogin (String password , String login){

        Map<Errors.Type, String> errorMap = new HashMap<>();
        if(login.isEmpty()) {
            errorMap.put(Errors.Type.login, Errors.FIELD_IS_REQUIRED);
        }

        return errorMap;
    }

    public void findErrors() {



    }
}
