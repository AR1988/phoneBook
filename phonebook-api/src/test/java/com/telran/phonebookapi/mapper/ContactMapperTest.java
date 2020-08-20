package com.telran.phonebookapi.mapper;

import com.telran.phonebookapi.dto.ContactDto;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContactMapperTest {

    ContactMapper contactMapper = new ContactMapper();

    //TODO
    @Test
    void ContactDto() {
        User user = new User("test@gmail.com", "112233");
        Contact contact = new Contact("Name", user);

        ContactDto contactDto = new ContactDto(contact.getId(), "Name", "LastName", "Description");

        ContactDto contactDtoMapped = contactMapper.mapContactToDto(contact);
        assertEquals(contactDto.id, contactDtoMapped.id);
        assertEquals(contactDto.firstName, contactDtoMapped.firstName);
        assertEquals(contactDto.lastName, contactDtoMapped.lastName);
        assertEquals(contactDto.description, contactDtoMapped.description);
    }
}
