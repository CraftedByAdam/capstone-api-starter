package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoriesController
{
    private CategoryService categoryService;
    private ProductService productService;

    public CategoriesController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    //Gets every category in the database so the website can show the list of departments to the user.
    @GetMapping
    @PreAuthorize("permitAll")
    public List<Category> getAll()
    {
        return categoryService.getAllCategories();
    }

    //Looks for one specific category using its ID.
    @GetMapping("/{id}")
    @PreAuthorize("permitAll")
    public Category getById(@PathVariable int id)
    {
        Category category = categoryService.getById(id);
        //If the ID doesn't exist, it throws a 404 Not Found Error.
         if (category == null) {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND);
         }
        return category;
    }

    //Finds all the products that belong to a specific category.
    @GetMapping("/{categoryId}/products")
    @PreAuthorize("permitAll")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        //I used the product service to look these up based on the categoryId from the url.
        return productService.listByCategoryId(categoryId);
    }

    //Lets an admin add a brand-new category.
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> addCategory(@RequestBody Category category)
    {
        //I used ResponseEntity so it returns a 201 Created status.
        Category saved = categoryService.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    //For when an admin needs to change a category info.
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Category updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        //Checks if the category exists first, then calls the service to save the new changes.
        if (categoryService.getById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return categoryService.update(id, category);
    }

    //Lets an admin remove a category.
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id)
    {
        //If it finds the category, it deletes it and sends back a 204 No Content status.
        if (categoryService.getById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
