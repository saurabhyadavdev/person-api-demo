package org.example.person.controllers;

import org.example.person.config.SecurityConfig;
import org.example.person.dto.PersonRequest;
import org.example.person.dto.PersonResponse;
import org.example.person.exceptions.GlobalExceptionHandler;
import org.example.person.exceptions.PersonNotFoundException;
import org.example.person.services.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    @Test
    @WithMockUser(roles = "USER")
    void getPersonById_ShouldReturn200() throws Exception {
        PersonResponse response = new PersonResponse(1L, "John", "Doe", 25, "Blue");
        when(personService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/persons/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
            .andExpect(jsonPath("$.age").value(25))
            .andExpect(jsonPath("$.favouriteColor").value("Blue"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getPersonById_NotFound_ShouldReturn404() throws Exception {
        when(personService.findById(1L)).thenThrow(new PersonNotFoundException(1L));

        mockMvc.perform(get("/api/v1/persons/1"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Person not found with id: 1"))
            .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllPersons_ShouldReturn200() throws Exception {
        PersonResponse p1 = new PersonResponse(1L, "John", "Doe", 25, "Blue");
        PersonResponse p2 = new PersonResponse(2L, "Jane", "Doe", 30, "Red");
        when(personService.findAll()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/v1/persons"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createPerson_ShouldReturn201() throws Exception {
        PersonResponse response = new PersonResponse(1L, "John", "Doe", 25, "Blue");
        when(personService.create(any(PersonRequest.class))).thenReturn(response);

        String requestBody = """
            {
                "firstName": "John",
                "lastName": "Doe",
                "age": 25,
                "favouriteColor": "Blue"
            }
            """;

        mockMvc.perform(post("/api/v1/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createPerson_WithInvalidBody_ShouldReturn400() throws Exception {
        String requestBody = """
            {
                "age": -1
            }
            """;

        mockMvc.perform(post("/api/v1/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updatePerson_ShouldReturn200() throws Exception {
        PersonResponse response = new PersonResponse(1L, "Jane", "Doe", 30, "Green");
        when(personService.update(eq(1L), any(PersonRequest.class))).thenReturn(response);

        String requestBody = """
            {
                "firstName": "Jane",
                "lastName": "Doe",
                "age": 30,
                "favouriteColor": "Green"
            }
            """;

        mockMvc.perform(put("/api/v1/persons/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deletePerson_ShouldReturn204() throws Exception {
        doNothing().when(personService).delete(1L);

        mockMvc.perform(delete("/api/v1/persons/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deletePerson_NotFound_ShouldReturn404() throws Exception {
        doThrow(new PersonNotFoundException(1L)).when(personService).delete(1L);

        mockMvc.perform(delete("/api/v1/persons/1"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Person not found with id: 1"))
            .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldReturn401WithoutAuth() throws Exception {
        mockMvc.perform(get("/api/v1/persons"))
            .andExpect(status().isUnauthorized());
    }
}
