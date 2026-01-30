package com.SpringBootMVC.SpringBootMVC;

import com.SpringBootMVC.SpringBootMVC.Controllers.UserController;
import com.SpringBootMVC.SpringBootMVC.Exceptions.GlobalExceptionHandler;
import com.SpringBootMVC.SpringBootMVC.Services.UserService;
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

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCreateUser_Success() throws Exception {
        UserDto userDto = new UserDto(null, "Sasha", "sasha@example.com", 21, List.of());
        UserDto createdUser = new UserDto(1L, "Sasha", "sasha@example.com", 21, List.of());

        when(userService.createUser(any(UserDto.class))).thenReturn(createdUser);

        mockMvc.perform(post("/User")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Sasha"))
                .andExpect(jsonPath("$.email").value("sasha@example.com"));
    }

    @Test
    public void testGetUser_Success() throws Exception {
        Long userId = 1L;
        UserDto userDto = new UserDto(userId, "Sasha", "sasha@example.com", 21, List.of());

        when(userService.findUserById(userId)).thenReturn(userDto);

        mockMvc.perform(get("/User/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Sasha"));
    }

    @Test
    public void testGetUser_WhenUserNotFound() throws Exception {
        Long notExistUserId = 999_999L;

        when(userService.findUserById(notExistUserId)).thenThrow(new NoSuchElementException("User not found"));

        mockMvc.perform(get("/User/{id}", notExistUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        Long userId = 1L;
        UserDto userDto = new UserDto(userId, "Sasha Updated", "sasha.updated@example.com", 22, List.of());
        UserDto updatedUser = new UserDto(userId, "Sasha Updated", "sasha.updated@example.com", 22, List.of());

        when(userService.updateUser(eq(userId), any(UserDto.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/User/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sasha Updated"));
    }

    @Test
    public void testDeleteUser_Success() throws Exception {
        Long userId = 1L;

        doNothing().when(userService).deleteUserById(userId);

        mockMvc.perform(delete("/User/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
