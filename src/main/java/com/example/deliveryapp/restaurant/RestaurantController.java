package com.example.deliveryapp.restaurant;

import com.example.deliveryapp.DTOs.ProductDTO;
import com.example.deliveryapp.DTOs.RestaurantDTO;
import com.example.deliveryapp.constants.Response;
import com.example.deliveryapp.image.Image;
import com.example.deliveryapp.image.ImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("delivery-app/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ImageRepo imageRepo;

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
    public Response addProduct(@RequestParam(value = "file")MultipartFile file,
                               @RequestParam(value = "name") String name,
                               @RequestParam(value = "price") Double price,
                               @RequestParam(value = "type") String type,
                               @RequestParam(value = "description") String description,
                               @RequestParam(value = "ingredients") String ingredients,
                               @RequestParam(value = "restaurantName") String restaurantName,
                               @RequestParam(value = "containsGluten") Boolean containsGluten,
                               @RequestParam(value = "containsLactose") Boolean containsLactose,
                               @RequestParam(value = "isVegetarian") Boolean isVegetarian

    ) throws IOException {
        try{
            this.restaurantService.addProduct(file, name, price, type, description, ingredients, restaurantName, containsGluten, containsLactose, isVegetarian);
            return new Response("added with succes", HttpStatus.OK);

        }catch (IOException e){
            return new Response("Error on adding a product", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/get-restaurant-products/{restaurantName}")
    public ResponseEntity<List<ProductDTO>> getRestaurantProducts(@PathVariable String restaurantName, @RequestParam(value = "type")String type){

        return new ResponseEntity<List<ProductDTO>>(this.restaurantService.getRestaurantProducts(restaurantName, type), HttpStatus.OK);
    }

    @GetMapping("/get-image-product/{imageId}")
    public ResponseEntity<byte[]> getImageById(@PathVariable long imageId){

        Optional<Image> optionalImage = imageRepo.findById(imageId);
        if (optionalImage.isPresent()) {
            Image image = optionalImage.get();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
                    .body(image.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get-product-by-restaurant-and-product-Name/{restaurantName}")
    public ProductDTO getProductByRestaurantAndProductName(@PathVariable String restaurantName, @RequestParam(value = "productName")String productName){
        return this.restaurantService.getProductByName(restaurantName,productName);
    }

    @PostMapping("/get-products-by-ingredients")
//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Set<ProductDTO> getProductsByIngredients(
            @RequestParam(value = "foodType") String foodType,
            @RequestParam(value = "ingredientList") String ingredientList){
        return this.restaurantService.getProductsByIngredients(foodType, ingredientList);
    }


}
