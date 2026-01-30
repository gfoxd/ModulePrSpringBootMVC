package com.SpringBootMVC.SpringBootMVC.model;

import jakarta.validation.constraints.*;

public record PetDto(

        @Null
        Long id,

        @NotBlank
        @Size(max = 50)
        String name,

        @NotBlank
        @Min(1)
        Long userId
) {}
