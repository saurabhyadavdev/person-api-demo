package org.example.person.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.example.person.dto.PersonRequest;
import org.example.person.dto.PersonResponse;
import org.example.person.services.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("{id}")
    @Operation(operationId = "findPersonById", summary = "Get person information by id")
    @ApiResponse(responseCode = "200", description = "Fetched info successfully", content = @Content(mediaType = "application/json"))
    public PersonResponse get(@PathVariable @Parameter(description = "Person identifier") Long id) {
        return personService.findById(id);
    }

    @GetMapping
    @Operation(operationId = "findPersons", summary = "Get all persons information")
    @ApiResponse(responseCode = "200", description = "Fetched info successfully", content = @Content(mediaType = "application/json"))
    public List<PersonResponse> list() {
        return personService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(operationId = "addPerson", summary = "Add new person")
    @ApiResponse(responseCode = "201", description = "Added person successfully", content = @Content(mediaType = "application/json"))
    public PersonResponse create(@Valid @RequestBody PersonRequest request) {
        return personService.create(request);
    }

    @PutMapping("{id}")
    @Operation(operationId = "editPerson", summary = "Update the person")
    @ApiResponse(responseCode = "200", description = "Updated person successfully", content = @Content(mediaType = "application/json"))
    public PersonResponse update(@PathVariable Long id, @Valid @RequestBody PersonRequest request) {
        return personService.update(id, request);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(operationId = "removePerson", summary = "Delete the person")
    @ApiResponse(responseCode = "204", description = "Removed person successfully", content = @Content(mediaType = "application/json"))
    public void delete(@PathVariable @Parameter(description = "Person identifier") Long id) {
        personService.delete(id);
    }
}
