package com.telran.phonebookapi.service;

import com.telran.phonebookapi.dto.*;
import com.telran.phonebookapi.exception.UserNotFoundException;
import com.telran.phonebookapi.mapper.AddressMapper;
import com.telran.phonebookapi.mapper.ContactMapper;
import com.telran.phonebookapi.mapper.EmailMapper;
import com.telran.phonebookapi.mapper.PhoneMapper;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistance.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {

    static final String CONTACT_DOES_NOT_EXIST = "Error! This contact doesn't exist in our DB";
    private static final String USER_DOES_NOT_EXIST = "User not Found";

    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);

    private final IUserRepository userRepository;
    private final IContactRepository contactRepository;
    private final IAddressRepository addressRepository;
    private final IPhoneRepository phoneRepository;
    private final IEmailRepository emailRepository;
    private final ContactMapper contactMapper;
    private final AddressMapper addressMapper;
    private final PhoneMapper phoneMapper;
    private final EmailMapper emailMapper;

    public ContactService(IUserRepository userRepository, IContactRepository contactRepository, IAddressRepository addressRepository, IPhoneRepository phoneRepository, IEmailRepository emailRepository, ContactMapper contactMapper, AddressMapper addressMapper, PhoneMapper phoneMapper, EmailMapper emailMapper) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
        this.addressRepository = addressRepository;
        this.phoneRepository = phoneRepository;
        this.emailRepository = emailRepository;
        this.contactMapper = contactMapper;
        this.addressMapper = addressMapper;
        this.phoneMapper = phoneMapper;
        this.emailMapper = emailMapper;
    }

    public void add(ContactDto contactDto) {
        User user = userRepository.findById(getUsername().email)
                .orElseThrow(() -> new EntityNotFoundException(UserService.USER_DOES_NOT_EXIST));
        Contact contact = new Contact(contactDto.firstName, user);
        contact.setLastName(contactDto.lastName);
        contact.setDescription(contactDto.description);
        contactRepository.save(contact);
    }

    public ContactDto getById(int id) {
        Contact contact = contactRepository.findByUserEmailAndId(getUsername().email, id)
                .orElseThrow(() -> {
                            logger.warn("Contact with id: " + id + " not owned by user \"" + getUsername().email + "\"");
                            return new EntityNotFoundException(CONTACT_DOES_NOT_EXIST);
                        }
                );

        return contactMapper.mapContactToDto(contact);
    }

    public ContactDto getByIdFullDetails(int id) {
        Contact contact = contactRepository.findByUserEmailAndId(getUsername().email, id)
                .orElseThrow(() -> {
                            logger.warn("Contact with id: " + id + " not owned by user \"" + getUsername().email + "\"");
                            return new EntityNotFoundException(CONTACT_DOES_NOT_EXIST);
                        }
                );

        ContactDto contactDto = contactMapper.mapContactToDtoFull(contact, getAllPhonesByContact(contact), getAllAddressesByContact(contact), getAllEmailsByContact(contact));

        contactDto.addresses = contact.getAddresses().stream()
                .map(addressMapper::mapAddressToDto)
                .collect(Collectors.toList());

        contactDto.phoneNumbers = contact.getPhoneNumbers().stream()
                .map(phoneMapper::mapPhoneToDto)
                .collect(Collectors.toList());

        contactDto.emails = contact.getEmails().stream()
                .map(emailMapper::mapEmailToDto)
                .collect(Collectors.toList());

        return contactDto;
    }

    public void editAllFields(ContactDto contactDto) {
        Contact contact = contactRepository.findById(contactDto.id)
                .orElseThrow(() -> {
                            logger.warn("Contact with id: " + contactDto.id + " not owned by user \"" + getUsername().email + "\"");
                            return new EntityNotFoundException(CONTACT_DOES_NOT_EXIST);
                        }
                );
        contact.setFirstName(contactDto.firstName);
        contact.setLastName(contactDto.lastName);
        contact.setDescription(contactDto.description);
        contactRepository.save(contact);
    }

    public void removeById(int id) {
        contactRepository.findById(id)
                .orElseThrow(() -> {
                            logger.warn("Contact with id: " + id + " not owned by user \"" + getUsername().email + "\"");
                            return new EntityNotFoundException(CONTACT_DOES_NOT_EXIST);
                        }
                );
        contactRepository.deleteById(id);
    }

    public List<ContactDto> getAllContactsByUserId() {
        return contactRepository.findAllByUserEmail(getUsername().email).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void addProfile(ContactDto contactDto) {
        User user = userRepository.findById(getUsername().email)
                .orElseThrow(() -> {
                            logger.warn("User with id: \"" + getUsername().email + "\"");
                            return new EntityNotFoundException(USER_DOES_NOT_EXIST);
                        }
                );
        Contact profile = user.getMyProfile();
        profile.setFirstName(contactDto.firstName);
        profile.setLastName(contactDto.lastName);
        profile.setDescription(contactDto.description);
        profile.setUser(user);
        user.addProfile(profile);
        contactRepository.save(profile);
    }

    public void editProfile(ContactDto contactDto) {
        Contact newProfile = contactRepository.findByUserEmailAndId(getUsername().email, contactDto.id)
                .orElseThrow(() -> new EntityNotFoundException(CONTACT_DOES_NOT_EXIST));
        newProfile.setFirstName(contactDto.firstName);
        newProfile.setLastName(contactDto.lastName);
        contactRepository.save(newProfile);
    }

    //For tests
    public ContactDto getLastCreatedContactByUser() {
        Contact contact = contactRepository.findTopByUserEmailOrderByIdDesc(getUsername().email);
        ContactDto contactDto = contactMapper.mapContactToDto(contact);
        return contactDto;
    }

    private List<PhoneDto> getAllPhonesByContact(Contact contact) {
        return phoneRepository.findAllByContactId(contact.getId())
                .stream()
                .map(phoneMapper::mapPhoneToDto)
                .collect(Collectors.toList());
    }
    //For test
    public int countContact() {
        return contactRepository.findAllByUserEmail(getUsername().email).size();
    }

    private List<EmailDto> getAllEmailsByContact(Contact contact) {
        return emailRepository.findAllByContactId(contact.getId())
                .stream()
                .map(emailMapper::mapEmailToDto)
                .collect(Collectors.toList());
    }

    private List<AddressDto> getAllAddressesByContact(Contact contact) {
        return addressRepository.findAllByContactId(contact.getId())
                .stream()
                .map(addressMapper::mapAddressToDto)
                .collect(Collectors.toList());
    }

    private ContactDto convertToDto(Contact contact) {
        return contactMapper.mapContactToDtoFull(contact, getAllPhonesByContact(contact), getAllAddressesByContact(contact), getAllEmailsByContact(contact));
    }

    public ContactDto getProfile() {
        User user = userRepository.findById(getUsername().email).orElseThrow(() -> new EntityNotFoundException(UserService.USER_DOES_NOT_EXIST));
        Contact contact = user.getMyProfile();
        if (contact.getFirstName() == null && contact.getUser() == null)
            throw new EntityNotFoundException(CONTACT_DOES_NOT_EXIST);
        return contactMapper.mapContactToDtoFull(contact, getAllPhonesByContact(contact), getAllAddressesByContact(contact), getAllEmailsByContact(contact));
    }

    public UserEmailDto getUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        try {
            return new UserEmailDto(userDetails.getUsername());
        } catch (NoResultException e) {
            logger.info("User not found");
            throw new UserNotFoundException(USER_DOES_NOT_EXIST);
        }
    }
}
