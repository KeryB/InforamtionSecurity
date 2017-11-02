package ru.information.security.utils;


import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONObject;
import ru.information.security.entity.Role;
import ru.information.security.entity.User;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class FileWorker {

    private static String fileName = "C://Users/Кирилл/IdeaProjects/InforamtionSecurity/src/main/resources/files/users.json";

    private static final String adminPassword = "admin123";

    private static Cipher cipher;

//    private static Key key;

    private static String algorithm = "AES";

    private static final String key = "123xasdfghjklzxc";

    private static SecretKey originalKey;

    public static List<User> readFromFile() {

            List<User> userList = new ArrayList<>();

        try {
//            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
//            key = keyGenerator.generateKey();
//            byte[] encoded = key.getEncoded();

            byte[] decodedKey = key.getBytes();

            originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, algorithm);
            cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                User user = new User(Role.admin.ordinal(), "admin", PasswordDecoder.hashPassword(adminPassword), false, false);
                writeToFile(Collections.singletonList(user));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            byte[] encryptedBytes = objectMapper.readValue(new FileInputStream(fileName), byte[].class);
            try {
                userList = decrypt(encryptedBytes, objectMapper);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userList;
    }

    public static void writeToFile(List<User> user) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            byte[] bytes = mapper.writeValueAsBytes(user);
            byte[] encryptedBytes = new byte[0];
            try {
                encryptedBytes = encrypt(bytes);
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
            }
            mapper.writeValue(fos, encryptedBytes);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static byte[] encrypt(byte[] bytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE, originalKey);
        return cipher.doFinal(bytes);
    }

    private static List<User> decrypt(byte[] encryptionBytes, ObjectMapper objectMapper) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        cipher.init(Cipher.DECRYPT_MODE, originalKey);

        byte[] recoveredBytes = new byte[0];
        recoveredBytes = cipher.doFinal(encryptionBytes);

        List<User> userList = null;

        try {
            userList = objectMapper.readValue(recoveredBytes, new TypeReference<List<User>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;

    }

}
