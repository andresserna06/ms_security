package com.uc.ms_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean // el bean es como si el metodo se conviertiera en una clase con unicamente este metodo y el metodo es statico, statico es que se puede usar en una clase sin crear una instancia
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {
        // Si se comentan estas lineas el backend no va a permitir llamar a sus endpointsm si no esta autorizado
        // se usa al momento de desarrollar para evitar que sea incomodo
        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login"
                        ).permitAll() // Bloquea las rutas excepto la del login

                        .anyRequest().authenticated()
                )

                .oauth2ResourceServer(resourceServer ->
                        resourceServer.jwt(
                                Customizer.withDefaults()
                        )
                );

        return http.build();
    }
}
