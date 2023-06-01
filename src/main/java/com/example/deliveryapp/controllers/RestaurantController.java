package com.example.deliveryapp.controllers;

import com.example.deliveryapp.DTOs.ProductDTO;
import com.example.deliveryapp.DTOs.RestaurantDTO;
import com.example.deliveryapp.constants.Response;
import com.example.deliveryapp.exceptions.InsertPictureException;
import com.example.deliveryapp.models.Image;
import com.example.deliveryapp.models.Product;
import com.example.deliveryapp.service.RestaurantService;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("delivery-app/restaurant")
@CrossOrigin()
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
    public Response addProduct(@RequestParam(value = "photo")MultipartFile file,
                               @RequestParam(value = "name") String name,
                               @RequestParam(value = "price") Double price,
                               @RequestParam(value = "type") String type,
                               @RequestParam(value = "description") String description,
                               @RequestParam(value = "ingredients") String ingredients,
                               @RequestParam(value = "restaurantName") String restaurantName) throws IOException {
        try{
            this.restaurantService.addProduct(file, name, price, type, description, ingredients, restaurantName);
            return new Response("added with succes", HttpStatus.OK);

        }catch (IOException e){
            return new Response("Error on adding a product", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/get-restaurant-products/{restaurantName}")
    public ResponseEntity<List<ProductDTO>> getRestaurantProducts(@PathVariable String restaurantName, @RequestParam(value = "type")String type){

//        List<ProductDTO> products = this.restaurantService.getRestaurantProducts(restaurantName, type);
//        ResponseEntity<List<ProductDTO>> response = new ResponseEntity<>()
//        for(ProductDTO product : products){
//
//            byte[] imageData = product.getPicture();
//            if (imageData != null && imageData.length > 0) {
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.IMAGE_PNG);
//                return new ResponseEntity<>(products, headers, HttpStatus.OK);
//            }
//        }
        return new ResponseEntity<List<ProductDTO>>(this.restaurantService.getRestaurantProducts(restaurantName, type), HttpStatus.OK);
    }

    @GetMapping("/get-product-photo")
    public ResponseEntity<?> getProductImage(@RequestParam(value = "restaurantName") String restaurantName, @RequestParam(value = "productName") String productName){
        byte [] image = this.restaurantService.getImageProduct(restaurantName, productName);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(image);
    }

    @GetMapping("/get-product-by-restaurant-and-product-Name/{restaurantName}")
    public ProductDTO getProductByRestaurantAndProductName(@PathVariable String restaurantName, @RequestParam(value = "productName")String productName){
        return this.restaurantService.getProductByName(restaurantName,productName);
    }



}
