package com.SpringBootMVC.SpringBootMVC.Exceptions;

import java.time.LocalDateTime;

public record ErrorMessageResponse(
        String message,
        String detailedMessage,
        LocalDateTime dateTime
) {}
