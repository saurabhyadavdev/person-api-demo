package org.example.person.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.person.models.Person;
import org.example.person.repositories.PersonRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    @Autowired
    private PersonRepository repository;

    @GetMapping("{id}")
    @Operation(operationId = "findPersonById", summary = "Get person information by id")
    @ApiResponse(responseCode = "200", description = "Fetched info successfully", content = @Content(mediaType = "application/json"))
    public Person get(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @GetMapping
    @Operation(operationId = "findPersons", summary = "Get all persons information")
    @ApiResponse(responseCode = "200", description = "Fetched info successfully", content = @Content(mediaType = "application/json"))
    public List<Person> list() {
        return repository.findAll();
    }

    @PostMapping
    @Operation(operationId = "addPerson", summary = "Add new person")
    @ApiResponse(responseCode = "201", description = "Added person successfully", content = @Content(mediaType = "application/json"))
    public Person create(@RequestBody final Person person) {
        System.out.println(person);
        return repository.save(person);
    }

    @PutMapping("{id}")
    @Operation(operationId = "editPerson", summary = "Update the person")
    @ApiResponse(responseCode = "200", description = "Updated person successfully", content = @Content(mediaType = "application/json"))
    public void update(@PathVariable Long id, @RequestBody final Person person) {
        Person existingPerson = repository.findById(id).orElse(null);
        BeanUtils.copyProperties(person, existingPerson, "id");
        repository.save(person);
    }

    @DeleteMapping("{id}")
    @Operation(operationId = "removePerson", summary = "Delete the person")
    @ApiResponse(responseCode = "200", description = "Removed person successfully", content = @Content(mediaType = "application/json"))
    public void delete(@PathVariable @Parameter(description = "Person identifier") Long id) {
        Person existingPerson = repository.findById(id).orElse(null);
        repository.deleteById(id);
    }

}
