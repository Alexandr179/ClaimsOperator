package ru.claims_operator.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.claims_operator.models.*;
import ru.claims_operator.repositories.ClaimsRepository;
import ru.claims_operator.repositories.UsersRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

@Component
public class TestPopulatedDataUtil {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ClaimsRepository claimsRepository;


    @Transactional
    public void initializeData() {

        User admin = User.builder()
                .firstName("admin")
                .email("admin@gmail.com")
                .hashPassword(passwordEncoder.encode("qwerty"))
                .tokens(Arrays.asList("admin_token", "admin_token2"))//         simple security_rest_tokens
                .roles(Collections.singleton(Role.ADMIN))
                .build();


        User user1 = User.builder()
                .firstName("user1")
                .email("user1@gmail.com")
                .hashPassword(passwordEncoder.encode("qwerty"))
                .tokens(Arrays.asList("user1_token", "user1_token2"))
                .roles(Collections.singleton(Role.USER))
                .build();

        User user2 = User.builder()
                .firstName("user2")
                .email("user2@gmail.com")
                .hashPassword(passwordEncoder.encode("qwerty"))
                .tokens(Arrays.asList("user2_token", "user2_token2"))
                .roles(Collections.singleton(Role.USER))
                .build();

        User operator = User.builder()
                .firstName("operator")
                .email("operator@gmail.com")
                .hashPassword(passwordEncoder.encode("qwerty"))
                .tokens(Arrays.asList("operator_token", "operator_token2"))
                .roles(Collections.singleton(Role.OPERATOR))
                .build();

        usersRepository.save(admin);
        usersRepository.save(user1);
        usersRepository.save(user2);
        usersRepository.save(operator);


        Claim user1_claim_a = Claim.builder()
                .claimsUser(user1)
                .state(State.SENDED)
                .text("Claim A (from user1)")
                .createdTime(LocalDateTime.now())// !necessarily by TIMESTAMP DEFAULT now() at DB
                .build();

        Claim user1_claim_b = Claim.builder()
                .claimsUser(user1)
                .state(State.DRAFT)
                .text("Claim B (from user1)")
                .createdTime(LocalDateTime.now())
                .build();

        Claim user2_claim_a = Claim.builder()
                .claimsUser(user2)
                .state(State.SENDED)
                .text("Claim C (from user2)")
                .createdTime(LocalDateTime.now())
                .build();

        claimsRepository.save(user1_claim_a);
        claimsRepository.save(user1_claim_b);
        claimsRepository.save(user2_claim_a);
    }
}
