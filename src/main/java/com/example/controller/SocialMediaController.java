package com.example.controller;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 /*Needed endpoints
    POST    /register                        → Register new user
    POST    /login                           → Login existing user
    POST    /messages                        → Create new message
    GET     /messages                        → Get all messages
    GET     /messages/{messageId}           → Get message by ID
    DELETE  /messages/{messageId}           → Delete message by ID
    PATCH   /messages/{messageId}           → Update messageText by ID
    GET     /accounts/{accountId}/messages  → Get messages by user ID
  */

@RestController
public class SocialMediaController {


  private final AccountService accountService;
  private final MessageService messageService;

  public SocialMediaController(AccountService accountService, MessageService messageService)
  {
    this.accountService = accountService;
    this.messageService = messageService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Account loginRequest)
  {
    String username = loginRequest.getUsername();
    String password = loginRequest.getPassword();

    Account account = accountService.login(username, password);

    if(account == null)
    {
      return ResponseEntity.status(401).body("Invalid username or password.");
    }

    return ResponseEntity.ok(account);
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody Account newAccount)
  {
    String username = newAccount.getUsername();
    String password = newAccount.getPassword();

    if(username == null || username.isBlank() || password == null || password.length() < 4)
    {
      return ResponseEntity.status(400).body("Cannot have empty username or password with less than 4 characters.");
    }

    if(accountService.usernameExists(username))
    {
      return ResponseEntity.status(409).body("Username exists.");
    }

    Account savedAccount = accountService.registerAccount(newAccount);

    return ResponseEntity.ok(savedAccount);
  }

  @PostMapping("/messages")
  public ResponseEntity<?> createMessage(@RequestBody Message message) {
      try {
          if (!messageService.isValidMessage(message)) 
          {
              return ResponseEntity.badRequest().body("Invalid message");
          }
          Message savedMessage = messageService.createMessage(message);
          return ResponseEntity.ok(savedMessage);
      } catch (Exception e) {
          e.printStackTrace(); // for debugging
          return ResponseEntity.status(400).body("Internal Server Error: " + e.getMessage());
      }
  }

  @GetMapping("/messages")
  public ResponseEntity<List<Message>> getAllMessages()
  {
    List<Message> messages = messageService.getAllMessages();
    return ResponseEntity.ok(messages);
  }

  @GetMapping("/messages/{messageId}")
  public ResponseEntity<Message> getMessagesById(@PathVariable Integer messageId)
  {
    Optional<Message> optionalMessage = messageService.getMessageById(messageId);

    if (optionalMessage.isPresent()) {
        return ResponseEntity.ok(optionalMessage.get());
    } else {
        return ResponseEntity.ok().build();
    }
  }

  @DeleteMapping("/messages/{messageId}")
  public ResponseEntity<?> deleteMessageById(@PathVariable Integer messageId)
  {
    Integer rowsDeleted = messageService.deleteMessageById(messageId);
        if (rowsDeleted == 0) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok(rowsDeleted);
  }

  @PatchMapping("/messages/{messageId}")
  public ResponseEntity<?> patchMessageById(@RequestBody Map<String, String> body, @PathVariable Integer messageId) 
  {

    String newMessage = body.get("messageText");

    Optional<Message> updated = messageService.editMessage(newMessage, messageId);

    if (updated.isPresent()) {
        return ResponseEntity.ok(1); // 1 row updated
    } else {
        return ResponseEntity.badRequest().body("Invalid message ID or message text.");
    }
  }

  @GetMapping("/accounts/{accountId}/messages")
  public ResponseEntity<List<Message>> getAllMessagesByUser(@PathVariable Integer accountId)
  {
    List<Message> messages = messageService.getMessageByAccountId(accountId);
        
    return ResponseEntity.ok(messages);
  }



}
