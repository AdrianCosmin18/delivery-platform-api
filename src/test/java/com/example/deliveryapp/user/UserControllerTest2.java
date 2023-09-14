package com.example.deliveryapp.user;

import com.example.deliveryapp.DTOs.AddressDTO;
import com.example.deliveryapp.DTOs.UserDTO;
import com.example.deliveryapp.DeliveryAppApplication;
import com.example.deliveryapp.email.EmailSenderService;
import com.example.deliveryapp.security.jwt.JWTTokenProvider;
import com.example.deliveryapp.system.annotations.MockSecurityContext;
import com.example.deliveryapp.system.annotations.WithCosminUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = DeliveryAppApplication.class)
public class UserControllerTest2 {

    @Autowired
    MockMvc mock;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JWTTokenProvider jwtTokenProvider;

    @MockBean
    private EmailSenderService emailSenderService;

    private ObjectMapper mapper = new ObjectMapper();


    @Test
    //@WithCosminUser
//    @MockSecurityContext(username = "cosminadrian1304@gmail.com")
    @MockSecurityContext
    void shouldGetUserByEmail() throws Exception{

        UserDTO user = UserDTO.builder().email("cosmin@yahoo.com").build();

        when(this.userService.getUserByEmail(user.getEmail())).thenReturn(user);

        this.mock.perform(MockMvcRequestBuilders.get("/delivery-app/user/get-user/{email}", user.getEmail())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(user)));
    }

    @Test
//    @WithCosminUser
    @MockSecurityContext
    void shouldAddAddress() throws Exception{

        AddressDTO addressDTO = AddressDTO.builder()
                .cityName("Bucuresti")
                .street("Aleea Castanelor")
                .number(3)
                .build();

        this.mock.perform(MockMvcRequestBuilders.post("/delivery-app/user/add-address")
                        .contentType(MediaType.APPLICATION_JSON) //specificam ce trimitem noi
                        .content(mapper.writeValueAsString(addressDTO)))//specificam ce trimitem noi
                .andExpect(status().isOk()); // ce se asteapta la returnare
    }








}
