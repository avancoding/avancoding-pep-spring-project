package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entity.Account;

/*
 * findByUsername(String username)

    existsByUsername(String username)
 */

public interface AccountRepository extends JpaRepository<Account, Integer>{
    Account findByUsername(String username);
    boolean existsByUsername(String username);
}
