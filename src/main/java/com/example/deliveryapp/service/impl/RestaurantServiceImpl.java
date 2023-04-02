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
import com.example.deliveryapp.utils.ImageUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public void addProduct(MultipartFile file, String name, Double price, String type, String description, String ingredients, String restaurantName) throws IOException {
        if (this.productRepo.getProductByNameAndRestaurantName(name, restaurantName).isPresent()) {
            throw new DeliveryCustomException("Already exists this product in this restaurant");
        }

        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .type(type)
                .picture(ImageUtils.compressImage(file.getBytes()))
                .ingredients(ingredients)
                .build();

        Restaurant restaurant = this.restaurantRepo.getRestaurantByName(restaurantName)
                .orElseThrow(() -> new DeliveryCustomException("There is no restaurant with this name"));

        product.setRestaurant(restaurant);

        restaurant.addProduct(product);
        this.restaurantRepo.save(restaurant);
    }

    @Override
    public byte[] getImageProduct(String restaurantName, String productName){

        Product product = this.productRepo.getProductByNameAndRestaurantName(productName, restaurantName)
                .orElseThrow(() -> new DeliveryCustomException("No product from this restaurant"));
        return ImageUtils.decompressImage(product.getPicture());
    }

    @Override
    public List<ProductDTO> getRestaurantProducts(String restaurantName){

        Restaurant restaurant = this.restaurantRepo.getRestaurantByName(restaurantName)
                .orElseThrow(() -> new DeliveryCustomException("There is no restaruant with this name"));

        List<Product> products = restaurant.getProducts();
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
                        .picture(ImageUtils.decompressImage(p.getPicture()))
                        .restaurantName(restaurant.getName())
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
