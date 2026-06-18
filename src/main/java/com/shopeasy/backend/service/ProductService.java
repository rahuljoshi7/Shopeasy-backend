package com.shopeasy.backend.service;

import com.shopeasy.backend.dto.ProductDto;
import com.shopeasy.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll(Sort.by("id"))
                .stream()
                .map(ProductDto::from)
                .toList();
    }

    public ProductDto getProduct(Long id) {
        return productRepository.findById(id)
                .map(ProductDto::from)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }
}
