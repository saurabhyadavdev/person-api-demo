package org.example.person.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PersonRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Min(value = 1, message = "Age must be positive")
    private int age;

    @NotBlank(message = "Favourite color is required")
    private String favouriteColor;
}
