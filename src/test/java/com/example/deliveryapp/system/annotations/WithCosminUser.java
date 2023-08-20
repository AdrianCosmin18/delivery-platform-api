package com.example.deliveryapp.system.annotations;

import com.example.deliveryapp.security.security.UserRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = WithCosminUserSecurityContextFactory.class)
public @interface WithCosminUser {

    String lastName() default "Nedelcu";
    String firstName() default "Adrian Cosmin";
    String email() default "cosminadrian1304@gmail.com";
    String password() default "";
    String phone() default "0773941000";
    String[] authorities() default {"ROLE_USER"};
}
