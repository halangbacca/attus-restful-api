package com.attus.api.controller;

import com.attus.api.mapper.PersonMapper;
import com.attus.api.model.Person;
import com.attus.api.record.request.PersonRequest;
import com.attus.api.record.response.PersonResponse;
import com.attus.api.service.PersonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonController {
    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<List<PersonResponse>> findAll() {

        List<PersonResponse> personResponse = service.findAll()
                .stream()
                .map(PersonMapper.INSTANCE::personToResponse).toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(personResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(PersonMapper.INSTANCE.personToResponse(service.findById(id)));
    }

    @PostMapping()
    public ResponseEntity<Object> save(@RequestBody @Valid PersonRequest request) {
        Person person = PersonMapper.INSTANCE.requestToPerson(request);
        return ResponseEntity.ok(service.save(person));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody PersonRequest request) {

        Person updatedPerson = service.update(id, PersonMapper.INSTANCE.requestToPerson(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PersonMapper.INSTANCE.personToResponse(updatedPerson));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable @NotNull Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
