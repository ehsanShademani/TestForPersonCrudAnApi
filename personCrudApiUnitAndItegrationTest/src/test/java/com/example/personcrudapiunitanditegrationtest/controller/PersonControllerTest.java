package com.example.personcrudapiunitanditegrationtest.controller;

import com.example.personcrudapiunitanditegrationtest.mapper.PersonMapper;
import com.example.personcrudapiunitanditegrationtest.modele.dto.PersonDto;
import com.example.personcrudapiunitanditegrationtest.modele.entity.Person;
import com.example.personcrudapiunitanditegrationtest.repository.PersonRepository;
import com.example.personcrudapiunitanditegrationtest.service.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {
    @MockBean
    private PersonService personService;
    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PersonRepository personRepository;
    @Captor
    private ArgumentCaptor<Person> personArgumentCaptor;

    private PersonController personController;
    @Autowired
    private MockMvc mockMvc;
    @Test
    public void testCreate() throws Exception {

//        PersonDto personDto = new PersonDto(null, "ehsan", 27, "Shademani");
        Person person = new Person(null,"ehsan","shademani",27);
//        when(personMapper.entityToDto(personEntity)).thenReturn(personDto);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJeson(person))))
                .andExpect(status().isCreated());
        personRepository.save(person);
        PersonDto personDto = personMapper.entityToDto(person);
        personService.create(personDto);
        assertThat(personRepository.findById(person.getId())).isPresent();


//        Person personValue= personMapper.dtoToEntity(personDto);

//        ArgumentCaptor<Person> personArgumentCapture =ArgumentCaptor.forClass(Person.class);
//        when((personRepository).save(personArgumentCaptor.capture())).thenReturn(personArgumentCapture.getValue());
//        assertThat(personRepository.findById(personArgumentCapture.capture().getId())).isPresent();
//        assertThat(personRepository.findById(person.getId())).isPresent()
//                .hasValueSatisfying(p->assertThat(p).usingRecursiveComparison()
//                        .isEqualTo(personDto));
    }
    @Test
    public void testFindById() throws Exception {
        Long id = 1l;
        Person person = new Person(id,"ehsan","shademani",27);
        PersonDto personDto = personMapper.entityToDto(person);
        personRepository.save(person);
//        when(personMapper.entityToDto(any(Person.class))).thenReturn(personDto);
        mockMvc.perform((get("/person/1")))
                .andExpect(status().isAccepted());
//        then(personRepository.save(personMapper.dtoToEntity(personDto)));
        assertThat(personRepository.findById(id)).isPresent()
                .hasValueSatisfying(p->assertThat(p).usingRecursiveComparison()
                        .isEqualTo(personDto));


    }
    @Test
    public void testFindAll() throws Exception {
        Person person = new Person(1l,"ehsan","shademani",27);
        Person person1 = new Person(2l,"parsa","shademani",17);
//        PersonDto personDto = personMapper.entityToDto(person);
//        PersonDto personDto1 = personMapper.entityToDto(person1);
//        List<PersonDto> personDtos=new ArrayList<>();
//        List<Person> people = new ArrayList<>();
        List<Person> personList= personRepository.saveAll(Arrays.asList(person,person1));
        mockMvc.perform(get("/person/findAll"))
                .andExpect(status().isAccepted());
        assertThat(personRepository.findAll()).size()
                .isEqualTo(personList.size());

    }


    @Test
    public void testDeleteById() throws Exception {
        Long id = 1l;
        Person person = new Person(id,"ehsan","shademani",27);
        PersonDto personDto =new PersonDto(1l,"ehsan",27,"shademani");

        personRepository.save( personMapper.dtoToEntity(personDto));
        mockMvc.perform(delete("/person/1"))
                .andExpect(status().isOk());
        personRepository.deleteById(id);
        assertThat(personRepository.findById(id)).isEmpty();

    }
    @Test
    public void testUpdate() throws Exception {
        Long id = 1l;
        Person person = new Person(1l,"ehsan","shademani",27);
        PersonDto personDto = personMapper.entityToDto(person);
        mockMvc.perform(put("/person/1").contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJeson(personDto))))
                .andExpect(status().isOk());
        personRepository.save(person);
        personRepository.findById(person.getId()).orElse(null);
        assertThat(person).isNotNull();
        assertThat(personRepository.findById(id)).isPresent()
                .hasValueSatisfying(p->assertThat(p).usingRecursiveComparison()
                        .isEqualTo(person));

    }
    public String objectToJeson(Object object){
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed to convert object to json");
            return null;
        }
    }
}
