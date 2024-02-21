package com.example.personcrudapiunitanditegrationtest.mapper;

import com.example.personcrudapiunitanditegrationtest.base.BaseEntity;

public interface IbaseMapper<E extends BaseEntity,D> {
    E dtoToEntity(D dto);
    D entityToDto(E entity);
}
