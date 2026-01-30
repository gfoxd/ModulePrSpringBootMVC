package com.SpringBootMVC.SpringBootMVC.Services;

import com.SpringBootMVC.SpringBootMVC.model.PetDto;
import com.SpringBootMVC.SpringBootMVC.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.NoSuchElementException;

@Service
public class PetService {

    @Autowired
    private UserService userService;

    private Long idCounter = 1L;
    private HashMap<Long, PetDto> pets = new HashMap<>();

    public PetDto createPet(PetDto petToCreate) {
        Long id = idCounter++;
        Long userId = petToCreate.userId();

        UserDto userDto = userService.findUserById(userId);

        PetDto newPet = new PetDto(id, petToCreate.name(), userId);
        pets.put(id, newPet);

        userService.findUserById(userId).pets().add(newPet);
        return newPet;
    }

    public void deletePetById(Long id) {
        if (!pets.containsKey(id)) {
            throw new NoSuchElementException(String.format("Pet with id=%s not found", id));
        }

        Long idOwnerPet = pets.get(id).userId();
        pets.remove(id);

        userService.findUserById(idOwnerPet).pets().removeIf(pet -> pet.id().equals(id));
    }
}
