package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entity.Message;
import java.util.List;

/*
 * findByPostedBy(int accountId)

    Standard CRUD methods from JpaRepository
 */
public interface MessageRepository extends JpaRepository<Message, Integer>{
   List<Message> findByPostedBy(Integer accountId);
}
