package com.attus.api.service;

import com.attus.api.model.Address;
import com.attus.api.model.Person;
import com.attus.api.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    @Mock
    private PersonRepository repository;

    @InjectMocks
    private PersonService service;

    @Test
    @DisplayName("When there is a person registered with the given ID, the person must be returned")
    void personFound() {
        Long id = 1L;

        Person person = new Person();
        person.setId(1L);

        Mockito.when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(person));

        Person result = service.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    @DisplayName("When there is no person registered with the given id, an exception must be throw")
    void personNotFoundById() {
        assertThrows(EntityNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    @DisplayName("When there are people registered, a list of people must be returned")
    void listAllPersons() {
        List<Person> persons = List.of(
                new Person(1L, "Halan", new Date(), new Address()),
                new Person(2L, "Halan", new Date(), new Address())
        );

        Mockito.when(repository.findAll()).thenReturn(persons);

        List<Person> list = service.findAll();

        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals(persons.size(), list.size());
    }

    @Test
    @DisplayName("When there is no record of people, an empty list must be returned")
    void noPersonFound() {
        List<Person> list = service.findAll();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("When an attempt is made to register a person and the data is valid, the person must be registered")
    void registerValidPerson() {
        Person person = new Person(1L, "Halan", new Date(), new Address());

        Mockito.when(repository.save(Mockito.any(Person.class)))
                .thenReturn(person);

        Person result = service.save(person);

        assertNotNull(result);
        assertEquals(person.getId(), result.getId());
    }

    @Test
    @DisplayName("When an attempt to delete a person with a valid id is made, it must be deleted")
    void deletePerson() {
        Person person = new Person(1L, "Halan", new Date(), new Address());

        Mockito.when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(person));

        assertDoesNotThrow(() -> service.deleteById(1L));
    }

    @Test
    @DisplayName("When an attempt to delete a non-existent person, it must throw an exception")
    void deletePersonNotFound() {
        assertThrows(EntityNotFoundException.class, () -> service.deleteById(1L));
    }

    @Test
    @DisplayName("When an attempt is made to update an existent person and the data is valid, the person must be registered")
    void updateValidPerson() {
        Person person = new Person(1L, "Halan", new Date(), new Address());

        Mockito.when(repository.existsById(1L))
                .thenReturn(true);

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(person));

        Mockito.when(repository.save(Mockito.any(Person.class)))
                .thenReturn(person);

        Person result = service.update(1L, person);

        assertDoesNotThrow(() -> service.update(1L, person));
        assertNotNull(result);
        assertEquals(person.getId(), result.getId());
    }

    @Test
    @DisplayName("When an attempt to update a non-existent person, it must throw an exception")
    void updatePersonNotFound() {
        Person person = new Person(1L, "Halan", new Date(), new Address());
        assertThrows(EntityNotFoundException.class, () -> service.update(2L, person));
    }

}