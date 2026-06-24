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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
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

        Product hat = new Product(1, "hat", 121.0, 1, "sel hat", "Subssss", 50, true, null);
        Product shirt = new Product(2, "shirt", 131.0, 2, "sel shirt", "Subssss", 100, false, null);
        Product bag = new Product(3, "bag", 111.0, 3, "sel bag", "Subssss", 100, true, null);

        testProducts.add(hat);
        testProducts.add(shirt);
        testProducts.add(bag);

        when(productRepository.findAll()).thenReturn(testProducts);

        // Act
        List<Product> products = productService.search(null, null, null, null);

        // Assert
        assertEquals(3, products.size());
    }

    @Test
    public void update_shouldUpdateStock_whenStockIsChanged() {

        // arrange
        Product fakeOriginalProduct = new Product(1, "product1", 121.0, 1, "sel ppppp", "Subssss", 50, true, null);
        Product fakeNewProduct = new Product(1, "product1", 121.0, 1, "sel ppppp", "Subssss", 100, true, null);


        when(productRepository.findById(1)).thenReturn(Optional.of(fakeOriginalProduct));
        when(productRepository.save(fakeOriginalProduct)).thenReturn(fakeOriginalProduct);

        // act
        Product updateProduct = productService.update(1, fakeNewProduct);

        // assert
        assertEquals(100, updateProduct.getStock());
        verify(productRepository).save(fakeOriginalProduct);

    }
}