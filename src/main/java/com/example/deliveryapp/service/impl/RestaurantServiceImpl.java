package com.example.deliveryapp.service.impl;

import com.example.deliveryapp.DTOs.ProductDTO;
import com.example.deliveryapp.DTOs.RestaurantDTO;
import com.example.deliveryapp.constants.Constants;
import com.example.deliveryapp.constants.FoodType;
import com.example.deliveryapp.exceptions.DeliveryCustomException;
import com.example.deliveryapp.models.City;
import com.example.deliveryapp.models.Product;
import com.example.deliveryapp.models.Restaurant;
import com.example.deliveryapp.repos.CityRepo;
import com.example.deliveryapp.repos.ProductRepo;
import com.example.deliveryapp.repos.RestaurantRepo;
import com.example.deliveryapp.service.RestaurantService;
import com.example.deliveryapp.utils.ImageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.deliveryapp.constants.Consts.BURGER_SHOP;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepo restaurantRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CityRepo cityRepo;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;



    @Override
    public void addRestaurant(String restaurantName){
        if(this.restaurantRepo.getRestaurantByName(restaurantName).isPresent()){
            throw new DeliveryCustomException(Constants.RESTAURANT_NAME_ALREADY_EXISTS_EXCEPTION.getMessage());
        }
        this.restaurantRepo.save(new Restaurant(restaurantName));
    }

    @Override
    @Transactional
    public void deleteRestaurant(String name){
        if(this.restaurantRepo.getRestaurantByName(name).isEmpty()){
            throw new DeliveryCustomException(Constants.RESTAURANT_NOT_FOUND_BY_NAME_EXCEPTION.getMessage());
        }
        this.restaurantRepo.deleteRestaurantByName(name);
    }

    @Override
    public List<RestaurantDTO> getRestaurants(){
        List<Restaurant> restaurants = this.restaurantRepo.findAll();
        if (restaurants.isEmpty()){
            return new ArrayList<>();
        }

        List<RestaurantDTO> restaurantDTOS = new ArrayList<>();
        for(Restaurant r: restaurants){
            restaurantDTOS.add(this.mapper.map(r, RestaurantDTO.class));
        }

        return restaurantDTOS;
    }


    @Override
    public void addProduct(String file, String name, Double price, String type, String description, String ingredients, String restaurantName) throws IOException {

    }

    @Override
    public void addProduct(MultipartFile file, String name, Double price, String type, String description, String ingredients, String restaurantName) throws IOException {
        if (this.productRepo.getProductByNameAndRestaurantName(name, restaurantName).isPresent()) {
            throw new DeliveryCustomException("Already exists this product in this restaurant");
        }

        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .type(type)
//                .picture(ImageUtils.compressImage(file.getBytes()))
                .ingredients(ingredients)
                .build();

        Restaurant restaurant = this.restaurantRepo.getRestaurantByName(restaurantName)
                .orElseThrow(() -> new DeliveryCustomException("There is no restaurant with this name"));

        product.setRestaurant(restaurant);

        restaurant.addProduct(product);
        this.restaurantRepo.save(restaurant);
    }


    @Override
    public List<ProductDTO> getRestaurantProducts(String restaurantName, String foodType){

        Restaurant restaurant = this.restaurantRepo.getRestaurantByName(restaurantName)
                .orElseThrow(() -> new DeliveryCustomException(Constants.RESTAURANT_NOT_FOUND_BY_NAME_EXCEPTION.getMessage()));

        List<Product> products = restaurant.getProducts().stream().filter(product -> product.getType().equals(foodType)).collect(Collectors.toList());
        if(products.isEmpty()){
            return new ArrayList<>();
        }else{

            List<ProductDTO> productDTOList = new ArrayList<>();
            for(Product p: products){

                ProductDTO productDTO = ProductDTO.builder()
                        .name(p.getName())
                        .price(p.getPrice())
                        .type(p.getType())
                        .description(p.getDescription())
                        .ingredients(p.getIngredients())
//                        .image(p.getImage().getData())
                        .restaurantName(restaurant.getName())
                        .containsLactose(p.getContainsLactose())
                        .containsGluten(p.getContainsGluten())
                        .isVegetarian(p.getIsVegetarian())
                        .build();

                productDTOList.add(productDTO);
            }

            return productDTOList;
        }
    }

//    public byte[] getProductImage(byte[] img){
//
//        ResponseEntity<byte[]> responseEntity = ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType.valueOf("image/png"))
//                .body(img);
//        return responseEntity.getBody();
//    }
    public String convertByteToBase64Picture(byte[] imageBytes){
        String imageBase64 = Base64Utils.encodeToString(imageBytes);
        return "data:image/png;base64," + imageBase64;
    }

    @Override
    public void deleteProduct(String productName, String restaurantName) {

    }

    public void putRestaurantInACity(String restaurantName, String cityName){

        Restaurant restaurant = this.restaurantRepo.getRestaurantByName(restaurantName)
                .orElseThrow(() -> new DeliveryCustomException(Constants.RESTAURANT_NOT_FOUND_BY_NAME_EXCEPTION.getMessage()));

        City city = this.cityRepo.getCityByName(cityName)
                .orElseThrow(() -> new DeliveryCustomException(Constants.CITY_NOT_FOUND_EXCEPTION.getMessage()));

        if(restaurant.getCities().stream().anyMatch(c -> c.getName().toLowerCase().equals(city.getName().toLowerCase()))){
            throw new DeliveryCustomException(Constants.RESTAURANT_ALREADY_EXISTS_IN_CITY_EXCEPTION.getMessage());
        }

        restaurant.addCity(city);
        restaurantRepo.save(restaurant);
    }

    public void removeRestaurantFromCity(String restaurantName, String cityName){

        Restaurant restaurant = this.restaurantRepo.getRestaurantByName(restaurantName)
                .orElseThrow(() -> new DeliveryCustomException(Constants.RESTAURANT_NOT_FOUND_BY_NAME_EXCEPTION.getMessage()));

        City city = this.cityRepo.getCityByName(cityName)
                .orElseThrow(() -> new DeliveryCustomException(Constants.CITY_NOT_FOUND_EXCEPTION.getMessage()));

        List<City> cityList = restaurant.getCities();
        if(cityList.stream().anyMatch(c -> c.getName().toLowerCase().equals(cityName.toLowerCase()))){
            restaurant.removeFromCity(city);
            restaurantRepo.save(restaurant);
        }else{
            throw new DeliveryCustomException(Constants.RESTAURANT_NOT_PRESENT_IN_CITY_EXCEPTION.getMessage());
        }
    }
    @Override
    public ProductDTO getProductByName(String restaurantName, String productName){

        Restaurant restaurant = this.restaurantRepo.getRestaurantByName(restaurantName)
                .orElseThrow(() -> new DeliveryCustomException(Constants.RESTAURANT_NOT_FOUND_BY_NAME_EXCEPTION.getMessage()));
        List<Product> products = restaurant.getProducts().stream().filter(product -> product.getName().equals(productName)).collect(Collectors.toList());

        if(products.size() == 1){
            Product p = products.get(0);
            return ProductDTO.builder()
                    .name(p.getName())
                    .price(p.getPrice())
                    .type(p.getType())
                    .description(p.getDescription())
                    .ingredients(p.getIngredients())
//                    .picture(p.getPicture())
                    .restaurantName(restaurant.getName())
                    .containsLactose(p.getContainsLactose())
                    .containsGluten(p.getContainsGluten())
                    .isVegetarian(p.getIsVegetarian())
                    .build();
        }

        throw new DeliveryCustomException("No product in this restaurant with this name");
    }

    @Override
    public Set<ProductDTO> getProductsByIngredients(String foodType, String ingredientList){

        List<ProductDTO> products = this.getRestaurantProducts(BURGER_SHOP, foodType);
        List<String> ingredientsL = List.of(ingredientList.split(","));
        Set<ProductDTO> productDTOList = new HashSet<>();


        for(ProductDTO p: products){
            for(String ingr: ingredientsL){
                if(this.checkIfProductContainsIngredient(p, ingr)){
                    productDTOList.add(p);
                }
            }
        }
        return productDTOList;
    }

    boolean checkIfProductContainsIngredient(ProductDTO product, String ingredient){

        List<String> ingredients = List.of(product.getIngredients().split(","));
        for(String ingr: ingredients){
            if(ingr.equals(ingredient)){
                return true;
            }
        }
        return false;
    }
}
