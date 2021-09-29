package Matcher.components.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    public List<Account> getAllAccount() {
        List<Account> accounts = new ArrayList<Account>();
        accountRepository.findAll().forEach(accountSQL -> accounts.add(accountSQL));
        return accounts;
    }

    public Account getAccountById(int id) {
        return accountRepository.findById(id).get();
    }

    public List<Account> getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

//    public List<Account> getAccountByToken(int token) {
//        return accountRepository.findByToken(token);
//    }

    public boolean authenticatePassword(String username, String password) {
        List<Account> userAccounts = getAccountByUsername(username);
        if (userAccounts.size() == 0) {
            return false;
        } else {
            Account account = userAccounts.get(0);
            String salt = account.getSalt();
            String expectedHash = account.getPassword();
            return Passwords.isExpectedPassword(password.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), expectedHash.getBytes(StandardCharsets.UTF_8));
        }
    }

//    public String authenticateToken(int token) throws Exception {
//        List<Account> tokenAccounts = getAccountByToken(token);
//        if (tokenAccounts.size() != 0) {
//            return tokenAccounts.get(0).getUsername();
//        } else {
//            throw new Exception("Invalid token");
//        }
//    }

    public void saveOrUpdate(Account account) {
        List<Account> prevAccounts = getAccountByUsername(account.getUsername());
        if (prevAccounts.size() != 0) {
            delete(prevAccounts.get(0).getId());
        }
        accountRepository.save(account);
    }

    public void delete(int id) {
        accountRepository.deleteById(id);
    }

}
