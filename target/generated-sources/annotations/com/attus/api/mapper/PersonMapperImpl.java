package com.attus.api.mapper;

import com.attus.api.model.Address;
import com.attus.api.model.Person;
import com.attus.api.record.request.AddressRequest;
import com.attus.api.record.request.PersonRequest;
import com.attus.api.record.response.AddressResponse;
import com.attus.api.record.response.PersonResponse;
import java.util.Date;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-01T02:21:47-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
public class PersonMapperImpl implements PersonMapper {

    @Override
    public Person requestToPerson(PersonRequest request) {
        if ( request == null ) {
            return null;
        }

        Person person = new Person();

        person.setName( request.name() );
        person.setBirthday( request.birthday() );
        person.setAddress( addressRequestToAddress( request.address() ) );

        return person;
    }

    @Override
    public PersonResponse personToResponse(Person person) {
        if ( person == null ) {
            return null;
        }

        String name = null;
        Date birthday = null;
        AddressResponse address = null;

        name = person.getName();
        birthday = person.getBirthday();
        address = addressToAddressResponse( person.getAddress() );

        PersonResponse personResponse = new PersonResponse( name, birthday, address );

        return personResponse;
    }

    protected Address addressRequestToAddress(AddressRequest addressRequest) {
        if ( addressRequest == null ) {
            return null;
        }

        Address address = new Address();

        address.setStreet( addressRequest.street() );
        address.setCity( addressRequest.city() );
        address.setState( addressRequest.state() );
        address.setZip( addressRequest.zip() );
        address.setNumber( addressRequest.number() );

        return address;
    }

    protected AddressResponse addressToAddressResponse(Address address) {
        if ( address == null ) {
            return null;
        }

        String street = null;
        String city = null;
        String state = null;
        String zip = null;
        String number = null;

        street = address.getStreet();
        city = address.getCity();
        state = address.getState();
        zip = address.getZip();
        number = address.getNumber();

        AddressResponse addressResponse = new AddressResponse( street, city, state, zip, number );

        return addressResponse;
    }
}
