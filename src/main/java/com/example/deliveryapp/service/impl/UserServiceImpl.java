package com.example.deliveryapp.service.impl;

import com.example.deliveryapp.DTOs.*;
import com.example.deliveryapp.exceptions.DeliveryCustomException;
import com.example.deliveryapp.models.*;
import com.example.deliveryapp.models.embeddedKey.OrderItemId;
import com.example.deliveryapp.repos.*;
import com.example.deliveryapp.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
            throw new DeliveryCustomException("Already exists a user with this email");
        }
        if(this.userRepo.getUserByPhone(userDTO.getPhone()).isPresent()){
            throw new DeliveryCustomException("This phone number belongs to someone else");
        }
        userRepo.save(this.mapper.map(userDTO, User.class));
    }

    @Override
    public void addRestaurantToWishlist(String email, String restaurantName){
        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException("There is no user with this email"));

        Restaurant restaurant = this.restaurantRepo.getRestaurantByName(restaurantName)
                .orElseThrow(() -> new DeliveryCustomException("There is no restaurant with this name"));

        if(user.getRestaurants().stream().anyMatch(r -> r.getName().toLowerCase().equals(restaurantName.toLowerCase()))){
            throw new DeliveryCustomException("User already has this restaurant in his wishlist");
        }

        user.addRestaurant(restaurant);
        this.userRepo.save(user);
    }

    @Override
    public void removeRestaurantFromWishlist(String email, String restaurantName){
        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException("There is no user with this email"));

        Restaurant restaurant = this.restaurantRepo.getRestaurantByName(restaurantName)
                .orElseThrow(() -> new DeliveryCustomException("There is no restaurant with this name"));

        if(user.getRestaurants().stream().anyMatch(r -> r.getName().toLowerCase().equals(restaurantName.toLowerCase()))){
            user.removeRestaurant(restaurant);
            userRepo.save(user);
        }else{
            throw new DeliveryCustomException("User doesn't have this restaurant in his wishlist");
        }
    }

    @Override
    public void addAddress(String email, AddressDTO addressDTO){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException("There is no user with this email"));

        List<Address> userAddresses = user.getAddresses();

        City city = this.cityRepo.getCityByName(addressDTO.getCityName())
                .orElseThrow(() -> new DeliveryCustomException("There is no city with this name in the db"));

        Address address = new Address(addressDTO.getStreet(), addressDTO.getNumber());
        if(addressDTO.getIsDefault()){

            userAddresses.forEach(adr -> adr.setIsDefault(false));
            address.setIsDefault(true);
        }else{

            address.setIsDefault(false);
        }
        address.setCity(city);
        address.setUser(user);

        if(userAddresses.stream().anyMatch(adr -> adr.compare(address))){
            throw new DeliveryCustomException("User already has this address");
        }

        user.addAddress(address);
        this.userRepo.save(user);
    }

    @Override
    public void removeAddress(String email, AddressDTO addressDTO){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException("There is no user with this email"));

        City city = this.cityRepo.getCityByName(addressDTO.getCityName())
                .orElseThrow(() -> new DeliveryCustomException("There is no city with this name in the db"));

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

        throw new DeliveryCustomException("User does not have this address");
    }

    @Override
    public List<Address> getUserAddresses(String email){
        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException("There is no user with this email"));

        return user.getAddresses();
    }

    @Override
    public void addCard(String email, CardDTO cardDTO) {

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException("There is no user with this email"));

        Card card = Card.builder()
                .cardNumber(cardDTO.getCardNumber())
                .cardHolderName(cardDTO.getCardHolderName())
                .securityCode(cardDTO.getSecurityCode())
                .expiryDate(cardDTO.getExpiryDate().atDay(1))
                .build();

        List<Card> cards = user.getCards();
        if (cards.stream().anyMatch(c -> c.equals(card))){
            throw new DeliveryCustomException("Already added this card");
        }

        card.setUser(user);
        user.addCard(card);
        this.userRepo.save(user);
    }

    @Override
    public List<CardDTO> getUserCards(String email){

        User user = this.userRepo.getUserByEmail(email)
                .orElseThrow(() -> new DeliveryCustomException("There is no user with this email"));

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
                .orElseThrow(() -> new DeliveryCustomException("There is no user with this email"));

        List<Card> userCards = user.getCards();

        if(userCards.stream().anyMatch(card -> card.getCardNumber().equals(cardNumber))){

            List<Card> cards = userCards.stream().filter(card1 -> card1.getCardNumber().equals(cardNumber)).collect(Collectors.toList());
            user.deleteCard(cards.get(0));

            this.userRepo.save(user);
        }else{
            throw new DeliveryCustomException("User does not own this card");
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
                .orElseThrow(() -> new DeliveryCustomException("There is no user with this email"));


        List<OrderItem> orderItems = new ArrayList<>();
        Order currentOrder = this.orderRepo.saveAndFlush(new Order(0D, "creating"));

        for(ProductCart pc: orderRequest.getProductsInCart()){

            Product product = this.productRepo.getProductByNameAndRestaurantName( pc.getProductName(), pc.getRestaurantName())
                    .orElseThrow(() -> new DeliveryCustomException("No product from this restaurant"));

            OrderItemId orderItemId = new OrderItemId(currentOrder.getId(), product.getId());
            OrderItem orderItem = OrderItem.builder()
                    .id(orderItemId)
                    .quantity(pc.getQuantity())
                    .price(pc.getQuantity() * product.getPrice())
                    .build();

            product.addOrderItem(orderItem);
            orderItem.setOrder(currentOrder);
            orderItem.setProduct(product);

            orderItems.add(orderItem);
        }

        currentOrder.setUser(user);
        currentOrder.setOrderItems(orderItems);


        Card userCard = this.cardRepo.getCardByCardNumber(orderRequest.getCardNumber())
                .orElseThrow(() -> new DeliveryCustomException("No card in db with this number"));

        if(user.getCards().stream().noneMatch(card -> card.equals(userCard))){
            throw new DeliveryCustomException("Problems with user card, it seems that this card isn't assigned to this user");
        }

        currentOrder.setCard(userCard);
        currentOrder.setAmount(orderItems.stream().mapToDouble(OrderItem::getPrice).sum());

        Address userAddress = this.addressRepo.getFullAddress(
                orderRequest.getAddressDTO().getCityName(),
                orderRequest.getAddressDTO().getStreet(),
                orderRequest.getAddressDTO().getNumber())
                .orElseThrow(() -> new DeliveryCustomException("There is no address like this in db"));

        if(user.getAddresses().stream().noneMatch(address -> address.equals(userAddress))){
            throw new DeliveryCustomException("Problems with user address, it seems that user doesn't have this address in his list");
        }
        currentOrder.setAddress(userAddress);

        List<Courier> couriers = this.courierRepo.findAll();
        Courier randomCourier = couriers.get(new Random().nextInt(couriers.size()));
        currentOrder.setCourier(randomCourier);

        currentOrder.setStatus("Comanda este in preparare, cand va fi gata, curierul o va ridica");
        currentOrder.setDeliverTime(LocalDateTime.now().plusMinutes(45));

        this.orderRepo.saveAndFlush(currentOrder);
    }


}
