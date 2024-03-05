package com.example.personcrudapiunitanditegrationtest.controller;

import com.example.personcrudapiunitanditegrationtest.modele.dto.PersonDto;
import com.example.personcrudapiunitanditegrationtest.service.PersonService;
import lombok.AllArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequestMapping(value = "/person")
@RestController
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping(value = "/insert")
    public ResponseEntity<PersonDto> creat(@RequestBody PersonDto personDto) {

        PersonDto result = personService.create(personDto);
        if (result.getAge() == null || result.getName().isEmpty()) return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.status(201).body(result);

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


    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> update(@PathVariable("id") Long id, @RequestBody PersonDto personDto) throws NoSuchElementException {
        PersonDto result = personService.update(id,personDto);
        if (result==null)return ResponseEntity.notFound().build();
        return ResponseEntity.status(200).body(result);
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
