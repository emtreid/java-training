package Matcher.controllers;

import Matcher.DTO.LoginDTO;
import Matcher.Matcher;
import Matcher.components.Account.Account;
import Matcher.components.Account.AccountService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class UserController {

    private final Matcher matcher;
    private final AccountService accountService;

    @Autowired
    public UserController(Matcher matcher, AccountService accountService) {
        this.matcher = matcher;
        this.accountService = accountService;
    }

    @PostMapping("/login")
    public String login(@RequestBody() LoginDTO loginDetails) throws Exception {
        String username = loginDetails.getUsername();
        String password = loginDetails.getPassword();
        if (accountService.authenticatePassword(username, password)) {
            return getJWTToken(username);
        } else return "access denied";
    }

    public final static SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);


    private String getJWTToken(String username) {

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(secretKey).compact();
        return "Bearer " + token;
    }
}
