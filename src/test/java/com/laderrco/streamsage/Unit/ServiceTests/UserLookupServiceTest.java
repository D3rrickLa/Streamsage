package com.laderrco.streamsage.Unit.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.laderrco.streamsage.domains.Enums.Roles;
import com.laderrco.streamsage.entities.User;
import com.laderrco.streamsage.repositories.UserRepository;
import com.laderrco.streamsage.services.UserLookupService;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserLookupServiceTest {
    
    @Mock
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    private UserLookupService userService;


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
    void testFindByEmail() {
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        Optional<User> expectedUser = userService.findByEmail("john@example.com");

        assertTrue(expectedUser.isPresent());
        assertEquals(expectedUser.get(), user);
    }
}
