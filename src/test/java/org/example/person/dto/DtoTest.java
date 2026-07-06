package org.example.person.dto;

import org.example.person.exceptions.ErrorResponse;
import org.example.person.exceptions.PersonNotFoundException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DtoTest {

    @Test
    void personRequestShouldHaveNoArgConstructor() {
        PersonRequest request = new PersonRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setAge(25);
        request.setFavouriteColor("Blue");

        assertThat(request.getFirstName()).isEqualTo("John");
        assertThat(request.getLastName()).isEqualTo("Doe");
        assertThat(request.getAge()).isEqualTo(25);
        assertThat(request.getFavouriteColor()).isEqualTo("Blue");
    }

    @Test
    void personResponseShouldHaveAllArgsConstructor() {
        PersonResponse response = new PersonResponse(1L, "John", "Doe", 25, "Blue");

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getFirstName()).isEqualTo("John");
        assertThat(response.getLastName()).isEqualTo("Doe");
        assertThat(response.getAge()).isEqualTo(25);
        assertThat(response.getFavouriteColor()).isEqualTo("Blue");
    }

    @Test
    void personResponseShouldHaveNoArgConstructor() {
        PersonResponse response = new PersonResponse();
        response.setId(1L);

        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    void personNotFoundExceptionShouldContainIdInMessage() {
        PersonNotFoundException ex = new PersonNotFoundException(42L);
        assertThat(ex.getMessage()).contains("42");
    }

    @Test
    void errorResponseShouldHaveErrorAndStatus() {
        ErrorResponse error = new ErrorResponse("Not found", 404);
        assertThat(error.error()).isEqualTo("Not found");
        assertThat(error.status()).isEqualTo(404);
    }
}
