package ru.information.security.utils;


public class Errors {

    public static final String USER_ALREADY_EXIST = "Пользователь с таким ником уже есть";

    public static final String FIELD_IS_REQUIRED  = "Данное поле обязательно к заполеннию!";

    public static final String LOGIN_OR_PASSWORD_IS_NOT_VALID = "Логин или пароль не правильны!";

    public static final String SAME_PASSWORD = "Вы ввели такой же пароль";

    public static final String MATCH_PASSWORD = "Пароли не совпадают";

    public static final String PASSWORD_CONSTRAINT = "Пароль не удовлетворяет парольному ограничению";

    public enum Type{
        login,
        password,
        loginOrPasswordIncorrect,
        SAME_PASSWORD,
        MatchPassword,
        passwordConstraint
    }
}
