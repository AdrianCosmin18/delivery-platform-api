package com.example.deliveryapp.repos;

import com.example.deliveryapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByPhone(String phone);
}
