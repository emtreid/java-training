package Matcher.components.Account;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface AccountRepository extends CrudRepository<Account, Integer> {
    List<Account> findByUsername(String username);

//    List<Account> findByToken(int token);
}
