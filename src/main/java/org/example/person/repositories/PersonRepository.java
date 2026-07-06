package org.example.person.repositories;

import org.example.person.models.Person;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PersonRepository {

    private final PersonJpaRepository jpaRepository;

    public PersonRepository(PersonJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    public Optional<Person> findById(Long id) {
        return jpaRepository.findById(id);
    }

    public List<Person> findAll() {
        return jpaRepository.findAll();
    }

    public Person save(Person person) {
        return jpaRepository.save(person);
    }

    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
