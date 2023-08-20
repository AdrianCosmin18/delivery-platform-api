package com.example.deliveryapp.user;

import com.example.deliveryapp.exceptions.DeliveryCustomException;
import com.example.deliveryapp.system.annotations.WithCosminUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @Autowired
    private MockMvc mock;

    @InjectMocks
    private UserController userController;

    private ObjectMapper mapper = new ObjectMapper();


    @BeforeEach void setUp(){
        mock = MockMvcBuilders.standaloneSetup(userController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(mapper))
                .build();
    }

    @Test
    @WithCosminUser
    void shouldChangePassword() throws Exception {

        String email = "cosmin@yahoo.com";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        this.mock.perform(MockMvcRequestBuilders.put("/delivery-app/user/change-password/{email}?oldPassword={oldPassword}&newPassword={newPassword}", email, oldPassword, newPassword)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }




}