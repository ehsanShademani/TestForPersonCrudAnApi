package com.example.personcrudapiunitanditegrationtest.service;

import com.example.personcrudapiunitanditegrationtest.mapper.PersonMapper;
import com.example.personcrudapiunitanditegrationtest.modele.dto.PersonDto;
import com.example.personcrudapiunitanditegrationtest.modele.entity.Person;
import com.example.personcrudapiunitanditegrationtest.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class PersonService  {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonService(PersonRepository personRepository,PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper=personMapper;
    }
    public PersonDto findById(Long id) throws RuntimeException {
        Person person = personRepository.findById(id).orElseThrow(RuntimeException::new);
        return personMapper.entityToDto(person);
    }


    public List<PersonDto> findAll() {
        return personRepository.findAll().stream().map(personMapper::entityToDto).collect(Collectors.toList());
    }


    public PersonDto create(PersonDto personDto) {
        Person entity = personMapper.dtoToEntity(personDto);
        return personMapper.entityToDto(personRepository.save(entity));
    }


    public PersonDto update(Long id,PersonDto personDto) throws RuntimeException {
        Optional<Person> person1 = personRepository.findById(id);
        if (person1.isPresent()) {
            Person existingPerson = person1.get();
            existingPerson.setAge(personDto.getAge());
            existingPerson.setName(personDto.getName());
            existingPerson.setLastName(personDto.getLastName());
            Person person =personRepository.save(existingPerson);
            return personMapper.entityToDto(person);
        }else throw new RuntimeException("person not found with id : "+ id);
    }
    public void deleteById(Long id) throws RuntimeException {

        personRepository.findById(id).orElseThrow(()->new RuntimeException());


        personRepository.deleteById(id);

    }
}
