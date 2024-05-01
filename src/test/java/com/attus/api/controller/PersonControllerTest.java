package com.attus.api.controller;

import com.attus.api.model.Address;
import com.attus.api.model.Person;
import com.attus.api.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService service;

    @Test
    @DisplayName("When there is no record of people, an empty list must be returned")
    void noPersonFound() throws Exception {
        mockMvc.perform(get("/api/person")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(empty())));
    }

    @Test
    @DisplayName("When there are people registered, a list of people must be returned")
    void listAllPersons() throws Exception {
        var persons = List.of(
                new Person(1L, "Halan", new Date(), new Address()),
                new Person(2L, "Halan", new Date(), new Address())
        );
        Mockito.when(service.findAll()).thenReturn(persons);
        mockMvc.perform(get("/api/person")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(persons.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(persons.get(1).getName())));
    }

    @Test
    @DisplayName("When there is no person registered with the given id, an exception must be throw")
    void personNotFoundById() throws Exception {
        Mockito.when(service.findById(Mockito.anyLong())).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(get("/api/person/{id}", '1')
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("When there is a person registered with the given ID, the person must be returned")
    void personFound() throws Exception {
        Person person = new Person(1L, "Halan", new Date(), new Address());
        Mockito.when(service.findById(Mockito.anyLong())).thenReturn(person);
        mockMvc.perform(get("/api/person/{id}", "1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(person.getName())));
    }

    @Test
    @DisplayName("When registering a person with invalid data, errors must be returned")
    void invalidRegistration() throws Exception {
        Person request = new Person();
        String requestJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/person")
                        .content(requestJson)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest()); // 400
    }

    @Test
    @DisplayName("When an attempt is made to register a person and the data is valid, the person must be registered")
    void registerValidPerson() throws Exception {
        Person request = new Person(1L, "Halan", new Date(), new Address(1L, "Rua São Joaquim", "Itajaí", "SC", "88309-000", "137"));
        Mockito.when(service.save(Mockito.any(Person.class))).thenReturn(request);
        String requestJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/person")
                        .content(requestJson)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("When an attempt to delete a non-existent person, it must throw an exception")
    void deletePersonNotFound() throws Exception {
        Mockito.doThrow(EntityNotFoundException.class).when(service).deleteById(Mockito.anyLong());
        mockMvc.perform(delete("/api/person/{id}", "1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("When an attempt to delete a person with a valid id is made, it must be deleted")
    void deletePerson() throws Exception {
        mockMvc.perform(delete("/api/person/{id}", "1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("When an attempt to update a non-existent person, it must throw an exception")
    void invalidTaskUpdate() throws Exception {
        mockMvc.perform(put("/api/person/{id}", "1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When trying to update a person with invalid data, it should return errors")
    void updateInvalidPerson() throws Exception {
        Person request = new Person();
        String requestJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(put("/api/person/{id}", "1")
                        .content(requestJson)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When an attempt is made to update an existent person and the data is valid, the person must be registered")
    void updateValidPerson() throws Exception {
        Person request = new Person(1L, "Halan", new Date(), new Address(1L, "Rua São Joaquim", "Itajaí", "SC", "88309-000", "137"));
        String requestJson = objectMapper.writeValueAsString(request);
        request.setName("Teste");
        Mockito.when(service.update(Mockito.anyLong(), Mockito.any(Person.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(request).getBody());
        mockMvc.perform(put("/api/person/{id}", request.getId())
                        .content(requestJson)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address", is(notNullValue())))
                .andExpect(jsonPath("$.name", is("Teste")));
    }

}