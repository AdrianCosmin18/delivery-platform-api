package com.example.deliveryapp.system.annotations;

import com.example.deliveryapp.security.security.UserRole;
import com.example.deliveryapp.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.security.core.context.SecurityContextHolder.clearContext;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

final class WithCosminUserSecurityContextFactory implements WithSecurityContextFactory<WithCosminUser> {

    public SecurityContext createSecurityContext(WithCosminUser withCosminUser){

        User cosminUser = new User(
                withCosminUser.lastName(),
                withCosminUser.firstName(),
                withCosminUser.email(),
                withCosminUser.password(),
                withCosminUser.phone());

        cosminUser.setUserRole(UserRole.USER);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            cosminUser,
            withCosminUser.password(),
            Arrays.stream(withCosminUser.authorities()).map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );

        clearContext();
        getContext().setAuthentication(authenticationToken);
        return getContext();
    }
}
