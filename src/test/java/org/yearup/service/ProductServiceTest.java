package org.yearup.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yearup.models.Product;
import org.yearup.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void search_shouldReturn_allProduct_whenNoFilterApplied() {

        // Arrange
        List<Product> testProducts = new ArrayList<>();

        Product hat = new Product();
        hat.setFeatured(true);
        testProducts.add(hat);

        Product shirt = new Product();
        shirt.setFeatured(false);
        testProducts.add(shirt);

        Product bag = new Product();
        bag.setFeatured(true);
        testProducts.add(bag);

        when(productRepository.findAll()).thenReturn(testProducts);

        // Act
        List<Product> products = productService.search(null, null, null, null);

        // Assert
        assertEquals(3, products.size());
    }
}