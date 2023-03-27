package com.example.deliveryapp.controllers;

import com.example.deliveryapp.DTOs.ProductDTO;
import com.example.deliveryapp.DTOs.RestaurantDTO;
import com.example.deliveryapp.constants.Response;
import com.example.deliveryapp.exceptions.InsertPictureException;
import com.example.deliveryapp.models.Product;
import com.example.deliveryapp.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("delivery-app/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/{restaurantName}")
    public void addRestaurant(@PathVariable String restaurantName){
        this.restaurantService.addRestaurant(restaurantName);
    }

    @GetMapping()
    public ResponseEntity<List<RestaurantDTO>> getRestaurants(){
        return new ResponseEntity<List<RestaurantDTO>>(this.restaurantService.getRestaurants(), HttpStatus.OK);
    }

    @PostMapping("/put-restaurant-to-city")
    public void putRestaurantInACity(@RequestParam(value = "restaurantName")String restaurantName, @RequestParam(value = "cityName") String cityName){
        this.restaurantService.putRestaurantInACity(restaurantName, cityName);
    }

    @DeleteMapping("/{restaurantName}")
    public void deleteRestaurant(@PathVariable String restaurantName){
        this.restaurantService.deleteRestaurant(restaurantName);
    }

    @DeleteMapping("/remove-restaurant-from-city")
    public void removeRestaurantFromCity(@RequestParam(value = "restaurantName")String restaurantName, @RequestParam(value = "cityName") String cityName){
        this.restaurantService.removeRestaurantFromCity(restaurantName, cityName);
    }


    @PostMapping("/add-product")
    public Response addProduct(@RequestParam(value = "photo")MultipartFile file, @RequestBody ProductDTO productDTO) throws IOException{
        try{
            this.restaurantService.addProduct(file, productDTO);
            return new Response("added with succes", HttpStatus.OK);
        }catch (IOException e){
            return new Response("Error on adding a product", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-products/{restaurantName}")
    public ResponseEntity<List<ProductDTO>> getProducts(@PathVariable String restaurantName){
        return new ResponseEntity<List<ProductDTO>>(this.restaurantService.getRestaurantProducts(restaurantName), HttpStatus.OK);
    }


}
