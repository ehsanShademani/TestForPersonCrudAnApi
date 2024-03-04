package com.example.personcrudapiunitanditegrationtest.controller;

import com.example.personcrudapiunitanditegrationtest.modele.dto.PersonDto;
import com.example.personcrudapiunitanditegrationtest.modele.entity.Person;
import com.example.personcrudapiunitanditegrationtest.repository.PersonRepository;
import com.example.personcrudapiunitanditegrationtest.service.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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

import static java.lang.Character.getType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

import org.springframework.http.MediaType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class PersonControllerTest {
    @Autowired
    MockMvc mockMvc;
    static ObjectMapper jsonObjectMapper = new ObjectMapper();
    @Autowired
    PersonController personController;

    private PersonDto creatPersonTest() {
        return new PersonDto(1L, "Ehsan", 27, "Shademani");

    }

    private MockHttpServletRequestBuilder putRequestBuilder(PersonDto personDto) throws JsonProcessingException {

        return MockMvcRequestBuilders.put("/person/{id}", personDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObjectMapper.writeValueAsString(personDto));
    }

    private MockHttpServletRequestBuilder createRequestBuilder(PersonDto personDto) throws JsonProcessingException {

        return MockMvcRequestBuilders.post("/person", personDto)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObjectMapper.writeValueAsString(personDto));
    }

    private MockHttpServletRequestBuilder getByIdRequestBuilder(Long id) throws JsonProcessingException {

        return MockMvcRequestBuilders.get("/person/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObjectMapper.writeValueAsString(id));
    }

    private MockHttpServletRequestBuilder getAllRequestBuilder() throws JsonProcessingException {

        return MockMvcRequestBuilders.get("/person/findAll")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObjectMapper.writeValueAsString(any(PersonDto.class)));
    }

    private MockHttpServletRequestBuilder deleteRequestBuilder(Long id) throws JsonProcessingException {

        return MockMvcRequestBuilders.delete("/person/{id}", id);
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON);
//                .content(jsonObjectMapper.writeValueAsString(personDto));
    }

    private MvcResult mockResult(Integer statusCode, MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(requestBuilder).andExpect(status().is(statusCode)).andReturn();
    }

    @Test
    @Rollback
    public void creatTest() throws Exception {
        PersonDto personDto = creatPersonTest();
        personDto.setId(null);
        jsonObjectMapper.findAndRegisterModules();
        MockHttpServletRequestBuilder requestBuilder = createRequestBuilder(personDto);
        MvcResult result = mockResult(201, requestBuilder);
        String responseContent = result.getResponse().getContentAsString();
        Person savedPerson = jsonObjectMapper.readValue(responseContent, Person.class);
        assertNotNull(savedPerson.getId());
        assertEquals(savedPerson.getName(), personDto.getName());
        assertEquals(savedPerson.getAge(), personDto.getAge());
        assertEquals(savedPerson.getLastName(), personDto.getLastName());
    }


    @Test
    @Rollback
    public void updateTest() throws Exception {
        PersonDto personDto = creatPersonTest();
        ResponseEntity<PersonDto> savedPerson =
                personController.creat(new PersonDto(personDto.getId(), personDto.getName(), personDto.getAge(), personDto.getLastName()));

        PersonDto savedPersonUpdate = savedPerson.getBody();//update age
        assert savedPersonUpdate != null;
        savedPersonUpdate.setAge(30);
        MockHttpServletRequestBuilder requestBuilder = putRequestBuilder(savedPersonUpdate);
        MvcResult result = mockResult(200, requestBuilder);
        String response = result.getResponse().getContentAsString();

        PersonDto updatePerson = jsonObjectMapper.readValue(response, PersonDto.class);
        assertNotNull(updatePerson.getId());
        assertEquals(updatePerson.getName(), savedPersonUpdate.getName());
        assertEquals(updatePerson.getLastName(), savedPersonUpdate.getLastName());
        assertEquals(updatePerson.getAge(), savedPersonUpdate.getAge());

    }

    @Test
    @Rollback
    public void ifPersonWithThatIdWasNotExistYouCantUpdate() throws Exception {
        PersonDto personDto = creatPersonTest();
        Long invalidId = 870789L;
        MockHttpServletRequestBuilder requestBuilderGet = getByIdRequestBuilder(invalidId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult Getresult = mockMvc.perform(requestBuilderGet).andExpect(status().isNotFound())
                .andReturn();

        MockHttpServletRequestBuilder requestBuilder = putRequestBuilder(personDto)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Rollback
    public void getByIdTest() throws Exception {
        PersonDto personDto = creatPersonTest();
        ResponseEntity<PersonDto> savedPerson = personController.creat
                (new PersonDto(personDto.getId(), personDto.getName(), personDto.getAge(), personDto.getLastName()));
        PersonDto foundSavedPerson = savedPerson.getBody();
        MockHttpServletRequestBuilder requestBuilder = getByIdRequestBuilder(foundSavedPerson.getId());
        MvcResult result = mockResult(202, requestBuilder);
        String response = result.getResponse().getContentAsString();
        PersonDto foundPerson = jsonObjectMapper.readValue(response, PersonDto.class);
        assertNotNull(foundPerson.getId());
        assertEquals(foundPerson.getName(), foundSavedPerson.getName());
        assertEquals(foundPerson.getLastName(), foundSavedPerson.getLastName());
        assertEquals(foundPerson.getAge(), foundSavedPerson.getAge());


    }

    @Test
    public void ifPersonWithThatIdWasNotExistYouCantGet() throws Exception {
        Long unValidId = 33224342234L;
        MockHttpServletRequestBuilder requestBuilder = getByIdRequestBuilder(unValidId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Rollback
    public void getAllPerson() throws Exception {
        PersonDto personDto = creatPersonTest();
        ResponseEntity<PersonDto> savedPerson = personController.creat
                (new PersonDto(personDto.getId(), personDto.getName(), personDto.getAge(), personDto.getLastName()));
        PersonDto foundSavedPerson = savedPerson.getBody();
        List<PersonDto> personDtoList = new ArrayList<>();
        personDtoList.add(foundSavedPerson);
        MockHttpServletRequestBuilder requestBuilder = getAllRequestBuilder();
        MvcResult result = mockResult(202, requestBuilder);
        String response = result.getResponse().getContentAsString();
        List<PersonDto> foundPersonDtos =
                jsonObjectMapper.readValue(response, new TypeReference<List<PersonDto>>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        });
        assertFalse(foundPersonDtos.isEmpty());
        assertTrue(foundPersonDtos.stream().anyMatch(p -> p.getId().equals(savedPerson.getBody().getId())));

    }
//    public void testIfAntPersonWasntExistReturnNothing() throws Exception {
//        MockHttpServletRequestBuilder requestBuilder = getAllRequestBuilder();
//        MvcResult result = mockResult(200,requestBuilder);
//        String response = result.getResponse().getContentAsString();
//        List<PersonDto> foundPersonDtos = jsonObjectMapper.readValue(response, new TypeReference<List<PersonDto>>() {
//            @Override
//            public Type getType() {
//                return super.getType();
//            }
//        });
//        assertTrue(foundPersonDtos.isEmpty());
//    }


    @Test
    @Rollback
    public void deletePersonTest() throws Exception {
        PersonDto personDto = creatPersonTest();
        ResponseEntity<PersonDto> savedPerson = personController.creat
                (new PersonDto(personDto.getId(), personDto.getName(), personDto.getAge(), personDto.getLastName()));
        PersonDto foundSavedPerson = savedPerson.getBody();
        MockHttpServletRequestBuilder requestBuilder = deleteRequestBuilder(foundSavedPerson.getId());
        MvcResult result = mockResult(204, requestBuilder);
        MockHttpServletRequestBuilder getByIdRequestBuilder = getByIdRequestBuilder(foundSavedPerson.getId());
        MvcResult getResult = mockResult(404, getByIdRequestBuilder);


    }

    @Test
    public void ifPersonIdWasNotExistYouCantDelete() throws Exception {
        Long notValidId = (long) Math.random();
        MockHttpServletRequestBuilder requestBuilder = deleteRequestBuilder(notValidId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isNotFound())
                .andReturn();
    }
}
