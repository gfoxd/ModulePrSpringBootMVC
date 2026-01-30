package com.SpringBootMVC.SpringBootMVC.Controllers;

import com.SpringBootMVC.SpringBootMVC.Services.UserService;
import com.SpringBootMVC.SpringBootMVC.model.UserDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.logging.Logger;

@RestController
@RequestMapping("/User")
public class UserController {

    private static final Logger log = Logger.getLogger(UserController.class.getName());

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody UserDto userDto
    ) {
        log.info("UserController request createUser");

        UserDto createdUser = userService.createUser(userDto);

        return ResponseEntity.created(URI.create("/User/" + createdUser.id())).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable Long id
    ) {
        log.info("UserController request getUser");

        UserDto userDto = userService.findUserById(id);

        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDto userDto
    ) {
        log.info("UserController request updateUser");

        UserDto updatedUser = userService.updateUser(id, userDto);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id
    ) {
        log.info("UserController request deleteUser");

        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }
}

