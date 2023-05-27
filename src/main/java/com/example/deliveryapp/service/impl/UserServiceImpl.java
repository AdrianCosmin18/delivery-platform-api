package com.example.deliveryapp.service.impl;

import com.example.deliveryapp.DTOs.*;
import com.example.deliveryapp.constants.Constants;
import com.example.deliveryapp.exceptions.DeliveryCustomException;
import com.example.deliveryapp.models.*;
import com.example.deliveryapp.models.embeddedKey.OrderItemId;
import com.example.deliveryapp.repos.*;
import com.example.deliveryapp.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RestaurantRepo restaurantRepo;
    @Autowired
    private CityRepo cityRepo;
    @Autowired
    private CardRepo cardRepo;
    @Autowired
    private ProductRepo productRepo;
//    @Autowired
//    private CartRepo cartRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private CourierRepo courierRepo;

    @Autowired
    private ModelMapper mapper;



    @Override
    public List<User> getUsers(){

        return this.userRepo.findAll();
    }

    @Override
    public void addUser(UserDTO userDTO){
        if(this.userRepo.getUserByEmail(userDTO.getEmail()).isPresent()){
            throw new DeliveryCustomException(Constants.USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION.getMessage());
        }
        if(this.userRepo.getUserByPhone(userDTO.getPhone()).isPresent()){
            throw new DeliveryCustomException(Constants.USER_ALREADY_EXISTS_PHONE_EXCEPTION.getMessage());
        }

        User user = new User(userDTO.getLastName(), userDTO.getFirstName(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getPhone());
        userRepo.saveAndFlush(user);
    }

    @Override
    public void addRestaurantToWishlist(String email, String restaurantName){
        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION.getMessage()));

        Restaurant restaurant = this.restaurantRepo.getRestaurantByName(restaurantName)
                .orElseThrow(() -> new DeliveryCustomException(Constants.RESTAURANT_NOT_FOUND_BY_NAME_EXCEPTION.getMessage()));

        if(user.getRestaurants().stream().anyMatch(r -> r.getName().toLowerCase().equals(restaurantName.toLowerCase()))){
            throw new DeliveryCustomException(Constants.WISHLIST_EXIST_EXCEPTION.getMessage());
        }

        user.addRestaurant(restaurant);
        this.userRepo.save(user);
    }

    @Override
    public void removeRestaurantFromWishlist(String email, String restaurantName){
        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION.getMessage()));

        Restaurant restaurant = this.restaurantRepo.getRestaurantByName(restaurantName)
                .orElseThrow(() -> new DeliveryCustomException(Constants.RESTAURANT_NOT_FOUND_BY_NAME_EXCEPTION.getMessage()));

        if(user.getRestaurants().stream().anyMatch(r -> r.getName().toLowerCase().equals(restaurantName.toLowerCase()))){
            user.removeRestaurant(restaurant);
            userRepo.save(user);
        }else{
            throw new DeliveryCustomException(Constants.WISHLIST_NOT_EXIST_EXCEPTION.getMessage());
        }
    }

    @Override
    public void addAddress(String email, AddressDTO addressDTO){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION.getMessage()));

        List<Address> userAddresses = user.getAddresses();

        City city = this.cityRepo.getCityByName(addressDTO.getCityName())
                .orElseThrow(() -> new DeliveryCustomException(Constants.CITY_NOT_FOUND_EXCEPTION.getMessage()));

        Address address = new Address(
                addressDTO.getStreet(),
                addressDTO.getNumber(),
                addressDTO.getBlock(),
                addressDTO.getStaircase(),
                addressDTO.getFloor(),
                addressDTO.getApartment(),
                addressDTO.getInterphone(),
                addressDTO.getDetails(),
                addressDTO.getIsDefault());
        if(addressDTO.getIsDefault()){

            userAddresses.forEach(adr -> adr.setIsDefault(false));
            address.setIsDefault(true);
        }else{

            address.setIsDefault(false);
        }


        if(userAddresses.stream().anyMatch(adr -> adr.compare(address))){
            throw new DeliveryCustomException(Constants.USER_ALREADY_OWN_ADDRESS_EXCEPTION.getMessage());
        }
        address.setCity(city);

        user.addAddress(address);
        this.userRepo.save(user);
    }

    @Override
    public void removeAddress(String email, AddressDTO addressDTO){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION.getMessage()));

        City city = this.cityRepo.getCityByName(addressDTO.getCityName())
                .orElseThrow(() -> new DeliveryCustomException(Constants.CITY_NOT_FOUND_EXCEPTION.getMessage()));

        List<Address> userAddresses = user.getAddresses();

        Address address = Address.builder()
                .street(addressDTO.getStreet())
                .number(addressDTO.getNumber())
                .city(city)
                .build();

        if(userAddresses.stream().anyMatch(adr -> adr.compare(address))){
            user.deleteAddress(address);
            this.userRepo.save(user);
        }

        throw new DeliveryCustomException(Constants.USER_NOT_OWN_ADDRESS_EXCEPTION.getMessage());
    }

    @Override
    public List<AddressDTO> getUserAddresses(String email){
        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION.getMessage()));

        List<AddressDTO> addressDTOList = new ArrayList<>();
        for (Address address: user.getAddresses()){

            AddressDTO addressDTO = AddressDTO.builder()
                    .id(address.getId())
                    .street(address.getStreet())
                    .number(address.getNumber())
                    .cityName(address.getCity().getName())
                    .apartment(address.getApartment())
                    .block(address.getBlock())
                    .staircase(address.getStaircase())
                    .floor(address.getFloor())
                    .interphone(address.getInterphone())
                    .details(address.getDetails())
                    .isDefault(address.getIsDefault())
            .build();

            addressDTOList.add(addressDTO);
        }
        return addressDTOList;
    }

    @Override
    public void setAsMainAddress(String email, long addressId){
        //functie care seteaza o adresa ca fiind principala =>
        //celelalte vor deveni neprincipale


        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION.getMessage()));

        List<Address> userAddresses = user.getAddresses();
        if(!userAddresses.stream().anyMatch(address -> address.getId() == addressId)){
            throw new DeliveryCustomException(Constants.ADDRESS_NOT_FOUND_EXCEPTION.getMessage());
        }else{

            userAddresses.forEach(adr -> adr.setIsDefault(false));
            userAddresses.forEach(adr -> {
                if(adr.getId() == addressId){
                    adr.setIsDefault(true);
                }
            });

            this.userRepo.saveAndFlush(user);
        }
    }

    @Override
    public void addCard(String email, CardDTO cardDTO) {

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_NOT_FOUND_BY_EMAIL.getMessage()));

        Card card = Card.builder()
                .cardNumber(cardDTO.getCardNumber())
                .cardHolderName(cardDTO.getCardHolderName())
                .securityCode(cardDTO.getSecurityCode())
                .expiryDate(cardDTO.getExpiryDate().atDay(1))
                .build();

        List<Card> cards = user.getCards();
        if (cards.stream().anyMatch(c -> c.equals(card))){
            throw new DeliveryCustomException(Constants.USER_CARD_ALREADY_EXISTS_EXCEPTION.getMessage());
        }

        user.addCard(card);
        this.userRepo.save(user);
    }

    @Override
    public List<CardDTO> getUserCards(String email){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_NOT_FOUND_BY_EMAIL.getMessage()));

        if(user.getCards().isEmpty()){
            return new ArrayList<>();
        }

        List<CardDTO> cardsDto = new ArrayList<>();
        for(Card c: user.getCards()){

            CardDTO cardDTO = CardDTO.builder()
                    .cardHolderName(c.getCardHolderName())
                    .cardNumber(c.getCardNumber())
                    .securityCode(c.getSecurityCode())
                    .expiryDate(YearMonth.of(c.getExpiryDate().getYear(), c.getExpiryDate().getMonth()))
                    .build();

            cardsDto.add(cardDTO);
        }

        return cardsDto;

    }

    @Override
    public void removeCard(String email, String cardNumber){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_NOT_FOUND_BY_EMAIL.getMessage()));

        List<Card> userCards = user.getCards();

        if(userCards.stream().anyMatch(card -> card.getCardNumber().equals(cardNumber))){

            List<Card> cards = userCards.stream().filter(card1 -> card1.getCardNumber().equals(cardNumber)).collect(Collectors.toList());
            user.deleteCard(cards.get(0));

            this.userRepo.save(user);
        }else{
            throw new DeliveryCustomException(Constants.USER_CARD_NOT_OWN_EXCEPTION.getMessage());
        }
    }

//    @Override
//    public boolean areProductsFromOtherRestaurantInCart(String email, String restaurantName, String currentProductName){
//
//        User user = this.userRepo.getUserByEmail(email)
//                .orElseThrow(() -> new DeliveryCustomException("No user with this email"));
//
//        if(user.getProductCart().isEmpty()){
//            return false;
//        }
//
//        Product product = this.productRepo.getProductByNameAndRestaurantName(currentProductName, restaurantName)
//                .orElseThrow(() -> new DeliveryCustomException("No product with this name in this restaurant"));
//
//        List<Cart> userCart = user.getProductCart();
//        return !userCart.get(0).getProduct().getRestaurant().getName().equals(product.getRestaurant().getName());
//    }
//
//    //daca cosul e gol, adaugam in el produsul
//    //daca nu e gol, verificam daca este un produs de la un alt restaurant
//    //  daca este, atunci golim cosul si adaugam acest produs produs
//    //daca nu e gol si este un produs de la acelasi restaurant, verificam daca este cumva acelasi produs
//    //  cu cel pe care incercam sa il adaugam acum, daca sunt identice, doar crestem cantitea cu cat am mai adaugat
//    //  daca nu este acelasi produs, atunci doar il adaugam
//    @Override
//    public void addProductToUserCart(String email, String restaurantName, String currentProductName, Integer quantity){
//
//        User user = this.userRepo.getUserByEmail(email)
//                .orElseThrow(() -> new DeliveryCustomException("No user with this email"));
//
//        Product product = this.productRepo.getProductByNameAndRestaurantName(currentProductName, restaurantName)
//                .orElseThrow(() -> new DeliveryCustomException("No product with this name in this restaurant"));
//
//        List<Cart> userCart = user.getProductCart();
//
//        if(user.getProductCart().isEmpty()){
//
//            CartId cartId = new CartId(user.getId(), product.getId());
//            Cart cart = new Cart(cartId, quantity, user, product);
//            this.cartRepo.save(cart);
//
//        } else if (this.areProductsFromOtherRestaurantInCart(email, restaurantName, currentProductName)) {
//
//            user.setProductCart(new ArrayList<>());
//            CartId cartId = new CartId(user.getId(), product.getId());
//            Cart cart = new Cart(cartId, quantity, user, product);
//            user.addProductCart(cart);
//            product.addUserCart(cart);
//            this.cartRepo.save(cart);
//
//        } else if (userCart.stream().map(c -> c.getProduct()).equals(product)) {
//
//            Cart cart = userCart.stream().filter(c -> c.getProduct().equals(product)).collect(Collectors.toList()).get(0);
//            cart.setQuantity(cart.getQuantity() + quantity);
//            this.cartRepo.save(cart);
//
//        } else if (!userCart.stream().map(c -> c.getProduct()).equals(product)) {
//
//            CartId cartId = new CartId(user.getId(), product.getId());
//            Cart cart = new Cart(cartId, quantity, user, product);
//            this.cartRepo.save(cart);
//        }
//    }

    @Override
    @Transactional
    public void placeOrder(CreateOrderRequest orderRequest){

        User user = this.userRepo.getUserByEmail(orderRequest.getEmailUser())
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_NOT_FOUND_BY_EMAIL.getMessage()));


        List<OrderItem> orderItems = new ArrayList<>();
        Order currentOrder = this.orderRepo.saveAndFlush(new Order(0D, "creating"));
        currentOrder.setOrderItems(new ArrayList<>());

        for(ProductCart pc: orderRequest.getProductsInCart()){

            Product product = this.productRepo.getProductByNameAndRestaurantName( pc.getProductName(), pc.getRestaurantName())
                    .orElseThrow(() -> new DeliveryCustomException(Constants.PRODUCT_NOT_FOUND_BY_RESTAURANT_AND_NAME.getMessage()));

            OrderItemId orderItemId = new OrderItemId(currentOrder.getId(), product.getId());
            OrderItem orderItem = OrderItem.builder()
                    .id(orderItemId)
                    .quantity(pc.getQuantity())
                    .price(pc.getQuantity() * product.getPrice())
                    .build();

            product.addOrderItem(orderItem);
            // orderItem.setProduct(product);
            //orderItem.setOrder(currentOrder);
            currentOrder.addOrderItem(orderItem);

            orderItems.add(orderItem);
        }


        user.addOrder(currentOrder);
        //currentOrder.setUser(user);
        //currentOrder.setOrderItems(orderItems);


//        Card userCard = this.cardRepo.getCardByCardNumber(orderRequest.getCardNumber())
//                .orElseThrow(() -> new DeliveryCustomException(Constants.CARD_NOT_FOUND_BY_NUMBER_EXCEPTION.getMessage()));

        Card userCard = user.getCards().stream().filter(card -> card.getCardNumber().equals(orderRequest.getCardNumber())).collect(Collectors.toList()).get(0);
        if(userCard == null){
            throw new DeliveryCustomException(Constants.USER_CARD_NOT_OWN_EXCEPTION.getMessage());
        }

        //currentOrder.setCard(userCard);
        userCard.addOrder(currentOrder);
        currentOrder.setAmount(orderItems.stream().mapToDouble(OrderItem::getPrice).sum());

        Address userAddress = user.getAddresses().stream().filter(
                address -> address.getCity().getName().equals(orderRequest.getAddressDTO().getCityName()) &&
                        address.getStreet().equals(orderRequest.getAddressDTO().getStreet()) &&
                        address.getNumber().equals(orderRequest.getAddressDTO().getNumber())).collect(Collectors.toList()).get(0);

        if(userAddress == null){
            throw new DeliveryCustomException(Constants.USER_NOT_OWN_ADDRESS_EXCEPTION.getMessage());
        }
        //currentOrder.setAddress(userAddress);

        userAddress.addOrder(currentOrder);

        List<Courier> couriers = this.courierRepo.findAll();
        Courier randomCourier = couriers.get(new Random().nextInt(couriers.size()));
        //currentOrder.setCourier(randomCourier);
        randomCourier.addOrder(currentOrder);

        currentOrder.setStatus("Comanda este in preparare, cand va fi gata, curierul o va ridica");
        currentOrder.setDeliverTime(LocalDateTime.now().plusMinutes(45));

        this.orderRepo.saveAndFlush(currentOrder);
    }

    @Override
    public UserDTO getUserByEmail(String email){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_NOT_FOUND_BY_EMAIL.getMessage()));

        return this.mapper.map(user, UserDTO.class);
    }

    public User findByEmail(String email){

        return this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_NOT_FOUND_BY_EMAIL.getMessage()));
    }

    @Override
    public Long findIdByUsername(String email){
        return this.userRepo.findIdByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException(Constants.USER_NOT_FOUND_BY_EMAIL.getMessage()));
    }

    @Override
    public String findFirstNameByUsername(String email){
        return this.userRepo.findFirstNameByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException(Constants.USER_NOT_FOUND_BY_EMAIL.getMessage()));
    }

    @Override
    public void updateUser(String email, UserDTO userDTO){
        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_NOT_FOUND_BY_EMAIL.getMessage()));

        user.setLastName(userDTO.getLastName());
        user.setFirstName(userDTO.getFirstName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());

        this.userRepo.saveAndFlush(user);
    }
}
