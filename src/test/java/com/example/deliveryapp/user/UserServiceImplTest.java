package com.example.deliveryapp.user;

import com.example.deliveryapp.DTOs.AddressDTO;
import com.example.deliveryapp.DTOs.UserDTO;
import com.example.deliveryapp.address.Address;
import com.example.deliveryapp.city.City;
import com.example.deliveryapp.city.CityRepo;
import com.example.deliveryapp.exceptions.DeliveryCustomException;
import com.example.deliveryapp.order.OrderRepo;
import com.example.deliveryapp.system.annotations.WithCosminUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private CityRepo cityRepo;
    @Mock
    private OrderRepo orderRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void addUser(){
        UserDTO userDTO = UserDTO.builder()
                .phone("0789456123")
                .lastName("Nedelcu")
                .firstName("Cosmin")
                .password("parola")
                .email("cosmin@yahoo.com")
                .build();

        when(this.userRepo.getUserByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(this.userRepo.getUserByEmail(userDTO.getEmail())).thenReturn(Optional.empty());

        this.userService.addUser(userDTO);
        then(this.userRepo).should().saveAndFlush(userArgumentCaptor.capture());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String expectedPassword = userDTO.getPassword();
        String actualPassword = userArgumentCaptor.getValue().getPassword();

        assertThat(passwordEncoder.matches(expectedPassword, actualPassword)).isTrue();
    }

    @Test
    void shouldThrowException1AddUser(){

        User user = new User();
        user.setEmail("cosmin@yahoo.com");

        UserDTO userDTO = UserDTO.builder()
                .phone("0789456123")
                .lastName("Nedelcu")
                .firstName("Cosmin")
                .password("parola")
                .email("cosmin@yahoo.com")
                .build();

        when(this.userRepo.getUserByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));
        assertThrows(DeliveryCustomException.class, () -> userService.addUser(userDTO));
    }

    @Test
    void shouldThrowException2AddUser(){

        User user = new User();
        user.setPhone("0789456123");

        UserDTO userDTO = UserDTO.builder()
                .phone("0789456123")
                .lastName("Nedelcu")
                .firstName("Cosmin")
                .password("parola")
                .email("cosmin@yahoo.com")
                .build();

        when(this.userRepo.getUserByPhone(userDTO.getPhone())).thenReturn(Optional.of(user));
        assertThrows(DeliveryCustomException.class, () -> userService.addUser(userDTO));
    }

    @Test
    void shouldAddAddress(){
        City city = new City("Bucuresti", "Romania");

        User user = new User();
        user.setEmail("cosminadrian1304@gmail.com");
        String username = "cosminadrian1304@gmail.com";


        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(username);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        List<Address> addresses = new ArrayList<>();
        addresses.add(Address.builder().street("strada").number(4).city(city).isDefault(true).build());

        user.setAddresses(addresses);


        UserDTO userDTO = UserDTO.builder()
                .phone("0789456123")
                .lastName("Nedelcu")
                .firstName("Cosmin")
                .password("parola")
                .email("cosminadrian1304@gmail.com")
                .build();

        AddressDTO addressDTO= AddressDTO.builder()
                .street("Aleea Callatis")
                .number(3)
                .block("A12")
                .staircase("D")
                .floor(4)
                .apartment(59)
                .interphone("59")
                .isDefault(true)
                .cityName("Bucuresti")
                .build();

        when(this.userRepo.getUserByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));
        when(this.cityRepo.getCityByName(addressDTO.getCityName())).thenReturn(Optional.of(city));

        this.userService.addAddress(addressDTO);
        then(this.userRepo).should().save(userArgumentCaptor.capture());
        assertThat(userArgumentCaptor.getValue().getAddresses().get(0).getIsDefault()).isEqualTo(Boolean.FALSE);
    }

    @Test
    void shouldAddAddress2(){
        City city = new City("Bucuresti", "Romania");

        User user = new User();
        user.setEmail("cosmin@yahoo.com");
        List<Address> addresses = new ArrayList<>();
        addresses.add(Address.builder().street("strada").number(4).city(city).isDefault(true).build());

        user.setAddresses(addresses);


        UserDTO userDTO = UserDTO.builder()
                .phone("0789456123")
                .lastName("Nedelcu")
                .firstName("Cosmin")
                .password("parola")
                .email("cosmin@yahoo.com")
                .build();

        AddressDTO addressDTO= AddressDTO.builder()
                .street("Aleea Callatis")
                .number(3)
                .block("A12")
                .staircase("D")
                .floor(4)
                .apartment(59)
                .interphone("59")
                .isDefault(false)
                .cityName("Bucuresti")
                .build();

        when(this.userRepo.getUserByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));
        when(this.cityRepo.getCityByName(addressDTO.getCityName())).thenReturn(Optional.of(city));


        this.userService.addAddress(addressDTO);
        then(this.userRepo).should().save(userArgumentCaptor.capture());
        assertThat(userArgumentCaptor.getValue().getAddresses().get(0).getIsDefault()).isEqualTo(Boolean.TRUE);
    }

    @Test
    void shouldThrowExceptionAddAddress(){
        City city = new City("Bucuresti", "Romania");

        User user = new User();
        user.setEmail("cosmin@yahoo.com");
        List<Address> addresses = new ArrayList<>();
        addresses.add(Address.builder()
                .street("Aleea Callatis")
                .number(3)
                .block("A12")
                .staircase("D")
                .floor(4)
                .apartment(59)
                .interphone("59")
                .city(city)
                .isDefault(false)
                .build());

        user.setAddresses(addresses);


        UserDTO userDTO = UserDTO.builder()
                .phone("0789456123")
                .lastName("Nedelcu")
                .firstName("Cosmin")
                .password("parola")
                .email("cosmin@yahoo.com")
                .build();

        AddressDTO addressDTO = AddressDTO.builder()
                .street("Aleea Callatis")
                .number(3)
                .block("A12")
                .staircase("D")
                .floor(4)
                .apartment(59)
                .interphone("59")
                .cityName(city.getName())
                .isDefault(false)
                .build();

        when(this.userRepo.getUserByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));
        when(this.cityRepo.getCityByName(addressDTO.getCityName())).thenReturn(Optional.of(city));

        assertThrows(DeliveryCustomException.class, () -> this.userService.addAddress(addressDTO));
    }

    @Test
    void removeAddress1(){
        City city = new City("Bucuresti", "Romania");

        User user = new User();
        user.setEmail("cosmin@yahoo.com");
        user.setId(1L);
        List<Address> addresses = new ArrayList<>();
        addresses.add(Address.builder().id(1L).street("strada").number(4).city(city).isDefault(true).build());
        user.setAddresses(addresses);


        UserDTO userDTO = UserDTO.builder()
                .phone("0789456123")
                .lastName("Nedelcu")
                .firstName("Cosmin")
                .password("parola")
                .email("cosmin@yahoo.com")
                .build();

        when(this.userRepo.getUserByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));

        this.userService.removeAddress(user.getEmail(), 1L);
        then(this.userRepo).should().saveAndFlush(userArgumentCaptor.capture());
        then(this.orderRepo).should().updateOrderByAddressId(1L);
        assertThat(userArgumentCaptor.getValue().getAddresses().size()).isEqualTo(0);
    }

    @Test
    void shouldThrowExceptionRemoveAddress(){

        City city = new City("Bucuresti", "Romania");

        User user = new User();
        user.setEmail("cosmin@yahoo.com");
        user.setId(1L);
        List<Address> addresses = new ArrayList<>();
        addresses.add(Address.builder().id(2L).street("strada").number(4).city(city).isDefault(true).build());
        user.setAddresses(addresses);

        UserDTO userDTO = UserDTO.builder()
                .phone("0789456123")
                .lastName("Nedelcu")
                .firstName("Cosmin")
                .password("parola")
                .email("cosmin@yahoo.com")
                .build();

        when(this.userRepo.getUserByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));
        assertThrows(DeliveryCustomException.class, () -> this.userService.removeAddress(user.getEmail(), 1L));
    }

    @Test
    void shouldUpdateAddress(){

        City city = new City("Bucuresti", "Romania");

        User user = new User();
        user.setEmail("cosmin@yahoo.com");
        List<Address> addresses = new ArrayList<>();
        addresses.add(Address.builder().id(1L).street("alee").number(90).build());
        addresses.add(Address.builder().id(2L).street("strada").number(4).city(city).isDefault(true).build());

        user.setAddresses(addresses);


        UserDTO userDTO = UserDTO.builder().email("cosmin@yahoo.com").build();

        AddressDTO addressDTO= AddressDTO.builder()
                .street("Aleea Callatis")
                .number(3)
                .block("A12")
                .staircase("D")
                .floor(4)
                .apartment(59)
                .interphone("59")
                .isDefault(true)
                .cityName("Bucuresti")
                .build();

        when(this.userRepo.getUserByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));
        when(this.cityRepo.getCityByName(addressDTO.getCityName())).thenReturn(Optional.of(city));
        this.userService.updateAddress(user.getEmail(), 2L, addressDTO);
        then(this.userRepo).should().saveAndFlush(userArgumentCaptor.capture());
        assertThat(userArgumentCaptor.getValue().getAddresses().get(0).getIsDefault()).isFalse();
    }


}