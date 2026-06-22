package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

@Service
public class ShoppingCartService
{
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId)
    {
        ShoppingCart shoppingCart = new ShoppingCart();

        for (CartItem cartItem : shoppingCartRepository.findByUserId(userId)) {
            Product product = productService.getById(cartItem.getProductId());
            ShoppingCartItem item = new ShoppingCartItem();
            item.setProduct(product);
            item.setQuantity(cartItem.getQuantity());
            shoppingCart.add(item);
        }
        return shoppingCart;
    }

    public ShoppingCart updateCart(int userId, int productId) {
        CartItem item = shoppingCartRepository.findByUserIdAndProductId(userId, productId);
        if (item == null) {
            item = new CartItem();
            item.setUserId(userId);
            item.setProductId(productId);
            item.setQuantity(1);
        }else {
            item.setQuantity(item.getQuantity() + 1);
        }

        shoppingCartRepository.save(item);
        return getByUserId(userId);
    }

    public ShoppingCart updateQuantity(int userId, int productId, CartItem updateItem) {
        CartItem item = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        if (item != null) {
            item.setQuantity(updateItem.getQuantity());
            shoppingCartRepository.save(item);
        }

        return getByUserId(userId);
    }

    @Transactional
    public void clearCart(int userId) {
        shoppingCartRepository.deleteByUserId(userId);
    }
}
