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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest()
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class PersonControllerTest {
    @MockBean
    private PersonService personService;
    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private PersonRepository personRepository;


    @Autowired
    private MockMvc mockMvc;




        @Test
        public void testCreate() throws Exception {

            PersonDto personDto1 = new PersonDto(null, "ehsan", 27, "Shademani");
            Person person = new Person(null,"ehsan","shademani",27);
//        when(personMapper.entityToDto(person)).thenReturn(personDto);

             mockMvc.perform(post("/person")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(Objects.requireNonNull(objectToJeson(person))))
                    .andExpect(status().isCreated());
            personService.create(personDto1);
            personRepository.save(person);

            assertThat(personRepository.findById(person.getId())).isPresent()
                    .hasValueSatisfying((p)->assertThat(p).isEqualToComparingFieldByFieldRecursively(person));



        }



    @Test
    public void testFindById() throws Exception {
        Long id = 1L;
        Person person = new Person(id,"ehsan","shademani",27);

        PersonDto personDto = personMapper.entityToDto(person);
        personRepository.save(person);
        mockMvc.perform((get("/person/1")))
                .andExpect(status().isAccepted());
        assertThat(personRepository.findById(id)).isPresent()
                .hasValueSatisfying(p->assertThat(p).usingRecursiveComparison()
                        .isEqualTo(personDto));


    }
    @Test
    public void testFindAll() throws Exception {
        Person person = new Person(1L,"ehsan","shademani",27);
        Person person1 = new Person(2L,"parsa","shademani",17);

        List<Person> personList= personRepository.saveAll(Arrays.asList(person,person1));
        mockMvc.perform(get("/person/findAll"))
                .andExpect(status().isAccepted());
        assertThat(personRepository.findAll()).size()
                .isEqualTo(personList.size());

    }


    @Test
    public void testDeleteById() throws Exception {
        Long id = 1L;
        PersonDto personDto =new PersonDto(1L,"ehsan",27,"shademani");

        personRepository.save( personMapper.dtoToEntity(personDto));
        mockMvc.perform(delete("/person/1"))
                .andExpect(status().isOk());
        personRepository.deleteById(id);
        assertThat(personRepository.findById(id)).isEmpty();

    }
    @Test
    public void testUpdate() throws Exception {
        Long id = 1L;
        Person person = new Person(id,"ehsan","shademani",27);

        PersonDto personDto = personMapper.entityToDto(person);
        personService.create(personDto);
        personRepository.save(person);
        mockMvc.perform(put("/person/1").contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJeson(personDto))))
                .andExpect(status().isOk());
        personService.update(personDto.getId(),personDto );
        personRepository.save(person);
        Person updatePerson =personRepository.findById(person.getId()).orElse(null);
        updatePerson.setId(1L);
        assertThat(updatePerson).isNotNull();
        assertThat(updatePerson.getId()).isEqualTo(1L);
        assertThat(updatePerson.getName()).isEqualTo("ehsan");
        assertThat(updatePerson.getAge()).isEqualTo(27);
        assertThat(updatePerson.getLastName()).isEqualTo("shademani");

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
