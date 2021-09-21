package Matcher.components.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Account> getAccountByToken(int token) {
        return accountRepository.findByToken(token);
    }

    public int getToken(String username, String password) throws Exception {
        List<Account> userAccounts = getAccountByUsername(username);
        if (userAccounts.size() == 0) {
            throw new Exception("Invalid username or password");
        } else if (userAccounts.get(0).getPassword().equals(password)) {
            return userAccounts.get(0).getToken();
        } else {
            throw new Exception("Invalid username or password");
        }
    }

    public String authenticateToken(int token) throws Exception {
        List<Account> tokenAccounts = getAccountByToken(token);
        if (tokenAccounts.size() != 0) {
            return tokenAccounts.get(0).getUsername();
        } else {
            throw new Exception("Invalid token");
        }
    }

    public void saveOrUpdate(Account account) {
        List<Account> prevAccounts = getAccountByUsername(account.getUsername());
        if (prevAccounts.size() != 0) {
            delete(prevAccounts.get(0).getId());
        }
        accountRepository.save(account);
        System.out.println("currentLength" + getAllAccount().size());
    }

    public void delete(int id) {
        accountRepository.deleteById(id);
    }

}
