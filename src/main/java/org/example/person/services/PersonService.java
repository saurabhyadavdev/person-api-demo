package org.example.person.services;

import org.example.person.dto.PersonRequest;
import org.example.person.dto.PersonResponse;
import org.example.person.exceptions.PersonNotFoundException;
import org.example.person.models.Person;
import org.example.person.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonResponse findById(Long id) {
        Person person = personRepository.findById(id)
            .orElseThrow(() -> new PersonNotFoundException(id));
        return toResponse(person);
    }

    public List<PersonResponse> findAll() {
        return personRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    public PersonResponse create(PersonRequest request) {
        Person person = new Person();
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setAge(request.getAge());
        person.setFavouriteColor(request.getFavouriteColor());
        Person saved = personRepository.save(person);
        return toResponse(saved);
    }

    public PersonResponse update(Long id, PersonRequest request) {
        Person existing = personRepository.findById(id)
            .orElseThrow(() -> new PersonNotFoundException(id));
        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setAge(request.getAge());
        existing.setFavouriteColor(request.getFavouriteColor());
        Person saved = personRepository.save(existing);
        return toResponse(saved);
    }

    public void delete(Long id) {
        if (!personRepository.existsById(id)) {
            throw new PersonNotFoundException(id);
        }
        personRepository.deleteById(id);
    }

    private PersonResponse toResponse(Person person) {
        PersonResponse response = new PersonResponse();
        response.setId(person.getId());
        response.setFirstName(person.getFirstName());
        response.setLastName(person.getLastName());
        response.setAge(person.getAge());
        response.setFavouriteColor(person.getFavouriteColor());
        return response;
    }
}
