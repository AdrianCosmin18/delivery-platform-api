package com.example.deliveryapp.security.security;

import com.example.deliveryapp.security.jwt.JwtAccessDeniedHandler;
import com.example.deliveryapp.security.jwt.JwtAuthenticationEntryPoint;
import com.example.deliveryapp.security.jwt.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.deliveryapp.security.utils.Util.PUBLIC_URLS;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration //acelasi lucru cu component
@EnableWebSecurity//da drumul la spring security
@EnableGlobalMethodSecurity(prePostEnabled = true)//pot sa pun rolurile pe endpoint
public class SecurityConfiguration {

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private UserDetailsService userDetailsService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);//builder de autentificare
        authenticationManagerBuilder.userDetailsService(userDetailsService);

        //csrf -> double security;
        // cors -> accesarea site-ului de pe un anumit localhost, trebuie sa configuram a.i. de pe ce localhost putem accesa
        //inainte puneam pe controller @Cors si ne lasa de pe orice url, lipsit de protectie

        httpSecurity.csrf().disable().cors().and()
                .sessionManagement().sessionCreationPolicy(STATELESS)// sesiune unica, nu conteaza ce request-uri au fost initial
                .and().authorizeRequests().antMatchers(PUBLIC_URLS).permitAll()// pentru url-urile date, pot fi accesate de oricine fara a fi logat
                .anyRequest().authenticated()// pentru orice alt request, userul trebuie sa fie autentificat
                .and()
                .exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler)// in cazul in care e autentificat dar nu are permisiune pentru acel endpoint, in urma verificarii daca avea acces pe endpoint
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)//se verifica daca se poate accesa acest endpoint
                .and()
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);// se stocheaza permisiunile userului
        //nu conteaza ordinea inlantuirii  de mai sus, doar a 2-a cu a 3-a trebuie sa respecte ordinea
        //addFilterBefore => se executa prima din aceasta inlantuire
        return httpSecurity.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
