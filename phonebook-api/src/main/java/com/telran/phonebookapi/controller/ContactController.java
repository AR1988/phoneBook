package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.ContactDto;
import com.telran.phonebookapi.dto.UserEmailDto;
import com.telran.phonebookapi.service.ContactService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/contact")
public class ContactController {

    ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("")
    public void addContact(@Valid @RequestBody ContactDto contactDto) {
        contactService.add(contactDto);
    }

    @GetMapping("/{id}")
    public ContactDto getById(@PathVariable int id) {
        return contactService.getById(id);
    }

    @GetMapping("/{id}/extended")
    public ContactDto getByIdFullDetails(@PathVariable int id) {
        return contactService.getByIdFullDetails(id);
    }

    @PutMapping("")
    public void editContact(@Valid @RequestBody ContactDto contactDto) {
        contactService.editAllFields(contactDto);
    }

    @DeleteMapping("/{id}")
    public void removeById(@PathVariable int id) {
        contactService.removeById(id);
    }

    @PostMapping("/all")
    public List<ContactDto> requestAllContactsByUserEmail(@Valid @RequestBody UserEmailDto userEmailDto) {
        return contactService.getAllContactsByUserId(userEmailDto);
    }

    @PostMapping("/profile")
    public void addProfile(@Valid @RequestBody ContactDto contactDto) {
        contactService.addProfile(contactDto);
    }

    @PutMapping("/profile")
    public void editProfile(@Valid @RequestBody ContactDto contactDto) {
        contactService.editProfile(contactDto);
    }

    @PostMapping("/get-profile")
    public ContactDto getProfile(@Valid @RequestBody UserEmailDto userEmailDto) {
        return contactService.getProfile(userEmailDto);
    }

    @PostMapping("/{id}/{email}")
    public void addEmail(@PathVariable int id, @PathVariable String email) {
        contactService.addEmail(id, email);
    }

    @GetMapping("/{id}/emails")
    public List<String> requestAllEmailByContactId(@PathVariable int id) {
        return contactService.getAllEmails(id);
    }

    @DeleteMapping("/{id}/{email}")
    public void deleteEmail(@PathVariable int id, @PathVariable String email) {
        contactService.deleteEmail(id, email);

    }
}
