package com.example.deliveryapp.service.impl;

import com.example.deliveryapp.DTOs.ProductDTO;
import com.example.deliveryapp.repos.ProductRepo;
import com.example.deliveryapp.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ModelMapper mapper;






}
