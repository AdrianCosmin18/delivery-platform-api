package com.example.deliveryapp.security.security;

import com.example.deliveryapp.exceptions.DeliveryCustomException;
import com.example.deliveryapp.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsImpl implements UserDetailsService {
    //clasa default pentru service de useri, facem o implementare pentru a preciza ca vrem sa venim cu
    //userii nostrii din repoul nostru

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String s){

        return userRepo.getUserByEmail(s)
                .orElseThrow(() -> new DeliveryCustomException("User " + s + " not found"));
    }

}
