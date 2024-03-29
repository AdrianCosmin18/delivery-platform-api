package com.example.deliveryapp.restaurant;

import com.example.deliveryapp.DTOs.ProductDTO;
import com.example.deliveryapp.DTOs.RestaurantDTO;
import com.example.deliveryapp.constants.Constants;
import com.example.deliveryapp.exceptions.DeliveryCustomException;
import com.example.deliveryapp.city.City;
import com.example.deliveryapp.image.Image;
import com.example.deliveryapp.product.Product;
import com.example.deliveryapp.city.CityRepo;
import com.example.deliveryapp.image.ImageRepo;
import com.example.deliveryapp.product.ProductRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
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
    ImageRepo imageRepo;

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
    public void addProduct(
            MultipartFile file,
            String name,
            Double price,
            String type,
            String description,
            String ingredients,
            String restaurantName,
            boolean containsGluten,
            boolean containsLactose,
            boolean isVegetarian
    ) throws IOException {
        if (this.productRepo.getProductByNameAndRestaurantName(name, restaurantName).isPresent()) {
            throw new DeliveryCustomException("Already exists this product in this restaurant");
        }

        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .type(type)
                .ingredients(ingredients)
                .containsGluten(containsGluten)
                .containsLactose(containsLactose)
                .isVegetarian(isVegetarian)
                .build();

        if(!file.isEmpty()){
            Image image = new Image();
            image.setName(file.getOriginalFilename());
            image.setData(file.getBytes());
            Image savedImage = imageRepo.save(image);

            product.setImage(image);
        }



        Restaurant restaurant = this.restaurantRepo.getRestaurantByName(restaurantName)
                .orElseThrow(() -> new DeliveryCustomException("There is no restaurant with this name"));


        restaurant.addProduct(product);
        this.restaurantRepo.save(restaurant);
    }


    @Override
    public List<ProductDTO> getRestaurantProducts(String restaurantName, String foodType){

        Restaurant restaurant = this.restaurantRepo.getRestaurantByName(restaurantName)
                .orElseThrow(() -> new DeliveryCustomException(Constants.RESTAURANT_NOT_FOUND_BY_NAME_EXCEPTION.getMessage()));

        List<Product> products = restaurant.getProducts().stream().filter(product -> product.getType().equals(foodType)).sorted(Comparator.comparingLong(Product::getId)).collect(Collectors.toList());
        if(products.isEmpty()){
            return new ArrayList<>();
        }else{

            List<ProductDTO> productDTOList = new ArrayList<>();
            for(Product p: products){

                ProductDTO productDTO = ProductDTO.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .price(p.getPrice())
                        .type(p.getType())
                        .description(p.getDescription())
                        .ingredients(p.getIngredients())
                        .imageId(p.getImage() != null ? p.getImage().getId() : -1)
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
                    .id(p.getId())
                    .name(p.getName())
                    .price(p.getPrice())
                    .type(p.getType())
                    .description(p.getDescription())
                    .ingredients(p.getIngredients())
                    .imageId(p.getImage() != null ? p.getImage().getId() : -1)                    .restaurantName(restaurant.getName())
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
