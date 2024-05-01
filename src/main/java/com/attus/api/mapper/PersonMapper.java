package com.attus.api.mapper;

import com.attus.api.model.Person;
import com.attus.api.record.request.PersonRequest;
import com.attus.api.record.response.PersonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    Person requestToPerson(PersonRequest request);

    PersonResponse personToResponse(Person person);
}
