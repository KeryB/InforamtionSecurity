package ru.information.security.utils;


import org.mindrot.jbcrypt.BCrypt;

public class PasswordDecoder {

    private static final int workload = 12;

    public static String hashPassword(String password_plaintext) {
        return(BCrypt.hashpw(password_plaintext, BCrypt.gensalt(workload)));
    }

    public static boolean checkPassword(String password, String hash) {
        if(password.isEmpty() && hash.isEmpty()){
            return true;
        } else if(!password.isEmpty() && hash.isEmpty()){
            return false;
        } else {
            return BCrypt.checkpw(password, hash);
        }
    }
}
