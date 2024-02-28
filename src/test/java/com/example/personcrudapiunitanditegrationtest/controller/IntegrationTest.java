//package com.example.personcrudapiunitanditegrationtest.controller;
//
//import com.example.personcrudapiunitanditegrationtest.modele.dto.PersonDto;
//import com.example.personcrudapiunitanditegrationtest.modele.entity.Person;
//import com.example.personcrudapiunitanditegrationtest.repository.PersonRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
//import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
//import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class IntegrationTest {
//
//
//
//        @Autowired
//        private MockMvc mockMvc;
//
//        @Autowired
//        private ObjectMapper objectMapper;
//
//        @Autowired
//        private PersonRepository personRepository;
//
//        @Test
//        public void testCreatePerson() throws Exception {
//            // Create a new person
//            PersonDto personDTO = new PersonDto();
//            personDTO.setName("Ehsan");
//            personDTO.setLastName("Shademani");
//            personDTO.setAge(25);
//
//            String jsonRequest = objectMapper.writeValueAsString(personDTO);
//
//            mockMvc.perform(post("/person")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(jsonRequest))
//                    .andExpect(status().isCreated());
//
//            // Verify that the person was saved in the repository
//            List<Person> persons = personRepository.findAll();
//            assertThat(persons).isNotEmpty();
//            assertThat(persons.get(0).getName()).isEqualTo("Alice");
//        }
//
//        @Test
//        public void testGetAllPersons() throws Exception {
//            // Save a person to the repository for testing
//            Person person = new Person();
//            person.setName("Ehsan");
//            person.setLastName("Shdemani");
//            person.setAge(30);
//            personRepository.save(person);
//
//            mockMvc.perform(get("/person/findAll"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$", hasSize(1)))
//                    .andExpect(jsonPath("$[0].name", is("Bob")));
//        }
//
//        @Test
//        public void testUpdatePerson() throws Exception {
//            // Save a person to the repository for testing
//            Person person = new Person();
//            person.setName("Ehsan");
//            person.setLastName("Shademani");
//            person.setAge(35);
//            personRepository.save(person);
//
//            PersonDto updatedPersonDTO = new PersonDto();
//            updatedPersonDTO.setName("Ehsan Updated");
//            updatedPersonDTO.setLastName("Shademani Updated");
//            updatedPersonDTO.setAge(40);
//
//            String jsonRequest = objectMapper.writeValueAsString(updatedPersonDTO);
//
//            mockMvc.perform(put("/person/{id}", person.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(jsonRequest))
//                    .andExpect(status().isOk());
//
//            // Verify that the person was updated in the repository
//            Person updatedPerson = personRepository.findById(person.getId()).orElse(null);
//            assertThat(updatedPerson).isNotNull();
//            assertThat(updatedPerson.getName()).isEqualTo("Charlie Updated");
//        }
//
//        @Test
//        public void testDeletePerson() throws Exception {
//            // Save a person to the repository for testing
//            Person person = new Person();
//            person.setName("Ehsan");
//            person.setLastName("Shademani");
//            person.setAge(45);
//            personRepository.save(person);
//
//            mockMvc.perform(delete("/person/{id}", person.getId()))
//                    .andExpect(status().isOk());
//
//            // Verify that the person was deleted from the repository
//            assertThat(personRepository.findById(person.getId())).isEmpty();
//        }
//    }
//
//
//}
