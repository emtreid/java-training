package Matcher.components.Account;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class Passwords {
    private HashMap<String, String> passwords;
    private HashMap<String, Integer> tokens;
    private HashMap<Integer, String> usernames;

    public Passwords() {
        passwords = new HashMap<String, String>();
        tokens = new HashMap<String, Integer>();
        usernames = new HashMap<Integer, String>();
    }

    public void addAccount(String username, String password) {
        if (passwords.get(username) == null) {
            passwords.put(username, password);
            int token = (username + password).hashCode();
            tokens.put(username, token);
            usernames.put(token, username);
        }

    }

    public int getToken(String username, String password) throws Exception {
        if (passwords.get(username).equals(password)) {
            return tokens.get(username);
        } else {
            throw new Exception("Invalid username or password");
        }
    }

    public String authenticateToken(int token) throws Exception {
        if (usernames.containsKey(token)) {
            return usernames.get(token);
        } else {
            throw new Exception("Invalid token");
        }
    }
}
