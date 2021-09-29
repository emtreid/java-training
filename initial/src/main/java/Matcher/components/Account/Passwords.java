package Matcher.components.Account;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.HashMap;

@Service
public class Passwords {
//    private final HashMap<String, String> passwords;
//    private final HashMap<String, Integer> tokens;
//    private final HashMap<Integer, String> usernames;

    public Passwords() {
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return bytes;
    }

    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, 1000, 128);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public static boolean isExpectedPassword(char[] password, byte[] salt, byte[] expectedHash) {
        byte[] pwdHash = hash(password, salt);
        pwdHash = new String(pwdHash, StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8);
        Arrays.fill(password, Character.MIN_VALUE);
        if (pwdHash.length != expectedHash.length) return false;
        for (int i = 0; i < pwdHash.length; i++) {
            if (pwdHash[i] != expectedHash[i]) return false;
        }
        return true;
    }

//        passwords = new HashMap<String, String>();
//        tokens = new HashMap<String, Integer>();
//        usernames = new HashMap<Integer, String>();
//    }
//
//    public void addAccount(String username, String password) {
//        if (passwords.get(username) == null) {
//            passwords.put(username, password);
//            int token = (username + password).hashCode();
//            tokens.put(username, token);
//            usernames.put(token, username);
//        }
//
//    }
//
//    public int getToken(String username, String password) throws Exception {
//        if (passwords.get(username).equals(password)) {
//            return tokens.get(username);
//        } else {
//            throw new Exception("Invalid username or password");
//        }
//    }
//
//    public String authenticateToken(int token) throws Exception {
//        if (usernames.containsKey(token)) {
//            return usernames.get(token);
//        } else {
//            throw new Exception("Invalid token");
//        }
//    }
}
