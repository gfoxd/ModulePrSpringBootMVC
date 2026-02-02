package com.SpringBootMVC.SpringBootMVC.Services;

import com.SpringBootMVC.SpringBootMVC.model.PetDto;
import com.SpringBootMVC.SpringBootMVC.model.UserDto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private Long idCounter = 1L;
    private HashMap<Long, UserDto> users = new HashMap<>();

    public UserDto createUser(UserDto userToCreate) {
        Long id = idCounter++;
        List<PetDto> pets = new ArrayList<>();

        UserDto newUser = new UserDto(id, userToCreate.name(), userToCreate.email(), userToCreate.age(), pets);
        users.put(id, newUser);
        return newUser;
    }

    public UserDto findUserById(Long id) {
        if (id == null) {
            throw new NoSuchElementException("User ID cannot be null");
        }

        if (users.get(id) == null) {
            throw new NoSuchElementException("User with id=" + id + " not found");
        } else {
            return users.get(id);
        }
    }

    public UserDto updateUser(Long id, UserDto userToUpdate) {
        List<PetDto> pets = new ArrayList<>();

        if (userToUpdate == null) {
            throw new NoSuchElementException(String.format("User with id=%s not found", id));
        }

        UserDto updatedUser = new UserDto(id, userToUpdate.name(), userToUpdate.email(), userToUpdate.age(), pets);
        users.remove(id);
        users.put(id, updatedUser);

        return updatedUser;
    }

    public void deleteUserById(Long id) {
        UserDto result = users.remove(id);

        if (result == null) {
            throw new NoSuchElementException(String.format("User with id=%s not found", id));
        }
    }
}
