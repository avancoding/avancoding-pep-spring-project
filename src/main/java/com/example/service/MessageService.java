package com.example.service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.MessageRepository;
import com.example.entity.*;
import java.util.*;

/*
 * Create Message: Validate user exists, message length.

    Read Messages:

    Get all messages.

    Get message by ID.

    Get messages by user ID.

    Update Message:

    Check message exists, validate new text, update.

    Delete Message:

    Check message exists, delete.
 */
@Service
@Transactional
public class MessageService {

   private final MessageRepository messageRepository;

   @Autowired
   public MessageService(MessageRepository messageRepository)
   {
      this.messageRepository = messageRepository;
   }

   public boolean isValidMessage(Message message) 
   {
      return message.getMessageText() != null &&
            !message.getMessageText().isBlank() &&
            message.getMessageText().length() <= 255 &&
            message.getPostedBy() != null; 
   }

   public Message createMessage(Message message) 
   {
      return messageRepository.save(message);
   }

   public List<Message> getAllMessages()
   {
      return messageRepository.findAll();
   }

   public Optional<Message> getMessageById(Integer Id)
   {
      return messageRepository.findById(Id);
   }

   public Integer deleteMessageById(Integer Id)
   {
         // If it doesn’t exist, return 0
        if (!messageRepository.existsById(Id)) {
            return 0;
        }

        messageRepository.deleteById(Id);
        return 1;
   }

   public Optional<Message> editMessage(String newMessageText, Integer Id)
   {  
       Optional<Message> messageOptional = messageRepository.findById(Id);
       if (messageOptional.isPresent()) {
        Message message = messageOptional.get();

        if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
            return Optional.empty();
        }

        message.setMessageText(newMessageText);
        Message savedMessage = messageRepository.save(message);
        return Optional.of(savedMessage); // Save and return updated
    }

    return Optional.empty(); // Message not found
   }

   public List<Message> getMessageByAccountId(Integer accountId)
   {
      return messageRepository.findByPostedBy(accountId);
   }

}
