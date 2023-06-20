package com.example.deliveryapp.courier;

import com.example.deliveryapp.DTOs.CourierDTO;
import com.example.deliveryapp.constants.Constants;
import com.example.deliveryapp.exceptions.DeliveryCustomException;
import com.example.deliveryapp.order.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierRepo courierRepo;
    @Autowired
    private ModelMapper mapper;


    @Override
    public void addCourier(CourierDTO courierDTO) {

        if(this.courierRepo.getCourierByPhone(courierDTO.getPhone()).isPresent()){
            throw new DeliveryCustomException(Constants.PHONE_COURIER_ALREADY_EXISTS_EXCEPTION.getMessage());
        }

        this.courierRepo.save(this.mapper.map(courierDTO, Courier.class));
    }

    @Override
    public List<CourierDTO> getCouriers() {

        List<Courier> couriers = this.courierRepo.findAll();
        List<CourierDTO> courierDTOs = new ArrayList<>();

        couriers.forEach(courier -> {

            List<Order> orders = courier
                    .getOrders()
                    .stream()
                    .filter(order -> order.getOrderInDelivery() != null && order.getDeliveredTime() == null)
                    .collect(Collectors.toList());

            courierDTOs.add(
                    CourierDTO.builder()
                            .id(courier.getId())
                            .fullName(courier.getFullName())
                            .phone(courier.getPhone())
                            .ordersAssigned(orders.size())
                            .vehicleType(courier.getVehicleType())
                            .build());
        });
        return courierDTOs;
    }

    @Override
    public CourierDTO getCourierById(Long id){

        Courier courier = this.courierRepo.findById(id)
                .orElseThrow(() -> new DeliveryCustomException("Courier not found by id"));

        return CourierDTO.builder()
                .id(courier.getId())
                .fullName(courier.getFullName())
                .phone(courier.getPhone())
                .vehicleType(courier.getVehicleType())
                .build();
    }
}
