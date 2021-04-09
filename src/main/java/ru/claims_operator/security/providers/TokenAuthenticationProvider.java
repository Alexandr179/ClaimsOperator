/*
 * 44.Rest_API_Spring_Boot. 19.12.2020, 13:29 / 19.12.2020, 13:29   @A.Alexandr
 * Copyright (c)
 */
package ru.claims_operator.security.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.claims_operator.repositories.UsersRepository;
import ru.claims_operator.security.authentication.TokenAuthentication;
import ru.claims_operator.security.details.UsersDetailsImpl;
import ru.claims_operator.models.User;

import java.util.Optional;

/**
 * если провайдер делает setAuthentication() = true; .. то запрос проходит в Controller !!
 */

@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

//    @Autowired// -------- ..убрали
//    @Qualifier("customTokenUserDetailsService")// !!без  @Qualifier 'падаем' в реализацию springSecurity..
//    private UserDetailsService userDetailsService;

    @Autowired
    private UsersRepository usersRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("PROVIDER " + authentication.getName());
        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;// приводим к нашей аут.по токену

        // убрали/переделали.. не имеет отношения
//        UserDetails userDetails = userDetailsService.loadUserByUsername(tokenAuthentication.getName());
//        tokenAuthentication.setAuthenticated(true);
//        tokenAuthentication.setUserDetails(userDetails);
//        return tokenAuthentication;

        Optional<User> userOptional = usersRepository.findByToken(tokenAuthentication.getName());
        if (userOptional.isPresent()) {
            UsersDetailsImpl userDetails = new UsersDetailsImpl(userOptional.get());
            tokenAuthentication.setAuthenticated(true);// TODO <<< <<< <<< о чем говорили..
            tokenAuthentication.setUserDetails(userDetails);
            return tokenAuthentication;
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Override// кукую аутентификацию поддерживает (нашу кастомную TokenAuthentication)
    public boolean supports(Class<?> authClass) {
        return authClass.getName().equals(TokenAuthentication.class.getName());
    }
}
