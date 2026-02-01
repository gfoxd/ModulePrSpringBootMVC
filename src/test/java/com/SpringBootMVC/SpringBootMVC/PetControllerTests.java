package com.SpringBootMVC.SpringBootMVC;

import com.SpringBootMVC.SpringBootMVC.Controllers.PetController;
import com.SpringBootMVC.SpringBootMVC.Exceptions.GlobalExceptionHandler;
import com.SpringBootMVC.SpringBootMVC.Services.PetService;
import com.SpringBootMVC.SpringBootMVC.Services.UserService;
import com.SpringBootMVC.SpringBootMVC.model.PetDto;
import com.SpringBootMVC.SpringBootMVC.model.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
@Import(GlobalExceptionHandler.class)
public class PetControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @MockBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCreatePet_WhenUserNotFound() throws Exception {
        Long notExistUserId = 999_999L;
        PetDto petDto = new PetDto(null, "Gav gav", notExistUserId);

        doThrow(NoSuchElementException.class)
                .when(petService)
                .createPet(petDto);

        mockMvc.perform(post("/Pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreatePet_Success() throws Exception {
        PetDto petDto = new PetDto(null, "Gav gav", 1L);
        PetDto createdPet = new PetDto(1L, "Gav gav", 1L);

        when(userService.findUserById(1L)).thenReturn(new UserDto(1L, "Sasha", "123123@gmail.com", 21, List.of()));
        when(petService.createPet(any(PetDto.class))).thenReturn(createdPet);

        mockMvc.perform(post("/Pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Gav gav"))
                .andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    public void testDeletePet_Success() throws Exception {
        Long petId = 1L;
        doNothing().when(petService).deletePetById(petId);

        mockMvc.perform(delete("/Pet/{id}", petId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
