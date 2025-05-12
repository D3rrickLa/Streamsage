package com.laderrco.streamsage.Unit.ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.laderrco.streamsage.domains.Enums.Roles;
import com.laderrco.streamsage.dtos.AuthenticationRequest;
import com.laderrco.streamsage.dtos.CredentialsDTO;
import com.laderrco.streamsage.dtos.UserInfoDTO;
import com.laderrco.streamsage.entities.User;
import com.laderrco.streamsage.repositories.UserRepository;
import com.laderrco.streamsage.services.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;

    @MockitoBean
    @Lazy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    private UserServiceImpl userService; // Inject mocked dependencies

    private User user;

    @BeforeEach
    public void init() {
        user =  User.builder()
        .id(1L)
        .firstName("john")
        .lastName("smith")
        .email("john@example.com")
        .role(Roles.ROLE_USER)
        .password(passwordEncoder.encode("1234"))
        .build();
    }


    @Test
    @Order(1)
    void testGetUsers() throws Exception {
        given(userRepository.findAll()).willReturn(List.of(user, user));

        List<User> userList = userService.getUsers();

        assertThat(userList).isNotEmpty();
    }

    @Test
    @Order(2)
    void testFindById() throws Exception {
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        User existingUser = userService.findById(user.getId()).get();

        assertThat(existingUser).isNotNull();
    }

    @Test
    @Order(4)
    void testSaveUser() {
        given(userRepository.save(user)).willReturn(user);

        //action
        User savedUser = userService.save(user);

        // verify the output
        System.out.println(savedUser);
        assertThat(savedUser).isNotNull();   
    }


    @Test
    @Order(3)
    void testUpdateUser() throws Exception {
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        user.setEmail("test123@sample.com");
        user.setFirstName("jimmy");
        given(userRepository.save(user)).willReturn(user);

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setEmail("test123@sample.com");
        userInfoDTO.setFirstName("jimmy");
        userInfoDTO.setLastName("smith");
        User updatedUser = userService.updateUserProfile(user.getId(), userInfoDTO);
        
        assertThat(updatedUser.getEmail()).isEqualTo("test123@sample.com");
        assertThat(updatedUser.getFirstName()).isEqualTo("jimmy");

    }

    @Test
    @Order(5)
    void testDeleteUser_Correct() throws Exception {
        // willDoNothing().given(userRepository).deleteById(user.getId());
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
      
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(user.getEmail(), "1234");
        
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, passwordEncoder); // had to make a new instance because the PasswordEncoder wasn't being set
        
        userServiceImpl.delete(authenticationRequest);
        verify(userRepository, times(1)).findByEmail("john@example.com");
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_Fail() throws Exception {
        // willDoNothing().given(userRepository).deleteById(user.getId());
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
      
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(user.getEmail(), "11234");
        
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, passwordEncoder); // had to make a new instance because the PasswordEncoder wasn't being set
        
        // Expect exception when calling delete() due to incorrect password
        Exception exception = assertThrows(Exception.class, () -> {
            userServiceImpl.delete(authenticationRequest);
        });

        assertEquals("Incorrect password given", exception.getMessage()); // ✅ Validate error message
        verify(userRepository, times(1)).findByEmail("john@example.com");
        verify(userRepository, times(0)).delete(user);
    }

    @Test
    @Order(6)
    void testUpdateUserPassword() throws Exception {
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        CredentialsDTO credentialsDTO = new CredentialsDTO("1234", "56789");
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, passwordEncoder); // had to make a new instance because the PasswordEncoder wasn't being set

        userServiceImpl.updateUserPassword(user.getId(), credentialsDTO);

        assertThat(passwordEncoder.matches(credentialsDTO.getNewPassword(), user.getPassword())).isTrue();
        verify(userRepository, times(1)).save(user);

    }

    @Test
    void testFindIdByEmail_Correct() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        Long id  = userService.findIdByEmail();

        assertThat(user.getId()).isEqualTo(id);

    }
}
