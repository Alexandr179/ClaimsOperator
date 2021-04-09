package ru.claims_operator.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.claims_operator.security.filter.TokenAuthenticationFilter;
import ru.claims_operator.security.providers.TokenAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Autowired
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Autowired
    protected void configure(HttpSecurity http) throws Exception {
        // disable all at REST..
        http.csrf().disable();
        http.formLogin().disable();
        http.logout().disable();
        http.sessionManagement().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);// ..from - 2H DB connection
        http.addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter.class);
        http.authorizeRequests()
//                .antMatchers("/**").authenticated()// доступно только аутентифицированным (*только ресурс пройдет)

                .antMatchers("**/users/**").hasAuthority("ADMIN")
                .antMatchers("**/claims/**").hasAuthority("OPERATOR")
                .antMatchers("**/claims/**").hasAuthority("USER")
        ;

        http.headers().frameOptions().disable();// from normal functionality DB
    }


    @Override// used provider
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(tokenAuthenticationProvider);
    }
}
