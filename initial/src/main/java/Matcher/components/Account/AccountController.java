package Matcher.components.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/account")
    private List<Account> getAllAccount() {
        return accountService.getAllAccount();
    }

    @GetMapping("/account/{id}")
    private Account getAccount(@PathVariable("id") int id) {
        return accountService.getAccountById(id);
    }

    @DeleteMapping("/account/{id}")
    private void deleteAccount(@PathVariable("id") int id) {
        accountService.delete(id);
    }

    @PostMapping("/account")
    private int saveAccount(@RequestBody Account account) {
        accountService.saveOrUpdate(account);
        return account.getId();
    }
}
