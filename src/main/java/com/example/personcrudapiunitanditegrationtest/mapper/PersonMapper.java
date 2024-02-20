package com.example.personcrudapiunitanditegrationtest.mapper;

import com.example.personcrudapiunitanditegrationtest.modele.dto.PersonDto;
import com.example.personcrudapiunitanditegrationtest.modele.entity.Person;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper implements IbaseMapper<Person, PersonDto> {
    @Override
    public Person dtoToEntity(PersonDto dto) {
        Person entity = new Person();
        BeanUtils.copyProperties(dto,entity);
        return entity;
    }

    @Override
    public PersonDto entityToDto(Person entity) {
        PersonDto dto = new PersonDto();
        BeanUtils.copyProperties(entity,dto);
        return dto;
    }
}
