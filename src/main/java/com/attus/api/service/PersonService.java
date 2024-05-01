package com.attus.api.service;

import com.attus.api.model.Person;
import com.attus.api.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository repository;

    public PersonService(PersonRepository personRepository) {
        this.repository = personRepository;
    }

    public Person save(Person request) {
        return repository.save(request);
    }

    public List<Person> findAll() {
        return repository.findAll();
    }

    public Person findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found!"));
    }

    public void deleteById(Long id) {
        repository.findById(id)
                .map(person -> {
                    repository.deleteById(id);
                    return true;
                })
                .orElseThrow(() -> new EntityNotFoundException("Person not found!"));
    }

    public Person update(Long id, Person request) {
        if (this.repository.existsById(id)) {
            Optional<Person> person = repository.findById(id);
            request.setId(id);
            request.getAddress().setId(person.get().getAddress().getId());
            return this.repository.save(request);
        }
        throw new EntityNotFoundException("Person not found!");
    }

}
