package org.example.person.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse {

    private long id;
    private String firstName;
    private String lastName;
    private int age;
    private String favouriteColor;
}
