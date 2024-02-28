package com.example.personcrudapiunitanditegrationtest.controller;

import com.example.personcrudapiunitanditegrationtest.modele.dto.PersonDto;
import com.example.personcrudapiunitanditegrationtest.modele.entity.Person;
import com.example.personcrudapiunitanditegrationtest.repository.PersonRepository;
import com.example.personcrudapiunitanditegrationtest.service.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class PersonControllerTest {
    @Autowired
    MockMvc mockMvc;
    static ObjectMapper jsonObjectMapper= new ObjectMapper();
    @Autowired
    PersonService personService;
    @BeforeAll
    public void setUp(){
        MockitoAnnotations.initMocks(this);

    }
    private PersonDto creatPersonTest(){
       return new  PersonDto(1L,"Ehsan",27,"Shademani");

    }
    private MockHttpServletRequestBuilder putRequestBuilder(PersonDto personDto) throws JsonProcessingException {

        return MockMvcRequestBuilders.put("/person/{id}",personDto)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObjectMapper.writeValueAsString(personDto));
    }
    private MockHttpServletRequestBuilder createRequestBuilder(PersonDto personDto) throws JsonProcessingException {

        return MockMvcRequestBuilders.post("/person",personDto)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObjectMapper.writeValueAsString(personDto));
    }

    private MockHttpServletRequestBuilder getByIdRequestBuilder(PersonDto personDto) throws JsonProcessingException {

        return MockMvcRequestBuilders.get("/person/{id}",personDto)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObjectMapper.writeValueAsString(personDto));
    }
    private MockHttpServletRequestBuilder getAllRequestBuilder(PersonDto personDto) throws JsonProcessingException {

        return MockMvcRequestBuilders.get("/person/findAll",personDto)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObjectMapper.writeValueAsString(personDto));
    }
    private MockHttpServletRequestBuilder deleteRequestBuilder(PersonDto personDto) throws JsonProcessingException {

        return MockMvcRequestBuilders.delete("/person/{id}",personDto)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObjectMapper.writeValueAsString(personDto));
    }
    private MvcResult mockResult(Integer statusCode,MockHttpServletRequestBuilder requestBuilder) throws Exception {
       return mockMvc.perform(requestBuilder).andExpect(status().is(statusCode)).andReturn();
    }
    @Test
    @Rollback
    @DisplayName("should return person with 201 status code")
    public void creatTest() throws Exception {
        PersonDto personDto = creatPersonTest();
        jsonObjectMapper.findAndRegisterModules();
        MockHttpServletRequestBuilder requestBuilder = createRequestBuilder(personDto);
        MvcResult result = mockResult(201,requestBuilder);
        String responseContent = result.getResponse().getContentAsString();
        Person savedPerson=jsonObjectMapper.readValue(responseContent, Person.class);
        assertNotNull(savedPerson.getId());
        assertEquals(savedPerson.getName(),personDto.getName());
        assertEquals(savedPerson.getAge(),personDto.getAge());
        assertEquals(savedPerson.getLastName(),personDto.getLastName());
    }
    @Test
    @Rollback
    @DisplayName("should update person with 200 status")
    public void updateTest() throws Exception {
        PersonDto personDto = creatPersonTest();
        PersonDto savedPerson =
                personService.create(new PersonDto(personDto.getId(),personDto.getName(),personDto.getAge(), personDto.getLastName()));

        savedPerson.setAge(30);//update age







        MockHttpServletRequestBuilder requestBuilder = putRequestBuilder(savedPerson);
        MvcResult result = mockResult(200,requestBuilder);
        String response = result.getResponse().getContentAsString();

        PersonDto updatePerson = jsonObjectMapper.readValue(response,PersonDto.class);
        assertNotNull(updatePerson.getId());
        assertEquals(updatePerson.getName(),savedPerson.getName());
        assertEquals(updatePerson.getLastName(),savedPerson.getLastName());
        assertEquals(updatePerson.getAge(),savedPerson.getAge());

    }


}
