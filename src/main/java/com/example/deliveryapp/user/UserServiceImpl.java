package com.example.deliveryapp.user;

import com.example.deliveryapp.DTOs.*;
import com.example.deliveryapp.address.Address;
import com.example.deliveryapp.address.AddressRepo;
import com.example.deliveryapp.card.Card;
import com.example.deliveryapp.card.CardRepo;
import com.example.deliveryapp.city.City;
import com.example.deliveryapp.city.CityRepo;
import com.example.deliveryapp.constants.Constants;
import com.example.deliveryapp.constants.OrderStatus;
import com.example.deliveryapp.courier.Courier;
import com.example.deliveryapp.courier.CourierRepo;
import com.example.deliveryapp.email.EmailSenderService;
import com.example.deliveryapp.exceptions.DeliveryCustomException;
import com.example.deliveryapp.order.Order;
import com.example.deliveryapp.order.OrderRepo;
import com.example.deliveryapp.orderItem.OrderItem;
import com.example.deliveryapp.product.Product;
import com.example.deliveryapp.product.ProductRepo;
import com.example.deliveryapp.restaurant.Restaurant;
import com.example.deliveryapp.restaurant.RestaurantRepo;
import com.example.deliveryapp.security.security.UserRole;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.example.deliveryapp.constants.Consts.MASTERCARD;
import static com.example.deliveryapp.constants.Consts.VISA;

@Service
@Transactional
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
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private CourierRepo courierRepo;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private EmailSenderService emailSenderService;



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
        address.setCity(city);
        if(addressDTO.getIsDefault()){

            userAddresses.forEach(adr -> adr.setIsDefault(false));
            address.setIsDefault(true);
        }else{

            address.setIsDefault(false);
        }


        if(userAddresses.stream().anyMatch(adr -> adr.compare(address))){
            throw new DeliveryCustomException(Constants.USER_ALREADY_OWN_ADDRESS_EXCEPTION.getMessage());
        }

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
    public void removeAddress(String email, long addressId){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_NOT_FOUND_BY_EMAIL.getMessage()));

        List<Address> userAddresses = user.getAddresses();


        if(userAddresses.stream().anyMatch(address -> address.getId().equals(addressId))){

            List<Address> addresses = user.getAddresses()
                    .stream()
                    .filter(adr -> adr.getId() == addressId)
                    .collect(Collectors.toList());

            Address address = addresses.get(0);
            user.deleteAddress(address);
            this.userRepo.saveAndFlush(user);
            this.orderRepo.updateOrderByAddressId(addressId);
        }else{
            throw new DeliveryCustomException(Constants.USER_NOT_OWN_ADDRESS_EXCEPTION.getMessage());
        }
    }

    public int indexAddress(long addressId, List<Address> addresses){
        for(int i = 0; i < addresses.size(); i++){
            if(addresses.get(i).getId() == addressId){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void updateAddress(String email, long addressId, AddressDTO addressDTO){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION.getMessage()));

        City city = this.cityRepo.getCityByName(addressDTO.getCityName())
                .orElseThrow(() -> new DeliveryCustomException(Constants.CITY_NOT_FOUND_EXCEPTION.getMessage()));

        List<Address> addresses = user.getAddresses().stream().filter(adr -> adr.getId() == addressId).collect(Collectors.toList());
        if (addresses.size() > 1){
            throw new DeliveryCustomException("Error: there are more addresses with the same id");
        }

        Address userAddress = addresses.get(0);

        if(addressDTO.getIsDefault()){
            user.getAddresses().forEach(address -> address.setIsDefault(false));
        }

        userAddress.setIsDefault(addressDTO.getIsDefault());
        userAddress.setStreet(addressDTO.getStreet());
        userAddress.setNumber(addressDTO.getNumber());
        userAddress.setBlock(addressDTO.getBlock());
        userAddress.setStaircase(addressDTO.getStaircase());
        userAddress.setFloor(addressDTO.getFloor());
        userAddress.setApartment(addressDTO.getApartment());
        userAddress.setInterphone(addressDTO.getInterphone());
        userAddress.setDetails(addressDTO.getDetails());
        userAddress.setIsDefault(addressDTO.getIsDefault());
        userAddress.setCity(city);

        this.userRepo.saveAndFlush(user);
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
    public boolean isAnyMainAddress(String email){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION.getMessage()));

        for(Address address: user.getAddresses()){
            if(address.getIsDefault()){
                return true;
            }
        }
        return false;
    }

    @Override
    public AddressDTO getMainAddress(String email){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION.getMessage()));

        if(!this.isAnyMainAddress(email)){
            throw new DeliveryCustomException("There is no main address for this user");
        }

        Address address = user.getAddresses().stream().filter(Address::getIsDefault).collect(Collectors.toList()).get(0);

        return AddressDTO.builder()
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
    }

    @Override
    public void addCard(String email, CardDTO cardDTO) {

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_NOT_FOUND_BY_EMAIL.getMessage()));

        List<Card> userCards = user.getCards();
        int year = cardDTO.getExpiryDate().getYear();
        int month = cardDTO.getExpiryDate().getMonthValue();
        int lastDayOfMonth = YearMonth.of(year, month).lengthOfMonth();
        Card card = Card.builder()
                .cardNumber(cardDTO.getCardNumber())
                .cardHolderName(cardDTO.getCardHolderName())
                .securityCode(cardDTO.getSecurityCode())
                .expiryDate(cardDTO.getExpiryDate().atDay(lastDayOfMonth))
                .build();
        switch (cardDTO.getCardNumber().charAt(0)){
            case '2':{
                card.setCardType(MASTERCARD);
                break;
            }
            case '4':{
                card.setCardType(VISA);
                break;
            }
            case '5':{
                card.setCardType(MASTERCARD);
                break;
            }
            default:{
                throw new DeliveryCustomException(Constants.CARD_NOT_VALID.getMessage());
            }
        }

        if(cardDTO.getIsDefault()){
            userCards.forEach(c -> c.setIsDefault(false));
            card.setIsDefault(true);
        }else{
            card.setIsDefault(false);
        }

        if (userCards.stream().anyMatch(c -> c.equals(card))){
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
                    .id(c.getId())
                    .cardHolderName(c.getCardHolderName())
                    .cardNumber("***" + c.getCardNumber().substring(c.getCardNumber().length() - 4))
                    .securityCode(c.getSecurityCode())
                    .expiryDate(YearMonth.of(c.getExpiryDate().getYear(), c.getExpiryDate().getMonth()))
                    .isDefault(c.getIsDefault())
                    .cardType(c.getCardType())
                    .fullExpiryDate(c.getExpiryDate())
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
            Card card = cards.get(0);
            card.getOrders().forEach(card::deleteOrder);
            user.deleteCard(cards.get(0));

            this.userRepo.save(user);
        }else{
            throw new DeliveryCustomException(Constants.USER_CARD_NOT_OWN_EXCEPTION.getMessage());
        }
    }

    @Override
    public void removeCard(String email, long cardId){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_NOT_FOUND_BY_EMAIL.getMessage()));
        List<Card> userCards = user.getCards();

        if(userCards.stream().anyMatch(card -> card.getId().equals(cardId))){

            List<Card> cards = userCards.stream().filter(card1 -> card1.getId() == cardId).collect(Collectors.toList());
            Card card = cards.get(0);
            user.deleteCard(cards.get(0));
            this.userRepo.saveAndFlush(user);
            this.orderRepo.updateOrderByCardId(cardId);
        }else{
            throw new DeliveryCustomException(Constants.USER_CARD_NOT_OWN_EXCEPTION.getMessage());
        }
    }


    @Override
    public void setAsMainCard(String email, long cardId){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION.getMessage()));

        List<Card> userCards = user.getCards();
        if(!userCards.stream().anyMatch(card -> card.getId() == cardId)) {
            throw new DeliveryCustomException(Constants.CARD_NOT_FOUND_BY_NUMBER_EXCEPTION.getMessage());
        }else{
            userCards.forEach(card -> card.setIsDefault(false));
            userCards.forEach(card -> {
                if(card.getId() == cardId){
                    card.setIsDefault(true);
                }
            });
            this.userRepo.saveAndFlush(user);
        }

    }

    @Override
    public boolean isAnyMainCard(String email){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION.getMessage()));

        for(Card card: user.getCards()){
            if(card.getIsDefault()){
                return true;
            }
        }
        return false;
    }

    @Override
    public CardDTO getMainCard(String email){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION.getMessage()));

        if(!this.isAnyMainCard(email)){
            throw new DeliveryCustomException("There is no main card for this user");
        }

        Card c = user.getCards().stream().filter(Card::getIsDefault).collect(Collectors.toList()).get(0);

        return CardDTO.builder()
                .id(c.getId())
                .cardHolderName(c.getCardHolderName())
                .cardNumber("***" + c.getCardNumber().substring(c.getCardNumber().length() - 4))
                .securityCode(c.getSecurityCode())
                .expiryDate(YearMonth.of(c.getExpiryDate().getYear(), c.getExpiryDate().getMonth()))
                .isDefault(c.getIsDefault())
                .cardType(c.getCardType())
                .fullExpiryDate(c.getExpiryDate())
                .build();
    }

    @Override
    @Transactional
    public void placeOrder(CreateOrderRequest orderRequest){

        User user = this.userRepo.getUserByEmail(orderRequest.getEmailUser())
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_NOT_FOUND_BY_EMAIL.getMessage()));


        List<OrderItem> orderItems = new ArrayList<>();
        Order currentOrder = this.orderRepo.saveAndFlush(new Order(0D, "creating", 0D, LocalDateTime.now()));
        currentOrder.setOrderItems(new ArrayList<>());

        for(OrderItemDTO pc: orderRequest.getProductsInCart()){

            Product product = this.productRepo.getProductByNameAndRestaurantName( pc.getProductName(), pc.getRestaurantName())
                    .orElseThrow(() -> new DeliveryCustomException(Constants.PRODUCT_NOT_FOUND_BY_RESTAURANT_AND_NAME.getMessage()));

            OrderItem orderItem = OrderItem.builder()
                    .quantity(pc.getQuantity())
                    .price(pc.getPrice())
                    .extraIngredients(pc.getExtraIngredients())
                    .lessIngredients(pc.getLessIngredients())
                    .build();

            product.addOrderItem(orderItem);
            currentOrder.addOrderItem(orderItem);

            orderItems.add(orderItem);
        }

        currentOrder.setCommentsSection(orderRequest.getCommentsSection());
        currentOrder.setTipsTax(orderRequest.getTipsTax());
        currentOrder.setDeliveryTax(orderRequest.getDeliveryTax());
        currentOrder.setProductsAmount(orderRequest.getProductsAmount());
        currentOrder.setAmount(orderRequest.getTotalAmount());
        user.addOrder(currentOrder);


        Card userCard = user.getCards().stream().filter(card -> card.getId() == orderRequest.getCardId()).collect(Collectors.toList()).get(0);
        if(userCard == null){
            throw new DeliveryCustomException(Constants.USER_CARD_NOT_OWN_EXCEPTION.getMessage());
        }

        userCard.addOrder(currentOrder);
        String initialCardNumber = "***" + userCard.getCardNumber().substring(userCard.getCardNumber().length() - 4);
        currentOrder.setInitialCardNumber(initialCardNumber);

        Address userAddress = user.getAddresses().stream().filter(adr -> adr.getId() == orderRequest.getAddressId()).collect(Collectors.toList()).get(0);
        if(userAddress == null){
            throw new DeliveryCustomException(Constants.USER_NOT_OWN_ADDRESS_EXCEPTION.getMessage());
        }

        userAddress.addOrder(currentOrder);

        String initialAddress =  userAddress.getStreet() + ", nr." +
                userAddress.getNumber() + ", " +
                userAddress.getCity().getName();
        currentOrder.setInitialAddress(initialAddress);
        currentOrder.setInitialCityName(userAddress.getCity().getName());

        List<Courier> couriers = this.courierRepo.findAll();
        Courier randomCourier = couriers.get(new Random().nextInt(couriers.size()));
        randomCourier.addOrder(currentOrder);

        currentOrder.setStatus(OrderStatus.PLACED_ORDER);
        currentOrder.setPlacedOrderTime(LocalDateTime.now());

        this.emailSenderService.sendEmail(user.getEmail(), "Comandă plasată - BurgerShop",
                this.formPlaceOrderSendMail(
                        user.getLastName(),
                        user.getFirstName(),
                        currentOrder.getId(),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).toString(),
                        initialAddress,
                        orderRequest,
                        initialCardNumber
                ));

        this.orderRepo.saveAndFlush(currentOrder);
    }

    private String formPlaceOrderSendMail(String lastName,
                                        String firstName,
                                        long orderId,
                                        String time,
                                        String address,
                                        CreateOrderRequest orderRequest,
                                        String cardNumber){
        String s = String.format("" +
                "Dragă %s %s,\n" +
                "\n" +
                "Îți mulțumim că ai ales BurgerShop pentru a-ți satisface pofta de burgeri delicioși. Comanda ta a fost plasată cu succes și suntem nerăbdători să-ți oferim o experiență culinară deosebită.\n" +
                "\n" +
                "Detalii comandă:\n" +
                "\n" +
                "Număr comandă: %d\n" +
                "Data și ora plasării: %s\n" +
                "Adresa de livrare: %s\n" +
                "\n" +
                "Produse comandate:\n" +
                "\n", lastName, firstName, orderId, time, address);

        for(OrderItemDTO o: orderRequest.getProductsInCart()){
            s += String.format("%s X %s\n", o.getProductName(), o.getQuantity().toString());
        }

        s += String.format(
                        "\nSumar comandă:\n" +
                        "\n" +
                        "Total produse: %f\n" +
                        "Taxe livrare: %f\n" +
                        "Total plată: %f\n" +
                        "\n" +
                        "Plata card: %s\n" +
                        "\n" +
                        "Echipa noastră pregătește cu atenție comanda ta și se va asigura că toate preparatele sunt proaspete și delicioase. În cazul în care ai specificat o opțiune personalizată sau cerințe speciale în legătură cu comanda, vom avea grijă să le respectăm în măsura posibilului.\n" +
                        "\n" +
                        "Te vom ține la curent cu starea comenzii și te vom notifica imediat ce comanda ta este pregătită și în drum spre adresa specificată.\n" +
                        "\n" +
                        "Cu drag,\n" +
                        "Echipa BurgerShop",
                orderRequest.getProductsAmount(),
                orderRequest.getDeliveryTax(),
                orderRequest.getTotalAmount(),
                cardNumber);

        return s;
    }

    @Override
    public List<OrderDTO> getAllHistoryOrders(String email){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_NOT_FOUND_BY_EMAIL.getMessage()));

        List<Order> userOrders =
                user.getOrders()
                        .stream()
                        .sorted(Comparator.comparing(Order::getPlacedOrderTime).reversed())
                        .collect(Collectors.toList());

        List<OrderDTO> userOrdersDto = new ArrayList<>();
        for(Order order: userOrders){

            String addressToString = order.getInitialAddress();
            String city = order.getInitialCityName();
            if(order.getAddress() != null){
                addressToString = order.getAddress().getStreet() + ", nr." +
                        order.getAddress().getNumber() + ", " +
                        order.getAddress().getCity().getName();

                city = order.getAddress().getCity().getName();
            }

            String cardNumber = order.getInitialCardNumber();
            if(order.getCard() != null){
                cardNumber = "***" + order.getCard().getCardNumber().substring(order.getCard().getCardNumber().length() - 4);
            }
            OrderDTO orderDTO = OrderDTO.builder()
                    .amount(order.getAmount())
                    .commentsSection(order.getCommentsSection())
                    .status(order.getStatus())
                    .deliverTime(order.getDeliveredTime())
                    .paymentConfirmed(order.getPaymentConfirmed())
                    .orderInPreparation(order.getOrderInPreparation())
                    .orderInDelivery(order.getOrderInDelivery())
                    .canceledOrder(order.getCanceledOrder())
                    .placedOrderTime(order.getPlacedOrderTime().toString())
                    .deliveryTax(order.getDeliveryTax())
                    .tipsTax(order.getTipsTax())
                    .productsAmount(order.getProductsAmount())
                    .id(order.getId())
                    .addressToString(addressToString)
                    .city(city)
                    .cardNumber(cardNumber)
                    .username(user.getLastName() + " " + user.getFirstName())
                    .build();

            userOrdersDto.add(orderDTO);
        }

        return userOrdersDto;
    }

    @Override
    public void confirmReceivedOrder(String email, long orderId){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_NOT_FOUND_BY_EMAIL.getMessage()));

        List<Order> orders = user.getOrders().stream().filter(order -> order.getId() == orderId).collect(Collectors.toList());
        if (orders.size() != 1) {
            throw new DeliveryCustomException("User does not have order with this id");
        }
        Order userOrder = orders.get(0);
        userOrder.setStatus(OrderStatus.ORDER_DELIVERED);
        userOrder.setDeliveredTime(LocalDateTime.now().toString());

        this.emailSenderService.sendEmail(user.getEmail(), "Comandă livrată cu succes - BurgerShop",
                this.formReceivedOrderSendEmail(
                        user.getLastName(),
                        user.getFirstName(),
                        orderId,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).toString(),
                        userOrder.getInitialAddress(),
                        userOrder.getAmount(),
                        userOrder.getInitialCardNumber()
                ));


        this.userRepo.saveAndFlush(user);
    }

    private String formReceivedOrderSendEmail(String lastName,
                                              String firstName,
                                              long orderId,
                                              String time,
                                              String address,
                                              double totalAmount,
                                              String cardNumber){
        String s = String.format("" +
                "Dragă %s %s,\n" +
                "\n" +
                "Îți mulțumim că ai ales BurgerShop pentru a-ți satisface pofta de burgeri delicioși. Comanda ta a fost livrată cu succes și suntem nerăbdători să auzim părerea ta.\n" +
                "\n" +
                "Detalii comandă:\n" +
                "\n" +
                "Număr comandă: %d\n" +
                "Data și ora livrării: %s\n" +
                "Adresa de livrare: %s\n" +
                "\n", lastName, firstName, orderId, time, address);


        s += String.format(
                        "Total plată: %f\n" +
                        "\n" +
                        "Plata card: %s\n" +
                        "\n" +
                        "Cu drag,\n" +
                        "Echipa BurgerShop",
                totalAmount,
                cardNumber);

        return s;
    }

    @Override
    public void cancelOrder(String email, long orderId){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_NOT_FOUND_BY_EMAIL.getMessage()));
        List<Order> orders = user.getOrders().stream().filter(order -> order.getId() == orderId).collect(Collectors.toList());
        if (orders.size() != 1) {
            throw new DeliveryCustomException("User does not have order with this id");
        }
        Order userOrder = orders.get(0);
        userOrder.setStatus(OrderStatus.CANCELED_ORDER);
        userOrder.setCanceledOrder(LocalDateTime.now().toString());

        this.emailSenderService.sendEmail(user.getEmail(), "Comandă anulată - BurgerShop",
                this.cancelOrderSendMail(
                        user.getLastName(),
                        user.getFirstName(),
                        orderId,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).toString()
                ));

        this.userRepo.saveAndFlush(user);
    }

    private String cancelOrderSendMail(String lastName,
                                      String firstName,
                                      long orderId,
                                      String time){
        String s = String.format("" +
                "Dragă %s %s,\n" +
                "\n" +
                "Ne pare rău sa vedem că ai ales să anulezi comanda\n" +
                "\n" +
                "Detalii comandă:\n" +
                "\n" +
                "Număr comandă: %d\n" +
                "Data și ora anulării: %s\n" +
                "Adresa de livrare: %s\n" +
                "\n", lastName, firstName, orderId, time);


        s += String.format("Cu drag,\nEchipa BurgerShop");

        return s;
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


        this.emailSenderService.sendEmail(
                userDTO.getEmail(),
                "Informații actualizate cu succes - Detalii personale",
                this.formChangePersonalInfoSendMail(userDTO, user.getLastName(), user.getFirstName()));

        this.userRepo.saveAndFlush(user);
    }

    @Override
    public void makeUserAsAdmin(String email){
        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException(Constants.USER_NOT_FOUND_BY_EMAIL.getMessage()));

        user.setUserRole(UserRole.ADMIN);
        this.userRepo.saveAndFlush(user);
    }

    @Override
    public void changePassword(String email, String newPassword){

        User user = this.findByEmail(email);
        user.setPassword(newPassword);
        this.emailSenderService.sendEmail(
                email,
                "Parola ta a fost modificată cu succes - Informații importante",
                this.formChangePasswordSendMail(user.getLastName(), user.getFirstName(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).toString()));
        this.userRepo.saveAndFlush(user);
    }

    private String formChangePersonalInfoSendMail(UserDTO userDTO, String oldLastname, String oldFirstname){
        return String.format("" +
                "Dragă %s %s,\n" +
                "\n" +
                "Îți confirmăm că datele tale personale au fost actualizate cu succes în aplicația BurgerShop. Echipa noastră a preluat noile informații și le-a integrat în sistemul nostru.\n" +
                "\n" +
                "Iată detaliile tale personale actualizate:\n" +
                "\n" +
                "Nume: %s\n" +
                "Prenume: %s\n" +
                "Număr de telefon: %s\n" +
                "Adresă de email: %s\n\n" +
                "Dacă modificările de mai sus nu sunt corecte sau ai întrebări suplimentare, te rugăm să ne contactezi cât mai curând posibil. Echipa noastră de asistență va fi bucuroasă să te ajute și să corecteze orice informație necesară.\n" +
                "\n" +
                "Suntem dedicați oferirii unei experiențe de livrare fără probleme și de calitate, iar informațiile tale actualizate ne vor ajuta să livrăm comenzile tale în mod corespunzător și să fim în permanență conectați cu tine.\n" +
                "\n" +
                "Te mulțumim pentru încrederea acordată și pentru că faci parte din comunitatea noastră de iubitori de burgeri. Ne dorim să-ți oferim cele mai gustoase și autentice preparate, direct la ușa ta.\n" +
                "\n" +
                "Cu poftă,\n" +
                "Echipa noastră de burgeri.", oldLastname, oldFirstname, userDTO.getLastName(), userDTO.getFirstName(), userDTO.getPhone(), userDTO.getEmail());
    }

    private String formChangePasswordSendMail(String lastname, String firstname, String time){
        return String.format("" +
                "Dragă %s %s,\n" +
                "\n" +
                "Îți confirmăm că parola contului tău în aplicația noastră BurgerShop a fost modificată cu succes. Ne dorim să te asigurăm că securitatea contului tău este o prioritate pentru noi și am luat toate măsurile necesare pentru a proteja datele tale personale.\n" +
                "\n" +
                "Dacă tu nu ai efectuat această modificare sau consideri că contul tău a fost compromis, te rugăm să ne contactezi imediat. Echipa noastră de suport tehnic va investiga situația și va lua măsurile necesare pentru a-ți proteja contul.\n" +
                "\n" +
                "În cazul în care ai fost tu cel care a solicitat modificarea parolei, te rugăm să ții cont de următoarele informații:\n" +
                "\n" +
                "Data și ora modificării parolei: %s\n" +
                "În cazul în care ai întâmpinat orice dificultăți în procesul de schimbare a parolei sau ai întrebări suplimentare, nu ezita să ne contactezi. Suntem aici pentru a te ajuta și pentru a asigura funcționarea corespunzătoare a contului tău.\n" +
                "\n" +
                "Îți recomandăm să menții parola contului tău confidențială și să o schimbi periodic în scopul securității. De asemenea, te sfătuim să utilizezi o parolă puternică, care să conțină o combinație de litere, cifre și caractere speciale.\n" +
                "\n" +
                "Îți mulțumim pentru înțelegere și cooperare. Ne dorim să-ți oferim o experiență sigură și plăcută în utilizarea aplicației noastre de livrare cu burgeri.\n" +
                "\n" +
                "Cu stimă,\n" +
                "Echipa noastră de burgeri", firstname, lastname, time);
    }
}