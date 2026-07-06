package org.example.person.repositories;

import org.example.person.models.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(statements = "DELETE FROM person", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PersonRepositoryTest {

    @Autowired
    private PersonJpaRepository personJpaRepository;

    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personRepository = new PersonRepository(personJpaRepository);
    }

    @Test
    void shouldSaveAndFindById() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAge(25);
        person.setFavouriteColor("Blue");

        Person saved = personRepository.save(person);
        assertThat(saved.getId()).isGreaterThan(0);

        Optional<Person> found = personRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("John");
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        Optional<Person> found = personRepository.findById(999L);
        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindAll() {
        Person p1 = new Person();
        p1.setFirstName("A");
        p1.setLastName("B");
        p1.setAge(20);
        p1.setFavouriteColor("Red");
        personJpaRepository.save(p1);

        Person p2 = new Person();
        p2.setFirstName("C");
        p2.setLastName("D");
        p2.setAge(30);
        p2.setFavouriteColor("Blue");
        personJpaRepository.save(p2);

        List<Person> all = personRepository.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    void shouldDeleteById() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAge(25);
        person.setFavouriteColor("Blue");
        Person saved = personJpaRepository.save(person);

        personRepository.deleteById(saved.getId());

        assertThat(personJpaRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void shouldCheckExists() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAge(25);
        person.setFavouriteColor("Blue");
        Person saved = personJpaRepository.save(person);

        assertThat(personRepository.existsById(saved.getId())).isTrue();
        assertThat(personRepository.existsById(999L)).isFalse();
    }
}
