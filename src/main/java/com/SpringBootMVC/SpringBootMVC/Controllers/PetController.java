package com.SpringBootMVC.SpringBootMVC.Controllers;

import com.SpringBootMVC.SpringBootMVC.Services.PetService;
import com.SpringBootMVC.SpringBootMVC.model.PetDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.logging.Logger;

@RestController
public class PetController {

    private static final Logger log = Logger.getLogger(PetController.class.getName());

    private PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping("/Pet")
    public ResponseEntity<PetDto> createPet(@RequestBody PetDto petDto) {
        log.info("PetController request createPet");
        PetDto createdPet = petService.createPet(petDto);
        return ResponseEntity.created(URI.create("/Pet/1")).body(createdPet);
    }

    @DeleteMapping("/Pet/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        log.info("PetController request deletePet");
        petService.deletePetById(id);
        return ResponseEntity.noContent().build();
    }
}
