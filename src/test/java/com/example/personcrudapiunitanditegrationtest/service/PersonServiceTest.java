package com.example.personcrudapiunitanditegrationtest.service;

import com.example.personcrudapiunitanditegrationtest.mapper.PersonMapper;
import com.example.personcrudapiunitanditegrationtest.modele.dto.PersonDto;
import com.example.personcrudapiunitanditegrationtest.modele.entity.Person;
import com.example.personcrudapiunitanditegrationtest.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
@ExtendWith(SpringExtension.class)
public class PersonServiceTest {
    @Mock
    PersonRepository personRepository;
    @Mock
    PersonMapper personMapper;
    @InjectMocks
    PersonService personService;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        personService = new PersonService(personRepository,personMapper);
    }
    @Test
    void testFindById() throws ChangeSetPersister.NotFoundException {
        //Given
        Person person =new Person(1L,"ehsan","shademani",27);

        PersonDto dto = new PersonDto(1L,"ehsan",27,"shademani");
        //when
        when(personMapper.entityToDto(any(Person.class))).thenReturn(dto);
        Mockito.when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));
        //then
        PersonDto found = personService.findById(person.getId());
        verify(personRepository).findById(person.getId());
        assertEquals("ehsan",found.getName());
        assertEquals(27,found.getAge());
        assertEquals("shademani",found.getLastName());
    }

    @Test
    void testDeleteById() {
        //Given
        Long id = 1L;
        Person person =new Person(id,"ehsan","shademani",27);


//        PersonDto personDto = new PersonDto(id,"ehsan",27,"shademani");
        //when
        Mockito.when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));
        doNothing().when(personRepository).deleteById(any(Long.class));

        personService.deleteById(person.getId());
        //then
        verify(personRepository).deleteById(any(Long.class));
    }

    @Test
    void testCreate() {
        //Given
        Long id = 1L;
        Person person = new Person(id,"ehsan","shademani",27);
        PersonDto dtoBase = new PersonDto(id,"ehsan",27,"shademani");
        //when
        Mockito.when(personMapper.entityToDto(any(Person.class))).thenReturn(dtoBase);
        Mockito.when(personMapper.dtoToEntity(any(PersonDto.class))).thenReturn(person);
        Mockito.when(personRepository.save(person)).thenReturn(person);
        personService.create(dtoBase);
        //then
        assertNotNull(dtoBase);
        assertEquals("ehsan",dtoBase.getName());
        assertEquals("shademani",dtoBase.getLastName());
    }

    @Test
    void testUpdate() throws ClassNotFoundException {
        //Given
        Long id = 1L;
        PersonDto dtoBase = new PersonDto(id,"ehsan",27,"shademani");
        Person person = new Person(id,"ehsan","shademani",27);
        //when
        when(personMapper.entityToDto(any(Person.class))).thenReturn(dtoBase);
        when(personRepository.save(person)).thenReturn(person);
        when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));

        //then
        PersonDto savePerson = personService.update(dtoBase.getId(),dtoBase);
        savePerson.setName("ehsan update");
        savePerson.setId(person.getId());
        savePerson.setAge(person.getAge());
        savePerson.setLastName(person.getLastName());
        personService.update(dtoBase.getId(),savePerson);
        assertEquals("ehsan update",savePerson.getName());

    }

    @Test
    void testFindAll() {
        //Given

//        List<Person> people = new ArrayList<>();
        List<PersonDto> result = new ArrayList<>();
//        Person person1 = new Person(1L,"ehsan","Shademani",27);
//        Person person2 = new Person(2L,"parsa","Shademani",16);
//        people.add(person1);
//        people.add(person2);
        PersonDto personDto = new PersonDto(1L,"ehsan",27,"Shademani");
        PersonDto personDto1 = new PersonDto(2L,"parsa",16,"Shademani");
        result.add(personDto);
        result.add(personDto1);

        //when

//        when(personMapper.entityToDto(any(Person.class))).thenReturn(personDto);
        Mockito.when(personService.findAll()).thenReturn(result);
//        Mockito.when(personRepository.findAll()).thenReturn(people);
        //then


        assertEquals(2,result.size());
        Assertions.assertEquals(result.get(0).getId(),1L);
        Assertions.assertEquals(result.get(0).getAge(),27);
        Assertions.assertEquals(result.get(0).getLastName(),"Shademani");
        Assertions.assertEquals(result.get(0).getName(),"ehsan");


        Assertions.assertEquals(result.get(1).getId(),2L);
        Assertions.assertEquals(result.get(1).getAge(),16);
        Assertions.assertEquals(result.get(1).getLastName(),"Shademani");
        Assertions.assertEquals(result.get(1).getName(),"parsa");
    }
    @Test
    void testIfTestWasNullReturnNull(){
        PersonDto personDto = new PersonDto();
        List<PersonDto> personDtos = new ArrayList<>();
        personDtos.add(personDto);
        Mockito.when(personService.findAll()).thenReturn(null);
    }
    @Test
    void testIfPersonIsNotPresentWithCurrentId() {
        //Given
        Long id = 2L;
        PersonDto dtoBase = new PersonDto(id,"ehsan",27,"shademani");
//        Person person = new Person(id,"ehsan","shademani",27);
        when(personRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        PersonDto updatePersonDto = personService.update(id,dtoBase);
        //then
            assertThrows(RuntimeException.class,()-> personService.update(updatePersonDto.getId(),updatePersonDto));

    }
    @Test
    void  testIfPersonHasntNameReturnException(){
        PersonDto personDto = new PersonDto(1L,null,27,"Shademani");

        assertThrows(RuntimeException.class,()->personService.create(personDto));

    }
    @Test
    void  testIfPersonHasntAgeReturnException(){
        PersonDto personDto = new PersonDto(1L,"Ehsan",null,"Shademani");
        assertNull(personService.create(personDto));
    }
}
