package com.example.deliveryapp.service.impl;

import com.example.deliveryapp.DTOs.ProductDTO;
import com.example.deliveryapp.DTOs.RestaurantDTO;
import com.example.deliveryapp.exceptions.DeliveryCustomException;
import com.example.deliveryapp.models.City;
import com.example.deliveryapp.models.Product;
import com.example.deliveryapp.models.Restaurant;
import com.example.deliveryapp.repos.CityRepo;
import com.example.deliveryapp.repos.ProductRepo;
import com.example.deliveryapp.repos.RestaurantRepo;
import com.example.deliveryapp.service.RestaurantService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepo restaurantRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CityRepo cityRepo;
    private ModelMapper mapper;

    public RestaurantServiceImpl() {this.mapper = new ModelMapper();}

    @Override
    public void addRestaurant(String restaurantName){
        if(this.restaurantRepo.getRestaurantByName(restaurantName).isPresent()){
            throw new DeliveryCustomException("Already exists this restaurant in the db");
        }
        this.restaurantRepo.save(new Restaurant(restaurantName));
    }

    @Override
    @Transactional
    public void deleteRestaurant(String name){
        if(this.restaurantRepo.getRestaurantByName(name).isEmpty()){
            throw new DeliveryCustomException("There is no restaurant in the db with this name");
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
    public void addProduct(ProductDTO productDTO) {
        if (this.productRepo.getProductByNameAndRestaurantName(productDTO.getName(), productDTO.getRestaurantName()).isPresent()) {
            throw new DeliveryCustomException("Already exists this product in this restaurant");
        }
        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .type(productDTO.getType())
                .picture(productDTO.getPicture())
                .ingredients(productDTO.getIngredients())
                .build();

        Restaurant restaurant = this.restaurantRepo.getRestaurantByName(productDTO.getRestaurantName())
                .orElseThrow(() -> new DeliveryCustomException("There is no restaurant with this name"));

        product.setRestaurant(restaurant);
        restaurant.addProduct(product);
    }

    @Override
    public void deleteProduct(String productName, String restaurantName) {

    }

    public void putRestaurantInACity(String restaurantName, String cityName){

        Restaurant restaurant = this.restaurantRepo.getRestaurantByName(restaurantName)
                .orElseThrow(() -> new DeliveryCustomException("There is no restaurant with this name"));

        City city = this.cityRepo.getCityByName(cityName)
                .orElseThrow(() -> new DeliveryCustomException("There is no city with this name"));

        if(restaurant.getCities().stream().anyMatch(c -> c.getName().toLowerCase().equals(city.getName().toLowerCase()))){
            throw new DeliveryCustomException("There is already this restaurant in this city");
        }

        restaurant.addCity(city);
        restaurantRepo.save(restaurant);
    }

    public void removeRestaurantFromCity(String restaurantName, String cityName){

        Restaurant restaurant = this.restaurantRepo.getRestaurantByName(restaurantName)
                .orElseThrow(() -> new DeliveryCustomException("There is no restaurant with this name"));

        City city = this.cityRepo.getCityByName(cityName)
                .orElseThrow(() -> new DeliveryCustomException("There is no city with this name"));

        List<City> cityList = restaurant.getCities();
        if(cityList.stream().anyMatch(c -> c.getName().toLowerCase().equals(cityName.toLowerCase()))){
            restaurant.removeFromCity(city);
            restaurantRepo.save(restaurant);
        }else{
            throw new DeliveryCustomException("This restaurant isn't present in this city");
        }
    }
}
