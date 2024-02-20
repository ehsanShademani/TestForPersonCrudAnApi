package com.example.personcrudapiunitanditegrationtest.controller;

import com.example.personcrudapiunitanditegrationtest.modele.dto.PersonDto;
import com.example.personcrudapiunitanditegrationtest.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/person")
@RestController
@AllArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getById(@PathVariable Long id) throws RuntimeException {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(personService.findById(id));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<PersonDto>> getAllPerson() {

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(personService.findAll());
    }

    @PostMapping()
    public ResponseEntity creat(@RequestBody PersonDto personDto) {
        return ResponseEntity.status(201).body(personService.create(personDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody PersonDto personDto) throws RuntimeException {
        return ResponseEntity.status(200).body(personService.update(id, personDto));
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Long id) {
        personService.deleteById(id);
    }
}
