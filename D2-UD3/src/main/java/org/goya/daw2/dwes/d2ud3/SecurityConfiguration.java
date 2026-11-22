package org.goya.daw2.dwes.d2ud3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

        @Bean
        public PasswordEncoder passworEncoder(){return new BCryptPasswordEncoder() ;}

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

        Map<String, String> usuariosMap = new LinkedHashMap<>();
        usuariosMap.put("user1", encoder.encode("1234"));
        usuariosMap.put("user2", encoder.encode("abcd"));
        usuariosMap.put("admin", encoder.encode("admin123"));
        List<UserDetails> listaUsuarios = new ArrayList<>();

        for (Map.Entry<String, String> entry : usuariosMap.entrySet()) {

            UserDetails user = User.withUsername(entry.getKey())
                    .password(entry.getValue())
                    .authorities("ROLE_USER")
                    .build();

            listaUsuarios.add(user);
        }

        return new InMemoryUserDetailsManager(listaUsuarios);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // <-- Añadido throws Exception
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                )

                .csrf(csrf -> csrf.disable())
                .build(); //
    }

}
