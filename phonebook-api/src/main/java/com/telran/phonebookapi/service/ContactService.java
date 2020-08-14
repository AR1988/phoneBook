package com.telran.phonebookapi.service;

import com.telran.phonebookapi.dto.AddressDto;
import com.telran.phonebookapi.dto.ContactDto;
import com.telran.phonebookapi.dto.PhoneDto;
import com.telran.phonebookapi.dto.UserEmailDto;
import com.telran.phonebookapi.mapper.AddressMapper;
import com.telran.phonebookapi.mapper.ContactMapper;
import com.telran.phonebookapi.mapper.PhoneMapper;
import com.telran.phonebookapi.model.Address;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.model.Phone;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistance.IAddressRepository;
import com.telran.phonebookapi.persistance.IContactRepository;
import com.telran.phonebookapi.persistance.IPhoneRepository;
import com.telran.phonebookapi.persistance.IUserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {

    static final String CONTACT_DOES_NOT_EXIST = "Error! This contact doesn't exist in our DB";

    IUserRepository userRepository;
    IContactRepository contactRepository;
    IAddressRepository addressRepository;
    IPhoneRepository phoneRepository;
    ContactMapper contactMapper;
    AddressMapper addressMapper;
    PhoneMapper phoneMapper;

    public ContactService(IUserRepository userRepository, IContactRepository contactRepository, IAddressRepository addressRepository, IPhoneRepository phoneRepository, ContactMapper contactMapper, AddressMapper addressMapper, PhoneMapper phoneMapper) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
        this.addressRepository = addressRepository;
        this.phoneRepository = phoneRepository;
        this.contactMapper = contactMapper;
        this.addressMapper = addressMapper;
        this.phoneMapper = phoneMapper;
    }

    public void add(ContactDto contactDto) {
        User user = userRepository.findById(contactDto.userId).orElseThrow(() -> new EntityNotFoundException(UserService.USER_DOES_NOT_EXIST));
        Contact contact = new Contact(contactDto.firstName, user);
        contact.setLastName(contactDto.lastName);
        contact.setDescription(contactDto.description);
        contactRepository.save(contact);
    }

    public ContactDto getById(int id) {
        Contact contact = contactRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(CONTACT_DOES_NOT_EXIST));
        return contactMapper.mapContactToDto(contact);
    }

    public ContactDto getByIdFullDetails(int id) {
        Contact contact = contactRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(CONTACT_DOES_NOT_EXIST));
        ContactDto contactDto = contactMapper.mapContactToDtoFull(contact, getAllPhonesByContact(contact), getAllAddressesByContact(contact), getAllEmails(contact));

        contactDto.addresses = contact.getAddresses().stream()
                .map(addressMapper::mapAddressToDto)
                .collect(Collectors.toList());

        contactDto.phoneNumbers = contact.getPhoneNumbers().stream()
                .map(phoneMapper::mapPhoneToDto)
                .collect(Collectors.toList());

        contactDto.emails = contact.getEmails();

        return contactDto;
    }

    public void editAllFields(ContactDto contactDto) {
        Contact contact = contactRepository.findById(contactDto.id).orElseThrow(() -> new EntityNotFoundException(CONTACT_DOES_NOT_EXIST));
        contact.setFirstName(contactDto.firstName);
        contact.setLastName(contactDto.lastName);
        contact.setDescription(contactDto.description);
        contactRepository.save(contact);
    }

    public void removeById(int id) {
        contactRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(CONTACT_DOES_NOT_EXIST));
        contactRepository.deleteById(id);
    }

    public List<ContactDto> getAllContactsByUserId(UserEmailDto userEmailDto) {

        return contactRepository.findAllByUserEmail(userEmailDto.email).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public void addProfile(ContactDto contactDto) {
        User user = userRepository.findById(contactDto.userId).orElseThrow(() -> new EntityNotFoundException(UserService.USER_DOES_NOT_EXIST));

        Contact contact = new Contact();
        contact.setFirstName(contactDto.firstName);
        contact.setLastName(contactDto.lastName);
        contact.setDescription(contactDto.description);
        contact.setUser(user);

        user.addProfile(contact);
        contactRepository.save(contact);
    }

    public void editProfile(ContactDto contactDto) {
        Contact contact = contactRepository.findById(contactDto.id).orElseThrow(() -> new EntityNotFoundException(CONTACT_DOES_NOT_EXIST));
        contact.setFirstName(contactDto.firstName);
        contact.setLastName(contactDto.lastName);
        User user = userRepository.findById(contactDto.userId).orElseThrow(() -> new EntityNotFoundException(UserService.USER_DOES_NOT_EXIST));
        user.addProfile(contact);
        contactRepository.save(contact);
    }


    private List<PhoneDto> getAllPhonesByContact(Contact contact) {
        List<Phone> phones = phoneRepository.findAllByContactId(contact.getId());

        return phones
                .stream()
                .map(this::convertPhoneNumberToDto)
                .collect(Collectors.toList());
    }

    private List<AddressDto> getAllAddressesByContact(Contact contact) {
        List<Address> phones = addressRepository.findAllByContactId(contact.getId());

        return phones
                .stream()
                .map(this::convertFromAddressToAddressDTO)
                .collect(Collectors.toList());
    }

    private List<String> getAllEmails(Contact contact) {

        //TODO добавил пока так, не знаю как добавлять emails
        List<String> email = Arrays.asList("eamil.1@gmaiil.com",
                "eamil.2@gmaiil.com",
                "eamil.3@gmaiil.com",
                "eamil.4@gmaiil.com");

        email.forEach(contact::addEmail);
        return contact.getEmails();
    }


    private PhoneDto convertPhoneNumberToDto(Phone phoneNumber) {
        return phoneMapper.mapPhoneToDto(phoneNumber);
    }

    private AddressDto convertFromAddressToAddressDTO(Address address) {
        return addressMapper.mapAddressToDto(address);
    }

    private ContactDto convertToDto(Contact contact) {
        return contactMapper.mapContactToDtoFull(contact, getAllPhonesByContact(contact), getAllAddressesByContact(contact), getAllEmails(contact));
    }

    public ContactDto getProfile(UserEmailDto userEmailDto) {
        User user = userRepository.findById(userEmailDto.email).orElseThrow(() -> new EntityNotFoundException(UserService.USER_DOES_NOT_EXIST));
        Contact contact = user.getMyProfile();
        return contactMapper.mapContactToDtoFull(contact, getAllPhonesByContact(contact), getAllAddressesByContact(contact), getAllEmails(contact));
    }
}
