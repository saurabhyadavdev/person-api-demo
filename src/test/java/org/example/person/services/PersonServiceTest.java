package org.example.person.services;

import org.example.person.dto.PersonRequest;
import org.example.person.dto.PersonResponse;
import org.example.person.exceptions.PersonNotFoundException;
import org.example.person.models.Person;
import org.example.person.repositories.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    void findById_ShouldReturnPersonResponse() {
        Person person = new Person();
        person.setId(1L);
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAge(25);
        person.setFavouriteColor("Blue");

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        PersonResponse response = personService.findById(1L);
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getFirstName()).isEqualTo("John");
        assertThat(response.getLastName()).isEqualTo("Doe");
        assertThat(response.getAge()).isEqualTo(25);
        assertThat(response.getFavouriteColor()).isEqualTo("Blue");
    }

    @Test
    void findById_ShouldThrowWhenNotFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personService.findById(1L))
            .isInstanceOf(PersonNotFoundException.class)
            .hasMessage("Person not found with id: 1");
    }

    @Test
    void findAll_ShouldReturnListOfResponses() {
        Person p1 = new Person();
        p1.setId(1L);
        p1.setFirstName("John");
        p1.setLastName("Doe");
        p1.setAge(25);
        p1.setFavouriteColor("Blue");

        when(personRepository.findAll()).thenReturn(List.of(p1));

        List<PersonResponse> all = personService.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void create_ShouldSaveAndReturnResponse() {
        PersonRequest request = new PersonRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setAge(25);
        request.setFavouriteColor("Blue");

        Person savedPerson = new Person();
        savedPerson.setId(1L);
        savedPerson.setFirstName("John");
        savedPerson.setLastName("Doe");
        savedPerson.setAge(25);
        savedPerson.setFavouriteColor("Blue");

        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);

        PersonResponse response = personService.create(request);
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getFirstName()).isEqualTo("John");
    }

    @Test
    void update_ShouldModifyAndReturnResponse() {
        PersonRequest request = new PersonRequest();
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setAge(30);
        request.setFavouriteColor("Green");

        Person existing = new Person();
        existing.setId(1L);
        existing.setFirstName("John");
        existing.setLastName("Doe");
        existing.setAge(25);
        existing.setFavouriteColor("Blue");

        when(personRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(personRepository.save(any(Person.class))).thenAnswer(i -> i.getArgument(0));

        PersonResponse response = personService.update(1L, request);
        assertThat(response.getFirstName()).isEqualTo("Jane");
        assertThat(response.getAge()).isEqualTo(30);
        assertThat(response.getFavouriteColor()).isEqualTo("Green");
    }

    @Test
    void update_ShouldThrowWhenNotFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        PersonRequest request = new PersonRequest();
        assertThatThrownBy(() -> personService.update(1L, request))
            .isInstanceOf(PersonNotFoundException.class);
    }

    @Test
    void delete_ShouldRemoveWhenExists() {
        when(personRepository.existsById(1L)).thenReturn(true);

        personService.delete(1L);

        verify(personRepository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowWhenNotFound() {
        when(personRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> personService.delete(1L))
            .isInstanceOf(PersonNotFoundException.class);
        verify(personRepository, never()).deleteById(any());
    }
}
