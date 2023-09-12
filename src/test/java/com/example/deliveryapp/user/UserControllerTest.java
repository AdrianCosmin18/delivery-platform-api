package com.example.deliveryapp.user;

import com.example.deliveryapp.DTOs.*;
import com.example.deliveryapp.address.Address;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private AuthenticationManager authenticationManager;

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

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, oldPassword);

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, oldPassword))).thenReturn(authentication);

        this.mock.perform(MockMvcRequestBuilders.put("/delivery-app/user/change-password/{email}?oldPassword={oldPassword}&newPassword={newPassword}",
                                email,
                                oldPassword,
                                newPassword)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithCosminUser
    void shouldGetUserByEmail() throws Exception{

//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserDTO user = UserDTO.builder().email("cosmin@yahoo.com").build();

        when(this.userService.getUserByEmail(user.getEmail())).thenReturn(user);

        this.mock.perform(MockMvcRequestBuilders.get("/delivery-app/user/get-user/{email}", user.getEmail())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(user)));
    }

    @Test
    @WithCosminUser
    void shouldGetUser() throws Exception{

        UserDTO user = UserDTO.builder().email("cosminadrian1304@gmail.com").build();
        when(this.userService.getUser()).thenReturn(user);

        this.mock.perform(MockMvcRequestBuilders.get("/delivery-app/user/get-user")
                        .accept(MediaType.APPLICATION_JSON)) // ce specificam noi
                .andExpect(status().isOk())// ce se asteapta la returnare
                .andExpect(content().string(mapper.writeValueAsString(user))); // ce se asteapta la returnare
    }

    @Test
    @WithCosminUser
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

    @Test
    @WithCosminUser
    void shouldDeleteAddress() throws Exception {
        long addressId = 1L;
        UserDTO user = UserDTO.builder().email("cosmin@yahoo.com").build();

        this.mock.perform(MockMvcRequestBuilders.delete("/delivery-app/user/delete-address/{addressId}", addressId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithCosminUser
    void shouldGetAddresses() throws Exception {
        UserDTO user = UserDTO.builder().email("cosmin@yahoo.com").build();

        List<AddressDTO> addressDTOList = new ArrayList<>();
        addressDTOList.add(AddressDTO.builder().cityName("City1").street("Strada1").number(1).build());
        addressDTOList.add(AddressDTO.builder().cityName("City2").street("Strada2").number(2).build());

        when(this.userService.getUserAddresses(user.getEmail())).thenReturn(addressDTOList);
        this.mock.perform(MockMvcRequestBuilders.get("/delivery-app/user/get-user-addresses/{email}", user.getEmail())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(addressDTOList)));
    }

    @Test
    @WithCosminUser
    void shouldSetAsMainAddress() throws Exception {
        UserDTO user = UserDTO.builder().email("cosmin@yahoo.com").build();
        long addressId = 1L;

        this.mock.perform(MockMvcRequestBuilders.put("/delivery-app/user/set-as-main-address/{email}/{addressId}", user.getEmail(), addressId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        then(this.userService).should().setAsMainAddress(user.getEmail(), addressId); //este necesara aceasta linie ?
    }

    @Test
    @WithCosminUser
    void shouldUpdateAddress() throws Exception {
        UserDTO user = UserDTO.builder().email("cosmin@yahoo.com").build();
        long addressId = 1L;
        AddressDTO addressDTO = AddressDTO.builder().cityName("City1").street("Strada1").number(1).build();

        this.mock.perform(MockMvcRequestBuilders.put("/delivery-app/user/update-address/{email}/{addressId}", user.getEmail(), addressId)
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(addressDTO)))
                .andExpect(status().isOk());

        then(this.userService).should().updateAddress(user.getEmail(), addressId, addressDTO);
    }

    @Test
    @WithCosminUser
    void shouldGetMainAddressOfUser() throws Exception {
        UserDTO user = UserDTO.builder().email("cosmin@yahoo.com").build();
        AddressDTO addressDTO = AddressDTO.builder().cityName("City1").street("Strada1").number(1).build();

        when(this.userService.getMainAddress(user.getEmail())).thenReturn(addressDTO);
        this.mock.perform(MockMvcRequestBuilders.get("/delivery-app/user/get-main-address/{email}", user.getEmail())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(addressDTO)));
    }

    @Test
    @WithCosminUser
    void shouldCheckIfUserHasMainAddress() throws Exception {
        UserDTO user = UserDTO.builder().email("cosmin@yahoo.com").build();
        AddressDTO addressDTO = AddressDTO.builder().cityName("City1").street("Strada1").number(1).build();

        when(this.userService.isAnyMainAddress(user.getEmail())).thenReturn(true);
        this.mock.perform(MockMvcRequestBuilders.get("/delivery-app/user/has-user-main-address/{email}", user.getEmail())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(true)));
    }

    @Test
    @WithCosminUser
    void shouldAddCard() throws Exception {
        UserDTO user = UserDTO.builder().email("cosmin@yahoo.com").build();
        CardDTO cardDTO = CardDTO.builder()
                .cardNumber("1234123412341234")
                .cardType("mastercard")
                .cardHolderName("Nume Prenume")
                .build();

        this.mock.perform(MockMvcRequestBuilders.post("/delivery-app/user/add-card/{email}", user.getEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithCosminUser
    void shouldGetUserCards() throws Exception {
        UserDTO user = UserDTO.builder().email("cosmin@yahoo.com").build();
        List<CardDTO> cardDTOS = new ArrayList<>();
        cardDTOS.add(CardDTO.builder()
                .cardNumber("1234123412341234")
                .cardType("Mastercard")
                .cardHolderName("Nume Prenume")
                .build());
        cardDTOS.add(CardDTO.builder()
                .cardNumber("1234123412345555")
                .cardType("Visa")
                .cardHolderName("Nume Prenume")
                .build());

        when(this.userService.getUserCards(user.getEmail())).thenReturn(cardDTOS);
        this.mock.perform(MockMvcRequestBuilders.get("/delivery-app/user/get-user-cards/{email}", user.getEmail())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(cardDTOS)));
    }

    @Test
    @WithCosminUser
    void shouldDeleteCard() throws Exception {
        UserDTO user = UserDTO.builder().email("cosmin@yahoo.com").build();
        long cardId = 1;

        this.mock.perform(MockMvcRequestBuilders.delete("/delivery-app/user/delete-card/{email}/{cardId}", user.getEmail(), cardId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        then(this.userService).should().removeCard(user.getEmail(), cardId);//este necesara aceasta linie ?
    }

    @Test
    @WithCosminUser
    void shouldSetAsMainCard() throws Exception {
        UserDTO user = UserDTO.builder().email("cosmin@yahoo.com").build();
        long cardId = 1;

        this.mock.perform(MockMvcRequestBuilders.put("/delivery-app/user/set-as-main-card/{email}/{cardId}", user.getEmail(), cardId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        then(this.userService).should().setAsMainCard(user.getEmail(), cardId);
    }

    @Test
    @WithCosminUser
    void shouldUserHasAnyMainCard() throws Exception {
        UserDTO user = UserDTO.builder().email("cosmin@yahoo.com").build();
        CardDTO cardDTO = CardDTO.builder()
                .cardNumber("1234123412341234")
                .cardType("mastercard")
                .cardHolderName("Nume Prenume")
                .build();

        when(this.userService.isAnyMainCard(user.getEmail())).thenReturn(true);
        this.mock.perform(MockMvcRequestBuilders.get("/delivery-app/user/has-user-main-card/{email}", user.getEmail())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(true)));

    }

    @Test
    @WithCosminUser
    void shouldGetMainCard() throws Exception {
        UserDTO user = UserDTO.builder().email("cosmin@yahoo.com").build();
        CardDTO cardDTO = CardDTO.builder()
                .cardNumber("1234123412341234")
                .cardType("mastercard")
                .cardHolderName("Nume Prenume")
                .build();

        when(this.userService.getMainCard(user.getEmail())).thenReturn(cardDTO);
        this.mock.perform(MockMvcRequestBuilders.get("/delivery-app/user/get-user-main-card/{email}", user.getEmail())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(cardDTO)));
    }

    @Test
    @WithCosminUser
    void shouldPlaceOrder() throws Exception {
        UserDTO user = UserDTO.builder().email("cosmin@yahoo.com").build();
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setEmailUser(user.getEmail());
        orderRequest.setCardId(1L);
        orderRequest.setAddressId(1L);

        OrderItemDTO order = OrderItemDTO.builder().price(15D).quantity(1).build();
        orderRequest.setProductsInCart(new ArrayList<>(Collections.singletonList(order)));

        this.mock.perform(MockMvcRequestBuilders.post("/delivery-app/user/place-order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @WithCosminUser
    void shouldGetHistoryOrders() throws Exception {
        UserDTO user = UserDTO.builder().email("cosmin@yahoo.com").build();
        List<OrderDTO> orders = new ArrayList<>();
        orders.add(OrderDTO.builder().courierId(1L).amount(23D).build());

        when(this.userService.getAllHistoryOrders(user.getEmail())).thenReturn(orders);
        this.mock.perform(MockMvcRequestBuilders.get("/delivery-app/user/history-orders/{email}", user.getEmail())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(orders)));
    }

    @Test
    @WithCosminUser
    void shouldUpdateUser() throws Exception {
        UserDTO user = UserDTO.builder().email("cosmin@yahoo.com").firstName("Name").build();
        String email = "cosminoldemail@yahoo.com";

        this.mock.perform(MockMvcRequestBuilders.put("/delivery-app/user/update-user/{email}", email)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk());
        then(this.userService).should().updateUser(email, user);
    }
}