package com.example.deliveryapp.user;

import com.example.deliveryapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByPhone(String phone);
    @Query("select u.id from User u where u.email = ?1")
    Optional<Long> findIdByUsername(String email);

    @Query("select u.firstName from User u where u.email = ?1")
    Optional<String> findFirstNameByUsername(String email);

}
