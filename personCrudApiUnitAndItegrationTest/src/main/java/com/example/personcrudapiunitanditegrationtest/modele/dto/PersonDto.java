package com.example.personcrudapiunitanditegrationtest.modele.dto;

import com.example.personcrudapiunitanditegrationtest.base.BaseDto;
import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto implements BaseDto {
    private Long id;
    private String name;
    private Integer age;
    private String lastName;

    @Override
    public String toString() {
        return "PersonDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDto personDto = (PersonDto) o;
        return Objects.equals(id, personDto.id) && Objects.equals(name, personDto.name) && Objects.equals(age, personDto.age) && Objects.equals(lastName, personDto.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, lastName);
    }
}
