package com.SpringBootMVC.SpringBootMVC.model;

import jakarta.validation.constraints.*;

import java.util.List;

public record UserDto(

        @Null
        Long id,

        @NotBlank
        @Size(max = 50)
        String name,

        @Email
        String email,

        Integer age,

        @Null
        List<PetDto> pets
) {}
