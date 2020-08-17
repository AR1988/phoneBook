package com.telran.phonebookapi.mapper;

import com.telran.phonebookapi.dto.AddressDto;
import com.telran.phonebookapi.dto.ContactDto;
import com.telran.phonebookapi.dto.PhoneDto;
import com.telran.phonebookapi.model.Contact;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContactMapper {

    public ContactDto mapContactToDtoFull(Contact contact, List<PhoneDto> allPhonesByContact, List<AddressDto> allAddressesByContact, List<String> allEmails) {
        ContactDto contactDto =

                new ContactDto(contact.getId(),
                        contact.getFirstName(),
                        contact.getLastName(),
                        contact.getDescription(),
                        contact.getUser().getEmail(),
                        allPhonesByContact,
                        allAddressesByContact,
                        allEmails);
        System.out.println();

        return contactDto;
    }

    public ContactDto mapContactToDto(Contact contact) {
        return new ContactDto(contact.getId(),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getDescription(),
                contact.getUser().getEmail());
    }
}
