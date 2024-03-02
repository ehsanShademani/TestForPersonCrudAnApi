package com.example.personcrudapiunitanditegrationtest.controller;

import com.example.personcrudapiunitanditegrationtest.modele.dto.PersonDto;
import com.example.personcrudapiunitanditegrationtest.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequestMapping("/person")
@RestController

public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getById(@PathVariable Long id) {


            var result = personService.findById(id);
            if(result == null) return ResponseEntity.notFound().build();
            else
                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body(result);

    }


    @GetMapping("/findAll")
    public ResponseEntity<List<PersonDto>> getAllPerson() {
        var result = personService.findAll();
        if (result==null)return ResponseEntity.notFound().build();
        else
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }

    @PostMapping()
    public ResponseEntity creat(@RequestBody PersonDto personDto) {
        return ResponseEntity.status(201).body(personService.create(personDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody PersonDto personDto) throws NoSuchElementException {
        return ResponseEntity.status(200).body(personService.update(id, personDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) throws NoSuchElementException {
        Optional<PersonDto> optionalPersonDto= Optional.ofNullable(personService.findById(id));

            if (optionalPersonDto.isPresent()){

                personService.deleteById(id);
                return ResponseEntity.noContent().build();
            }else return ResponseEntity.notFound().build();


    }
}
