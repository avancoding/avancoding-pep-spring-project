package com.example.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

/* 
 * Register: Validate uniqueness of username, validate password length, save to DB.
    Login: Fetch by username, validate password match.
*/
@Service
@Transactional
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository)
    {
        this.accountRepository = accountRepository;
    }
    
    public Account registerAccount(Account account)
    {
        return accountRepository.save(account);
    }
    
    public boolean usernameExists(String username) {
        return accountRepository.existsByUsername(username);
    }

    public Account login(String username, String password)
    {
        Account account = accountRepository.findByUsername(username);

        //if account doesn't exit or password doesn't match
        if(account == null || !account.getPassword().equals(password))
        {
            return null;
        }
        return account;
    }
}
