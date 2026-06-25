package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.CartItem;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;

//Tells Spring this is an API
@RestController
//Set base URL to /cart
@RequestMapping("/cart")
//Allows the website to talk to my backend
@CrossOrigin
public class ShoppingCartController
{
    private ShoppingCartService shoppingCartService;
    private UserService userService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @GetMapping
    //Only people with a login token cna see it.
    @PreAuthorize("isAuthenticated()")
    public ShoppingCart getCart(Principal principal)
    {
        //Used the Principle object to find the userId, so I know whose cart to get from the service.
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        return shoppingCartService.getByUserId(userId);
    }

    @PostMapping("/products/{productId}")
    @PreAuthorize("isAuthenticated()")                                //To get the productId  from the url
    public ResponseEntity<ShoppingCart> updatedCart(Principal principal, @PathVariable int productId) {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        ShoppingCart updated = shoppingCartService.updateCart(userId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    //Changes the quantity when the user wants to do it manually.
    @PutMapping("/products/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ShoppingCart updateExistingProduct(Principal principal, @PathVariable int productId, @RequestBody CartItem item) {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        return shoppingCartService.updateQuantity(userId, productId, item);
    }

    //Deletes the whole cart if the user hits clear.
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ShoppingCart deleteCartItem(Principal principal) {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        shoppingCartService.clearCart(userId);
        //Called getCart(principal) sp th uer heats back an empty cart.
        return getCart(principal);
    }
}
