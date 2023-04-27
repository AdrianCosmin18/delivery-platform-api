package com.example.deliveryapp.security.security;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.deliveryapp.security.security.UserPermission.*;

@AllArgsConstructor
public enum UserRole {

    USER(Sets.newHashSet(
            ADD_PRODUCT_TO_WISHLIST,
            DELETE_FROM_WISHLIST,
            ADD_ADDRESS,
            DELETE_ADDRESS,
            ADD_CARD,
            REMOVE_CARD,
            PLACE_ORDER));

    private final Set<UserPermission> permissions;

    public Set<UserPermission> getPermissions(){ return permissions; }

    //implementez rolul in spring security luand permisiunile generate
    //acel set de permisiuni specifice unui user => ROLE_USER
    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> collect = getPermissions()
                .stream()
                .map(e -> new SimpleGrantedAuthority(e.getPermission()))
                .collect(Collectors.toSet());

        collect.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return collect;
    }


}
