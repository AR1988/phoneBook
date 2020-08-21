package com.telran.phonebookapi.service;

import com.telran.phonebookapi.dto.NewPasswordDto;
import com.telran.phonebookapi.dto.RecoveryPasswordDto;
import com.telran.phonebookapi.dto.UserDto;
import com.telran.phonebookapi.model.RecoveryToken;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistance.IActivationTokenRepository;
import com.telran.phonebookapi.persistance.IContactRepository;
import com.telran.phonebookapi.persistance.IRecoveryTokenRepository;
import com.telran.phonebookapi.persistance.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    IUserRepository userRepository;

    @Mock
    IRecoveryTokenRepository recoveryTokenRepository;

    @Mock
    IActivationTokenRepository activationTokenRepository;

    @Mock
    EmailSender emailSender;

    @InjectMocks
    UserService userService;

    @Test
    public void testSendRecoveryToken_tokenIsSavedToRepo() {
        String email = "johndoe@mail.com";
        User ourUser = new User(email, "1234");
        RecoveryPasswordDto recoveryPasswordDto = new RecoveryPasswordDto(email);

        when(userRepository.findById(email)).thenReturn(Optional.of(ourUser));
        userService.sendRecoveryToken(recoveryPasswordDto);

        verify(recoveryTokenRepository, times(1)).save(any());

        verify(recoveryTokenRepository, times(1)).save(argThat(token ->
                token.getUser().getEmail().equals(email)));

        verify(emailSender, times(1)).sendMail(eq(email), anyString(), anyString());
    }

    @Test
    public void testCreateNewPassword_newPasswordIsSaved() {
        User ourUser = new User("johndoe@mail.com", "12345678");
        ourUser.setActive(true);
        String token = UUID.randomUUID().toString();
        RecoveryToken recoveryToken = new RecoveryToken(token, ourUser);
        when(recoveryTokenRepository.findById(token)).thenReturn(Optional.of(recoveryToken));
        when(bCryptPasswordEncoder.encode("4321")).thenReturn("4321Encoded");
        NewPasswordDto newPasswordDto = new NewPasswordDto();
        newPasswordDto.password = "4321";
        newPasswordDto.token = token;
        userService.createNewPassword(newPasswordDto);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        verify(userRepository, times(1)).save(any());
        verify(recoveryTokenRepository, times(1)).findById(token);
        verify(recoveryTokenRepository, times(1)).delete(any());
        User capturedUser = userArgumentCaptor.getValue();
        assertEquals("4321Encoded", capturedUser.getPassword());
    }

    @Captor
    ArgumentCaptor<User> userArgumentCaptor;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    IContactRepository contactRepository;

    @Test
    public void testAdd_user_passesToRepo() {
        UserDto userDto = new UserDto("ivanov@gmail.com", "12345678");
        when(bCryptPasswordEncoder.encode("12345678")).thenReturn("87654321");
        userService.addUser(userDto);
        verify(userRepository).save(userArgumentCaptor.capture());
        List<User> capturedUsers = userArgumentCaptor.getAllValues();
        assertEquals(1, capturedUsers.size());
        User capturedUser = capturedUsers.get(0);
        assertEquals(userDto.email, capturedUser.getEmail());
        assertEquals("87654321", capturedUser.getPassword());
        verify(userRepository, times(1)).save(any());
        verify(userRepository, times(1)).save(argThat(user ->
                user.getEmail().equals(userDto.email)
                        && user.getPassword().equals("87654321")
        ));
        verify(activationTokenRepository, times(1)).save(any());
        verify(activationTokenRepository, times(1)).save(argThat(token ->
                token.getUser().getEmail().equals(userDto.email)
        ));
        verify(emailSender, times(1)).sendMail(eq(userDto.email),
                eq(UserService.ACTIVATION_SUBJECT),
                anyString());

    }

    //TODO
    @Test
    public void testEditAllFields_userExist_AllFieldsChanged() {

//        User oldUser = new User("test@gmail.com", "12345678");
//        Contact oldProfile = new Contact();
//        oldProfile.setFirstName("Name");
//        oldProfile.setLastName("LastName");
//        oldUser.setMyProfile(oldProfile);
//
//        UserDto userDto = new UserDto("test@gmail.com", "12345678");
//        ContactDto profileDto = new ContactDto();
//        profileDto.firstName = "NewName";
//        profileDto.lastName = "NewLastName";
//        userDto.myProfile = profileDto;
//
//        when(userRepository.findById(userDto.email)).thenReturn(Optional.of(oldUser));
//
//        userService.editAllFields(userDto);
//
//        verify(userRepository, times(1)).save(any());
//
//        verify(userRepository, times(1)).save(argThat(user ->
//                user.getEmail().equals(userDto.email)
//                        && user.getMyProfile().getFirstName().equals(userDto.myProfile.firstName) && user.getMyProfile().getLastName().equals(userDto.myProfile.lastName
//                )));
    }
    //TODO
//    @Test
//    public void testEditAny_userDoesNotExist_EntityNotFoundException() {
//
//        UserDto userDto = new UserDto("test@gmail.com", "12345678");
//
//        Exception exception = assertThrows(EntityNotFoundException.class, () -> userService.editAllFields(userDto));
//
//        verify(userRepository, times(1)).findById(any());
//        assertEquals("Error! This user doesn't exist in our DB", exception.getMessage());
//    }

    @Captor
    ArgumentCaptor<User> userCaptor;
    //TODO
//    @Test
//    public void testRemoveById_userExists_UserDeleted() {
//
//        User user = new User("test@gmail.com", "12345678");
//
//        UserDto userDto = new UserDto(user.getEmail(), user.getPassword());
//        when(userRepository.findById(userDto.email)).thenReturn(Optional.of(user));
//        when(userService.getUserId()).thenReturn(new UserEmailDto(user.getEmail()));
//        userService.removeUser();
//
//        List<User> capturedUsers = userCaptor.getAllValues();
//        verify(userRepository, times(1)).deleteById(userDto.email);
//        assertEquals(0, capturedUsers.size());
//    }

}

